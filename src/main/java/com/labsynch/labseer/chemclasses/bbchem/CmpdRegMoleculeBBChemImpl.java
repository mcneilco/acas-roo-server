package com.labsynch.labseer.chemclasses.bbchem;

import java.io.IOException;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.utils.SimpleUtil;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

public class CmpdRegMoleculeBBChemImpl implements CmpdRegMolecule {

	Logger logger = LoggerFactory.getLogger(CmpdRegMoleculeBBChemImpl.class);

	BBChemParentStructure molecule;

	private BBChemStructureService bbChemStructureService;

	public CmpdRegMoleculeBBChemImpl(BBChemParentStructure molecule, BBChemStructureService bbChemStructureService) {
		this.molecule = molecule;
		this.bbChemStructureService = bbChemStructureService;
	}

	public CmpdRegMoleculeBBChemImpl(String singleSDF, BBChemStructureService bbChemStructureService) throws CmpdRegMolFormatException {
		this.bbChemStructureService = bbChemStructureService;
		this.molecule = bbChemStructureService.parseSDF(singleSDF).get(0);
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
		if(this.getRegistrationStatus() == RegistrationStatus.ERROR) { 
			return null; 
		} else if(this.molecule.getMolecularFormula() != null) {
			return this.molecule.getMolecularFormula();
		} else {
			try {
				this.molecule = bbChemStructureService.getProcessedStructure(this.molecule.getMol(), false);
				return this.molecule.getMolecularFormula();
			} catch (CmpdRegMolFormatException e) {
				logger.error(e.getMessage());
				return null;
			}
		}
	}

	@Override
	public Double getExactMass() {
		if(this.getRegistrationStatus() == RegistrationStatus.ERROR) { 
			return null; 
		} else if(this.molecule.getExactMolWeight() != null) {
			return this.molecule.getExactMolWeight();
		} else {
			try {
				this.molecule = bbChemStructureService.getProcessedStructure(this.molecule.getMol(), false);
				return this.molecule.getExactMolWeight();
			} catch (CmpdRegMolFormatException e) {
				logger.error(e.getMessage());
				return null;
			}
		}	
	}

	@Override
	public Double getMass() {
		if(this.getRegistrationStatus() == RegistrationStatus.ERROR) { 
			return null; 
		} else if(this.molecule.getAverageMolWeight() != null) {
			return this.molecule.getAverageMolWeight();
		} else {
			try {
				this.molecule = bbChemStructureService.getProcessedStructure(this.molecule.getMol(), false);
				return this.molecule.getAverageMolWeight();
			} catch (CmpdRegMolFormatException e) {
				logger.error(e.getMessage());
				return null;
			}
		}
	}

	@Override
	public int getTotalCharge() {
		if(this.getRegistrationStatus() == RegistrationStatus.ERROR) { 
			return -1; 
		} else if(this.molecule.getTotalCharge() != null) {
			return this.molecule.getTotalCharge();
		} else {
			try {
				this.molecule = bbChemStructureService.getProcessedStructure(this.molecule.getMol(), false);
				return this.molecule.getTotalCharge();
			} catch (CmpdRegMolFormatException e) {
				logger.error(e.getMessage());
				return -1;
			}
		}
	}

	@Override
	public String getSmiles() {
		if(this.getRegistrationStatus() == RegistrationStatus.ERROR) { 
			return null; 
		} else if(this.molecule.getSmiles() != null) {
			return this.molecule.getSmiles();
		} else {
			try {
				this.molecule = bbChemStructureService.getProcessedStructure(this.molecule.getMol(), false);
				return this.molecule.getSmiles();
			} catch (CmpdRegMolFormatException e) {
				logger.error(e.getMessage());
				return null;
			}
		}
	}

	@Override
	public CmpdRegMolecule replaceStructure(String newStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeBBChemImpl newMol = new CmpdRegMoleculeBBChemImpl(newStructure, bbChemStructureService);
		newMol.molecule.setProperties(this.molecule.getProperties());
		return newMol;
	}

	@Override
	public String getMrvStructure() {
		// not implemented in BBChem
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
		ObjectNode draw_options = mapper.createObjectNode();
		draw_options.put("width", wSize);
		draw_options.put("height", hSize);
		requestData.put("draw_options", draw_options);
		String request = requestData.toString();
		logger.info("Image request" + request);

		// Return the response bytes
		return SimpleUtil.postRequestToExternalServerBinaryResponse(url, request, logger);

	}

	@Override
	public void dearomatize() {
		// BBCHem implementation always kekulizes the structure
	}

	public BBChemParentStructure getMolecule() {
		return this.molecule;
	}

	@Override
	public StandardizationStatus getStandardizationStatus() {
		return this.molecule.getStandardizationStatus();
	}

	@Override
	public String getStandardizationComment() {
		return this.molecule.getStandardizationComment();
	}

	@Override
	public RegistrationStatus getRegistrationStatus() {
		return this.molecule.getRegistrationStatus();

	}

	@Override
	public String getRegistrationComment() {
		return this.molecule.getRegistrationComment();

	}

}
