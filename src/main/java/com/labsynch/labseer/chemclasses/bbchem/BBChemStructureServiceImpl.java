package com.labsynch.labseer.chemclasses.bbchem;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labsynch.labseer.domain.BBChemParentStructure;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import org.RDKit.RDKFuncs;
import org.RDKit.RDKFuncsConstants;
import org.RDKit.RDKFuncsJNI;
import org.RDKit.ROMol;
import org.RDKit.RWMol;
import org.RDKit.SanitizeFlags;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class BBChemStructureServiceImpl  implements BBChemStructureService {

	Logger logger = LoggerFactory.getLogger(BBChemStructureServiceImpl.class);

	static {
		System.loadLibrary("GraphMolWrap");
	}

	@Autowired
	private PropertiesUtilService propertiesUtilService;


	@Override
	public JsonNode getPreprocessorSettings() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(propertiesUtilService.getPreprocessorSettings());
		return jsonNode;
	}

	@Override
	public BBChemParentStructure getProcessedStructure(String molfile) throws CmpdRegMolFormatException {
		BBChemParentStructure bbChemStructure = new BBChemParentStructure();

		// Read the preprocessor settings as json
		JsonNode jsonNode = null;
		try{
			jsonNode = getPreprocessorSettings();
		} catch (IOException e) {
			logger.error("Error parsing preprocessor settings json");
			throw new CmpdRegMolFormatException("Error parsing preprocessor settings json");
		}

		// Extract the processURL
		JsonNode urlNode = jsonNode.get("processURL");
		if(urlNode == null || urlNode.isNull()) {
			logger.error("Missing preprocessorSettings processURL!!");
		}
		String url = urlNode.asText();

		// Get the standardization actions and options
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();
		JsonNode standardizerActions = jsonNode.get("standardizer_actions");
		ObjectNode options = (ObjectNode) jsonNode.get("process_options");
		options.put("standardizer_actions", standardizerActions);
		requestData.put("options", options);

		// Add the structures to the request
		ArrayNode arrayNode = mapper.createArrayNode();
		arrayNode.add(molfile);
		requestData.put("structures", arrayNode);

		// Post to the service and parse the response
		try {
			String requestString = requestData.toString();
			logger.info("requestString: " + requestString);
			HttpURLConnection connection = SimpleUtil.postRequest(url, requestString, logger);
			String postResponse = null;
			if(connection.getResponseCode() != 200) {
				logger.error("Error posting to preprocessor service: " + connection.getResponseMessage());
				throw new CmpdRegMolFormatException("Error posting to preprocessor service: " + connection.getResponseMessage());
			} else {
				postResponse = SimpleUtil.getStringBody(connection);
			}
			logger.info("Got response: "+ postResponse);

			// Parse the response json to get the standardized mol
			ObjectMapper responseMapper = new ObjectMapper();
			JsonNode responseNode = responseMapper.readTree(postResponse);
			for (JsonNode responseJsonNode : responseNode)  {
				JsonNode registrationHashesNode = responseJsonNode.get("registration_hash");
				String registrationHash = registrationHashesNode.get(0).asText();
				bbChemStructure.setReg(registrationHashesNode.get(0).asText());

				JsonNode noStereoHashNode = responseJsonNode.get("no_stereo_hash");
				bbChemStructure.setPreReg(noStereoHashNode.asText());

				JsonNode sdfNode = responseJsonNode.get("sdf");
				bbChemStructure.setMol(sdfNode.asText());

				JsonNode averageMolWeightNode = responseJsonNode.get("molecular_weight");
				bbChemStructure.setAverageMolWeight(averageMolWeightNode.asDouble());

				JsonNode exactMolWeightNode = responseJsonNode.get("exact_molecular_weight");
				bbChemStructure.setExactMolWeight(exactMolWeightNode.asDouble());

				JsonNode totalChargeNode = responseJsonNode.get("total_charge");
				bbChemStructure.setTotalCharge(Integer.valueOf(totalChargeNode.asInt()));

				JsonNode smilesNode = responseJsonNode.get("smiles");
				bbChemStructure.setSmiles(smilesNode.asText());

				JsonNode molecularFormulaNode = responseJsonNode.get("molecular_formula");
				bbChemStructure.setMolecularFormula(molecularFormulaNode.asText());

			}


		} catch (Exception e) {
			logger.error("Error posting to preprocessor service: " + e.getMessage());
			throw new CmpdRegMolFormatException("Error posting to preprocessor service: " + e.getMessage());
		}

		// Set recorded date todays date. This simplifies the code when we need to persist the structure
		// in various places.
		bbChemStructure.setRecordedDate(new Date());
		return bbChemStructure;
	}

	@Override
	public String getSDF(BBChemParentStructure bbChemStructure) throws IOException{
		// Read the preprocessor settings as json
		JsonNode jsonNode = getPreprocessorSettings();

		// Extract the processURL
		JsonNode urlNode = jsonNode.get("exportSDFURL");
		if(urlNode == null || urlNode.isNull()) {
			logger.error("Missing preprocessorSettings exportSDFURL!!");
		}
		String url = urlNode.asText();

		// Create the request data object
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		// Create sdfs arraynode and mol node
		ArrayNode sdfsNode = mapper.createArrayNode();
		ObjectNode molNode = mapper.createObjectNode();


		// Add the properties to the mol node
		ArrayNode propertiesNode = mapper.createArrayNode();
		for (Map.Entry<String,String> entry : bbChemStructure.getProperties().entrySet()) {
			ObjectNode propertyNode = mapper.createObjectNode();
			propertyNode.put("name", entry.getKey());
			propertyNode.put("value", entry.getValue());
			propertiesNode.add(propertyNode);

		}
		molNode.put("properties", propertiesNode);

		// Add the structure to the mol node
		molNode.put("sdf", bbChemStructure.getMol());

		// Add the mol node to the sdfs array node
		sdfsNode.add(molNode);

		// Add the sdfs to the request data
		requestData.put("sdfs", sdfsNode);

		// Post to the service and parse the response
		String requestString = requestData.toString();
		logger.info("requestString: " + requestString);
		String postResponse = SimpleUtil.postRequestToExternalServer(url, requestString, logger);
		logger.info("Got response: "+ postResponse);

		// Parse the response json
		ObjectMapper responseMapper = new ObjectMapper();
		JsonNode responseNode = responseMapper.readTree(postResponse);
		return responseNode.asText();
	}

	@Override
	public List<BBChemParentStructure> parseSDF(String molfile) throws CmpdRegMolFormatException {
		List<BBChemParentStructure> bbChemStructures = new ArrayList<BBChemParentStructure>();

		// Read the preprocessor settings as json
		JsonNode jsonNode = null;
		try{
			jsonNode = getPreprocessorSettings();
		} catch (IOException e) {
			logger.error("Error parsing preprocessor settings json");
			throw new CmpdRegMolFormatException("Error parsing preprocessor settings json");
		}

		// Extract the processURL
		JsonNode urlNode = jsonNode.get("parseURL");
		if(urlNode == null || urlNode.isNull()) {
			logger.error("Missing preprocessorSettings parseURL!!");
		}
		String url = urlNode.asText();

		// Create the request data object
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		// Add the structures to the request
		Base64.Encoder encoder = Base64.getEncoder();
		String encodedString = encoder.encodeToString(molfile.getBytes(StandardCharsets.UTF_8) );
		requestData.put("molv3", encodedString);

		// Post to the service and parse the response
		try {
			String requestString = requestData.toString();
			logger.info("requestString: " + requestString);
			String postResponse = SimpleUtil.postRequestToExternalServer(url, requestString, logger);
			logger.info("Got response: "+ postResponse);

			// Parse the response json to get the standardized mol
			ObjectMapper responseMapper = new ObjectMapper();
			JsonNode responseNode = responseMapper.readTree(postResponse);
			for (JsonNode responseJsonNode : responseNode)  {
				BBChemParentStructure bbChemStructure = new BBChemParentStructure();
				JsonNode molNode = responseJsonNode.get("mol");
				bbChemStructure.setMol(molNode.asText());

				bbChemStructure.setProperties(new HashMap<String, String>());
				JsonNode propertiesJsonNode = responseJsonNode.get("properties");
				for (JsonNode propertyNode : propertiesJsonNode) {
					String name = propertyNode.get("name").asText();
					String value = propertyNode.get("value").asText();
					bbChemStructure.getProperties().put(name, value);
				}
				bbChemStructures.add(bbChemStructure);
			}


		} catch (Exception e) {
			logger.error("Error posting to preprocessor service: " + e);
			throw new CmpdRegMolFormatException("Error posting to preprocessor service: " + e);
		}

		return bbChemStructures;

	}

	@Override
	public RWMol getPartiallySanitizedRWMol(String molstructure) {
		// https://www.rdkit.org/docs/cppapi/namespaceRDKit.html#a3a2051f80037d7633c3ea4cc72f58856
		// MolBlockToMol
		// molBlock: string containing the Mol block
		// sanitize: (optional) toggles sanitization of the molecule. Defaults to True.
		// removeHs: (optional) toggles removing hydrogens from the molecule. This only make sense when sanitization is done. Defaults to true.
		// strictParsing: (optional) if this is false, the parser is more lax about. correctness of the content. Defaults to true.
		RWMol mol = RDKFuncs.MolBlockToMol(molstructure, false, false, false);

		// Do partial sanization on mol so that we can calculate properties
		// https://www.rdkit.org/docs/cppapi/namespaceRDKit_1_1MolOps.html#a1ea8c8a254b2f9cd006029e9ad72deac
		// mol	: the RWMol to be cleaned
		// operationThatFailed	: the first (if any) sanitization operation that fails is set here. The values are taken from the SanitizeFlags enum. On success, the value is SanitizeFlags::SANITIZE_NONE
		// sanitizeOps	: the bits here are used to set which sanitization operations are carried out. The elements of the SanitizeFlags enum define the operations.
		int ops = (SanitizeFlags.SANITIZE_ALL.swigValue() ^ SanitizeFlags.SANITIZE_CLEANUP.swigValue() ^
		SanitizeFlags.SANITIZE_PROPERTIES.swigValue() ^ SanitizeFlags.SANITIZE_KEKULIZE.swigValue() ^
		SanitizeFlags.SANITIZE_FINDRADICALS.swigValue() ^ SanitizeFlags.SANITIZE_CLEANUPCHIRALITY.swigValue());
		RDKFuncs.sanitizeMol(mol, ops);

		return mol;
	}

	@Override
	public String getMolStructureFromRDKMol(ROMol rdkMol) {
		// https://www.rdkit.org/docs/cppapi/namespaceRDKit.html#a8159eb5c49b1d325ac994a15607cdffa
		// MolToMolBlock
		// mol	- the molecule in question
		// includeStereo	- toggles inclusion of stereochemistry information
		// confId	- selects the conformer to be used
		// kekulize	- triggers kekulization of the molecule before it is written
		// forceV3000	- force generation a V3000 mol block (happens automatically with more than 999 atoms or bonds)
		return RDKFuncs.MolToMolBlock(rdkMol, true, -1, false, false);
	}

	@Override
	public List<String> getMolFragments(String molfile) throws CmpdRegMolFormatException {

		// Read the preprocessor settings as json
		JsonNode jsonNode = null;
		try{
			jsonNode = getPreprocessorSettings();
		} catch (IOException e) {
			logger.error("Error parsing preprocessor settings json");
			throw new CmpdRegMolFormatException("Error parsing preprocessor settings json");
		}

		// Extract the processURL
		JsonNode urlNode = jsonNode.get("splitURL");
		if(urlNode == null || urlNode.isNull()) {
			logger.error("Missing preprocessorSettings splitURL!!");
		}
		String url = urlNode.asText();

		// Get the standardization actions and options
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		// Add the structures to the request
		ArrayNode arrayNode = mapper.createArrayNode();
		arrayNode.add(molfile);
		requestData.put("sdfs", arrayNode);

		List<String> molStrings = new ArrayList<>();
		// Post to the service and parse the response
		try {
			String requestString = requestData.toString();
			logger.info("requestString: " + requestString);
			HttpURLConnection connection = SimpleUtil.postRequest(url, requestString, logger);
			String postResponse = null;
			if(connection.getResponseCode() != 200) {
				logger.error("Error posting to split service: " + connection.getResponseMessage());
				throw new CmpdRegMolFormatException("Error posting to split service: " + connection.getResponseMessage());
			} else {
				postResponse = SimpleUtil.getStringBody(connection);
			}
			logger.info("Got response: "+ postResponse);

			// Parse the response json to get the standardized mol
			ObjectMapper responseMapper = new ObjectMapper();
			JsonNode responseNode = responseMapper.readTree(postResponse);
			JsonNode resultsNode = responseNode.get("results");
			for (JsonNode resultNode : resultsNode)  {
				JsonNode splitSDFS = resultNode.get("split_sdfs");
				molStrings.add(splitSDFS.get(0).asText());

			}
		} catch (Exception e) {
			logger.error("Error posting to split service: " + e.getMessage());
			throw new CmpdRegMolFormatException("Error posting to split service: " + e.getMessage());
		}

		return molStrings;
	}

}
