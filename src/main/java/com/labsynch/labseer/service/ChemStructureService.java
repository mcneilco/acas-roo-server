package com.labsynch.labseer.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.dto.MolConvertOutputDTO;
import com.labsynch.labseer.dto.StrippedSaltDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;

@Service
public interface ChemStructureService {


	public int getCount(String structureTable);

	public int saveStructure(String molfile, String structureTable);

	public int[] searchMolStructures(String molfile, String structureTable, String searchType, Float simlarityPercent) throws CmpdRegMolFormatException;

	void closeConnection();

	public int[] searchMolStructures(String molfile, String structureTable, String searchType) throws CmpdRegMolFormatException;

	public int[] searchMolStructures(String molfile, String structureTable,
			String plainTable, String searchType, Float simlarityPercent) throws CmpdRegMolFormatException;

	public boolean dropJChemTable(String tableName);

	public int[] searchMolStructures(String molfile, String structureTable,
			String plainTable, String searchType) throws CmpdRegMolFormatException;

	public boolean createJChemTable(String tableName, boolean tautomerDupe);

	public int saveStructure(String molfile, String structureTable, boolean checkForDupes);

	public CmpdRegMolecule[] searchMols(String molfile, String structureTable,
			int[] cdHitList, String plainTable, String searchType,
			Float simlarityPercent) throws CmpdRegMolFormatException;

	public double getMolWeight(String molStructure) throws CmpdRegMolFormatException;

	public CmpdRegMolecule toMolecule(String molStructure) throws CmpdRegMolFormatException;

	public String toMolfile(String molStructure) throws CmpdRegMolFormatException;

	public String toSmiles(String molStructure) throws CmpdRegMolFormatException;

	public boolean createJchemPropertyTable();


	public int[] checkDupeMol(String molStructure, String structureTable, String plainTable) throws CmpdRegMolFormatException;

	public String toInchi(String molStructure);

	public boolean updateStructure(String molStructure, String structureTable, int cdId);

	public String getMolFormula(String molStructure) throws CmpdRegMolFormatException;

	public boolean deleteAllJChemTableRows(String tableName);

	public boolean deleteJChemTableRows(String tableName, int[] cdIds);

	boolean checkForSalt(String molfile) throws CmpdRegMolFormatException;

	public boolean updateStructure(CmpdRegMolecule mol, String structureTable, int cdId);
	double getExactMass(String molStructure) throws CmpdRegMolFormatException;

	boolean deleteStructure(String structureTable, int cdId);

	public CmpdRegMolecule[] searchMols(String molfile, String structureTable,
			int[] inputCdIdHitList, String plainTable, String searchType,
			Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException;

	public int[] searchMolStructures(String molfile, String structureTable,
			String plainTable, String searchType, Float simlarityPercent,
			int maxResults) throws CmpdRegMolFormatException;

	MolConvertOutputDTO toFormat(String structure, String inputFormat, String outputFormat) throws IOException, CmpdRegMolFormatException;

	MolConvertOutputDTO cleanStructure(String structure, int dim, String opts) throws IOException, CmpdRegMolFormatException;

	String hydrogenizeMol(String structure, String inputFormat, String method) throws IOException, CmpdRegMolFormatException;

	String getCipStereo(String structure) throws IOException, CmpdRegMolFormatException;

	StrippedSaltDTO stripSalts(CmpdRegMolecule inputStructure) throws CmpdRegMolFormatException;

	public String standardizeStructure(String molfile) throws CmpdRegMolFormatException, StandardizerException, IOException;

	public boolean compareStructures(String preMolStruct, String postMolStruct, String string);

	public boolean standardizedMolCompare(String queryMol, String targetMol) throws CmpdRegMolFormatException;

	public boolean isEmpty(String molFile) throws CmpdRegMolFormatException;

	default boolean isIdenticalDisplay(String molStructure, String molStructure2) {
		//strip the first 2 lines of each mol and do a string equals
		String mol1 = molStructure.substring(StringUtils.ordinalIndexOf(molStructure, "\n", 2)+1);
		String mol2 = molStructure2.substring(StringUtils.ordinalIndexOf(molStructure2, "\n", 2)+1);
		return(mol1.equals(mol2));
	}
	
}
