package com.labsynch.labseer.db.migration.postgres.jchem;

import java.sql.Connection;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chemaxon.util.ConnectionHandler;
import chemaxon.jchem.db.Updater;

public class V2_4_0_6__Add_jchem_migration_to_22_12_0 implements JdbcMigration {

	Logger logger = LoggerFactory.getLogger(V2_4_0_6__Add_jchem_migration_to_22_12_0.class);

	// Updating jchem version to 22.12.0

	public void migrate(Connection conn) throws Exception {
		logger.info("Preparing to upgrade jchem version to 22.12.0");
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
				logger.info("Is upgrade required: " + ui.isStructuralChange);
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