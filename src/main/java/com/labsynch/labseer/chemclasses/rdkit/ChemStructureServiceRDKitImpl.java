package com.labsynch.labseer.chemclasses.rdkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import org.springframework.jdbc.core.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.RDKit.*;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import java.util.UUID;

@Component
public class ChemStructureServiceRDKitImpl implements ChemStructureService {

	Logger logger = LoggerFactory.getLogger(ChemStructureServiceRDKitImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
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

			// If preprocessor settings are not null then use the preprocessor settings
			if(propertiesUtilService.getPreprocessorSettings() != null) {
	
				// Read the preprocessor settings as json
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode jsonNode = null;
				try{
					jsonNode = objectMapper.readTree(propertiesUtilService.getPreprocessorSettings());

				} catch (IOException e) {
					logger.error("Error parsing preprocessor settings json: " + propertiesUtilService.getPreprocessorSettings());
					throw new CmpdRegMolFormatException("Error parsing preprocessor settings json: " + propertiesUtilService.getPreprocessorSettings());
				}
	
				// Extract the url to call
				JsonNode urlNode = jsonNode.get("processURL");
				if(urlNode == null || urlNode.isNull()) {
					logger.error("Missing preprocessorSettings processURL!!");
				}
	
				String url = urlNode.asText();
	
				// Get the preprocessory settings
				ObjectMapper mapper = new ObjectMapper();
				ObjectNode requestData = mapper.createObjectNode();
				JsonNode standardizerActions = jsonNode.get("standardizer_actions");
				ObjectNode options = (ObjectNode) jsonNode.get("process_options");
				options.put("standardizer_actions", standardizerActions);
				requestData.put("options", options);

				ArrayNode arrayNode = mapper.createArrayNode();
				arrayNode.add(molfile);
				requestData.put("structures", arrayNode);
		
				// Post to the service
				try {
					String requestString = requestData.toString();
					logger.info("requestString: " + requestString);
					String postResponse = SimpleUtil.postRequestToExternalServer(url, requestString, logger);
					logger.info("Got response: "+ postResponse);
		
					// Parse the response json to get the standardized mol
					ObjectMapper responseMapper = new ObjectMapper();
					JsonNode responseNode = responseMapper.readTree(postResponse);
					for (JsonNode responseJsonNode : responseNode) {
						JsonNode registrationHashesNode = responseJsonNode.get("registration_hash");
						for (JsonNode registrationHashNode : registrationHashesNode) {
							logger.info(registrationHashNode.asText());
						}
						JsonNode noStereoHashNode = responseJsonNode.get("no_stereo_hash");
						String noStereoHash = noStereoHashNode.asText();
						logger.info(noStereoHash);
					}
	
				} catch (Exception e) {
					logger.error("Error posting to preprocessor service: " + e.getMessage());
					throw new CmpdRegMolFormatException("Error posting to preprocessor service: " + e.getMessage());
				}
		
			}
			return new int[0];
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
		return new CmpdRegMoleculeRDKitImpl(RWMol.MolFromMolBlock(molStructure));
	}

	@Override
	public String toMolfile(String molStructure) throws CmpdRegMolFormatException {
		RWMol mol = RWMol.MolFromMolBlock(molStructure);
		mol.Kekulize();
		return mol.MolToMolBlock()
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
		return new int[0];
	}

	@Override
	public String toInchi(String molStructure) {
		return RDKFuncs.MolBlockToInchi(molStructure, new ExtraInchiReturnValues());
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
		boolean foundNonCovalentSalt = false;
		RWMol mol = RWMol.MolFromMolBlock(molfile);
		ROMol_Vect frags = RDKFuncs.getMolFrags(mol);
		if(frags.size() > 1.0) {
			foundNonCovalentSalt = true;
		}
		return foundNonCovalentSalt;
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
		boolean badStructureFlag = false;
		RWMol mol = null;
		try {
			if (inputFormat == null || inputFormat.equalsIgnoreCase("mol")) {
				mol = RWMol.MolFromMolBlock(structure);
			} else if (inputFormat.equalsIgnoreCase("sdf")) {
				mol = RWMol.MolFromMolBlock(structure);
			} else if (inputFormat.equalsIgnoreCase("smi")) {
				mol = RWMol.MolFromSmiles(structure);
			}
		} catch (Exception e) {
			badStructureFlag = true;
		}

		MolConvertOutputDTO output = new MolConvertOutputDTO();		
		if (!badStructureFlag){
			if (outputFormat == null || outputFormat.equalsIgnoreCase("mol")) {
				output.setStructure(mol.MolToMolBlock());
			} else if (inputFormat.equalsIgnoreCase("sdf")) {
				output.setStructure(mol.MolToMolBlock());
			} else if (inputFormat.equalsIgnoreCase("smi")) {
				output.setStructure(mol.MolToSmiles());
			}
			output.setFormat(outputFormat);
			String contentUrl = "TO DO: Download Link";
			output.setContentUrl(contentUrl);
		} 
		return output;
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
		RWMol mol = RWMol.MolFromMolBlock(structure);
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
		String molOut = null;

		// Read the preprocessor settings as json
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(propertiesUtilService.getPreprocessorSettings());

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
		ObjectNode structuresNode = objectMapper.createObjectNode();
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
		RWMol mol = RWMol.MolFromMolBlock(molFile);
		Boolean hasAtoms =  mol.getNumAtoms() == 0.0;
		Boolean hasBonds =  mol.getNumBonds() == 0.0;
		Boolean hasSGroups = RDKFuncs.getSubstanceGroupCount(mol) == 0.0;
		return !hasAtoms && !hasBonds && !hasSGroups;
	}

}

