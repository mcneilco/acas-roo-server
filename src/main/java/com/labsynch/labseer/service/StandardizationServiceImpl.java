package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.domain.StandardizationDryRunCompound;
import com.labsynch.labseer.domain.StandardizationHistory;
import com.labsynch.labseer.domain.StandardizationSettings;
import com.labsynch.labseer.dto.configuration.StandardizerSettingsConfigDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;
import com.labsynch.labseer.exceptions.StructureSaveException;
import com.labsynch.labseer.service.ChemStructureService.SearchType;
import com.labsynch.labseer.service.ChemStructureService.StructureType;
import com.labsynch.labseer.utils.PropertiesUtilService;

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

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		logger.info("Application context: " + context.getDisplayName());
		if (context.getDisplayName().equals("Root WebApplicationContext")) {
			// this is the root context so wait for the web application context to be initialized
			return;
		}

		logger.info("Checking compound standardization state");
		try {
			// Get the current configuration settings
			StandardizerSettingsConfigDTO currentStandardizationSettings = chemStructureService
					.getStandardizerSettings();

			// Cancel all running standardization histories as failed
			List<StandardizationHistory> histories = StandardizationHistory.findAllStandardizationHistorys();
			for (StandardizationHistory history : histories) {
				if(history.getStandardizationStatus() != null && history.getStandardizationStatus().equals("running")){
					logger.info("Failing running standardization for run id " + history.getId());
					history.setStandardizationStatus("failed");
					history.merge();
				}
				if(history.getDryRunStatus() != null && history.getDryRunStatus().equals("running")){
					logger.info("Failing running standardization dry run for run id " + history.getId());
					history.setDryRunStatus("failed");
					history.merge();
				}
			}

			// Get the applied settings from the history table which should have the most
			// recent standardization settings applied
			StandardizationHistory appliedStandardizationSettings = getMostRecentStandardizationHistory();

			if (appliedStandardizationSettings != null) {
				// If we have applied standardization settings previously
				if (currentStandardizationSettings.hashCode() != appliedStandardizationSettings.getSettingsHash()) {
					// If the applied settings are different from the current settings
					logger.info(
							"Standardizer configs have changed, marking 'standardization needed' according to configs as "
									+ currentStandardizationSettings.getShouldStandardize());

					// We should only ever really have one standardization settings row at any given
					// time
					List<StandardizationSettings> standardizationSettingses = StandardizationSettings
							.findAllStandardizationSettingses();

					if (standardizationSettingses.size() == 0) {
						// If we have no standardization settings rows, create one and set the value to
						// shouldStandardize to whatever the current settings say
						// Note - this is a weird state, if we have previously applied standardized
						// settings then we should have a standardization settings row
						StandardizationSettings standardizationSettings = new StandardizationSettings();
						standardizationSettings
								.setNeedsStandardization(currentStandardizationSettings.getShouldStandardize());
						standardizationSettings.setModifiedDate(new Date());
						standardizationSettings.persist();
					} else {

						// If there is more than 0, then just update the current row
						standardizationSettingses.get(0)
								.setNeedsStandardization(currentStandardizationSettings.getShouldStandardize());
						standardizationSettingses.get(0).setModifiedDate(new Date());
						standardizationSettingses.get(0).merge();
					}
				} else {
					logger.info("Standardizer configs have not changed, not marking 'standardization needed'");
				}
			} else {
				logger.info("No standardization history found in database");
				StandardizationSettings standardizationSettings = new StandardizationSettings();
				standardizationSettings.setModifiedDate(new Date());
				Long numberOfParents = Parent.countParents();
				if (numberOfParents == 0.0) {
					logger.info(
							"There are no parents registered, we can assume standardization configs match the database standardization state so storing configs as the standardization settings");
					standardizationSettings.setNeedsStandardization(false);
					standardizationSettings.persist();
					logger.info("Saved current standardization settings");
					executeStandardization("acas", "Initialization");
				} else {
					logger.warn(
							"Standardization is in an unknown state because there are no rows in standardization history table.");
					if (currentStandardizationSettings.getShouldStandardize() == false) {
						logger.info(
								"Standardization is turned off so marking the database as not requiring standardization at this time");
						standardizationSettings.setNeedsStandardization(false);
						standardizationSettings.persist();
					} else {
						logger.warn(
								"Standardization is turned on so we don't know the current database standardization state. Assuming no standardization is needed.");
						standardizationSettings.setNeedsStandardization(false);
						standardizationSettings.persist();
					}
				}
			}
		} catch (StandardizerException e) {
			logger.error("Caught error on checking standardization state", e);
		}
	}

	@Transactional
	private HashMap<String, Integer> saveDryRunStructures(HashMap<String, CmpdRegMolecule> structures) {
		HashMap<String, Integer> saveResults = chemStructureService.saveStructures(structures, StructureType.DRY_RUN, false);
		return saveResults;
	}

	@Override
	public int populateStandardizationDryRunTable()
			throws CmpdRegMolFormatException, IOException, StandardizerException {
		List<Long> parentIds = Parent.getParentIds();

		Session session = StandardizationDryRunCompound.entityManager().unwrap(Session.class);
		Long startTime = new Date().getTime();
		Long currentTime = new Date().getTime();

		int batchSize = propertiesUtilService.getStandardizationBatchSize();
		Parent parent;
		StandardizationDryRunCompound stndznCompound;
		int nonMatchingCmpds = 0;
		int totalCount = parentIds.size();
		logger.info("number of parents to check: " + totalCount);
		Date qcDate = new Date();
		String asDrawnStruct;
		Integer cdId = 0;
		List<Lot> queryLots;
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
		List<List<Long>> parentIdGroups = splitArrayIntoGroups(parentIds, batchSize);

		// Do a bulk standardization
		for (List<Long> pIdGroup : parentIdGroups) {
			logger.info("Starting batch of " + pIdGroup.size() + " parents");

			// Create standardization hashmap
			HashMap<String, String> parentIdsToStructures = new HashMap<String, String>();
			HashMap<Long, Parent> parents = new HashMap<Long, Parent>();
			for(Long parentId : pIdGroup) {
				parent = Parent.findParent(parentId);
				parents.put(parentId, parent);
				parentIdsToStructures.put(parentId.toString(parentId), parent.getMolStructure());
			}

			// Do standardization
			logger.info("Starting standardization of " + parentIdsToStructures.size() + " compounds");
			// Start timer
			long standardizationStart = new Date().getTime();
			HashMap<String, CmpdRegMolecule> standardizationResults = chemStructureService.standardizeStructures(parentIdsToStructures);
			long standardizationEnd = new Date().getTime();
			// Convert the ms time to seconds
			long standardizationTime = (standardizationEnd - standardizationStart) / 1000;
			logger.info("Standardization took " + standardizationTime + " seconds");

			logger.info("Starting saving of " + pIdGroup.size() + " dry run structures");
			long structureSaveStart = new Date().getTime();

			// Save the standardized dry run structures and return the hashmap of String (parent id) and Integer (cd id).  We use the cdIds below
			// when saving the dry run compounds to the database which links the structures and compounds together.
			HashMap<String, Integer> parentIdToStructureId = saveDryRunStructures(standardizationResults);
			long structureSaveEnd = new Date().getTime();
			// Convert the ms time to seconds
			long saveTime = (structureSaveEnd - structureSaveStart) / 1000;
			logger.info("Saving took " + saveTime + " seconds");

			logger.info("Starting saving of " + pIdGroup.size() + " standardization dry run compounds");
			long dryRunCompoundSaveStart = new Date().getTime();
			for(Long parentId : pIdGroup) {
				parent = parents.get(parentId);
				stndznCompound = new StandardizationDryRunCompound();
				stndznCompound.setRunNumber(runNumber);
				stndznCompound.setQcDate(qcDate);
				stndznCompound.setParentId(parent.getId());
				stndznCompound.setCorpName(parent.getCorpName());
				stndznCompound.setAlias(getParentAlias(parent));
				stndznCompound.setStereoCategory(parent.getStereoCategory().getName());
				stndznCompound.setStereoComment(parent.getStereoComment());
				stndznCompound.setOldMolWeight(parent.getMolWeight());

				asDrawnStruct = parent.getMolStructure();

				queryLots = Lot.findLotByParentAndLowestLotNumber(parent).getResultList();
				if (queryLots.size() > 0 && queryLots.get(0).getAsDrawnStruct() != null) {
					asDrawnStruct = queryLots.get(0).getAsDrawnStruct();
				} else {
					asDrawnStruct = parent.getMolStructure();
				}

				CmpdRegMolecule cmpdRegMolecule = standardizationResults.get(parentId.toString());
				stndznCompound.setMolStructure(cmpdRegMolecule.getMolStructure());
				stndznCompound.setNewMolWeight(cmpdRegMolecule.getMass());
	
				if (parent.getMolWeight() == 0 && stndznCompound.getNewMolWeight() == 0) {
					logger.debug("mol weight 0 before and after standardization - skipping");
	
				} else {

					boolean displayTheSame = chemStructureService.isIdenticalDisplay(parent.getMolStructure(),
						stndznCompound.getMolStructure());

					if (!displayTheSame) {
						stndznCompound.setDisplayChange(true);
						logger.debug("the compounds are NOT matching: " + parent.getCorpName());
						nonMatchingCmpds++;
					}
					boolean asDrawnDisplaySame = chemStructureService.isIdenticalDisplay(asDrawnStruct,
						stndznCompound.getMolStructure());

					if (!asDrawnDisplaySame) {
						stndznCompound.setAsDrawnDisplayChange(true);
						logger.debug("the compounds are NOT matching: " + parent.getCorpName());
						nonMatchingCmpds++;
					}
				}
				cdId = parentIdToStructureId.get(parentId.toString());

				if (cdId == -1) {
					logger.error("Bad molformat. Please fix the molfile for Corp Name " + stndznCompound.getCorpName()
							+ ", Parent ID " + stndznCompound.getParentId() + ": " + stndznCompound.getMolStructure());
				} else {
					stndznCompound.setCdId(cdId);
					stndznCompound.persist();
				}
				p++;
			}
			// End timer
			long dryRunCompoundSaveEnd = new Date().getTime();
			// Convert the ms time to seconds
			long dryRunCompoundSaveTime = (dryRunCompoundSaveEnd - dryRunCompoundSaveStart) / 1000;
			logger.info("Saving took " + dryRunCompoundSaveTime + " seconds");
			
			// End loop through parent id group
			logger.debug("flushing loader session");
			session.flush();
			session.clear();
			
			// Compute your percentage.
			percent = (float) Math.floor(p * 100f / totalCount);
			if (percent != previousPercent) {
				currentTime = new Date().getTime();
				// Output if different from the last time.
				logger.info("populating standardization dry run table " + percent + "% complete (" + p + " of "
					+ totalCount + ") average speed (rows/min):"+ (p/((currentTime - startTime) / 60.0 / 1000.0)));
				currentTime = new Date().getTime();
				logger.debug("Time Elapsed:"+ (currentTime - startTime));
			}
			// Update the percentage.
			previousPercent = percent;
		}

		logger.info(
				"total number of non matching; structure, display or as drawn display changes: " + nonMatchingCmpds);
		return (nonMatchingCmpds);
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
	public int dupeCheckStandardizationStructures() throws CmpdRegMolFormatException {
		List<Long> dryRunIds = StandardizationDryRunCompound.findAllIds().getResultList();

		Session session = StandardizationDryRunCompound.entityManager().unwrap(Session.class);
		Long startTime = new Date().getTime();
		Long currentTime = new Date().getTime();

		int totalCount = dryRunIds.size();
		logger.debug("number of compounds found in dry run table: " + totalCount);
		int totalNewDuplicateCount = 0;
		int totalExistingDuplicateCount = 0;
		if (dryRunIds.size() > 0) {
			int[] hits;
			StandardizationDryRunCompound dryRunCompound;
			String newDuplicateCorpNames = "";
			String oldDuplicateCorpNames = "";
			int newDupeCount = 0;
			int oldDuplicateCount = 0;

			float percent = 0;
			int p = 1;
			float previousPercent = percent;
			previousPercent = percent;
			for (Long dryRunId : dryRunIds) {
				boolean firstNewDuplicateHit = true;
				boolean firstOldDuplicateHit = true;
				dryRunCompound = StandardizationDryRunCompound.findStandardizationDryRunCompound(dryRunId);
				logger.debug("query compound: " + dryRunCompound.getCorpName());
				if (dryRunCompound.getNewMolWeight() == 0) {
					logger.debug("mol has a weight of 0 - skipping");
				} else {
					HashMap<String, Integer> chemStructureHashMap = new HashMap<String, Integer>();

					// Arbitrary key to call service and fetch cmpdreg molecule
					String tmpStructureKey = "TmpStructureKey01";
					chemStructureHashMap.put(tmpStructureKey, dryRunCompound.getCdId());
					HashMap<String, CmpdRegMolecule> cmpdRegMolecules = chemStructureService.getCmpdRegMolecules(chemStructureHashMap,
							StructureType.DRY_RUN);

					// Pass -1F for simlarityPercent (non nullable int required in function signature not used in DUPLICATE_TAUTOMER searches)
					// Pass -1 for maxResults (non nullable int required in function signature we don't want to limit the hit counts here)
					hits = chemStructureService.searchMolStructures(cmpdRegMolecules.get(tmpStructureKey),
							StructureType.DRY_RUN, SearchType.DUPLICATE_TAUTOMER, -1F, -1);
					newDupeCount = hits.length;
					for (int hit : hits) {
						List<StandardizationDryRunCompound> searchResults = StandardizationDryRunCompound
								.findStandardizationDryRunCompoundsByCdId(hit).getResultList();
						for (StandardizationDryRunCompound searchResult : searchResults) {
							if (searchResult.getCorpName().equalsIgnoreCase(dryRunCompound.getCorpName())) {
								newDupeCount = newDupeCount - 1;
							} else {
								if (StringUtils.equals(searchResult.getStereoCategory(),
										dryRunCompound.getStereoCategory())
										&& StringUtils.equalsIgnoreCase(searchResult.getStereoComment(),
												dryRunCompound.getStereoComment())) {
									if (!firstNewDuplicateHit)
										newDuplicateCorpNames = newDuplicateCorpNames.concat(";");
									newDuplicateCorpNames = newDuplicateCorpNames.concat(searchResult.getCorpName());
									firstNewDuplicateHit = false;
									logger.info("found new dupe parents");
									logger.info("query: " + dryRunCompound.getCorpName() + "     dupe: "
											+ searchResult.getCorpName());
									totalNewDuplicateCount++;
								} else {
									newDupeCount = newDupeCount - 1;
									logger.debug("found different stereo codes and comments");
								}
							}
						}
					}
					hits = chemStructureService.searchMolStructures(cmpdRegMolecules.get("mol"),
							StructureType.PARENT, SearchType.DUPLICATE_TAUTOMER, -1F, -1);
					oldDuplicateCount = hits.length;
					dryRunCompound.setChangedStructure(true);
					for (int hit : hits) {
						List<Parent> searchResults = Parent.findParentsByCdId(hit).getResultList();
						for (Parent searchResult : searchResults) {
							if (searchResult.getCorpName().equalsIgnoreCase(dryRunCompound.getCorpName())) {
								oldDuplicateCount = oldDuplicateCount - 1;
								dryRunCompound.setChangedStructure(false);
							} else {
								if (StringUtils.equals(searchResult.getStereoCategory().getName(),
										dryRunCompound.getStereoCategory())
										&& StringUtils.equalsIgnoreCase(searchResult.getStereoComment(),
												dryRunCompound.getStereoComment())) {
									if (!firstOldDuplicateHit)
										oldDuplicateCorpNames = oldDuplicateCorpNames.concat(";");
									oldDuplicateCorpNames = oldDuplicateCorpNames.concat(searchResult.getCorpName());
									firstOldDuplicateHit = false;
									logger.info("found old dupe parents");
									logger.info("query: " + dryRunCompound.getCorpName() + "     dupe: "
											+ searchResult.getCorpName());
									totalExistingDuplicateCount++;
								} else {
									oldDuplicateCount = oldDuplicateCount - 1;
									logger.debug("found different stereo codes and comments");
								}
							}
						}
					}
					dryRunCompound.setNewDuplicateCount(newDupeCount);
					if (!newDuplicateCorpNames.equals("")) {
						dryRunCompound.setNewDuplicates(newDuplicateCorpNames);
					}
					dryRunCompound.setExistingDuplicateCount(oldDuplicateCount);
					if (!oldDuplicateCorpNames.equals("")) {
						dryRunCompound.setExistingDuplicates(oldDuplicateCorpNames);
					}

				}

				dryRunCompound.merge();
				newDuplicateCorpNames = "";
				oldDuplicateCorpNames = "";

				if (p % 100 == 0){
					logger.debug("flushing loader session");
					session.flush();
					session.clear();
				}

				// Compute your percentage.
				percent = (float) Math.floor(p * 100f / totalCount);
				if (percent != previousPercent) {
					currentTime = new Date().getTime();
					// Output if different from the last time.
					logger.info("checking for standardization duplicates " + percent + "% complete (" + p + "/"
							+ totalCount + ") average speed (rows/min):"+ (p/((currentTime - startTime) / 60.0 / 1000.0)));
					logger.debug("Time Elapsed:"+ (currentTime - startTime));
				}
				// Update the percentage.
				previousPercent = percent;
				p++;

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
	public String getStandardizationDryRunReport()
			throws StandardizerException, CmpdRegMolFormatException, IOException {
		List<StandardizationDryRunCompound> stndznCompounds = StandardizationDryRunCompound.findStandardizationChanges()
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
		boolean truncateTable = chemStructureService.truncateStructureTable(StructureType.DRY_RUN);
		StandardizationDryRunCompound.truncateTable();
	}

	@Transactional
	public Boolean updateStructure(CmpdRegMolecule cmpdRegMolecule, int cdId) {
		Boolean success = chemStructureService.updateStructure(cmpdRegMolecule, StructureType.PARENT, cdId);
		return success;
	}

	List<List<Long>> splitArrayIntoGroups(List<Long> array, int groupSize) {
		// Split array into groups of groupSize
		List<List<Long>> groups = new ArrayList<List<Long>>();
		List<Long> group = new ArrayList<Long>();
		int loopCount = 1;
		for (Long l : array) {
			group.add(l);
			// Check to see if we are at the end of the group or if we are at the end of the array
			// if so, then add the group to the list of groups and start a new group
			if (group.size() == groupSize || loopCount == array.size()) {
				groups.add(group);
				group = new ArrayList<Long>();
			}
			loopCount ++;
		}
		return groups;
	}

	@Override
	public int restandardizeParentStructures(List<Long> parentIds)
			throws CmpdRegMolFormatException, StandardizerException, IOException {

		int batchSize = propertiesUtilService.getStandardizationBatchSize();
		Parent parent;
		List<Lot> lots;
		Lot lot;
		String originalStructure = null;
		String standardizedMol;
		int totalCount = parentIds.size();
		logger.info("number of parents to restandardize: " + totalCount);
		float percent = 0;
		int p = 0;
		float previousPercent = percent;
		previousPercent = percent;

		Session session = Parent.entityManager().unwrap(Session.class);
		Long startTime = new Date().getTime();
		Long currentTime = new Date().getTime();
		
		List<List<Long>> parentIdGroups = splitArrayIntoGroups(parentIds, batchSize);


		// Do a bulk standardization
		for (List<Long> pIdGroup : parentIdGroups) {
			logger.info("Starting batch of " + pIdGroup.size() + " parents");

			// Create standardization hashmap
			HashMap<String, String> parentIdsToParents = new HashMap<String, String>();
			HashMap<Long, Parent> parents = new HashMap<Long, Parent>();
			for(Long parentId : pIdGroup) {
				parent = Parent.findParent(parentId);
				parents.put(parentId, parent);
				parentIdsToParents.put(parentId.toString(parentId), parent.getMolStructure());
			}

			// Do standardization
			logger.debug("Starting standardization of " + parentIdsToParents.size() + " compounds");
			// Start timer
			long standardizationStart = new Date().getTime();
			HashMap<String, CmpdRegMolecule> standardizationResults = chemStructureService.standardizeStructures(parentIdsToParents);
			long standardizationEnd = new Date().getTime();
			// Convert the ms time to seconds
			long standardizationTime = (standardizationEnd - standardizationStart) / 1000;
			logger.info("Standardization took " + standardizationTime + " seconds");

			for(Long parentId : pIdGroup) {

				parent = parents.get(parentId);
				originalStructure = parent.getMolStructure();

				List<Lot> queryLots = Lot.findLotByParentAndLowestLotNumber(parent).getResultList();
				if (queryLots.size() > 0 && queryLots.get(0).getAsDrawnStruct() != null) {
					originalStructure = queryLots.get(0).getAsDrawnStruct();
				}

				// We standardize the structure first
				CmpdRegMolecule cmpdRegMolecule = standardizationResults.get(parentId.toString());
				standardizedMol = cmpdRegMolecule.getMolStructure();

				// Now we update the parent structure
				Boolean success = updateStructure(cmpdRegMolecule, parent.getCdId());

				// In the case where we are switching chemistry engines the structure might not exist,
				// so we need to check for that	and if it does not exist we need to create it
				if(!success){
					logger.warn("Could not update structure for parent: " + parentId + "  " + parent.getCorpName());
					logger.info("Assuming the structure did not exist in the first place and saving a new one");
					int newCdId = chemStructureService.saveStructure(cmpdRegMolecule, StructureType.PARENT, false);
					parent.setCdId(newCdId);
					logger.info("Updated parent with new cdId: " + newCdId);
				}

				// Save the standardized structure and possibly the new cdid to the parent
				parent.setMolStructure(standardizedMol);
				parent.merge();
				p++;

			}

			logger.debug("flushing loader session");
			session.flush();
			session.clear();
			
			// Compute your percentage.
			percent = (float) Math.floor(p * 100f / totalCount);
			if (percent != previousPercent) {
				currentTime = new Date().getTime();
				// Output if different from the last time.
				logger.info("parent structure restandardization " + percent + "% complete (" + p + " of "
				+ totalCount + ") average speed (rows/min):"+ (p/((currentTime - startTime) / 60.0 / 1000.0)));
				currentTime = new Date().getTime();
				logger.debug("Time Elapsed:"+ (currentTime - startTime));
			}
			// Update the percentage.
			previousPercent = percent;
		}
		return parentIds.size();
	}

	@Override
	public String executeStandardization(String username, String reason) throws StandardizerException {
		StandardizationHistory standardizationHistory = getMostRecentStandardizationHistory();
		StandardizerSettingsConfigDTO standardizationSettings = chemStructureService.getStandardizerSettings();
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
			standardizationHistory.setStandardizationComplete(new Date());
			standardizationHistory.setStandardizationStatus("failed");
			standardizationHistory.merge();
			return (standardizationHistory.toJson());
		}

		StandardizationSettings stndardizationSettings = this.getStandardizationSettings();
		stndardizationSettings.setNeedsStandardization(false);
		stndardizationSettings.persist();

		standardizationHistory = StandardizationDryRunCompound.addStatsToHistory(standardizationHistory);
		standardizationHistory.setStructuresUpdatedCount(result);
		standardizationHistory.setStandardizationComplete(new Date());
		standardizationHistory.setStandardizationStatus("complete");
		standardizationHistory.setStandardizationUser(username);
		standardizationHistory.setStandardizationReason(reason);
		standardizationHistory.merge();
		this.reset();
		return standardizationHistory.toJson();
	}

	private int runStandardization() throws CmpdRegMolFormatException, IOException, StandardizerException {
		List<Long> parentIds = StandardizationDryRunCompound.findParentIdsWithStandardizationChanges().getResultList();
		logger.info("standardization initialized");
		logger.info("save missing salt structures - started");
		try {
			Collection<Salt> missingSaltStructures = saltStructureService.saveMissingStructures();
			logger.info("saved " + missingSaltStructures.size() + " missing salt structures");
		} catch (StructureSaveException e) {
			throw new StandardizerException(e);
		}
		logger.info("save missing salt structures - complete");
		logger.info("restandardize parent structures - started");
		int result = restandardizeParentStructures(parentIds);
		logger.info("restandardize parent structures - complete");
		return (result);
	}

	@Override
	public String getDryRunStats() throws StandardizerException {
		StandardizerSettingsConfigDTO standardizerConfigs = chemStructureService.getStandardizerSettings();
		StandardizationHistory dryRunStats = new StandardizationDryRunCompound().fetchStats();
		dryRunStats.setSettings(standardizerConfigs.toJson());
		dryRunStats.setSettingsHash(standardizerConfigs.hashCode());
		return dryRunStats.toJson();
	}

	@Override
	public StandardizationSettings getStandardizationSettings() {
		List<StandardizationSettings> standardizationSettingses = StandardizationSettings
				.findAllStandardizationSettingses("modifiedDate", "DESC");
		StandardizationSettings standardizationSettings = new StandardizationSettings();
		if (standardizationSettingses.size() > 0) {
			standardizationSettings = standardizationSettingses.get(0);
		}
		return (standardizationSettings);
	}

	@Override
	public List<StandardizationHistory> getStandardizationHistory() {
		List<StandardizationHistory> standardizationSettingses = StandardizationHistory
				.findStandardizationHistoryEntries(0, 500, "dateOfStandardization", "DESC");
		return standardizationSettingses;
	}

	@Transactional
	private StandardizationHistory setCurrentStandardizationDryRunStatus(String status)  throws StandardizerException{
		StandardizationHistory stndznHistory = getMostRecentStandardizationHistory();
		StandardizerSettingsConfigDTO standardizationSettings = chemStructureService.getStandardizerSettings();
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
		int numberOfDisplayChanges = -1;
		try {
			numberOfDisplayChanges = this.runDryRun();
		} catch (Exception e) {
			logger.error("error running dry run", e);
			stndznHistory.setDryRunComplete(new Date());
			stndznHistory.setDryRunStatus("failed");
			stndznHistory.merge();
			throw e;
		}

		stndznHistory.setDryRunComplete(new Date());
		stndznHistory.setDryRunStatus("complete");
		stndznHistory.merge();
	}

	private int runDryRun() throws CmpdRegMolFormatException, IOException, StandardizerException {
		logger.info("standardization dry run initialized");
		logger.info("step 1/3: resetting dry run table");
		this.reset();
		logger.info("step 2/3: populating dry run table");
		int numberOfDisplayChanges = this.populateStandardizationDryRunTable();
		logger.info("step 3/3: checking for standardization duplicates");
		numberOfDisplayChanges = this.dupeCheckStandardizationStructures();
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
