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
	private final String FINGERPRINT_PATH = "/fingerprint/api/v0";
	private final String IMAGE_PATH = "image/api/v0/";	
	private final String PARSESDF_PATH =  "/parse/api/v0/";	
	private final String PROCESS_PATH = "/preprocessor/api/v0/process";
	private final String SPLIT_PATH = "/split/api/v0";
	private final String SUBSTRUCTURE_PATH = "/substructure/api/v0";

	@Override
	public JsonNode getPreprocessorSettings() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(propertiesUtilService.getPreprocessorSettings());
		return jsonNode;
	}

	private String getLDChemBaseUrl() throws IOException {
		JsonNode jsonNode = null;
		jsonNode = getPreprocessorSettings();
		JsonNode urlNode = jsonNode.get("ldchemURL");
		if (urlNode == null || urlNode.isNull()) {
			logger.error("Missing preprocessorSettings ldchemURL!!");
			throw new IOException("Missing preprocessorSettings ldchemURL!!");
		}
		String base_ld_chem_url = urlNode.asText();
		return base_ld_chem_url;
	}

	private HashMap<String, BitSet> molsToFingerprints(HashMap<String, String> structures, String type)
			throws CmpdRegMolFormatException {
		// Fetch the fingerprint from the BBChem fingerprint service
		String url = null;
		try {
			url = getLDChemBaseUrl() + FINGERPRINT_PATH;
		} catch (IOException e) {
			throw new CmpdRegMolFormatException(e);
		}

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

		String url = getLDChemBaseUrl() + PROCESS_PATH;

		JsonNode jsonNode = getPreprocessorSettings();

		// Create the request data object
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		// Get the process options
		ObjectNode options = (ObjectNode) jsonNode.get("process_options");

		// Get the standardizer actions
		JsonNode standardizerActions = jsonNode.get("standardizer_actions");

		options.put("standardizer_actions", standardizerActions);
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

		String url = getLDChemBaseUrl() + EXPORTSDF_PATH;

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

		String url = null;
		try {
			url = getLDChemBaseUrl() + PARSESDF_PATH;
		} catch (IOException e) {
			throw new CmpdRegMolFormatException(e);
		}

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

		String url = null;
		try {
			url = getLDChemBaseUrl() + SPLIT_PATH;
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

		String url = null;
		try {
			url = getLDChemBaseUrl() + SUBSTRUCTURE_PATH;
		} catch (IOException e) {
			throw new CmpdRegMolFormatException(e);
		}

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
		String url = getLDChemBaseUrl() + IMAGE_PATH;

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
		String url = getLDChemBaseUrl() + CONVERTER_PATH;

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
		logger.debug("Got response: " + postResponse);

		// Parse the response json
		ObjectMapper responseMapper = new ObjectMapper();
		JsonNode responseNode = responseMapper.readTree(postResponse);
		return responseNode.get(0).asText();
	}
	
	

}
