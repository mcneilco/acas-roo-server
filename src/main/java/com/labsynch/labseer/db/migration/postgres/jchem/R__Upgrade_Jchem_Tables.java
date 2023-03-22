package com.labsynch.labseer.db.migration.postgres.jchem;

import java.sql.Connection;
import java.sql.SQLException;

import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.MigrationInfoProvider;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chemaxon.version.VersionInfo;

import chemaxon.jchem.db.UpdateHandlerException;
import chemaxon.jchem.db.Updater;
import chemaxon.util.ConnectionHandler;

public class R__Upgrade_Jchem_Tables implements JdbcMigration, MigrationInfoProvider, MigrationChecksumProvider {

	private static int count;

	Logger logger = LoggerFactory.getLogger(V2_4_0_8__Upgrade_Jchem_Tables.class);

	public void migrate(Connection conn) throws Exception {
		logger.info("ATTEMPTING TO UPGRADE JCHEM TABLES");
		conn.setAutoCommit(true);
		logger.info("connection autocommit mode: " + conn.getAutoCommit());
		logger.info("getTransactionIsolation  " + conn.getTransactionIsolation());

		recalculateJChemTable(conn);

		conn.setAutoCommit(false);
		logger.info("connection autocommit mode: " + conn.getAutoCommit());

	}

	// Example of implementation used for Repeated Migrations
	// https://github.com/flyway/flyway/issues/1814

    @Override
    public MigrationVersion getVersion() {
        return null;
    }

    @Override
    public String getDescription() {
        return "R__Upgrade_Jchem_Tables";
    }

	// 	Add a public Integer getChecksum() method which gets the jchem version as a string and runs:
	// jchemVersion.hashCode() to generate an integer
	// 
	// 	https://apidocs.chemaxon.com/jchem/doc/dev/java/api/com/chemaxon/version/VersionInfo.html
	//
	//  static String	getVersion()	Returns the product version.

    @Override
    public Integer getChecksum() {
		String jchemVersion = VersionInfo.getVersion();
		System.out.println("R__Upgrade_Jchem_Tables is checking jChem Version:" + jchemVersion);
        return jchemVersion.hashCode();
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
