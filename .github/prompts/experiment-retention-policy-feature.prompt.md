---
mode: agent
---
Summary

This SQL sets up and implments a policy to permanently delete experiments and all their related data from the database after they have been marked as deleted or overwritten for protocols configured with a protocol value for deleted experiment retention days. It tracks retention periods, identifies expired experiments, deletes all associated records in the correct order, and logs when each experiments are hard deleted.

TODO The deletion of associated experiment files has not been described in this document yet.  This will likely be implemented as a modified version of this script 
Hard delete a list of experiments in ACAS | 5. Delete files from the filestore using the same deleted experiment retention days protocol configuration and using the new files deleted date value kind to track those experiments which have had their backing files deleted.
SQL

Insert required value kinds

deleted experiment retention days (numericValue_deleted experiment retention days):
This kind is used to store, for each protocol, the number of days an experiment should be retained after being marked as deleted or overwritten.
It is a numeric value and is referenced when determining which experiments are expired and eligible for hard deletion.
database deleted date (dateValue_database deleted date):
This kind is used to record the date when an experiment is hard deleted from the database.
When an experiment is hard deleted, a new experiment_value row of this kind is inserted to mark the deletion event.
files deleted date (dateValue_files deleted date):
This kind is used to record the date when associated files for a hard-deleted experiment are deleted from storage.
It allows you to track which experiments have had their files removed, separate from the database deletion.



INSERT INTO value_kind (id, kind_name, ls_type_and_kind, version, ls_type)
VALUES (
    nextval('value_kind_pkseq'),
    'deleted experiment retention days',
    'numericValue_deleted experiment retention days',
    0,
    (SELECT id FROM value_type WHERE type_name = 'numericValue')
),
(
    nextval('value_kind_pkseq'),
    'database deleted date',
    'dateValue_database deleted date',
    0,
    (SELECT id FROM value_type WHERE type_name = 'dateValue')
),
(
    nextval('value_kind_pkseq'),
    'files deleted date',
    'dateValue_files deleted date',
    0,
    (SELECT id FROM value_type WHERE type_name = 'dateValue')
)
ON CONFLICT (ls_type_and_kind) DO NOTHING;
 
Annotate a protocol with a retention policy 

Creates a new transaction record in the ls_transaction table, with a comment indicating the action ("deleted experiment retention days"), the current timestamp, and version 0. The new transaction's id is captured as new_txn.id.
Inserts a new protocol_value 7 day “deleted experiment retention days” row the protocol that:
Has a label "Vial Inventory" (and is not ignored or deleted).
Does not already have a protocol_value of kind “deleted experiment retention days” for that protocol_state (the NOT EXISTS clause).



WITH new_txn AS (
    INSERT INTO ls_transaction (id, comments, recorded_date, version)
    VALUES (nextval('value_pkseq'), 'deleted experiment retention days', now(), 0)
    RETURNING id
)
INSERT INTO protocol_value
    (id, ls_type, ls_kind, ls_type_and_kind, numeric_value, ls_transaction, recorded_by, recorded_date, version, protocol_state_id, deleted, ignored, public_data)
SELECT
    nextval('value_pkseq'), 'numericValue', 'deleted experiment retention days', 7, new_txn.id, 'bbolt', now(), 0, ps.id, false, false, false
FROM
    protocol p
    JOIN protocol_label pl ON p.id = pl.protocol_id
    JOIN protocol_state ps ON p.id = ps.protocol_id
    CROSS JOIN new_txn nt
WHERE
    pl.label_text = 'Vial Inventory'
    AND pl.ignored = false
    AND pl.deleted = false
    AND pl.ls_type = 'name'
    AND pl.ls_kind = 'protocol name'
    AND p.ignored = false
    AND p.deleted = false
    AND ps.ignored = false
    AND ps.deleted = false
    AND ps.ls_type = 'metadata'
    AND ps.ls_kind = 'protocol metadata'
    AND NOT EXISTS (
        SELECT 1 FROM protocol_value pv
        WHERE pv.protocol_state_id = ps.id
          AND pv.ls_kind = 'deleted experiment retention days'
          AND pv.ignored = false
          AND pv.deleted = false
    );
Delete experiment data

Identifies expired experiments for deletion based on protocol retention policy and experiment status (“overwritten” or “deleted”), excluding those which already have a “database deleted date”
Deletes data in the correct order: value tables, state tables, linker tables, then main tables, to satisfy foreign key constraints.
Only main table deletes (subject, treatment_group, analysis_group) use the orphaned temp tables to ensure only unreferenced rows are deleted.
Inserts a new experiment_value row for each deleted experiment to record the "database deleted date,".
All operations are wrapped in a transaction for atomicity and safety.



BEGIN;
CREATE TEMP TABLE temp_expired_experiments AS
SELECT
    e.id AS experiment_id,
    e.code_name AS experiment_code,
    p.code_name AS protocol_code,
    pv.numeric_value AS retention_days,
    ev.code_value,
    ev.recorded_date AS status_recorded_date,
    ev.id as statud_value_id,
    ev.recorded_date + INTERVAL '1 day' * pv.numeric_value AS expiration_date
FROM
    protocol p
    JOIN protocol_state ps ON p.id = ps.protocol_id
        AND ps.ignored = false
        AND ps.deleted = false
    JOIN protocol_value pv ON ps.id = pv.protocol_state_id
        AND pv.ls_type_and_kind = 'numericValue_deleted experiment retention days'
        AND pv.ignored = false
        AND pv.deleted = false
    JOIN experiment e ON e.protocol_id = p.id
    JOIN experiment_state es ON e.id = es.experiment_id
        AND es.ls_type_and_kind = 'metadata_experiment metadata'
        AND es.ignored = false
        AND es.deleted = false
    JOIN experiment_value ev ON es.id = ev.experiment_state_id
        AND ev.ls_type_and_kind = 'codeValue_experiment status'
        AND ev.code_value IN ('deleted', 'overwritten')
        AND ev.ignored = false
        AND ev.deleted = false
WHERE
    p.ignored = false
    AND p.deleted = false
    AND ev.recorded_date < NOW() - INTERVAL '1 day' * pv.numeric_value
    -- Only select those that have not already been hard deleted
    AND NOT EXISTS (
        SELECT 1 FROM experiment_value ev2
        WHERE ev2.experiment_state_id = es.id
          AND ev2.ls_type_and_kind = 'dateValue_database deleted date'
          AND ev2.ignored = false
          AND ev2.deleted = false
    )
-- Capture orphaned analysis_group ids before deleting experiment_analysisgroup
CREATE TEMP TABLE temp_orphaned_analysis_groups AS
SELECT DISTINCT ag.id AS analysis_group_id
FROM analysis_group ag
JOIN experiment_analysisgroup eag ON ag.id = eag.analysis_group_id
JOIN experiment e ON eag.experiment_id = e.id
JOIN temp_expired_experiments tee ON e.code_name = tee.experiment_code;
-- Capture orphaned treatment_group ids before deleting analysisgroup_treatmentgroup
CREATE TEMP TABLE temp_orphaned_treatment_groups AS
SELECT DISTINCT tg.id AS treatment_group_id
FROM treatment_group tg
JOIN analysisgroup_treatmentgroup agtg ON tg.id = agtg.treatment_group_id
JOIN analysis_group ag ON agtg.analysis_group_id = ag.id
JOIN temp_orphaned_analysis_groups oa ON ag.id = oa.analysis_group_id;
-- Capture orphaned subject ids before deleting treatmentgroup_subject
CREATE TEMP TABLE temp_orphaned_subjects AS
SELECT DISTINCT s.id AS subject_id
FROM subject s
JOIN treatmentgroup_subject tgs ON s.id = tgs.subject_id
JOIN treatment_group tg ON tgs.treatment_group_id = tg.id
JOIN temp_orphaned_treatment_groups otg ON tg.id = otg.treatment_group_id;
-- Now perform deletes in correct order
-- 1. Value tables
DELETE FROM subject_value
USING subject_state, subject, treatmentgroup_subject, treatment_group, analysisgroup_treatmentgroup, analysis_group, experiment_analysisgroup, experiment, temp_expired_experiments
WHERE subject_value.subject_state_id = subject_state.id
  AND subject_state.subject_id = subject.id
  AND subject.id = treatmentgroup_subject.subject_id
  AND treatmentgroup_subject.treatment_group_id = treatment_group.id
  AND treatment_group.id = analysisgroup_treatmentgroup.treatment_group_id
  AND analysisgroup_treatmentgroup.analysis_group_id = analysis_group.id
  AND analysis_group.id = experiment_analysisgroup.analysis_group_id
  AND experiment_analysisgroup.experiment_id = experiment.id
  AND experiment.code_name = temp_expired_experiments.experiment_code;
DELETE FROM treatment_group_value
USING treatment_group_state, treatment_group, analysisgroup_treatmentgroup, analysis_group, experiment_analysisgroup, experiment, temp_expired_experiments
WHERE treatment_group_value.treatment_state_id = treatment_group_state.id
  AND treatment_group_state.treatment_group_id = treatment_group.id
  AND treatment_group.id = analysisgroup_treatmentgroup.treatment_group_id
  AND analysisgroup_treatmentgroup.analysis_group_id = analysis_group.id
  AND analysis_group.id = experiment_analysisgroup.analysis_group_id
  AND experiment_analysisgroup.experiment_id = experiment.id
  AND experiment.code_name = temp_expired_experiments.experiment_code;
DELETE FROM analysis_group_value
USING analysis_group_state, analysis_group, experiment_analysisgroup, experiment, temp_expired_experiments
WHERE analysis_group_value.analysis_state_id = analysis_group_state.id
  AND analysis_group_state.analysis_group_id = analysis_group.id
  AND analysis_group.id = experiment_analysisgroup.analysis_group_id
  AND experiment_analysisgroup.experiment_id = experiment.id
  AND experiment.code_name = temp_expired_experiments.experiment_code;
-- 2. State tables
DELETE FROM subject_state
USING subject, treatmentgroup_subject, treatment_group, analysisgroup_treatmentgroup, analysis_group, experiment_analysisgroup, experiment, temp_expired_experiments
WHERE subject_state.subject_id = subject.id
  AND subject.id = treatmentgroup_subject.subject_id
  AND treatmentgroup_subject.treatment_group_id = treatment_group.id
  AND treatment_group.id = analysisgroup_treatmentgroup.treatment_group_id
  AND analysisgroup_treatmentgroup.analysis_group_id = analysis_group.id
  AND analysis_group.id = experiment_analysisgroup.analysis_group_id
  AND experiment_analysisgroup.experiment_id = experiment.id
  AND experiment.code_name = temp_expired_experiments.experiment_code;
DELETE FROM treatment_group_state
USING treatment_group, analysisgroup_treatmentgroup, analysis_group, experiment_analysisgroup, experiment, temp_expired_experiments
WHERE treatment_group_state.treatment_group_id = treatment_group.id
  AND treatment_group.id = analysisgroup_treatmentgroup.treatment_group_id
  AND analysisgroup_treatmentgroup.analysis_group_id = analysis_group.id
  AND analysis_group.id = experiment_analysisgroup.analysis_group_id
  AND experiment_analysisgroup.experiment_id = experiment.id
  AND experiment.code_name = temp_expired_experiments.experiment_code;
DELETE FROM analysis_group_state
USING analysis_group, experiment_analysisgroup, experiment, temp_expired_experiments
WHERE analysis_group_state.analysis_group_id = analysis_group.id
  AND analysis_group.id = experiment_analysisgroup.analysis_group_id
  AND experiment_analysisgroup.experiment_id = experiment.id
  AND experiment.code_name = temp_expired_experiments.experiment_code;
-- 3. Linker tables
DELETE FROM treatmentgroup_subject
USING treatment_group, temp_orphaned_treatment_groups
WHERE treatmentgroup_subject.treatment_group_id = treatment_group.id
  AND treatment_group.id = temp_orphaned_treatment_groups.treatment_group_id;
DELETE FROM analysisgroup_treatmentgroup
USING analysis_group, temp_orphaned_analysis_groups
WHERE analysisgroup_treatmentgroup.analysis_group_id = analysis_group.id
  AND analysis_group.id = temp_orphaned_analysis_groups.analysis_group_id;
DELETE FROM experiment_analysisgroup
USING experiment, temp_expired_experiments
WHERE experiment_analysisgroup.experiment_id = experiment.id
  AND experiment.code_name = temp_expired_experiments.experiment_code;
-- 4. Main tables (only these use orphaned temp tables)
DELETE FROM subject
USING temp_orphaned_subjects
WHERE subject.id = temp_orphaned_subjects.subject_id;
DELETE FROM treatment_group
USING temp_orphaned_treatment_groups
WHERE treatment_group.id = temp_orphaned_treatment_groups.treatment_group_id;
DELETE FROM analysis_group
USING temp_orphaned_analysis_groups
WHERE analysis_group.id = temp_orphaned_analysis_groups.analysis_group_id;
-- Insert a new experiment_value for hard deleted date
WITH new_txn AS (
    INSERT INTO ls_transaction (id, comments, recorded_date, version)
    VALUES (nextval('value_pkseq'), 'database hard delete', now(), 0)
    RETURNING id
)
INSERT INTO experiment_value
    (id, ls_type, ls_kind, ls_type_and_kind, date_value, ls_transaction, recorded_by, recorded_date, version, experiment_state_id, deleted, ignored, public_data)
SELECT
    nextval('value_pkseq'),
    'dateValue',
    'database deleted date',
    'dateValue_database deleted date',
    now(),
    new_txn.id,
    'bbolt',
    now(),
    0,
    es.id,
    false,
    false,
    false
FROM experiment_state es
JOIN experiment e ON es.experiment_id = e.id
JOIN temp_expired_experiments tee ON e.code_name = tee.experiment_code
CROSS JOIN new_txn
WHERE es.ignored = false
  AND es.deleted = false
  AND es.ls_type = 'metadata'
  AND es.ls_kind = 'experiment metadata';
COMMIT;
