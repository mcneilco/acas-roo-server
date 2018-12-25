package com.labsynch.labseer.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.CompoundType;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.domain.QcCompound;
import com.labsynch.labseer.domain.ParentAnnotation;
import com.labsynch.labseer.domain.StereoCategory;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ParentAliasDTO;
import com.labsynch.labseer.dto.ParentDTO;
import com.labsynch.labseer.dto.ParentEditDTO;
import com.labsynch.labseer.dto.ParentValidationDTO;
import com.labsynch.labseer.dto.configuration.MainConfigDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.utils.Configuration;
import com.labsynch.labseer.utils.MoleculeUtil;



@Service
public class ParentServiceImpl implements ParentService {

	Logger logger = LoggerFactory.getLogger(ParentServiceImpl.class);

	public static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	@Autowired
	public ChemStructureService chemStructureService;

	@Autowired
	public ParentLotService parentLotService;

	@Autowired
	public ParentStructureService parentStructureService;

	@Autowired
	public ParentAliasService parentAliasService;
	
	@Autowired
	public CmpdRegSDFWriterFactory cmpdRegSDFWriterFactory;
	
	@Autowired
	public CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	@Override
	@Transactional
	public ParentValidationDTO validateUniqueParent(Parent queryParent) throws CmpdRegMolFormatException {
		ParentValidationDTO validationDTO = new ParentValidationDTO();

		if (queryParent.getCorpName() == null) validationDTO.getErrors().add(new ErrorMessage("error","Must provide corpName for parent to be validated"));
		if (queryParent.getStereoCategory() == null) validationDTO.getErrors().add(new ErrorMessage("error","Must provide stereo category for parent to be validated"));
		if (queryParent.getStereoCategory().getCode().equalsIgnoreCase("see_comments") && (queryParent.getStereoComment() == null || queryParent.getStereoComment().length()==0)){
			validationDTO.getErrors().add(new ErrorMessage("error","Stereo category is See Comments, but no stereo comment provided"));
		}
		if (chemStructureService.checkForSalt(queryParent.getMolStructure())){
			if (queryParent.getIsMixture() != null){
				if (!queryParent.getIsMixture()){
					validationDTO.getErrors().add(new ErrorMessage("error","Multiple fragments found. Please register the neutral base parent or mark as a Mixture"));
				}
			}else{
				validationDTO.getErrors().add(new ErrorMessage("error","Multiple fragments found. Please register the neutral base parent or mark as a Mixture"));
			}
		}
		if (!validationDTO.getErrors().isEmpty()){
			for (ErrorMessage error : validationDTO.getErrors()){
				logger.error(error.getMessage());
			}
			return validationDTO;
		}
		Collection<ParentDTO> dupeParents = new HashSet<ParentDTO>();
		int[] dupeParentList = chemStructureService.checkDupeMol(queryParent.getMolStructure(), "Parent_Structure", "Parent");
		if (dupeParentList.length > 0){
			searchResultLoop:
				for (int foundParentCdId : dupeParentList){
					List<Parent> foundParents = Parent.findParentsByCdId(foundParentCdId).getResultList();
					for (Parent foundParent : foundParents){
						//same structure hits
						if (queryParent.getCorpName().equals(foundParent.getCorpName())){
							//corpName match => this is the parent we're searching on. ignore this match.
							continue;
						}else{
							//same structure, different corpName => check stereo category
							if(queryParent.getStereoCategory().getCode().equalsIgnoreCase(foundParent.getStereoCategory().getCode())){
								//same structure and stereo category => check stereo comment
								if (queryParent.getStereoComment() == null && foundParent.getStereoComment() == null){
									//both null - stereo comments match => this is a dupe
									ParentDTO foundDupeParentDTO = new ParentDTO();
									foundDupeParentDTO.setCorpName(foundParent.getCorpName());
									foundDupeParentDTO.setStereoCategory(foundParent.getStereoCategory());
									foundDupeParentDTO.setStereoComment(foundParent.getStereoComment());
									dupeParents.add(foundDupeParentDTO);
								}else if (queryParent.getStereoComment() == null || foundParent.getStereoComment() == null){
									//one null, the other is not => not a dupe
									continue;
								}else if (queryParent.getStereoComment().equalsIgnoreCase(foundParent.getStereoComment())){
									//same stereo comment => this is a dupe
									ParentDTO foundDupeParentDTO = new ParentDTO();
									foundDupeParentDTO.setCorpName(foundParent.getCorpName());
									foundDupeParentDTO.setStereoCategory(foundParent.getStereoCategory());
									foundDupeParentDTO.setStereoComment(foundParent.getStereoComment());
									dupeParents.add(foundDupeParentDTO);
								}else{
									//different stereo comment => not a dupe
									continue;
								}
							}else{
								//different stereo category => not a dupe
								continue;
							}
						}
					}
				}
		}
		if (!dupeParents.isEmpty()){
			validationDTO.setParentUnique(false);
			validationDTO.setDupeParents(dupeParents);
			return validationDTO;
		}else{
			validationDTO.setParentUnique(true);
			validationDTO.setAffectedLots(parentLotService.getCodeTableLotsByParentCorpName(queryParent.getCorpName()));
			return validationDTO;
		}
	}

	@Override
	public Collection<CodeTableDTO> updateParent(Parent parent){
		Set<ParentAlias> parentAliases = parent.getParentAliases();
		parent = parentStructureService.update(parent);
		//save parent aliases
		logger.info("--------- Number of parentAliases to save: " + parentAliases.size());
		parent = parentAliasService.updateParentAliases(parent, parentAliases);
		if (logger.isDebugEnabled()) logger.debug("Parent aliases after save: "+ ParentAlias.toJsonArray(parent.getParentAliases()));
		Collection<CodeTableDTO> affectedLots = parentLotService.getCodeTableLotsByParentCorpName(parent.getCorpName());
		return affectedLots;
	}

	@Override
	public int restandardizeAllParentStructures() throws CmpdRegMolFormatException, IOException{
		List<Long> parentIds = Parent.getParentIds();
		Parent parent;
		List<Lot> lots;
		Lot lot;
		String originalStructure = null;
		String result;
		logger.info("number of parents to restandardize: " + parentIds.size());
		for  (Long parentId : parentIds){
			parent = Parent.findParent(parentId);
			lots = Lot.findLotsByParent(parent).getResultList();
			lot = lots.get(0);
			if (lots.size() > 0 && lot.getAsDrawnStruct() != null){
				originalStructure = lot.getAsDrawnStruct();				
			} else {
				logger.warn("Did not find the asDrawnStruct for parent: " + parentId + "  " + parent.getCorpName());
				originalStructure = parent.getMolStructure();
			}
			result = chemStructureService.standardizeStructure(originalStructure);
			parent.setMolStructure(result);
			parent = parentStructureService.update(parent);
		}

		return parentIds.size();
	}
	
	@Override
	public int restandardizeParentStructures(List<Long> parentIds) throws CmpdRegMolFormatException, IOException{
		Parent parent;
		List<Lot> lots;
		Lot lot;
		String originalStructure = null;
		String result;
		logger.info("number of parents to restandardize: " + parentIds.size());
		for  (Long parentId : parentIds){
			parent = Parent.findParent(parentId);
			lots = Lot.findLotsByParent(parent).getResultList();
			lot = lots.get(0);
			if (lots.size() > 0 && lot.getAsDrawnStruct() != null){
				originalStructure = lot.getAsDrawnStruct();				
			} else {
				logger.warn("Did not find the asDrawnStruct for parent: " + parentId + "  " + parent.getCorpName());
				originalStructure = parent.getMolStructure();
			}
			result = chemStructureService.standardizeStructure(originalStructure);
			parent.setMolStructure(result);
			parent = parentStructureService.update(parent);
		}

		return parentIds.size();
	}
	
	@Override
	public int restandardizeParentStructsWithDisplayChanges() throws CmpdRegMolFormatException, IOException{
		List<Long> parentIds = QcCompound.findParentsWithDisplayChanges().getResultList();
		int result = restandardizeParentStructures(parentIds);
		return result;
	}


	@Override
	public int findPotentialDupeParentStructures(String dupeCheckFile){
		List<Long> parentIds = Parent.getParentIds();
		Parent parent;
		List<Parent> dupeParents = new ArrayList<Parent>();
		int[] hits;
		logger.info("number of parents to check: " + parentIds.size());
		try {
			CmpdRegSDFWriter dupeMolExporter = cmpdRegSDFWriterFactory.getCmpdRegSDFWriter(dupeCheckFile);
			for  (Long parentId : parentIds){
				parent = Parent.findParent(parentId);
				hits = chemStructureService.searchMolStructures(parent.getMolStructure(), "Parent_Structure", "DUPLICATE_TAUTOMER");
				if (hits.length == 0){
					logger.error("did not find a match for parentId: " + parentId + "   parent: " + parent.getCorpName());
				} 
				for (int hit:hits){
					List<Parent> searchResultParents = Parent.findParentsByCdId(hit).getResultList();
					for (Parent searchResultParent : searchResultParents){
						if (searchResultParent.getCorpName().equalsIgnoreCase(parent.getCorpName())){
							logger.debug("found the same parent" + parent.getCorpName());
						} else {
							logger.info("found dupe parents");
							logger.info("query: " + parent.getCorpName() + "     dupe: " + searchResultParent.getCorpName());
							dupeParents.add(searchResultParent);
							CmpdRegMolecule parentMol = cmpdRegMoleculeFactory.getCmpdRegMolecule(parent.getMolStructure());
							parentMol.setProperty("corpName", parent.getCorpName());
							parentMol.setProperty("stereoCategory", parent.getStereoCategory().getName());
							parentMol.setProperty("stereoComment", parent.getStereoComment());
							parentMol.setProperty("dupeCorpName", searchResultParent.getCorpName());
							parentMol.setProperty("dupeStereoCategory", searchResultParent.getStereoCategory().getName());
							parentMol.setProperty("dupeStereoComment", searchResultParent.getStereoComment());

							//							MoleculeUtil.setMolProperty(parentMol, "dupeMolStructure", searchResultParent.getMolStructure());
							parentMol.setProperty("dupeMolSmiles", chemStructureService.toSmiles(searchResultParent.getMolStructure()));

							dupeMolExporter.writeMol(parentMol);
						}
					}
				}
			}
			dupeMolExporter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmpdRegMolFormatException e) {
			logger.error("Bad mol format",e);
		}



		logger.info("total number of potential dupes found: " + dupeParents.size());

		return (dupeParents.size());
		//export dupes to a SDF file for review

	}


	@Override
	public int findDupeParentStructures(String dupeCheckFile){
		List<Long> parentIds = Parent.getParentIds();
		Parent parent;
		List<Parent> dupeParents = new ArrayList<Parent>();
		int[] hits;
		logger.info("number of parents to check: " + parentIds.size());
		try {
			CmpdRegSDFWriter dupeMolExporter = cmpdRegSDFWriterFactory.getCmpdRegSDFWriter(dupeCheckFile);
			for  (Long parentId : parentIds){
				parent = Parent.findParent(parentId);
				hits = chemStructureService.searchMolStructures(parent.getMolStructure(), "Parent_Structure", "DUPLICATE_TAUTOMER");
				if (hits.length == 0){
					logger.error("did not find a match for parentId: " + parentId + "   parent: " + parent.getCorpName());
				} 
				String dupeCorpNames = "";
				boolean firstDupeHit = true;
				for (int hit:hits){
					List<Parent> searchResultParents = Parent.findParentsByCdId(hit).getResultList();
					for (Parent searchResult : searchResultParents){
						if (searchResult.getCorpName().equalsIgnoreCase(parent.getCorpName())){
							logger.debug("found the same parent: " + parent.getCorpName());
						} else {
							if (searchResult.getStereoCategory() == parent.getStereoCategory()
									&& searchResult.getStereoComment().equalsIgnoreCase(parent.getStereoComment())){
								logger.info("found dupe parents -- matching structure, stereo category, and stereo comments");
								logger.info("query: " + parent.getCorpName() + "     dupe: " + searchResult.getCorpName());
								if (!firstDupeHit) dupeCorpNames = dupeCorpNames.concat(";");
								dupeCorpNames = dupeCorpNames.concat(searchResult.getCorpName());
								firstDupeHit = false;
								dupeParents.add(searchResult);							
								CmpdRegMolecule parentMol = cmpdRegMoleculeFactory.getCmpdRegMolecule(parent.getMolStructure());
								parentMol.setProperty("corpName", parent.getCorpName());
								parentMol.setProperty("dupeCorpName", searchResult.getCorpName());
								parentMol.setProperty("stereoCategory", searchResult.getStereoCategory().getName());
								parentMol.setProperty("stereoComment", searchResult.getStereoComment());
								parentMol.setProperty("dupeMolSmiles", chemStructureService.toSmiles(searchResult.getMolStructure()));
								dupeMolExporter.writeMol(parentMol);

							} else {
								logger.info("found different stereo codes and comments");
							}
						}
					}
				}
			}
			dupeMolExporter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmpdRegMolFormatException e) {
			logger.error("Bad mol format",e);
		}
		logger.info("total number of dupes found: " + dupeParents.size());
		return (dupeParents.size());
	}

	// code to check the structures -- original as drawn mol --> standardized versus the original struct
	// may need a qc_compound table to store the mols and a list of identified dupes
	// id, runNumber, date, parent_id, corpName, dupeCount, dupeCorpName, asDrawnStruct, preMolStruct, postMolStruct, comment

	@Override
	@Transactional
	public void qcCheckParentStructures() throws CmpdRegMolFormatException, IOException{
		List<Long> parentIds = Parent.getParentIds();
		Parent parent;
		QcCompound qcCompound;
		List<Parent> dupeParents = new ArrayList<Parent>();
		int[] hits;
		int nonMatchingCmpds = 0;
		logger.info("number of parents to check: " + parentIds.size());
		Date qcDate = new Date();
		String asDrawnStruct;
		for  (Long parentId : parentIds){
			parent = Parent.findParent(parentId);
			qcCompound = new QcCompound();
			qcCompound.setQcDate(qcDate);
			qcCompound.setParentId(parent.getId());
			qcCompound.setCorpName(parent.getCorpName());
			List<Lot> queryLots = Lot.findLotByParentAndLowestLotNumber(parent).getResultList();
			if (queryLots.size() != 1) logger.error("!!!!!!!!!!!!  odd lot number size   !!!!!!!!!  " + queryLots.size() + "  saltForm: " + parent.getId());
			if (queryLots.size() > 0){
				asDrawnStruct = queryLots.get(0).getAsDrawnStruct();
			} else {
				asDrawnStruct = parent.getMolStructure();
			}
			qcCompound.setMolStructure(chemStructureService.standardizeStructure(asDrawnStruct));				
			boolean matching = chemStructureService.compareStructures(asDrawnStruct, qcCompound.getMolStructure(), "DUPLICATE");
			if (!matching){
				qcCompound.setDisplayChange(true);
				logger.info("the compounds are NOT matching: " + parent.getCorpName());
				nonMatchingCmpds++;
			}
			qcCompound.persist();
		}
		logger.info("total number of nonMatching compounds: " + nonMatchingCmpds);
	}

	@Override
	public void dupeCheckQCStructures() throws CmpdRegMolFormatException{

		List<Long> qcIds = QcCompound.findAllIds().getResultList();
		logger.info("number of qcCompounds found: " + qcIds.size());
		if (qcIds.size() > 0){
			int[] hits;	
			List<Long> testQcIds = qcIds.subList(0, 10);
			logger.info("size of test subList" + testQcIds.size());
			QcCompound qcCompound;
			Parent queryParent;
			for (Long qcId : testQcIds){
				qcCompound = QcCompound.findQcCompound(qcId);
				queryParent = Parent.findParent(qcCompound.getParentId());
				logger.info("query compound: " + qcCompound.getCorpName());
				hits = chemStructureService.searchMolStructures(qcCompound.getMolStructure(), "Parent_Structure", "DUPLICATE_TAUTOMER");
				for (int hit:hits){
					List<Parent> searchResultParents = Parent.findParentsByCdId(hit).getResultList();
					for (Parent searchResultParent : searchResultParents){
						if (searchResultParent.getCorpName().equalsIgnoreCase(queryParent.getCorpName())){
							//logger.info("found the same parent");
						} else {
							if (searchResultParent.getStereoCategory() == queryParent.getStereoCategory()
									&& searchResultParent.getStereoComment().contentEquals(queryParent.getStereoComment())){
								logger.info("found dupe parents");
								logger.info("query: " + qcCompound.getCorpName() + "     dupe: " + searchResultParent.getCorpName());
								//								dupeParents.add(searchResultParent);							
							} else {
								logger.info("found different stereo codes and comments");
							}

						}
					}

				}

			}			
		}


	}



	
	@Override
	public String updateParentMetaArray(String jsonInput, String modifiedByUser){
		int parentCounter = 0;
		Collection<ParentEditDTO> parentDTOs = ParentEditDTO.fromJsonArrayToParentEditDTO(jsonInput);
		for (ParentEditDTO parentDTO : parentDTOs){
			updateParentMeta(parentDTO, modifiedByUser);
			parentCounter++;
		}
		String returnMessage = "Number of parents updated: " + parentCounter;

		return  returnMessage;
	}
		
	
	@Override
	public Parent updateParentMeta(ParentEditDTO parentDTO, String modifiedByUser){

		// NON-EDITABLE fields via this service
		//		private Set<SaltFormDTO> saltForms = new HashSet<SaltFormDTO>();
		//		private String molStructure;
		//		private Double exactMass;
		//		private String molFormula;
		//		private int CdId;
		//	    private Double molWeight;
		
		Author modifiedUser = Author.findAuthorsByUserName(modifiedByUser).getSingleResult();
		Parent parent = null;
		if (parentDTO.getId() != null){
			parent = Parent.findParent(parentDTO.getId());
		} else {
			List<Parent> parents = Parent.findParentsByCorpNameEquals(parentDTO.getCorpName()).getResultList();
			if (parents.size() == 0){
				String errorMessage = "ERROR: Did not find requested query parent: " + parentDTO.getCorpName();
				logger.error(errorMessage);
				throw new RuntimeException(errorMessage);
			} else if (parents.size() > 1){
				String errorMessage = "ERROR: found multiple parents for: " + parentDTO.getCorpName();		
				logger.error(errorMessage);		
				throw new RuntimeException(errorMessage);
			} else {
				parent = parents.get(0);
			}
		}

		if (parentDTO.getComment() != null && parentDTO.getComment().length() > 0) parent.setComment(parentDTO.getComment());
		if (parentDTO.getStereoCategoryCode() != null && parentDTO.getStereoCategoryCode().length() > 0){
			parent.setStereoCategory(StereoCategory.findStereoCategorysByCodeEquals(parentDTO.getStereoCategoryCode()).getSingleResult());
		}
		if (parentDTO.getStereoComment() != null && parentDTO.getStereoComment().length() > 0) parent.setStereoComment(parentDTO.getStereoComment());

		ParentValidationDTO parentValidationDTO = null;
		try {
			parentValidationDTO = validateUniqueParent(parent);
		} catch (CmpdRegMolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!parentValidationDTO.isParentUnique()){
			String errorMessage = "ERROR: Changing stereocategory and/or comments will create a duplicate parent";
			logger.error(errorMessage );
			throw new RuntimeException(errorMessage);
		} 
		
		
		if (parentDTO.getChemistCode() != null && parentDTO.getChemistCode().length() > 0){
			parent.setChemist(Author.findAuthorsByUserName(parentDTO.getChemistCode()).getSingleResult().getUserName());
		}
		if (parentDTO.getCommonName() != null && parentDTO.getCommonName().length() > 0) parent.setCommonName(parentDTO.getCommonName());
		if (parentDTO.getIgnore() != null) parent.setIgnore(parentDTO.getIgnore());
		if (parentDTO.getIsMixture() != null) parent.setIsMixture(parentDTO.getIsMixture());
		if (parentDTO.getCompoundTypeCode() != null && parentDTO.getCompoundTypeCode().length() > 0){
			parent.setCompoundType(CompoundType.findCompoundTypesByCodeEquals(parentDTO.getCompoundTypeCode()).getSingleResult());
		}
		if (parentDTO.getParentAnnotationCode() != null && parentDTO.getParentAnnotationCode().length() > 0){
			parent.setParentAnnotation(ParentAnnotation.findParentAnnotationsByCodeEquals(parentDTO.getParentAnnotationCode()).getSingleResult());
		}
		
		if (parentDTO.getLiveDesignAliases() != null && parentDTO.getLiveDesignAliases().length() > 0){
			parent = parentAliasService.updateParentLiveDesignAlias(parent, parentDTO.getLiveDesignAliases());
		}

		if (parentDTO.getCommonNameAliases() != null && parentDTO.getCommonNameAliases().length() > 0){
			parent = parentAliasService.updateParentCommonNameAlias(parent, parentDTO.getCommonNameAliases());
		}
		
		if (parentDTO.getDefaultAliases() != null && parentDTO.getDefaultAliases().length() > 0){
			parent = parentAliasService.updateParentDefaultAlias(parent, parentDTO.getDefaultAliases());
		}

		
		if (parentDTO.getParentAliases() != null && !parentDTO.getParentAliases().isEmpty()){
			parent = updateParentAliasSet(parent, parentDTO);
		}
		

		if (parentValidationDTO.isParentUnique()){
			parent.setModifiedDate(new Date());
			parent.setModifiedBy(modifiedUser.getUserName());
			parent.merge();
		}

		return parent;
	}


	private Parent updateParentAliasSet(Parent parent, ParentEditDTO parentDTO) {
		Set<ParentAliasDTO> parentDTOAliases = parentDTO.getParentAliases();
		Map<String, ParentAliasDTO> parentDTOAliasMAP = new HashMap<String, ParentAliasDTO>();
		for (ParentAliasDTO parentDTOAlias : parentDTOAliases){
			parentDTOAliasMAP.put(parentDTOAlias.getAliasName(), parentDTOAlias);
		}
		
		List<ParentAlias> parentAliases = ParentAlias.findParentAliasesByParent(parent).getResultList();
		Map<String, ParentAlias> parentAliasMAP = new HashMap<String, ParentAlias>();
		for (ParentAlias parentAlias : parentAliases){
			parentAliasMAP.put(parentAlias.getAliasName(), parentAlias);
		}
		
		if (parentAliases != null && !parentAliases.isEmpty()){
			for (ParentAlias parentAlias : parentAliases){
				if (!parentDTOAliasMAP.containsKey(parentAlias.getAliasName())){
					parentAlias.setIgnored(true);
					parentAlias.merge();						
				}
			}
		}

		Set<ParentAlias> newParentAliases = new HashSet<ParentAlias>();
		for (ParentAliasDTO parentAliasDTO : parentDTO.getParentAliases()){
			if (parentAliasMAP.containsKey(parentAliasDTO.getAliasName())){
				
			} else {
				
			}
			ParentAlias newParentAlias = new ParentAlias();
			newParentAlias.setAliasName(parentAliasDTO.getAliasName());
			newParentAlias.setLsType(parentAliasDTO.getLsType());
			newParentAlias.setLsKind(parentAliasDTO.getLsKind());
			newParentAlias.setParent(parent);
			newParentAlias.persist();
			newParentAliases.add(newParentAlias);
		}
		
		parent.setParentAliases(newParentAliases);
		
		return parent;
	}

}

