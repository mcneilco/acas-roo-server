package com.labsynch.labseer.chemclasses.bbchem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.domain.AbstractBBChemStructure;
import com.labsynch.labseer.domain.BBChemDryRunStructure;
import com.labsynch.labseer.domain.BBChemSaltFormStructure;
import com.labsynch.labseer.domain.BBChemSaltStructure;
import com.labsynch.labseer.domain.BBChemParentStructure;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.dto.MolConvertOutputDTO;
import com.labsynch.labseer.dto.StrippedSaltDTO;
import com.labsynch.labseer.dto.configuration.StandardizerSettingsConfigDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import org.springframework.jdbc.core.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.RDKit.RDKFuncs;
import org.RDKit.ROMol;
import org.RDKit.ROMol_Vect;
import org.RDKit.RWMol;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import java.util.UUID;

import javax.persistence.TypedQuery;

import static java.lang.Math.toIntExact;
@Component
public class ChemStructureServiceBBChemImpl implements ChemStructureService {

	Logger logger = LoggerFactory.getLogger(ChemStructureServiceBBChemImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private BBChemStructureService bbChemStructureService;

	@Autowired
	private JdbcTemplate basicJdbcTemplate;

	static {
		System.loadLibrary("GraphMolWrap");
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.basicJdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return basicJdbcTemplate;
	}

	@Override
	public CmpdRegMolecule[] searchMols(String molfile, StructureType structureType, int[] cdHitList, SearchType searchType, Float simlarityPercent) throws CmpdRegMolFormatException {
		int maxResults = propertiesUtilService.getMaxSearchResults();
		return searchMols(molfile, structureType, cdHitList, searchType, simlarityPercent, maxResults);
	}

	private List<? extends AbstractBBChemStructure> searchBBChemStructures(String molfile, StructureType structureType, int[] inputCdIdHitList, SearchType searchType, Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException {

		BBChemParentStructure serviceBBChemStructure = bbChemStructureService.getProcessedStructure(molfile);

		// Create empty list
		List<? extends AbstractBBChemStructure> bbChemStructures = new ArrayList<AbstractBBChemStructure>();
		TypedQuery<? extends AbstractBBChemStructure> query;
		if (searchType == SearchType.FULL_TAUTOMER){
			// FULL TAUTOMER hashes are stored on the pre reg hash column
			if(structureType == StructureType.PARENT) {
				query = BBChemParentStructure.findBBChemParentStructuresByPreRegEquals(serviceBBChemStructure.getPreReg());
			} else if (structureType == StructureType.SALT) {
				query = BBChemSaltStructure.findBBChemSaltStructuresByPreRegEquals(serviceBBChemStructure.getPreReg());
			} else if (structureType == StructureType.SALT_FORM) {
				query = BBChemSaltFormStructure.findBBChemSaltFormStructuresByPreRegEquals(serviceBBChemStructure.getPreReg());
			} else if (structureType == StructureType.DRY_RUN) {
				query = BBChemDryRunStructure.findBBChemDryRunStructuresByPreRegEquals(serviceBBChemStructure.getPreReg());
			} else {
				throw new CmpdRegMolFormatException("Structure type not implemented for BBChem searches" + structureType);
			}
		}else if (searchType == SearchType.DUPLICATE_TAUTOMER | searchType == SearchType.EXACT){
			// DUPLICATE TAUTOMER hashes are stored on the reg column
			if(structureType == StructureType.PARENT) {
				query = BBChemParentStructure.findBBChemParentStructuresByRegEquals(serviceBBChemStructure.getReg());
			} else if (structureType == StructureType.SALT) {
				query = BBChemSaltStructure.findBBChemSaltStructuresByRegEquals(serviceBBChemStructure.getReg());
			} else if (structureType == StructureType.SALT_FORM) {
				query = BBChemSaltFormStructure.findBBChemSaltFormStructuresByRegEquals(serviceBBChemStructure.getReg());
			} else if (structureType == StructureType.DRY_RUN) {
				query = BBChemDryRunStructure.findBBChemDryRunStructuresByRegEquals(serviceBBChemStructure.getReg());
			} else {
				throw new CmpdRegMolFormatException("Structure type not implemented for BBChem searches" + structureType);
			}
		} else {
			throw new CmpdRegMolFormatException("Search type not implemented for BBChem searches" + searchType);
		}
	
		// int is not nullable so you either have a valid maxResults passed in
		// or -1 to express unlimited
		if(maxResults > -1) {
			query.setMaxResults(maxResults);
		}

		bbChemStructures = query.getResultList();
		
		return(bbChemStructures);
	}

	@Override
	public CmpdRegMolecule[] searchMols(String molfile, StructureType structureType, int[] inputCdIdHitList, SearchType searchType, Float simlarityPercent, int maxResults)
			throws CmpdRegMolFormatException {
		
			List<? extends AbstractBBChemStructure>  bbChemStructures = searchBBChemStructures(molfile, structureType, inputCdIdHitList, searchType, simlarityPercent, maxResults);

			List<CmpdRegMolecule> resultList = new ArrayList<CmpdRegMolecule>();
			for (AbstractBBChemStructure hit : bbChemStructures) {
				resultList.add(new CmpdRegMoleculeBBChemImpl(hit.getMol(), bbChemStructureService));
			}
			CmpdRegMolecule[] resultArray = new CmpdRegMolecule[resultList.size()];
			
			return resultList.toArray(resultArray);
	}


	@Override
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType) throws CmpdRegMolFormatException {
		return searchMolStructures(molfile, structureType, searchType, -1F, -1);

	}

	@Override
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType, Float simlarityPercent) throws CmpdRegMolFormatException {
		return searchMolStructures(molfile, structureType, searchType, simlarityPercent, -1);	
	}

	@Override
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType, Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException {

		List<? extends AbstractBBChemStructure> bbChemStructures = searchBBChemStructures(molfile, structureType,  new int[0] , searchType, simlarityPercent, maxResults);

		// Create empty int hit list
		int[] hits = new int[bbChemStructures.size()];
		for (int i = 0; i < hits.length; i++) {
			hits[i] = toIntExact(bbChemStructures.get(i).getId());
		}
		return hits;
	}

	@Override
	public int saveStructure(String molfile, StructureType structureType) {
		boolean checkForDupes = false;
		return saveStructure(molfile, structureType, checkForDupes);
	}

	@Override
	public void closeConnection() {
		// Not used for this implementation
	}

	public String getBBChemStructureTableNameFromStructureType(StructureType structureType) {
		String plainTable = null;
		if (structureType == StructureType.PARENT){
			plainTable = "bbchem_parent_structure";
		} else if (structureType == StructureType.SALT_FORM){
			plainTable = "bbchem_salt_form_structure";
		} else if (structureType == StructureType.SALT){
			plainTable = "bbchem_salt_structure";
		} else if (structureType == StructureType.DRY_RUN){
			plainTable = "bbchem_dry_run_structure";
		}
		return(plainTable);
	}

	@Override
	@Transactional
	public boolean truncateStructureTable(StructureType structureType) {
		String truncateStatement = "TRUNCATE TABLE " + getBBChemStructureTableNameFromStructureType(structureType);
		BBChemParentStructure.entityManager().createNativeQuery(truncateStatement).executeUpdate();
		return true;
	}

	@Override
	public int saveStructure(String molfile, StructureType structureType, boolean checkForDupes) {

		Long cdId=0L;
		try {
			BBChemParentStructure bbChemStructure = bbChemStructureService.getProcessedStructure(molfile);

			if (structureType == StructureType.PARENT){
				// Can't type cast from subclass to superclass so we go to json and back
				BBChemParentStructure bbStructure = BBChemParentStructure.fromJsonToBBChemParentStructure(bbChemStructure.toJson());

				if(checkForDupes){
					List<BBChemParentStructure> bbChemStructures =  BBChemParentStructure.findBBChemParentStructuresByRegEquals(bbStructure.getReg()).getResultList();
					if(bbChemStructures.size() > 0){
						logger.error("BBChem structure for " + structureType + " type already exists with id "+ bbChemStructures.get(0).getId());
						return 0;
					}
				}
				bbStructure.persist();
				cdId = bbStructure.getId();
			} else if (structureType == StructureType.SALT_FORM){
				// Can't type cast from subclass to superclass so we go to json and back
				BBChemSaltFormStructure bbChemSaltFormStructure = BBChemSaltFormStructure.fromJsonToBBChemSaltFormStructure(bbChemStructure.toJson());

				if(checkForDupes){
					List<BBChemSaltFormStructure> bbChemStructures =  BBChemSaltFormStructure.findBBChemSaltFormStructuresByRegEquals(bbChemSaltFormStructure.getReg()).getResultList();
					if(bbChemStructures.size() > 0){
						logger.error("Salt form structure already exists with id "+ bbChemStructures.get(0).getId());
						return 0;
					}
				}
				bbChemSaltFormStructure.persist();
				cdId = bbChemSaltFormStructure.getId();
			} else if (structureType == StructureType.SALT){
				// Can't type cast from subclass to superclass so we go to json and back
				BBChemSaltStructure bbChemStructureSalt = BBChemSaltStructure.fromJsonToBBChemSaltStructure(bbChemStructure.toJson());

				if(checkForDupes){
					List<BBChemSaltStructure> bbChemStructures =  BBChemSaltStructure.findBBChemSaltStructuresByRegEquals(bbChemStructureSalt.getReg()).getResultList();
					if(bbChemStructures.size() > 0){
						logger.error("Salt structure already exists with id "+ bbChemStructures.get(0).getId());
						return 0;
					}
				}
				bbChemStructureSalt.persist();
				cdId = bbChemStructureSalt.getId();
			} else if (structureType == StructureType.SALT_FORM){
				// Can't type cast from subclass to superclass so we go to json and back
				BBChemSaltFormStructure bbChemSaltFormStructure = BBChemSaltFormStructure.fromJsonToBBChemSaltFormStructure(bbChemStructure.toJson());

				if(checkForDupes){
					List<BBChemSaltFormStructure> bbChemStructures =  BBChemSaltFormStructure.findBBChemSaltFormStructuresByRegEquals(bbChemSaltFormStructure.getReg()).getResultList();
					if(bbChemStructures.size() > 0){
						logger.error("Salt structure already exists with id "+ bbChemStructures.get(0).getId());
						return 0;
					}
				}
				bbChemSaltFormStructure.persist();
				cdId = bbChemSaltFormStructure.getId();
			} else if (structureType == StructureType.DRY_RUN){
				// Can't type cast from subclass to superclass so we go to json and back
				BBChemDryRunStructure bbChemStructureDryRun = BBChemDryRunStructure.fromJsonToBBChemDryRunStructure(bbChemStructure.toJson());

				if(checkForDupes){
					List<BBChemDryRunStructure> bbChemStructures =  BBChemDryRunStructure.findBBChemDryRunStructuresByRegEquals(bbChemStructureDryRun.getReg()).getResultList();
					if(bbChemStructures.size() > 0){
						logger.error("DryRun structure already exists with id "+ bbChemStructures.get(0).getId());
						return 0;
					}
				}
				bbChemStructureDryRun.persist();
				cdId = bbChemStructureDryRun.getId();
			}

			return toIntExact(cdId);
		} catch (CmpdRegMolFormatException e) {
			logger.error("Error saving structure: " + e.getMessage());
			return -1;
		}
	}

	@Override
	public double getMolWeight(String molStructure) throws CmpdRegMolFormatException {
		// Calculates the average molecular weight of a molecule
		return bbChemStructureService.getProcessedStructure(molStructure).getAverageMolWeight();
	}

	@Override
	public CmpdRegMolecule toMolecule(String molStructure) throws CmpdRegMolFormatException {
		return new CmpdRegMoleculeBBChemImpl(molStructure, bbChemStructureService);
	}

	@Override
	public String toMolfile(String molStructure) throws CmpdRegMolFormatException {
		return bbChemStructureService.getProcessedStructure(molStructure).getMol();
	}

	@Override
	public String toSmiles(String molStructure) throws CmpdRegMolFormatException {
		return bbChemStructureService.getProcessedStructure(molStructure).getSmiles();
	}

	@Override
	public int[] checkDupeMol(String molStructure, StructureType structureType)
			throws CmpdRegMolFormatException {
		return searchMolStructures(molStructure, structureType, SearchType.DUPLICATE_TAUTOMER); 
	}

	@Override
	public String toInchi(String molStructure) {
		try {
			return bbChemStructureService.getProcessedStructure(molStructure).getInchi();
		} catch (CmpdRegMolFormatException e) {
			logger.error("Error calculating inchi: ", e);
			return null;
		}
	}

	@Override
	public boolean updateStructure(String molStructure, StructureType structureType, int cdId) {
		Long id= new Long(cdId);
		try {
			BBChemParentStructure bbChemStructureUpdated = bbChemStructureService.getProcessedStructure(molStructure);
			if (structureType == StructureType.PARENT){
				BBChemParentStructure bbChemStructureSaved = BBChemParentStructure.findBBChemParentStructure(id);
				bbChemStructureSaved.updateStructureInfo(bbChemStructureUpdated.getMol(), bbChemStructureUpdated.getReg(), bbChemStructureUpdated.getPreReg());
				bbChemStructureSaved.persist();
			} else if (structureType == StructureType.SALT_FORM){
				BBChemSaltFormStructure bbChemSaltFormStructureSaved = BBChemSaltFormStructure.findBBChemSaltFormStructure(id);
				bbChemSaltFormStructureSaved.updateStructureInfo(bbChemStructureUpdated.getMol(), bbChemStructureUpdated.getReg(), bbChemStructureUpdated.getPreReg());
				bbChemSaltFormStructureSaved.persist();
			} else if (structureType == StructureType.SALT){
				BBChemSaltStructure bbChemSaltStructureSaved = BBChemSaltStructure.findBBChemSaltStructure(id);
				bbChemSaltStructureSaved.updateStructureInfo(bbChemStructureUpdated.getMol(), bbChemStructureUpdated.getReg(), bbChemStructureUpdated.getPreReg());
				bbChemSaltStructureSaved.persist();
			} else if (structureType == StructureType.DRY_RUN){
				BBChemDryRunStructure bbChemDryRunStructureSaved = BBChemDryRunStructure.findBBChemDryRunStructure(id);
				bbChemDryRunStructureSaved.updateStructureInfo(bbChemStructureUpdated.getMol(), bbChemStructureUpdated.getReg(), bbChemStructureUpdated.getPreReg());
				bbChemDryRunStructureSaved.persist();
			}
		} catch (CmpdRegMolFormatException e) {
			logger.error("Error updating structure: ", e);
			return false;
		}
		return true;
	}

	@Override
	public String getMolFormula(String molStructure) throws CmpdRegMolFormatException {
		return bbChemStructureService.getProcessedStructure(molStructure).getMolecularFormula();
	}

	@Override
	public boolean checkForSalt(String molfile) throws CmpdRegMolFormatException {
		boolean foundNonCovalentSalt = false;
		RWMol mol = bbChemStructureService.getPartiallySanitizedRWMol(molfile);
		ROMol_Vect frags = RDKFuncs.getMolFrags(mol);
		if(frags.size() > 1.0) {
			foundNonCovalentSalt = true;
		}
		return foundNonCovalentSalt;
	}

	@Override
	public boolean updateStructure(CmpdRegMolecule mol, StructureType structureType, int cdId) {
		try {
			updateStructure(mol.getMolStructure(), structureType, cdId);
		} catch (CmpdRegMolFormatException e) {
			logger.error("Error updating structure: " + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public double getExactMass(String molStructure) throws CmpdRegMolFormatException {
		return bbChemStructureService.getProcessedStructure(molStructure).getExactMolWeight();
	}

	@Override
	public boolean deleteStructure(StructureType structureType, int cdId) {
		Long id = new Long(cdId);
		if (structureType == StructureType.PARENT){
			BBChemParentStructure bbChemStructure = BBChemParentStructure.findBBChemParentStructure(id);
			bbChemStructure.remove();
		} else if (structureType == StructureType.SALT_FORM){
			BBChemSaltFormStructure bbChemSaltFormStructure = BBChemSaltFormStructure.findBBChemSaltFormStructure(id);
			bbChemSaltFormStructure.remove();
		} else if (structureType == StructureType.SALT){
			BBChemSaltStructure bbChemSaltStructure = BBChemSaltStructure.findBBChemSaltStructure(id);
			bbChemSaltStructure.remove();
		} else if (structureType == StructureType.DRY_RUN){
			BBChemDryRunStructure bbChemDryRunStructure = BBChemDryRunStructure.findBBChemDryRunStructure(id);
			bbChemDryRunStructure.remove();
		}
		return true;
	}

	private String getServiceFormat(String format) {
		String serviceFormat = null;
		if (format == null || format.equalsIgnoreCase("mol")) {
			serviceFormat = "sdf";
		} else if (format.equalsIgnoreCase("sdf")) {
			serviceFormat = "sdf";
		} else if (format.equalsIgnoreCase("smi")) {
			serviceFormat = "smiles";
		}
		return serviceFormat;
	}

	@Override
	public MolConvertOutputDTO toFormat(String structure, String inputFormat, String outputFormat) throws IOException, CmpdRegMolFormatException {
		// Calls preprocessor URL and gets the standardized structure from the preprocessor URL
		MolConvertOutputDTO output = new MolConvertOutputDTO();

		// Read the preprocessor settings as json
		JsonNode jsonNode = bbChemStructureService.getPreprocessorSettings();

		// Extract the url to call
		JsonNode urlNode = jsonNode.get("converterURL");
		if(urlNode == null || urlNode.isNull()) {
			logger.error("Missing preprocessorSettings converterURL!!");
		}

		String url = urlNode.asText();

		// Create the request format
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		requestData.put("input_format", getServiceFormat(inputFormat));
		requestData.put("output_format", getServiceFormat(outputFormat));

		ArrayNode inputsNode = mapper.createArrayNode();
		inputsNode.add(structure);
		requestData.put("inputs", inputsNode);		

		// Post to the service
		String postResponse = SimpleUtil.postRequestToExternalServer(url, requestData.toString(), logger);
		logger.info("Got response: "+ postResponse);

		// Parse the response json
		ObjectMapper responseMapper = new ObjectMapper();
		JsonNode responseNode = responseMapper.readTree(postResponse);
		output.setStructure(responseNode.get(0).asText());
		output.setFormat(outputFormat);
		String contentUrl = "TO DO: Download Link";
		output.setContentUrl(contentUrl);
		return output;
	}

	@Override
	public MolConvertOutputDTO cleanStructure(String structure, int dim, String opts)
			throws IOException, CmpdRegMolFormatException {
		// Unused for this implementation
		return null;
	}

	@Override
	public String hydrogenizeMol(String structure, String inputFormat, String method)
			throws IOException, CmpdRegMolFormatException {
			RWMol mol = bbChemStructureService.getPartiallySanitizedRWMol(structure);
			if (method.equalsIgnoreCase("HYDROGENIZE")){
				RDKFuncs.addHs(mol);		
			} else {
				RDKFuncs.removeHs(mol);
			}
			return mol.MolToMolBlock();
	}

	@Override
	public String getCipStereo(String structure) throws IOException, CmpdRegMolFormatException {
		//Jchem implementation returns something like
		//e.g. [TH: 1{0,3,2} S]
		//Indigo implementaiton returns the mol file after running some Indigo function
		//No implementing this function because its not used anywhere that I can find
		//Perhaps it was used as a utility function at somepoint
		return "NOT IMPLEMENTED";
	}

	@Override
	public StrippedSaltDTO stripSalts(CmpdRegMolecule inputStructure) throws CmpdRegMolFormatException {
		RWMol clone = bbChemStructureService.getPartiallySanitizedRWMol(inputStructure.getMolStructure());
		List<CmpdRegMoleculeBBChemImpl> allFrags = new ArrayList<CmpdRegMoleculeBBChemImpl>();
	    ROMol_Vect frags = RDKFuncs.getMolFrags(clone);
		for (int i = 0; i < frags.size(); i++) {
			RWMol frag = (RWMol) frags.get(i);
			CmpdRegMoleculeBBChemImpl fragWrapper = new CmpdRegMoleculeBBChemImpl(bbChemStructureService.getMolStructureFromRDKMol(frag), bbChemStructureService);
			allFrags.add(fragWrapper);
		}
	
		Map<Salt, Integer> saltCounts = new HashMap<Salt, Integer>();
		Set<CmpdRegMoleculeBBChemImpl> unidentifiedFragments = new HashSet<CmpdRegMoleculeBBChemImpl>();
		for (CmpdRegMoleculeBBChemImpl fragment : allFrags){
			int[] cdIdMatches = searchMolStructures(fragment.getMolStructure(), StructureType.SALT, SearchType.DUPLICATE_TAUTOMER);
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
		// Calls preprocessor URL and gets the standardized structure from the preprocessor URL
		String molOut = null;

		// Read the preprocessor settings as json
		JsonNode jsonNode = bbChemStructureService.getPreprocessorSettings();

		// Extract the url to call
		JsonNode urlNode = jsonNode.get("preprocessURL");
		if(urlNode == null || urlNode.isNull()) {
			logger.error("Missing preprocessorSettings preprocessURL!!");
		}

		String url = urlNode.asText();

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();
		JsonNode standardizerActions = jsonNode.get("standardizer_actions");
		requestData.put("config", standardizerActions);
		requestData.put("output_format", "MOL");

		// Create a "structures": { "structure-id": "molfile" } object and add it to the request
		ObjectNode structuresNode = mapper.createObjectNode();
		String id = UUID.randomUUID().toString();
		structuresNode.put(id, molfile);
		requestData.put("structures", structuresNode);

		// Post to the service
		String postResponse = SimpleUtil.postRequestToExternalServer(url, requestData.toString(), logger);
		logger.info("Got response: "+ postResponse);

		// Parse the response json to get the standardized mol
		ObjectMapper responseMapper = new ObjectMapper();
		JsonNode responseNode = responseMapper.readTree(postResponse);
		JsonNode structuresResponseNode = responseNode.get("structures");
		JsonNode structureResponseNode = structuresResponseNode.get(id);
		molOut = structureResponseNode.get("structure").asText();
	
		return molOut;
	}

	@Override
	public boolean compareStructures(String preMolStruct, String postMolStruct, SearchType searchType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean standardizedMolCompare(String queryMol, String targetMol) throws CmpdRegMolFormatException {
		try {
			BBChemParentStructure queryStructure = bbChemStructureService.getProcessedStructure(queryMol);
			BBChemParentStructure targetStructure = bbChemStructureService.getProcessedStructure(targetMol);
			return queryStructure.getReg() == targetStructure.getReg();
		} catch (Exception e) {
			logger.error("Error in standardizedMolCompare: ", e);
			throw new CmpdRegMolFormatException(e);
		}
	}

	@Override
	public boolean isEmpty(String molFile) throws CmpdRegMolFormatException {
		RWMol mol = bbChemStructureService.getPartiallySanitizedRWMol(molFile);
		Boolean hasAtoms =  mol.getNumAtoms() == 0.0;
		Boolean hasBonds =  mol.getNumBonds() == 0.0;
		Boolean hasSGroups = RDKFuncs.getSubstanceGroupCount(mol) == 0.0;
		return !hasAtoms && !hasBonds && !hasSGroups;
	}

	@Override
	public StandardizerSettingsConfigDTO getStandardizerSettings() throws StandardizerException{
		// Read the preprocessor settings as json
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		try{
			jsonNode = bbChemStructureService.getPreprocessorSettings().get("standardizer_actions");
		} catch (IOException e) {
			logger.error("Error parsing preprocessor settings json", e);
			throw new StandardizerException("Error parsing preprocessor settings json");
		}

		StandardizerSettingsConfigDTO standardizationConfigDTO = new StandardizerSettingsConfigDTO();
		standardizationConfigDTO.setSettings(jsonNode.toString());
		standardizationConfigDTO.setType("bbchem");
		standardizationConfigDTO.setShouldStandardize(true);
		return standardizationConfigDTO;

	}

}

