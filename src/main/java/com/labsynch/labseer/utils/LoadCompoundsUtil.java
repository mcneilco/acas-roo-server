package com.labsynch.labseer.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReader;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReaderFactory;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.labseer.domain.CorpName;
import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Isotope;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.StereoCategory;
import com.labsynch.labseer.dto.Metalot;
import com.labsynch.labseer.dto.MetalotReturn;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ErrorMessage;
import com.labsynch.labseer.service.MetalotService;

@Service
@Transactional
public class LoadCompoundsUtil {

	static Logger logger = LoggerFactory.getLogger(LoadCompoundsUtil.class);

	@Autowired
	private MetalotService metalotServ;
	
	@Autowired
	private CmpdRegSDFReaderFactory sdfReaderFactory;
	
	@Autowired
	private CmpdRegSDFWriterFactory sdfWriterFactory;

    @Transactional
	public void loadCompounds(String inputFileName, String outputFileName){
		//simple utility to load compounds

		try {
			CmpdRegSDFReader mi = sdfReaderFactory.getCmpdRegSDFReader(inputFileName);
			CmpdRegSDFWriter me = sdfWriterFactory.getCmpdRegSDFWriter(outputFileName);
			CmpdRegMolecule mol = null;

			while ((mol = mi.readNextMol()) != null) {

				boolean goodMolToProcess = true;
				long cmpdCorpId = 0;

				try {
					cmpdCorpId = Long.parseLong(MoleculeUtil.getMolProperty(mol, "CMPD"));
				} catch (Exception e) {
					logger.error("unable to parse the CMPD field. " + MoleculeUtil.getMolProperty(mol, "CMPD"));
					mol.setProperty("error", "unable to parse CMPD field. Not a number ");
					goodMolToProcess = false;
				}


				Metalot metaLot = new Metalot();
//				mol.clearExtraLabels();

				Date lotSynthesisDate = null;
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				if (MoleculeUtil.validateMolProperty(mol, "Date")){
					try {
						lotSynthesisDate = df.parse(MoleculeUtil.getMolProperty(mol, "Date"));
						Date bottomDate = df.parse("01/01/1920");
						Date topDate = df.parse("12/31/2020");
						if (lotSynthesisDate.before(bottomDate) || lotSynthesisDate.after(topDate)){
							logger.error("Bad date: = " + MoleculeUtil.getMolProperty(mol, "Date"));
							mol.setProperty("error", "Bad date field.  " + MoleculeUtil.getMolProperty(mol, "Date"));
							goodMolToProcess = false;
						}
						
					} catch (ParseException e){
						logger.error("error parsing the date. Date field = " + MoleculeUtil.getMolProperty(mol, "Date"));
						mol.setProperty("error", "unable to parse date field.  " + MoleculeUtil.getMolProperty(mol, "Date"));
						goodMolToProcess = false;
					}
				}

				//look for author. Create a new one if absent.
				Author chemist = null;
				if (MoleculeUtil.validateMolProperty(mol, "Registered By")){
					String chemistCodeName = MoleculeUtil.getMolProperty(mol, "Registered By");
					logger.debug("query chemist = " + chemistCodeName);
					try{
						chemist = Author.findAuthorsByUserName(chemistCodeName).getSingleResult();
					} catch (EmptyResultDataAccessException e){
						if (chemistCodeName.trim().equalsIgnoreCase("")){
							//default set Russell as the chemist
							chemistCodeName = "rkey";
							chemist = Author.findAuthorsByUserName(chemistCodeName).getSingleResult();
						} else {
							logger.debug("create the new chemist" + chemistCodeName);
							chemist = new Author();
							chemist.setUserName(chemistCodeName);
							chemist.setFirstName(chemistCodeName);
							chemist.persist();							
						}
					}
					logger.debug("chemist is: " + chemist.toJson());
				} else {
					//default set Russell as the chemist
					String chemistCodeName = "rkey";
					chemist = Author.findAuthorsByUserName(chemistCodeName).getSingleResult();
				}

				String parentCorpName = CorpName.formatCorpName(cmpdCorpId);
				Parent parent = null;
				try{
					parent = Parent.findParentsByCorpNameEquals(parentCorpName).getSingleResult();
				} catch (EmptyResultDataAccessException e){
					logger.debug("create the new parent");
					parent = new Parent();
					parent.setMolStructure(MoleculeUtil.exportMolAsText(mol, "mol"));
					parent.setCorpName(parentCorpName);
					parent.setParentNumber(cmpdCorpId);
					parent.setChemist(chemist.getUserName());
					parent.setRegistrationDate(lotSynthesisDate);

					StereoCategory stereoCategory = null;
					if (MoleculeUtil.validateMolProperty(mol, "StereoCategory")){
						String stereoCategoryCode = MoleculeUtil.getMolProperty(mol, "StereoCategory");
						try{
							stereoCategory = StereoCategory.findStereoCategorysByCodeEquals(stereoCategoryCode).getSingleResult();			    		
						} catch (EmptyResultDataAccessException e2){
							logger.error("Did not find the stereoCategory code" + stereoCategoryCode);
							stereoCategory = StereoCategory.findStereoCategorysByCodeEquals("unknown").getSingleResult();			    		
						}
					} else {
						stereoCategory = StereoCategory.findStereoCategorysByCodeEquals("unknown").getSingleResult();			    		
					}
					logger.debug("stereoCategoryCode: " + stereoCategory.getCode());
					parent.setStereoCategory(stereoCategory);
				}
				logger.debug("parent: " + parent.toJson());

				Set<IsoSalt> isoSalts = new HashSet<IsoSalt>();
				boolean saltIsotopeError = false;
				
				double saltCount = 0d;
				try {
					saltCount = Double.parseDouble(MoleculeUtil.getMolProperty(mol, "Count"));
				} catch (Exception e) {
					saltCount = 0d;
					logger.error("unable to parse the saltCount field. " + MoleculeUtil.getMolProperty(mol, "Count"));
					mol.setProperty("error", "unable to parse saltCount field. Not a number or missing. ");
					//goodMolToProcess = false;
					me.writeMol(mol);
				}
				
				//search for isotope --- not laid out easily	
				Isotope isotope = null;
				if (MoleculeUtil.validateMolProperty(mol, "Salt")){
					String isotopeAbbrev = MoleculeUtil.getMolProperty(mol, "Salt");
					try{
						isotope = Isotope.findIsotopesByAbbrevEquals(isotopeAbbrev).getSingleResult();
						logger.debug("found an isotope: " + isotopeAbbrev);
						//now cheat to use the code below
						saltCount = 1;
						mol.setProperty("RemovedSalts", isotopeAbbrev);
					} catch (EmptyResultDataAccessException e){
						logger.debug("Did not find the isotope code " + isotopeAbbrev );
					}
				}	
				if (saltCount > 0){
					IsoSalt isoSalt = new IsoSalt();
					Salt salt = null;
					if (MoleculeUtil.validateMolProperty(mol, "RemovedSalts")){
						String saltAbbrev = MoleculeUtil.getMolProperty(mol, "RemovedSalts");
						if (!saltAbbrev.equalsIgnoreCase("")){
							logger.debug("Query Salt = " + saltAbbrev);
							try{
								salt = Salt.findSaltsByAbbrevEquals(saltAbbrev).getSingleResult();
								logger.debug("found a salt: " + salt.toJson());
								isoSalt.setType("salt");
								isoSalt.setSalt(salt);
								isoSalt.setEquivalents(saltCount);
							} catch (EmptyResultDataAccessException e2){
								logger.debug("Did not find the salt code " + saltAbbrev + "  Check if it is an isotope.");
								//							    	check that it is not an isotope
								try {
									isotope = Isotope.findIsotopesByAbbrevEquals(saltAbbrev).getSingleResult();
									isoSalt.setType("isotope");
									isoSalt.setIsotope(isotope);
									isoSalt.setEquivalents(saltCount);
								}  catch (EmptyResultDataAccessException e3){
									logger.error("Not an isotope either!!. Did not find the isotope code " + saltAbbrev);
									saltIsotopeError = true;
									goodMolToProcess = false;
									mol.setProperty("error", "unknown salt: compound with new salt or isotope ");
								}
							}
						}

						if (!saltIsotopeError){
							isoSalts.add(isoSalt);				    		
						}	
					}
				}
				String saltFormCorpName = CorpName.generateSaltFormCorpName(parent.getCorpName(), isoSalts);
				SaltForm saltForm = null;
				try{
					saltForm = SaltForm.findSaltFormsByCorpNameEquals(saltFormCorpName).getSingleResult();	    		
				} catch (EmptyResultDataAccessException e){
					logger.debug("new salForm: " + saltFormCorpName);
					saltForm = new SaltForm();
					saltForm.setParent(parent);
					saltForm.setChemist(chemist.getUserName());
					saltForm.setMolStructure("");
					saltForm.setCorpName(saltFormCorpName);
					saltForm.setRegistrationDate(lotSynthesisDate);
				}
				for (IsoSalt isoSalt : isoSalts){
					isoSalt.setSaltForm(saltForm);
					logger.debug("isoSalt: " + isoSalt.toJson());
				}


				int lotNumber = 0;
				if (MoleculeUtil.validateMolProperty(mol, "Lot")){
					try {
						lotNumber = Integer.valueOf(MoleculeUtil.getMolProperty(mol, "Lot"));						
					} catch (Exception e){
						logger.error("unable to convert the lot number. " + MoleculeUtil.getMolProperty(mol, "Lot"));
						mol.setProperty("error", "bad lot number: compound without a lot number ");
						goodMolToProcess = false;
					}
				}
				String lotCorpName = saltForm.getCorpName().concat(CorpName.separator).concat(String.format("%02d", lotNumber));


				Lot lot = null;

				try{
					lot = Lot.findLotsByCorpNameEquals(lotCorpName).getSingleResult();	    		
				} catch (EmptyResultDataAccessException e){
					logger.debug("new lot created: " + lotCorpName);
					lot = new Lot();
					lot.setCorpName(lotCorpName);
					lot.setLotNumber(lotNumber);
				} catch (IncorrectResultSizeDataAccessException e2){
					logger.error("found multiple lot " + lotCorpName);
					List<Lot> lots = Lot.findLotsByCorpNameEquals(lotCorpName).getResultList();
					if (lots.size() > 1){
						goodMolToProcess = false;
						mol.setProperty("error", "dupelot: found multiple lots with the same name: " + lots.size() );
						lot = lots.get(0);
					}
				}


				if (MoleculeUtil.validateMolProperty(mol, "Date")){
					lot.setSynthesisDate(lotSynthesisDate);	
					lot.setRegistrationDate(lotSynthesisDate);
				}
				lot.setAsDrawnStruct(MoleculeUtil.exportMolAsText(mol, "mol"));
				lot.setChemist(chemist.getUserName());
				lot.setSaltForm(saltForm);


				if (MoleculeUtil.validateMolProperty(mol, "Notebook")){
					lot.setNotebookPage(MoleculeUtil.getMolProperty(mol, "Notebook"));
				}

				String lotComments = "";
				if (MoleculeUtil.validateMolProperty(mol, "Reviewed By")){
					String reviewNote = MoleculeUtil.getMolProperty(mol, "Reviewed By");
					lotComments = lotComments.concat("Reviewed by: " + reviewNote);

				}

				if (MoleculeUtil.validateMolProperty(mol, "Synonyms_notes")){
					String synonymNote = MoleculeUtil.getMolProperty(mol, "Synonyms_notes");
					lotComments = lotComments.concat(" Note: " + synonymNote);
				}
				lot.setComments(lotComments);	

				//check for existing lot. Will simply update if already registered?

				logger.debug("New lot corpname: " + lotCorpName);
				lot.setCorpName(lotCorpName);
				if (lot.getLotNumber() == 0){
					lot.setIsVirtual(true);
				} else {
					lot.setIsVirtual(false);
				}

				metaLot.setLot(lot);
				metaLot.setIsosalts(isoSalts);
				metaLot.setSkipParentDupeCheck(true);
				logger.debug(metaLot.toJson());

				if (goodMolToProcess){
					MetalotReturn results = metalotServ.save(metaLot);
					logger.debug("lot saved: " + results.toJson());
					if(results.getErrors().size() > 0){
						logger.error("Error while saving the metaLot." );
						ArrayList<ErrorMessage> errors = results.getErrors();
						String errorMessage = "";
						for (ErrorMessage error : errors){
							errorMessage = errorMessage.concat(". ").concat(error.getMessage());
						}
						mol.setProperty("metalot-error", errorMessage);
						me.writeMol(mol);
					}
				} else {
					logger.error("Unable to process the bad mol. " );
					me.writeMol(mol);
				}


			}
			mi.close();
			me.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmpdRegMolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
