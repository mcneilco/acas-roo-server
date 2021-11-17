package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.domain.StandardizationDryRunCompound;
import com.labsynch.labseer.domain.StandardizationHistory;
import com.labsynch.labseer.domain.StandardizationSettings;
import com.labsynch.labseer.dto.configuration.StandardizerSettingsConfigDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;
import com.labsynch.labseer.service.ChemStructureService.SearchType;
import com.labsynch.labseer.service.ChemStructureService.StructureType;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
public class StandardizationServiceImpl implements StandardizationService, ApplicationListener<ContextRefreshedEvent> {

	Logger logger = LoggerFactory.getLogger(StandardizationServiceImpl.class);

	@Autowired
	public ChemStructureService chemStructureService;
	
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
		logger.info("Checking compound standardization state");

		try {
			//Get the current configuration settings
			StandardizerSettingsConfigDTO currentStandardizationSettings = chemStructureService.getStandardizerSettings();

			//Get the applied settings from the history table which should have the most recent standardization settings applied
			StandardizationHistory appliedStandardizationSettings = getMostRecentStandardizationHistory();

			if(appliedStandardizationSettings != null) {
				// If we have applied standardization settings previously 
				if(currentStandardizationSettings.hashCode() != appliedStandardizationSettings.getSettingsHash()) {
					// If the applied settings are different from the current settings
					logger.info("Standardizer configs have changed, marking 'standardization needed' according to configs as " +currentStandardizationSettings.getShouldStandardize());

					// We should only ever really have one standardization settings row at any given time
					List<StandardizationSettings> standardizationSettingses = StandardizationSettings.findAllStandardizationSettingses();

					if(standardizationSettingses.size() == 0) {
						// If we have no standardization settings rows, create one and set the value to shouldStandardize to whatever the current settings say
						// Note - this is a weird state,  if we have previously applied standardized settings then we should have a standardization settings row
						StandardizationSettings standardizationSettings = new StandardizationSettings();
						standardizationSettings.setNeedsStandardization(currentStandardizationSettings.getShouldStandardize());
						standardizationSettings.setModifiedDate(new Date());
						standardizationSettings.persist();
					} else {

						// If there is more than 0, then just update the current row
						standardizationSettingses.get(0).setNeedsStandardization(currentStandardizationSettings.getShouldStandardize());
						standardizationSettingses.get(0).setModifiedDate(new Date());
						standardizationSettingses.get(0).merge();
					}
				} else{
					logger.info("Standardizer configs have not changed, not marking 'standardization needed'");
				}
			} else {
				logger.info("No standardization history found in database");
				StandardizationSettings standardizationSettings = new StandardizationSettings();
				standardizationSettings.setModifiedDate(new Date());
				Long numberOfParents = Parent.countParents();
				if(numberOfParents == 0.0) {
					logger.info("There are no parents registered, we can assume standardization configs match the database standardization state so storing configs as the standardization settings");
					standardizationSettings.setNeedsStandardization(false);
					standardizationSettings.persist();
					logger.info("Saved current standardization settings");
					executeStandardization("acas", "Initialization");
				} else {
					logger.warn("Standardization is in an unknown state because there are no rows in standardization history table.");
					if(currentStandardizationSettings.getShouldStandardize() == false) {
						logger.info("Standardization is turned off so marking the database as not requiring standardization at this time");
						standardizationSettings.setNeedsStandardization(false);
						standardizationSettings.persist();
					} else {
						logger.warn("Standardization is turned on so we don't know the current database standardization state. Assuming no standardization is needed.");
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
	@Override
	public int populateStanardizationDryRunTable() throws CmpdRegMolFormatException, IOException, StandardizerException{
		List<Long> parentIds = Parent.getParentIds();
		Parent parent;
		StandardizationDryRunCompound stndznCompound;
		int nonMatchingCmpds = 0;
		int totalCount = parentIds.size();
		logger.info("number of parents to check: " + totalCount);
		Date qcDate = new Date();
		String asDrawnStruct;
		Integer cdId = 0 ;
		List<Lot> queryLots;
		Integer runNumber = StandardizationDryRunCompound.findMaxRunNumber().getSingleResult();
		if (runNumber == null){
			runNumber = 1;
		} else {
			runNumber++;
		}
		float percent = 0;
		int p = 1;
	    float previousPercent = percent;
		previousPercent = percent;
		for  (Long parentId : parentIds){
			parent = Parent.findParent(parentId);
			stndznCompound = new StandardizationDryRunCompound();
			stndznCompound.setRunNumber(runNumber);
			stndznCompound.setQcDate(qcDate);
			stndznCompound.setParentId(parent.getId());
			stndznCompound.setCorpName(parent.getCorpName());
			stndznCompound.setAlias(getParentAlias(parent));
			stndznCompound.setStereoCategory(parent.getStereoCategory().getName());
			stndznCompound.setStereoComment(parent.getStereoComment());
			stndznCompound.setOldMolWeight(parent.getMolWeight());
			
			queryLots = Lot.findLotByParentAndLowestLotNumber(parent).getResultList();
			if (queryLots.size() != 1) logger.error("!!!!!!!!!!!!  odd lot number size   !!!!!!!!!  " + queryLots.size() + "  saltForm: " + parent.getId());
			if (queryLots.size() > 0 && queryLots.get(0).getAsDrawnStruct() != null){
				asDrawnStruct = queryLots.get(0).getAsDrawnStruct();
			} else {
				asDrawnStruct = parent.getMolStructure();
			}
			logger.debug("attempting to standardize: " + parent.getCorpName() + "   " + asDrawnStruct);
			stndznCompound.setMolStructure(chemStructureService.standardizeStructure(asDrawnStruct));				
			stndznCompound.setNewMolWeight(chemStructureService.getMolWeight(stndznCompound.getMolStructure()));
			
			if(parent.getMolWeight() == 0 && stndznCompound.getNewMolWeight() == 0) {
				logger.debug("mol weight 0 before and after standardization - skipping");

			} else {
				boolean displayTheSame = chemStructureService.isIdenticalDisplay(parent.getMolStructure(), stndznCompound.getMolStructure());
				if (!displayTheSame){
					stndznCompound.setDisplayChange(true);
					logger.debug("the compounds are NOT matching: " + parent.getCorpName());
					nonMatchingCmpds++;
				}
				boolean asDrawnDisplaySame = chemStructureService.isIdenticalDisplay(asDrawnStruct, stndznCompound.getMolStructure());
				if (!asDrawnDisplaySame){
					stndznCompound.setAsDrawnDisplayChange(true);
					logger.debug("the compounds are NOT matching: " + parent.getCorpName());
					nonMatchingCmpds++;
				}
			}
			cdId = chemStructureService.saveStructure(stndznCompound.getMolStructure(), StructureType.DRY_RUN, false);
			if (cdId == -1){
				logger.error("Bad molformat. Please fix the molfile: " + stndznCompound.getMolStructure());
			} else {
				stndznCompound.setCdId(cdId);
				stndznCompound.persist();
			}
			// Compute your percentage.
			percent = (float)Math.floor(p * 100f / totalCount);
			if(percent != previousPercent)
			{
			    // Output if different from the last time.
			    logger.info("populating standardization dry run table " + percent + "% complete");
			}
			// Update the percentage.
			previousPercent = percent;
			p++;
		}
		logger.info("total number of non matching; structure, display or as drawn display changes: " + nonMatchingCmpds);
		return (nonMatchingCmpds);
	}

	@Transactional
	private String getParentAlias(Parent parent) {
		StringBuilder aliasSB = new StringBuilder();
		Set<ParentAlias> parentAliases = parent.getParentAliases();
		boolean firstAlias = true;
		for (ParentAlias parentAlias : parentAliases){
			if (firstAlias){
				aliasSB.append(parentAlias.getAliasName());
				firstAlias = false;
			} else {
				aliasSB.append(";").append(parentAlias.getAliasName());
			}
		}
		return aliasSB.toString();
	}


	@Override
	public int dupeCheckStandardizationStructures() throws CmpdRegMolFormatException{
		List<Long> qcIds = StandardizationDryRunCompound.findAllIds().getResultList();
		int totalCount = qcIds.size();
		logger.debug("number of compounds found in dry run table: " + totalCount);
		int totalNewDuplicateCount = 0;
		int totalExistingDuplicateCount = 0;
		if (qcIds.size() > 0){
			int[] hits;	
			StandardizationDryRunCompound qcCompound;
			String newDuplicateCorpNames = "";
			String oldDuplicateCorpNames = "";
			int newDupeCount = 0;
			int oldDuplicateCount = 0;
			
			float percent = 0;
			int p = 1;
		    float previousPercent = percent;
			previousPercent = percent;
			for (Long qcId : qcIds){
				boolean firstNewDuplicateHit = true;
				boolean firstOldDuplicateHit = true;
				qcCompound = StandardizationDryRunCompound.findStandardizationDryRunCompound(qcId);
				logger.debug("query compound: " + qcCompound.getCorpName());
				if(qcCompound.getNewMolWeight() == 0) {
					logger.debug("mol has a weight of 0 - skipping");			
				} else {
					hits = chemStructureService.searchMolStructures(qcCompound.getMolStructure(), StructureType.DRY_RUN, SearchType.DUPLICATE_TAUTOMER);
					newDupeCount = hits.length;
					qcCompound.setChangedStructure(true);
					for (int hit:hits){
						List<StandardizationDryRunCompound> searchResults = StandardizationDryRunCompound.findStandardizationDryRunCompoundsByCdId(hit).getResultList();
						for (StandardizationDryRunCompound searchResult : searchResults){
							if (searchResult.getCorpName().equalsIgnoreCase(qcCompound.getCorpName())){
								qcCompound.setChangedStructure(false);
								newDupeCount = newDupeCount-1;
							} else {
								if (StringUtils.equals(searchResult.getStereoCategory(), qcCompound.getStereoCategory())
										&& StringUtils.equalsIgnoreCase(searchResult.getStereoComment(), qcCompound.getStereoComment())){
									if (!firstNewDuplicateHit) newDuplicateCorpNames = newDuplicateCorpNames.concat(";");
									newDuplicateCorpNames = newDuplicateCorpNames.concat(searchResult.getCorpName());
									firstNewDuplicateHit = false;
									logger.debug("found new dupe parents");
									logger.debug("query: " + qcCompound.getCorpName() + "     dupe: " + searchResult.getCorpName());
									totalNewDuplicateCount++;
								} else {
									logger.debug("found different stereo codes and comments");
								}
							}
						}
					}
					
					hits = chemStructureService.searchMolStructures(qcCompound.getMolStructure(), StructureType.PARENT, SearchType.DUPLICATE_TAUTOMER);
					oldDuplicateCount = hits.length;
					for (int hit:hits){
						List<Parent> searchResults = Parent.findParentsByCdId(hit).getResultList();
						for (Parent searchResult : searchResults){
							if (searchResult.getCorpName().equalsIgnoreCase(qcCompound.getCorpName())){
								oldDuplicateCount = oldDuplicateCount-1;
							} else {
								if (StringUtils.equals(searchResult.getStereoCategory().getName(), qcCompound.getStereoCategory())
										&& StringUtils.equalsIgnoreCase(searchResult.getStereoComment(), qcCompound.getStereoComment())){
									if (!firstOldDuplicateHit) oldDuplicateCorpNames = oldDuplicateCorpNames.concat(";");
									oldDuplicateCorpNames = oldDuplicateCorpNames.concat(searchResult.getCorpName());
									firstOldDuplicateHit = false;
									logger.debug("found old dupe parents");
									logger.debug("query: " + qcCompound.getCorpName() + "     dupe: " + searchResult.getCorpName());
									totalExistingDuplicateCount++;
								} else {
									logger.debug("found different stereo codes and comments");
								}
							}
						}
					}
					qcCompound.setNewDuplicateCount(newDupeCount);
					if(!newDuplicateCorpNames.equals("")) {
						qcCompound.setNewDuplicates(newDuplicateCorpNames);
					}
					qcCompound.setExistingDuplicateCount(oldDuplicateCount);
					if(!oldDuplicateCorpNames.equals("")) {
						qcCompound.setExistingDuplicates(oldDuplicateCorpNames);
					}	

				}

				qcCompound.merge();
				newDuplicateCorpNames = "";
				oldDuplicateCorpNames = "";
				
				// Compute your percentage.
				percent = (float)Math.floor(p * 100f / totalCount);
				if(percent != previousPercent)
				{
				    // Output if different from the last time.
				    logger.info("checking for standardization duplicates " + percent + "% complete");
				}
				// Update the percentage.
				previousPercent = percent;
				p++;

			}
						
		}
		return (totalNewDuplicateCount);
	}
	
	@Override
	public String standardizeSingleMol(String mol) throws CmpdRegMolFormatException, StandardizerException, IOException {
		String result = "";
		result = chemStructureService.standardizeStructure(mol);
		return(result);
	}
	
	@Override
	public String getStandardizationDryRunReport() throws StandardizerException, CmpdRegMolFormatException, IOException{
		List<StandardizationDryRunCompound> stndznCompounds = StandardizationDryRunCompound.findStandardizationChanges().getResultList();
		String json = "[]";
		if(stndznCompounds.size() > 0) {
			json = StandardizationDryRunCompound.toJsonArray(stndznCompounds);

		}
		return(json);
	}
	
	@Override
	@Transactional
	public void reset() {
		boolean truncateTable = chemStructureService.truncateStructureTable(StructureType.DRY_RUN);
		StandardizationDryRunCompound.truncateTable();
	}
	
	@Override
	public int restandardizeParentStructures(List<Long> parentIds) throws CmpdRegMolFormatException, StandardizerException, IOException {
		Parent parent;
		List<Lot> lots;
		Lot lot;
		String originalStructure = null;
		String result;
		int totalCount = parentIds.size();
		logger.info("number of parents to restandardize: " + totalCount);
		float percent = 0;
		int p = 1;
		float previousPercent = percent;
		previousPercent = percent;
		for  (Long parentId : parentIds){
			parent = Parent.findParent(parentId);
			lots = Lot.findLotsByParent(parent).getResultList();
			lot = lots.get(0);
			if (lots.size() > 0 && lot.getAsDrawnStruct() != null){
				originalStructure = lot.getAsDrawnStruct();				
			} else {
				logger.warn("Did not find the asDrawnStruct for parent: " + parentId + "  " + parent.getCorpName());
				originalStructure = parent.getMolStructure();
			}
			
			result = chemStructureService.standardizeStructure(originalStructure);

			// Compute your percentage.
			percent = (float)Math.floor(p * 100f / totalCount);
			if(percent != previousPercent)
			{
				// Output if different from the last time.
				logger.info("standardization " + percent + "% complete");
			}
			// Update the percentage.
			previousPercent = percent;
			p++;

		}
		return parentIds.size();
	}

	@Override
	public String executeStandardization(String username, String reason) throws StandardizerException{
		StandardizationHistory standardizationHistory = getMostRecentStandardizationHistory();
		StandardizerSettingsConfigDTO standardizationSettings = chemStructureService.getStandardizerSettings();
		if(standardizationHistory == null || StringUtils.equalsIgnoreCase(standardizationHistory.getStandardizationStatus(), "complete")) {
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
			standardizationHistory.persist();
			return(standardizationHistory.toJson());
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
		standardizationHistory.persist();
		this.reset();
		return standardizationHistory.toJson();
	}
	
	@Transactional
	private int runStandardization() throws CmpdRegMolFormatException, IOException, StandardizerException {
		List<Long> parentIds = StandardizationDryRunCompound.findParentIdsWithStandardizationChanges().getResultList();
		logger.info("standardization initialized");
		int result = restandardizeParentStructures(parentIds);
		logger.info("standardization complete");
		return(result);
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
		List<StandardizationSettings> standardizationSettingses = StandardizationSettings.findAllStandardizationSettingses("modifiedDate", "DESC");
		StandardizationSettings standardizationSettings = new StandardizationSettings();
		if(standardizationSettingses.size() > 0) {
			standardizationSettings = standardizationSettingses.get(0);
		}
		return(standardizationSettings);
	}

	@Override
	public List<StandardizationHistory> getStanardizationHistory() {
		List<StandardizationHistory> standardizationSettingses = StandardizationHistory.findStandardizationHistoryEntries(0, 500, "dateOfStandardization", "DESC");
		return standardizationSettingses;
	}

	@Override
	public void executeDryRun() throws CmpdRegMolFormatException, IOException, StandardizerException {
		StandardizationHistory stndznHistory = getMostRecentStandardizationHistory();
		StandardizerSettingsConfigDTO standardizationSettings = chemStructureService.getStandardizerSettings();
		if(stndznHistory == null || StringUtils.equalsIgnoreCase(stndznHistory.getStandardizationStatus(), "complete")) {
			stndznHistory = new StandardizationHistory();
			stndznHistory.setSettings(standardizationSettings.toJson());
			stndznHistory.setSettingsHash(standardizationSettings.hashCode());
			stndznHistory.setRecordedDate(new Date());			
		}
		stndznHistory.setDryRunStatus("running");
		stndznHistory.setDryRunStart(new Date());
		stndznHistory.setDryRunComplete(null);
		stndznHistory.persist();
		
	    int numberOfDisplayChanges = this.runDryRun();
	    
		stndznHistory.setDryRunComplete(new Date());
		stndznHistory.setDryRunStatus("complete");
		stndznHistory.persist();
	}
	
	@Transactional
	private int runDryRun() throws CmpdRegMolFormatException, IOException, StandardizerException {
		logger.info("standardization dry run initialized");
		logger.info("step 1/3: resetting dry run table");
		this.reset();
		logger.info("standardization dry run 1% complete");
		logger.info("step 2/3: populating dry run table");
		int numberOfDisplayChanges = this.populateStanardizationDryRunTable();  
		logger.info("standardization dry run 20% complete");
		logger.info("step 3/3: checking for standardization duplicates");
	    numberOfDisplayChanges = this.dupeCheckStandardizationStructures();
		logger.info("standardization dry run complete");
	    return(numberOfDisplayChanges);
	}
	
	
	public StandardizationHistory getMostRecentStandardizationHistory() {
		List<StandardizationHistory> standardizationSettingses = StandardizationHistory.findStandardizationHistoryEntries(0, 1, "id", "DESC");
		if(standardizationSettingses.size() > 0) {
			return standardizationSettingses.get(0);
		} else {
			return null;
		}
	}
}


