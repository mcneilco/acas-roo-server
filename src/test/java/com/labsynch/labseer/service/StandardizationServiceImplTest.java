package com.labsynch.labseer.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.Date;

import com.labsynch.labseer.domain.StandardizationHistory;
import com.labsynch.labseer.dto.configuration.StandardizerSettingsConfigDTO;

import org.junit.Test;

/**
 * Pure unit tests (no Spring context / DB) for the dry-run initialization snapshot logic.
 * Guards against the config-mismatch deadlock seen when the chemistry suite version changes
 * while an incomplete dry-run record is reused.
 */
public class StandardizationServiceImplTest {

	private StandardizerSettingsConfigDTO settingsForSuite(String suiteVersion) {
		StandardizerSettingsConfigDTO dto = new StandardizerSettingsConfigDTO();
		dto.setShouldStandardize(true);
		dto.setType("bbchem");
		dto.setSettings("{\"standardizer_actions\":{\"NEUTRALIZE\":true},"
				+ "\"hash_scheme\":\"TAUTOMER_INSENSITIVE_LAYERS\","
				+ "\"schrodinger_suite_version\":\"" + suiteVersion + "\"}");
		return dto;
	}

	@Test
	public void reusedHistoryAdoptsCurrentSettingsSnapshot() {
		// An orphaned, incomplete dry-run record snapshotted under an OLD suite build.
		StandardizerSettingsConfigDTO oldSettings = settingsForSuite("Schrodinger Suite 2026-3, Build 058");
		StandardizationHistory history = new StandardizationHistory();
		history.setSettings(oldSettings.toJson());
		history.setSettingsHash(oldSettings.hashCode());
		history.setDryRunStatus("failed");
		history.setDryRunComplete(new Date());

		// The initiating pod now runs with a NEW suite build and re-initializes the dry run.
		StandardizerSettingsConfigDTO currentSettings = settingsForSuite("Schrodinger Suite 2026-3, Build 073");
		Date start = new Date();
		StandardizationServiceImpl.applyDryRunInitialization(history, currentSettings, "running", start);

		// Snapshot must reflect the CURRENT config so the worker guard matches and proceeds.
		assertEquals(currentSettings.hashCode(), history.getSettingsHash());
		assertEquals(currentSettings.toJson(), history.getSettings());
		assertEquals("running", history.getDryRunStatus());
		assertEquals(start, history.getDryRunStart());
		assertNull(history.getDryRunComplete());
	}

	@Test
	public void suiteVersionChangeChangesTheHash() {
		// Documents the exact trigger: identical actions, different suite build => different hash.
		int oldHash = settingsForSuite("Schrodinger Suite 2026-3, Build 058").hashCode();
		int newHash = settingsForSuite("Schrodinger Suite 2026-3, Build 073").hashCode();
		assertFalse("suite version must affect the settings hash", oldHash == newHash);
	}
}
