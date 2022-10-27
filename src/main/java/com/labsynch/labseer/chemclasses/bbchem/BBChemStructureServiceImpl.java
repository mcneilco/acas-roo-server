package com.labsynch.labseer.chemclasses.bbchem;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule.StandardizationStatus;
import com.labsynch.labseer.domain.AbstractBBChemStructure;
import com.labsynch.labseer.dto.StandardizationSettingsConfigCheckResponseDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.Request;
import com.labsynch.labseer.utils.Response;
import com.labsynch.labseer.utils.SimpleUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BBChemStructureServiceImpl implements BBChemStructureService {

	Logger logger = LoggerFactory.getLogger(BBChemStructureServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	private static final String EMPTY_STRUCTURE = "EMPTY_STRUCTURE";
	private static final String ERROR_STRUCTURE = "ERROR_STRUCTURE";


	private final String CONVERTER_PATH = "/converter/api/v0/convert";
	private final String EXPORTSDF_PATH = "/sdf_export/api/v0/";
	private final String FINGERPRINT_PATH = "/fingerprint/api/v0/";
	private final String IMAGE_PATH = "/image/api/v0/";	
	private final String PARSESDF_PATH =  "/parse/api/v0/";	
	private final String PROCESS_PATH = "/preprocessor/api/v0/process";
	private final String SPLIT_PATH = "/split/api/v0";
	private final String SUBSTRUCTURE_PATH = "/substructure/api/v0";
	private final String CONFIG_CHECK_PATH = "/preprocessor/api/v0/config/check";
	private final String CONFIG_FIX_PATH = "/preprocessor/api/v0/config/fix";
	private final String HEALTH_PATH = "/preprocessor/api/v0/health";

	@Override
	public JsonNode getPreprocessorSettings() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(propertiesUtilService.getPreprocessorSettings());
		return jsonNode;
	}

	private HashMap<String, BitSet> molsToFingerprints(HashMap<String, String> structures, String type)
			throws CmpdRegMolFormatException {
		// Fetch the fingerprint from the BBChem fingerprint service
		String url = null;
		url = propertiesUtilService.getLDChemURL() + FINGERPRINT_PATH;

		// Create the request data object
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		// Split the list of structures into chunks of
		// propertiesUtilService.getExternalStructureProcessingBatchSize()
		List<List<String>> structureGroups = splitIntoListOfLists(structures);

		Collection<Callable<Response>> tasks = new ArrayList<>();
		// Create the tasks and include an id for the task
		for (int i = 0; i < structureGroups.size(); i++) {
			List<String> structureGroupList = structureGroups.get(i);

			// Add the structures to the request
			ArrayNode arrayNode = mapper.createArrayNode();

			for (String structure : structureGroupList) {
				arrayNode.add(structure);
			}
			requestData.put("input", arrayNode);
			requestData.put("input_format", "sdf");
			requestData.put("fingerprint_use", type);

			// Post to the service and parse the response
			String requestString = requestData.toString();
			logger.debug("requestString: " + requestString);
			tasks.add(new Request(i, url, requestString));
		}

		ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		logger.info("Invoking " + tasks.size() + " fingerprint tasks");
		List<Future<Response>> results = pool.invokeAll(tasks);
		HashMap<Integer, JsonNode> responseMap = new HashMap<Integer, JsonNode>();
		for (Future<Response> response : results) {
			String responseBody;
			int responseCode;
			Integer responseId;
			try {
				responseBody = response.get().getResponseBody();
				responseCode = response.get().getResponseCode();
				responseId = response.get().getId();
			} catch (InterruptedException | ExecutionException e) {
				throw new CmpdRegMolFormatException("Error posting to fingerprint service: " + e.getMessage());
			}

			if (responseCode != 200) {
				logger.error("Response Body: " + responseBody);
				logger.error("URL was: " + url);
				throw new CmpdRegMolFormatException("Error posting to fingerprint service: " + responseBody);
			}

			// Parse the response json to get the standardized mol
			ObjectMapper responseMapper = new ObjectMapper();
			JsonNode responseNode;
			try {
				responseNode = responseMapper.readTree(responseBody);
			} catch (IOException e) {
				throw new CmpdRegMolFormatException("Error parsing finger print service response: " + responseBody);
			}
			responseMap.put(responseId, responseNode);
		}
		logger.info("Got response for all " + tasks.size() + " fingerprint tasks");

		// The output array is guaranteed to be in the same order as its inputs
		// Sort the response hashmap by the keys
		List<Integer> responseIds = new ArrayList<Integer>(responseMap.keySet());
		Collections.sort(responseIds);

		// Return hashmap with the String key from the input hashmap and the fingerprint
		// BitSet
		HashMap<String, BitSet> fingerprints = new HashMap<String, BitSet>();
		Object[] structuresArray = structures.keySet().toArray();
		int s = 0;
		for (Integer responseId : responseIds) {
			logger.debug("Response ID: " + responseId);
			// Combine the json nodes
			JsonNode responseNode = responseMap.get(responseId);
			JsonNode resultsNode = responseNode.get("fingerprint_results");
			for (int i = 0; i < resultsNode.size(); i++) {
				JsonNode resultNode = resultsNode.get(i);
				String stringFingerPrint = resultNode.asText();
				BitSet bitSet = SimpleUtil.stringToBitSet(stringFingerPrint);
				String structureKey = structuresArray[s].toString();
				s++;
				fingerprints.put(structureKey, bitSet);
			}
		}
		return fingerprints;
	}

	private BitSet getFingerprint(String molStructure, String type) throws CmpdRegMolFormatException {
		// Get a single fingerprint by calling the BBChem fingerprint service
		try {
			// Create the request data object
			HashMap<String, String> structures = new HashMap<String, String>();

			// Temporary key just to match the molToFingerprints hashmap key/value inputs
			String structureKey = "TmpKey01";
			structures.put(structureKey, molStructure);
			HashMap<String, BitSet> fingerPrintHashMap = molsToFingerprints(structures, type);
			return fingerPrintHashMap.get(structureKey);
		} catch (Exception e) {
			logger.error("Error posting to fingerprint service: " + e.getMessage());
			throw new CmpdRegMolFormatException("Error posting to fingerprint service: " + e.getMessage());
		}
	}

	List<List<String>> splitIntoListOfLists(HashMap<String, String> stringHashMap) {
		// Split the hashmap into chunks of
		// propertiesUtilService.getExternalStructureProcessingBatchSize()
		List<List<String>> groups = new ArrayList<List<String>>();
		List<String> group = new ArrayList<String>();
		Object[] array = stringHashMap.keySet().toArray();
		for (String key : stringHashMap.keySet()) {
			group.add(stringHashMap.get(key));

			// Check if structure group size is now
			// propertiesUtilService.getExternalStructureProcessingBatchSize() or this is
			// the last item in the original hashmap
			if (group.size() == propertiesUtilService.getExternalStructureProcessingBatchSize()
					|| key == array[array.length - 1]) {
				groups.add(group);
				group = new ArrayList<String>();
			}
		}
		return groups;
	}

	private JsonNode postToProcessService(HashMap<String, String> structures) throws IOException {

		String url = propertiesUtilService.getLDChemURL() + PROCESS_PATH;

		JsonNode jsonNode = getPreprocessorSettings();

		// Create the request data object
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		// Get the process options
		ObjectNode options = (ObjectNode) jsonNode.get("process_options");

		// Get the standardizer actions
		JsonNode standardizerActions = propertiesUtilService.getStandardizerActions();

		options.replace("standardizer_actions", standardizerActions);
		requestData.put("options", options);

		// Set timeout (default to 900)
		JsonNode timeoutNode = jsonNode.get("timeout");
		int timeout;
		if(timeoutNode == null) {
			timeout = 900;
			logger.info("Timeout not set in preprocessor settings, using default of " + String.valueOf(timeout));
		} else {
			timeout = timeoutNode.asInt();
		}
		requestData.put("timeout", timeout);

		// Split the list of structures into chunks for processing
		List<List<String>> structureGroups = splitIntoListOfLists(structures);

		Collection<Callable<Response>> tasks = new ArrayList<>();
		// Create the tasks and include an id for the task
		for (int i = 0; i < structureGroups.size(); i++) {
			List<String> structureGroupList = structureGroups.get(i);

			// Add the structures to the request
			ArrayNode arrayNode = mapper.createArrayNode();

			for (String structure : structureGroupList) {
				arrayNode.add(structure);
			}
			requestData.put("structures", arrayNode);

			// Post to the service and parse the response
			String requestString = requestData.toString();
			logger.debug("requestString: " + requestString);
			tasks.add(new Request(i, url, requestString));
		}

		ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		logger.info("Invoking " + tasks.size() + " process tasks");
		List<Future<Response>> results = pool.invokeAll(tasks);
		HashMap<Integer, JsonNode> responseMap = new HashMap<Integer, JsonNode>();
		for (Future<Response> response : results) {
			String responseBody;
			int responseCode;
			int responseId;
			try {
				responseBody = response.get().getResponseBody();
				responseCode = response.get().getResponseCode();
				responseId = response.get().getId();
			} catch (InterruptedException | ExecutionException e) {
				throw new IOException("Error posting to process service: " + e.getMessage());
			}

			if (responseCode != 200) {
				logger.error("Response Body: " + responseBody);
				logger.error("URL was: " + url);
				throw new IOException("Error posting to process service: " + responseBody);
			}

			// Parse the response json to get the standardized mol
			ObjectMapper responseMapper = new ObjectMapper();
			JsonNode responseNode = responseMapper.readTree(responseBody);
			responseMap.put(responseId, responseNode);
		}
		logger.info("Got response for all " + tasks.size() + " process tasks");

		// Sort the response hashmap by the keys
		List<Integer> responseIds = new ArrayList<Integer>(responseMap.keySet());
		Collections.sort(responseIds);
		// Empty json node array
		ArrayNode responseArray = mapper.createArrayNode();
		for (Integer responseId : responseIds) {
			logger.debug("Response ID: " + responseId);
			// Combine the json nodes
			JsonNode responseNode = responseMap.get(responseId);
			responseArray.addAll((ArrayNode) responseNode);
		}
		return responseArray;
	}

	@Override
	public JsonNode postToProcessService(String molfile) throws IOException {

		// New hashamp
		HashMap<String, String> structures = new HashMap<String, String>();
		// Add the molfile to the hashmap
		structures.put("mol", molfile);
		// Post to the service
		JsonNode jsonNode = postToProcessService(structures);

		return jsonNode;
	}

	@Override
	public HashMap<String, BBChemParentStructure> getProcessedStructures(HashMap<String, String> structures,
			Boolean includeFingerprints) throws CmpdRegMolFormatException {

		// Return map
		HashMap<String, BBChemParentStructure> processedStructures = new HashMap<String, BBChemParentStructure>();

		// Post to the service and parse the response
		try {
			JsonNode responseNode;
			try {
				responseNode = postToProcessService(structures);
			} catch (IOException e) {
				throw new CmpdRegMolFormatException(e);
			}

			// The output array is guaranteed to be in the same order as its inputs
			// Its most efficient to match the input keys to the output keys by index
			// when the input keys are converted to an array first
			Object[] structuresArray = structures.keySet().toArray();
			// Loop through length of response node
			for (int i = 0; i < responseNode.size(); i++) {

				JsonNode responseJsonNode = responseNode.get(i);

				BBChemParentStructure bbChemStructure = new BBChemParentStructure();

				// The output array is guaranteed to be in the same order as its inputs
				// So get the key from the array we created above and assume the same order
				String structureKey = structuresArray[i].toString();

				// Throw exception if there is an error reading the molecule
				JsonNode errorCodeNode = responseJsonNode.get("error_code");

				// Check if we have an error code
				if (errorCodeNode != null) {
					String errorCode = errorCodeNode.asText();
					String msg = responseJsonNode.get("error_msg").asText();
					if (errorCode.equals("4004")) {
						logger.warn("Processor error code '" + errorCode + "' for '" + structureKey + "': " + msg);
						bbChemStructure.setStandardizationStatus(StandardizationStatus.SUCCESS);
						bbChemStructure.setReg(EMPTY_STRUCTURE);
						bbChemStructure.setPreReg(EMPTY_STRUCTURE);
						bbChemStructure.setMol(structures.get(structureKey));
						bbChemStructure.setAverageMolWeight(0.0);
						bbChemStructure.setExactMolWeight(0.0);
						bbChemStructure.setTotalCharge(0);
						bbChemStructure.setRegistrationStatus(CmpdRegMolecule.RegistrationStatus.WARNING);
						bbChemStructure.setRegistrationComment(msg);

					} else {
						// Print the structures
						logger.error("Processor error code '" + errorCode + "' for '" + structureKey + "': " + msg);
						bbChemStructure.setStandardizationStatus(StandardizationStatus.ERROR);
						bbChemStructure.setReg(ERROR_STRUCTURE);
						bbChemStructure.setPreReg(ERROR_STRUCTURE);
						bbChemStructure.setMol(structures.get(structureKey));
						bbChemStructure.setRegistrationStatus(CmpdRegMolecule.RegistrationStatus.ERROR);
						bbChemStructure.setRegistrationComment(msg);
					}
				} else {

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

					bbChemStructure.setRegistrationStatus(CmpdRegMolecule.RegistrationStatus.SUCCESS);
					bbChemStructure.setRegistrationComment(null);

					bbChemStructure.setStandardizationStatus(StandardizationStatus.SUCCESS);
					
				}

				// Set recorded date todays date. This simplifies the code when we need to
				// persist the structure
				// in various places.
				bbChemStructure.setRecordedDate(new Date());

				// Add to the map
				processedStructures.put(structureKey, bbChemStructure);
			}

			if (includeFingerprints) {
				// Input fingerprint hashes
				HashMap<String, String> processedStructureHash = new HashMap<String, String>();
				for (String structureId : processedStructures.keySet()) {
					BBChemParentStructure processedStructure = processedStructures.get(structureId);
					if (processedStructure.getRegistrationStatus() == CmpdRegMolecule.RegistrationStatus.SUCCESS) {
						processedStructureHash.put(structureId, processedStructure.getMol());
					}
				}

				// Get the fingerprints
				HashMap<String, BitSet> substructureHashMap = molsToFingerprints(processedStructureHash,
						"substructure_search");
				HashMap<String, BitSet> similarityHashMap = molsToFingerprints(processedStructureHash,
						"similarity_score");

				// Add the substructure fingerprints to the processed structures
				for (String structureId : substructureHashMap.keySet()) {
					processedStructures.get(structureId).setSubstructure(substructureHashMap.get(structureId));
				}

				// Add the similarity fingerprints to the processed structures
				for (String structureId : similarityHashMap.keySet()) {
					processedStructures.get(structureId).setSimilarity(similarityHashMap.get(structureId));
				}
			}

			return processedStructures;
		} catch (CmpdRegMolFormatException e) {
			logger.error("Error processing structures: " + e.getMessage());
			throw new CmpdRegMolFormatException(e);
		}

	}

	@Override
	public BBChemParentStructure getProcessedStructure(String molfile, Boolean includeFingerprints)
			throws CmpdRegMolFormatException {
		BBChemParentStructure bbChemStructure = new BBChemParentStructure();
		// Post to the service and parse the response
		try {
			// Input hashmap
			HashMap<String, String> structures = new HashMap<String, String>();
			structures.put("molstructure", molfile);

			HashMap<String, BBChemParentStructure> processedStructuresHashMap = getProcessedStructures(structures,
					includeFingerprints);

			// Get the first structure
			bbChemStructure = processedStructuresHashMap.get("molstructure");
			return bbChemStructure;

		} catch (CmpdRegMolFormatException e) {
			logger.error("Error in getProcessedStructures: " + e.getMessage());
			throw new CmpdRegMolFormatException(e);
		}
	}

	@Override
	public String getSDF(List<BBChemParentStructure> bbChemStructures) throws IOException {

		String url = propertiesUtilService.getLDChemURL() + EXPORTSDF_PATH;

		// Create the request data object
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		// Create sdfs arraynode and mol node
		ArrayNode sdfsNode = mapper.createArrayNode();

		for(BBChemParentStructure bbChemStructure : bbChemStructures) {
			ObjectNode molNode = mapper.createObjectNode();

			// Add the properties to the mol node
			ArrayNode propertiesNode = mapper.createArrayNode();
			for (Map.Entry<String, String> entry : bbChemStructure.getProperties().entrySet()) {
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
	
	
		}

		// Add the sdfs to the request data
		requestData.put("sdfs", sdfsNode);
		
		// Post to the service and parse the response
		String requestString = requestData.toString();
		logger.info("Making to bbchem sdf export service");
		logger.debug("requestString: " + requestString);
		String postResponse = SimpleUtil.postRequestToExternalServer(url, requestString, logger);
		logger.info("Got response from bbchem sdf export service");
		logger.debug("Got response: " + postResponse);

		// Parse the response json
		ObjectMapper responseMapper = new ObjectMapper();
		JsonNode responseNode = responseMapper.readTree(postResponse);
		return responseNode.asText();

	}

	@Override
	public String getSDF(BBChemParentStructure bbChemStructure) throws IOException {

		ArrayList<BBChemParentStructure> structureList = new ArrayList<BBChemParentStructure>();
		structureList.add(bbChemStructure);
		return getSDF(structureList);
	}

	@Override
	public List<BBChemParentStructure> parseSDF(String molfile) throws CmpdRegMolFormatException {
		List<BBChemParentStructure> bbChemStructures = new ArrayList<BBChemParentStructure>();

		String url = propertiesUtilService.getLDChemURL() + PARSESDF_PATH;

		// Create the request data object
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		// Add the structures to the request
		Base64.Encoder encoder = Base64.getEncoder();
		String encodedString = encoder.encodeToString(molfile.getBytes(StandardCharsets.UTF_8));
		requestData.put("molv3", encodedString);

		// Post to the service and parse the response
		try {
			String requestString = requestData.toString();
			logger.debug("requestString: " + requestString);
			HttpURLConnection connection = SimpleUtil.postRequest(url, requestString, logger);
			String postResponse = null;
			if (connection.getResponseCode() != 200) {
				logger.error("Error posting to sdf parse service: " + connection.getResponseMessage());
				String body = SimpleUtil.getStringBody(connection);
				logger.error("Response Body: " + body);
				logger.error("URL was: " + url);
				logger.error("Request was : " + requestString);
				throw new CmpdRegMolFormatException(body);
			} else {
				postResponse = SimpleUtil.getStringBody(connection);
			}
			logger.debug("Got response: " + postResponse);

			// Parse the response json to get the standardized mol
			ObjectMapper responseMapper = new ObjectMapper();
			JsonNode responseNode = responseMapper.readTree(postResponse);
			for (JsonNode responseJsonNode : responseNode) {
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
			logger.error("Error posting to parse service: " + e);
			throw new CmpdRegMolFormatException(e.getMessage());
		}

		return bbChemStructures;

	}

	@Override
	public List<String> getMolFragments(String molfile) throws CmpdRegMolFormatException {

		String url = propertiesUtilService.getLDChemURL() + SPLIT_PATH;

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
			logger.debug("requestString: " + requestString);
			HttpURLConnection connection = SimpleUtil.postRequest(url, requestString, logger);
			String postResponse = null;
			if (connection.getResponseCode() != 200) {
				logger.error("Error posting to split service: " + connection.getResponseMessage());
				logger.error("Response Body: " + SimpleUtil.getStringBody(connection));
				logger.error("URL was: " + url);
				logger.error("Request was : " + requestString);
				throw new CmpdRegMolFormatException(
						"Error posting to split service: " + connection.getResponseMessage());
			} else {
				postResponse = SimpleUtil.getStringBody(connection);
			}
			logger.debug("Got response: " + postResponse);

			// Parse the response json to get the standardized mol
			ObjectMapper responseMapper = new ObjectMapper();
			JsonNode responseNode = responseMapper.readTree(postResponse);
			JsonNode resultsNode = responseNode.get("results");
			for (JsonNode resultNode : resultsNode) {
				JsonNode splitSDFS = resultNode.get("split_sdfs");
				for (JsonNode sdfNode : splitSDFS) {
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
	public HashMap<? extends AbstractBBChemStructure, Boolean> substructureMatch(String queryMol,
			List<? extends AbstractBBChemStructure> needsMatchStructures)
			throws CmpdRegMolFormatException {
		// Fetch the fingerprint from the BBChem finerprint service

		String url = propertiesUtilService.getLDChemURL() + SUBSTRUCTURE_PATH;

		// Create the request data object
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		// Add the structures to the request
		ArrayNode arrayNode = mapper.createArrayNode();
		for (AbstractBBChemStructure needsMatch : needsMatchStructures) {
			arrayNode.add(needsMatch.getMol());
		}
		requestData.replace("needs_match", arrayNode);
		requestData.put("query", queryMol);
		requestData.put("query_format", "sdf");
		requestData.put("boolean_results", true);
		// Post to the service and parse the response
		try {
			String requestString = requestData.toString();
			logger.debug("requestString: " + requestString);
			HttpURLConnection connection = SimpleUtil.postRequest(url, requestString, logger);
			String postResponse = null;
			if (connection.getResponseCode() != 200) {
				logger.error("Error posting to substructure match service: " + connection.getResponseMessage());
				logger.error("Response Body: " + SimpleUtil.getStringBody(connection));
				logger.error("URL was: " + url);
				logger.error("Request was : " + requestString);
				throw new CmpdRegMolFormatException(
						"Error posting to substructure match service: " + connection.getResponseMessage());
			} else {
				postResponse = SimpleUtil.getStringBody(connection);
			}
			logger.debug("Got response: " + postResponse);

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

	public byte[] callImageService(CmpdRegMolecule molecule, String imageFormat, String hSize, String wSize)
			throws IOException {

		// Extract the url to call
		String url = propertiesUtilService.getLDChemURL() + IMAGE_PATH;

		// Create the request json
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();
		String mol = "";
		try {
			mol = molecule.getMolStructure();
		} catch (CmpdRegMolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		requestData.put("molv3", mol);
		requestData.put("format", imageFormat);
		ObjectNode draw_options = mapper.createObjectNode();
		draw_options.put("width", wSize);
		draw_options.put("height", hSize);
		requestData.replace("draw_options", draw_options);
		String request = requestData.toString();
		logger.info("Image request" + request);

		// Return the response bytes
		return SimpleUtil.postRequestToExternalServerBinaryResponse(url, request, logger);
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
	public String convert(String structure, String inputFormat, String outputFormat)
			throws IOException, CmpdRegMolFormatException {

		// Read the preprocessor settings as json
		String url = propertiesUtilService.getLDChemURL() + CONVERTER_PATH;

		// Create the request format
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		requestData.put("input_format", getServiceFormat(inputFormat));
		requestData.put("output_format", getServiceFormat(outputFormat));

		ArrayNode inputsNode = mapper.createArrayNode();
		inputsNode.add(structure);
		requestData.replace("inputs", inputsNode);

		// Post to the service
		String postResponse = SimpleUtil.postRequestToExternalServer(url, requestData.toString(), logger);
		logger.debug("Got response: " + postResponse);

		// Parse the response json
		ObjectMapper responseMapper = new ObjectMapper();
		JsonNode responseNode = responseMapper.readTree(postResponse);
		return responseNode.get(0).asText();
	}

	@Override
	public StandardizationSettingsConfigCheckResponseDTO configCheck(String oldConfig, String newConfig, String oldHashScheme, String newHashScheme, String oldPreprocessorVersion, String oldSchrodingerSuiteVersion) throws IOException {
		// The service call to BBChem accepts old configurations and new configurations and returns validation results including if the new config is valid, if preprocessing (standardization) is needed
		// and reasons for why the new config is invalid and/or preprocessing is needed
		// accepts e.g.:
		// {
		// 	"new_config": {
		// 		"CHOOSE_CANONICAL_TAUTOMER": false,
		// 		"CLEAR_INVALID_WEDGE_BONDS": true,
		// 		"EXPLICIT_HYDROGENS": "REMOVE_ALL",
		// 		"GENERATE_COORDINATES": "FULL_ALIGNED",
		// 		"GENERATE_V3K_SDF": true,
		// 		"HEAVY_HYDROGEN_DT": false,
		// 		"KEEP_ONLY_LARGEST_STRUCTURE": false,
		// 		"NEUTRALIZE": true,
		// 		"REMOVE_PROPERTIES": false,
		// 		"REMOVE_SGROUP_DATA": "NONE",
		// 		"RING_REPRESENTATION": "KEKULE",
		// 		"STRIP_AND_GROUPS_ON_SINGLE_ATOM": true,
		// 		"TRANSFORMATIONS": [],
		// 		"CHIRAL_FLAG_0_MEANING": "UNGROUPED_ARE_ABSOLUTE",
		// 		"RESOLVE_AMBIGUOUS_TAUTOMERS": false
		// 	},
		// 	"new_hash_scheme": "TAUTOMER_INSENSITIVE_LAYERS",
		// 	"old_state": {
		// 		"schrodinger_suite_version": "bbchem_2019.1-61",
		// 		"preprocessor_version": "Schrodinger Suite 2019-1, Build 130",
		// 		"config": {
		// 			"CHOOSE_CANONICAL_TAUTOMER": false,
		// 			"CLEAR_INVALID_WEDGE_BONDS": true,
		// 			"EXPLICIT_HYDROGENS": "REMOVE_ALL",
		// 			"GENERATE_COORDINATES": "FULL_ALIGNED",
		// 			"HEAVY_HYDROGEN_DT": false,
		// 			"KEEP_ONLY_LARGEST_STRUCTURE": false,
		// 			"NEUTRALIZE": true,
		// 			"REMOVE_PROPERTIES": false,
		// 			"REMOVE_SGROUP_DATA": "NONE",
		// 			"RING_REPRESENTATION": "KEKULE",
		// 			"STRIP_AND_GROUPS_ON_SINGLE_ATOM": true,
		// 			"TRANSFORMATIONS": [],
		// 			"CHIRAL_FLAG_0_MEANING": "UNGROUPED_ARE_ABSOLUTE",
		// 			"RESOLVE_AMBIGUOUS_TAUTOMERS": false
		// 		},
		// 		"hash_scheme": "TAUTOMER_INSENSITIVE_LAYERS"
		// 	}
		// }
		// Returns e.g.
		// {
		// 	"valid": true,
		// 	"new_state": {
		// 		"config": "{\"CHIRAL_FLAG_0_MEANING\": \"UNGROUPED_ARE_ABSOLUTE\", \"CHOOSE_CANONICAL_TAUTOMER\": false, \"CLEAR_INVALID_WEDGE_BONDS\": true, \"EXPLICIT_HYDROGENS\": \"REMOVE_ALL\", \"GENERATE_COORDINATES\": \"FULL_ALIGNED\", \"HEAVY_HYDROGEN_DT\": false, \"KEEP_ONLY_LARGEST_STRUCTURE\": false, \"MAX_NUM_ATOMS\": null, \"NEUTRALIZE\": true, \"REMOVE_PROPERTIES\": false, \"REMOVE_SGROUP_DATA\": \"NONE\", \"RESOLVE_AMBIGUOUS_TAUTOMERS\": false, \"RING_REPRESENTATION\": \"KEKULE\", \"STRIP_AND_GROUPS_ON_SINGLE_ATOM\": true, \"STRIP_SALTS\": null, \"TRANSFORMATIONS\": []}",
		// 		"hash_scheme": "TAUTOMER_INSENSITIVE_LAYERS",
		// 		"preprocessor_version": "bbchem_2022.1-61",
		// 		"schrodinger_suite_version": "Schrodinger Suite 2022-2, Build 130"
		// 	},
		// 	"rerun_preprocessor": false
		// }
		String url = propertiesUtilService.getLDChemURL() + CONFIG_CHECK_PATH;

		// Create the request format
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		// New config
		requestData.replace("new_config", mapper.readTree(newConfig));
		requestData.put("new_hash_scheme", newHashScheme);

		// Old state
		ObjectNode oldState = mapper.createObjectNode();
		oldState.replace("config", mapper.readTree(oldConfig));
		oldState.put("hash_scheme", oldHashScheme);
		oldState.put("preprocessor_version", oldPreprocessorVersion);
		oldState.put("schrodinger_suite_version", oldSchrodingerSuiteVersion);
		requestData.replace("old_state", oldState);

		// Post to the service
		String request = requestData.toString();
		logger.info("Making a config check request to " + url + " with " + request);
		String postResponse = SimpleUtil.postRequestToExternalServer(url, request, logger);
		logger.info("Got response: " + postResponse);

		StandardizationSettingsConfigCheckResponseDTO response = new StandardizationSettingsConfigCheckResponseDTO();
		JsonNode responseNode = mapper.readTree(postResponse);

		// Check if the config is valid
		if(responseNode.get("valid") != null) {
			response.setValid(responseNode.get("valid").asBoolean());
		} else {
			response.setValid(false);
		}

		// Check if rerun_preprocessor
		if(responseNode.get("rerun_preprocessor") != null) {
			response.setNeedsRestandardization(responseNode.get("rerun_preprocessor").asBoolean());
		} else {
			response.setNeedsRestandardization(true);
		}

		// Check if reason
		if(responseNode.get("reasons") != null) {
			// response node reasons is an array of strings create a new array of strings and get it from the response node
			ArrayNode reasons = (ArrayNode) responseNode.get("reasons");
			List<String> reasonsList = new ArrayList<>();
			for (JsonNode reason : reasons) {
				reasonsList.add(reason.textValue());
			}
			response.setReasons(reasonsList);
		}
			
		return response;
	}

	@Override
	public StandardizationSettingsConfigCheckResponseDTO configFix(JsonNode inputConfigs) throws IOException {
		// Services a list of configs and returns a list of "fixed" configs
		// e.g.
		// ["{\"CHIRAL_FLAG_0_MEANING\": \"UNGROUPED_ARE_ABSOLUTE\", \"CHOOSE_CANONICAL_TAUTOMER\": false, \"CLEAR_INVALID_WEDGE_BONDS\": true, \"EXPLICIT_HYDROGENS\": \"REMOVE_ALL\", \"GENERATE_COORDINATES\": \"FULL_ALIGNED\", \"HEAVY_HYDROGEN_DT\": false, \"KEEP_ONLY_LARGEST_STRUCTURE\": false, \"MAX_NUM_ATOMS\": null, \"NEUTRALIZE\": true, \"REMOVE_PROPERTIES\": false, \"REMOVE_SGROUP_DATA\": \"NONE\", \"RESOLVE_AMBIGUOUS_TAUTOMERS\": false, \"RING_REPRESENTATION\": \"KEKULE\", \"STRIP_AND_GROUPS_ON_SINGLE_ATOM\": true, \"STRIP_SALTS\": null, \"TRANSFORMATIONS\": []}"]
		// returns e.g.
		// [
		//     {
		//         "is_input_valid": true,
		//         "error_message": "",
		//         "fixed_config": {
		//             "MAX_NUM_ATOMS": null,
		//             "KEEP_ONLY_LARGEST_STRUCTURE": false,
		//             "REMOVE_PROPERTIES": false,
		//             "STRIP_SALTS": null,
		//             "RESOLVE_AMBIGUOUS_TAUTOMERS": false,
		//             "CHOOSE_CANONICAL_TAUTOMER": false,
		//             "TRANSFORMATIONS": [],
		//             "NEUTRALIZE": true,
		//             "EXPLICIT_HYDROGENS": "REMOVE_ALL",
		//             "GENERATE_COORDINATES": "FULL_ALIGNED",
		//             "CHIRAL_FLAG_0_MEANING": "UNGROUPED_ARE_ABSOLUTE",
		//             "HEAVY_HYDROGEN_DT": false,
		//             "RING_REPRESENTATION": "KEKULE",
		//             "REMOVE_SGROUP_DATA": "NONE",
		//             "CLEAR_INVALID_WEDGE_BONDS": true,
		//             "STRIP_AND_GROUPS_ON_SINGLE_ATOM": true
		//         }
		//     }
		// ]
		String url = propertiesUtilService.getLDChemURL() + CONFIG_FIX_PATH;
		
		// Array of arrays
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode requestData = mapper.createArrayNode();
		requestData.add(inputConfigs.toString());
		String request = requestData.toString();
		logger.info("Making a config fix request to " + url + " with " + request);
		String postResponse = SimpleUtil.postRequestToExternalServer(url, request, logger);
		logger.info("Got response: " + postResponse);

		// Parse the response json into an array of arrays with the first element being the object we need to interpret
		JsonNode responseNode = mapper.readTree(postResponse);
		JsonNode firstElement = responseNode.get(0);

		StandardizationSettingsConfigCheckResponseDTO response = new StandardizationSettingsConfigCheckResponseDTO();
		if(firstElement.get("is_input_valid") != null) {
			response.setValid(firstElement.get("is_input_valid").asBoolean());
		} else {
			response.setValid(false);
		}

		if(firstElement.get("error_message") != null) {
			response.setReasons(firstElement.get("error_message").asText());
		}

		if(firstElement.get("fixed_config") != null) {
			JsonNode fixedConfigs = firstElement.get("fixed_config");
			response.setValidatedSettings(fixedConfigs.toString());
			
			// The service doesn't return a list of reasons, so we print it here
			List<String> diffs = SimpleUtil.diffJsonObjects(fixedConfigs, inputConfigs);
			if(diffs.size() > 0) {
				logger.warn("Found diffs in the config fix response: " + diffs);
				for(String diff : diffs) {
					logger.warn(diff);
				}
			}

			response.setSuggestedConfigurationChanges(diffs);
		}

		return response;

	}

	@Override
	public JsonNode health() throws IOException {
		// Get request to the health endpoint
		// returns e.g.
		// {
		// 	"status": "OK",
		// 	"formats": [
		// 		"MOL",
		// 		"SDF"
		// 	],
		// 	"default_timeout": 60,
		// 	"suite_version": "Schrodinger Suite 2022-2, Build 130",
		// 	"preprocessor_version": "bbchem_2022.1-61",
		// 	"currently_submitted": 0,
		// 	"recent_jobs": 0,
		// 	"recent_error_rate": "n/a",
		// 	"recent_timeout_rate": "n/a",
		// 	"recent_avg_total_runtime": "n/a",
		// 	"recent_avg_runtime_per_success": "n/a"
		// }
		String url = propertiesUtilService.getLDChemURL() + HEALTH_PATH;

		logger.info("Making a health request to " + url);
		String response = SimpleUtil.getRequestToExternalServer(url, logger);
		logger.info("Got health response: " + response);
		// Parse the response json
		ObjectMapper responseMapper = new ObjectMapper();
		JsonNode responseNode = responseMapper.readTree(response);
		return responseNode;
	}
		
	

}
