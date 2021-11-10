package com.labsynch.labseer.db.migration.postgres.jchem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chemaxon.jchem.db.DatabaseProperties;
import chemaxon.jchem.db.StructureTableOptions;
import chemaxon.jchem.db.UpdateHandler;
import chemaxon.util.ConnectionHandler;

public class V2_0_0_7__Create_Jchem_Tables implements JdbcMigration {

	Logger logger = LoggerFactory.getLogger(V2_0_0_7__Create_Jchem_Tables.class);

	public void migrate(Connection conn) throws Exception {
		logger.info("ATTEMPTING TO CREATE JCHEM TABLES");
		conn.setAutoCommit(true);
		logger.info("conneciton autocommit mode: " + conn.getAutoCommit());
		logger.info("getTransactionIsolation  " + conn.getTransactionIsolation());
		
		createJchemPropertyTable(conn);
		createJChemTable(conn, "Salt_Structure", false);
		createJChemTable(conn, "SaltForm_Structure", true);
		createJChemTable(conn, "Parent_Structure", true);	
	
		conn.setAutoCommit(false);
		logger.info("conneciton autocommit mode: " + conn.getAutoCommit());
		
	}	
	
	private boolean createJchemPropertyTable(Connection conn) throws SQLException {
		ConnectionHandler ch = new ConnectionHandler();
		ch.setConnection(conn);

		boolean tableCreated = true;
		try{
			if (!DatabaseProperties.propertyTableExists(ch)){
				DatabaseProperties.createPropertyTable(ch);				
				logger.info("created the Jchem property table" );
			}
		} catch (SQLException e) {
			logger.error("SQL error - unable to create the Jchem property table: " + e );
			tableCreated = false;
			throw new SQLException("SQL error - unable to create the Jchem property table");
		}

		return tableCreated;
	}

	private boolean createJChemTable(Connection conn, String tableName, boolean tautomerDupe) throws SQLException {
		ConnectionHandler ch = new ConnectionHandler();
		ch.setConnection(conn);
		StructureTableOptions options = new StructureTableOptions(tableName, StructureTableOptions.TABLE_TYPE_MOLECULES);
		options.setTautomerDuplicateChecking(tautomerDupe);
		
		try {
			String[] tables = UpdateHandler.getStructureTables(ch);
			List<String> tableList = Arrays.asList(tables); 
			if (!tableList.contains(tableName)){
				UpdateHandler.createStructureTable(ch, options );
				logger.info("created the Jchem structure table " + tableName );
			}
		} catch (SQLException e) {
			logger.error("SQL error. Unable to create the Jchem structure table " + e + "  " + tableName );
			throw new SQLException("SQL error. Unable to create the Jchem structure table");
		}

		return false;
	}

}

