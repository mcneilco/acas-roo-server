# ACAS-994 — Experiment Retention Purge: manual end-to-end test

Reproducible transcript of validating the all-in-roo retention purge on a local docker-compose
stack against the real ACAS schema. Branch `ACAS-994-retention-policy` in both `acas-roo-server`
and `acas`.

## 1. Build the roo image with the retention changes

```bash
cd acas-roo-server          # on branch ACAS-994-retention-policy
docker build --build-arg CHEMISTRY_PACKAGE=indigo \
  -t mcneilco/acas-roo-server-oss:latest-indigo -f Dockerfile-multistage .
```

## 2. Bring up the stack

```bash
cd ../acas                  # on branch ACAS-994-retention-policy (dev mode bind-mounts the source)
docker compose up -d
# roo applies Flyway migration V2.4.3.1 (experiment retention value kinds) on startup.
```

Folder deletion reuses config roo already gets from acas: `server.service.persistence.filePath`
(`/home/runner/build/privateUploads`, mounted into roo via `volumes_from: acas`) and
`server.nodeapi.path` (`http://acas:3001`, the internal API serving `folders-for-codes`). No extra
setup is needed.

> Config keys (documented with defaults in acas `conf/config.properties.example`, enabled by default):
> `server.experiment.retention.enabled`, `server.experiment.retention.cron` (default `0 0 5 * * *`,
> interpreted in `server.experiment.retention.cronZone`, default **UTC**),
> `server.experiment.retention.batchSize` (default 100000). The scheduled purge is off by default;
> this test triggers it on demand via the admin endpoint.

## 3. Create the test user (bob, admin)

```bash
for ep in getOrCreateACASBob getOrCreateGlobalProject getOrCreateGlobalProjectRole \
          giveBobRoles syncRoles; do
  curl -s -o /dev/null -w "$ep -> %{http_code}\n" "http://localhost:3001/api/systemTest/$ep"
done
```

The `mcneilco/acas-postgres` image is seeded with sample protocols/experiments, which the rest of
this test uses. (To load fresh experiments instead, use `acasclient.experiment_loader(Path(sel_csv),
'bob', False)` — requires registering any compounds the SEL file references.)

## 4. Per-protocol retention is honored

Pick two seeded experiments under two protocols (both already soft-deleted in the seed data) and
give the protocols different retention windows. PROT-8/EXPT-29 and PROT-9/EXPT-30 here. Find each
protocol's `metadata` state id, then insert the retention value:

```sql
-- protocol metadata state ids (PROT-8 -> 3540, PROT-9 -> 4697 in the seed image)
INSERT INTO protocol_value
  (id, deleted, ignored, ls_kind, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, protocol_state_id, numeric_value)
VALUES
  (nextval('value_pkseq'), false, false, 'deleted experiment retention days', 'numericValue', 'numericValue_deleted experiment retention days', false, 'bob', now(), 3540, 1),   -- PROT-8 = 1 day
  (nextval('value_pkseq'), false, false, 'deleted experiment retention days', 'numericValue', 'numericValue_deleted experiment retention days', false, 'bob', now(), 4697, 100); -- PROT-9 = 100 days
```

Trigger the purge (admin-gated; bob has the admin role):

```bash
curl -s -u bob:secret -X POST http://localhost:8080/acas/api/v1/experiments/retention/run
# -> ["EXPT-00000029"]      (PROT-8 1-day: expired; PROT-9 100-day: NOT expired)
```

Result observed:
- `["EXPT-00000029"]` purged; `EXPT-00000030` (100-day) and experiments under protocols with **no**
  retention value untouched.
- `analysis_group_value` total dropped 2077 -> 1021 (= EXPT-29's 1056 child values cascaded).

## 5. Crash-resume path

If the file step can't complete (acas unreachable, or the pod is killed mid-run), the child data is
already deleted but the experiment shell + persistent `retention_work_*` tables remain. The next run
**resumes from those work tables** and completes — observed when the file step deferred on one run and
the next run finished it cleanly:

```bash
curl -s -u bob:secret -X POST http://localhost:8080/acas/api/v1/experiments/retention/run
# -> ["EXPT-00000029"]   (shell deleted, work tables dropped on this resumed run)
```

roo log (resume completing the cascade):

```
itx_expt_expt: finished, 0 rows deleted.
experiment_value: finished, 77 rows deleted.
experiment_label: finished, 1 rows deleted.
experiment_state: finished, 11 rows deleted.
experiment: finished, 1 rows deleted.
Retention: purged 1 experiments: [EXPT-00000029]
```

## 6. Scale test — chunked, per-batch-committed delete at 20,000,000 child rows

Inflate one experiment's analysis-group values to 20M by generating rows attached to one of its
analysis states (new ids via the sequence; reuse a real `ls_type_and_kind` to satisfy the
`agv_tk_fk` FK). EXPT-38 / PROT-12 (sole experiment on its protocol, analysis_state 4823):

```sql
INSERT INTO analysis_group_value
  (id, deleted, ignored, ls_kind, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, analysis_state_id, numeric_value)
SELECT nextval('value_pkseq'), false, false, 'In vitro Clint', 'numericValue',
       'numericValue_In vitro Clint', false, 'bob', now(), 4823, (gs % 100)::numeric
FROM generate_series(1, 20000000) gs;
-- INSERT 0 20000000   (~9m44s to load)
```

> For a "copy the real rows N times" variant: `SELECT nextval('value_pkseq'), agv.<cols...> FROM
> analysis_group_value agv JOIN ... JOIN experiment e ... CROSS JOIN generate_series(1, N) WHERE
> e.code_name = '<expt>'` (CROSS JOIN must come before WHERE).

Make EXPT-38 expired and purge:

```sql
-- PROT-12 metadata protocol_state id = 4815
INSERT INTO protocol_value
  (id, deleted, ignored, ls_kind, ls_type, ls_type_and_kind, public_data, recorded_by, recorded_date, protocol_state_id, numeric_value)
VALUES (nextval('value_pkseq'), false, false, 'deleted experiment retention days', 'numericValue',
        'numericValue_deleted experiment retention days', false, 'bob', now(), 4815, 1);
```

```bash
time curl -s -u bob:secret -X POST http://localhost:8080/acas/api/v1/experiments/retention/run
# -> ["EXPT-00000038"]   (HTTP 200 in ~41s for 20,000,003 child rows)
```

roo log — the chunked per-batch-commit delete (BATCH_SIZE = 100000), **200 committed batches**:

```
analysis_group_value: deleted 100000 this batch (running total   100000)
... (200 batches of 100k, several per second) ...
analysis_group_value: deleted 100000 this batch (running total 20000000)
analysis_group_value: deleted      3 this batch (running total 20000003)
analysis_group_value: finished, 20000003 rows deleted.
Retention: purged 1 experiments: [EXPT-00000038]
```

Verified: EXPT-38 gone; global `analysis_group_value` 20,000,992 -> 989; no leftover
`retention_work_*` tables. **20M child rows deleted in ~41s in 200 bounded, per-batch-committed
transactions** — never a single giant transaction. This is the behavior the large-scale (113M-row)
production scenario requires; at 113M it would be ~1130 batches on the same bounded footprint.

## 7. Idempotency

```bash
curl -s -u bob:secret -X POST http://localhost:8080/acas/api/v1/experiments/retention/run
# -> []     (nothing left expired)
```

## Issues found during this test (and fixed)

- **Flyway version collision** — the retention value-kinds migration was `V2.4.3.0`, which master
  already uses; roo refused to start. Renamed to `V2.4.3.1`. (Only the real stack surfaced this.)
- **Partial state if the file step can't run** — if acas is unreachable (or the persistence path /
  nodeapi URL are unset), the file step defers: child data is deleted but the experiment shell +
  work tables are left for the next run to resume (recoverable). Folder deletion reuses the existing
  `server.service.persistence.filePath` + `server.nodeapi.path`.

## Cleanup

```bash
cd acas && docker compose down -v
```
