package com.labsynch.labseer.service;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.stream.Collectors;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule.RegistrationStatus;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.domain.StandardizationDryRunCompound;
import com.labsynch.labseer.domain.StandardizationHistory;
import com.labsynch.labseer.domain.StandardizationDryRunCompound.SyncStatus;
import com.labsynch.labseer.dto.StandardizationDryRunSearchDTO;
import com.labsynch.labseer.dto.StandardizationSettingsConfigCheckResponseDTO;
import com.labsynch.labseer.dto.configuration.StandardizerSettingsConfigDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;
import com.labsynch.labseer.exceptions.StructureSaveException;
import com.labsynch.labseer.service.ChemStructureService.SearchType;
import com.labsynch.labseer.service.ChemStructureService.StructureType;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

@Service
public class StandardizationServiceImpl implements StandardizationService, ApplicationListener<ContextRefreshedEvent> {

	Logger logger = LoggerFactory.getLogger(StandardizationServiceImpl.class);

	@Autowired
	public ChemStructureService chemStructureService;

	@Autowired
	private SaltStructureService saltStructureService;

	@Autowired
	public ParentLotService parentLotService;

	@Autowired
	public ParentStructureService parentStructureService;

	@Autowired
	public ParentAliasService parentAliasService;

	@Autowired
	CmpdRegSDFWriterFactory cmpdRegSDFWriterFactory;

	@Autowired
	CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	@Autowired
	PropertiesUtilService propertiesUtilService;

	@Autowired
	public CmpdRegSDFWriterFactory sdfWriterFactory;

	public void failRunnningStandardization() {
		// Cancel all running standardization histories as failed
		List<StandardizationHistory> histories = StandardizationHistory.findAllStandardizationHistorys();
		for (StandardizationHistory history : histories) {
			if (history.getStandardizationStatus() != null
					&& history.getStandardizationStatus().equals("running")) {
				logger.info("Failing running standardization for run id " + history.getId());
				history.setStandardizationStatus("failed");
				history.merge();
			}
			if (history.getDryRunStatus() != null && history.getDryRunStatus().equals("running")) {
				logger.info("Failing running standardization dry run for run id " + history.getId());
				history.setDryRunStatus("failed");
				history.merge();
			}
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		logger.info("Application context: " + context.getDisplayName());
		if (context.getDisplayName().equals("Root WebApplicationContext")) {
			// this is the root context so wait for the web application context to be
			// initialized
			return;
		}

		try{
			checkStandardizationState();
		} catch (Exception e) {
			logger.warn("Error checking standardization state", e);
		}

		logger.info("Checking for structures missing from chemistry engine tables");
		try {
			chemStructureService.fillMissingStructures();
		} catch (Exception e) {
			logger.error("Error in trying to fill missing structures", e);
		}
	}

	@Transactional
	@Override
	public StandardizationSettingsConfigCheckResponseDTO checkStandardizationState() throws StandardizerException {
		logger.info("Checking compound standardization state");

		// Get the current settings from the config file using "false" which tell getStandardizerSettings to get the raw configs as opposed to the fixed/applied configs
		StandardizerSettingsConfigDTO currentRawStandardizerSettings = chemStructureService.getStandardizerSettings(false);

		// Get the applied settings from the history table which should have the most recent standardization settings applied
		List<StandardizationHistory> completedHistories = StandardizationHistory.findStandardizationHistoriesByStatus("complete", 0, 1, "id", "DESC").getResultList();

		StandardizationHistory mostRecentHistory;
		if (completedHistories.size() > 0) {
			// Got a complete history where the standardization actually standaradized compounds
			mostRecentHistory = completedHistories.get(0);
		} else {
			// There are 2 normal states where there are no completed histories
			// 1. We upgraded to the new standardization code and there are no completed standardizations
			// 2. This is a new install and there are no completed standardizations
			// We account for these states by:
			// In case 1, we pass along an empty StandardizationHistory, which will cause the system to report that it needs standardization.
			// In case 2 we try to fill in an initial standardization record with the current settings, provided they're valid settings, so no restandardization is needed.
			mostRecentHistory = new StandardizationHistory();
			StandardizationSettingsConfigCheckResponseDTO checkStandarizerOutput = chemStructureService.checkStandardizerSettings(mostRecentHistory, currentRawStandardizerSettings);
			if(Parent.countParents() == 0) {
				// We need to call checkStandardization settings as it is responsible for making sure the applied settings are
				// filled in approriatly before we save the history with applied settings.
				if(!checkStandarizerOutput.getValid()) {
					// If the settings are invalid then we don't save a standardization history as there has never been a valid state
					String initialSettingsInvalidReasons = checkStandarizerOutput.getInvalidReasons().stream().collect(Collectors.joining(System.lineSeparator()));
					logger.info("Initial standardization settings are invalid: " + initialSettingsInvalidReasons);
				} else {

					// If there is no standardization history the lets create one
					mostRecentHistory = StandardizationDryRunCompound.addStatsToHistory(mostRecentHistory);
					mostRecentHistory.setStructuresStandardizedCount(0);
					mostRecentHistory.setStructuresUpdatedCount(0);
					mostRecentHistory.setStandardizationComplete(new Date());
					mostRecentHistory.setStandardizationStatus("complete");
					mostRecentHistory.setStandardizationUser("acas");
					mostRecentHistory.setStandardizationReason("Initial standardization record");
					// Get the applied standardizer settings
					StandardizerSettingsConfigDTO appliedStandardizerSettings = chemStructureService.getStandardizerSettings(true);
					mostRecentHistory.setSettings(appliedStandardizerSettings.toJson());
					mostRecentHistory.setSettingsHash(appliedStandardizerSettings.hashCode());
					mostRecentHistory.persist();

				}
			}

		}

		// Do a config check to verify that settings are valid and if we need to restandardize or not
		StandardizationSettingsConfigCheckResponseDTO configCheckResponse = chemStructureService.checkStandardizerSettings(mostRecentHistory, currentRawStandardizerSettings);


		// If the settings are invalid log the reasons
		if (!configCheckResponse.getValid()) {
			String invalidReasons = configCheckResponse.getInvalidReasons().stream().collect(Collectors.joining(System.lineSeparator()));
			logger.info(
					"The system is configured with invalid standardizer configurations, "
							+ " reasons: " + invalidReasons
			);
		}

		// If we have applied standardization settings previously
		if (configCheckResponse.getNeedsRestandardization()) {
			String needsStandardizationReasons = configCheckResponse.getNeedsRestandardizationReasons().stream().collect(Collectors.joining(System.lineSeparator()));
			logger.info(
					"System requires restandardization, "
							+ " reasons: " + needsStandardizationReasons
			);
		}

		return configCheckResponse;
	}

	@Transactional
	private HashMap<String, Integer> saveDryRunStructures(HashMap<String, CmpdRegMolecule> structures) {
		HashMap<String, Integer> saveResults = chemStructureService.saveStructures(structures,
				StructureType.STANDARDIZATION_DRY_RUN, false);
		return saveResults;
	}

	@Override
	@Transactional
	public int populateStandardizationDryRunTable()
			throws CmpdRegMolFormatException, IOException, StandardizerException {
		List<Long> parentIds = Parent.getParentIds();

		Long startTime = new Date().getTime();

		int batchSize = propertiesUtilService.getStandardizationBatchSize();
		int nonMatchingCmpds = 0;
		int totalCount = parentIds.size();
		logger.info("number of parents to check: " + totalCount);

		Integer runNumber = StandardizationDryRunCompound.findMaxRunNumber().getSingleResult();
		if (runNumber == null) {
			runNumber = 1;
		} else {
			runNumber++;
		}
		float percent = 0;
		int p = 0;
		float previousPercent = percent;
		previousPercent = percent;

		// Split parent ids into groups of batchSize
		List<List<Long>> parentIdGroups = SimpleUtil.splitArrayIntoGroups(parentIds, batchSize);

		// Do a bulk standardization
		for (List<Long> pIdGroup : parentIdGroups) {
			dryrunStandardizeBatch(pIdGroup, nonMatchingCmpds, runNumber);
			p = p + pIdGroup.size();

			// Compute your percentage.
			percent = (float) Math.floor(p * 100f / totalCount);
			if (percent != previousPercent) {
				Long currentTime = new Date().getTime();
				// Output if different from the last time.
				logger.info("populating standardization dry run table " + percent + "% complete (" + p + " of "
						+ totalCount + ") average speed (rows/min):"
						+ (p / ((currentTime - startTime) / 60.0 / 1000.0)));
				currentTime = new Date().getTime();
				logger.debug("Time Elapsed:" + (currentTime - startTime));
			}
			// Update the percentage.
			previousPercent = percent;
		}
		logger.info("total number of non matching; structure, display or as drawn display changes: " + nonMatchingCmpds);
		return (nonMatchingCmpds);
	}

	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private void dryrunStandardizeBatch(List<Long> pIdGroup, int nonMatchingCmpds, int runNumber) throws StandardizerException, CmpdRegMolFormatException {
		Parent parent;
		StandardizationDryRunCompound stndznCompound;
		Date qcDate = new Date();
		Integer cdId = 0;

		logger.info("Starting batch of " + pIdGroup.size() + " parents");
		// Create standardization hashmap
		HashMap<String, String> parentIdsToStructures = new HashMap<String, String>();
		HashMap<String, String> parentIdsToAsDrawnStructs = new HashMap<String, String>();
		HashMap<Long, String> parentAsDrawnMap = Lot.getOriginallyDrawnAsStructuresByParentIds(pIdGroup);
		HashMap<Long, Parent> parents = new HashMap<Long, Parent>();
		for (Long parentId : pIdGroup) {
			parent = Parent.findParent(parentId);
			parents.put(parentId, parent);

			// Get the as drawn structure
			String asDrawnStruct = parentAsDrawnMap.get(parentId);
			if (asDrawnStruct == null) {
				logger.warn("Parent " + parentId + " has no as drawn structure");
				parentIdsToStructures.put(parentId.toString(parentId), parent.getMolStructure());
			} else {
				parentIdsToStructures.put(parentId.toString(parentId), asDrawnStruct);
				parentIdsToAsDrawnStructs.put(parentId.toString(parentId), asDrawnStruct);
			}

		}

		// Do standardization
		logger.info("Starting standardization of " + parentIdsToStructures.size() + " compounds");
		// Start timer
		long standardizationStart = new Date().getTime();
		HashMap<String, CmpdRegMolecule> standardizationResults = chemStructureService
				.standardizeStructures(parentIdsToStructures);
		long standardizationEnd = new Date().getTime();
		// Convert the ms time to seconds
		long standardizationTime = (standardizationEnd - standardizationStart) / 1000;
		logger.info("Standardization took " + standardizationTime + " seconds");

		logger.info("Starting saving of " + pIdGroup.size() + " dry run structures");
		long structureSaveStart = new Date().getTime();

		// Save the standardized dry run structures and return the hashmap of String
		// (parent id) and Integer (cd id). We use the cdIds below
		// when saving the dry run compounds to the database which links the structures
		// and compounds together.
		HashMap<String, Integer> parentIdToStructureId = saveDryRunStructures(standardizationResults);
		long structureSaveEnd = new Date().getTime();
		// Convert the ms time to seconds
		long saveTime = (structureSaveEnd - structureSaveStart) / 1000;
		logger.info("Saving took " + saveTime + " seconds");

		logger.info("Starting saving of " + pIdGroup.size() + " standardization dry run compounds");
		long dryRunCompoundSaveStart = new Date().getTime();
		for (Long parentId : pIdGroup) {
			parent = parents.get(parentId);
			stndznCompound = new StandardizationDryRunCompound();
			stndznCompound.setRunNumber(runNumber);
			stndznCompound.setQcDate(qcDate);
			stndznCompound.setParent(parent);
			stndznCompound.setAlias(getParentAlias(parent));

			CmpdRegMolecule cmpdRegMolecule = standardizationResults.get(parentId.toString());
			stndznCompound.setMolStructure(cmpdRegMolecule.getMolStructure());
			stndznCompound.setStandardizationStatus(cmpdRegMolecule.getStandardizationStatus());
			stndznCompound.setStandardizationComment(cmpdRegMolecule.getStandardizationComment());
			stndznCompound.setRegistrationStatus(cmpdRegMolecule.getRegistrationStatus());
			stndznCompound.setRegistrationComment(cmpdRegMolecule.getRegistrationComment());

			Double newMolWeight = cmpdRegMolecule.getMass(false);
			if (newMolWeight != null) {
				DecimalFormat dMolWeight = new DecimalFormat("#.###");
				stndznCompound.setNewMolWeight(Double.valueOf(dMolWeight.format(newMolWeight)));
			} else {
				stndznCompound.setNewMolWeight(null);
			}

			if (newMolWeight == null || stndznCompound.getParent().getMolWeight() == null) {
				stndznCompound.setDeltaMolWeight(null);
			} else {
				DecimalFormat deltaMolFormat = new DecimalFormat("#.###");
				Double deltaMolWeight = stndznCompound.getParent().getMolWeight() - stndznCompound.getNewMolWeight();
				stndznCompound.setDeltaMolWeight(Double.valueOf(deltaMolFormat.format(deltaMolWeight)));
				if(Math.abs(stndznCompound.getDeltaMolWeight()) >= 0.01) {
					stndznCompound.setSyncStatus(SyncStatus.READY);
				}
			}

			boolean displayTheSame = chemStructureService.isIdenticalDisplay(parent.getMolStructure(),
					stndznCompound.getMolStructure());

			if (!displayTheSame) {
				stndznCompound.setDisplayChange(true);
				stndznCompound.setSyncStatus(SyncStatus.READY);
				logger.debug("the compounds are NOT matching: " + parent.getCorpName());
				nonMatchingCmpds++;
			}
			String asDrawnStruct = parentIdsToAsDrawnStructs.get(parentId.toString(parentId));
			boolean asDrawnDisplaySame = chemStructureService.isIdenticalDisplay(asDrawnStruct,
					stndznCompound.getMolStructure());

			if (!asDrawnDisplaySame) {
				stndznCompound.setAsDrawnDisplayChange(true);
				logger.debug("the compounds are NOT matching: " + parent.getCorpName());
				nonMatchingCmpds++;
			}

			cdId = parentIdToStructureId.get(parentId.toString());

			if (cdId == -1) {
				logger.error("Bad molformat. Please fix the molfile for Corp Name " + stndznCompound.getParent().getCorpName()
						+ ", Parent ID " + stndznCompound.getParent().getId() + ": " + stndznCompound.getMolStructure());
			} else {
				stndznCompound.setCdId(cdId);
				stndznCompound.persist();
			}
		}
		// End timer
		long dryRunCompoundSaveEnd = new Date().getTime();
		// Convert the ms time to seconds
		long dryRunCompoundSaveTime = (dryRunCompoundSaveEnd - dryRunCompoundSaveStart) / 1000;
		logger.info("Saving took " + dryRunCompoundSaveTime + " seconds");
	}
	
	private String getParentAlias(Parent parent) {
		StringBuilder aliasSB = new StringBuilder();
		Set<ParentAlias> parentAliases = parent.getParentAliases();
		boolean firstAlias = true;
		for (ParentAlias parentAlias : parentAliases) {
			if (firstAlias) {
				aliasSB.append(parentAlias.getAliasName());
				firstAlias = false;
			} else {
				aliasSB.append(";").append(parentAlias.getAliasName());
			}
		}
		return aliasSB.toString();
	}

	@Override
	@Transactional
	public int dupeCheckStandardizationStructures() {
		List<Long> dryRunIds = StandardizationDryRunCompound.findAllIds().getResultList();

		Long startTime = new Date().getTime();

		int totalCount = dryRunIds.size();
		logger.debug("number of compounds found in dry run table: " + totalCount);
		int totalNewDuplicateCount = 0;

		// Split parent ids into groups of batchSize of 1
		// We tested this with large batch sizes and it was faster doing 1 per transaction
		int batchSize = propertiesUtilService.getStandardizationBatchSize();
		List<List<Long>> dryRunIdGroups = SimpleUtil.splitArrayIntoGroups(dryRunIds, batchSize);

		float percent = 0;
		float previousPercent = percent;
		previousPercent = percent;

		int p = 1;
		// Do a bulk standardization
		for (List<Long> dIdGroup : dryRunIdGroups) {
			totalNewDuplicateCount = totalNewDuplicateCount + dupeCheckStandardizationStructuresBatch(dIdGroup);
			p = p+dIdGroup.size();

			// Compute your percentage.
			percent = (float) Math.floor(p * 100f / totalCount);
			if (percent != previousPercent) {
				Long currentTime = new Date().getTime();
				// Output if different from the last time.
				logger.info("checking for standardization duplicates " + percent + "% complete (" + p + "/"
						+ totalCount + ") average speed (rows/min):"
						+ (p / ((currentTime - startTime) / 60.0 / 1000.0)));
				logger.debug("Time Elapsed:" + (currentTime - startTime));
			}
			// Update the percentage.
			previousPercent = percent;
		}
		return(totalNewDuplicateCount);
	}

	private int dupeCheckStandardizationStructuresBatch(List<Long> dryRunIds) {
		return dryRunIds.parallelStream()
			.mapToInt(dryRunId -> {
				try {
					return dupeCheckStandardizationStructure(dryRunId);
				} catch (CmpdRegMolFormatException e) {
					throw new RuntimeException(e);
				}
			})
			.sum();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private int dupeCheckStandardizationStructure(Long dryRunId) throws CmpdRegMolFormatException {

		int[] hits;
		StandardizationDryRunCompound dryRunCompound;
		String newDuplicateCorpNames = "";
		String oldDuplicateCorpNames = "";
		int newDupeCount = 0;
		int oldDuplicateCount = 0;
		String tmpStructureKey = "TmpStructureKey01";
		int totalNewDuplicateCount = 0;

		dryRunCompound = StandardizationDryRunCompound.findStandardizationDryRunCompound(dryRunId);

		if (dryRunCompound.getRegistrationStatus() == RegistrationStatus.ERROR) {
			logger.info("skipping dupe check for compound with registration status "
					+ dryRunCompound.getRegistrationStatus() + ": " + dryRunCompound.getParent().getCorpName());
		} else {
			logger.debug("query compound: " + dryRunCompound.getParent().getCorpName());

			// NEW DUPLICATES
			// Search the Dry Run Standardization structures using the newly Standardized Dry Run structure to get a list of duplicates that will exist on the system when the standardization is complete

			// Get the structure from the dry run compound
			HashMap<String, Integer> dryRunChemStructureHashMap = new HashMap<String, Integer>();
			dryRunChemStructureHashMap.put(tmpStructureKey, dryRunCompound.getCdId());
			HashMap<String, CmpdRegMolecule> standardizationDryRunMolecules = chemStructureService.getCmpdRegMolecules(
				dryRunChemStructureHashMap,
					StructureType.STANDARDIZATION_DRY_RUN);

			// Query for structure matches against Dry Run Standarization
			// Pass -1F for simlarityPercent (non nullable int required in function
			// signature not used in DUPLICATE_TAUTOMER searches)
			// Pass -1 for maxResults (non nullable int required in function signature we
			// don't want to limit the hit counts here)
			hits = chemStructureService.searchMolStructures(standardizationDryRunMolecules.get(tmpStructureKey),
					StructureType.STANDARDIZATION_DRY_RUN, SearchType.DUPLICATE_TAUTOMER, -1F, -1);

			// Check for duplicates (stereo category, stereo comment matches with cdId hit list)
			List<StandardizationDryRunCompound> dryRunDupes = StandardizationDryRunCompound.checkForDuplicateStandardizationDryRunCompoundByCdId(dryRunCompound.getId(), hits).getResultList();
			newDupeCount = dryRunDupes.size();
			if(newDupeCount > 0) {
				newDuplicateCorpNames = dryRunDupes
					.stream()
					.map(d -> d.getParent().getCorpName())
					.collect(
						Collectors.joining(";")
					);
				logger.info("found dry run dupe - query: '" + dryRunCompound.getParent().getCorpName() + "' dupes list: "
					+ newDuplicateCorpNames);	
			}

			dryRunCompound.setNewDuplicateCount(newDupeCount);
			if (!newDuplicateCorpNames.equals("")) {
				dryRunCompound.setNewDuplicates(newDuplicateCorpNames);
			}

			// CHANGED STURCTURE check to see if the newly standardized structure still gets a hit when searching for parents if not then it's a changed structure
			hits = chemStructureService.searchMolStructures(standardizationDryRunMolecules.get(tmpStructureKey),
					StructureType.PARENT, SearchType.DUPLICATE_TAUTOMER, -1F, -1);

			// If not then we mark the structure as changed
			int parentCdId = dryRunCompound.getParent().getCdId();
			if(Arrays.stream(hits).anyMatch(x -> x == parentCdId)) {
				dryRunCompound.setChangedStructure(false);
			} else {
				dryRunCompound.setChangedStructure(true);
				dryRunCompound.setSyncStatus(SyncStatus.READY);
			}

			// OLD DUPLICATES
			// Search for Parent structures using the Parent structure to get a list of duplicates that existed on the system before the dry run

			// Get the structure from the parent
			HashMap<String, Integer> parentStructureHashMap = new HashMap<String, Integer>();
			parentStructureHashMap.put(tmpStructureKey, dryRunCompound.getParent().getCdId());
			HashMap<String, CmpdRegMolecule> parentCmpdRegMolecules = chemStructureService.getCmpdRegMolecules(
				parentStructureHashMap,
					StructureType.PARENT);

			// Query for structure matches against Parent
			// Pass -1F for simlarityPercent (non nullable int required in function
			// signature not used in DUPLICATE_TAUTOMER searches)
			// Pass -1 for maxResults (non nullable int required in function signature we
			// don't want to limit the hit counts here)
			hits = chemStructureService.searchMolStructures(parentCmpdRegMolecules.get(tmpStructureKey),
					StructureType.PARENT, SearchType.DUPLICATE_TAUTOMER, -1F, -1);
			
			// Check for duplicates (stereo category, stereo comment matches with cdId hit list)
			List<Parent> parentDupes = Parent.checkForDuplicateParentByCdId(dryRunCompound.getParent().getId(), hits).getResultList();
			oldDuplicateCount = parentDupes.size();
			if(oldDuplicateCount > 0) {
				oldDuplicateCorpNames = parentDupes
					.stream()
					.map(p -> p.getCorpName())
					.collect(
						Collectors.joining(";")
					);
				logger.info("found dupe parents - query: '" + dryRunCompound.getParent().getCorpName() + "' dupes list: "
					+ oldDuplicateCorpNames);	
			}
			dryRunCompound.setExistingDuplicateCount(oldDuplicateCount);
			if (!oldDuplicateCorpNames.equals("")) {
				dryRunCompound.setExistingDuplicates(oldDuplicateCorpNames);
			}

		}
	
		return (totalNewDuplicateCount);
	}

	@Override
	public String standardizeSingleMol(String mol)
			throws CmpdRegMolFormatException, StandardizerException, IOException {
		String result = "";
		result = chemStructureService.standardizeStructure(mol);
		return (result);
	}

	@Override
	public String getStandardizationDryRunReportFiles(String sdfFileName)
			throws IOException, CmpdRegMolFormatException {
		List<StandardizationDryRunCompound> stndznCompounds = StandardizationDryRunCompound.findReadyStandardizationChanges()
				.getResultList();

		return (writeStandardizationCompoundsToSDF(stndznCompounds, sdfFileName));
	}

	@Override
	public String getStandardizationDryRunReportFiles(StandardizationDryRunSearchDTO searchCriteria)
			throws IOException, CmpdRegMolFormatException {
		List<StandardizationDryRunCompound> stndznCompounds = StandardizationDryRunCompound
				.searchStandardiationDryRun(searchCriteria)
				.getResultList();
		return (writeStandardizationCompoundsToSDF(stndznCompounds, searchCriteria.getFilePath()));
	}

	public String writeStandardizationCompoundsToSDF(List<StandardizationDryRunCompound> stndznCompounds,
			String sdfFileName) throws IOException, CmpdRegMolFormatException {

		// Create/recreate file
		File sdfFile = new File(sdfFileName);
		if (sdfFile.exists()) {
			sdfFile.delete();
		}

		// Get SD Writer
		CmpdRegSDFWriter sdfWriter = sdfWriterFactory.getCmpdRegSDFWriter(sdfFileName);

		// Fetch all compounds as cmpdreg molecules
		HashMap<String, Integer> standardizationDryRunHashCompoundHashmap = new HashMap<String, Integer>();
		for (StandardizationDryRunCompound stndznCompound : stndznCompounds) {
			standardizationDryRunHashCompoundHashmap.put(stndznCompound.getParent().getCorpName(), stndznCompound.getCdId());
		}
		HashMap<String, CmpdRegMolecule> cmpdRegMolecules = chemStructureService
				.getCmpdRegMolecules(standardizationDryRunHashCompoundHashmap, StructureType.STANDARDIZATION_DRY_RUN);

		// Loop stndznCompounds and write cmpdreg molecule to the sdf file
		List<CmpdRegMolecule> cmpdRegMoleculeList = new ArrayList<CmpdRegMolecule>();
		for (StandardizationDryRunCompound stndznCompound : stndznCompounds) {
			CmpdRegMolecule cmpdRegMolecule = cmpdRegMolecules.get(stndznCompound.getParent().getCorpName());
			if (cmpdRegMolecule != null) {
				cmpdRegMolecule.setProperty("Corporate ID", stndznCompound.getParent().getCorpName());
				cmpdRegMolecule.setProperty("Standardization Status", stndznCompound.getStandardizationStatus().name());
				cmpdRegMolecule.setProperty("Standardization Comment", stndznCompound.getStandardizationComment());
				cmpdRegMolecule.setProperty("Registration Status", stndznCompound.getRegistrationStatus().name());
				cmpdRegMolecule.setProperty("Registration Comment", stndznCompound.getRegistrationComment());
				cmpdRegMolecule.setProperty("Structure Change", String.valueOf(stndznCompound.isChangedStructure()));
				cmpdRegMolecule.setProperty("Display Change", String.valueOf(stndznCompound.isDisplayChange()));
				cmpdRegMolecule.setProperty("New Duplicates", stndznCompound.getNewDuplicates());
				cmpdRegMolecule.setProperty("Existing Duplicates", stndznCompound.getExistingDuplicates());
				cmpdRegMolecule.setProperty("Delta Mol. Weight", String.valueOf(stndznCompound.getDeltaMolWeight()));
				cmpdRegMolecule.setProperty("New Mol. Weight", String.valueOf(stndznCompound.getNewMolWeight()));
				cmpdRegMolecule.setProperty("Old Mol. Weight", String.valueOf(stndznCompound.getParent().getMolWeight()));
				cmpdRegMolecule.setProperty("As Drawn Display Change",
						String.valueOf(stndznCompound.isAsDrawnDisplayChange()));
				cmpdRegMolecule.setProperty("Stereo Category", stndznCompound.getParent().getStereoCategory().getName());
				cmpdRegMolecule.setProperty("Stereo Comment", stndznCompound.getParent().getStereoComment());
				cmpdRegMoleculeList.add(cmpdRegMolecule);
			}
		}
		sdfWriter.writeMols(cmpdRegMoleculeList);
		sdfWriter.close();
		// Return the path to the sdf file
		return sdfFileName;
	}

	@Override
	public String getStandardizationDryRunReport()
			throws StandardizerException, CmpdRegMolFormatException, IOException {
		List<StandardizationDryRunCompound> stndznCompounds = StandardizationDryRunCompound.findReadyStandardizationChanges()
				.getResultList();

		String json = "[]";
		if (stndznCompounds.size() > 0) {
			json = StandardizationDryRunCompound.toJsonArray(stndznCompounds);

		}
		return (json);
	}

	@Override
	@Transactional
	public void reset() {
		boolean truncateTable = chemStructureService.truncateStructureTable(StructureType.STANDARDIZATION_DRY_RUN);
		StandardizationDryRunCompound.truncateTable();
	}

	@Transactional
	public Boolean updateStructure(CmpdRegMolecule cmpdRegMolecule, int cdId) {
		Boolean success = chemStructureService.updateStructure(cmpdRegMolecule, StructureType.PARENT, cdId);
		return success;
	}

	@Transactional
	public int recalculateLotMolWeights(List<Long> parentIds) {
		EntityManager em = Parent.entityManager();
		String updateLotSql = "UPDATE lot SET lot_mol_weight = parent.mol_weight + salt_form.salt_weight, version = lot.version+1, modified_date = :modifiedDate FROM parent, salt_form WHERE parent.id = salt_form.parent and salt_form.id = lot.salt_form and parent.id in (:parentIds)";
		Query updateLotQuery = em.createNativeQuery(updateLotSql);
		updateLotQuery.setParameter("parentIds", parentIds);
		updateLotQuery.setParameter("modifiedDate", new Date());
		return updateLotQuery.executeUpdate();
	}

	@Override
	@Transactional
	public int restandardizeParentsOfStandardizationDryRunCompounds()
			throws CmpdRegMolFormatException, StandardizerException, IOException {

		int batchSize = propertiesUtilService.getStandardizationBatchSize();

		int totalCount = StandardizationDryRunCompound.getReadyStandardizationChangesCount();
		int remainingCount = totalCount;
		logger.info("number of parents to restandardize: " + totalCount);
		float percent = 0;
		int p = 0;
		float previousPercent = percent;
		previousPercent = percent;

		Long startTime = new Date().getTime();
		Long currentTime = new Date().getTime();
		
		// Do a bulk standardization
		while(remainingCount > 0) {
			logger.info("Starting batch of " + batchSize + " compounds");
			int completedCount = restandardizeParentsOfStandardizationDryRunCompoundsBatch(batchSize);
			p =  p + completedCount;

			// Compute your percentage.
			percent = (float) Math.floor(p * 100f / totalCount);
			if (percent != previousPercent) {
				currentTime = new Date().getTime();
				// Output if different from the last time.
				logger.info("parent structure restandardization " + percent + "% complete (" + p + " of "
						+ totalCount + ") average speed (rows/min):"
						+ (p / ((currentTime - startTime) / 60.0 / 1000.0)));
				currentTime = new Date().getTime();
				logger.debug("Time Elapsed:" + (currentTime - startTime));
			}
			// Update the percentage.
			previousPercent = percent;

			remainingCount = StandardizationDryRunCompound.getReadyStandardizationChangesCount();
		}
		return(p);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int restandardizeParentsOfStandardizationDryRunCompoundsBatch(int batchSize) throws CmpdRegMolFormatException, StandardizerException, IOException {

		// Create a hashmap we can use to store the cmpdreg molecules for each cdId in the standardizationDryRunCompounds
		HashMap<String, Integer> cdIdMap = new HashMap<String, Integer>();
		List<StandardizationDryRunCompound> standardizationDryRunCompounds = StandardizationDryRunCompound.findReadyStandardizationChanges().setMaxResults(batchSize).getResultList();
		for(StandardizationDryRunCompound c : standardizationDryRunCompounds) {
			// For each standardization compound we want to make a hash of it id and cdId
			cdIdMap.put(String.valueOf(c.getId()), c.getCdId());
		}
		// Get the cmpd reg molecules from the dry run standardization
		HashMap<String, CmpdRegMolecule> cmpdRegMolecules = chemStructureService.getCmpdRegMolecules(cdIdMap, StructureType.STANDARDIZATION_DRY_RUN);

		logger.info("Starting save of " + standardizationDryRunCompounds.size() + " compounds");
		long savingStart = new Date().getTime();
		List<Long> pIds = new ArrayList<Long>();
		for (StandardizationDryRunCompound standardizationCompound : standardizationDryRunCompounds) {
			// Parent should have been eagerly loaded from the query
			Parent parent = standardizationCompound.getParent();

			// Get the molecule from the hashmap using the id of the standardization compound
			CmpdRegMolecule cmpdRegMolecule = cmpdRegMolecules.get(String.valueOf(standardizationCompound.getId()));

			// Get the now standardized mol
			String standardizedMol = cmpdRegMolecule.getMolStructure();

			// Update the structure parent structure by pushing the cmpdreg molecule to the StructureType.PARENT table and updating the parent cdid
			Boolean success = updateStructure(cmpdRegMolecule, parent.getCdId());

			// In the case where we are switching chemistry engines the structure might not
			// exist,
			// so we need to check for that and if it does not exist we need to create it
			if (!success) {
				logger.warn("Could not update structure for parent: " + parent.getId() + "  " + parent.getCorpName());
				logger.info("Assuming the structure did not exist in the first place and saving a new one");
				int newCdId = chemStructureService.saveStructure(cmpdRegMolecule, StructureType.PARENT, false);
				parent.setCdId(newCdId);
				logger.info("Updated parent with new cdId: " + newCdId);
			}

			// Update the mol structure
			parent.setMolStructure(standardizedMol);

			// Update other properties
			if (cmpdRegMolecule.getExactMass() != null) {
				DecimalFormat dExactMass = new DecimalFormat("#.######");
				parent.setExactMass(Double.valueOf(dExactMass.format(cmpdRegMolecule.getExactMass())));
			} else {
				parent.setExactMass(null);
			}

			if (cmpdRegMolecule.getMass(false) != null) {
				DecimalFormat dMolWeight = new DecimalFormat("#.###");
				parent.setMolWeight(Double.valueOf(dMolWeight.format(cmpdRegMolecule.getMass(false))));
			} else {
				parent.setMolWeight(null);
			}

			parent.setMolFormula(cmpdRegMolecule.getFormula(false));

			pIds.add(parent.getId());

			standardizationCompound.setSyncStatus(SyncStatus.COMPLETE);
		}
		// Update lot information, this is much faster than looping through the
		// salt_forms and lots using hibernate
		if(pIds.size() > 0) {
			int countLotsUpdated = recalculateLotMolWeights(pIds);
			logger.info("Updated " + countLotsUpdated + " lots");
		}

		long savingEnd = new Date().getTime();
		// Convert the ms time to seconds
		long savingTime = (savingEnd - savingStart) / 1000;
		logger.info("Saving took " + savingTime + " seconds");

		return standardizationDryRunCompounds.size();
	}

	@Override
	@Transactional
	public String executeStandardization(String username, String reason) throws StandardizerException {
		StandardizationHistory standardizationHistory = getMostRecentStandardizationHistory();
		// We pass true to get the applied settings as oppoosed to the configuration file setttings
		StandardizerSettingsConfigDTO standardizationSettings = chemStructureService.getStandardizerSettings(true);
		if (standardizationHistory == null
				|| StringUtils.equalsIgnoreCase(standardizationHistory.getStandardizationStatus(), "complete")) {
			standardizationHistory = new StandardizationHistory();
			standardizationHistory.setSettings(standardizationSettings.toJson());
			standardizationHistory.setSettingsHash(standardizationSettings.hashCode());
			standardizationHistory.setRecordedDate(new Date());
		}
		standardizationHistory.setStandardizationStart(new Date());
		standardizationHistory.setStandardizationComplete(null);
		standardizationHistory.setStandardizationStatus("running");
		standardizationHistory.persist();

		Integer result;
		try {
			result = this.runStandardization();
		} catch (CmpdRegMolFormatException | IOException | StandardizerException e) {
			logger.error("Failed running standardization", e);
			standardizationHistory.setStandardizationComplete(new Date());
			standardizationHistory.setStandardizationStatus("failed");
			standardizationHistory.merge();
			return (standardizationHistory.toJson());
		}

		standardizationHistory = StandardizationDryRunCompound.addStatsToHistory(standardizationHistory);
		standardizationHistory.setStructuresUpdatedCount(result);
		standardizationHistory.setStandardizationComplete(new Date());
		standardizationHistory.setStandardizationStatus("complete");
		standardizationHistory.setStandardizationUser(username);
		standardizationHistory.setStandardizationReason(reason);
		standardizationHistory.merge();

		checkStandardizationState();
		return standardizationHistory.toJson();
	}

	private int runStandardization() throws CmpdRegMolFormatException, IOException, StandardizerException {
		logger.info("standardization initialized");
		logger.info("restandardize parent structures - started");
		int result = -1;
		try {
			result = restandardizeParentsOfStandardizationDryRunCompounds();
		} catch (Exception e) {
			throw new StandardizerException(e);
		}
		logger.info("restandardize parent structures - complete");
		return (result);
	}

	@Override
	public String getDryRunStats() throws StandardizerException {
		StandardizerSettingsConfigDTO standardizerConfigs = chemStructureService.getStandardizerSettings(true);
		StandardizationHistory dryRunStats = new StandardizationDryRunCompound().fetchStats();
		dryRunStats.setSettings(standardizerConfigs.toJson());
		dryRunStats.setSettingsHash(standardizerConfigs.hashCode());
		return dryRunStats.toJson();
	}

	@Override
	public List<StandardizationHistory> getStandardizationHistory() {
		List<StandardizationHistory> standardizationSettingses = StandardizationHistory
				.findStandardizationHistoryEntries(0, 500, "dateOfStandardization", "DESC");
		return standardizationSettingses;
	}

	@Transactional
	private StandardizationHistory setCurrentStandardizationDryRunStatus(String status) throws StandardizerException {
		StandardizationHistory stndznHistory = getMostRecentStandardizationHistory();
		StandardizerSettingsConfigDTO standardizationSettings = chemStructureService.getStandardizerSettings(true);
		if (stndznHistory == null
				|| StringUtils.equalsIgnoreCase(stndznHistory.getStandardizationStatus(), "complete")) {
			stndznHistory = new StandardizationHistory();
			stndznHistory.setSettings(standardizationSettings.toJson());
			stndznHistory.setSettingsHash(standardizationSettings.hashCode());
			stndznHistory.setRecordedDate(new Date());
		}
		stndznHistory.setDryRunStatus(status);
		stndznHistory.setDryRunStart(new Date());
		stndznHistory.setDryRunComplete(null);
		stndznHistory.persist();
		return stndznHistory;
	}

	@Override
	public void executeDryRun() throws CmpdRegMolFormatException, IOException, StandardizerException {

		StandardizationHistory stndznHistory = this.setCurrentStandardizationDryRunStatus("running");
		try {
			runDryRun();
		} catch (Exception e) {
			logger.error("error running dry run", e);
			stndznHistory.setDryRunComplete(new Date());
			stndznHistory.setDryRunStatus("failed");
			stndznHistory.merge();
			throw e;
		}

		stndznHistory.setDryRunComplete(new Date());
		stndznHistory.setDryRunStatus("complete");
		stndznHistory
				.setDryRunStandardizationChangesCount(StandardizationDryRunCompound.getReadyStandardizationChangesCount());
		stndznHistory.merge();
	}

	private int runDryRun() throws CmpdRegMolFormatException, IOException, StandardizerException {
		logger.info("standardization dry run initialized");
		logger.info("step 1/3: resetting dry run table");
		this.reset();
		logger.info("step 2/3: populating dry run table");
		this.populateStandardizationDryRunTable();
		logger.info("step 3/3: checking for standardization duplicates");
		int numberOfDisplayChanges = this.dupeCheckStandardizationStructures();
		logger.info("standardization dry run complete");
		return numberOfDisplayChanges;
	}

	public StandardizationHistory getMostRecentStandardizationHistory() {
		List<StandardizationHistory> standardizationSettingses = StandardizationHistory
				.findStandardizationHistoryEntries(0, 1, "id", "DESC");
		if (standardizationSettingses.size() > 0) {
			return standardizationSettingses.get(0);
		} else {
			return null;
		}
	}
}

