package com.labsynch.labseer.db.migration.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class V2_0_1_5__sync_sequences implements SpringJdbcMigration {

    Logger logger = LoggerFactory.getLogger(V2_0_1_5__sync_sequences.class);

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        logger.info("Starting sequence synchronization migration...");
        
        // List of tables that were populated using hibernate_sequence in V2_0_1_4__set_defaults
        // and now have individual sequences that need to be synchronized
        String[] tables = {
            "physical_state",
            "stereo_category", 
            "isotope",
            "operator",
            "unit",
            "file_type",
            "purity_measured_by",
            "pre_def_corp_name"
        };
        
        for (String tableName : tables) {
            syncSequenceForTable(jdbcTemplate, tableName);
        }
        
        logger.info("Sequence synchronization migration completed.");
    }

    private void syncSequenceForTable(JdbcTemplate jdbcTemplate, String tableName) {
        try {
            // First, check if table exists and has data
            String checkTableSql = "SELECT COUNT(*) FROM " + tableName;
            Long count = jdbcTemplate.queryForObject(checkTableSql, Long.class);
            
            if (count == null || count == 0) {
                logger.info("Table {} is empty, skipping sequence sync", tableName);
                return;
            }
            
            // Get the current maximum ID in the table
            String maxIdSql = "SELECT COALESCE(MAX(id), 0) FROM " + tableName;
            Long maxId = jdbcTemplate.queryForObject(maxIdSql, Long.class);
            
            if (maxId == null) {
                maxId = 0L;
            }
            
            logger.info("Table {}: found {} records, max ID is {}", tableName, count, maxId);
            
            // Find the sequence name for this table
            String findSequenceSql = "SELECT pg_get_serial_sequence(?, 'id')";
            String sequenceName = jdbcTemplate.queryForObject(findSequenceSql, String.class, tableName);
            
            if (sequenceName != null) {
                // Get current sequence value
                String getCurrentSeqSql = "SELECT last_value FROM " + sequenceName;
                Long currentSeqValue = jdbcTemplate.queryForObject(getCurrentSeqSql, Long.class);
                
                logger.info("Table {}: sequence {} current value is {}", tableName, sequenceName, currentSeqValue);
                
                if (currentSeqValue == null || currentSeqValue <= maxId) {
                    // Set sequence to max(id) + 1
                    Long nextSeqValue = maxId + 1;
                    String setSequenceSql = "SELECT setval(?, ?, false)";
                    jdbcTemplate.queryForObject(setSequenceSql, Long.class, sequenceName, nextSeqValue);
                    
                    logger.info("Table {}: synchronized sequence {} to start at {}", tableName, sequenceName, nextSeqValue);
                } else {
                    logger.info("Table {}: sequence {} is already ahead of max ID, no sync needed", tableName, sequenceName);
                }
            } else {
                // Try common sequence naming patterns
                String[] possibleSequenceNames = {
                    tableName + "_id_seq",
                    tableName + "_seq", 
                    tableName + "_pkey_seq"
                };
                
                boolean found = false;
                for (String seqName : possibleSequenceNames) {
                    try {
                        String checkSeqSql = "SELECT 1 FROM information_schema.sequences WHERE sequence_name = ?";
                        List<Integer> results = jdbcTemplate.query(checkSeqSql, new RowMapper<Integer>() {
                            @Override
                            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                                return rs.getInt(1);
                            }
                        }, seqName);
                        
                        if (!results.isEmpty()) {
                            // Get current sequence value
                            String getCurrentSeqSql = "SELECT last_value FROM " + seqName;
                            Long currentSeqValue = jdbcTemplate.queryForObject(getCurrentSeqSql, Long.class);
                            
                            logger.info("Table {}: found sequence {} with current value {}", tableName, seqName, currentSeqValue);
                            
                            if (currentSeqValue == null || currentSeqValue <= maxId) {
                                // Set sequence to max(id) + 1
                                Long nextSeqValue = maxId + 1;
                                String setSequenceSql = "SELECT setval(?, ?, false)";
                                jdbcTemplate.queryForObject(setSequenceSql, Long.class, seqName, nextSeqValue);
                                
                                logger.info("Table {}: synchronized sequence {} to start at {}", tableName, seqName, nextSeqValue);
                            } else {
                                logger.info("Table {}: sequence {} is already ahead of max ID, no sync needed", tableName, seqName);
                            }
                            found = true;
                            break;
                        }
                    } catch (Exception e) {
                        // Continue trying other sequence names
                        logger.debug("Sequence {} not found for table {}", seqName, tableName);
                    }
                }
                
                if (!found) {
                    logger.warn("No sequence found for table {}. This table might not use auto-generated IDs.", tableName);
                }
            }
            
        } catch (Exception e) {
            logger.error("Error synchronizing sequence for table {}: {}", tableName, e.getMessage(), e);
            // Don't throw - continue with other tables
        }
    }
}