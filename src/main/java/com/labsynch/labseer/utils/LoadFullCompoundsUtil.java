package com.labsynch.labseer.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.NoResultException;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReader;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReaderFactory;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.domain.StereoCategory;
import com.labsynch.labseer.domain.Vendor;
import com.labsynch.labseer.dto.Metalot;
import com.labsynch.labseer.dto.MetalotReturn;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.service.ErrorMessage;
import com.labsynch.labseer.service.MetalotService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoadFullCompoundsUtil {

	static Logger logger = LoggerFactory.getLogger(LoadFullCompoundsUtil.class);

	@Autowired
	private MetalotService metalotServ;

	@Autowired
	private ChemStructureService chemService;

	@Autowired
	private CmpdRegSDFReaderFactory sdfReaderFactory;

	@Autowired
	private CmpdRegSDFWriterFactory sdfWriterFactory;

	public void loadCompounds(String inputFileName, String outputFileName, HashMap<String, String> propertiesMap) {
		// simple utility to load compounds

		// I want to set up a hash map of properties
		// ALIAS --> parent.commonName (string 1000)
		// VENDORCATNO --> lot.supplierID (String 255)
		// PROJECT --> lot.project (Project --> findByCode)

		// vendor --> lot.supplier (String 255)
		// chemist --> lot.chemist (Author --> findByUserName)

		try {
			CmpdRegSDFReader mi = sdfReaderFactory.getCmpdRegSDFReader(inputFileName);
			CmpdRegMolecule mol = null;

			int molCounter = 0;
			// && molCounter < 5
			while ((mol = mi.readNextMol()) != null) {
				mol.dearomatize();
				logger.debug("current molCounter: " + molCounter);
				molCounter++;
				if (molCounter > 0) {
					loadCompoundsMol(mol, outputFileName, propertiesMap);
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmpdRegMolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Transactional
	private void loadCompoundsMol(CmpdRegMolecule mol, String outputFileName, HashMap<String, String> propertiesMap)
			throws IllegalArgumentException, IOException, CmpdRegMolFormatException {
		// simple utility to load compound mol
		CmpdRegSDFWriter me = sdfWriterFactory.getCmpdRegSDFWriter(outputFileName);

		boolean goodMolToProcess = true;
		// mol.clearExtraLabels();

		// look for cmpd scientist. Create a new one if absent.
		String chemistCodeName = null;
		Author cmpdChemist = null;
		if (MoleculeUtil.validateMolProperty(mol, propertiesMap.get("compound_chemist"))) {
			chemistCodeName = MoleculeUtil.getMolProperty(mol, propertiesMap.get("compound_chemist")).toLowerCase();
			logger.debug("query chemist = " + chemistCodeName);
			logger.debug("chemist is: " + cmpdChemist.toJson());
		} else if (propertiesMap.get("compound_chemist_code") != null) {
			// get chemist codename from the map
			chemistCodeName = propertiesMap.get("compound_chemist_code");
		} else {
			chemistCodeName = "cchemist"; // defaul value
		}
		try {
			cmpdChemist = Author.findAuthorsByUserName(chemistCodeName).getSingleResult();
		} catch (NoResultException e) {
			if (chemistCodeName.trim().equalsIgnoreCase("")) {
				// default set cchemist as the chemist
				chemistCodeName = "cchemist";
				cmpdChemist = Author.findAuthorsByUserName(chemistCodeName).getSingleResult();
			} else {
				logger.debug("create the new chemist" + chemistCodeName);
				cmpdChemist = new Author();
				cmpdChemist.setUserName(chemistCodeName);
				cmpdChemist.setFirstName(chemistCodeName);
				cmpdChemist.persist();
			}
		}

		Author lotChemist = cmpdChemist;

		Metalot metaLot = new Metalot();
		Lot lot = new Lot();
		SaltForm saltForm = new SaltForm();
		Parent parent = new Parent();

		lot.setAsDrawnStruct(mol.getMolStructure());

		// mol.clearExtraLabels();
		parent.setMolStructure(mol.getMolStructure());
		parent.setChemist(cmpdChemist.getUserName());

		if (MoleculeUtil.validateMolProperty(mol, propertiesMap.get("compound_alias"))) {
			String cmpdAlias = MoleculeUtil.getMolProperty(mol, propertiesMap.get("compound_alias"));
			parent.setCommonName(cmpdAlias);
		}

		String stereoCategoryCode = null;
		if (propertiesMap.get("stereo_category") == null) {
			stereoCategoryCode = "unknown";
		} else {
			stereoCategoryCode = propertiesMap.get("stereo_category");
		}
		try {
			StereoCategory stereoCategory = StereoCategory.findStereoCategorysByCodeEquals(stereoCategoryCode)
					.getSingleResult();
			logger.info("found the following stereo category  " + stereoCategory.toJson());
			parent.setStereoCategory(stereoCategory);
		} catch (NoResultException e) {
			logger.error("Did not find the query stereoCategoryCode: " + stereoCategoryCode);
		}

		saltForm.setParent(parent);
		saltForm.setChemist(cmpdChemist.getUserName());
		saltForm.setMolStructure("");

		lot.setSynthesisDate(new Date());
		lot.setChemist(lotChemist.getUserName());
		lot.setSaltForm(saltForm);
		lot.setLotMolWeight(mol.getMass(false));
		lot.setIsVirtual(false);

		String supplierCode;
		if (MoleculeUtil.validateMolProperty(mol, propertiesMap.get("supplier"))) {
			supplierCode = MoleculeUtil.getMolProperty(mol, propertiesMap.get("supplier"));
		} else {
			supplierCode = propertiesMap.get("supplier");
		}
		if (supplierCode != null) {
			try {
				Vendor vendor = Vendor.findVendorsByCodeEquals(supplierCode).getSingleResult();
				lot.setVendor(vendor);
			} catch (NoResultException e) {
				logger.error("Did not find the query supplierCode: " + supplierCode);
			}
		}

		if (MoleculeUtil.validateMolProperty(mol, propertiesMap.get("notebook"))) {
			String notebook = MoleculeUtil.getMolProperty(mol, propertiesMap.get("notebook"));
			lot.setNotebookPage(notebook);
		}

		if (MoleculeUtil.validateMolProperty(mol, propertiesMap.get("supplier_id"))) {
			String supplierId = MoleculeUtil.getMolProperty(mol, propertiesMap.get("supplier_id"));
			lot.setSupplierID(supplierId);
		}

		metaLot.setLot(lot);

		if (goodMolToProcess) {
			logger.debug("attempting to save the good mol");
			MetalotReturn results = metalotServ.save(metaLot);
			logger.debug("lot saved: " + results.toJson());
			if (results.getErrors().size() > 0) {
				logger.error("Error while saving the metaLot.");
				ArrayList<ErrorMessage> errors = results.getErrors();
				String errorMessage = "";
				boolean firstError = true;
				for (ErrorMessage error : errors) {
					if (firstError) {
						errorMessage = error.getMessage();
						firstError = false;
					} else {
						errorMessage = errorMessage.concat(". ").concat(error.getMessage());
					}
				}
				mol.setProperty("metalot-error", errorMessage);
				me.writeMol(mol);
			}
		} else {
			logger.error("Unable to process the bad mol. ");
			me.writeMol(mol);
		}

		me.close();
	}

}