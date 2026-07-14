package com.labsynch.labseer.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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
