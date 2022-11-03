package com.labsynch.labseer.db.migration.postgres.jchem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chemaxon.jchem.db.StructureTableOptions;
import chemaxon.jchem.db.UpdateHandler;
import chemaxon.util.ConnectionHandler;

import chemaxon.jchem.db.UpdateHandlerException;
import chemaxon.jchem.db.Updater;

public class V1_3_0_2__Add_standardization_jchem_tables implements JdbcMigration {

	Logger logger = LoggerFactory.getLogger(V1_3_0_2__Add_standardization_jchem_tables.class);

	// Updating jchem version

	public void migrate(Connection conn) throws Exception {
		logger.info("creating standardization_dry_run_structure table");
		conn.setAutoCommit(true);
		logger.info("connection autocommit mode: " + conn.getAutoCommit());
		logger.info("getTransactionIsolation  " + conn.getTransactionIsolation());

		ConnectionHandler ch = new ConnectionHandler();
		ch.setConnection(conn);		

		try {
			Updater ud = new Updater(ch);
			Updater.UpdateInfo ui = null;
			String message = "";
			while ((ui = ud.getNextUpdateInfo()) != null) {
				logger.info("\n" + ui.processingMessage + "\n");
				logger.info("Is structure change required: " + ui.isStructuralChange);
				message = ud.performCurrentUpdate();
				logger.info(message);
			}
	
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}		
		
		conn.setAutoCommit(false);
		logger.info("connection autocommit mode: " + conn.getAutoCommit());

	}
}