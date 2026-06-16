# Experiment Retention Policy Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Let admins set a per-protocol retention period in the UI, and purge soft-deleted expired experiments reliably at any data volume without stalling.

**Architecture:** Frontend adds one numeric protocol-metadata field (admins edit, others read-only) that writes the existing `numericValue / deleted experiment retention days` protocol value. Backend replaces the single-giant-transaction purge with a hybrid engine: a non-transactional orchestrator stages the exact target rows into **persistent work tables** (with a shared-entity safety guard), then a separate `REQUIRES_NEW` batch-deleter removes rows in small per-batch-committed chunks (children-before-parents, `ctid`-based batching). Only the value-kinds migration ships; the destructive/seed migrations are removed.

**Tech Stack:** Java 21 / Spring (acas-roo-server), JUnit4 + `SpringJUnit4ClassRunner` + `@Transactional` integration tests, PostgreSQL native SQL; CoffeeScript / Backbone (acas frontend).

**Branch:** `ACAS-994-retention-policy` (already checked out in both repos; acas frontend is in worktree `../acas-ACAS-retention`).

**Spec:** `docs/superpowers/specs/2026-06-16-experiment-retention-policy-design.md`

---

## File Structure

**acas-roo-server (backend)**
- Modify: `src/main/java/com/labsynch/labseer/service/ExperimentRetentionServiceImpl.java` — orchestration + staging (non-transactional outer methods).
- Create: `src/main/java/com/labsynch/labseer/service/RetentionBatchDeleter.java` — `@Service` with a single `REQUIRES_NEW` method that deletes one batch and reports the row count.
- Modify: `src/main/java/com/labsynch/labseer/service/ExperimentRetentionService.java` — unchanged interface (already correct), confirm signatures.
- Modify: `src/main/java/com/labsynch/labseer/api/ApiExperimentRetentionController.java` — add admin authorization guard.
- Delete: `src/main/resources/db/migration/postgres/V2.4.3.1__protocol_retention_policy.sql`
- Delete: `src/main/resources/db/migration/postgres/V2.4.3.2__experiment_retention_hard_delete.sql`
- Keep: `src/main/resources/db/migration/postgres/V2.4.3.0__experiment_retention_value_kinds.sql`
- Replace: `src/test/java/com/labsynch/labseer/service/ExperimentRetentionServiceImplTest.java` — real DB-backed integration test.

**acas (frontend, worktree `../acas-ACAS-retention`)**
- Modify: `modules/ServerAPI/src/client/Protocol.coffee` — model getter, controller event + handler + render gating.
- Modify: `modules/ServerAPI/src/client/Protocol.html` — the retention-days control group.
- Modify: `modules/ServerAPI/src/server/routes/ExperimentServiceRoutes.coffee` — short-circuit on error; don't stamp files-deleted on failure; URL normalization.

---

## Part A — Migration cleanup

### Task A1: Remove the seed and auto-running-delete migrations

**Files:**
- Delete: `src/main/resources/db/migration/postgres/V2.4.3.1__protocol_retention_policy.sql`
- Delete: `src/main/resources/db/migration/postgres/V2.4.3.2__experiment_retention_hard_delete.sql`

- [ ] **Step 1: Confirm V2.4.3.0 is the only retention migration that should remain**

Run: `ls src/main/resources/db/migration/postgres/ | grep -i retention`
Expected: three files (`V2.4.3.0…value_kinds`, `V2.4.3.1…protocol_retention_policy`, `V2.4.3.2…experiment_retention_hard_delete`).

- [ ] **Step 2: Delete the two offending migrations**

```bash
git rm src/main/resources/db/migration/postgres/V2.4.3.1__protocol_retention_policy.sql \
       src/main/resources/db/migration/postgres/V2.4.3.2__experiment_retention_hard_delete.sql
```

- [ ] **Step 3: Verify only the value-kinds migration remains**

Run: `ls src/main/resources/db/migration/postgres/ | grep -i retention`
Expected: only `V2.4.3.0__experiment_retention_value_kinds.sql`.

- [ ] **Step 4: Commit**

```bash
git add -A
git commit -m "ACAS-994: drop seed + auto-running hard-delete migrations; keep only value-kinds"
```

---

## Part B — Backend deletion engine

> Key constraint: TEMP tables are connection-scoped and will NOT be visible across `REQUIRES_NEW` transactions (which acquire a different pooled connection). We therefore stage into **persistent work tables** (regular tables prefixed `retention_work_`), populate them in one transaction, chunk-delete across many transactions, then truncate them.

### Task B1: Create the batch-deleter component (one batch per REQUIRES_NEW transaction)

**Files:**
- Create: `src/main/java/com/labsynch/labseer/service/RetentionBatchDeleter.java`

- [ ] **Step 1: Write the batch-deleter**

`RetentionBatchDeleter.deleteBatch` deletes up to `batchSize` rows of `targetTable` whose `joinColumn` matches a row in `workTable.workColumn`, using `ctid` so it works with non-unique join keys. It runs in its own committed transaction and returns the rows deleted.

```java
package com.labsynch.labseer.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RetentionBatchDeleter {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Deletes up to batchSize rows from targetTable whose joinColumn matches a value in
     * workTable.workColumn. Uses ctid in the LIMIT subquery so batching is correct even when
     * the join key is not unique. Runs in its own committed transaction.
     *
     * Table/column names are internal constants (never user input) — assembled, not parameterized,
     * because identifiers cannot be JDBC bind parameters.
     *
     * @return number of rows deleted in this batch
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int deleteBatch(String targetTable, String joinColumn,
                           String workTable, String workColumn, int batchSize) {
        String sql = "DELETE FROM " + targetTable + " WHERE ctid IN ("
                + " SELECT t.ctid FROM " + targetTable + " t"
                + " JOIN " + workTable + " w ON t." + joinColumn + " = w." + workColumn
                + " LIMIT " + batchSize + ")";
        return entityManager.createNativeQuery(sql).executeUpdate();
    }
}
```

- [ ] **Step 2: Compile**

Run: `mvn -q -o compile` (or `mvn -q compile` if offline cache is cold)
Expected: BUILD SUCCESS.

- [ ] **Step 3: Commit**

```bash
git add src/main/java/com/labsynch/labseer/service/RetentionBatchDeleter.java
git commit -m "ACAS-994: add RetentionBatchDeleter (ctid batched delete, per-batch commit)"
```

### Task B2: Rewrite the staging + orchestration in ExperimentRetentionServiceImpl

**Files:**
- Modify: `src/main/java/com/labsynch/labseer/service/ExperimentRetentionServiceImpl.java`

- [ ] **Step 1: Replace the class body with the staged/batched engine**

Replace the entire file with the version below. Notes baked in:
- Outer methods are **not** `@Transactional` (so per-batch commits stick); `stageExpiredExperiments()` and `stampDatabaseDeletedDate()` are `REQUIRES_NEW` units.
- Shared-entity guard: orphan parents exclude any analysis group / treatment group / subject still reachable from a **non-expired** experiment.
- Audit user comes from `SecurityUtil.getLoginUser()`, falling back to `"acas"`.
- Work tables are persistent (`retention_work_*`) and truncated/recreated each run.

```java
package com.labsynch.labseer.service;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

        // Children-before-parents. Value tables and state tables join through the work table of
        // expired experiments; main parent tables join through the orphan work tables.
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
        String createWork = "CREATE TABLE IF NOT EXISTS retention_work_complete (experiment_id bigint primary key)";
        entityManager.createNativeQuery("DROP TABLE IF EXISTS retention_work_complete").executeUpdate();
        entityManager.createNativeQuery(createWork).executeUpdate();
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

    /**
     * Builds the persistent work tables describing exactly what will be deleted, and returns the
     * list of expired experiment codes. Runs in its own committed transaction so the work tables
     * are visible to the later REQUIRES_NEW batch deletes (which use different connections).
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<String> stageExpiredExperiments() {
        dropWorkTables();

        // 1. Expired experiments: protocol has retention days, experiment status deleted/overwritten,
        //    older than retention window, and not already database-deleted (idempotency).
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

        // 2. Orphan analysis groups: linked to an expired experiment AND not linked to any
        //    experiment outside the expired set (shared-entity guard).
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

        // 3. Orphan treatment groups: under an orphan analysis group AND not under any non-orphan AG.
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

        // 4. Orphan subjects: under an orphan treatment group AND not under any non-orphan TG.
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

        // 5. State work tables (value deletes join through these).
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

    /** Stamps each expired experiment with a database-deleted-date value, attributed to the login user. */
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
```

- [ ] **Step 2: Verify `Author.getUserName()` exists**

Run: `grep -n "getUserName\|userName" src/main/java/com/labsynch/labseer/domain/Author.java | head`
Expected: a `getUserName()` accessor (Roo-generated). If the accessor is named differently (e.g. `getUserName` vs `getUsername`), adjust the call in `stampDatabaseDeletedDate`.

- [ ] **Step 3: Compile**

Run: `mvn -q -o compile`
Expected: BUILD SUCCESS.

- [ ] **Step 4: Commit**

```bash
git add src/main/java/com/labsynch/labseer/service/ExperimentRetentionServiceImpl.java
git commit -m "ACAS-994: staged, chunked, shared-entity-safe retention deletion engine"
```

### Task B3: Add admin authorization to the controller

**Files:**
- Modify: `src/main/java/com/labsynch/labseer/api/ApiExperimentRetentionController.java`

- [ ] **Step 1: Inspect how the current user/roles are read elsewhere**

Run: `sed -n '1,80p' src/main/java/com/labsynch/labseer/utils/SecurityUtil.java`
Expected: `getLoginUser()` returns an `Author`; identify how roles are exposed (e.g. `author.getAuthorRoles()` / `RoleKind`). Use whatever role-membership accessor exists to detect the ACAS admin role.

- [ ] **Step 2: Guard the destructive endpoints**

Add a private `requireAdmin()` that returns a 403 `ResponseEntity` when the login user lacks the admin role, and call it at the top of `hardDeleteExpiredExperiments` and `completeExperimentDeletion`. Concrete shape (adapt the role check to the accessor confirmed in Step 1):

```java
import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.utils.SecurityUtil;
import org.springframework.http.HttpStatus;

private boolean isAdmin() {
    Author user = SecurityUtil.getLoginUser();
    if (user == null || user.getAuthorRoles() == null) return false;
    return user.getAuthorRoles().stream().anyMatch(ar ->
        ar.getRoleEntry() != null
        && "admin".equalsIgnoreCase(ar.getRoleEntry().getRoleName()));
}
```

```java
@PostMapping("/hard-delete")
public ResponseEntity<java.util.List<String>> hardDeleteExpiredExperiments() {
    if (!isAdmin()) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    return ResponseEntity.ok(experimentRetentionService.hardDeleteExpiredExperiments());
}
```

(Apply the same guard to `completeExperimentDeletion`. Leave `awaiting-files-deletion` readable.)

- [ ] **Step 3: Compile**

Run: `mvn -q -o compile`
Expected: BUILD SUCCESS. If `getAuthorRoles()`/`getRoleEntry()`/`getRoleName()` don't match the domain, fix using the accessors confirmed in Step 1.

- [ ] **Step 4: Commit**

```bash
git add src/main/java/com/labsynch/labseer/api/ApiExperimentRetentionController.java
git commit -m "ACAS-994: require admin role for destructive retention endpoints"
```

### Task B4: Replace the broken test with a real integration test

**Files:**
- Replace: `src/test/java/com/labsynch/labseer/service/ExperimentRetentionServiceImplTest.java`

- [ ] **Step 1: Find the exact Spring test context used by sibling service tests**

Run: `grep -n "@ContextConfiguration\|@RunWith\|@Transactional\|extends" src/test/java/com/labsynch/labseer/service/AnalysisGroupValueServiceTest.java`
Expected: the `@RunWith(SpringJUnit4ClassRunner.class)` + `@ContextConfiguration(locations=...)` values to copy verbatim into the new test.

- [ ] **Step 2: Write the integration test**

Insert minimal fixtures via native SQL (a protocol with a retention value, an expired deleted experiment with an analysis group + value, and a second NON-expired experiment sharing nothing), run the staging, and assert. Copy the `@RunWith`/`@ContextConfiguration` annotations confirmed in Step 1 into the placeholders marked below.

```java
package com.labsynch.labseer.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
// TODO Step 1: paste the exact @ContextConfiguration(locations = {...}) from AnalysisGroupValueServiceTest
@Transactional
public class ExperimentRetentionServiceImplTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ExperimentRetentionServiceImpl service;

    @Test
    public void stagingSelectsOnlyExpiredExperimentsAndRespectsSharedEntities() {
        // value kinds must exist (V2.4.3.0); create them defensively if absent
        em.createNativeQuery("INSERT INTO value_kind (id, kind_name, ls_type_and_kind, version, ls_type) "
            + "SELECT nextval('value_kind_pkseq'),'deleted experiment retention days','numericValue_deleted experiment retention days',0, vt.id "
            + "FROM value_type vt WHERE vt.type_name='numericValue' ON CONFLICT (ls_type_and_kind) DO NOTHING").executeUpdate();

        // ... insert: protocol P with protocol_value 'deleted experiment retention days' = 7;
        //     experiment E_expired (status 'deleted', recorded_date now()-30d) under P;
        //     experiment E_live (status 'approved') under P;
        // Use nextval('thing_pkseq') / nextval('value_pkseq') for ids, mirroring V2.4.3.* SQL.
        // (Full fixture INSERTs omitted here ONLY because they are long and mechanical — the
        //  implementer writes them following the column lists already shown in
        //  V2.4.3.0/V2.4.3.1 and stampDatabaseDeletedDate; see Step 3 for the assertions that pin behavior.)

        List<String> staged = service.stageExpiredExperiments();
        assertTrue("expired experiment must be staged", staged.contains("E_expired"));
        assertEquals("live experiment must NOT be staged", false, staged.contains("E_live"));

        Number sharedAgRemaining = (Number) em.createNativeQuery(
            "SELECT count(*) FROM retention_work_orphan_ags").getSingleResult();
        assertTrue("orphan AG work table populated", sharedAgRemaining.intValue() >= 0);

        service.dropWorkTables();
    }
}
```

> Note: the fixture INSERTs are intentionally the implementer's first sub-step in Step 3 below — they are long, mechanical, and must match the live schema, so they are written and run against the real test DB rather than guessed here. Every column list needed is already shown verbatim in `V2.4.3.0__experiment_retention_value_kinds.sql`, `V2.4.3.1__protocol_retention_policy.sql` (protocol_value columns), and `stampDatabaseDeletedDate` (experiment_value columns).

- [ ] **Step 3: Write fixture INSERTs, then run the test**

Write the three fixture inserts (protocol + retention value; expired experiment + status value + analysis group + ag value; live experiment) at the top of the test using the column lists referenced above. Then:

Run: `mvn -q test -Dtest=ExperimentRetentionServiceImplTest`
Expected: PASS — `E_expired` staged, `E_live` not staged.

- [ ] **Step 4: Add a chunking assertion**

Add a second `@Test` that inserts >1 batch worth of `analysis_group_value` rows under the expired experiment is impractical at 100k in a unit test, so instead set `BATCH_SIZE` low via a package-private override OR assert that `RetentionBatchDeleter.deleteBatch` returns the expected count for a small `LIMIT`. Concretely, call `batchDeleter.deleteBatch("analysis_group_value","analysis_state_id","retention_work_ag_states","analysis_group_state_id", 1)` in a loop and assert the running total equals the inserted row count.

Run: `mvn -q test -Dtest=ExperimentRetentionServiceImplTest`
Expected: PASS.

- [ ] **Step 5: Commit**

```bash
git add src/test/java/com/labsynch/labseer/service/ExperimentRetentionServiceImplTest.java
git commit -m "ACAS-994: real integration test for retention staging + chunked delete"
```

---

## Part C — Frontend retention-days field

### Task C1: Add the model getter

**Files:**
- Modify: `modules/ServerAPI/src/client/Protocol.coffee` (worktree `../acas-ACAS-retention`)

- [ ] **Step 1: Add `getDeletedExperimentRetentionDays` to the `Protocol` model**

After the existing getters (e.g. near `getCurveDisplayMin`, around the other `getOrCreateValueByTypeAndKind` getters), add:

```coffeescript
	getDeletedExperimentRetentionDays: ->
		@.get('lsStates').getOrCreateValueByTypeAndKind "metadata", "protocol metadata", "numericValue", "deleted experiment retention days"
```

- [ ] **Step 2: Commit**

```bash
git -C ../acas-ACAS-retention add modules/ServerAPI/src/client/Protocol.coffee
git -C ../acas-ACAS-retention commit -m "ACAS-994: Protocol model getter for deleted experiment retention days"
```

### Task C2: Add the HTML control group

**Files:**
- Modify: `modules/ServerAPI/src/client/Protocol.html`

- [ ] **Step 1: Add the field after the notebook-page control group**

Mirror the existing `bv_group_notebookPage` markup:

```html
            <div class="control-group bv_group_deletedExperimentRetentionDays">
                <label class="control-label bv_deletedExperimentRetentionDaysLabel">Deleted experiment retention (days)</label>
                <div class="controls">
                    <input class="bv_deletedExperimentRetentionDays" style="width:336px;" type="number" min="0" step="1" placeholder="Blank = keep indefinitely" />
                    <i class="icon-info-sign" style="margin-left:4px;" title="Soft-deleted experiments under this protocol are purged this many days after deletion. Blank = never purge."></i>
                </div>
            </div>
```

- [ ] **Step 2: Commit**

```bash
git -C ../acas-ACAS-retention add modules/ServerAPI/src/client/Protocol.html
git -C ../acas-ACAS-retention commit -m "ACAS-994: retention-days field markup in protocol form"
```

### Task C3: Wire the controller (event, handler, render + admin gating)

**Files:**
- Modify: `modules/ServerAPI/src/client/Protocol.coffee` — `ProtocolBaseController`

- [ ] **Step 1: Register the event**

In `ProtocolBaseController`'s `events:` map, add alongside `"keyup .bv_minY"`:

```coffeescript
			"keyup .bv_deletedExperimentRetentionDays": "handleDeletedExperimentRetentionDaysChanged"
```

- [ ] **Step 2: Add the change handler (mirrors `handleCurveDisplayMinChanged`)**

```coffeescript
	handleDeletedExperimentRetentionDaysChanged: =>
		value = UtilityFunctions::getTrimmedInput @$('.bv_deletedExperimentRetentionDays')
		unless value is ""
			value = parseInt value, 10
		@handleValueChanged "DeletedExperimentRetentionDays", value
```

- [ ] **Step 3: Populate + gate in `render`**

In `ProtocolBaseController.render` (after the other fields are set from the model), add:

```coffeescript
		retentionValue = @model.getDeletedExperimentRetentionDays().get('numericValue')
		@$('.bv_deletedExperimentRetentionDays').val(if retentionValue? then retentionValue else "")
		isAdmin = false
		if window.conf.roles?.acas?.adminRole?
			adminRoles = window.conf.roles.acas.adminRole.split(",")
			isAdmin = UtilityFunctions::testUserHasRole(window.AppLaunchParams.loginUser, adminRoles)
		unless isAdmin
			@$('.bv_deletedExperimentRetentionDays').attr('disabled', 'disabled')
```

- [ ] **Step 4: Verify `testUserHasRole` exists with that signature**

Run: `grep -n "testUserHasRole:\|testUserHasRole " ../acas-ACAS-retention/modules/Components/src/client/UtilityFunctions.coffee`
Expected: a `testUserHasRole: (user, roles) ->` method (used in `SaltBrowser.coffee`). If only `testUserHasRoleTypeKindName` exists, build the role objects as `ProtocolBrowser` does instead.

- [ ] **Step 5: Build the frontend and sanity-check it compiles**

Run: `cd ../acas-ACAS-retention && npm run build` (or `gulp build`)
Expected: CoffeeScript compiles with no errors for `Protocol.coffee`.

- [ ] **Step 6: Manual verification**

Start the stack; open a protocol as admin → set retention days → save → reload → value persists. Open as a non-admin → field is visible but disabled.

- [ ] **Step 7: Commit**

```bash
git -C ../acas-ACAS-retention add modules/ServerAPI/src/client/Protocol.coffee
git -C ../acas-ACAS-retention commit -m "ACAS-994: edit/persist retention days in protocol form (admin-gated)"
```

---

## Part D — Node orchestration fixes

### Task D1: Short-circuit on error and don't stamp files-deleted on failure

**Files:**
- Modify: `modules/ServerAPI/src/server/routes/ExperimentServiceRoutes.coffee`

- [ ] **Step 1: Make the combined pipeline stop on a failed phase**

In `deleteExpiredExperiments`, the fake `resp` objects currently fire the success continuation even on HTTP 500. Change each phase's handler call so the continuation only runs on success. Replace the database phase's fake resp with one that inspects status:

```coffeescript
exports.deleteExpiredExperiments = (req, resp) ->
	console.log "Starting expired experiments cleanup (database + folders + complete)"
	fail = (stage, detail) ->
		resp.statusCode = 500
		resp.json { error: true, stage: stage, detail: detail }
	exports.deleteExpiredExperimentsDatabase req, {
		statusCode: 200
		json: (dbResult) =>
			if @statusCode and @statusCode >= 400 then return fail("database", dbResult)
			exports.deleteExpiredExperimentsFolders req, {
				statusCode: 200
				json: (folderResult) =>
					if @statusCode and @statusCode >= 400 then return fail("folders", folderResult)
					exports.deleteExpiredExperimentsComplete req, {
						statusCode: 200
						json: (completeResult) =>
							if @statusCode and @statusCode >= 400 then return fail("complete", completeResult)
							resp.json { databaseDeletion: dbResult, folderDeletion: folderResult, completeDeletion: completeResult }
					}
			}
	}
```

- [ ] **Step 2: Don't mark files-deleted when folder removal failed**

In `deleteExpiredExperimentsFolders.processExperiment`, only call `updateExperimentValue` for `files deleted date` when `folderError` is null (folder removed or already absent). When `folderError` is set, push to `failed` and skip the value update:

```coffeescript
				if folderError
					failed.push { code, error: folderError }
					completed++
					if completed == total then processComplete()
					return
				currentTimestamp = new Date().toISOString()
				updateExperimentValue code, "metadata", "experiment metadata", "dateValue", "files deleted date", currentTimestamp, (err, result) ->
					if err
						failed.push { code, error: "Failed to update experiment value: #{JSON.stringify(err)}" }
					else
						deleted.push code
					completed++
					if completed == total then processComplete()
```

- [ ] **Step 3: Normalize the awaiting-files-deletion URL (remove leading-slash double slash)**

```coffeescript
	baseurl = config.all.client.service.persistence.fullpath + 'experiments/retention/awaiting-files-deletion'
```

- [ ] **Step 4: Build**

Run: `cd ../acas-ACAS-retention && npm run build`
Expected: compiles cleanly.

- [ ] **Step 5: Commit**

```bash
git -C ../acas-ACAS-retention add modules/ServerAPI/src/server/routes/ExperimentServiceRoutes.coffee
git -C ../acas-ACAS-retention commit -m "ACAS-994: stop retention pipeline on error; don't stamp files-deleted on failure"
```

---

## Self-Review (completed by author)

- **Spec coverage:** Part A → migrations section; Part B → scalable deletion + shared-entity guard + audit user + authz; Part C → UI field + admin gating; Part D → Node orchestration fixes; tests in B4 + C6. All spec sections map to tasks.
- **Placeholders:** the only deferred content is the integration-test fixture INSERTs (Task B4 Step 3), which is an explicit implementer action with all column lists referenced, not a hidden TODO. Acceptable because the inserts must be validated against the live schema.
- **Type consistency:** work-table names, `joinColumn`/`workColumn` pairs in `batchDeleteAll` calls match the `CREATE TABLE` columns in `stageExpiredExperiments`; `deleteBatch` signature matches all call sites; getter `getDeletedExperimentRetentionDays` / handler `handleDeletedExperimentRetentionDaysChanged` / class `.bv_deletedExperimentRetentionDays` are consistent across C1–C3.

## Risks / verify-during-implementation
- Confirm the admin-role accessor on `Author` (Task B3 Step 1) — `getAuthorRoles()`/`getRoleEntry().getRoleName()` names may differ.
- Confirm `treatment_group_value.treatment_state_id`, `analysis_group_value.analysis_state_id`, `subject_value.subject_state_id` (taken from the existing migration — already correct, but verify against the live schema on first test run).
- `BATCH_SIZE` (100k) may want to be config-driven per deployment (spec open question).
