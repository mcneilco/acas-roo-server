-- Minimal schema (only columns the retention SQL touches)
CREATE SEQUENCE value_pkseq;
CREATE TABLE protocol (id bigint primary key, ignored boolean, deleted boolean);
CREATE TABLE protocol_state (id bigint primary key, protocol_id bigint, ignored boolean, deleted boolean);
CREATE TABLE protocol_value (id bigint primary key, protocol_state_id bigint, ls_type_and_kind text, numeric_value numeric, ignored boolean, deleted boolean);
CREATE TABLE experiment (id bigint primary key, code_name text, protocol_id bigint);
CREATE TABLE experiment_state (id bigint primary key, experiment_id bigint, ls_type_and_kind text, ls_type text, ls_kind text, ignored boolean, deleted boolean);
CREATE TABLE experiment_value (id bigint primary key, experiment_state_id bigint, ls_type_and_kind text, code_value text, recorded_date timestamp, ignored boolean, deleted boolean);
CREATE TABLE experiment_label (id bigint primary key, experiment_id bigint);
CREATE TABLE experiment_analysisgroup (experiment_id bigint, analysis_group_id bigint);
CREATE TABLE analysis_group (id bigint primary key);
CREATE TABLE analysis_group_state (id bigint primary key, analysis_group_id bigint);
CREATE TABLE analysis_group_value (id bigint primary key, analysis_state_id bigint);
CREATE TABLE analysisgroup_treatmentgroup (analysis_group_id bigint, treatment_group_id bigint);
CREATE TABLE treatment_group (id bigint primary key);
CREATE TABLE treatment_group_state (id bigint primary key, treatment_group_id bigint);
CREATE TABLE treatment_group_value (id bigint primary key, treatment_state_id bigint);
CREATE TABLE treatmentgroup_subject (treatment_group_id bigint, subject_id bigint);
CREATE TABLE subject (id bigint primary key);
CREATE TABLE subject_state (id bigint primary key, subject_id bigint);
CREATE TABLE subject_value (id bigint primary key, subject_state_id bigint);

-- Fixtures: one protocol with 7-day retention
INSERT INTO protocol VALUES (1,false,false);
INSERT INTO protocol_state VALUES (1,1,false,false);
INSERT INTO protocol_value VALUES (1,1,'numericValue_deleted experiment retention days',7,false,false);

-- E_expired: deleted 30 days ago -> should be purged
INSERT INTO experiment VALUES (1,'EXPT-EXPIRED',1);
INSERT INTO experiment_state VALUES (1,1,'metadata_experiment metadata','metadata','experiment metadata',false,false);
INSERT INTO experiment_value VALUES (1,1,'codeValue_experiment status','deleted', now()-interval '30 days',false,false);
INSERT INTO experiment_label VALUES (1,1);

-- E_live: approved -> must be left alone
INSERT INTO experiment VALUES (2,'EXPT-LIVE',1);
INSERT INTO experiment_state VALUES (2,2,'metadata_experiment metadata','metadata','experiment metadata',false,false);
INSERT INTO experiment_value VALUES (2,2,'codeValue_experiment status','approved', now()-interval '30 days',false,false);

-- AG1: belongs only to E_expired -> should be deleted
INSERT INTO analysis_group VALUES (1);
INSERT INTO analysis_group_state VALUES (1,1);
INSERT INTO analysis_group_value VALUES (1,1);
INSERT INTO experiment_analysisgroup VALUES (1,1);

-- AG_shared: belongs to BOTH E_expired and E_live -> shared-entity guard must PRESERVE it
INSERT INTO analysis_group VALUES (2);
INSERT INTO analysis_group_state VALUES (2,2);
INSERT INTO analysis_group_value VALUES (2,2);
INSERT INTO experiment_analysisgroup VALUES (1,2);
INSERT INTO experiment_analysisgroup VALUES (2,2);

-- ===== staging (verbatim from ExperimentRetentionServiceImpl.stageExpiredExperiments) =====
CREATE TABLE retention_work_expired_experiments AS
SELECT DISTINCT e.id AS experiment_id, e.code_name AS experiment_code
FROM protocol p
JOIN protocol_state ps ON p.id = ps.protocol_id AND ps.ignored = false AND ps.deleted = false
JOIN protocol_value pv ON ps.id = pv.protocol_state_id AND pv.ls_type_and_kind = 'numericValue_deleted experiment retention days' AND pv.ignored = false AND pv.deleted = false
JOIN experiment e ON e.protocol_id = p.id
JOIN experiment_state es ON e.id = es.experiment_id AND es.ls_type_and_kind = 'metadata_experiment metadata' AND es.ignored = false AND es.deleted = false
JOIN experiment_value ev ON es.id = ev.experiment_state_id AND ev.ls_type_and_kind = 'codeValue_experiment status' AND ev.code_value IN ('deleted','overwritten') AND ev.ignored = false AND ev.deleted = false
WHERE p.ignored = false AND p.deleted = false AND ev.recorded_date < NOW() - INTERVAL '1 day' * pv.numeric_value;

CREATE TABLE retention_work_expired_states AS
SELECT es.id AS experiment_state_id FROM experiment_state es JOIN retention_work_expired_experiments w ON es.experiment_id = w.experiment_id;

CREATE TABLE retention_work_orphan_ags AS
SELECT DISTINCT ag.id AS analysis_group_id FROM analysis_group ag
JOIN experiment_analysisgroup eag ON ag.id = eag.analysis_group_id
JOIN retention_work_expired_experiments w ON eag.experiment_id = w.experiment_id
WHERE NOT EXISTS (SELECT 1 FROM experiment_analysisgroup eag2 WHERE eag2.analysis_group_id = ag.id AND eag2.experiment_id NOT IN (SELECT experiment_id FROM retention_work_expired_experiments));

CREATE TABLE retention_work_orphan_tgs AS
SELECT DISTINCT tg.id AS treatment_group_id FROM treatment_group tg
JOIN analysisgroup_treatmentgroup agtg ON tg.id = agtg.treatment_group_id
JOIN retention_work_orphan_ags oa ON agtg.analysis_group_id = oa.analysis_group_id
WHERE NOT EXISTS (SELECT 1 FROM analysisgroup_treatmentgroup agtg2 WHERE agtg2.treatment_group_id = tg.id AND agtg2.analysis_group_id NOT IN (SELECT analysis_group_id FROM retention_work_orphan_ags));

CREATE TABLE retention_work_orphan_subjects AS
SELECT DISTINCT s.id AS subject_id FROM subject s
JOIN treatmentgroup_subject tgs ON s.id = tgs.subject_id
JOIN retention_work_orphan_tgs ot ON tgs.treatment_group_id = ot.treatment_group_id
WHERE NOT EXISTS (SELECT 1 FROM treatmentgroup_subject tgs2 WHERE tgs2.subject_id = s.id AND tgs2.treatment_group_id NOT IN (SELECT treatment_group_id FROM retention_work_orphan_tgs));

CREATE TABLE retention_work_ag_states AS SELECT ags.id AS analysis_group_state_id FROM analysis_group_state ags JOIN retention_work_orphan_ags oa ON ags.analysis_group_id = oa.analysis_group_id;

-- ===== assertions on staging =====
DO $$ BEGIN
  IF (SELECT count(*) FROM retention_work_expired_experiments) <> 1 THEN RAISE EXCEPTION 'expected 1 expired experiment, got %', (SELECT count(*) FROM retention_work_expired_experiments); END IF;
  IF NOT EXISTS (SELECT 1 FROM retention_work_expired_experiments WHERE experiment_code='EXPT-EXPIRED') THEN RAISE EXCEPTION 'EXPT-EXPIRED not staged'; END IF;
  IF EXISTS (SELECT 1 FROM retention_work_expired_experiments WHERE experiment_code='EXPT-LIVE') THEN RAISE EXCEPTION 'EXPT-LIVE wrongly staged'; END IF;
  IF NOT EXISTS (SELECT 1 FROM retention_work_orphan_ags WHERE analysis_group_id=1) THEN RAISE EXCEPTION 'AG1 should be an orphan'; END IF;
  IF EXISTS (SELECT 1 FROM retention_work_orphan_ags WHERE analysis_group_id=2) THEN RAISE EXCEPTION 'SHARED AG2 wrongly marked orphan (shared-entity guard failed!)'; END IF;
  RAISE NOTICE 'STAGING ASSERTIONS PASSED';
END $$;

-- ===== deletes (join-to-work-table form, matching RetentionBatchDeleter joins) =====
DELETE FROM analysis_group_value t USING retention_work_ag_states w WHERE t.analysis_state_id = w.analysis_group_state_id;
DELETE FROM analysis_group_state t USING retention_work_orphan_ags w WHERE t.analysis_group_id = w.analysis_group_id;
DELETE FROM analysisgroup_treatmentgroup t USING retention_work_orphan_ags w WHERE t.analysis_group_id = w.analysis_group_id;
DELETE FROM experiment_analysisgroup t USING retention_work_expired_experiments w WHERE t.experiment_id = w.experiment_id;
DELETE FROM analysis_group t USING retention_work_orphan_ags w WHERE t.id = w.analysis_group_id;
DELETE FROM experiment_value t USING retention_work_expired_states w WHERE t.experiment_state_id = w.experiment_state_id;
DELETE FROM experiment_label t USING retention_work_expired_experiments w WHERE t.experiment_id = w.experiment_id;
DELETE FROM experiment_state t USING retention_work_expired_experiments w WHERE t.experiment_id = w.experiment_id;
DELETE FROM experiment t USING retention_work_expired_experiments w WHERE t.id = w.experiment_id;

-- ===== assertions on results =====
DO $$ BEGIN
  IF EXISTS (SELECT 1 FROM experiment WHERE code_name='EXPT-EXPIRED') THEN RAISE EXCEPTION 'EXPT-EXPIRED not deleted'; END IF;
  IF NOT EXISTS (SELECT 1 FROM experiment WHERE code_name='EXPT-LIVE') THEN RAISE EXCEPTION 'EXPT-LIVE wrongly deleted!'; END IF;
  IF EXISTS (SELECT 1 FROM analysis_group WHERE id=1) THEN RAISE EXCEPTION 'AG1 not deleted'; END IF;
  IF EXISTS (SELECT 1 FROM analysis_group_value WHERE id=1) THEN RAISE EXCEPTION 'AG1 value not deleted'; END IF;
  IF NOT EXISTS (SELECT 1 FROM analysis_group WHERE id=2) THEN RAISE EXCEPTION 'SHARED AG2 wrongly deleted (live experiment data lost!)'; END IF;
  IF NOT EXISTS (SELECT 1 FROM analysis_group_value WHERE id=2) THEN RAISE EXCEPTION 'SHARED AG2 value wrongly deleted!'; END IF;
  IF EXISTS (SELECT 1 FROM experiment_label WHERE experiment_id=1) THEN RAISE EXCEPTION 'expired label not deleted'; END IF;
  RAISE NOTICE 'RESULT ASSERTIONS PASSED — expired purged, live + shared AG preserved';
END $$;
