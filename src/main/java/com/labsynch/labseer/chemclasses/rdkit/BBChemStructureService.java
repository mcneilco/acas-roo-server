package com.labsynch.labseer.chemclasses.rdkit;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.labsynch.labseer.domain.RDKitStructure;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ExternalStructureService;
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
public class BBChemStructureService  implements ExternalStructureService {
    
    Logger logger = LoggerFactory.getLogger(BBChemStructureService.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
    public void populateDescriptors(RDKitStructure rdKitStructure) {
		rdKitStructure.setAverageMolWeight(-1.0);
		rdKitStructure.setExactMolWeight(-1.0);
		rdKitStructure.setSmiles("C");
		rdKitStructure.setTotalCharge(-1);
	}

	@Override
	public JsonNode getPreprocessorSettings() throws IOException {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(propertiesUtilService.getPreprocessorSettings());
			return jsonNode;
	}

	@Override
	public RDKitStructure getRDKitStructureFromProcessService(String molfile) throws CmpdRegMolFormatException {
		RDKitStructure rdkitStructure = new RDKitStructure();

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
			String postResponse = SimpleUtil.postRequestToExternalServer(url, requestString, logger);
			logger.info("Got response: "+ postResponse);

			// Parse the response json to get the standardized mol
			ObjectMapper responseMapper = new ObjectMapper();
			JsonNode responseNode = responseMapper.readTree(postResponse);
			for (JsonNode responseJsonNode : responseNode)  {
				JsonNode registrationHashesNode = responseJsonNode.get("registration_hash");
				String registrationHash = registrationHashesNode.get(0).asText();
				rdkitStructure.setReg(registrationHashesNode.get(0).asText());

				JsonNode noStereoHashNode = responseJsonNode.get("no_stereo_hash");
				rdkitStructure.setPreReg(noStereoHashNode.asText());

				JsonNode sdfNode = responseJsonNode.get("sdf");
				rdkitStructure.setMol(sdfNode.asText());

				JsonNode averageMolWeightNode = responseJsonNode.get("molecular_weight");
				rdkitStructure.setAverageMolWeight(averageMolWeightNode.asDouble());

				JsonNode exactMolWeightNode = responseJsonNode.get("molecular_weight");
				rdkitStructure.setExactMolWeight(-1.0);

				JsonNode smilesNode = responseJsonNode.get("smiles");
				rdkitStructure.setSmiles(smilesNode.asText());

				JsonNode molecularFormulaNode = responseJsonNode.get("molecular_formula");
				rdkitStructure.setMolecularFormula(molecularFormulaNode.asText());

			}


		} catch (Exception e) {
			logger.error("Error posting to preprocessor service: " + e.getMessage());
			throw new CmpdRegMolFormatException("Error posting to preprocessor service: " + e.getMessage());
		}

		// Set recorded date todays date. This simplifies the code when we need to persist the structure
		// in various places.
		rdkitStructure.setRecordedDate(new Date());
		return rdkitStructure;
	}

	@Override
	public String getSDFFromRDkitStructure(RDKitStructure rdkitStructure) throws IOException{
		// Read the preprocessor settings as json
		JsonNode jsonNode = getPreprocessorSettings();

		// Extract the processURL
		JsonNode urlNode = jsonNode.get("exportSDFURL");
		if(urlNode == null || urlNode.isNull()) {
			logger.error("Missing preprocessorSettings exportSDFURL!!");
		}
		String url = urlNode.asText();

		// Get the standardization actions and options
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		// Create sdfs arraynode and mol node
		ArrayNode sdfsNode = mapper.createArrayNode();
		ObjectNode molNode = mapper.createObjectNode();


		// Add the properties to the mol node
		ArrayNode propertiesNode = mapper.createArrayNode();
		for (Map.Entry<String,String> entry : rdkitStructure.getProperties().entrySet()) {
			ObjectNode propertyNode = mapper.createObjectNode();
			propertyNode.put("name", entry.getKey());
			propertyNode.put("value", entry.getValue());
			propertiesNode.add(propertyNode);

		}
		molNode.put("properties", propertiesNode);

		// Add the structure to the mol node
		molNode.put("sdf", rdkitStructure.getMol());

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
	public List<RDKitStructure> getRDKitStructuresFromSDFService(String molfile) throws CmpdRegMolFormatException {
		List<RDKitStructure> rdkitStructures = new ArrayList<RDKitStructure>();

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

		// Get the standardization actions and options
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
				RDKitStructure rdkitStructure = new RDKitStructure();
				JsonNode molNode = responseJsonNode.get("mol");
				rdkitStructure.setMol(molNode.asText());

				rdkitStructure.setProperties(new HashMap<String, String>());
				JsonNode propertiesJsonNode = responseJsonNode.get("properties");
				for (JsonNode propertyNode : propertiesJsonNode) {
					String name = propertyNode.get("name").asText();
					String value = propertyNode.get("value").asText();
					rdkitStructure.getProperties().put(name, value);
				}
				rdkitStructures.add(rdkitStructure);
			}


		} catch (Exception e) {
			logger.error("Error posting to preprocessor service: " + e);
			throw new CmpdRegMolFormatException("Error posting to preprocessor service: " + e);
		}

		return rdkitStructures;

	}
}
