package com.labsynch.labseer.chemclasses.indigo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoException;
import com.epam.indigo.IndigoObject;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.dto.MolConvertOutputDTO;
import com.labsynch.labseer.dto.StrippedSaltDTO;
import com.labsynch.labseer.dto.configuration.MainConfigDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.utils.Configuration;

@Component
public class ChemStructureServiceIndigoImpl implements ChemStructureService {

	Logger logger = LoggerFactory.getLogger(ChemStructureServiceIndigoImpl.class);

	private boolean shouldCloseConnection = false;

	public void setShouldCloseConnection(boolean shouldCloseConnection)
	{
		this.shouldCloseConnection = shouldCloseConnection;
	}

	public boolean getShouldCloseConnection()
	{
		return shouldCloseConnection;
	}

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

	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();
	private static String exactSearchDef = mainConfig.getServerSettings().getExactMatchDef();
	private static long maxSearchTime = mainConfig.getServerSettings().getMaxSearchTime();
	private static int maxSearchResults = mainConfig.getServerSettings().getMaxSearchResults();
	private static boolean useStandardizer = mainConfig.getServerSettings().isUseExternalStandardizerConfig();
	private static String standardizerConfigFilePath = mainConfig.getServerSettings().getStandardizerConfigFilePath();
	
	private Indigo indigo = new Indigo();
	
	@Override
	public int getCount(String structureTable) {
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
	public boolean compareStructures(String preMolStruct, String postMolStruct, String searchType){


		//logger.info("SearchType is: " + searchType);
		boolean compoundsMatch = false;
		try {
			IndigoObject queryMol = indigo.loadMolecule(preMolStruct);
			IndigoObject targetMol = indigo.loadMolecule(preMolStruct);

			compoundsMatch = (indigo.exactMatch(queryMol, targetMol, "ALL") != null);
			if (!compoundsMatch){
				logger.info(queryMol.smiles());
				logger.info(targetMol.smiles());				
			}

		} catch (IndigoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return compoundsMatch;
	}



	@Override
	public String standardizeStructure(String molfile) throws CmpdRegMolFormatException{
		//Call method below after parsing molfile
		CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(molfile);
		return standardizeMolecule(molWrapper.molecule).molfile();
	}


	public IndigoObject standardizeMolecule(IndigoObject molecule) {
		//Indigo standardizer configs
		logger.error("Standardizer with Indigo chemistry services is not implemented!");
//		indigo.setOption("standardize-stereo", true);
		molecule.standardize();		
		return molecule;
	}



	@Override
	public int saveStructure(String molfile, String structureTable) {
		boolean checkForDupes = false;
		return saveStructure(molfile, structureTable, checkForDupes);
	}

	@Override
	public int saveStructure(String molfile, String structureTable, boolean checkForDupes) {
		return  (int) Parent.countParents() + 1;
	}

	@Override
	public void closeConnection() {
		//do nothing
	}


	@Override
	public int[] searchMolStructures(String molfile, String structureTable, String searchType) throws CmpdRegMolFormatException {
		String plainTable = null;
		if (structureTable.equalsIgnoreCase("Parent_Structure")){
			plainTable = "parent";
		} else if (structureTable.equalsIgnoreCase("SaltForm_Structure")){
			plainTable = "salt_form";
		} else if (structureTable.equalsIgnoreCase("Salt_Structure")){
			plainTable = "salt";
		} else if (structureTable.equalsIgnoreCase("Dry_Run_Compound_Structure")){
			plainTable = "dry_run_compound";
		}



		return searchMolStructures(molfile, structureTable, plainTable, searchType);	
	}

	@Override
	public int[] searchMolStructures(String molfile, String structureTable, String searchType, Float simlarityPercent) throws CmpdRegMolFormatException {
		return searchMolStructures(molfile, structureTable, null,searchType, simlarityPercent);	
	}

	@Override
	public int[] searchMolStructures(String molfile, String structureTable, String plainTable, String searchType) throws CmpdRegMolFormatException {
		return searchMolStructures(molfile, structureTable, plainTable, searchType, 0f);	
	}

	@Override
	public int[] searchMolStructures(String molfile, String structureTable, String plainTable, String searchType, Float simlarityPercent) throws CmpdRegMolFormatException {
		int maxResultCount = maxSearchResults;
		return searchMolStructures(molfile, structureTable, plainTable, searchType, simlarityPercent, maxResultCount);	

	}

	@Override
	@Transactional
	public int[] searchMolStructures(String molfile, String structureTable, String plainTable, String searchType, 
			Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException {

		Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());	
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		long maxTime = maxSearchTime;
		int maxResultCount = maxResults;
		//Indigo: the structures in the plainTable are being used. Ignore structureTable from here on out.
		logger.debug("Search table is  " + plainTable);		
		logger.debug("Search type is  " + searchType);		
		logger.debug("Max number of results is  " + maxResults);		

		logger.debug("Dissimilarity similarity is  " + simlarityPercent);		

		float indSimilarity = 1.0f - simlarityPercent;
		logger.debug("Indigo search similarity is  " + indSimilarity);		


		if (searchType.equalsIgnoreCase("EXACT")){
			searchType = exactSearchDef;
		}

		try {
			CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(molfile);
			IndigoObject mol = molWrapper.molecule;
			
			String baseQuery = "SELECT cd_id FROM " + plainTable + " WHERE mol_structure @ ";
			String bingoFunction = null;
			String orderBy = " ORDER BY cd_id";
			
			if (useStandardizer){
				mol = standardizeMolecule(mol);
				mol.dearomatize();				
			} else {
				mol.dearomatize();				
			}

			logger.debug("definition of exact search: " + exactSearchDef);
			logger.debug("selected search type: " + searchType);
			
			if (searchType.equalsIgnoreCase("SUBSTRUCTURE")) {
				bingoFunction = "cast( ( :queryMol , :parameters ) as bingo.sub)";
			}else if (searchType.equalsIgnoreCase("SIMILARITY")) {
				bingoFunction = "cast( ( :minSimilarity , :maxSimilarity , :queryMol , :metric ) as bingo.sim)";
				orderBy = " ORDER BY "+bingoFunction;
			}else { 
				bingoFunction = "cast(( :queryMol , :parameters ) as bingo.exact)";
			}
			
			EntityManager em = Parent.entityManager();
			Query query = em.createNativeQuery(baseQuery + bingoFunction + orderBy);
			
			query.setParameter("queryMol", mol.molfile());
			query.setMaxResults(maxResults);
			
			//May need additional research / decisions around which options to use
			//Basic Indigo search types corresponding to JChem search types
			//CReg: "DUPLICATE" or "DUPLICATE_NO_TAUTOMER" :: JChem: "DUPLICATE", "TAUTOMER_SEARCH_OFF" :: Bingo.Exact, "ALL"
			//CReg: "DUPLICATE_TAUTOMER" :: JChem: "DUPLICATE", "TAUTOMER_SEARCH_ON" :: Bingo.Exact, "TAU"
			//CReg: "STEREO_IGNORE" :: JChem: "STEREO_IGNORE" :: Bingo.Exact "ALL -STE"
			//(NOT SURE ABOUT THIS) CReg: "FULL_TAUTOMER", or default/unrecognized searchType :: JChem: "FULL", "CHARGE_MATCHING_IGNORE", "ISOTOPE_MATCHING_IGNORE", "STEREO_IGNORE", "TAUTOMER_SEARCH_ON" :: Bingo.Exact "TAU ALL -ELE -MAS -STE"
			//CReg: "SUBSTRUCTURE" :: JChem: "SUBSTRUCTURE" :: Bingo.Sub
			//CReg: "SIMILARITY" :: JChem "SIMILARITY" :: Bingo.Sim > $min
			//CReg: "FULL" :: JChem "FULL", "CHARGE_MATCHING_IGNORE", "ISOTOPE_MATCHING_IGNORE", "STEREO_IGNORE", "TAUTOMER_SEARCH_OFF" :: Bingo.Exact "ALL -ELE -MAS -STE"
			
			if (searchType.equalsIgnoreCase("SUBSTRUCTURE")) {
				query.setParameter("parameters", "");
			}else if (searchType.equalsIgnoreCase("SIMILARITY")) {
				query.setParameter("minSimilarity", indSimilarity);
				query.setParameter("maxSimilarity", 1.0);
				query.setParameter("metric", "Tanimoto");
			}else {
				String parameters = null;
				switch (searchType.toUpperCase()) {
				case "DUPLICATE":
					parameters = "ALL";
					break;
				case "DUPLICATE_TAUTOMER":
					parameters = "TAU";
					break;
				case "STEREO_IGNORE":
					parameters = "ALL -STE";
					break;
				case "FULL_TAUTOMER":
					parameters = "TAU";
					break;
				case "FULL":
					parameters = "ALL -ELE -MAS -STE";
					break;
				default:
					//TODO: this should match FULL_TAUTOMER if possible
					parameters = "ALL";
					break;
				}
				query.setParameter("parameters", parameters);
			}
			if (logger.isDebugEnabled()) logger.debug(query.unwrap(org.hibernate.Query.class).getQueryString());
			//TODO: should do an audit of the search types being used by CReg.
			
			List<Integer> hitListList = query.getResultList();
			
			//if we are searching in DUPLICATE_TAUTOMER mode and the above TAU search returned results
			//we need to also check the stereochemistry matches since we can't do both at once.
			//the overall results should be the intersection of both queries
			if (hitListList.size() > 0 && mol.countAtoms() > 0 && searchType.toUpperCase().equals("DUPLICATE_TAUTOMER")) {
				query.setParameter("parameters", "STE");
				List<Integer> stereoHitListList = query.getResultList();
				hitListList.retainAll(stereoHitListList);
			}
			
			
			if (hitListList.size() > 0){
				logger.debug("found a matching molecule!!!  " + hitListList.size());
			}
			
			int[] hitList = ArrayUtils.toPrimitive(hitListList.toArray(new Integer[hitListList.size()]));

			return hitList;
			
		}catch (JpaSystemException sqlException) {
			logger.error("Caught search error", sqlException);
			Exception rootCause = new Exception(ExceptionUtils.getRootCause(sqlException).getMessage(), ExceptionUtils.getRootCause(sqlException));
			throw new CmpdRegMolFormatException(rootCause);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public boolean checkForSalt(String molfile) throws CmpdRegMolFormatException{
		IndigoObject mol = indigo.loadMolecule(molfile);
		int fragCount = mol.countComponents();
		boolean foundNonCovalentSalt = false;
		if (fragCount > 1){
			foundNonCovalentSalt = true;
		}

		return foundNonCovalentSalt;

	}

	@Override
	public StrippedSaltDTO stripSalts(CmpdRegMolecule inputStructure) throws CmpdRegMolFormatException{
		CmpdRegMoleculeIndigoImpl molWrapper = (CmpdRegMoleculeIndigoImpl) inputStructure;
		IndigoObject rawMolecule = molWrapper.molecule;
		IndigoObject clone = rawMolecule.clone();
		List<CmpdRegMoleculeIndigoImpl> allFrags = new ArrayList<CmpdRegMoleculeIndigoImpl>();
		for (IndigoObject component : clone.iterateComponents()){
			CmpdRegMoleculeIndigoImpl fragMolecule = new CmpdRegMoleculeIndigoImpl(component.clone());
			allFrags.add(fragMolecule);
		}		
		Map<Salt, Integer> saltCounts = new HashMap<Salt, Integer>();
		Set<CmpdRegMoleculeIndigoImpl> unidentifiedFragments = new HashSet<CmpdRegMoleculeIndigoImpl>();
		for (CmpdRegMoleculeIndigoImpl fragment : allFrags){
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
	@Transactional
	public CmpdRegMolecule[] searchMols(String molfile, String structureTable, int[] inputCdIdHitList, String plainTable, String searchType, Float simlarityPercent) throws CmpdRegMolFormatException {
		int maxResults = maxSearchResults;
		return searchMols(molfile, structureTable, inputCdIdHitList, plainTable, searchType, simlarityPercent, maxResults);
	}

	@Override
	@Transactional
	public CmpdRegMolecule[] searchMols(String molfile, String structureTable, int[] inputCdIdHitList, 
			String plainTable, String searchType, Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException {

		Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());	
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		long maxTime = maxSearchTime;
		int maxResultCount = maxResults;
		//Indigo: the structures in the plainTable are being used. Ignore structureTable from here on out.
		logger.debug("Search table is  " + plainTable);		
		logger.debug("Search type is  " + searchType);		
		logger.debug("Max number of results is  " + maxResults);	
		
		logger.debug("Dissimilarity similarity is  " + simlarityPercent);		

		float indSimilarity = 1.0f - simlarityPercent;
		logger.debug("Indigo search similarity is  " + indSimilarity);		


		if (searchType.equalsIgnoreCase("EXACT")){
			searchType = exactSearchDef;
		}
		
		List<CmpdRegMoleculeIndigoImpl> moleculeList = new ArrayList<CmpdRegMoleculeIndigoImpl>();
		try {
			CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(molfile);
			IndigoObject mol = molWrapper.molecule;
			
			String baseQuery = "SELECT cd_id, mol_structure FROM " + plainTable + " WHERE mol_structure @ ";
			String bingoFunction = null;
			String orderBy = " ORDER BY cd_id";
			String filterIdsClause = "";
			
			if (useStandardizer){
				mol = standardizeMolecule(mol);
				mol.aromatize();				
			} else {
				mol.aromatize();				
			}

			logger.debug("definition of exact search: " + exactSearchDef);
			logger.debug("selected search type: " + searchType);
			
			if (searchType.equalsIgnoreCase("SUBSTRUCTURE")) {
				bingoFunction = "cast(( :queryMol , :parameters ) as bingo.sub) ";
			}else if (searchType.equalsIgnoreCase("SIMILARITY")) {
				bingoFunction = "cast( ( :minSimilarity , :maxSimilarity , :queryMol , :metric ) as bingo.sim)";
				orderBy = " ORDER BY "+bingoFunction;
			}else { 
				bingoFunction = "cast( ( :queryMol , :parameters ) as bingo.exact)";
			}
			
			if (inputCdIdHitList != null && inputCdIdHitList.length > 0) filterIdsClause = " AND cd_id IN :filterCdIds ";
			
			EntityManager em = Parent.entityManager();
			Query query = em.createNativeQuery(baseQuery + bingoFunction + filterIdsClause + orderBy);
			
			query.setParameter("queryMol", mol.molfile());
			query.setMaxResults(maxResults);
			
			if (inputCdIdHitList != null && inputCdIdHitList.length > 0) query.setParameter("filterCdIds", Arrays.asList(ArrayUtils.toObject(inputCdIdHitList)));

			//May need additional research / decisions around which options to use
			//Basic Indigo search types corresponding to JChem search types
			//CReg: "DUPLICATE" or "DUPLICATE_NO_TAUTOMER" :: JChem: "DUPLICATE", "TAUTOMER_SEARCH_OFF" :: Bingo.Exact, "ALL"
			//CReg: "DUPLICATE_TAUTOMER" :: JChem: "DUPLICATE", "TAUTOMER_SEARCH_ON" :: Bingo.Exact, "TAU"
			//CReg: "STEREO_IGNORE" :: JChem: "STEREO_IGNORE" :: Bingo.Exact "ALL -STE"
			//(NOT SURE ABOUT THIS) CReg: "FULL_TAUTOMER", or default/unrecognized searchType :: JChem: "FULL", "CHARGE_MATCHING_IGNORE", "ISOTOPE_MATCHING_IGNORE", "STEREO_IGNORE", "TAUTOMER_SEARCH_ON" :: Bingo.Exact "TAU ALL -ELE -MAS -STE"
			//CReg: "SUBSTRUCTURE" :: JChem: "SUBSTRUCTURE" :: Bingo.Sub
			//CReg: "SIMILARITY" :: JChem "SIMILARITY" :: Bingo.Sim > $min
			//CReg: "FULL" :: JChem "FULL", "CHARGE_MATCHING_IGNORE", "ISOTOPE_MATCHING_IGNORE", "STEREO_IGNORE", "TAUTOMER_SEARCH_OFF" :: Bingo.Exact "ALL -ELE -MAS -STE"
			
			if (searchType.equalsIgnoreCase("SUBSTRUCTURE")) {
				query.setParameter("parameters", "");
			}else if (searchType.equalsIgnoreCase("SIMILARITY")) {
				query.setParameter("minSimilarity", indSimilarity);
				query.setParameter("maxSimilarity", 1.0);
				query.setParameter("metric", "Tanimoto");
			}else {
				String parameters = null;
				switch (searchType.toUpperCase()) {
				case "DUPLICATE":
					parameters = "ALL";
					break;
				case "DUPLICATE_TAUTOMER":
					parameters = "TAU";
					break;
				case "STEREO_IGNORE":
					parameters = "ALL -STE";
					break;
				case "FULL_TAUTOMER":
					parameters = "TAU";
					break;
				case "FULL":
					parameters = "ALL -ELE -MAS -STE";
					break;
				default:
					//TODO: this should match FULL_TAUTOMER if possible
					parameters = "ALL";
					break;
				}
				query.setParameter("parameters", parameters);
			}
			if (logger.isDebugEnabled()) logger.debug(query.unwrap(org.hibernate.Query.class).getQueryString());
			//TODO: should do an audit of the search types being used by CReg.
			
			//list of maps of {"cdId": ###, "molStructure": "molfile string"}
			List<Object[]> hitListList = query.getResultList();
			for (Object[] hit : hitListList) {
				Integer cdId = (Integer) hit[0];
				String molStructure = (String)  hit[1];
				CmpdRegMoleculeIndigoImpl molecule = new  CmpdRegMoleculeIndigoImpl(molStructure);
				molecule.setProperty("cd_id", String.valueOf(cdId));
				moleculeList.add(molecule);
			}


			if (moleculeList.size() > 0){
				logger.debug("found a matching molecule!!!  " + moleculeList.size());
			}

			if (this.shouldCloseConnection) {
				conn.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JpaSystemException sqlException) {
			logger.error("Caught search error", sqlException);
			Exception rootCause = new Exception(ExceptionUtils.getRootCause(sqlException).getMessage(), ExceptionUtils.getRootCause(sqlException));
			throw new CmpdRegMolFormatException(rootCause);
		}

		CmpdRegMoleculeIndigoImpl[] moleculeHits = moleculeList.toArray(new CmpdRegMoleculeIndigoImpl[0]);
		
		return moleculeHits;
	}


	@Override
	public CmpdRegMolecule toMolecule(String molStructure) {
		boolean badStructureFlag = false;
		IndigoObject mol = null;
		String lineEnd = System.getProperty("line.separator");
		try {
			mol = indigo.loadMolecule(molStructure);	
		} catch (IndigoException e1) {
			logger.debug("failed first attempt: bad mol structure: " + molStructure);
			// clean up the molString and try again
			try {
				molStructure = new StringBuilder().append(lineEnd).append(molStructure).append(lineEnd).toString();
				mol = indigo.loadMolecule(molStructure);
			} catch (IndigoException e2) {
				logger.debug("failed second attempt: bad mol structure: " + molStructure);
				badStructureFlag = true;
				logger.error("bad mol structure: " + molStructure);
			}
		}	

		if (!badStructureFlag){
			return new CmpdRegMoleculeIndigoImpl(mol);
		} else {
			return null;
		}
	}

	@Override
	public String toMolfile(String molStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeIndigoImpl molecule = new CmpdRegMoleculeIndigoImpl(molStructure);
		IndigoObject rawMolecule = molecule.molecule;
		rawMolecule.dearomatize();
		return rawMolecule.molfile();
		
	}

	@Override
	public String getCipStereo(String structure) throws IOException, CmpdRegMolFormatException{	
		CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(structure);
		IndigoObject molecule = molWrapper.molecule;
		indigo.setOption("molfile-saving-add-stereo-desc", "1");
		return molecule.molfile();
	}

	@Override
	public String hydrogenizeMol(String structure, String inputFormat, String method) throws IOException, CmpdRegMolFormatException{	
		CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(structure);
		IndigoObject molecule = molWrapper.molecule;
		molecule.unfoldHydrogens();
		return molecule.molfile();
	}

	@Override
	public MolConvertOutputDTO cleanStructure(String structure, int dim, String opts) throws IOException, CmpdRegMolFormatException {				
		CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(structure);
		IndigoObject molecule = molWrapper.molecule;
		molecule.layout();
		MolConvertOutputDTO output = new MolConvertOutputDTO();
		output.setFormat("mol");
		output.setStructure(molecule.molfile());
		return output;
	}

	@Override
	public MolConvertOutputDTO toFormat(String structure, String inputFormat, String outputFormat) throws IOException, CmpdRegMolFormatException {			
		CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(structure);
		IndigoObject molecule = molWrapper.molecule;
		if (inputFormat.equalsIgnoreCase("smiles")) molecule.layout();
		MolConvertOutputDTO output = new MolConvertOutputDTO();
		output.setFormat("mol");
		output.setStructure(molecule.molfile());
		return output;
	}

	@Override
	public String toInchi(String molStructure) {
//		boolean badStructureFlag = false;
//		IndigoObject mol = null;
//		try {
//			mol = indigo.loadMolecule(molStructure);			
//		} catch (IndigoException e) {
//			badStructureFlag = true;
//		}
//
//		if (!badStructureFlag){
//			return mol.toFormat("inchi");
//		} else {
//			return molStructure;
//		}
		logger.error("inChi not implemented with Indigo Services");
		return molStructure;
	}

	@Override
	public String toSmiles(String molStructure) {
		boolean badStructureFlag = false;
		IndigoObject mol = null;
		try {
			mol = indigo.loadMolecule(molStructure);			
		} catch (IndigoException e) {
			badStructureFlag = true;
		}

		if (!badStructureFlag){
			return mol.smiles();
		} else {
			return molStructure;
		}
	}

	@Override
	public boolean isEmpty(String molStructure) throws CmpdRegMolFormatException {
		// Return empty if no atoms bonds or SGroups
		CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(molStructure);
		IndigoObject mol = molWrapper.molecule;
		Boolean hasAtoms =  mol.countAtoms() == 0.0;
		Boolean hasBonds =  mol.countBonds() == 0.0;
		Boolean hasSGroups = mol.countGenericSGroups() == 0.0 && mol.countDataSGroups() == 0.0;
		return !hasAtoms && !hasBonds && !hasSGroups;
	}

	@Override
	public double getMolWeight(String molStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeIndigoImpl mol = new CmpdRegMoleculeIndigoImpl(molStructure);
		return mol.getMass();
	}

	@Override
	public double getExactMass(String molStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeIndigoImpl mol = new CmpdRegMoleculeIndigoImpl(molStructure);
		return mol.getExactMass();
	}

	@Override
	public  String getMolFormula(String molStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeIndigoImpl mol = new CmpdRegMoleculeIndigoImpl(molStructure);
		return mol.getFormula();
	}

	@Override
	public boolean createJChemTable(String tableName, boolean tautomerDupe) {
		//no-op
		return false;
	}	

	@Override
	@Transactional
	public boolean dropJChemTable(String tableName) {
		//no-op
		return false;
	}	

	@Override
	@Transactional
	public boolean deleteAllJChemTableRows(String tableName) {
		//no-op
		return false;
	}	

	@Override
	public boolean deleteJChemTableRows(String tableName, int[] cdIds) {
		//no-op
		return false;
	}	

	@Override
	public boolean createJchemPropertyTable() {
		//no-op
		return false;
	}

	@Override
	public int[] checkDupeMol(String molStructure, String structureTable, String plainTable) throws CmpdRegMolFormatException {

		return searchMolStructures(molStructure, structureTable, plainTable, "DUPLICATE_TAUTOMER"); 
	}

	@Override
	public boolean updateStructure(String molStructure, String structureTable, int cdId) {
		//no-op
		return true;
	}	

	@Override
	public boolean updateStructure(CmpdRegMolecule mol, String structureTable, int cdId) {
		//no-op
		return true;
	}

	@Override
	public boolean deleteStructure(String structureTable, int cdId){
		//no-op
		return true;
	}

	@Override
	public boolean standardizedMolCompare(String queryMol, String targetMol) throws CmpdRegMolFormatException {
		IndigoObject queryMolecule = (new CmpdRegMoleculeIndigoImpl(queryMol)).molecule;
		queryMolecule.standardize();
		IndigoObject targetMolecule = (new CmpdRegMoleculeIndigoImpl(targetMol)).molecule;
		targetMolecule.standardize();
		return (indigo.exactMatch(queryMolecule, targetMolecule) != null);
	}
}

