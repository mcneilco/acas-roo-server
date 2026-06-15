package com.labsynch.labseer.service;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExperimentRetentionServiceImpl implements ExperimentRetentionService {

	private static final Logger logger = LoggerFactory.getLogger(ExperimentRetentionServiceImpl.class);
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<String> getExperimentsAwaitingFilesDeletion() {
        String query = "SELECT DISTINCT e.code_name " +
            "FROM experiment e " +
            "JOIN experiment_state es ON e.id = es.experiment_id AND es.ls_type = 'metadata' AND es.ls_kind = 'experiment metadata' AND es.ignored = false AND es.deleted = false " +
            "WHERE EXISTS (" +
            "    SELECT 1 FROM experiment_value ev1 " +
            "    WHERE ev1.experiment_state_id = es.id " +
            "    AND ev1.ls_type_and_kind = 'dateValue_database deleted date' " +
            "    AND ev1.ignored = false AND ev1.deleted = false" +
            ") " +
            "AND NOT EXISTS (" +
            "    SELECT 1 FROM experiment_value ev2 " +
            "    WHERE ev2.experiment_state_id = es.id " +
            "    AND ev2.ls_type_and_kind = 'dateValue_files deleted date' " +
            "    AND ev2.ignored = false AND ev2.deleted = false" +
            ") " +
            "ORDER BY e.code_name";

        @SuppressWarnings("unchecked")
        List<String> experimentCodes = entityManager.createNativeQuery(query).getResultList();
        
        logger.info("Found {} experiments awaiting files deletion: {}", experimentCodes.size(), experimentCodes);
        return experimentCodes;
    }

    @Override
    @Transactional
    public List<String> completeExperimentDeletion() {
        // 1. Drop and create temp table for experiments ready for final deletion
        entityManager.createNativeQuery("DROP TABLE IF EXISTS temp_complete_deletion_experiments").executeUpdate();
        String createTempComplete = "CREATE TEMP TABLE temp_complete_deletion_experiments AS\n" +
            "SELECT e.id AS experiment_id, e.code_name AS experiment_code\n" +
            "FROM experiment e\n" +
            "JOIN experiment_state es ON e.id = es.experiment_id AND es.ls_type = 'metadata' AND es.ls_kind = 'experiment metadata' AND es.ignored = false AND es.deleted = false\n" +
            "WHERE EXISTS (\n" +
            "    SELECT 1 FROM experiment_value ev1\n" +
            "    WHERE ev1.experiment_state_id = es.id\n" +
            "    AND ev1.ls_type_and_kind = 'dateValue_database deleted date'\n" +
            "    AND ev1.ignored = false AND ev1.deleted = false\n" +
            ")\n" +
            "AND EXISTS (\n" +
            "    SELECT 1 FROM experiment_value ev2\n" +
            "    WHERE ev2.experiment_state_id = es.id\n" +
            "    AND ev2.ls_type_and_kind = 'dateValue_files deleted date'\n" +
            "    AND ev2.ignored = false AND ev2.deleted = false\n" +
            ")";
        entityManager.createNativeQuery(createTempComplete).executeUpdate();

        // Get codes to return
        @SuppressWarnings("unchecked")
        List<String> completeExperimentCodes = entityManager.createNativeQuery("SELECT experiment_code FROM temp_complete_deletion_experiments").getResultList();
        if (completeExperimentCodes.isEmpty()) {
            logger.info("No experiments found ready for complete deletion.");
            return Collections.emptyList();
        }

        Function<Integer, String> rows = n -> n + (n == 1 ? " row" : " rows");

        // 2. Delete experiment values
        int experimentValueRows = entityManager.createNativeQuery(
            "DELETE FROM experiment_value " +
            "WHERE experiment_state_id IN (" +
            "    SELECT es.id FROM experiment_state es " +
            "    JOIN temp_complete_deletion_experiments tcd ON es.experiment_id = tcd.experiment_id" +
            ")"
        ).executeUpdate();
        logger.info("Deleted {} from experiment_value", rows.apply(experimentValueRows));

        // 3. Delete experiment states
        int experimentStateRows = entityManager.createNativeQuery(
            "DELETE FROM experiment_state " +
            "WHERE experiment_id IN (SELECT experiment_id FROM temp_complete_deletion_experiments)"
        ).executeUpdate();
        logger.info("Deleted {} from experiment_state", rows.apply(experimentStateRows));

        // 4. Delete experiment records
        int experimentRows = entityManager.createNativeQuery(
            "DELETE FROM experiment " +
            "WHERE id IN (SELECT experiment_id FROM temp_complete_deletion_experiments)"
        ).executeUpdate();
        logger.info("Deleted {} from experiment", rows.apply(experimentRows));

        logger.info("Completed full deletion of {} experiments: {}", completeExperimentCodes.size(), completeExperimentCodes);
        return completeExperimentCodes;
    }

    @Override
    @Transactional
    public List<String> hardDeleteExpiredExperiments() {
        // 1. Drop and create temp table for expired experiments
        entityManager.createNativeQuery("DROP TABLE IF EXISTS temp_expired_experiments").executeUpdate();
        String createTempExpired = "CREATE TEMP TABLE temp_expired_experiments AS\n" +
            "SELECT e.id AS experiment_id, e.code_name AS experiment_code, p.code_name AS protocol_code, pv.numeric_value AS retention_days, ev.code_value, ev.recorded_date AS status_recorded_date, ev.id as statud_value_id, ev.recorded_date + INTERVAL '1 day' * pv.numeric_value AS expiration_date\n" +
            "FROM protocol p\n" +
            "JOIN protocol_state ps ON p.id = ps.protocol_id AND ps.ignored = false AND ps.deleted = false\n" +
            "JOIN protocol_value pv ON ps.id = pv.protocol_state_id AND pv.ls_type = 'numericValue' AND pv.ls_kind = 'deleted experiment retention days' AND pv.ignored = false AND pv.deleted = false\n" +
            "JOIN experiment e ON e.protocol_id = p.id\n" +
            "JOIN experiment_state es ON e.id = es.experiment_id AND es.ls_type_and_kind = 'metadata_experiment metadata' AND es.ignored = false AND es.deleted = false\n" +
            "JOIN experiment_value ev ON es.id = ev.experiment_state_id AND ev.ls_type_and_kind = 'codeValue_experiment status' AND ev.code_value IN ('deleted', 'overwritten') AND ev.ignored = false AND ev.deleted = false\n" +
            "WHERE p.ignored = false AND p.deleted = false AND ev.recorded_date < NOW() - INTERVAL '1 day' * pv.numeric_value\n" +
            "AND NOT EXISTS (SELECT 1 FROM experiment_value ev2 WHERE ev2.experiment_state_id = es.id AND ev2.ls_type_and_kind = 'dateValue_database deleted date' AND ev2.ignored = false AND ev2.deleted = false)";
        entityManager.createNativeQuery(createTempExpired).executeUpdate();

        // Get codes to return
        @SuppressWarnings("unchecked")
        List<String> expiredExperimentCodes = entityManager.createNativeQuery("SELECT experiment_code FROM temp_expired_experiments").getResultList();
        if (expiredExperimentCodes.isEmpty()) {
            logger.info("No expired experiments found for hard deletion.");
            return Collections.emptyList();
        }

        Function<Integer, String> rows = n -> n + (n == 1 ? " row" : " rows");

        // 2. Drop and create temp tables for orphans
        entityManager.createNativeQuery("DROP TABLE IF EXISTS temp_orphaned_analysis_groups").executeUpdate();
        entityManager.createNativeQuery("CREATE TEMP TABLE temp_orphaned_analysis_groups AS\nSELECT DISTINCT ag.id AS analysis_group_id FROM analysis_group ag JOIN experiment_analysisgroup eag ON ag.id = eag.analysis_group_id JOIN experiment e ON eag.experiment_id = e.id JOIN temp_expired_experiments tee ON e.code_name = tee.experiment_code").executeUpdate();
        entityManager.createNativeQuery("DROP TABLE IF EXISTS temp_orphaned_treatment_groups").executeUpdate();
        entityManager.createNativeQuery("CREATE TEMP TABLE temp_orphaned_treatment_groups AS\nSELECT DISTINCT tg.id AS treatment_group_id FROM treatment_group tg JOIN analysisgroup_treatmentgroup agtg ON tg.id = agtg.treatment_group_id JOIN analysis_group ag ON agtg.analysis_group_id = ag.id JOIN temp_orphaned_analysis_groups oa ON ag.id = oa.analysis_group_id").executeUpdate();
        entityManager.createNativeQuery("DROP TABLE IF EXISTS temp_orphaned_subjects").executeUpdate();
        entityManager.createNativeQuery("CREATE TEMP TABLE temp_orphaned_subjects AS\nSELECT DISTINCT s.id AS subject_id FROM subject s JOIN treatmentgroup_subject tgs ON s.id = tgs.subject_id JOIN treatment_group tg ON tgs.treatment_group_id = tg.id JOIN temp_orphaned_treatment_groups otg ON tg.id = otg.treatment_group_id").executeUpdate();

        // 3. Value tables
        int subjectValueRows = entityManager.createNativeQuery("DELETE FROM subject_value USING subject_state, subject, treatmentgroup_subject, treatment_group, analysisgroup_treatmentgroup, analysis_group, experiment_analysisgroup, experiment, temp_expired_experiments WHERE subject_value.subject_state_id = subject_state.id AND subject_state.subject_id = subject.id AND subject.id = treatmentgroup_subject.subject_id AND treatmentgroup_subject.treatment_group_id = treatment_group.id AND treatment_group.id = analysisgroup_treatmentgroup.treatment_group_id AND analysisgroup_treatmentgroup.analysis_group_id = analysis_group.id AND analysis_group.id = experiment_analysisgroup.analysis_group_id AND experiment_analysisgroup.experiment_id = experiment.id AND experiment.code_name = temp_expired_experiments.experiment_code").executeUpdate();
        logger.info("Deleted {} from subject_value", rows.apply(subjectValueRows));

        int treatmentGroupValueRows = entityManager.createNativeQuery("DELETE FROM treatment_group_value USING treatment_group_state, treatment_group, analysisgroup_treatmentgroup, analysis_group, experiment_analysisgroup, experiment, temp_expired_experiments WHERE treatment_group_value.treatment_state_id = treatment_group_state.id AND treatment_group_state.treatment_group_id = treatment_group.id AND treatment_group.id = analysisgroup_treatmentgroup.treatment_group_id AND analysisgroup_treatmentgroup.analysis_group_id = analysis_group.id AND analysis_group.id = experiment_analysisgroup.analysis_group_id AND experiment_analysisgroup.experiment_id = experiment.id AND experiment.code_name = temp_expired_experiments.experiment_code").executeUpdate();
        logger.info("Deleted {} from treatment_group_value", rows.apply(treatmentGroupValueRows));

        int analysisGroupValueRows = entityManager.createNativeQuery("DELETE FROM analysis_group_value USING analysis_group_state, analysis_group, experiment_analysisgroup, experiment, temp_expired_experiments WHERE analysis_group_value.analysis_state_id = analysis_group_state.id AND analysis_group_state.analysis_group_id = analysis_group.id AND analysis_group.id = experiment_analysisgroup.analysis_group_id AND experiment_analysisgroup.experiment_id = experiment.id AND experiment.code_name = temp_expired_experiments.experiment_code").executeUpdate();
        logger.info("Deleted {} from analysis_group_value", rows.apply(analysisGroupValueRows));

        // 4. State tables
        int subjectStateRows = entityManager.createNativeQuery("DELETE FROM subject_state USING subject, treatmentgroup_subject, treatment_group, analysisgroup_treatmentgroup, analysis_group, experiment_analysisgroup, experiment, temp_expired_experiments WHERE subject_state.subject_id = subject.id AND subject.id = treatmentgroup_subject.subject_id AND treatmentgroup_subject.treatment_group_id = treatment_group.id AND treatment_group.id = analysisgroup_treatmentgroup.treatment_group_id AND analysisgroup_treatmentgroup.analysis_group_id = analysis_group.id AND analysis_group.id = experiment_analysisgroup.analysis_group_id AND experiment_analysisgroup.experiment_id = experiment.id AND experiment.code_name = temp_expired_experiments.experiment_code").executeUpdate();
        logger.info("Deleted {} from subject_state", rows.apply(subjectStateRows));

        int treatmentGroupStateRows = entityManager.createNativeQuery("DELETE FROM treatment_group_state USING treatment_group, analysisgroup_treatmentgroup, analysis_group, experiment_analysisgroup, experiment, temp_expired_experiments WHERE treatment_group_state.treatment_group_id = treatment_group.id AND treatment_group.id = analysisgroup_treatmentgroup.treatment_group_id AND analysisgroup_treatmentgroup.analysis_group_id = analysis_group.id AND analysis_group.id = experiment_analysisgroup.analysis_group_id AND experiment_analysisgroup.experiment_id = experiment.id AND experiment.code_name = temp_expired_experiments.experiment_code").executeUpdate();
        logger.info("Deleted {} from treatment_group_state", rows.apply(treatmentGroupStateRows));

        int analysisGroupStateRows = entityManager.createNativeQuery("DELETE FROM analysis_group_state USING analysis_group, experiment_analysisgroup, experiment, temp_expired_experiments WHERE analysis_group_state.analysis_group_id = analysis_group.id AND analysis_group.id = experiment_analysisgroup.analysis_group_id AND experiment_analysisgroup.experiment_id = experiment.id AND experiment.code_name = temp_expired_experiments.experiment_code").executeUpdate();
        logger.info("Deleted {} from analysis_group_state", rows.apply(analysisGroupStateRows));

        // 5. Linker tables
        int treatmentGroupSubjectRows = entityManager.createNativeQuery("DELETE FROM treatmentgroup_subject USING treatment_group, temp_orphaned_treatment_groups WHERE treatmentgroup_subject.treatment_group_id = treatment_group.id AND treatment_group.id = temp_orphaned_treatment_groups.treatment_group_id").executeUpdate();
        logger.info("Deleted {} from treatmentgroup_subject", rows.apply(treatmentGroupSubjectRows));

        int analysisGroupTreatmentGroupRows = entityManager.createNativeQuery("DELETE FROM analysisgroup_treatmentgroup USING analysis_group, temp_orphaned_analysis_groups WHERE analysisgroup_treatmentgroup.analysis_group_id = analysis_group.id AND analysis_group.id = temp_orphaned_analysis_groups.analysis_group_id").executeUpdate();
        logger.info("Deleted {} from analysisgroup_treatmentgroup", rows.apply(analysisGroupTreatmentGroupRows));

        int experimentAnalysisGroupRows = entityManager.createNativeQuery("DELETE FROM experiment_analysisgroup USING experiment, temp_expired_experiments WHERE experiment_analysisgroup.experiment_id = experiment.id AND experiment.code_name = temp_expired_experiments.experiment_code").executeUpdate();
        logger.info("Deleted {} from experiment_analysisgroup", rows.apply(experimentAnalysisGroupRows));

        // 6. Main tables
        int subjectRows = entityManager.createNativeQuery("DELETE FROM subject USING temp_orphaned_subjects WHERE subject.id = temp_orphaned_subjects.subject_id").executeUpdate();
        logger.info("Deleted {} from subject", rows.apply(subjectRows));

        int treatmentGroupRows = entityManager.createNativeQuery("DELETE FROM treatment_group USING temp_orphaned_treatment_groups WHERE treatment_group.id = temp_orphaned_treatment_groups.treatment_group_id").executeUpdate();
        logger.info("Deleted {} from treatment_group", rows.apply(treatmentGroupRows));

        int analysisGroupRows = entityManager.createNativeQuery("DELETE FROM analysis_group USING temp_orphaned_analysis_groups WHERE analysis_group.id = temp_orphaned_analysis_groups.analysis_group_id").executeUpdate();
        logger.info("Deleted {} from analysis_group", rows.apply(analysisGroupRows));

        // 7. experiment_label
        int experimentLabelRows = entityManager.createNativeQuery("DELETE FROM experiment_label WHERE experiment_id IN (SELECT experiment_id FROM temp_expired_experiments)").executeUpdate();
        logger.info("Deleted {} from experiment_label", rows.apply(experimentLabelRows));

        // 8. Insert experiment_value for deleted date
        Query insertTxnQuery = entityManager.createNativeQuery("INSERT INTO ls_transaction (id, comments, recorded_date, version) VALUES (nextval('value_pkseq'), 'database hard delete', now(), 0) RETURNING id");
        Long txnId = ((Number) insertTxnQuery.getSingleResult()).longValue();
        
        Query insertExpValQuery = entityManager.createNativeQuery(
                "INSERT INTO experiment_value (id, ls_type, ls_kind, ls_type_and_kind, date_value, ls_transaction, recorded_by, recorded_date, version, experiment_state_id, deleted, ignored, public_data) " +
                "SELECT nextval('value_pkseq'), 'dateValue', 'database deleted date', 'dateValue_database deleted date', now(), :txnId, 'bbolt', now(), 0, es.id, false, false, false " +
                "FROM experiment_state es JOIN temp_expired_experiments tee ON es.experiment_id = tee.experiment_id " +
                "WHERE es.ignored = false AND es.deleted = false AND es.ls_type = 'metadata' AND es.ls_kind = 'experiment metadata'");
        insertExpValQuery.setParameter("txnId", txnId);
        int expValRows = insertExpValQuery.executeUpdate();
        logger.info("Inserted {} experiment_value rows for database deleted date", rows.apply(expValRows));

        // Can't delete the experiment values, states and experiment yet because we need to delete the experiment folder
        // 9. Delete from experiment
        // int experimentRows = entityManager.createNativeQuery("DELETE FROM experiment WHERE id IN (SELECT experiment_id FROM temp_expired_experiments)").executeUpdate();
        // logger.info("Deleted {} from experiment", rows.apply(experimentRows));

        logger.info("Hard deleted {} experiments: {}", expiredExperimentCodes.size(), expiredExperimentCodes);
        return expiredExperimentCodes;
    }
}
