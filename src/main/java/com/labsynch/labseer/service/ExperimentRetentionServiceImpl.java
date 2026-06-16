package com.labsynch.labseer.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Retention purge of expired (soft-deleted / overwritten) experiments.
 *
 * Runs entirely inside roo on an internal schedule. Multi-pod safe: every pod's scheduled tick
 * tries a Postgres advisory lock and only the winner runs (the lock auto-releases if the pod
 * dies). Crash-safe / resumable: staging writes persistent work tables that are reused on the
 * next run, and every delete is idempotent, so a killed pod simply resumes. The heavy child
 * deletes run in per-batch-committed chunks via {@link RetentionBatchDeleter}.
 */
@Service
public class ExperimentRetentionServiceImpl implements ExperimentRetentionService {

    private static final Logger logger = LoggerFactory.getLogger(ExperimentRetentionServiceImpl.class);

    private static final int ADVISORY_LOCK_NAMESPACE_ACAS = "acas".hashCode();
    private static final int ADVISORY_LOCK_EXPERIMENT_RETENTION = "experiment-retention".hashCode();

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RetentionBatchDeleter batchDeleter;

    @Autowired
    private RetentionFileService retentionFileService;

    @Autowired
    private DataSource dataSource;

    @Value("${server.experiment.retention.enabled:true}")
    private boolean retentionEnabled;

    @Value("${server.experiment.retention.batchSize:100000}")
    private int batchSize;

    // ---- scheduled entry point ----------------------------------------------

    // Runs at a fixed wall-clock time (default 05:00 UTC daily). cron is the standard 6-field Spring
    // expression; cronZone defaults to UTC (ACAS containers run UTC).
    @Scheduled(cron = "${server.experiment.retention.cron:0 0 5 * * *}", zone = "${server.experiment.retention.cronZone:UTC}")
    public void scheduledPurge() {
        if (!retentionEnabled) {
            return;
        }
        try {
            purgeExpiredExperiments();
        } catch (Exception e) {
            logger.error("Scheduled experiment retention purge failed", e);
        }
    }

    // ---- public entry point (also used by the manual admin endpoint) --------

    @Override
    public List<String> purgeExpiredExperiments() {
        Connection lockConnection = null;
        boolean lockAcquired = false;
        try {
            lockConnection = dataSource.getConnection();
            lockAcquired = tryAcquireClusterLock(lockConnection,
                ADVISORY_LOCK_NAMESPACE_ACAS, ADVISORY_LOCK_EXPERIMENT_RETENTION);
            if (!lockAcquired) {
                logger.info("Another pod ({}) holds the experiment-retention lock. Skipping this run.", getPodName());
                return Collections.emptyList();
            }
            logger.info("Acquired experiment-retention cluster lock on pod {}.", getPodName());
            return doPurge();
        } catch (SQLException e) {
            logger.error("Experiment retention skipped: could not acquire database advisory lock", e);
            return Collections.emptyList();
        } finally {
            if (lockAcquired && lockConnection != null) {
                releaseClusterLock(lockConnection, ADVISORY_LOCK_NAMESPACE_ACAS, ADVISORY_LOCK_EXPERIMENT_RETENTION);
            }
            if (lockConnection != null) {
                try {
                    lockConnection.close();
                } catch (SQLException e) {
                    logger.warn("Failed to close experiment-retention lock connection", e);
                }
            }
        }
    }

    // ---- orchestration -------------------------------------------------------

    private List<String> doPurge() {
        List<String> codes = stageExpiredExperiments();
        if (codes.isEmpty()) {
            dropWorkTables();
            logger.info("No expired experiments found for retention purge.");
            return Collections.emptyList();
        }
        logger.info("Retention: purging {} expired experiments.", codes.size());

        // 1. Child data (heavy) — chunked, per-batch commit.
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

        // 2. Folders on disk (paths resolved by acas). If skipped (config missing / acas unreachable),
        //    leave the experiment shells + work tables in place and resume on the next run.
        boolean filesDone = retentionFileService.deleteFolders(codes);
        if (!filesDone) {
            logger.warn("Retention: folder deletion deferred; leaving {} experiment shells for the next run.", codes.size());
            return codes;
        }

        // 3. Experiment-experiment interactions (FK to experiment), then the experiment shell.
        batchDeleteAll("itx_expt_expt_value", "ls_state", "retention_work_itx_states", "itx_state_id");
        batchDeleteAll("itx_expt_expt_state", "id", "retention_work_itx_states", "itx_state_id");
        batchDeleteAll("itx_expt_expt", "id", "retention_work_itx", "itx_id");

        batchDeleteAll("experiment_value", "experiment_state_id", "retention_work_expired_states", "experiment_state_id");
        batchDeleteAll("experiment_label", "experiment_id", "retention_work_expired_experiments", "experiment_id");
        batchDeleteAll("experiment_state", "experiment_id", "retention_work_expired_experiments", "experiment_id");
        batchDeleteAll("experiment", "id", "retention_work_expired_experiments", "experiment_id");

        dropWorkTables();
        logger.info("Retention: purged {} experiments: {}", codes.size(), codes);
        return codes;
    }

    // ---- staging (persistent work tables; resume if already present) ---------

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<String> stageExpiredExperiments() {
        if (workTableExists("retention_work_expired_experiments")) {
            logger.info("Retention: resuming from existing work tables.");
            return expiredCodes();
        }

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
            + "  AND ev.recorded_date < NOW() - INTERVAL '1 day' * pv.numeric_value"
        ).executeUpdate();
        entityManager.createNativeQuery("CREATE INDEX ON retention_work_expired_experiments (experiment_id)").executeUpdate();

        entityManager.createNativeQuery(
            "CREATE TABLE retention_work_expired_states AS "
            + "SELECT es.id AS experiment_state_id FROM experiment_state es "
            + "JOIN retention_work_expired_experiments w ON es.experiment_id = w.experiment_id"
        ).executeUpdate();
        entityManager.createNativeQuery("CREATE INDEX ON retention_work_expired_states (experiment_state_id)").executeUpdate();

        // Orphan analysis groups: reachable from an expired experiment AND not from any non-expired one.
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

        // Experiment-experiment interactions referencing an expired experiment (FK to experiment).
        entityManager.createNativeQuery(
            "CREATE TABLE retention_work_itx AS "
            + "SELECT DISTINCT i.id AS itx_id FROM itx_expt_expt i "
            + "WHERE i.first_experiment_id IN (SELECT experiment_id FROM retention_work_expired_experiments) "
            + "   OR i.second_experiment_id IN (SELECT experiment_id FROM retention_work_expired_experiments)"
        ).executeUpdate();
        entityManager.createNativeQuery("CREATE INDEX ON retention_work_itx (itx_id)").executeUpdate();

        entityManager.createNativeQuery(
            "CREATE TABLE retention_work_itx_states AS "
            + "SELECT s.id AS itx_state_id FROM itx_expt_expt_state s "
            + "JOIN retention_work_itx w ON s.itx_experiment_experiment = w.itx_id"
        ).executeUpdate();
        entityManager.createNativeQuery("CREATE INDEX ON retention_work_itx_states (itx_state_id)").executeUpdate();

        return expiredCodes();
    }

    @SuppressWarnings("unchecked")
    private List<String> expiredCodes() {
        return entityManager.createNativeQuery(
            "SELECT experiment_code FROM retention_work_expired_experiments ORDER BY experiment_code"
        ).getResultList();
    }

    private boolean workTableExists(String table) {
        Object reg = entityManager.createNativeQuery("SELECT to_regclass(:t)")
            .setParameter("t", table)
            .getSingleResult();
        return reg != null;
    }

    // ---- helpers -------------------------------------------------------------

    private void batchDeleteAll(String targetTable, String joinColumn, String workTable, String workColumn) {
        long total = 0;
        int deleted;
        do {
            deleted = batchDeleter.deleteBatch(targetTable, joinColumn, workTable, workColumn, batchSize);
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
            "retention_work_itx_states", "retention_work_itx",
            "retention_work_subject_states", "retention_work_tg_states", "retention_work_ag_states",
            "retention_work_orphan_subjects", "retention_work_orphan_tgs", "retention_work_orphan_ags",
            "retention_work_expired_states", "retention_work_expired_experiments"
        }) {
            entityManager.createNativeQuery("DROP TABLE IF EXISTS " + t).executeUpdate();
        }
    }

    // ---- cluster lock (mirrors StandardizationServiceImpl) -------------------

    private boolean tryAcquireClusterLock(Connection connection, int lockNamespace, int lockId) throws SQLException {
        String sql = "SELECT pg_try_advisory_lock(?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, lockNamespace);
            statement.setInt(2, lockId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        }
        return false;
    }

    private void releaseClusterLock(Connection connection, int lockNamespace, int lockId) {
        String sql = "SELECT pg_advisory_unlock(?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, lockNamespace);
            statement.setInt(2, lockId);
            statement.executeQuery();
        } catch (SQLException e) {
            logger.warn("Failed to release experiment-retention cluster lock", e);
        }
    }

    private String getPodName() {
        String podName = System.getenv("HOSTNAME");
        return StringUtils.isBlank(podName) ? "<unknown-pod>" : podName;
    }
}
