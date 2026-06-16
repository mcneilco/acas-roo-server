# Experiment Retention Policy — Design (combined: UI + scalable deletion)

- **Jira:** ACAS-994
- **Date:** 2026-06-16
- **Repos:** `acas` (Node/CoffeeScript frontend), `acas-roo-server` (Java/Spring backend)
- **Branches:** `ACAS-994-retention-policy` in both repos

## 1. Background & Problem

Experiments in ACAS are **soft-deleted** — marking an experiment "deleted" or "overwritten"
flags rows (`ignored = true` / `deleted = true`) but leaves all data in the database.

Some protocols (e.g. inventory tracking) have their experiments **reloaded wholesale** on a
recurring basis, so each reload leaves a full soft-deleted copy behind. Over time this causes
serious database bloat. A real cleanup run encountered **~2,300 soft-deleted experiments with
~113 million `analysis_group_value` rows beneath them.**

A first implementation of a retention feature exists (recovered from local history; see
ACAS-994 history) but has two classes of problems:

1. **It does not scale.** The deletion runs as a single giant transaction with multi-join
   `DELETE ... USING` statements. At production scale a plain-SQL equivalent stalled; the only
   thing that worked operationally was **chunked deletes with per-batch commits** (see the
   reference script in section 5).
2. **Safety gaps.** Destructive logic ships inside auto-running migrations, shared child
   entities can be deleted out from under live experiments, audit values are stamped with a
   hardcoded username, the destructive endpoint is not role-gated, and the Node cleanup
   pipeline does not stop on error. There is also **no UI** for setting the policy.

This spec covers the complete corrected feature: a per-protocol policy UI, a scalable deletion
engine, and the safety fixes that are cheap to do in the same rewrite.

## 2. Goals / Non-Goals

**Goals**
- Admins can set a per-protocol retention period (days) from the existing protocol screen.
- Soft-deleted experiments past their protocol's retention period are purged reliably at any
  data volume, without stalling or unbounded transactions.
- Purging never damages a non-expired ("live") experiment.
- Only authorized (admin) callers can trigger a purge.
- Only one migration ships (registers value kinds); no migration ever performs deletions.

**Non-Goals (this iteration)**
- No global/default or per-project policy (per-protocol only).
- No UI to trigger deletion or preview impact (policy-setting only; deletion stays driven by
  the existing job/cron caller).
- No change to the soft-delete behavior itself.

## 3. Part A — UI: inline protocol retention-days field

### Behavior
- Add a numeric field **"Deleted experiment retention (days)"** to the protocol editor.
- **Per-protocol**, stored as the existing protocol value
  `numericValue` / `deleted experiment retention days` (already defined in `ProtocolConfJSON`).
- **Admins edit; everyone else sees it read-only.**
- **Blank/unset = keep indefinitely.** The purge query only targets protocols that *have* a
  retention value, so clearing the field is the documented "disable retention" action.
- Validation: non-negative integer; reject negative/non-numeric using the form's existing
  validation styling.

### Files (acas frontend)
- `modules/ServerAPI/src/client/Protocol.coffee`
  - **Model `Protocol`**: add getter `getDeletedExperimentRetentionDays()` →
    `getOrCreateValueByTypeAndKind("metadata","protocol metadata","numericValue","deleted experiment retention days")`
    (mirrors `getAssayStage`, `getCurveDisplayMin`, etc.).
  - **`ProtocolBaseController`**: add `"keyup .bv_deletedExperimentRetentionDays"` to `events`;
    add `handleDeletedExperimentRetentionDaysChanged` calling
    `@handleValueChanged "DeletedExperimentRetentionDays", value`; in `render`, populate the
    input from the getter and apply admin gating (disable input when the user lacks the admin
    role).
- `modules/ServerAPI/src/client/Protocol.html` (`#ProtocolBaseView`): add the labeled numeric
  input `.bv_deletedExperimentRetentionDays` in the protocol-metadata area, with help text
  ("Blank = keep indefinitely").

### Admin gating (frontend)
Follow the existing `ProtocolBrowser` pattern:
`UtilityFunctions::testUserHasRoleTypeKindName(window.AppLaunchParams.loginUser, …)` with the
admin role from `window.conf.roles.acas.adminRole`. Non-admins get the field `disabled`. This
is a UI affordance only — real enforcement is the backend authorization in Part B.

### Data flow
Render → getter populates field. Admin edits → `keyup` handler → `handleValueChanged` updates
the value model and marks the form dirty → existing Save → Backbone `PUT /api/protocols/:id`
persists the nested `lsValue`. **No new frontend routes; no backend persistence changes** — the
value kind and protocol-save path already exist.

## 4. Part B — Scalable deletion engine (Java, Approach C: hybrid)

Replace the single-transaction `ExperimentRetentionServiceImpl` purge with a hybrid engine:
**Java orchestrates, stages the target set, and enforces safety; deletes run in small,
per-batch-committed chunks.**

### 4.1 Target-set staging (one transaction, read-only-ish)
Build work tables of exactly what will be removed:
- Expired experiments: protocol has a retention value, experiment status is `deleted` or
  `overwritten`, the status `recorded_date` is older than `now() - retention_days`, and the
  experiment is **not already** marked `database deleted date` (idempotency).
- Orphan parents to remove: analysis groups / treatment groups / subjects reachable from the
  expired experiments **(see 4.3 shared-entity guard)**.

### 4.2 Chunked, per-batch-committed deletes
- Each table is deleted **children-before-parents** in dependency order:
  `*_value` → `*_state` → linker tables (`treatmentgroup_subject`,
  `analysisgroup_treatmentgroup`, `experiment_analysisgroup`) → main tables
  (`subject`, `treatment_group`, `analysis_group`) → `experiment_label`.
- Deletion loops in **fixed-size batches (configurable, default 500k)**, each batch in its
  **own `REQUIRES_NEW` transaction** that commits before the next batch. The outer orchestration
  method is **NOT `@Transactional`** (Spring cannot commit mid-method).
- Batches use the **`ctid` trick** so batching works even when the join key is not unique
  (e.g. `experiment_analysisgroup.analysis_group_id`):
  ```sql
  DELETE FROM <table> WHERE ctid IN (
      SELECT t.ctid FROM <table> t JOIN <work_table> w ON t.<key> = w.<key> LIMIT :batch
  )
  ```
  Loop until a batch deletes 0 rows.
- Ensure indexes exist on the work-table join keys so each batch does not seq-scan.
- Log per-batch and running-total row counts.

### 4.3 Shared-entity guard (correctness)
Before adding an analysis group / treatment group / subject to the orphan work set, verify it
is **not also linked to a non-expired experiment**. Concretely, exclude any
`analysis_group` whose `experiment_analysisgroup` rows reference an experiment not in the
expired set (and likewise down the chain). This prevents purging data still owned by a live
experiment.

### 4.4 Phasing (unchanged model, made safe)
Three idempotent phases, each separately callable:
1. **Database delete** (`hardDeleteExpiredExperiments`): chunk-delete child/orphan data, then
   stamp each experiment with a `database deleted date` value. Experiment shell + status value
   are kept so files can still be located.
2. **Files delete** (Node `awaiting-files-deletion` + folder removal): for experiments having a
   `database deleted date` but no `files deleted date`, remove the on-disk folder and **only on
   success** stamp `files deleted date`.
3. **Complete** (`completeExperimentDeletion`): for experiments having **both** dates,
   chunk-delete the remaining `experiment_value` / `experiment_state` / `experiment` (and any
   outstanding FKs to `experiment`, e.g. experiment-experiment interaction tables — enumerate
   and include so the final delete cannot fail on an unhandled FK).

### 4.5 Authorization (backend)
The destructive endpoints (`/api/v1/experiments/retention/*` POSTs) require the ACAS admin role
(`@PreAuthorize` or equivalent in this codebase). The Node proxy additionally checks the admin
role before forwarding.

### 4.6 Audit user
Stamp `database deleted date` (and related audit values / `ls_transaction.recorded_by`) with a
**system/service user** (or the authenticated principal), never a hardcoded developer username.

## 5. Migrations

- **Keep** `V2.4.3.0__experiment_retention_value_kinds.sql` — registers the `value_kind` rows
  (`deleted experiment retention days`, `database deleted date`, `files deleted date`). Required
  reference data; idempotent.
- **Delete** `V2.4.3.1__protocol_retention_policy.sql` — it seeds a hardcoded 7-day policy on
  protocols named "Vial Inventory." That bootstrap is replaced by the UI; it should not ship.
- **Delete** `V2.4.3.2__experiment_retention_hard_delete.sql` — a `DO` block that executes the
  full hard-delete at migration time. Deletion must never be a side effect of applying a
  migration; the service is the runtime path.

**Reference (the operationally-proven chunking approach):** a `PROCEDURE` (not a `DO` block, so
it can `COMMIT` mid-loop) deleting 500k rows per batch via the `ctid`-in-`LIMIT` pattern, with
indexes on join keys, children-before-parents. Part B reproduces this technique with Java
driving per-batch `REQUIRES_NEW` transactions.

## 6. Node orchestration fixes (acas)

In `ExperimentServiceRoutes.coffee`:
- The combined `deleteExpiredExperiments` pipeline must **short-circuit on any phase error**
  (the current fake-`resp` objects fire the success continuation even on HTTP 500).
- The folder handler must **not** set `files deleted date` when folder removal failed.
- Normalize the proxy URL (avoid the double slash in the `awaiting-files-deletion` call) and
  validate that derived folder paths stay within the configured data root.

## 7. Testing

- **Replace the broken unit test** (`ExperimentRetentionServiceImplTest` mocks `JdbcTemplate`
  but the service uses `EntityManager`, and asserts an `int` against a `List<String>` — it does
  not compile).
- **DB-backed integration test** (Testcontainers/Postgres) covering:
  - expiry selection (respects per-protocol days; ignores protocols with no retention value),
  - idempotency (re-running does not re-process already-stamped experiments),
  - **shared-entity safety** — an analysis group shared with a live experiment is NOT deleted,
  - **chunking** — a dataset larger than one batch is fully removed across multiple batches,
  - FK completeness — an expired experiment with interactions deletes cleanly in the complete
    phase.
- **Manual UI check:** as admin, set/clear retention days, save, reload, confirm persisted; as
  non-admin, confirm the field is read-only.

## 8. Out of Scope
Global/default and per-project policies; UI-triggered deletion or impact preview; changes to
soft-delete semantics; dry-run mode (worth a future iteration given the destructiveness).

## 9. Open Questions / Risks
- Confirm the exact admin authorization mechanism used elsewhere in `acas-roo-server`
  controllers (annotation vs. manual role check) so Part B matches convention.
- Confirm the full set of FK tables referencing `experiment` (interaction tables, etc.) for the
  complete phase.
- Batch size (default 500k) may need tuning per deployment; expose as config.
