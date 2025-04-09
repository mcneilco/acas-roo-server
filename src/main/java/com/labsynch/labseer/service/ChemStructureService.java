package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule.RegistrationStatus;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.StandardizationDryRunCompound;
import com.labsynch.labseer.domain.StandardizationDryRunCompound.SyncStatus;
import com.labsynch.labseer.domain.StandardizationHistory;
import com.labsynch.labseer.dto.MolConvertOutputDTO;
import com.labsynch.labseer.dto.StandardizationSettingsConfigCheckResponseDTO;
import com.labsynch.labseer.dto.StrippedSaltDTO;
import com.labsynch.labseer.dto.configuration.StandardizerSettingsConfigDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public interface ChemStructureService {

	Logger logger = LoggerFactory.getLogger(StandardizationServiceImpl.class);

	public CmpdRegMolecule[] searchMols(String molfile, StructureType structureType, int[] cdHitList,
			SearchType searchType, Float simlarityPercent) throws CmpdRegMolFormatException;

	public CmpdRegMolecule[] searchMols(String molfile, StructureType structureType, int[] inputCdIdHitList,
			SearchType searchType, Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException;

	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType)
			throws CmpdRegMolFormatException;

	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType,
			Float simlarityPercent) throws CmpdRegMolFormatException;

	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType,
			Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException;

	public int saveStructure(String molfile, StructureType structureType);

	public int saveStructure(String molfile, StructureType structureType, boolean checkForDupes);

	void closeConnection();

	public boolean truncateStructureTable(StructureType structureType);

	public double getMolWeight(String molStructure) throws CmpdRegMolFormatException;

	public CmpdRegMolecule toMolecule(String molStructure) throws CmpdRegMolFormatException;

	public String toMolfile(String molStructure) throws CmpdRegMolFormatException;

	public String toSmiles(String molStructure) throws CmpdRegMolFormatException;

	public int[] checkDupeMol(String molStructure, StructureType structureType) throws CmpdRegMolFormatException;

	public String toInchi(String molStructure);

	public boolean updateStructure(String molStructure, StructureType structureType, int cdId);

	public boolean updateStructure(CmpdRegMolecule mol, StructureType structureType, int cdId);

	public String getMolFormula(String molStructure) throws CmpdRegMolFormatException;

	boolean checkForSalt(String molfile) throws CmpdRegMolFormatException;

	public List<Boolean> checkForSalts(Collection<String> molfiles) throws CmpdRegMolFormatException;

	double getExactMass(String molStructure) throws CmpdRegMolFormatException;

	boolean deleteStructure(StructureType structureType, int cdId);

	MolConvertOutputDTO toFormat(String structure, String inputFormat, String outputFormat)
			throws IOException, CmpdRegMolFormatException;

	MolConvertOutputDTO cleanStructure(String structure, int dim, String opts)
			throws IOException, CmpdRegMolFormatException;

	String hydrogenizeMol(String structure, String inputFormat, String method)
			throws IOException, CmpdRegMolFormatException;

	String getCipStereo(String structure) throws IOException, CmpdRegMolFormatException;

	StrippedSaltDTO stripSalts(CmpdRegMolecule inputStructure) throws CmpdRegMolFormatException;

	public String standardizeStructure(String molfile)
			throws CmpdRegMolFormatException, StandardizerException, IOException;

	public HashMap<String, CmpdRegMolecule> standardizeStructures(HashMap<String, String> structures)
			throws CmpdRegMolFormatException, StandardizerException;

	public boolean compareStructures(String preMolStruct, String postMolStruct, SearchType searchType);

	public boolean standardizedMolCompare(String queryMol, String targetMol) throws CmpdRegMolFormatException;

	public boolean isEmpty(String molFile) throws CmpdRegMolFormatException;

	public StandardizerSettingsConfigDTO getStandardizerSettings(Boolean appliedSettings) throws StandardizerException;

	public enum SearchType {
		DUPLICATE, DUPLICATE_TAUTOMER, DUPLICATE_NO_TAUTOMER, STEREO_IGNORE, FULL_TAUTOMER,
		SUBSTRUCTURE, SIMILARITY, FULL, EXACT, DEFAULT;

		// IGNORES case on purpose
		public static Optional<SearchType> getIfPresent(String str) {
			return Arrays.stream(SearchType.values())
					.filter(type -> str.trim().equalsIgnoreCase(type.name()))
					.findFirst();
		}
	}

	public enum StructureType {
		PARENT("PARENT"), SALT("SALT"), SALT_FORM("SALT_FORM"), DRY_RUN("DRY_RUN_COMPOUND"), COMPOUND("COMPOUND"),
		STANDARDIZATION_DRY_RUN("STANDARDIZATION_DRY_RUN_COMPOUND");

		// Note the entity table like "PARENT", "SALT_FORM"...etc.
		// are where the actual entity is stored. This can differ from
		// what the implementation often call the "structure table". The Structure
		// table may or may not exist as part of the chemical entities implementation.
		// e.g. Indigo stores structures directly in the parent table indexed by the
		// bingo
		// cartridge. Jchem uses a table called "parent_structure".
		public final String entityTable;

		private StructureType(String entityTable) {
			this.entityTable = entityTable;
		}

		// IGNORES case on purpose
		public static Optional<StructureType> getIfPresent(String str) {
			return Arrays.stream(StructureType.values())
					.filter(type -> str.trim().equalsIgnoreCase(type.name()))
					.findFirst();
		}
	}

	default boolean isIdenticalDisplay(String molStructure, String molStructure2) {
		// strip the first 2 lines of each mol and do a string equals
		String mol1 = molStructure.substring(StringUtils.ordinalIndexOf(molStructure, "\n", 2) + 1);
		String mol2 = molStructure2.substring(StringUtils.ordinalIndexOf(molStructure2, "\n", 2) + 1);
		return (mol1.equals(mol2));
	}

	public int saveStructure(CmpdRegMolecule cmpdregMolecule, StructureType structureType, boolean checkForDupes);

	public HashMap<String, Integer> saveStructures(HashMap<String, CmpdRegMolecule> structures,
			StructureType structureType, Boolean checkForDupes);

	public HashMap<String, CmpdRegMolecule> getCmpdRegMolecules(HashMap<String, Integer> keyIdToStructureId,
			StructureType structureType) throws CmpdRegMolFormatException;

	public int[] searchMolStructures(CmpdRegMolecule cmpdRegMolecule, StructureType structureType,
			SearchType searchType, Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException;

	public void fillMissingStructures() throws CmpdRegMolFormatException;

	public StandardizationSettingsConfigCheckResponseDTO checkStandardizerSettings(StandardizationHistory mostRecentStandardizationHistory, StandardizerSettingsConfigDTO standardizationSettingsConfigDTO);
	
	default void populateDuplicateChangedStructures(int batchSize) {
		// Warn that the default implementation has not been tested thoroughly
		logger.warn("Default implementation of populateDuplicateChangedStructures has not been tested.");
		while(true) {
			logger.info("Starting duplicate check for standardization dry run structures");
			int processedCount = dupeCheckStandardizationStructuresBatch(batchSize);
			int remainingToBeProcessed = StandardizationDryRunCompound.countUnprocessedDryRunStandardizationIds();
			if(processedCount > 0) {
				int totalProcessed = StandardizationDryRunCompound.rowCount() - remainingToBeProcessed;
				double percentProcessed = (double) totalProcessed / (double) (totalProcessed + remainingToBeProcessed) * 100.0;
				logger.info("Processed " + processedCount + " dry run compounds, total processed: " + totalProcessed
						+ ", remaining to be processed: " + remainingToBeProcessed
						+ ", percent processed: " + String.format("%.2f", percentProcessed) + "%");
			} else if (remainingToBeProcessed == 0) {
				logger.info("No more dry run compounds to process for duplicate check, exiting");
				break;
			}
			break;
		}
	}

	private int dupeCheckStandardizationStructuresBatch(int batchSize) {
		List<Long> parentIds = StandardizationDryRunCompound.fetchUnprocessedParentIds(batchSize);
		List<Long> processedDryRunIds = new ArrayList<Long>();
		int processedCount = parentIds.stream()
			.mapToInt(parentId -> {
				try {
					Long processedDryRunId = dupeCheckStandardizationStructure(parentId);
					processedDryRunIds.add(processedDryRunId);
					return 1;
				} catch(Exception e) {
					logger.error("Error checking for duplicates for dry run compound with parent_id: " + parentId, e);
					// In case of an error we return 0 to indicate that this compound was not processed.
					// This will allow the loop to continue processing other compounds.
					throw new RuntimeException(e);
				}
			})
			.sum();

		return processedCount;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	private Long dupeCheckStandardizationStructure(Long parentId) throws CmpdRegMolFormatException {
		int[] hits;
		StandardizationDryRunCompound dryRunCompound;
		String newDuplicateCorpNames = "";
		String oldDuplicateCorpNames = "";
		int newDupeCount = 0;
		int oldDuplicateCount = 0;
		String tmpStructureKey = "TmpStructureKey01";

		dryRunCompound = StandardizationDryRunCompound.findStandardizationDryRunCompoundByParentId(parentId);

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
			HashMap<String, CmpdRegMolecule> standardizationDryRunMolecules = getCmpdRegMolecules(
				dryRunChemStructureHashMap,
					StructureType.STANDARDIZATION_DRY_RUN);

			// Query for structure matches against Dry Run Standarization
			// Pass -1F for simlarityPercent (non nullable int required in function
			// signature not used in DUPLICATE_TAUTOMER searches)
			// Pass -1 for maxResults (non nullable int required in function signature we
			// don't want to limit the hit counts here)
			hits = searchMolStructures(standardizationDryRunMolecules.get(tmpStructureKey),
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
			hits = searchMolStructures(standardizationDryRunMolecules.get(tmpStructureKey),
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
			HashMap<String, CmpdRegMolecule> parentCmpdRegMolecules = getCmpdRegMolecules(
				parentStructureHashMap,
					StructureType.PARENT);

			// Query for structure matches against Parent
			// Pass -1F for simlarityPercent (non nullable int required in function
			// signature not used in DUPLICATE_TAUTOMER searches)
			// Pass -1 for maxResults (non nullable int required in function signature we
			// don't want to limit the hit counts here)
			hits = searchMolStructures(parentCmpdRegMolecules.get(tmpStructureKey),
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
		return (dryRunCompound.getId());
	}
}
