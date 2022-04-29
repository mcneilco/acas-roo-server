package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.dto.MolConvertOutputDTO;
import com.labsynch.labseer.dto.StrippedSaltDTO;
import com.labsynch.labseer.dto.configuration.StandardizerSettingsConfigDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public interface ChemStructureService {

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

	public StandardizerSettingsConfigDTO getStandardizerSettings() throws StandardizerException;

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

}
