package com.labsynch.labseer.db.migration.postgres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.transaction.Transactional;

import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.labsynch.labseer.domain.StandardizationHistory;
import com.labsynch.labseer.dto.configuration.MainConfigDTO;
import com.labsynch.labseer.dto.configuration.StandardizerSettingsConfigDTO;
import com.labsynch.labseer.utils.Configuration;

public class R__check_for_standardizer_configuration_changes implements SpringJdbcMigration, MigrationChecksumProvider {

	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();
	StandardizerSettingsConfigDTO standardizerConfigs = mainConfig.getStandardizerSettings();

	Logger logger = LoggerFactory.getLogger(R__check_for_standardizer_configuration_changes.class);
	//standardizer
	@Transactional
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		jdbcTemplate.setMaxRows(1);
		String selectStandardizationSettingsSQL = "SELECT * FROM standardization_settings order by id desc";
		String selectCountParentSQL = "SELECT count(id) FROM parent";
		StandardizationHistory standardizationHistory = new StandardizationHistory();
		standardizationHistory.setSettingsHash(standardizerConfigs.hashCode());
		standardizationHistory.setSettings(standardizerConfigs.toJson());
		standardizationHistory.setRecordedDate(new Date());
		standardizationHistory.save(jdbcTemplate);
		try{
			@SuppressWarnings("unchecked")
			StandardizationSettings standardizationSettings = (StandardizationSettings)jdbcTemplate.queryForObject(selectStandardizationSettingsSQL, new StandardizationSettingsRowMapper());
			logger.debug("Standardizer configs have changed, marking 'standardization needed' according to configs as " +standardizerConfigs.getShouldStandardize());
			standardizationSettings.setNeedsStandardization(standardizerConfigs.getShouldStandardize());
			standardizationSettings.setModifiedDate(new Date());
			standardizationSettings.save(jdbcTemplate);
		}catch(EmptyResultDataAccessException e){
			logger.debug("No standardization settings found in database");
			StandardizationSettings standardizationSettings = new StandardizationSettings();
			standardizationSettings.setModifiedDate(new Date());
			int numberOfParents = jdbcTemplate.queryForObject(selectCountParentSQL, Integer.class);
			if(numberOfParents == 0) {
				logger.debug("There are no parents registered, we can assume stanardization configs match the database standardization state so storing configs as the stanardization_settings");
				standardizationSettings.setNeedsStandardization(false);
				standardizationSettings.save(jdbcTemplate);
			} else {
				if(standardizerConfigs.getShouldStandardize() == false) {
					logger.warn("Standardization is turned off so marking the database as not requiring standardization at this time");
					standardizationSettings.setNeedsStandardization(false);
					standardizationSettings.save(jdbcTemplate);
				} else {
					logger.warn("Standardization is turned on but the database has not been stanardized so we don't know the current database stanardization state, stanardization_settings will reflect the unknown state");
					standardizationSettings.setNeedsStandardization(true);
					standardizationSettings.save(jdbcTemplate);
				}
			}
		}
	}

	private class StandardizationSettings {

	    private Long id;
	    private Long version;
	    private Date modifiedDate;
	    private Boolean needsStandardization;


	    public Long getId() {
	        return this.id;
	    }

		public void setId(Long id) {
	        this.id = id;
	    }

	    public Long getVersion() {
	        return this.version;
	    }

	    public void setVersion(Long version) {
	        this.version = version;
	    }

	    public Date getModifiedDate() {
	        return this.modifiedDate;
	    }

	    public void setModifiedDate(Date modifiedDate) {
	        this.modifiedDate = modifiedDate;
	    }

	    public Boolean getNeedsStandardization() {
	        return this.needsStandardization;
	    }

	    public void setNeedsStandardization(Boolean needsStandardization) {
	        this.needsStandardization = needsStandardization;
	    }

	    public void save(JdbcTemplate jdbcTemplate) {
	    	if(this.getId() == null) {
				String insert = "INSERT INTO standardization_settings"
						+ " (id, version, modified_date, needs_standardization) VALUES "
						+ "((SELECT nextval('stndzn_settings_pkseq')), "
						+ ""+0+", "
						+ "'"+new java.sql.Timestamp(this.getModifiedDate().getTime())+"', "
						+ "'"+this.getNeedsStandardization()+"'"
						+ ")";
				jdbcTemplate.update(insert);

	    	} else {
				String update = "UPDATE standardization_settings "
						+ "set (version, modified_date, needs_standardization) = ("
						+ ""+(this.getVersion()+1)+", "
						+ "'"+new java.sql.Timestamp(this.getModifiedDate().getTime())+"', "
						+ "'"+this.getNeedsStandardization()+"' "
						+ ") WHERE id = "+this.getId();
				jdbcTemplate.update(update);
	    	}

		}

	}

	@SuppressWarnings("rawtypes")
	public class StandardizationSettingsRowMapper implements RowMapper
	{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			StandardizationSettings standardizationSettings = new StandardizationSettings();
			standardizationSettings.setId(rs.getLong("id"));
			standardizationSettings.setVersion(rs.getLong("version"));
			standardizationSettings.setNeedsStandardization(rs.getBoolean("needs_standardization"));
			return standardizationSettings;
		}
	}

	private class StandardizationHistory {

		   private Long id;

		    private Date recordedDate;

			private String settings;

			private int settingsHash;

			private String dryRunStatus;

			private Date dryRunStart;

			private Date dryRunComplete;

			private String standardizationStatus;

			private Date standardizationStart;

			private Date standardizationComplete;

			private int structuresStandardizedCount;

			private int newDuplicateCount;

			private int oldDuplicateCount;

			private int displayChangeCount;

			private int asDrawnDisplayChangeCount;

			private int changedStructureCount;

	    public Long getId() {
	        return this.id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public Date getRecordedDate() {
	        return this.recordedDate;
	    }

	    public void setRecordedDate(Date recordedDate) {
	        this.recordedDate = recordedDate;
	    }

	    public String getSettings() {
	        return this.settings;
	    }

	    public void setSettings(String settings) {
	        this.settings = settings;
	    }

	    public int getSettingsHash() {
	        return this.settingsHash;
	    }

	    public void setSettingsHash(int settingsHash) {
	        this.settingsHash = settingsHash;
	    }

	    public String getDryRunStatus() {
	        return this.dryRunStatus;
	    }

	    public void setDryRunStatus(String dryRunStatus) {
	        this.dryRunStatus = dryRunStatus;
	    }

	    public Date getDryRunStart() {
	        return this.dryRunStart;
	    }

	    public void setDryRunStart(Date dryRunStart) {
	        this.dryRunStart = dryRunStart;
	    }

	    public Date getDryRunComplete() {
	        return this.dryRunComplete;
	    }

	    public void setDryRunComplete(Date dryRunComplete) {
	        this.dryRunComplete = dryRunComplete;
	    }

	    public String getStandardizationStatus() {
	        return this.standardizationStatus;
	    }

	    public void setStandardizationStatus(String standardizationStatus) {
	        this.standardizationStatus = standardizationStatus;
	    }

	    public Date getStandardizationStart() {
	        return this.standardizationStart;
	    }

	    public void setStandardizationStart(Date standardizationStart) {
	        this.standardizationStart = standardizationStart;
	    }

	    public Date getStandardizationComplete() {
	        return this.standardizationComplete;
	    }

	    public void setStandardizationComplete(Date standardizationComplete) {
	        this.standardizationComplete = standardizationComplete;
	    }

	    public int getStructuresStandardizedCount() {
	        return this.structuresStandardizedCount;
	    }

	    public void setStructuresStandardizedCount(int structuresStandardizedCount) {
	        this.structuresStandardizedCount = structuresStandardizedCount;
	    }

	    public int getNewDuplicateCount() {
	        return this.newDuplicateCount;
	    }

	    public void setNewDuplicateCount(int newDuplicateCount) {
	        this.newDuplicateCount = newDuplicateCount;
	    }

	    public int getOldDuplicateCount() {
	        return this.oldDuplicateCount;
	    }

	    public void setOldDuplicateCount(int oldDuplicateCount) {
	        this.oldDuplicateCount = oldDuplicateCount;
	    }

	    public int getDisplayChangeCount() {
	        return this.displayChangeCount;
	    }

	    public void setDisplayChangeCount(int displayChangeCount) {
	        this.displayChangeCount = displayChangeCount;
	    }

	    public int getAsDrawnDisplayChangeCount() {
	        return this.asDrawnDisplayChangeCount;
	    }

	    public void setAsDrawnDisplayChangeCount(int asDrawnDisplayChangeCount) {
	        this.asDrawnDisplayChangeCount = asDrawnDisplayChangeCount;
	    }

	    public int getChangedStructureCount() {
	        return this.changedStructureCount;
	    }

	    public void setChangedStructureCount(int changedStructureCount) {
	        this.changedStructureCount = changedStructureCount;
	    }

	    public void save(JdbcTemplate jdbcTemplate) {
	    	if(this.getId() == null) {
				String insert = "INSERT INTO standardization_history"
						+ " (id, version, recorded_date, settings_hash, settings) VALUES "
						+ "((SELECT nextval('stndzn_hist_pkseq')), "
						+ ""+0+", "
						+ "'"+new java.sql.Timestamp(this.getRecordedDate().getTime())+"', "
						+ "'"+this.getSettingsHash()+"', "
						+ "'"+this.getSettings()+"'"
						+ ")";
				jdbcTemplate.update(insert);

	    	}
		}

	}

	@SuppressWarnings("rawtypes")
	public class StandardizationHistoryRowMapper implements RowMapper
	{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			StandardizationHistory standardizationHistory = new StandardizationHistory();
			standardizationHistory.setId(rs.getLong("id"));
			standardizationHistory.setSettingsHash(rs.getInt("settings_hash"));
			return standardizationHistory;
		}
	}

	@Override
	public Integer getChecksum() {
		int hash = standardizerConfigs.hashCode();
		logger.debug("standardizerSettings configuration hash:" + hash);

		return java.util.Objects.hash(hash);
	}

}