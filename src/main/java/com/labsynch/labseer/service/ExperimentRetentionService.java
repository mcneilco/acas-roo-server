package com.labsynch.labseer.service;

import java.util.List;

public interface ExperimentRetentionService {

    /**
     * Runs the full retention purge for expired experiments: deletes child data and the
     * experiment shell from the database (in per-batch-committed chunks) and deletes the
     * experiment folders from disk. Coordinated across pods by a Postgres advisory lock and
     * resumable after a crash via persistent work tables.
     *
     * @return the list of experiment codes purged in this run
     */
    List<String> purgeExpiredExperiments();
}
