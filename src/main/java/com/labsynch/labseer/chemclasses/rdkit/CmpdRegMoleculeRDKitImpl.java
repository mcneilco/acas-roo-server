package com.labsynch.labseer.chemclasses.rdkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import org.RDKit.KeyErrorException;
import org.RDKit.RDKFuncs;
import org.RDKit.ROMol;
import org.RDKit.RWMol;
import org.RDKit.Str_Vect;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public class CmpdRegMoleculeRDKitImpl implements CmpdRegMolecule {

	Logger logger = LoggerFactory.getLogger(CmpdRegMoleculeRDKitImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	RWMol molecule;

	static {
		System.loadLibrary("GraphMolWrap");
	}

	public CmpdRegMoleculeRDKitImpl(String molStructure) throws CmpdRegMolFormatException {
		this.molecule = RWMol.MolFromMolBlock(molStructure);
	}

	public CmpdRegMoleculeRDKitImpl(RWMol molecule) throws CmpdRegMolFormatException {
		this.molecule = molecule;
	}

	public CmpdRegMoleculeRDKitImpl(ROMol readOnlyMol) {
		if (readOnlyMol == null) {
			logger.info("Got empty mol record");
		} else {
			// We want the molecule to be editable so we convert it to a RWMol
			this.molecule = new RWMol(readOnlyMol);
		}
	}

	@Override
	public void setProperty(String key, String value) {
		molecule.setProp(key, value);
	}

	@Override
	public String getProperty(String key) {
		try{
			String prop = molecule.getProp(key);
			if(prop.equals("")) {
				return null;
			} else {
				return prop;
			}
		} catch (KeyErrorException e) {
			return null;
		}
	}

	@Override
	public String[] getPropertyKeys() {
		if (this.molecule != null) {
			List<String> propertyKeys = new ArrayList<String>();
			Str_Vect propList = this.molecule.getPropList();
			for (int i = 0; i < propList.size(); i++) {
				propertyKeys.add(propList.get(i));
			}
			String[] keys = new String[propertyKeys.size()];
			return propertyKeys.toArray(keys);
		} else {
			return new String[0];
		}
	}

	@Override
	public String getPropertyType(String key) {
		// Does not exist
		return null;
	}

	@Override
	public String getMolStructure() throws CmpdRegMolFormatException {
		return this.molecule.MolToMolBlock();
	}

	@Override
	public String getFormula() {
		return RDKFuncs.calcMolFormula(this.molecule);

	}

	@Override
	public Double getExactMass() {
		return RDKFuncs.calcExactMW(this.molecule);
	}

	@Override
	public Double getMass() {
		return RDKFuncs.calcAMW(this.molecule);

	}

	@Override
	public int getTotalCharge() {
		return RDKFuncs.getFormalCharge(this.molecule);

	}

	@Override
	public String getSmiles() {
		return RDKFuncs.MolToSmiles(this.molecule);

	}

	@Override
	public CmpdRegMolecule replaceStructure(String newStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeRDKitImpl newMol = new CmpdRegMoleculeRDKitImpl(newStructure);
		Str_Vect propList = this.molecule.getPropList();
		for (int i = 0; i < propList.size(); i++) {
			newMol.setProperty(propList.get(i), this.getProperty(propList.get(i)));
		}
		return newMol;
	}

	@Override
	public String getMrvStructure() {
		// not implemented in RDkit
		return null;
	}

	@Override
	public byte[] toBinary(CmpdRegMolecule molecule, String imageFormat, String hSize, String wSize)
			throws IOException {

		// Read the preprocessor settings as json
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(propertiesUtilService.getPreprocessorSettings());

		// Extract the url to call
		JsonNode urlNode = jsonNode.get("imageURL");
		if (urlNode == null || urlNode.isNull()) {
			logger.error("Missing preprocessorSettings imageURL!!");
		}
		String url = urlNode.asText();

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
		ObjectNode options = mapper.createObjectNode();
		options.put("width", wSize);
		options.put("height", hSize);
		requestData.put("options", options);
		String request = requestData.toString();
		logger.info("Image request" + request);

		// Return the response bytes
		return SimpleUtil.postRequestToExternalServerBinaryResponse(url, request, logger);

	}

	@Override
	public void dearomatize() {
		this.molecule.Kekulize();
	}

}
