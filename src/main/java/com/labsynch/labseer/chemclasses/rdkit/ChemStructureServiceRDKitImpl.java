package com.labsynch.labseer.chemclasses.rdkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.dto.MolConvertOutputDTO;
import com.labsynch.labseer.dto.StrippedSaltDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ChemStructureService;

import org.springframework.jdbc.core.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.RDKit.*;

@Component
public class ChemStructureServiceRDKitImpl implements ChemStructureService {

	Logger logger = LoggerFactory.getLogger(ChemStructureServiceRDKitImpl.class);

	@Autowired
	private JdbcTemplate basicJdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.basicJdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate()
	{
		return basicJdbcTemplate;
	}
	
	@Override
	public int getCount(String structureTable) {
		// Count the number of rows in the structure table
		String sql = "select count(*) from " + structureTable;
		
		int count;
		Integer countInt = basicJdbcTemplate.queryForObject(sql, Integer.class);
		if (countInt == null){
			count = 0;
		} else {
			count = countInt;
		}

		return count;
	}

	@Override
	public int saveStructure(String molfile, String structureTable) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] searchMolStructures(String molfile, String structureTable, String searchType, Float simlarityPercent)
			throws CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeConnection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int[] searchMolStructures(String molfile, String structureTable, String searchType)
			throws CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] searchMolStructures(String molfile, String structureTable, String plainTable, String searchType,
			Float simlarityPercent) throws CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean dropJChemTable(String tableName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] searchMolStructures(String molfile, String structureTable, String plainTable, String searchType)
			throws CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createJChemTable(String tableName, boolean tautomerDupe) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int saveStructure(String molfile, String structureTable, boolean checkForDupes) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CmpdRegMolecule[] searchMols(String molfile, String structureTable, int[] cdHitList, String plainTable,
			String searchType, Float simlarityPercent) throws CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getMolWeight(String molStructure) throws CmpdRegMolFormatException {
		// Calculates the average molecular weight of a molecule
		RWMol mol = RWMol.MolFromMolBlock(molStructure);
		return RDKFuncs.calcAMW(mol, false);
	}

	@Override
	public CmpdRegMolecule toMolecule(String molStructure) throws CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toMolfile(String molStructure) throws CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toSmiles(String molStructure) throws CmpdRegMolFormatException {
		RWMol mol = RWMol.MolFromMolBlock(molStructure);
		return RDKFuncs.MolToSmiles(mol);
	}

	@Override
	public boolean createJchemPropertyTable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] checkDupeMol(String molStructure, String structureTable, String plainTable)
			throws CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toInchi(String molStructure) {
		RWMol mol = RWMol.MolFromMolBlock(molStructure);
		return RDKFuncs.MolToSmiles(mol);
	}

	@Override
	public boolean updateStructure(String molStructure, String structureTable, int cdId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getMolFormula(String molStructure) throws CmpdRegMolFormatException {
		RWMol mol = RWMol.MolFromMolBlock(molStructure);
		return RDKFuncs.calcMolFormula(mol);
	}

	@Override
	public boolean deleteAllJChemTableRows(String tableName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteJChemTableRows(String tableName, int[] cdIds) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkForSalt(String molfile) throws CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateStructure(CmpdRegMolecule mol, String structureTable, int cdId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getExactMass(String molStructure) throws CmpdRegMolFormatException {
		RWMol mol = RWMol.MolFromMolBlock(molStructure);
		return RDKFuncs.calcExactMW(mol, true);
	}

	@Override
	public boolean deleteStructure(String structureTable, int cdId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CmpdRegMolecule[] searchMols(String molfile, String structureTable, int[] inputCdIdHitList,
			String plainTable, String searchType, Float simlarityPercent, int maxResults)
			throws CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] searchMolStructures(String molfile, String structureTable, String plainTable, String searchType,
			Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MolConvertOutputDTO toFormat(String structure, String inputFormat, String outputFormat)
			throws IOException, CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MolConvertOutputDTO cleanStructure(String structure, int dim, String opts)
			throws IOException, CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String hydrogenizeMol(String structure, String inputFormat, String method)
			throws IOException, CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCipStereo(String structure) throws IOException, CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StrippedSaltDTO stripSalts(CmpdRegMolecule inputStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeRDKitImpl molWrapper = (CmpdRegMoleculeRDKitImpl) inputStructure;
		CmpdRegMoleculeRDKitImpl mol = molWrapper.clone();
		RWMol clone = mol.molecule;
		List<CmpdRegMoleculeRDKitImpl> allFrags = new ArrayList<CmpdRegMoleculeRDKitImpl>();
	    ROMol_Vect frags = RDKFuncs.getMolFrags(clone);
		for (int i = 0; i < frags.size(); i++) {
			RWMol frag = (RWMol) frags.get(i);
			CmpdRegMoleculeRDKitImpl fragWrapper = new CmpdRegMoleculeRDKitImpl(frag);
			allFrags.add(fragWrapper);
		}
	
		Map<Salt, Integer> saltCounts = new HashMap<Salt, Integer>();
		Set<CmpdRegMoleculeRDKitImpl> unidentifiedFragments = new HashSet<CmpdRegMoleculeRDKitImpl>();
		for (CmpdRegMoleculeRDKitImpl fragment : allFrags){
			int[] cdIdMatches = searchMolStructures(fragment.getMolStructure(), "Salt_Structure", "DUPLICATE_TAUTOMER");
			if (cdIdMatches.length>0){
				Salt foundSalt = Salt.findSaltsByCdId(cdIdMatches[0]).getSingleResult();
				if (saltCounts.containsKey(foundSalt)) saltCounts.put(foundSalt, saltCounts.get(foundSalt)+1);
				else saltCounts.put(foundSalt, 1);
			}else{
				unidentifiedFragments.add(fragment);
			}
		}
		StrippedSaltDTO resultDTO = new StrippedSaltDTO();
		resultDTO.setSaltCounts(saltCounts);
		resultDTO.setUnidentifiedFragments(unidentifiedFragments);
		logger.debug("Identified stripped salts:");
		for (Salt salt : saltCounts.keySet()){
			logger.debug("Salt Abbrev: "+salt.getAbbrev());
			logger.debug("Salt Count: "+ saltCounts.get(salt));
		}
		return resultDTO;
	}

	@Override
	public String standardizeStructure(String molfile) throws CmpdRegMolFormatException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean compareStructures(String preMolStruct, String postMolStruct, String string) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean standardizedMolCompare(String queryMol, String targetMol) throws CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty(String molFile) throws CmpdRegMolFormatException {
		// TODO Auto-generated method stub
		return false;
	}

}

