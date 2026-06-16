package com.labsynch.labseer.service;

import java.util.Collections;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.utils.SecurityUtil;

@Service
public class ExperimentRetentionServiceImpl implements ExperimentRetentionService {

    private static final Logger logger = LoggerFactory.getLogger(ExperimentRetentionServiceImpl.class);

    private static final int BATCH_SIZE = 100000;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RetentionBatchDeleter batchDeleter;

    // ---- public entry points -------------------------------------------------

    @Override
    public List<String> hardDeleteExpiredExperiments() {
        List<String> expiredCodes = stageExpiredExperiments();
        if (expiredCodes.isEmpty()) {
            logger.info("No expired experiments found for hard deletion.");
            return Collections.emptyList();
        }
        logger.info("Staged {} expired experiments for hard deletion.", expiredCodes.size());

        batchDeleteAll("subject_value", "subject_state_id", "retention_work_subject_states", "subject_state_id");
        batchDeleteAll("treatment_group_value", "treatment_state_id", "retention_work_tg_states", "treatment_group_state_id");
        batchDeleteAll("analysis_group_value", "analysis_state_id", "retention_work_ag_states", "analysis_group_state_id");

        batchDeleteAll("subject_state", "subject_id", "retention_work_orphan_subjects", "subject_id");
        batchDeleteAll("treatment_group_state", "treatment_group_id", "retention_work_orphan_tgs", "treatment_group_id");
        batchDeleteAll("analysis_group_state", "analysis_group_id", "retention_work_orphan_ags", "analysis_group_id");

        batchDeleteAll("treatmentgroup_subject", "treatment_group_id", "retention_work_orphan_tgs", "treatment_group_id");
        batchDeleteAll("analysisgroup_treatmentgroup", "analysis_group_id", "retention_work_orphan_ags", "analysis_group_id");
        batchDeleteAll("experiment_analysisgroup", "experiment_id", "retention_work_expired_experiments", "experiment_id");

        batchDeleteAll("subject", "id", "retention_work_orphan_subjects", "subject_id");
        batchDeleteAll("treatment_group", "id", "retention_work_orphan_tgs", "treatment_group_id");
        batchDeleteAll("analysis_group", "id", "retention_work_orphan_ags", "analysis_group_id");

        batchDeleteAll("experiment_label", "experiment_id", "retention_work_expired_experiments", "experiment_id");

        stampDatabaseDeletedDate();
        dropWorkTables();

        logger.info("Hard deleted {} experiments: {}", expiredCodes.size(), expiredCodes);
        return expiredCodes;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getExperimentsAwaitingFilesDeletion() {
        String query = "SELECT DISTINCT e.code_name FROM experiment e "
            + "JOIN experiment_state es ON e.id = es.experiment_id "
            + "  AND es.ls_type_and_kind = 'metadata_experiment metadata' AND es.ignored = false AND es.deleted = false "
            + "WHERE EXISTS (SELECT 1 FROM experiment_value ev1 WHERE ev1.experiment_state_id = es.id "
            + "  AND ev1.ls_type_and_kind = 'dateValue_database deleted date' AND ev1.ignored = false AND ev1.deleted = false) "
            + "AND NOT EXISTS (SELECT 1 FROM experiment_value ev2 WHERE ev2.experiment_state_id = es.id "
            + "  AND ev2.ls_type_and_kind = 'dateValue_files deleted date' AND ev2.ignored = false AND ev2.deleted = false) "
            + "ORDER BY e.code_name";
        @SuppressWarnings("unchecked")
        List<String> codes = entityManager.createNativeQuery(query).getResultList();
        logger.info("Found {} experiments awaiting files deletion.", codes.size());
        return codes;
    }

    @Override
    @Transactional
    public List<String> completeExperimentDeletion() {
        entityManager.createNativeQuery("DROP TABLE IF EXISTS retention_work_complete").executeUpdate();
        entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS retention_work_complete (experiment_id bigint primary key)").executeUpdate();
        entityManager.createNativeQuery(
            "INSERT INTO retention_work_complete (experiment_id) "
            + "SELECT DISTINCT e.id FROM experiment e "
            + "JOIN experiment_state es ON e.id = es.experiment_id "
            + "  AND es.ls_type_and_kind = 'metadata_experiment metadata' AND es.ignored = false AND es.deleted = false "
            + "WHERE EXISTS (SELECT 1 FROM experiment_value ev1 WHERE ev1.experiment_state_id = es.id "
            + "  AND ev1.ls_type_and_kind = 'dateValue_database deleted date' AND ev1.ignored = false AND ev1.deleted = false) "
            + "AND EXISTS (SELECT 1 FROM experiment_value ev2 WHERE ev2.experiment_state_id = es.id "
            + "  AND ev2.ls_type_and_kind = 'dateValue_files deleted date' AND ev2.ignored = false AND ev2.deleted = false)"
        ).executeUpdate();

        @SuppressWarnings("unchecked")
        List<String> codes = entityManager.createNativeQuery(
            "SELECT e.code_name FROM experiment e JOIN retention_work_complete w ON e.id = w.experiment_id"
        ).getResultList();
        if (codes.isEmpty()) {
            entityManager.createNativeQuery("DROP TABLE IF EXISTS retention_work_complete").executeUpdate();
            logger.info("No experiments ready for complete deletion.");
            return Collections.emptyList();
        }

        entityManager.createNativeQuery(
            "DELETE FROM experiment_value WHERE experiment_state_id IN ("
            + " SELECT es.id FROM experiment_state es JOIN retention_work_complete w ON es.experiment_id = w.experiment_id)"
        ).executeUpdate();
        entityManager.createNativeQuery(
            "DELETE FROM experiment_state WHERE experiment_id IN (SELECT experiment_id FROM retention_work_complete)"
        ).executeUpdate();
        entityManager.createNativeQuery(
            "DELETE FROM experiment_label WHERE experiment_id IN (SELECT experiment_id FROM retention_work_complete)"
        ).executeUpdate();
        entityManager.createNativeQuery(
            "DELETE FROM experiment WHERE id IN (SELECT experiment_id FROM retention_work_complete)"
        ).executeUpdate();
        entityManager.createNativeQuery("DROP TABLE IF EXISTS retention_work_complete").executeUpdate();

        logger.info("Completed full deletion of {} experiments: {}", codes.size(), codes);
        return codes;
    }

    // ---- staging -------------------------------------------------------------

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<String> stageExpiredExperiments() {
        dropWorkTables();

        entityManager.createNativeQuery(
            "CREATE TABLE retention_work_expired_experiments AS "
            + "SELECT DISTINCT e.id AS experiment_id, e.code_name AS experiment_code "
            + "FROM protocol p "
            + "JOIN protocol_state ps ON p.id = ps.protocol_id AND ps.ignored = false AND ps.deleted = false "
            + "JOIN protocol_value pv ON ps.id = pv.protocol_state_id "
            + "  AND pv.ls_type_and_kind = 'numericValue_deleted experiment retention days' "
            + "  AND pv.ignored = false AND pv.deleted = false "
            + "JOIN experiment e ON e.protocol_id = p.id "
            + "JOIN experiment_state es ON e.id = es.experiment_id "
            + "  AND es.ls_type_and_kind = 'metadata_experiment metadata' AND es.ignored = false AND es.deleted = false "
            + "JOIN experiment_value ev ON es.id = ev.experiment_state_id "
            + "  AND ev.ls_type_and_kind = 'codeValue_experiment status' AND ev.code_value IN ('deleted','overwritten') "
            + "  AND ev.ignored = false AND ev.deleted = false "
            + "WHERE p.ignored = false AND p.deleted = false "
            + "  AND ev.recorded_date < NOW() - INTERVAL '1 day' * pv.numeric_value "
            + "  AND NOT EXISTS (SELECT 1 FROM experiment_value ev2 WHERE ev2.experiment_state_id = es.id "
            + "    AND ev2.ls_type_and_kind = 'dateValue_database deleted date' AND ev2.ignored = false AND ev2.deleted = false)"
        ).executeUpdate();
        entityManager.createNativeQuery("CREATE INDEX ON retention_work_expired_experiments (experiment_id)").executeUpdate();

        entityManager.createNativeQuery(
            "CREATE TABLE retention_work_orphan_ags AS "
            + "SELECT DISTINCT ag.id AS analysis_group_id FROM analysis_group ag "
            + "JOIN experiment_analysisgroup eag ON ag.id = eag.analysis_group_id "
            + "JOIN retention_work_expired_experiments w ON eag.experiment_id = w.experiment_id "
            + "WHERE NOT EXISTS (SELECT 1 FROM experiment_analysisgroup eag2 "
            + "  WHERE eag2.analysis_group_id = ag.id "
            + "  AND eag2.experiment_id NOT IN (SELECT experiment_id FROM retention_work_expired_experiments))"
        ).executeUpdate();
        entityManager.createNativeQuery("CREATE INDEX ON retention_work_orphan_ags (analysis_group_id)").executeUpdate();

        entityManager.createNativeQuery(
            "CREATE TABLE retention_work_orphan_tgs AS "
            + "SELECT DISTINCT tg.id AS treatment_group_id FROM treatment_group tg "
            + "JOIN analysisgroup_treatmentgroup agtg ON tg.id = agtg.treatment_group_id "
            + "JOIN retention_work_orphan_ags oa ON agtg.analysis_group_id = oa.analysis_group_id "
            + "WHERE NOT EXISTS (SELECT 1 FROM analysisgroup_treatmentgroup agtg2 "
            + "  WHERE agtg2.treatment_group_id = tg.id "
            + "  AND agtg2.analysis_group_id NOT IN (SELECT analysis_group_id FROM retention_work_orphan_ags))"
        ).executeUpdate();
        entityManager.createNativeQuery("CREATE INDEX ON retention_work_orphan_tgs (treatment_group_id)").executeUpdate();

        entityManager.createNativeQuery(
            "CREATE TABLE retention_work_orphan_subjects AS "
            + "SELECT DISTINCT s.id AS subject_id FROM subject s "
            + "JOIN treatmentgroup_subject tgs ON s.id = tgs.subject_id "
            + "JOIN retention_work_orphan_tgs ot ON tgs.treatment_group_id = ot.treatment_group_id "
            + "WHERE NOT EXISTS (SELECT 1 FROM treatmentgroup_subject tgs2 "
            + "  WHERE tgs2.subject_id = s.id "
            + "  AND tgs2.treatment_group_id NOT IN (SELECT treatment_group_id FROM retention_work_orphan_tgs))"
        ).executeUpdate();
        entityManager.createNativeQuery("CREATE INDEX ON retention_work_orphan_subjects (subject_id)").executeUpdate();

        entityManager.createNativeQuery(
            "CREATE TABLE retention_work_ag_states AS "
            + "SELECT ags.id AS analysis_group_state_id FROM analysis_group_state ags "
            + "JOIN retention_work_orphan_ags oa ON ags.analysis_group_id = oa.analysis_group_id"
        ).executeUpdate();
        entityManager.createNativeQuery("CREATE INDEX ON retention_work_ag_states (analysis_group_state_id)").executeUpdate();

        entityManager.createNativeQuery(
            "CREATE TABLE retention_work_tg_states AS "
            + "SELECT tgs.id AS treatment_group_state_id FROM treatment_group_state tgs "
            + "JOIN retention_work_orphan_tgs ot ON tgs.treatment_group_id = ot.treatment_group_id"
        ).executeUpdate();
        entityManager.createNativeQuery("CREATE INDEX ON retention_work_tg_states (treatment_group_state_id)").executeUpdate();

        entityManager.createNativeQuery(
            "CREATE TABLE retention_work_subject_states AS "
            + "SELECT ss.id AS subject_state_id FROM subject_state ss "
            + "JOIN retention_work_orphan_subjects os ON ss.subject_id = os.subject_id"
        ).executeUpdate();
        entityManager.createNativeQuery("CREATE INDEX ON retention_work_subject_states (subject_state_id)").executeUpdate();

        @SuppressWarnings("unchecked")
        List<String> codes = entityManager.createNativeQuery(
            "SELECT experiment_code FROM retention_work_expired_experiments ORDER BY experiment_code"
        ).getResultList();
        return codes;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void stampDatabaseDeletedDate() {
        String recordedBy = "acas";
        Author user = SecurityUtil.getLoginUser();
        if (user != null && user.getUserName() != null) {
            recordedBy = user.getUserName();
        }

        Query insertTxn = entityManager.createNativeQuery(
            "INSERT INTO ls_transaction (id, comments, recorded_date, version) "
            + "VALUES (nextval('value_pkseq'), 'database hard delete', now(), 0) RETURNING id");
        Long txnId = ((Number) insertTxn.getSingleResult()).longValue();

        Query insert = entityManager.createNativeQuery(
            "INSERT INTO experiment_value "
            + "(id, ls_type, ls_kind, ls_type_and_kind, date_value, ls_transaction, recorded_by, recorded_date, version, experiment_state_id, deleted, ignored, public_data) "
            + "SELECT nextval('value_pkseq'), 'dateValue', 'database deleted date', 'dateValue_database deleted date', now(), :txnId, :recordedBy, now(), 0, es.id, false, false, false "
            + "FROM experiment_state es JOIN retention_work_expired_experiments w ON es.experiment_id = w.experiment_id "
            + "WHERE es.ignored = false AND es.deleted = false AND es.ls_type = 'metadata' AND es.ls_kind = 'experiment metadata'");
        insert.setParameter("txnId", txnId);
        insert.setParameter("recordedBy", recordedBy);
        int rows = insert.executeUpdate();
        logger.info("Inserted {} database-deleted-date values (recorded_by={}).", rows, recordedBy);
    }

    // ---- helpers -------------------------------------------------------------

    private void batchDeleteAll(String targetTable, String joinColumn, String workTable, String workColumn) {
        long total = 0;
        int deleted;
        do {
            deleted = batchDeleter.deleteBatch(targetTable, joinColumn, workTable, workColumn, BATCH_SIZE);
            total += deleted;
            if (deleted > 0) {
                logger.info("{}: deleted {} this batch (running total {})", targetTable, deleted, total);
            }
        } while (deleted > 0);
        logger.info("{}: finished, {} rows deleted.", targetTable, total);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void dropWorkTables() {
        for (String t : new String[] {
            "retention_work_subject_states", "retention_work_tg_states", "retention_work_ag_states",
            "retention_work_orphan_subjects", "retention_work_orphan_tgs", "retention_work_orphan_ags",
            "retention_work_expired_experiments"
        }) {
            entityManager.createNativeQuery("DROP TABLE IF EXISTS " + t).executeUpdate();
        }
    }
}
