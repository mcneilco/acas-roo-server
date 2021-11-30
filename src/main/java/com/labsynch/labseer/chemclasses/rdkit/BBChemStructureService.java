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
public class BBChemStructureService  implements ExternalStructureService {

	Logger logger = LoggerFactory.getLogger(BBChemStructureService.class);

	static {
		System.loadLibrary("GraphMolWrap");
	}

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	
	@Override
	public void populateDescriptors(RDKitStructure rdKitStructure) {

		RWMol mol = getPartiallySanitizedRWMol(rdKitStructure.getMol());

		// https://www.rdkit.org/docs/cppapi/classRDKit_1_1ROMol.html#a48ab4e1692b503eda6d31c5bb10fe0bb
		// calculates any of our lazy properties
		// 	bool 	strict = true	
		mol.updatePropertyCache(false);
		
		// https://www.rdkit.org/docs/cppapi/namespaceRDKit_1_1Descriptors.html#a4a0f6886e3798fcb195b6c270a3645c6
		// const ROMol & 	mol,
		// bool 	onlyHeavy = false
		rdKitStructure.setAverageMolWeight(RDKFuncs.calcAMW(mol, false));

		// https://www.rdkit.org/docs/cppapi/namespaceRDKit_1_1Descriptors.html#a468168875d98b415e14c6eac9d7c36ae
		// const ROMol & 	mol,
		// bool  onlyHeavy = false
		// https://www.mail-archive.com/rdkit-discuss@lists.sourceforge.net/msg10222.html
		// MolWt uses naturally occurring average atomic weights, the ones you find in
		// a typical periodic table. For example, Cl = 35.453.
		// ExactMolWt uses the weight of a specific isotope (the most naturally
		// abundant isotope unless the structure specifies a different one for an
		// atom). These are the atomic weights you find in an isotope chart, not a
		// regular periodic table. For example, 35Cl = 34.96885268.
		// https://www.nist.gov/pml/atomic-weights-and-isotopic-compositions-relative-atomic-masses
		rdKitStructure.setExactMolWeight(RDKFuncs.calcExactMW(mol, false));

		// https://www.rdkit.org/docs/cppapi/namespaceRDKit.html#a75e1240acc451a7a73280220f79b6c05
		// mol	: the molecule in question.
		// doIsomericSmiles	: include stereochemistry and isotope information in the SMILES
		// doKekule	: do Kekule smiles (i.e. don't use aromatic bonds) NOTE that this will throw an exception if the molecule cannot be kekulized.
		// rootedAtAtom	: make sure the SMILES starts at the specified atom. The resulting SMILES is not, of course, canonical.
		// canonical	: if false, no attempt will be made to canonicalize the SMILES
		// allBondsExplicit	: if true, symbols will be included for all bonds.
		// allHsExplicit	: if true, hydrogen counts will be provided for every atom.
		// doRandom : undocumented as of 2021-11-29 but default is false
		rdKitStructure.setSmiles(RDKFuncs.MolToSmiles(mol, true, false, -1, true, false, false, false, false));

		// https://www.rdkit.org/docs/cppapi/namespaceRDKit_1_1MolOps.html#a3c5021831c089f1dfacf1f3ce2ff9303
		rdKitStructure.setTotalCharge(RDKFuncs.getFormalCharge(mol));

		// https://www.rdkit.org/docs/source/rdkit.Chem.rdinchi.html#rdkit.Chem.rdinchi.MolBlockToInchi
		// Doesn't appear to be used anywhere in the code base
		// rdKitStructure.setInchi(RDKFuncs.MolBlockToInchi(rdKitStructure.getMol(), new ExtraInchiReturnValues());

		// https://www.rdkit.org/docs/cppapi/namespaceRDKit_1_1Descriptors.html#a4a80d0fa71a5bad581e4436fe37e9ce6
		// mol	the molecule of interest
		// separateIsotopes	if true, isotopes will show up separately in the formula. So C[13CH2]O will give the formula: C[13C]H6O
		// abbreviateHIsotopes	if true, 2H and 3H will be represented as D and T instead of [2H] and [3H]. This only applies if separateIsotopes is true
		rdKitStructure.setMolecularFormula(RDKFuncs.calcMolFormula(mol, false, false));
	}

	@Override
	public JsonNode getPreprocessorSettings() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(propertiesUtilService.getPreprocessorSettings());
		return jsonNode;
	}

	@Override
	public RDKitStructure getProcessedStructure(String molfile) throws CmpdRegMolFormatException {
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
	public String getSDF(RDKitStructure rdkitStructure) throws IOException{
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
	public List<RDKitStructure> parseSDF(String molfile) throws CmpdRegMolFormatException {
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

}
