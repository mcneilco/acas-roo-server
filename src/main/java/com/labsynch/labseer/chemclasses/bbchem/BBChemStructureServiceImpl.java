package com.labsynch.labseer.chemclasses.bbchem;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labsynch.labseer.domain.AbstractBBChemStructure;
import com.labsynch.labseer.domain.BBChemParentStructure;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

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

	@Autowired
	private PropertiesUtilService propertiesUtilService;


	@Override
	public JsonNode getPreprocessorSettings() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(propertiesUtilService.getPreprocessorSettings());
		return jsonNode;
	}

	private String getUrlFromPreprocessorSettings(String propertyKey) throws IOException {
		JsonNode jsonNode = null;
		jsonNode = getPreprocessorSettings();
		JsonNode urlNode = jsonNode.get(propertyKey);
		if(urlNode == null || urlNode.isNull()) {
			logger.error("Missing preprocessorSettings "+propertyKey+"!!");
			throw new IOException("Missing preprocessorSettings "+propertyKey+"!!");
		}
		String url = urlNode.getTextValue();
		return url;

	}

	private BitSet getFingerprint(String molStructure, String type)  throws CmpdRegMolFormatException{
		// Fetch the fingerprint from the BBChem fingerprint service
		String url = null;
		try {
			url = getUrlFromPreprocessorSettings("fingerprintURL");
		} catch (IOException e) {
			throw new CmpdRegMolFormatException(e);
		}
		
		// Create the request data object
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		// Add the structures to the request
		ArrayNode arrayNode = mapper.createArrayNode();
		arrayNode.add(molStructure);
		requestData.put("sdfs", arrayNode);

		requestData.put("fingerprint_type", type);

		// Post to the service and parse the response
		try {
			String requestString = requestData.toString();
			logger.info("requestString: " + requestString);
			HttpURLConnection connection = SimpleUtil.postRequest(url, requestString, logger);
			String postResponse = null;
			if(connection.getResponseCode() != 200) {
				logger.error("Error posting to fingerprint service: " + connection.getResponseMessage());
				throw new CmpdRegMolFormatException("Error posting to fingerprint service: " + connection.getResponseMessage());
			} else {
				postResponse = SimpleUtil.getStringBody(connection);
			}
			logger.info("Got response: "+ postResponse);

			// Parse the response json to get the standardized mol
			ObjectMapper responseMapper = new ObjectMapper();
			JsonNode responseNode = responseMapper.readTree(postResponse);
			JsonNode resultsNode = responseNode.get("fingerprint_results");
			return SimpleUtil.stringToBitSet(resultsNode.get(0).asText());
		} catch (Exception e) {
			logger.error("Error posting to fingerprint service: " + e.getMessage());
			throw new CmpdRegMolFormatException("Error posting to fingerprint service: " + e.getMessage());
		}
	}

	@Override
	public JsonNode postToProcessService(String molfile) throws IOException {
		
		String url = getUrlFromPreprocessorSettings("processURL");

		JsonNode jsonNode = getPreprocessorSettings();

		// Create the request data object
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		// Get the standardization actions and options
		ObjectNode options = (ObjectNode) jsonNode.get("process_options");

		// We do not want standardization here but the route requires a standardization object to be sent in.
		// Adding an empty standardizer actions so to avoid any standardization
		options.put("standardizer_actions", mapper.createObjectNode());
		requestData.put("options", options);

		// Add the structures to the request
		ArrayNode arrayNode = mapper.createArrayNode();
		arrayNode.add(molfile);
		requestData.put("structures", arrayNode);

		// Post to the service and parse the response
		String requestString = requestData.toString();
		logger.info("requestString: " + requestString);
		HttpURLConnection connection = SimpleUtil.postRequest(url, requestString, logger);

		String postResponse = null;
		if(connection.getResponseCode() != 200) {
			logger.error("Error posting to process service: " + connection.getResponseMessage());
			throw new IOException("Error posting to process service: " + connection.getResponseMessage());
		} else {
			postResponse = SimpleUtil.getStringBody(connection);
		}
		logger.info("Got response: "+ postResponse);

		// Parse the response json to get the standardized mol
		ObjectMapper responseMapper = new ObjectMapper();
		
		JsonNode responseNode = responseMapper.readTree(postResponse);

		return responseNode;
	}

	@Override
	public BBChemParentStructure getProcessedStructure(String molfile, Boolean includeFingerprints) throws CmpdRegMolFormatException {
		BBChemParentStructure bbChemStructure = new BBChemParentStructure();
		// Post to the service and parse the response
		try {
			JsonNode responseNode;
			try {
				responseNode = postToProcessService(molfile);
			} catch (IOException e) {
				throw new CmpdRegMolFormatException(e);
			}
			for (JsonNode responseJsonNode : responseNode)  {

				// Throw exception if there is an error reading the molecule
				JsonNode errorCodeNode = responseJsonNode.get("error_code");

				// Check if we have an error code
				if( errorCodeNode != null) {
					throw new CmpdRegMolFormatException("Error processing mol: Code " + errorCodeNode.getTextValue() + " " + responseJsonNode.get("error_msg").getTextValue());
				}

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

				if(includeFingerprints) {
					JsonNode fingerprintNode = responseJsonNode.get("fingerprint");
					bbChemStructure.setSubstructure(getFingerprint(bbChemStructure.getMol(), "substructure_search"));
					bbChemStructure.setSimilarity(getFingerprint(bbChemStructure.getMol(), "similarity_score"));
				}
			}


		} catch (CmpdRegMolFormatException e) {
			logger.error("Error posting to preprocessor service: " + e.getMessage());
			throw new CmpdRegMolFormatException(e);
		}

		// Set recorded date todays date. This simplifies the code when we need to persist the structure
		// in various places.
		bbChemStructure.setRecordedDate(new Date());
		return bbChemStructure;
	}

	@Override
	public String getSDF(BBChemParentStructure bbChemStructure) throws IOException{

		String url = getUrlFromPreprocessorSettings("exportSDFURL");


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

		String url = null;
		try {
			url = getUrlFromPreprocessorSettings("parseURL");
		} catch (IOException e) {
			throw new CmpdRegMolFormatException(e);
		}

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
	public List<String> getMolFragments(String molfile) throws CmpdRegMolFormatException {

		String url = null;
		try {
			url = getUrlFromPreprocessorSettings("splitURL");
		} catch (IOException e) {
			throw new CmpdRegMolFormatException(e);
		}

		// Create the request data object
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
				for (JsonNode sdfNode : splitSDFS)  {
					molStrings.add(sdfNode.asText());
				}
			}
		} catch (Exception e) {
			logger.error("Error posting to split service: " + e.getMessage());
			throw new CmpdRegMolFormatException("Error posting to split service: " + e.getMessage());
		}

		return molStrings;
	}

	@Override
	public HashMap<? extends AbstractBBChemStructure, Boolean> substructureMatch(String queryMol, List<? extends AbstractBBChemStructure> needsMatchStructures)
			throws CmpdRegMolFormatException {
		// Fetch the fingerprint from the BBChem finerprint service

		String url = null;
		try {
			url = getUrlFromPreprocessorSettings("substructureMatchURL");
		} catch (IOException e) {
			throw new CmpdRegMolFormatException(e);
		}
		
		// Create the request data object
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		// Add the structures to the request
		ArrayNode arrayNode = mapper.createArrayNode();
		for(AbstractBBChemStructure needsMatch : needsMatchStructures) {
			arrayNode.add(needsMatch.getMol());
		}
		requestData.put("needs_match_molv3s", arrayNode);
		requestData.put("query_molv3", queryMol);
		requestData.put("boolean_results", true);
		// Post to the service and parse the response
		try {
			String requestString = requestData.toString();
			logger.info("requestString: " + requestString);
			HttpURLConnection connection = SimpleUtil.postRequest(url, requestString, logger);
			String postResponse = null;
			if(connection.getResponseCode() != 200) {
				logger.error("Error posting to substructure match service: " + connection.getResponseMessage());
				throw new CmpdRegMolFormatException("Error posting to substructure match service: " + connection.getResponseMessage());
			} else {
				postResponse = SimpleUtil.getStringBody(connection);
			}
			logger.info("Got response: "+ postResponse);

			// Parse the response json to get the standardized mol
			ObjectMapper responseMapper = new ObjectMapper();
			JsonNode responseNode = responseMapper.readTree(postResponse);
			JsonNode resultsNode = responseNode.get("results");

			HashMap<AbstractBBChemStructure, Boolean> matchMap = new HashMap<>();
			// Loop through length of the results
			for (int i = 0; i < resultsNode.size(); i++) {
				matchMap.put(needsMatchStructures.get(i), resultsNode.get(i).get("has_hits").asBoolean());
			}
			return matchMap;
		} catch (Exception e) {
			logger.error("Error posting to fingerprint service: " + e.getMessage());
			throw new CmpdRegMolFormatException("Error posting to fingerprint service: " + e.getMessage());
		}
	}

}