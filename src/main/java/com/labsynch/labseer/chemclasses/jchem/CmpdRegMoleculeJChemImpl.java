package com.labsynch.labseer.chemclasses.jchem;

import java.io.IOException;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import chemaxon.formats.MolExporter;
import chemaxon.formats.MolFormatException;
import chemaxon.struc.MProp;
import chemaxon.struc.Molecule;
import chemaxon.util.MolHandler;

@Configurable
public class CmpdRegMoleculeJChemImpl implements CmpdRegMolecule {
	
	Logger logger = LoggerFactory.getLogger(CmpdRegMoleculeJChemImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	Molecule molecule;
	
	public CmpdRegMoleculeJChemImpl(String molStructure) throws MolFormatException{
		MolHandler mh = null;
		boolean badStructureFlag = false;
		Molecule mol = null;
		String lineEnd = System.getProperty("line.separator");
		try {
			mh = new MolHandler(molStructure);
			mol = mh.getMolecule();		
		} catch (MolFormatException e1) {
			logger.debug("failed first attempt: bad mol structure: " + molStructure);
			// clean up the molString and try again
			try {
				molStructure = new StringBuilder().append(lineEnd).append(molStructure).append(lineEnd).toString();
				mh = new MolHandler(molStructure);
				mol = mh.getMolecule();
			} catch (MolFormatException e2) {
				logger.debug("failed second attempt: bad mol structure: " + molStructure);
				badStructureFlag = true;
				logger.error("bad mol structure: " + molStructure);
			}
		}	

		if (!badStructureFlag){
			this.molecule = mol;
		} else {
			this.molecule = null;
		}
	}
	
	public CmpdRegMoleculeJChemImpl(Molecule mol){
		this.molecule = mol;
	}
	
	@Override
	public String getProperty(String key){
		return molecule.getProperty(key);
	}
	
	@Override
	public void setProperty(String key, String value){
		this.molecule.setProperty(key, value);
	}; 
	
	@Override
	public String[] getPropertyKeys(){
		return this.molecule.properties().getKeys();
	};

	@Override
	public String getPropertyType(String key){
		MProp prop = molecule.properties().get(key);
		return prop.getPropType();
	}

	@Override
	public String getMolStructure() {
		return this.molecule.toFormat("mol");
	}
	
	@Override
	public String getSmiles() {
		return this.molecule.toFormat("smiles");
	}
	
	@Override
	public String getMrvStructure() {
		return this.molecule.toFormat("mrv");
	};

	@Override
	public String getFormula() {
		return this.molecule.getFormula();
	}

	@Override
	public Double getExactMass() {
		return this.molecule.getExactMass();
	}

	@Override
	public Double getMass() {
		return this.molecule.getMass();
	}

	@Override
	public int getTotalCharge() {
		return this.molecule.getTotalCharge();
	}

	@Override
	public CmpdRegMolecule replaceStructure(String newStructure) throws CmpdRegMolFormatException {
		Molecule replacementMol;
		try {
			replacementMol = new CmpdRegMoleculeJChemImpl(newStructure).molecule;
		} catch (MolFormatException e) {
			throw new CmpdRegMolFormatException(e);
		}
		Molecule newMol = this.molecule.clone();
		newMol.removeAll();
		newMol.fuse(replacementMol);
		CmpdRegMoleculeJChemImpl newMolecule = new CmpdRegMoleculeJChemImpl(newMol);
		return newMolecule;
	}

	@Override
	public byte[] toBinary(CmpdRegMolecule molecule, String imageFormat, String hSize, String wSize) throws IOException {
		// If preprocessor settings are not null then use the preprocessor settings
		if(propertiesUtilService.getPreprocessorSettings() != null) {
			// Read the preprocessor settings as json
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(propertiesUtilService.getPreprocessorSettings());
			
			// Extract the url to call
			JsonNode urlNode = jsonNode.get("imageURL");
			if(urlNode == null || urlNode.isNull()) {
				logger.error("Missing preprocessorSettings imageURL!!");
			}
			String url = urlNode.asText();

			// Create the request json
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode requestData = mapper.createObjectNode();
			Molecule jchemMol = ((CmpdRegMoleculeJChemImpl) molecule).molecule;
			String mol = MolExporter.exportToFormat(jchemMol, "mol");
			requestData.put("molv3", mol);
			requestData.put("format", imageFormat);
			ObjectNode options = mapper.createObjectNode();
			options.put("width", wSize);
			options.put("height", hSize);
			requestData.put("options", options);
			String request = requestData.toString();
			logger.info("Image request"+ request);

			//Return the response bytes
			return SimpleUtil.postRequestToExternalServerBinaryResponse(url, request, logger);
		} else {
			Molecule mol = ((CmpdRegMoleculeJChemImpl) molecule).molecule;
			String format = imageFormat + ":" + "h" + hSize + ",w" + wSize + ",maxScale28";
			return MolExporter.exportToBinFormat(mol, format);
		}
	}

	@Override
	public void dearomatize() {
		this.molecule.dearomatize();	
	}

}
