# Retention delete-logic test

`retention_delete_logic_test.sql` validates the experiment-retention deletion logic
(`ExperimentRetentionServiceImpl`) against a real PostgreSQL instance, independent of the
full roo Spring context. It builds a minimal schema, inserts an expired experiment, a live
experiment, an analysis group owned only by the expired experiment, and an analysis group
**shared** between both, then runs the verbatim staging + cascade-delete SQL and asserts:

- only the expired experiment is staged (the live one is not);
- the shared-entity guard keeps the shared analysis group (and its value);
- the expired experiment, its label, and its exclusively-owned analysis group + value are purged;
- the live experiment is untouched.

Run it:

```bash
docker run -d --name rtest -e POSTGRES_PASSWORD=test -e POSTGRES_DB=acas postgres:16
until docker exec rtest pg_isready -U postgres; do sleep 1; done
docker exec -i rtest psql -v ON_ERROR_STOP=1 -U postgres -d acas < retention_delete_logic_test.sql
docker rm -f rtest
```

A non-zero exit (or any `ERROR:`) means an assertion failed. Both `... ASSERTIONS PASSED`
notices must print.

The full end-to-end behavior (the `@Scheduled` trigger, the `pg_try_advisory_lock` single-runner
across pods, and the roo→acas folder-path resolution + filesystem deletion) must be validated in a
deployed stack, since it requires the complete roo application context, a provisioned database, and
the acas node service.
