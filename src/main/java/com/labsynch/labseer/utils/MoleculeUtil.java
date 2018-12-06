package com.labsynch.labseer.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

@Component
public class MoleculeUtil {

	static Logger logger = LoggerFactory.getLogger(MoleculeUtil.class);
	
	private static CmpdRegMoleculeFactory cmpdRegMoleculeFactory;
	
	@Autowired
	public MoleculeUtil(CmpdRegMoleculeFactory cmpdRegMoleculeFactory) {
		MoleculeUtil.cmpdRegMoleculeFactory = cmpdRegMoleculeFactory;
	}

	public static boolean validateMolProperty(CmpdRegMolecule mol, String propName){
		
		String jchemVersion = Configuration.getConfigInfo().getServerSettings().getJchemVersion();
		
		// note: new method introduced in 5.7 - deprecates the method below
		// (MPropHandler.convertToString(mol.properties(), "date_submitted")
		boolean validProperty = false;
		String molProperty = null;

		if (propName != null && mol.getProperty(propName) != null){
			molProperty = mol.getProperty(propName).trim();
			if (!molProperty.equalsIgnoreCase("")){
				validProperty = true;
			}
		} 
		
		return validProperty;
	}
	
	public static String getMolProperty(CmpdRegMolecule mol, String propName){
		// note: new method introduced in 5.7 - deprecates the method below
		// (MPropHandler.convertToString(mol.properties(), "date_submitted")
//		MPropHandler.convertToString(mol.properties(), propName.trim());			
		
		String molProperty = null;
		if (mol.getProperty(propName) != null){
			molProperty = mol.getProperty(propName).trim();
		} else {
			logger.error("the requested property is null: " + propName);
		}
		
		return molProperty;
	}

	public static CmpdRegMolecule setMolProperty(CmpdRegMolecule mol, String key, String value){
		mol.setProperty(key, value);
		return mol;
	}
	

	public static byte[] exportMolAsBin(CmpdRegMolecule mol, String imageFormat, String hSize, String wSize) throws IOException{
		return mol.toBinary(mol, imageFormat, hSize, wSize);
	}

	public static String exportMolAsText(CmpdRegMolecule mol, String exportFormat) throws IOException, CmpdRegMolFormatException{
		if (exportFormat.equalsIgnoreCase("smiles")){
			return mol.getSmiles();
		}else if (exportFormat.equalsIgnoreCase("mrv")) {
			return mol.getMrvStructure();
		}else {
			return mol.getMolStructure();
		}

	}
	
	public static String getMolFormula(String molStructure) throws CmpdRegMolFormatException {
		CmpdRegMolecule mol = cmpdRegMoleculeFactory.getCmpdRegMolecule(molStructure);
		return mol.getFormula();
	}
	
}