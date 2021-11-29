package com.labsynch.labseer.chemclasses.rdkit;

import java.io.IOException;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.domain.RDKitStructure;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.utils.SimpleUtil;

import org.RDKit.RDKFuncs;
import org.RDKit.RWMol;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

public class CmpdRegMoleculeRDKitImpl implements CmpdRegMolecule {

	Logger logger = LoggerFactory.getLogger(CmpdRegMoleculeRDKitImpl.class);

	static {
		System.loadLibrary("GraphMolWrap");
	}
	
	RDKitStructure molecule;

	private ExternalStructureService bbChemStructureService;

	public CmpdRegMoleculeRDKitImpl(RDKitStructure molecule) {
		this.molecule = molecule;
	}

	public CmpdRegMoleculeRDKitImpl(String singleSDF, ExternalStructureService bbChemStructureService) {
		this.bbChemStructureService = bbChemStructureService;
		try {
			this.molecule = bbChemStructureService.parseSDF(singleSDF).get(0);
		} catch (CmpdRegMolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void setProperty(String key, String value) {
		this.molecule.getProperties().put(key, value);
	}

	@Override
	public String getProperty(String key) {
		String prop = this.molecule.getProperties().get(key);
		if(prop.equals("")) {
			return null;
		} else {
			return prop;
		}
	}

	@Override
	public String[] getPropertyKeys() {
		if (this.molecule != null) {
			return this.molecule.getProperties().keySet().toArray(new String[0]);
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
		return this.molecule.getMol();
	}

	@Override
	public String getFormula() {
		if(this.molecule.getMolecularFormula() != null) {
			return this.molecule.getMolecularFormula();
		} else {
			bbChemStructureService.populateDescriptors(this.molecule);
			return this.molecule.getMolecularFormula();
		}
	}

	@Override
	public Double getExactMass() {
		if(this.molecule.getExactMolWeight() != null) {
			return this.molecule.getExactMolWeight();
		} else {
			bbChemStructureService.populateDescriptors(this.molecule);
			return this.molecule.getExactMolWeight();
		}	
	}

	@Override
	public Double getMass() {
		if(this.molecule.getAverageMolWeight() != null) {
			return this.molecule.getAverageMolWeight();
		} else {
			bbChemStructureService.populateDescriptors(this.molecule);
			return this.molecule.getAverageMolWeight();
		}
	}

	@Override
	public int getTotalCharge() {
		if(this.molecule.getTotalCharge() != null) {
			return this.molecule.getTotalCharge();
		} else {
			bbChemStructureService.populateDescriptors(this.molecule);
			return this.molecule.getTotalCharge();
		}
	}

	@Override
	public String getSmiles() {
		if(this.molecule.getSmiles() != null) {
			return this.molecule.getSmiles();
		} else {
			bbChemStructureService.populateDescriptors(this.molecule);
			return this.molecule.getSmiles();
		}
	}

	@Override
	public CmpdRegMolecule replaceStructure(String newStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeRDKitImpl newMol = new CmpdRegMoleculeRDKitImpl(newStructure, bbChemStructureService);
		newMol.molecule.setProperties(this.molecule.getProperties());
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
		JsonNode jsonNode = bbChemStructureService.getPreprocessorSettings();

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
		RWMol mol = bbChemStructureService.getPartiallySanitizedRWMol(this.molecule.getMol());
		mol.Kekulize();
		this.molecule.setMol(bbChemStructureService.getMolStructureFromRDKMol(mol));
	}

}
