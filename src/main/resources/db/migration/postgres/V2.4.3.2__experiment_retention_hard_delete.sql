-- ACAS-ROO: Hard delete expired experiments and all related data
-- Implements experiment retention policy: identifies expired experiments, deletes all associated data, and logs hard deletes

DO $$
DECLARE
    -- No variables needed, all logic in SQL
BEGIN
    -- 1. Identify expired experiments
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
        );

    -- 2. Capture orphaned analysis_group ids before deleting experiment_analysisgroup
    CREATE TEMP TABLE temp_orphaned_analysis_groups AS
    SELECT DISTINCT ag.id AS analysis_group_id
    FROM analysis_group ag
    JOIN experiment_analysisgroup eag ON ag.id = eag.analysis_group_id
    JOIN experiment e ON eag.experiment_id = e.id
    JOIN temp_expired_experiments tee ON e.code_name = tee.experiment_code;

    -- 3. Capture orphaned treatment_group ids before deleting analysisgroup_treatmentgroup
    CREATE TEMP TABLE temp_orphaned_treatment_groups AS
    SELECT DISTINCT tg.id AS treatment_group_id
    FROM treatment_group tg
    JOIN analysisgroup_treatmentgroup agtg ON tg.id = agtg.treatment_group_id
    JOIN analysis_group ag ON agtg.analysis_group_id = ag.id
    JOIN temp_orphaned_analysis_groups oa ON ag.id = oa.analysis_group_id;

    -- 4. Capture orphaned subject ids before deleting treatmentgroup_subject
    CREATE TEMP TABLE temp_orphaned_subjects AS
    SELECT DISTINCT s.id AS subject_id
    FROM subject s
    JOIN treatmentgroup_subject tgs ON s.id = tgs.subject_id
    JOIN treatment_group tg ON tgs.treatment_group_id = tg.id
    JOIN temp_orphaned_treatment_groups otg ON tg.id = otg.treatment_group_id;

    -- 5. Delete value tables
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

    -- 6. Delete state tables
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

    -- 7. Delete linker tables
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

    -- 8. Delete main tables (only these use orphaned temp tables)
    DELETE FROM subject
    USING temp_orphaned_subjects
    WHERE subject.id = temp_orphaned_subjects.subject_id;

    DELETE FROM treatment_group
    USING temp_orphaned_treatment_groups
    WHERE treatment_group.id = temp_orphaned_treatment_groups.treatment_group_id;

    DELETE FROM analysis_group
    USING temp_orphaned_analysis_groups
    WHERE analysis_group.id = temp_orphaned_analysis_groups.analysis_group_id;

    -- 9. Insert a new experiment_value for hard deleted date
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
END $$;
