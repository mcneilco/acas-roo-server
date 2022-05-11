package com.labsynch.labseer.db.migration.postgres;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class V2_3_0_8__update_stereo_category_defaults implements SpringJdbcMigration {

	Logger logger = LoggerFactory.getLogger(V2_3_0_8__update_stereo_category_defaults.class);

	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		// We only want to do this migration if this is a new install
		// Check to see if there are any parents registered
		String parentCountSQL = "select count(*) from parent";
		int parentCount = jdbcTemplate.queryForObject(parentCountSQL, Integer.class);

		if (parentCount == 0) {
			String[] defaultStereoCategoriesStrings = new String[] {
					"No stereochemistry",
					"Single stereoisomer",
					"Single stereoisomer - arbitrary assign",
					"Single stereoisomer - partial assign",
					"Mixture",
					"Mixture - arbitrary assign",
					"Mixture - partial assign",
					"Relative mixture",
					"Relative mixture - arbitrary assign",
					"Unknown"
			};

			String delete = "DELETE FROM stereo_category";
			jdbcTemplate.update(delete);
			for (String defaultStereoCategoryString : defaultStereoCategoriesStrings) {
				String insert = "INSERT INTO stereo_category"
						+ " (id, code, name, version) VALUES "
						+ "((SELECT nextval('hibernate_sequence')), "
						+ "'" + defaultStereoCategoryString + "', "
						+ "'" + defaultStereoCategoryString + "', "
						+ " 0)";
				jdbcTemplate.update(insert);
			}
		} else {
			logger.info("Not updating stereo category defaults because there are already rows in the parent table");
		}
	}
}
