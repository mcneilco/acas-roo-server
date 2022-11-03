package com.labsynch.labseer.db.migration.postgres.jchem;

import java.sql.Connection;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;

import chemaxon.util.ConnectionHandler;
import chemaxon.jchem.db.Updater;
import chemaxon.jchem.db.UpdateHandlerException;

public class V2_4_0_6__Upgrade_Jchem_Tables implements JdbcMigration {

	Logger logger = LoggerFactory.getLogger(V2_4_0_6__Upgrade_Jchem_Tables.class);

	// Updating jchem version to 22.12.0

	public void migrate(Connection conn) throws Exception {
		logger.info("ATTEMPTING TO UPGRADE JCHEM TABLES for jchem version to 22.12.0");
		conn.setAutoCommit(true);
		logger.info("connection autocommit mode: " + conn.getAutoCommit());
		logger.info("getTransactionIsolation  " + conn.getTransactionIsolation());

		recalculateJChemTable(conn);

		conn.setAutoCommit(false);
		logger.info("connection autocommit mode: " + conn.getAutoCommit());

	}

	private boolean recalculateJChemTable(Connection conn) throws UpdateHandlerException, SQLException {
		ConnectionHandler ch = new ConnectionHandler();
		ch.setConnection(conn);

		String message = "";
		Updater ud = new Updater(ch);
		Updater.UpdateInfo ui = null;
		while ((ui = ud.getNextUpdateInfo()) != null) {
			logger.info("\n" + ui.processingMessage + "\n");
			logger.info("Is structure change required: " + ui.isStructuralChange);
			message = ud.performCurrentUpdate();
			logger.info(message);
		}

		logger.info("updated the Jchem structure tables ");

		return false;
	}

}