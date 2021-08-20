package com.labsynch.labseer.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.CorpName;
import com.labsynch.labseer.domain.FileList;
import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.LotAlias;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.domain.PreDef_CorpName;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.CorpNameDTO;
import com.labsynch.labseer.dto.LabelPrefixDTO;
import com.labsynch.labseer.dto.CreatePlateRequestDTO;
import com.labsynch.labseer.dto.Metalot;
import com.labsynch.labseer.dto.MetalotReturn;
import com.labsynch.labseer.dto.SetTubeLocationDTO;
import com.labsynch.labseer.dto.WellContentDTO;

import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.DupeParentException;
import com.labsynch.labseer.exceptions.DupeSaltFormCorpNameException;
import com.labsynch.labseer.exceptions.DupeSaltFormStructureException;
import com.labsynch.labseer.exceptions.JsonParseException;
import com.labsynch.labseer.exceptions.SaltFormMolFormatException;
import com.labsynch.labseer.exceptions.SaltedCompoundException;
import com.labsynch.labseer.exceptions.UniqueNotebookException;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;


@Service
public class MetalotServiceImpl implements MetalotService {

	@Autowired
	private ChemStructureService chemService;

	@Autowired
	private LotService lotService;

	@Autowired
	private CorpNameService corpNameService;

	@Autowired
	private ParentStructureServiceImpl parentStructureServiceImpl;

	@Autowired
	private ParentAliasService parentAliasService;

	@Autowired
	private SaltFormService saltFormService;

	@Autowired
	private IsoSaltService isoSaltService;

	@Autowired
	private LotAliasService lotAliasService;


	@Autowired
	private PropertiesUtilService propertiesUtilService;

	private static final Logger logger = LoggerFactory.getLogger(MetalotServiceImpl.class);

	//	@Transactional
	@Override
	public MetalotReturn save(Metalot metaLot){

		MetalotReturn mr = new MetalotReturn();
		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		try {
			mr = this.processAndSave(metaLot, mr, errors);

		} catch (CmpdRegMolFormatException e) {
			ErrorMessage parentError = new ErrorMessage();
			parentError.setLevel("error");
			parentError.setMessage("Bad molformat. Please fix the molfile: ");
			errors.add(parentError);			
		} catch (UniqueNotebookException e) {
			ErrorMessage lotError = new ErrorMessage();
			lotError.setLevel("error");
			lotError.setMessage("lot notebook is not unique");
			errors.add(lotError);			
		} catch (DupeParentException e) {
			ErrorMessage parentError = new ErrorMessage();
			parentError.setLevel("error");
			parentError.setMessage("Parent with matching structure and stereo category exists.");
			errors.add(parentError);
		}  catch (SaltedCompoundException e) {
			ErrorMessage parentError = new ErrorMessage();
			parentError.setLevel("error");
			parentError.setMessage("Parent appears to be salted.");
			errors.add(parentError);
		}  catch (JsonParseException e) {
			ErrorMessage parentError = new ErrorMessage();
			parentError.setLevel("error");
			parentError.setMessage("Unable to parse the parent JSON structure.");
			errors.add(parentError);
		} catch (DupeSaltFormCorpNameException e) {
			ErrorMessage saltFormError = new ErrorMessage();
			saltFormError.setLevel("warning");
			saltFormError.setMessage("Duplicate saltForm found by corpName. Please select existing saltForm.");
			errors.add(saltFormError);	
		} catch (SaltFormMolFormatException e) {
			ErrorMessage saltFormError = new ErrorMessage();
			saltFormError.setLevel("error");
			saltFormError.setMessage("Bad molformat. Please fix the saltForm molfile: ");
			logger.error(saltFormError.getMessage());
			errors.add(saltFormError);
		} catch (NoResultException e) {
			ErrorMessage error = new ErrorMessage();
			error.setLevel("error");
			error.setMessage("Could not find a definition container to create vials with. Please see your system administrator.");
			logger.error(error.getMessage());
			errors.add(error);
		} catch (NonUniqueResultException e) {
			ErrorMessage saltFormError = new ErrorMessage();
			saltFormError.setLevel("error");
			saltFormError.setMessage("Barcode already exists as a vial.");
			logger.error(saltFormError.getMessage());
			errors.add(saltFormError);
		}catch (Exception e) {
			ErrorMessage genericError = new ErrorMessage();
			genericError.setLevel("error");
			genericError.setMessage("Internal error encountered. Please contact your administrator.");
			logger.error("Uncaught error in metalot save.",e);
			errors.add(genericError);
		}

		mr.setErrors(errors);
		return mr;

	}

	@Transactional
	public MetalotReturn processAndSave(Metalot metaLot, MetalotReturn mr, ArrayList<ErrorMessage> errors) 
			throws UniqueNotebookException, DupeParentException, JsonParseException, 
			DupeSaltFormCorpNameException, DupeSaltFormStructureException, SaltFormMolFormatException, 
			SaltedCompoundException, IOException, CmpdRegMolFormatException {

		logger.info("attempting to save the metaLot. ");

		//		Metalot metaLot = null;		
		boolean dupeParent = false;
		boolean metalotError = false;

		//		try {
		//			metaLot = Metalot.fromJsonToMetalot(metaLotJson);				
		//		} catch (Exception e){
		//			logger.error("Unable to parse metaLot JSON. " + e);
		//			throw new JsonParseException("Unable to parse metaLot JSON");
		//		}
		//		
		Lot lot = metaLot.getLot();
		if (logger.isDebugEnabled()) logger.debug("attempting to save the lot. " + lot.toJson());
		logger.debug("is it a virtual lot: " + lot.getIsVirtual());



		if (lot.getId() == null ){
			if (propertiesUtilService.getUniqueNotebook() && lot.getNotebookPage() != null){
				boolean duplicateNotebook = false;
				try {
					duplicateNotebook = checkUniqueNotebook(lot);
				} catch (IllegalArgumentException e){
					throw new UniqueNotebookException("notebook page is required");
				}
				logger.info("unique Notebook. " + lot.getNotebookPage() + "  "+ duplicateNotebook);
				if (!duplicateNotebook){
					metalotError = true;
					throw new UniqueNotebookException("lot notebook is not unique");
				}
			}
		}


		Parent parent = metaLot.getLot().getSaltForm().getParent();
		if (parent == null) {
			parent = metaLot.getLot().getParent();
		}
		if (logger.isDebugEnabled()) logger.debug("parent: " + parent.toJson());


		Set<ParentAlias> parentAliases = parent.getParentAliases();
		int numberOfParentAliases = parentAliases.size();
		logger.info("number of parent aliases: " + numberOfParentAliases);

		String dupeParentNames = "";
		if (parent.getId() == null){
			logger.debug("this is a new parent");
			String molStructure;
			if (propertiesUtilService.getUseExternalStandardizerConfig()){
				molStructure = chemService.standardizeStructure(parent.getMolStructure());
				parent.setMolStructure(molStructure);
			}
			int dupeParentCount = 0;			
			if (!metaLot.isSkipParentDupeCheck()){
				int[] dupeParentList = {};
				if(propertiesUtilService.getRegisterNoStructureCompoundsAsUniqueParents() && chemService.getMolWeight(parent.getMolStructure()) == 0.0) {
					//if true then we are no checking this one for hits
					logger.warn("mol weight is 0 and registerNoStructureCompoundsAsUniqueParents so not checking for dupe parents by structure but other dupe checking will be done");
				} else {
					dupeParentList = chemService.checkDupeMol(parent.getMolStructure(), "Parent_Structure", "Parent");
				}
				if(dupeParentList.length > 0 && !metaLot.isSkipParentDupeCheck()){
					for (int parentCdId : dupeParentList){
						List<Parent> queryParents = Parent.findParentsByCdId(parentCdId).getResultList();
						for (Parent queryParent : queryParents){
							if (parent.getStereoCategory() != null && queryParent.getStereoCategory() != null){
								if (parent.getStereoCategory().getCode().equalsIgnoreCase(queryParent.getStereoCategory().getCode())){
									//parent structure and stereo category matches
									//determine if Stereo Comment is different
									boolean parentHasStereoComment = (parent.getStereoComment() != null && parent.getStereoComment().length() > 0);
									boolean queryParentHasStereoComment = (queryParent.getStereoComment() != null && queryParent.getStereoComment().length() > 0);
									if (!parentHasStereoComment & !queryParentHasStereoComment){
										//both stereo comments are null => dupes
										dupeParentCount++;
										dupeParent = true;
										logger.error("dupe parent is: " + queryParent.getCorpName());
										if (queryParent.getCorpName() != null) dupeParentNames = dupeParentNames.concat(queryParent.getCorpName()).concat(" ");
										if (queryParent.getCommonName() != null) dupeParentNames = dupeParentNames.concat(queryParent.getCommonName().concat(" "));
									}else if (!parentHasStereoComment || !queryParentHasStereoComment){
										//stereo comments are different - one is null and the other isn't - parents are not dupes
									}else if (parent.getStereoComment().equalsIgnoreCase(queryParent.getStereoComment())){
										//both stereo comments non-null
										dupeParentCount++;
										dupeParent = true;
										logger.error("dupe parent is: " + queryParent.getCorpName());
										if (queryParent.getCorpName() != null) dupeParentNames = dupeParentNames.concat(queryParent.getCorpName()).concat(" ");
										if (queryParent.getCommonName() != null) dupeParentNames = dupeParentNames.concat(queryParent.getCommonName().concat(" "));
									}else{
										//stereo category is same but comments are unique, so parents are not dupes
									}
								}
								//else stereo category is different
							} else {
								logger.error("the stereo category code is null -- parent: " + parent.getCorpName());
								logger.error("the stereo category code is null -- queryParent: " + queryParent.getCorpName());
								// assume it is a dupe. (an error case). The stereo category should not be null
								dupeParentCount++;
								dupeParent = true;
							}

						}

					}				
				}				
			}

			int cdId = 0;
			logger.debug("dupe parent count: " + dupeParentCount + "  " + dupeParent);

			if (dupeParent){
				//just stop and return an error
				ErrorMessage dupeParentError = new ErrorMessage();
				dupeParentError.setLevel("error");
				dupeParentError.setMessage("Duplicate parent found. Please register a new lot for " + dupeParentNames );
				logger.error(dupeParentError.getMessage());
				errors.add(dupeParentError);
				metalotError = true;
				throw new DupeParentException("Duplicate parent structure");

			}	else if (chemService.checkForSalt(parent.getMolStructure())){
				//multiple fragments
				if (parent.getIsMixture() != null){
					if (!parent.getIsMixture()){
						ErrorMessage multifragmentError = new ErrorMessage();
						multifragmentError.setLevel("error");
						multifragmentError.setMessage("Multiple fragments found. Please register the neutral base parent " );
						logger.error(multifragmentError.getMessage());
						errors.add(multifragmentError);
						metalotError = true;
						logger.error("found a compound with multiple fragments -- mark as an error.");
						logger.error("Salted molfile: " + parent.getMolStructure());
						throw new SaltedCompoundException("Salted parent structure");
					}else{
						//continue to save - structure is appropriately marked as a mixture
					}
				}else{
					ErrorMessage multifragmentError = new ErrorMessage();
					multifragmentError.setLevel("error");
					multifragmentError.setMessage("Multiple fragments found. Please register the neutral base parent " );
					logger.error(multifragmentError.getMessage());
					errors.add(multifragmentError);
					metalotError = true;
					logger.error("found a compound with multiple fragments -- mark as an error.");
					logger.error("Salted molfile: " + parent.getMolStructure());
					throw new SaltedCompoundException("Salted parent structure");
				}
			}
			if (!dupeParent) {
				boolean checkForDupe = false;
				cdId = chemService.saveStructure(parent.getMolStructure(), "Parent_Structure", checkForDupe);
				if (cdId == -1){
					logger.error("Bad molformat. Please fix the molfile: " + parent.getMolStructure());
					throw new CmpdRegMolFormatException();
				} else {
					parent.setCdId(cdId);
					if (parent.getCorpName() == null || parent.getCorpName().isEmpty() || parent.getCorpName().trim().equalsIgnoreCase("")){
						generateAndSetCorpName(parent);
					} 
					if (parent.getRegistrationDate() == null){
						parent.setRegistrationDate(new Date());				
					}
					DecimalFormat dExactMass = new DecimalFormat("#.######");
					parent.setExactMass(Double.valueOf(dExactMass.format(chemService.getExactMass(parent.getMolStructure()))));
					DecimalFormat dMolWeight = new DecimalFormat("#.###"); 
					parent.setMolWeight(Double.valueOf(dMolWeight.format(chemService.getMolWeight(parent.getMolStructure()))));

					parent.setMolFormula(chemService.getMolFormula(parent.getMolStructure()));
					//add unique constraint to parent corp name as well (parent and lot)
					boolean corpNameAlreadyExists = checkCorpNameAlreadyExists(parent.getCorpName());
					while (corpNameAlreadyExists) {
						generateAndSetCorpName(parent);
						corpNameAlreadyExists = checkCorpNameAlreadyExists(parent.getCorpName());
					}
					//try to set the parentNumber if it is not set already
					if (parent.getParentNumber() < 1){
						parent.setParentNumber(corpNameService.parseParentNumber(parent.getCorpName()));
					}
					logger.debug("Saving new parent with corp name "+parent.getCorpName()+" and parent number "+parent.getParentNumber());
					parent.persist();
				} 
			}

		} else {
			logger.debug("this is an old parent");
			parent = Parent.findParent(parent.getId()); //get the full parent object
			//				parent.merge();
		}

		if (!dupeParent){

			logger.info("not a dupe parent");

			//save parent aliases
			logger.info("--------- Number of parentAliases to save: " + parentAliases.size());
			parent = parentAliasService.updateParentAliases(parent, parentAliases);
			if (logger.isDebugEnabled()) logger.debug("Parent aliases after save: "+ ParentAlias.toJsonArray(parent.getParentAliases()));

			double totalSaltWeight = 0d;

			//save saltForm
			SaltForm saltForm = metaLot.getLot().getSaltForm();
			ErrorMessage saltFormError = new ErrorMessage();
			boolean newSaltForm = false;
			if (saltForm.getId() == null){
				newSaltForm = true;
				int cdId = 0;
				System.out.println("this is a new saltForm");

				String saltFormCorpName = corpNameService.generateSaltFormCorpName(parent.getCorpName(), metaLot.getIsosalts());
				saltForm.setCorpName(saltFormCorpName);
				logger.debug("saltForm corpName:= " + saltForm.getCorpName());
				long saltFormCount = SaltForm.countSaltFormsByCorpNameEquals(saltFormCorpName);
				logger.debug("number of saltForms found by corpName: " + saltFormCount);
				if (saltFormCount > 0){
					if(propertiesUtilService.getSaltBeforeLot()){
						saltFormError.setLevel("warning");
						saltFormError.setMessage("Duplicate saltForm found by corpName. Please select existing saltForm.");
						errors.add(saltFormError);	
						metalotError = true;
						logger.error(saltFormError.getMessage());
						throw new DupeSaltFormCorpNameException("Duplicate saltForm found by corpName.");
					}
				} else {
					if (saltForm.getMolStructure() == null || saltForm.getMolStructure().trim().equalsIgnoreCase("")){
						logger.debug("no salt form structure");

					} else {
						cdId = chemService.saveStructure(saltForm.getMolStructure(), "SaltForm_Structure", true);
						if (cdId == -1){
							saltFormError.setLevel("error");
							saltFormError.setMessage("Bad molformat. Please fix the molfile: " + saltForm.getMolStructure());
							logger.error(saltFormError.getMessage());
							errors.add(saltFormError);
							throw new SaltFormMolFormatException();
						} else if (cdId == 0){
							saltFormError.setLevel("warning");
							saltFormError.setMessage("Duplicate saltForm found. Please select existing saltForm.");
							logger.error(saltFormError.getMessage());
							errors.add(saltFormError);
							metalotError = true;
						}        						
					}					
				}

				if (saltForm.getRegistrationDate() == null){
					saltForm.setRegistrationDate(new Date());				
				}
				if (!metalotError){
					saltForm.setCdId(cdId);
					saltForm.setParent(parent);
					if (logger.isDebugEnabled()) logger.debug("persisted the new saltForm: " + saltForm.toJson());
					saltForm.persist();
					//				saltForm.flush();					
				}

			} else {
				logger.debug("this is an old saltForm");
				//				saltForm.merge();
			}  


			Set<IsoSalt> isoSalts = metaLot.getIsosalts();
			if (!newSaltForm && !metalotError) { 
				//			} else if (isoSalts.size() > 0 && newSaltForm && !metalotError){
				logger.debug("Checking if SaltForm needs to be updated");
				saltForm = saltFormService.updateSaltForm(parent, saltForm, isoSalts, lot, totalSaltWeight, errors);
				lot.setSaltForm(saltForm);
				totalSaltWeight = saltFormService.calculateSaltWeight(saltForm);
				logger.debug("calculate saltWeight: " + totalSaltWeight);
			} else if (newSaltForm && !metalotError){
				logger.debug("saving new set of isoSalts. Number of salts: " + isoSalts.size());
				for (IsoSalt isoSalt : isoSalts ){
					isoSalt.setSaltForm(saltForm);
					isoSalt.persist();
					double saltWeight = isoSaltService.calculateSaltWeight(isoSalt);
					totalSaltWeight = totalSaltWeight + saltWeight;
					logger.debug("current totalSaltWeigth: " + totalSaltWeight);	
				}
				saltForm.setSaltWeight(totalSaltWeight);
				saltForm.merge();
			}

			if (lot.getId() == null ){

				logger.info("do we have an error: " + metalotError);

				if (!metalotError){
					Set<LotAlias> lotAliases = lot.getLotAliases();
					int numberOfLotAliases = lotAliases.size();
					logger.info("number of lot aliases: " + numberOfLotAliases);

					logger.debug("this is a new lot");
					lot.setSaltForm(saltForm);
					//lot.setParent(parent);
					if(lot.getCorpName() == null || lot.getCorpName().trim().equalsIgnoreCase("")){
						lot.setCorpName(lotService.generateCorpName(lot));
					}
					if (lot.getRegistrationDate() == null){
						lot.setRegistrationDate(new Date());				
					}

					Double parentWeight = parent.getMolWeight();
					logger.debug("Here is the parent weight " + parentWeight);
					logger.debug("Here is the parent totalSaltWeigth " + totalSaltWeight);

					Double lotWeight = parent.getMolWeight() + totalSaltWeight;
					logger.debug("lotWeight: " + lotWeight);	

					lot.setLotMolWeight(lotWeight);
					logger.info("Here are the lot comments: " + lot.getComments());

					if(lot.getBuid() == null) {
						lot.setBuid(0L);
					}
					try {
						lot.persist();
						logger.debug("lot buid = " + lot.getBuid());
						if (!lot.getIsVirtual()){
							if (lot.getBuid() == 0){
								lot.setBuid(lot.getId());
								lot.merge();
							}						
						}

						lot.flush();
					} catch (Exception e){
						logger.error("Caught an exception saving the lot: " + e);
						//get a new corp name and try saving again
						lot.setCorpName(lotService.generateCorpName(lot));	
						lot.persist();
						if (!lot.getIsVirtual()){
							if (lot.getBuid() == 0){
								lot.setBuid(lot.getId());
								lot.merge();
							}						
						}
						lot.flush();
					}

					logger.debug("just persisted the lot");
					logger.debug("lot weight: " + lot.getLotMolWeight());
					logger.debug("lot synthesis date: " + lot.getSynthesisDate());
					logger.debug("lot registered date: " + lot.getRegistrationDate());					
					Set<FileList> fileLists = metaLot.getFileList();
					if (fileLists.size() > 0){
						Lot fileLot = Lot.findLot(lot.getId());
						System.out.println("save new set of fileLists");
						for (FileList fileList : fileLists){
							fileList.setLot(fileLot);
							fileList.persist();
						}
					} else {
						logger.debug("no fileLists to save");        	
					}

					logger.info("--------- Number of lotAliases to save: " + lotAliases.size());
					lot = lotAliasService.updateLotAliases(lot, lotAliases);
					if (logger.isDebugEnabled()) logger.debug("Lot aliases after save: "+ LotAlias.toJsonArray(lot.getLotAliases()));
					
					if (propertiesUtilService.getCompoundInventory()) {
						boolean hasBarcode = (lot.getBarcode() != null && lot.getBarcode().length() > 0);
						if (hasBarcode || !propertiesUtilService.getDisableTubeCreationIfNoBarcode() ) {
							createNewTube(lot);
						}
					}


					lot.clear();
				}

			} else {
				logger.debug("this is an old lot. " + lot.getId() + "  " + lot.getCorpName() + "  " + lot.getColor());

				Set<LotAlias> lotAliases = lot.getLotAliases();
				int numberOfLotAliases = lotAliases.size();
				logger.info("number of lot aliases: " + numberOfLotAliases);

				if (!metaLot.isSkipParentDupeCheck()){
					Lot oldLot = Lot.findLot(lot.getId());
					oldLot.setComments(lot.getComments());
					oldLot.setColor(lot.getColor());
					oldLot.setSupplier(lot.getSupplier());
					oldLot.setSupplierID(lot.getSupplierID());
					oldLot.setPhysicalState(lot.getPhysicalState());
					oldLot.setPercentEE(lot.getPercentEE());
					oldLot.setAmount(lot.getAmount());
					oldLot.setAmountUnits(lot.getAmountUnits());
					oldLot.setPurityMeasuredBy(lot.getPurityMeasuredBy());
					oldLot.setPurityOperator(lot.getPurityOperator());
					oldLot.setPurity(lot.getPurity());
					oldLot.setNotebookPage(lot.getNotebookPage());
					oldLot.setBarcode(lot.getBarcode());
					oldLot.setSynthesisDate(lot.getSynthesisDate());
					oldLot.setChemist(lot.getChemist());
					oldLot.setModifiedBy(lot.getModifiedBy());
					oldLot.setModifiedDate(lot.getModifiedDate());
					oldLot.setLotMolWeight(Parent.findParent(lot.getParent().getId()).getMolWeight() + totalSaltWeight);
					//
					oldLot.setRetain(lot.getRetain());
					oldLot.setRetainUnits(lot.getRetainUnits());
					oldLot.setRetainLocation(lot.getRetainLocation());
					oldLot.setLambda(lot.getLambda());
					oldLot.setAbsorbance(lot.getAbsorbance());
					oldLot.setStockSolvent(lot.getStockSolvent());
					oldLot.setStockLocation(lot.getStockLocation());
					//
					oldLot.setObservedMassOne(lot.getObservedMassOne());
					oldLot.setObservedMassTwo(lot.getObservedMassTwo());
					oldLot.setVendorID(lot.getVendorID());
					oldLot.setTareWeight(lot.getTareWeight());
					oldLot.setTareWeightUnits(lot.getTareWeightUnits());
					oldLot.setTotalAmountStored(lot.getTotalAmountStored());
					oldLot.setTotalAmountStoredUnits(lot.getTotalAmountStoredUnits());
					//
					oldLot.setSupplierLot(lot.getSupplierLot());
					oldLot.setMeltingPoint(lot.getMeltingPoint());
					oldLot.setBoilingPoint(lot.getBoilingPoint());
					oldLot.setProject(lot.getProject());
					//
					oldLot.setSolutionAmount(lot.getSolutionAmount());
					if (lot.getSolutionAmountUnits() != null && lot.getSolutionAmountUnits().getId() != null){
						oldLot.setSolutionAmountUnits(lot.getSolutionAmountUnits());						
					} else {
						logger.warn("WARN: solutionAmountUnits not defined! ");
						lot.setSolutionAmountUnits(null);
					}

					logger.info("--------- Number of lotAliases to save: " + lotAliases.size());
					oldLot = lotAliasService.updateLotAliases(oldLot, lotAliases);
					if (logger.isDebugEnabled()) logger.debug("Lot aliases after save: "+ LotAlias.toJsonArray(lot.getLotAliases()));

					oldLot.setVendor(lot.getVendor());
					oldLot.persist();
					oldLot.flush();

					//update cas-number
					SaltForm oldSaltForm = SaltForm.findSaltForm(lot.getSaltForm().getId());
					if (logger.isDebugEnabled()) logger.debug("found the saltForm: " + oldSaltForm.toJson());
					if (lot.getSaltForm().getCasNumber() != null){
						oldSaltForm.setCasNumber(lot.getSaltForm().getCasNumber());
						oldSaltForm.merge();
						logger.debug("the CAS number is updated.");
					}
					if (lot.getSaltForm().getMolStructure() != null){
						oldSaltForm.setMolStructure(lot.getSaltForm().getMolStructure());
						oldSaltForm.merge();
						logger.debug("the SaltForm structure is updated.");
					}

					logger.debug("the lot is updated ");
					if (logger.isDebugEnabled()) logger.debug(Lot.findLot(oldLot.getId()).toJson());

				} else {
					logger.error("found an old lot while saving the bulk load compounds");
					ErrorMessage lotError = new ErrorMessage();
					lotError.setLevel("error");
					lotError.setMessage("found an old lot while saving the bulk load compounds");
					errors.add(lotError);
				}

				logger.debug("file list is being updated.");

				Set<FileList> fileLists = metaLot.getFileList();
				logger.debug("save new set of fileLists");
				List<Long> fileIds = new ArrayList<Long>();
				if (fileLists.size() > 0){
					Lot fileLot = Lot.findLot(lot.getId());
					for (FileList fileList : fileLists){
						if (fileList.getId() == null){
							fileList.setLot(fileLot);
							fileList.persist();						
						} 
						fileIds.add(fileList.getId());
					}
				} 

				logger.debug("file list is updated.");

				List<FileList> dbFileLists = FileList.findFileListsByLot(lot).getResultList();				
				for (FileList dbFileList : dbFileLists){
					if(!fileIds.contains(dbFileList.getId())){
						Long oldId = dbFileList.getId();
						logger.debug("removing old fileList: " + oldId);
						FileList oldFileList = FileList.findFileList(oldId);
						if (oldFileList != null){
							try {
								oldFileList.remove();
								logger.debug("removed old fileList: " + oldId);
							} catch (Exception e){
								logger.error("unable to remove the file. Error = " + e);
							}

						}
					}
				}

			}

		}
		Metalot savedMetaLot = new Metalot();
		Lot savedLot = Lot.findLot(lot.getId());
		savedMetaLot.setLot(savedLot);

		SaltForm savedSaltForm = SaltForm.findSaltForm(savedLot.getSaltForm().getId());
		List<IsoSalt> isoSalts = IsoSalt.findIsoSaltsBySaltForm(savedSaltForm).getResultList();
		savedMetaLot.getIsosalts().addAll(isoSalts);

		List<FileList> fileLists = FileList.findFileListsByLot(savedLot).getResultList();
		savedMetaLot.getFileList().addAll(fileLists);

		mr.setErrors(errors);
		mr.setMetalot(savedMetaLot);

		logger.debug("just ready to finish the update");

		return mr;

	}

	@Transactional
	private void createNewTube(Lot lot) throws MalformedURLException, IOException, NoResultException, NonUniqueResultException {
		String baseurl = propertiesUtilService.getAcasURL();
		String url = baseurl + "containers?";
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("lsType","definition container");
		queryParams.put("lsKind","tube");
		queryParams.put("format","codetable");
		String definitionContainerCodeTable = SimpleUtil.getFromExternalServer(url, queryParams, logger);
		Collection<CodeTableDTO> definitionContainerResults = CodeTableDTO.fromJsonArrayToCoes(definitionContainerCodeTable);
		if (definitionContainerResults.size() == 0){
			logger.error("Could not find definition container for tube");
			throw new NoResultException("Could not find definition container for tube");
		}
		CodeTableDTO definitionContainer = CodeTableDTO.fromJsonArrayToCoes(definitionContainerCodeTable).iterator().next();
		String wellName = "A001";
		Date recordedDate = new Date();
		CreatePlateRequestDTO tubeRequest = new CreatePlateRequestDTO();
		tubeRequest.setBarcode(lot.getBarcode());
		if (tubeRequest.getBarcode() == null || tubeRequest.getBarcode().length() < 1) tubeRequest.setBarcode(lot.getCorpName());
		tubeRequest.setCreatedDate(recordedDate);
		tubeRequest.setCreatedUser(lot.getRegisteredBy());
		tubeRequest.setDefinition(definitionContainer.getCode());
		tubeRequest.setRecordedBy(lot.getRegisteredBy());
		Collection<WellContentDTO> wells = new ArrayList<WellContentDTO>();
		WellContentDTO well = new WellContentDTO();
		well.setWellName(wellName);
		if (lot.getAmount() != null) well.setAmount(new BigDecimal(lot.getAmount()));
		if (lot.getAmountUnits() != null) well.setAmountUnits(lot.getAmountUnits().getCode());
		well.setPhysicalState("solid");
		well.setBatchCode(lot.getCorpName());
		well.setRecordedBy(lot.getRegisteredBy());
		well.setRecordedDate(recordedDate);
		wells.add(well);
		tubeRequest.setWells(wells);
				
		url = baseurl + "containers/createTube";
		try {
			String createTubeResponse = SimpleUtil.postRequestToExternalServer(url, tubeRequest.toJson(), logger);
			logger.debug("Created tube: ");
			logger.debug(createTubeResponse);
		}catch (IOException e) {
			if (e.getMessage().contains("400")) {
				logger.error("Barcode "+tubeRequest.getBarcode()+" already exists!");
				throw new NonUniqueResultException("Barcode "+tubeRequest.getBarcode()+" already exists!");
			}else {
				throw e;
			}
		}
		
		if(lot.getStorageLocation() != null && lot.getStorageLocation().length() > 0) {
			SetTubeLocationDTO moveDTO = new SetTubeLocationDTO();
			moveDTO.setBarcode(tubeRequest.getBarcode());
			moveDTO.setLocationBreadCrumb(lot.getStorageLocation());
			moveDTO.setUser(lot.getRegisteredBy());
			moveDTO.setDate(recordedDate);
			try {
				String rootLabel = lot.getStorageLocation().split(">")[0];
				moveDTO.setRootLabel(rootLabel);
			} catch (Exception e) {
				//do nothing
			}
			Collection<SetTubeLocationDTO> moveDTOs = new ArrayList<SetTubeLocationDTO>();
			moveDTOs.add(moveDTO);
			url = propertiesUtilService.getAcasAppURL() + "setLocationByBreadCrumb";
			try {
				String setLocationResponse = SimpleUtil.postRequestToExternalServer(url, SetTubeLocationDTO.toJsonArray(moveDTOs), logger);
				logger.debug("Successfully set location: ");
				logger.debug(setLocationResponse);
			}catch (IOException e) {
				logger.error("Hit error trying to set location of new tube",e);
				logger.error(e.getMessage());
				throw e;
			}
		}
	}

	private boolean checkUniqueNotebook(Lot lot) {
		List<Lot> lots = Lot.findLotsByNotebookPageEquals(lot.getNotebookPage()).getResultList();
		logger.debug("number of lots found by notebook entry" + lots.size());
		if (lots.size() > 0){
			return false;
		} else {
			return true;
		}
	}

	public static String nullBlankRegDate(String metaLotJson) {
		Pattern synthDatePattern = Pattern.compile("(\"synthesisDate\":\"\")", Pattern.CASE_INSENSITIVE);
		Matcher matcher = synthDatePattern.matcher(metaLotJson);
		return (matcher.replaceFirst("\"synthesisDate\":null"));
	}

	public Metalot saveParentAliases(Metalot metalot){
		Parent parent = metalot.getLot().getParent();
		for (ParentAlias parentAlias : parent.getParentAliases()){
			if (parentAlias.getId() == null){
				parentAlias.setParent(parent);
				parentAlias.persist();
			}
		}
		return metalot;

	}

	public Metalot saveLotAliases(Metalot metalot){
		Lot lot = metalot.getLot();
		for (LotAlias lotAlias : lot.getLotAliases()){
			if (lotAlias.getId() == null){
				lotAlias.setLot(lot);
				lotAlias.persist();
			}
		}
		return metalot;

	}
	
	private void generateAndSetCorpName(Parent parent) throws MalformedURLException, IOException {
		if (propertiesUtilService.getCorpParentFormat().equalsIgnoreCase("license_plate_format")){
			parent.setCorpName(corpNameService.generateCorpLicensePlate());
			//TODO: set parent number if required
		} else if (propertiesUtilService.getCorpParentFormat().equalsIgnoreCase("pre_defined_format")){
			PreDef_CorpName preDef_CorpName = PreDef_CorpName.findNextCorpName();
			parent.setCorpName(preDef_CorpName.getCorpName());
			preDef_CorpName.setUsed(true);
			preDef_CorpName.persist();
			parent.setParentNumber(preDef_CorpName.getCorpNumber());							
		} else if (propertiesUtilService.getCorpParentFormat().equalsIgnoreCase("ACASLabelSequence")) {
			LabelPrefixDTO labelPrefixDTO = parent.getLabelPrefix();
			labelPrefixDTO.setNumberOfLabels(1L);
			labelPrefixDTO.setLabelPrefix(labelPrefixDTO.getName());
			String url = propertiesUtilService.getAcasURL()+"labelsequences/getLabels";
			String jsonContent = labelPrefixDTO.toSafeJson();
			String responseJson = SimpleUtil.postRequestToExternalServer(url, jsonContent, logger);
			AutoLabelDTO autoLabel = AutoLabelDTO.fromJsonArrayToAutoes(responseJson).iterator().next();
			parent.setCorpName(autoLabel.getAutoLabel());
			parent.setParentNumber(autoLabel.getLabelNumber());
		} else {
			CorpNameDTO corpName = corpNameService.generateParentNameFromSequence();
			parent.setCorpName(corpName.getCorpName());
			parent.setParentNumber(corpName.getCorpNumber());
		}
	}
	
	private boolean checkCorpNameAlreadyExists(String corpName) {
		if (Parent.countFindParentsByCorpNameEquals(corpName) > 0L){
			return true;
		}else {
			return false;
		}
	}


}
