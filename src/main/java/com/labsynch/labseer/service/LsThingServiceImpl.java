package com.labsynch.labseer.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.domain.LsTag;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolLabel;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ErrorMessageDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
import com.labsynch.labseer.exceptions.UniqueInteractionsException;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.utils.ItxLsThingLsThingComparator;
import com.labsynch.labseer.utils.LsThingComparatorByBatchNumber;
import com.labsynch.labseer.utils.LsThingComparatorByCodeName;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

@Service
public class LsThingServiceImpl implements LsThingService {

	private static final Logger logger = LoggerFactory.getLogger(LsThingServiceImpl.class);

	@Autowired
	private AutoLabelService autoLabelService;
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public String getProjectCodes(){

		String thingType = "project";
		String thingKind = "project";

		Collection<CodeTableDTO> codes = new HashSet<CodeTableDTO>();

		List<LsThing> projects = LsThing.findLsThing(thingType, thingKind).getResultList();
		for (LsThing project : projects){
			CodeTableDTO code = new CodeTableDTO();
			code.setCode(project.getCodeName());
			code.setIgnored(project.isIgnored());
			List<LsThingLabel> preferredNames = LsThingLabel.findLsThingPreferredName(project.getId(), "name", "project name").getResultList();
			if (preferredNames.size() != 1){
				//ERROR
			} else {
				code.setName(preferredNames.get(0).getLabelText());				
			}
			codes.add(code);
		}

		return CodeTableDTO.toJsonArray(codes);
	}


	@Override
	public PreferredNameResultsDTO getGeneCodeNameFromName(String json){

		String thingType = "gene";
		String thingKind = "entrez gene";
		String labelType = "name";
		String labelKind = "Entrez Gene ID";

		PreferredNameResultsDTO responseOutput = getPreferredNameFromName(thingType, thingKind, labelType, labelKind, json);

		return responseOutput;
	}
	
	@Override
	public PreferredNameResultsDTO getCodeNameFromName(String thingType, String thingKind, String labelType, String labelKind, PreferredNameRequestDTO requestDTO){

		logger.info("number of requests: " + requestDTO.getRequests().size());
		Collection<PreferredNameDTO> requests = requestDTO.getRequests();

		PreferredNameResultsDTO responseOutput = new PreferredNameResultsDTO();
		Collection<ErrorMessageDTO> errors = new HashSet<ErrorMessageDTO>();

		for (PreferredNameDTO request : requests){
			request.setPreferredName("");
			request.setReferenceName("");
			List<LsThing> lsThings = new ArrayList<LsThing>();
			if (labelType==null || labelKind==null || labelType.length()==0 || labelKind.length()==0){
				lsThings = LsThing.findLsThingByLabelText(thingType, thingKind, request.getRequestName()).getResultList();

			}else{
				lsThings = LsThing.findLsThingByLabelText(thingType, thingKind, labelType, labelKind, request.getRequestName()).getResultList();
			}
			if (lsThings.size() == 1){
				request.setPreferredName(pickBestLabel(lsThings.get(0)));
				request.setReferenceName(lsThings.get(0).getCodeName());
			} else if (lsThings.size() > 1){
				responseOutput.setError(true);
				ErrorMessageDTO error = new ErrorMessageDTO();
				error.setErrorCode("MULTIPLE RESULTS");
				error.setErrorMessage("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName() );	
				logger.error("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName());
				errors.add(error);
			} else {
				try{
					LsThing codeNameMatch = LsThing.findLsThingsByCodeNameEquals(request.getRequestName()).getSingleResult();
					request.setPreferredName(pickBestLabel(codeNameMatch));
					request.setReferenceName(codeNameMatch.getCodeName());
				}catch (EmptyResultDataAccessException e){
					logger.info("Did not find a LS_THING WITH THE REQUESTED NAME: " + request.getRequestName());
				}
			}
		}
		responseOutput.setResults(requests);
		responseOutput.setErrorMessages(errors);

		return responseOutput;
	}


	@Override
	public PreferredNameResultsDTO getPreferredNameFromName(String thingType, String thingKind, String labelType, String labelKind, String json){

		logger.info("in getPreferredNameFromName");

		PreferredNameRequestDTO requestDTO = PreferredNameRequestDTO.fromJsonToPreferredNameRequestDTO(json);	

		logger.info("number of requests: " + requestDTO.getRequests().size());

		Collection<PreferredNameDTO> requests = requestDTO.getRequests();

		PreferredNameResultsDTO responseOutput = new PreferredNameResultsDTO();
		Collection<ErrorMessageDTO> errors = new HashSet<ErrorMessageDTO>();

		Set<String> requestNameList = new HashSet<String>();
		for (PreferredNameDTO request : requests){
			requestNameList.add(request.getRequestName());
		}

		List<PreferredNameDTO> lsThingLabelsList = LsThingLabel.findLsThingPreferredName(thingType, thingKind, labelType, labelKind, requestNameList).getResultList();
		
		logger.info("number of thing labels found: " + lsThingLabelsList.size());
		MultiValueMap mvm = new MultiValueMap();
		for (PreferredNameDTO pn : lsThingLabelsList){
			mvm.put(pn.getRequestName(), pn);
		}

		for (PreferredNameDTO request : requests){
			request.setPreferredName("");
			request.setReferenceName("");
			//List<LsThingLabel> lsThingLabels = LsThingLabel.findLsThingPreferredName(thingType, thingKind, labelType, labelKind, request.getRequestName()).getResultList();
			@SuppressWarnings("unchecked")
			List<PreferredNameDTO> lsThingLabels = (List<PreferredNameDTO>) mvm.get(request.getRequestName());

			if (lsThingLabels != null && lsThingLabels.size() == 1){
				request.setPreferredName(lsThingLabels.get(0).getPreferredName());
				request.setReferenceName(lsThingLabels.get(0).getReferenceName());
			} else if (lsThingLabels != null && lsThingLabels.size() > 1){
				responseOutput.setError(true);
				ErrorMessageDTO error = new ErrorMessageDTO();
				error.setErrorCode("MULTIPLE RESULTS");
				error.setErrorMessage("FOUND MULTIPLE LS_THINGS WITH THE SAME NAME: " + request.getRequestName() );	
				logger.error("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName());
				errors.add(error);
			} else {
				logger.info("Did not find a LS_THING WITH THE REQUESTED NAME: " + request.getRequestName());
			}
		}


		responseOutput.setResults(requests);
		responseOutput.setErrorMessages(errors);

		logger.info(responseOutput.toJson());
		
		return responseOutput;
	}
	
	@Override
	public PreferredNameResultsDTO getPreferredNameFromName(String thingType, String thingKind, String labelType, String labelKind, PreferredNameRequestDTO requestDTO){

		logger.info("in getPreferredNameFromName");

		logger.info("number of requests: " + requestDTO.getRequests().size());

		Collection<PreferredNameDTO> requests = requestDTO.getRequests();

		PreferredNameResultsDTO responseOutput = new PreferredNameResultsDTO();
		Collection<ErrorMessageDTO> errors = new HashSet<ErrorMessageDTO>();

		Set<String> requestNameList = new HashSet<String>();
		for (PreferredNameDTO request : requests){
			requestNameList.add(request.getRequestName());
		}

		List<PreferredNameDTO> lsThingLabelsList = LsThingLabel.findLsThingPreferredName(thingType, thingKind, labelType, labelKind, requestNameList).getResultList();
		
		logger.info("number of thing labels found: " + lsThingLabelsList.size());
		MultiValueMap mvm = new MultiValueMap();
		for (PreferredNameDTO pn : lsThingLabelsList){
			mvm.put(pn.getRequestName(), pn);
		}

		for (PreferredNameDTO request : requests){
			request.setPreferredName("");
			request.setReferenceName("");
			//List<LsThingLabel> lsThingLabels = LsThingLabel.findLsThingPreferredName(thingType, thingKind, labelType, labelKind, request.getRequestName()).getResultList();
			@SuppressWarnings("unchecked")
			List<PreferredNameDTO> lsThingLabels = (List<PreferredNameDTO>) mvm.get(request.getRequestName());

			if (lsThingLabels != null && lsThingLabels.size() == 1){
				request.setPreferredName(lsThingLabels.get(0).getPreferredName());
				request.setReferenceName(lsThingLabels.get(0).getReferenceName());
			} else if (lsThingLabels != null && lsThingLabels.size() > 1){
				responseOutput.setError(true);
				ErrorMessageDTO error = new ErrorMessageDTO();
				error.setErrorCode("MULTIPLE RESULTS");
				error.setErrorMessage("FOUND MULTIPLE LS_THINGS WITH THE SAME NAME: " + request.getRequestName() );	
				logger.error("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName());
				errors.add(error);
			} else {
				logger.info("Did not find a LS_THING WITH THE REQUESTED NAME: " + request.getRequestName());
			}
		}


		responseOutput.setResults(requests);
		responseOutput.setErrorMessages(errors);

		logger.info(responseOutput.toJson());
		
		return responseOutput;
	}
	
	@Override
	@Transactional
	public LsThing updateLsThing(LsThing jsonLsThing){
		LsThing updatedLsThing = LsThing.updateNoMerge(jsonLsThing);
		if (jsonLsThing.getLsLabels() != null) {
			for(LsThingLabel lsThingLabel : jsonLsThing.getLsLabels()){
				logger.debug("Label in hand: " + lsThingLabel.getLabelText());			
				if (lsThingLabel.getId() == null){
					LsThingLabel newLsThingLabel = new LsThingLabel(lsThingLabel);
					newLsThingLabel.setLsThing(updatedLsThing);
					newLsThingLabel.persist();
					updatedLsThing.getLsLabels().add(newLsThingLabel);
				} else {
					LsThingLabel updatedLabel = LsThingLabel.update(lsThingLabel);
					logger.debug("updated lsThing label " + updatedLabel.getId());
				}
			}			
		} else {
			logger.debug("No lsThing labels to update");
		}
		updateLsStates(jsonLsThing, updatedLsThing);
		//updated itx and nested LsThings
		if(jsonLsThing.getFirstLsThings() != null){
			//there are itx's
			Set<ItxLsThingLsThing> firstLsThings = new HashSet<ItxLsThingLsThing>();
			for (ItxLsThingLsThing itxLsThingLsThing : jsonLsThing.getFirstLsThings()){
				ItxLsThingLsThing updatedItxLsThingLsThing;
				if (itxLsThingLsThing.getId() == null){
					//need to save a new itx
					logger.debug("saving new itxLsThingLsThing: " + itxLsThingLsThing.toJson());
					updateNestedFirstLsThing(itxLsThingLsThing);
					itxLsThingLsThing.setSecondLsThing(updatedLsThing);
					updatedItxLsThingLsThing = saveItxLsThingLsThing(itxLsThingLsThing);
					firstLsThings.add(updatedItxLsThingLsThing);
				}else {
					//old itx needs to be updated
					updateNestedFirstLsThing(itxLsThingLsThing);
					itxLsThingLsThing.setSecondLsThing(updatedLsThing);
					updatedItxLsThingLsThing = ItxLsThingLsThing.updateNoMerge(itxLsThingLsThing);
					updateItxLsStates(itxLsThingLsThing, updatedItxLsThingLsThing);
					updatedItxLsThingLsThing.merge();
					firstLsThings.add(updatedItxLsThingLsThing);
				}
			}
			updatedLsThing.setFirstLsThings(firstLsThings);
		}
		
		if(jsonLsThing.getSecondLsThings() != null){
			//there are itx's
			Set<ItxLsThingLsThing> secondLsThings = new HashSet<ItxLsThingLsThing>();
			for (ItxLsThingLsThing itxLsThingLsThing : jsonLsThing.getSecondLsThings()){
				ItxLsThingLsThing updatedItxLsThingLsThing;
				if (itxLsThingLsThing.getId() == null){
					//need to save a new itx
					logger.debug("saving new itxLsThingLsThing: " + itxLsThingLsThing.toJson());
					updateNestedSecondLsThing(itxLsThingLsThing);
					itxLsThingLsThing.setFirstLsThing(updatedLsThing);
					updatedItxLsThingLsThing = saveItxLsThingLsThing(itxLsThingLsThing);
					secondLsThings.add(updatedItxLsThingLsThing);
				}else {
					//old itx needs to be updated
					updateNestedSecondLsThing(itxLsThingLsThing);
					itxLsThingLsThing.setFirstLsThing(updatedLsThing);
					updatedItxLsThingLsThing = ItxLsThingLsThing.update(itxLsThingLsThing);
					updateItxLsStates(itxLsThingLsThing, updatedItxLsThingLsThing);
					secondLsThings.add(updatedItxLsThingLsThing);
				}
			}
			updatedLsThing.setSecondLsThings(secondLsThings);
		}
		updatedLsThing.merge();
		
		return updatedLsThing;

	}

	private void updateNestedFirstLsThing(ItxLsThingLsThing itxLsThingLsThing) {
		LsThing updatedNestedLsThing;
		if (itxLsThingLsThing.getFirstLsThing().getId() == null){
			//need to save a new nested lsthing
			logger.debug("saving new nested LsThing" + itxLsThingLsThing.getFirstLsThing().toJson());
			try{
				updatedNestedLsThing = saveLsThing(itxLsThingLsThing.getFirstLsThing());
				itxLsThingLsThing.setFirstLsThing(updatedNestedLsThing);
			} catch (UniqueNameException e){
				logger.error("Caught UniqueNameException trying to update nested LsThing");
			}
		}
		else{
			//just need to update the old nested lsThing inside the new itx
			updatedNestedLsThing = LsThing.update(itxLsThingLsThing.getFirstLsThing());
			updateLsStates(itxLsThingLsThing.getFirstLsThing(), updatedNestedLsThing);
			itxLsThingLsThing.setFirstLsThing(updatedNestedLsThing);
		}
	}
	
	private void updateNestedSecondLsThing(ItxLsThingLsThing itxLsThingLsThing) {
		LsThing updatedNestedLsThing;
		if (itxLsThingLsThing.getSecondLsThing().getId() == null){
			//need to save a new nested lsthing
			logger.debug("saving new nested LsThing" + itxLsThingLsThing.getSecondLsThing().toJson());
			try{
				updatedNestedLsThing = saveLsThing(itxLsThingLsThing.getSecondLsThing());
				itxLsThingLsThing.setSecondLsThing(updatedNestedLsThing);
			} catch (UniqueNameException e){
				logger.error("Caught UniqueNameException trying to update nested LsThing");
			}
		}
		else{
			//just need to update the old nested lsThing inside the new itx
			updatedNestedLsThing = LsThing.update(itxLsThingLsThing.getSecondLsThing());
			updateLsStates(itxLsThingLsThing.getSecondLsThing(), updatedNestedLsThing);
			itxLsThingLsThing.setSecondLsThing(updatedNestedLsThing);
		}
	}


	@Override
	@Transactional
	public LsThing saveLsThing(LsThing lsThing) throws UniqueNameException{
		boolean checkLsThingName = propertiesUtilService.getUniqueLsThingName();
		return saveLsThing(lsThing, checkLsThingName);
	}
	
	@Override
	@Transactional
	public LsThing saveLsThing(LsThing lsThing, boolean checkLsThingName) throws UniqueNameException{
		logger.debug("incoming meta lsThing: " + lsThing.toJson());

		//check if lsThing with the same name exists
		if (checkLsThingName){
			boolean lsThingExists = false;
			Set<LsThingLabel> lsThingLabels = lsThing.getLsLabels();
			for (LsThingLabel label : lsThingLabels){
				String labelText = label.getLabelText();
				List<LsThingLabel> foundLsThingLabels = LsThingLabel.findLsThingLabelsByLabelTextEqualsAndIgnoredNot(labelText, true).getResultList();	
				for (LsThingLabel foundLabel : foundLsThingLabels){
					LsThing foundLsThing = foundLabel.getLsThing();
					//if the lsThing is not hard deleted or soft deleted, there is a name conflict
					if (!foundLsThing.isIgnored()){
						lsThingExists = true;
					}
				}
			}

			if (lsThingExists){
				throw new UniqueNameException("LsThing with the same name exists");							
			}
		}

		LsThing newLsThing = new LsThing(lsThing);
		if (newLsThing.getCodeName() == null){
			if (newLsThing.getLsTypeAndKind() == null) newLsThing.setLsTypeAndKind(newLsThing.getLsType()+"_"+newLsThing.getLsKind());
			newLsThing.setCodeName(autoLabelService.getLsThingCodeName(newLsThing.getLsTypeAndKind()));
		}
		newLsThing.persist();
		logger.debug("persisted the newLsThing: " + newLsThing.toJson());


		if (lsThing.getLsLabels() != null) {
			Set<LsThingLabel> lsLabels = new HashSet<LsThingLabel>();
			for(LsThingLabel lsThingLabel : lsThing.getLsLabels()){
				LsThingLabel newLsThingLabel = new LsThingLabel(lsThingLabel);
				newLsThingLabel.setLsThing(newLsThing);
				logger.debug("here is the newLsThingLabel before save: " + newLsThingLabel.toJson());
				newLsThingLabel.persist();
				lsLabels.add(newLsThingLabel);
			}
			newLsThing.setLsLabels(lsLabels);
		} else {
			logger.debug("No lsThing labels to save");
		}

		if(lsThing.getLsStates() != null){
			Set<LsThingState> lsStates = new HashSet<LsThingState>();
			for(LsThingState lsThingState : lsThing.getLsStates()){
				LsThingState newLsThingState = new LsThingState(lsThingState);
				newLsThingState.setLsThing(newLsThing);
				logger.debug("here is the newLsThingState before save: " + newLsThingState.toJson());
				newLsThingState.persist();
				logger.debug("persisted the newLsThingState: " + newLsThingState.toJson());
				if (lsThingState.getLsValues() != null){
					Set<LsThingValue> lsValues = new HashSet<LsThingValue>();
					for(LsThingValue lsThingValue : lsThingState.getLsValues()){
						logger.debug("lsThingValue: " + lsThingValue.toJson());
						LsThingValue newLsThingValue = new LsThingValue(lsThingValue);
						newLsThingValue.setLsState(newLsThingState);
						newLsThingValue.persist();
						lsValues.add(newLsThingValue);
						logger.debug("persisted the lsThingValue: " + newLsThingValue.toJson());
					}	
					newLsThingState.setLsValues(lsValues);
				} else {
					logger.debug("No lsThing values to save");
				}
				lsStates.add(newLsThingState);
			}
			newLsThing.setLsStates(lsStates);
		}
		
		if(lsThing.getFirstLsThings() != null){
			Set<ItxLsThingLsThing> firstLsThings = new HashSet<ItxLsThingLsThing>();
			for (ItxLsThingLsThing itxLsThingLsThing : lsThing.getFirstLsThings()){
				if (itxLsThingLsThing.getId() == null){
					logger.debug("saving new itxLsThingLsThing: " + itxLsThingLsThing.toJson());
					if (itxLsThingLsThing.getFirstLsThing().getId() == null){
						logger.debug("saving new nested LsThing" + itxLsThingLsThing.getFirstLsThing().toJson());
						LsThing nestedLsThing = saveLsThing(itxLsThingLsThing.getFirstLsThing());
						itxLsThingLsThing.setFirstLsThing(nestedLsThing);
					}
					itxLsThingLsThing.setSecondLsThing(newLsThing);
					ItxLsThingLsThing newItxLsThingLsThing = saveItxLsThingLsThing(itxLsThingLsThing);
					firstLsThings.add(newItxLsThingLsThing);
				}else {
					firstLsThings.add(itxLsThingLsThing);
				}
			}
			newLsThing.setFirstLsThings(firstLsThings);
		}
		
		if(lsThing.getSecondLsThings() != null){
			Set<ItxLsThingLsThing> secondLsThings = new HashSet<ItxLsThingLsThing>();
			for (ItxLsThingLsThing itxLsThingLsThing : lsThing.getSecondLsThings()){
				if (itxLsThingLsThing.getId() == null){
					logger.debug("saving new itxLsThingLsThing: " + itxLsThingLsThing.toJson());
					if (itxLsThingLsThing.getSecondLsThing().getId() == null){
						logger.debug("saving new nested LsThing: " + itxLsThingLsThing.getSecondLsThing().toJson());
						LsThing nestedLsThing = saveLsThing(itxLsThingLsThing.getSecondLsThing());
						itxLsThingLsThing.setSecondLsThing(nestedLsThing);
					}
					itxLsThingLsThing.setFirstLsThing(newLsThing);
					ItxLsThingLsThing newItxLsThingLsThing = saveItxLsThingLsThing(itxLsThingLsThing);
					secondLsThings.add(newItxLsThingLsThing);
				}else {
					secondLsThings.add(itxLsThingLsThing);
				}
			}
			newLsThing.setSecondLsThings(secondLsThings);
		}
		return newLsThing;
	}
	
	@Override
	@Transactional
	public LsThing saveLsThing(LsThing lsThing, boolean isParent, boolean isBatch, Long parentId) throws UniqueNameException{
		//only check that the name is unique upon save if it's a parent and a component
		boolean checkUniqueLsThingName = propertiesUtilService.getUniqueLsThingName();
		LsThing savedLsThing = saveLsThing(lsThing, checkUniqueLsThingName);
		//after saving the lsThing, save the necessary interactions
		if (isBatch){
			LsThing parent = LsThing.findLsThing(parentId);
			saveItxLsThingLsThing("instantiates", "batch_parent", savedLsThing, parent, lsThing.getRecordedBy(), lsThing.getRecordedDate());
		}
		return savedLsThing;
	}
	
	
	private ItxLsThingLsThing saveItxLsThingLsThing(ItxLsThingLsThing itxLsThingLsThing){
		ItxLsThingLsThing newItxLsThingLsThing = new ItxLsThingLsThing(itxLsThingLsThing);
		newItxLsThingLsThing.persist();
		if(itxLsThingLsThing.getLsStates() != null){
			Set<ItxLsThingLsThingState> lsStates = new HashSet<ItxLsThingLsThingState>();
			for(ItxLsThingLsThingState itxLsThingLsThingState : itxLsThingLsThing.getLsStates()){
				ItxLsThingLsThingState newItxLsThingLsThingState = new ItxLsThingLsThingState(itxLsThingLsThingState);
				newItxLsThingLsThingState.setItxLsThingLsThing(newItxLsThingLsThing);
				logger.debug("here is the newItxLsThingLsThingState before save: " + newItxLsThingLsThingState.toJson());
				newItxLsThingLsThingState.persist();
				logger.debug("persisted the newItxLsThingLsThingState: " + newItxLsThingLsThingState.toJson());
				if (itxLsThingLsThingState.getLsValues() != null){
					Set<ItxLsThingLsThingValue> lsValues = new HashSet<ItxLsThingLsThingValue>();
					for(ItxLsThingLsThingValue itxLsThingLsThingValue : itxLsThingLsThingState.getLsValues()){
						logger.debug("itxLsThingLsThingValue: " + itxLsThingLsThingValue.toJson());
						ItxLsThingLsThingValue newItxLsThingLsThingValue = new ItxLsThingLsThingValue(itxLsThingLsThingValue);
						newItxLsThingLsThingValue.setLsState(newItxLsThingLsThingState);
						newItxLsThingLsThingValue.persist();
						lsValues.add(newItxLsThingLsThingValue);
						logger.debug("persisted the itxLsThingLsThingValue: " + newItxLsThingLsThingValue.toJson());
					}	
					newItxLsThingLsThingState.setLsValues(lsValues);
				} else {
					logger.debug("No itxLsThingLsThing values to save");
				}
				lsStates.add(newItxLsThingLsThingState);
			}
			newItxLsThingLsThing.setLsStates(lsStates);
		}
		return newItxLsThingLsThing;
	}
	
	private void saveItxLsThingLsThing(String lsType, String lsKind,
			LsThing firstLsThing, LsThing secondLsThing, int order, String recordedBy,
			Date recordedDate) {
		ItxLsThingLsThing itxLsThingLsThing = new ItxLsThingLsThing();
		itxLsThingLsThing.setLsType(lsType);
		itxLsThingLsThing.setLsKind(lsKind);
		itxLsThingLsThing.setFirstLsThing(firstLsThing);
		itxLsThingLsThing.setSecondLsThing(secondLsThing);
		itxLsThingLsThing.setRecordedBy(recordedBy);
		itxLsThingLsThing.setRecordedDate(recordedDate);
		ItxLsThingLsThingState itxLsThingLsThingState = new ItxLsThingLsThingState();
		itxLsThingLsThingState.setItxLsThingLsThing(itxLsThingLsThing);
		itxLsThingLsThingState.setLsType("metadata");
		itxLsThingLsThingState.setLsKind("composition");
		itxLsThingLsThingState.setRecordedBy(recordedBy);
		itxLsThingLsThingState.setRecordedDate(recordedDate);
		ItxLsThingLsThingValue itxLsThingLsThingValue = new ItxLsThingLsThingValue();
		itxLsThingLsThingValue.setLsState(itxLsThingLsThingState);
		itxLsThingLsThingValue.setLsType("numericValue");
		itxLsThingLsThingValue.setLsKind("order");
		itxLsThingLsThingValue.setRecordedBy(recordedBy);
		itxLsThingLsThingValue.setRecordedDate(recordedDate);
		itxLsThingLsThingValue.setNumericValue(new BigDecimal(order));
		itxLsThingLsThing.getLsStates().add(itxLsThingLsThingState);
		itxLsThingLsThingState.getLsValues().add(itxLsThingLsThingValue);
		itxLsThingLsThing.persist();
		itxLsThingLsThingState.persist();
		itxLsThingLsThingValue.persist();
		
	}


	private void saveItxLsThingLsThing(String lsType, String lsKind,
			LsThing firstLsThing, LsThing secondLsThing, String recordedBy, Date recordedDate) {
		ItxLsThingLsThing itxLsThingLsThing = new ItxLsThingLsThing();
		itxLsThingLsThing.setLsType(lsType);
		itxLsThingLsThing.setLsKind(lsKind);
		itxLsThingLsThing.setFirstLsThing(firstLsThing);
		itxLsThingLsThing.setSecondLsThing(secondLsThing);
		itxLsThingLsThing.setRecordedBy(recordedBy);
		itxLsThingLsThing.setRecordedDate(recordedDate);
		itxLsThingLsThing.persist();
	}


	@Override
	public String generateBatchCodeName(LsThing parent){
		String parentCodeName = parent.getCodeName();
		int batchNumber = getBatchNumber(parent);
		String batchCodeName = parentCodeName.concat("-"+ String.valueOf(batchNumber));
		return batchCodeName;
	}


	private int getBatchNumber(LsThing parent) {
		LsThingValue batchNumberValue = LsThingValue.findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(parent.getId(), "metadata", parent.getLsKind() + " " + parent.getLsType(), "numericValue", "batch number").getSingleResult();
		int batchNumber = batchNumberValue.getNumericValue().intValue();
		batchNumber += 1;
		batchNumberValue.setNumericValue(new BigDecimal(batchNumber));
		batchNumberValue.merge();
		return batchNumber;
	}


	@Override
	public Collection<LsThing> findBatchesByParentEquals(LsThing parent) {
		Collection<LsThing> batches;
		try{
			batches = LsThing.findFirstLsThingsByItxTypeKindEqualsAndSecondLsThingEquals("instantiates", "batch_parent", parent).getResultList();
		} catch (EmptyResultDataAccessException e){
			batches = null;
		}
		return batches;
	}
	
	@Override
	public Collection<LsThing> findCompositesByComponentEquals(LsThing component) {
		Collection<LsThing> batches;
		try{
			batches = LsThing.findFirstLsThingsByItxTypeEqualsAndSecondLsThingEquals("incorporates", component).getResultList();
		} catch (EmptyResultDataAccessException e){
			batches = null;
		}
		return batches;
	}
	
	@Override
	public LsThing findParentByBatchEquals(LsThing batch) {
		LsThing parent;
		try{
			parent = LsThing.findSecondLsThingsByItxTypeKindEqualsAndFirstLsThingEquals("instantiates", "batch_parent", batch).getSingleResult();
		} catch (EmptyResultDataAccessException e){
			parent = null;
		}
		return parent;
	}


	@Override
	public Collection<LsThing> findLsThingsByGenericMetaDataSearch(
			String queryString) {
		//make our HashSets: lsThingIdList will be filled/cleared/refilled for each term
		//lsThingList is the final search result
		HashSet<Long> lsThingIdList = new HashSet<Long>();
		HashSet<Long> lsThingAllIdList = new HashSet<Long>();
		Collection<LsThing> lsThingList = new HashSet<LsThing>();
		//Split the query up on spaces
		List<String> splitQuery = SimpleUtil.splitSearchString(queryString);
		logger.debug("Number of search terms: " + splitQuery.size());
		//Make the Map of terms and HashSets of lsThing id's then fill. We will run intersect logic later.
		Map<String, HashSet<Long>> resultsByTerm = new HashMap<String, HashSet<Long>>();
		for (String term : splitQuery) {
			lsThingIdList.addAll(findLsThingIdsByMetadata(term, "CODENAME"));
			lsThingIdList.addAll(findLsThingIdsByMetadata(term, "PARENT NAME"));
			lsThingIdList.addAll(findLsThingIdsByMetadata(term, "RECORDEDBY"));
			lsThingIdList.addAll(findLsThingIdsByMetadata(term, "SCIENTIST"));
			lsThingIdList.addAll(findLsThingIdsByMetadata(term, "LSKIND"));
			lsThingIdList.addAll(findLsThingIdsByMetadata(term, "DATE"));
			lsThingIdList.addAll(findLsThingIdsByMetadata(term, "NOTEBOOK"));

			resultsByTerm.put(term, new HashSet<Long>(lsThingIdList));
			lsThingAllIdList.addAll(lsThingIdList);
			lsThingIdList.clear();
		}
		//Here is the intersect logic
		for (String term: splitQuery) {
			lsThingAllIdList.retainAll(resultsByTerm.get(term));
		}
		for (Long id: lsThingAllIdList) lsThingList.add(LsThing.findLsThing(id));

		//This method uses finders that will find everything, whether or not it is ignored or deleted
		Collection<LsThing> result = new HashSet<LsThing>();
		for (LsThing lsThing: lsThingList) {
			//For LsThing Browser, we want to see soft deleted (ignored=true, deleted=false), but not hard deleted (ignored=deleted=true)
			if (lsThing.isDeleted()){
				logger.debug("removing a deleted lsThing from the results");
			} else {
				//Inject parent preferred label to all batch lsThings
				if (lsThing.getLsType().equals("batch")){
					LsThingLabel bestParentLabel = LsThingLabel.pickBestLabel(findParentByBatchEquals(lsThing).getLsLabels());
					lsThing.getLsLabels().add(bestParentLabel);
				}
				result.add(lsThing);
			}
		}
		return result;
	}


	private Collection<? extends Long> findLsThingIdsByMetadata(String queryString,
			String searchBy) {
		Collection<Long> lsThingIdList = new HashSet<Long>();
		if (searchBy == "CODENAME") {
			List<LsThing> lsThings = LsThing.findLsThingsByCodeNameLike(queryString).getResultList();
			if (!lsThings.isEmpty()){
				for (LsThing lsThing:lsThings) {
					lsThingIdList.add(lsThing.getId());
				}
			}
			lsThings.clear();
		}
		if (searchBy == "LSKIND") {
			List<LsThing> lsThings = LsThing.findLsThingsByLsKindLike(queryString).getResultList();
			if (!lsThings.isEmpty()){
				for (LsThing lsThing:lsThings) {
					lsThingIdList.add(lsThing.getId());
				}
			}
			lsThings.clear();
		}
		if (searchBy == "PARENT NAME") {
			Collection<LsThingLabel> lsThingLabels = LsThingLabel.findLsThingLabelsByLabelTextLike(queryString).getResultList();
			if (!lsThingLabels.isEmpty()) {
				for (LsThingLabel lsThingLabel: lsThingLabels) {
					LsThing parent = lsThingLabel.getLsThing();
					Collection<LsThing> batches = findBatchesByParentEquals(parent);
					for (LsThing batch : batches){
						lsThingIdList.add(batch.getId());
					}
					lsThingIdList.add(parent.getId());
				}
			}
			lsThingLabels.clear();
		}
		if (searchBy == "RECORDEDBY") {
			List<LsThing> lsThings = LsThing.findLsThingsByRecordedByLike(queryString).getResultList();
			if (!lsThings.isEmpty()){
				for (LsThing lsThing:lsThings) {
					lsThingIdList.add(lsThing.getId());
				}
			}
			lsThings.clear();
		}
		if (searchBy == "SCIENTIST") {
			Collection<LsThingValue> lsThingValues = LsThingValue.findLsThingValuesByLsKindEqualsAndCodeValueLike("scientist", queryString).getResultList();
			if (!lsThingValues.isEmpty()){
				for (LsThingValue lsThingValue : lsThingValues) {
					lsThingIdList.add(lsThingValue.getLsState().getLsThing().getId());
				}
			}
			lsThingValues.clear();
		}

		if (searchBy == "DATE") {
			Collection<LsThingValue> lsThingValues = new HashSet<LsThingValue>();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
			try {
				Date date = df.parse(queryString);
				lsThingValues = LsThingValue.findLsThingValuesByLsKindEqualsAndDateValueLike("completion date", date).getResultList();
			} catch (Exception e) {
				try {
					Date date = df2.parse(queryString);
					lsThingValues = LsThingValue.findLsThingValuesByLsKindEqualsAndDateValueLike("completion date", date).getResultList();
				} catch (Exception e2) {
					//do nothing
				}
			}
			if (!lsThingValues.isEmpty()) {
				for (LsThingValue lsThingValue : lsThingValues) {
					lsThingIdList.add(lsThingValue.getLsState().getLsThing().getId());
				}
			}
			lsThingValues.clear();
		}
		if (searchBy == "NOTEBOOK") {
			Collection<LsThingValue> lsThingValues = LsThingValue.findLsThingValuesByLsKindEqualsAndStringValueLike("notebook", queryString).getResultList();
			if (!lsThingValues.isEmpty()) {
				for (LsThingValue lsThingValue : lsThingValues) {
					lsThingIdList.add(lsThingValue.getLsState().getLsThing().getId());
				}
			}
			lsThingValues.clear();
		}

		return lsThingIdList;
	}


	@Override
	public Collection<LsThing> findLsThingsByGenericMetaDataSearch(
			String lsType, String queryString) {
		Collection<LsThing> searchResults = findLsThingsByGenericMetaDataSearch(queryString);
		if (lsType != null){
			Collection<LsThing> filteredResults = new HashSet<LsThing>();
			for (LsThing result : searchResults){
				if (result.getLsType().equals(lsType)) filteredResults.add(result);
			}
			return filteredResults;
		} else {
			return searchResults;
		}
		
	}


	@Override
	public Collection<CodeTableDTO> getCodeTableLsThings(String lsType,
			String lsKind, boolean includeIgnored) {
		Collection<LsThing> lsThings = findLsThingsByLsTypeAndLsKindAndIncludeIgnored(lsType, lsKind, includeIgnored);
		Collection<CodeTableDTO> codeTables = new HashSet<CodeTableDTO>();
		for (LsThing lsThing : lsThings){
			CodeTableDTO codeTable = new CodeTableDTO();
			codeTable.setCode(lsThing.getCodeName());
			codeTable.setName(pickBestLabel(lsThing));
			codeTable.setIgnored(lsThing.isIgnored());
			codeTables.add(codeTable);
		}
		
		return codeTables;
	}
	
	private String pickBestLabel(LsThing lsThing) {
		Collection<LsThingLabel> labels = lsThing.getLsLabels();
		if (labels.isEmpty()) return null;
		return LsThingLabel.pickBestLabel(labels).getLabelText();
	}


	@Override
	public Collection<LsThing> findLsThingsByLsTypeAndLsKindAndIncludeIgnored(String lsType, String lsKind, boolean includeIgnored){
		Collection<LsThing> searchResults = new HashSet<LsThing>();
		if (includeIgnored){
			try{
				searchResults = LsThing.findLsThingsByLsTypeAndKindEquals(lsType+"_"+lsKind).getResultList();
			} catch (EmptyResultDataAccessException e){}
		}
		else {
			try{
				searchResults = LsThing.findLsThing(lsType, lsKind).getResultList();
			} catch (EmptyResultDataAccessException e){}
		}
		return searchResults;
	}
	
	private void updateLsStates(LsThing jsonLsThing, LsThing updatedLsThing){
		if(jsonLsThing.getLsStates() != null){
			for(LsThingState lsThingState : jsonLsThing.getLsStates()){
				LsThingState updatedLsThingState;
				if (lsThingState.getId() == null){
					updatedLsThingState = new LsThingState(lsThingState);
					updatedLsThingState.setLsThing(updatedLsThing);
					updatedLsThingState.persist();
					updatedLsThing.getLsStates().add(updatedLsThingState);
				} else {
					updatedLsThingState = LsThingState.update(lsThingState);
					logger.debug("updated lsThing state " + updatedLsThingState.getId());

				}
				if (lsThingState.getLsValues() != null){
					for(LsThingValue lsThingValue : lsThingState.getLsValues()){
						LsThingValue updatedLsThingValue;
						if (lsThingValue.getId() == null){
							updatedLsThingValue = LsThingValue.create(lsThingValue);
							updatedLsThingValue.setLsState(LsThingState.findLsThingState(updatedLsThingState.getId()));
							updatedLsThingValue.persist();
							updatedLsThingState.getLsValues().add(updatedLsThingValue);
						} else {
							updatedLsThingValue = LsThingValue.update(lsThingValue);
							logger.debug("updated lsThing value " + updatedLsThingValue.getId());
						}
					}	
				} else {
					logger.debug("No lsThing values to update");
				}
			}
		}
	}
	
	private void updateItxLsStates(ItxLsThingLsThing jsonItxLsThingLsThing, ItxLsThingLsThing updatedItxLsThingLsThing){
		if(jsonItxLsThingLsThing.getLsStates() != null){
			for(ItxLsThingLsThingState itxLsThingLsThingState : jsonItxLsThingLsThing.getLsStates()){
				ItxLsThingLsThingState updatedItxLsThingLsThingState;
				if (itxLsThingLsThingState.getId() == null){
					updatedItxLsThingLsThingState = new ItxLsThingLsThingState(itxLsThingLsThingState);
					updatedItxLsThingLsThingState.setItxLsThingLsThing(updatedItxLsThingLsThing);
					updatedItxLsThingLsThingState.persist();
					updatedItxLsThingLsThing.getLsStates().add(updatedItxLsThingLsThingState);
				} else {
					updatedItxLsThingLsThingState = ItxLsThingLsThingState.update(itxLsThingLsThingState);
					updatedItxLsThingLsThingState.setItxLsThingLsThing(updatedItxLsThingLsThing);
					logger.debug("updated itxLsThingLsThing state " + updatedItxLsThingLsThingState.getId());

				}
				if (itxLsThingLsThingState.getLsValues() != null){
					for(ItxLsThingLsThingValue itxLsThingLsThingValue : itxLsThingLsThingState.getLsValues()){
						ItxLsThingLsThingValue updatedItxLsThingLsThingValue;
						if (itxLsThingLsThingValue.getId() == null){
							updatedItxLsThingLsThingValue = ItxLsThingLsThingValue.create(itxLsThingLsThingValue);
							updatedItxLsThingLsThingValue.setLsState(ItxLsThingLsThingState.findItxLsThingLsThingState(updatedItxLsThingLsThingState.getId()));
							updatedItxLsThingLsThingValue.persist();
							updatedItxLsThingLsThingState.getLsValues().add(updatedItxLsThingLsThingValue);
						} else {
							updatedItxLsThingLsThingValue = ItxLsThingLsThingValue.update(itxLsThingLsThingValue);
							updatedItxLsThingLsThingValue.setLsState(updatedItxLsThingLsThingState);
							logger.debug("updated itxLsThingLsThing value " + updatedItxLsThingLsThingValue.getId());
						}
					}	
				} else {
					logger.debug("No itxLsThingLsThing values to update");
				}
			}
		}
	}


	@Override
	@Transactional
	public Collection<LsThing> searchForDocumentThings(
			Map<String, String> searchParamsMap) {
		//make our HashSets: lsThingIdList will be filled/cleared/refilled for each term
		HashSet<Long> lsThingIdList = new HashSet<Long>();
		//allLsThingIdList aggregates all the search results, which we will then filter down by intersections
		HashSet<Long> allLsThingIdList = new HashSet<Long>();
		//lsThingList is the final search result
		Collection<LsThing> lsThingList = new HashSet<LsThing>();
		//map where key is paramName and value is list of ids found matching that param
		Map<String, HashSet<Long>> resultsByParam = new HashMap<String, HashSet<Long>>();
		searchParamsMap.remove("with");
		for (String paramName : searchParamsMap.keySet()){
			String param = searchParamsMap.get(paramName);
			logger.debug("Searching by "+paramName+" = "+param);
			lsThingIdList.addAll(findDocumentLsThingsByParam(paramName, param));
			resultsByParam.put(paramName, new HashSet<Long>(lsThingIdList));
			allLsThingIdList.addAll(lsThingIdList);
			lsThingIdList.clear();
		}
		//Here is the intersect logic
		for (String paramName: searchParamsMap.keySet()) {
			allLsThingIdList.retainAll(resultsByParam.get(paramName));
		}
		for (Long id: allLsThingIdList) lsThingList.add(LsThing.findLsThing(id));
        //This method uses finders that will find everything, whether or not it is ignored or deleted
		Collection<LsThing> result = new HashSet<LsThing>();
		for (LsThing lsThing: lsThingList) {
			//For Protocol Browser, we want to see soft deleted (ignored=true, deleted=false), but not hard deleted (ignored=deleted=true)
			if (lsThing.isDeleted()){
				logger.debug("removing a deleted lsThing from the results");
			} else {
				result.add(lsThing);
			}
		}
		return result;
	}


	private Collection<Long> findDocumentLsThingsByParam(
			String paramName, String param) {
		Collection<Long> lsThingIdList = new HashSet<Long>();
		/*
		 * List of Search params:
		 * documentCode - codeName of document LsThing
		 * documentType - LsType of document LsThing
		 * titleContains - like query on labelText of LsThingLabel of document (name_document name)
		 * project - lsThing
		 * owner - stringValue_owner in Document
		 * amountFrom - numericValue_amount in state with lsType=metadata
		 * amountTo - numericValue_amount in state with lsType=metadata
		 * createdDateFrom - recordedDate on Document LsThing (MM/dd/yyyy)
		 * createdDateTo - ???? should be dateValue in lsType = metadata
		 * active - stringValue_active in metadata state
		 * termType - another lsThing
		 * daysBefore - numericValue of TERM lsThing
		 * termDateFrom - dateValue_date of TERM lsThing
		 * termDateTo - dateValue_date of TERM lsThing
		 * missingAnnotation - ????
		 */
		if (paramName.equals("documentCode")){
			List<LsThing> lsThings = LsThing.findLsThingsByCodeNameLike(param).getResultList();
			if (!lsThings.isEmpty()){
				for (LsThing lsThing : lsThings){
					lsThingIdList.add(lsThing.getId());
				}
			}
			lsThings.clear();
		}
		if (paramName.equals("documentType")){
			List<LsThing> lsThings = LsThing.findLsThingsByLsTypeEquals(param).getResultList();
			if (!lsThings.isEmpty()){
				for (LsThing lsThing : lsThings){
					lsThingIdList.add(lsThing.getId());
				}
			}
			lsThings.clear();
		}
		if (paramName.equals("titleContains")){
			List<LsThingLabel> lsThingLabels = LsThingLabel.findLsThingLabelsByLabelTextLike(param).getResultList();
			if (!lsThingLabels.isEmpty()){
				for (LsThingLabel lsThingLabel : lsThingLabels){
					lsThingIdList.add(lsThingLabel.getLsThing().getId());
				}
			}
			lsThingLabels.clear();
		}
		if (paramName.equals("project")){
			LsThing project = LsThing.findLsThingsByCodeNameEquals(param).getSingleResult();
			List<LsThing> lsThings = LsThing.findFirstLsThingsByItxTypeKindEqualsAndSecondLsThingEquals("incorporates", "document_project", project).getResultList();
			if (!lsThings.isEmpty()){
				for (LsThing lsThing : lsThings){
					lsThingIdList.add(lsThing.getId());
				}
			}
			lsThings.clear();
			project.clear();
		}
		if (paramName.equals("owner")){
			Collection<LsThingValue> lsThingValues = LsThingValue.findLsThingValuesByStringValueLike(param).getResultList();
			if (!lsThingValues.isEmpty()){
				for (LsThingValue lsThingValue : lsThingValues) {
					lsThingIdList.add(lsThingValue.getLsState().getLsThing().getId());
				}
			}
			lsThingValues.clear();
		}
		if (paramName.equals("amountFrom")) {
			Collection<LsThingValue> lsThingValues = LsThingValue.findLsThingValuesByLsKindEqualsAndNumericValueGreaterThanEquals("amount", new BigDecimal(param)).getResultList();
			if (!lsThingValues.isEmpty()){
				for (LsThingValue lsThingValue : lsThingValues) {
					lsThingIdList.add(lsThingValue.getLsState().getLsThing().getId());
				}
			}
			lsThingValues.clear();
		}
		if (paramName.equals("amountTo")) {
			Collection<LsThingValue> lsThingValues = LsThingValue.findLsThingValuesByLsKindEqualsAndNumericValueLessThanEquals("amount", new BigDecimal(param)).getResultList();
			if (!lsThingValues.isEmpty()){
				for (LsThingValue lsThingValue : lsThingValues) {
					lsThingIdList.add(lsThingValue.getLsState().getLsThing().getId());
				}
			}
			lsThingValues.clear();
		}
		if (paramName.equals("createdDateFrom")){
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			try{
				Date date = df.parse(param);
				Collection<LsThing> lsThings = LsThing.findLsThingsByRecordedDateGreaterThan(date).getResultList();
				if (!lsThings.isEmpty()){
					for (LsThing lsThing : lsThings) {
						lsThingIdList.add(lsThing.getId());
					}
				}
				lsThings.clear();
			} catch (Exception e){
				logger.error("Error parsing date: " + param + ". Should be in format: MM/dd/yyyy");
			}
		}
		if (paramName.equals("createdDateTo")){
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			try{
				Date date = df.parse(param);
				Collection<LsThing> lsThings = LsThing.findLsThingsByRecordedDateLessThan(date).getResultList();
				if (!lsThings.isEmpty()){
					for (LsThing lsThing : lsThings) {
						lsThingIdList.add(lsThing.getId());
					}
				}
				lsThings.clear();
			} catch (Exception e){
				logger.error("Error parsing date: " + param + ". Should be in format: MM/dd/yyyy");
			}
		}
		if (paramName.equals("active")) {
			Collection<LsThingValue> lsThingValues = LsThingValue.findLsThingValuesByLsKindEqualsAndStringValueLike("active", param).getResultList();
			if (!lsThingValues.isEmpty()){
				for (LsThingValue lsThingValue : lsThingValues) {
					lsThingIdList.add(lsThingValue.getLsState().getLsThing().getId());
				}
			}
			lsThingValues.clear();
		}
		if (paramName.equals("termType")){
			LsThing termType = LsThing.findLsThingsByCodeNameEquals(param).getSingleResult();
			List<LsThing> terms = LsThing.findSecondLsThingsByItxTypeKindEqualsAndFirstLsThingEquals("classifies", "term type_term", termType).getResultList();
			if (!terms.isEmpty()){
				for (LsThing term: terms){
					List<LsThing> lsThings = LsThing.findFirstLsThingsByItxTypeKindEqualsAndSecondLsThingEquals("incorporates", "document_term", term).getResultList();
					if (!lsThings.isEmpty()){
						for (LsThing lsThing : lsThings){
							lsThingIdList.add(lsThing.getId());
						}
					}
					lsThings.clear();
				}
			}
			terms.clear();
			termType.clear();
		}
		if (paramName.equals("daysBefore")){
			Collection<LsThingValue> lsThingValues = LsThingValue.findLsThingValuesByLsKindEqualsAndNumericValueEquals("days before", new BigDecimal(param)).getResultList();
			Collection<LsThing> terms = new HashSet<LsThing>();
			if (!lsThingValues.isEmpty()){
				for(LsThingValue lsThingValue: lsThingValues){
					terms.add(lsThingValue.getLsState().getLsThing());
				}
			}
			lsThingValues.clear();
			if (!terms.isEmpty()){
				for (LsThing term: terms){
					List<LsThing> lsThings = LsThing.findFirstLsThingsByItxTypeKindEqualsAndSecondLsThingEquals("incorporates", "document_term", term).getResultList();
					if (!lsThings.isEmpty()){
						for (LsThing lsThing : lsThings){
							lsThingIdList.add(lsThing.getId());
						}
					}
					lsThings.clear();
				}
			}
			terms.clear();
		}
		if (paramName.equals("termDateFrom")){
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			try{
				Date date = df.parse(param);
				Collection<LsThingValue> lsThingValues = LsThingValue.findLsThingValuesByLsKindEqualsAndDateValueGreaterThanEquals("date",date).getResultList();
				Collection<LsThing> terms = new HashSet<LsThing>();
				if (!lsThingValues.isEmpty()){
					for(LsThingValue lsThingValue: lsThingValues){
						terms.add(lsThingValue.getLsState().getLsThing());
					}
				}
				lsThingValues.clear();
				if (!terms.isEmpty()){
					for (LsThing term: terms){
						List<LsThing> lsThings = LsThing.findFirstLsThingsByItxTypeKindEqualsAndSecondLsThingEquals("incorporates", "document_term", term).getResultList();
						if (!lsThings.isEmpty()){
							for (LsThing lsThing : lsThings){
								lsThingIdList.add(lsThing.getId());
							}
						}
						lsThings.clear();
					}
				}
				terms.clear();
			} catch (Exception e){
				logger.error("Error parsing date: " + param + ". Should be in format: MM/dd/yyyy");
			}
		}
		if (paramName.equals("termDateTo")){
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			try{
				Date date = df.parse(param);
				Collection<LsThingValue> lsThingValues = LsThingValue.findLsThingValuesByLsKindEqualsAndDateValueLessThanEquals("date",date).getResultList();
				Collection<LsThing> terms = new HashSet<LsThing>();
				if (!lsThingValues.isEmpty()){
					for(LsThingValue lsThingValue: lsThingValues){
						terms.add(lsThingValue.getLsState().getLsThing());
					}
				}
				lsThingValues.clear();
				if (!terms.isEmpty()){
					for (LsThing term: terms){
						List<LsThing> lsThings = LsThing.findFirstLsThingsByItxTypeKindEqualsAndSecondLsThingEquals("incorporates", "document_term", term).getResultList();
						if (!lsThings.isEmpty()){
							for (LsThing lsThing : lsThings){
								lsThingIdList.add(lsThing.getId());
							}
						}
						lsThings.clear();
					}
				}
				terms.clear();
			} catch (Exception e){
				logger.error("Error parsing date: " + param + ". Should be in format: MM/dd/yyyy");
			}		}
		if (paramName.equals("missingAnnotation")){
			//TODO: figure out what this is, then code it
		}
		
		return lsThingIdList;
	}


	@Override
	public ArrayList<ErrorMessage> validateLsThing(LsThing lsThing,
			boolean checkUniqueName, boolean checkUniqueInteractions,
			boolean checkOrderMatters, boolean checkForwardAndReverseAreSame) {
		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		if (checkUniqueName){
			try{
				checkLsThingUniqueName(lsThing);
			} catch (UniqueNameException e){
				logger.error("Caught UniqueNameException validating LsThing: " + e.getMessage().toString() + " whole message  " + e.toString());
	            ErrorMessage error = new ErrorMessage();
	            error.setErrorLevel("error");
	            error.setMessage(e.getMessage());
	            errors.add(error);
			}
		}
		if (checkUniqueInteractions){
			try{
				checkLsThingUniqueInteractions(lsThing, checkOrderMatters, checkForwardAndReverseAreSame);
			} catch (UniqueInteractionsException e){
				logger.error("Caught UniqueInteractionsException validating LsThing: " + e.getMessage().toString() + " whole message  " + e.toString());
	            ErrorMessage error = new ErrorMessage();
	            error.setErrorLevel("error");
	            error.setMessage(e.getMessage());
	            errors.add(error);
			}
		}
		
		return errors;
	}


	private void checkLsThingUniqueInteractions(LsThing lsThing,
			boolean checkOrderMatters, boolean checkForwardAndReverseAreSame) throws UniqueInteractionsException {
		Set<ItxLsThingLsThing> secondItxLsThings = lsThing.getFirstLsThings();
		if (!checkOrderMatters){
			//order doesn't matter. We're just checking for a unique set of "incorporates" interactions
			HashSet<LsThing> foundLsThings = null;
			for (ItxLsThingLsThing secondItxLsThing : secondItxLsThings){
				LsThing secondLsThing = secondItxLsThing.getSecondLsThing();
				String lsKind = secondItxLsThing.getLsKind();
				Collection<LsThing> foundFirstLsThings = LsThing.findFirstLsThingsByItxTypeKindEqualsAndSecondLsThingEquals("incorporates", lsKind, secondLsThing).getResultList();
				//look for "firstLsThings" that are like the one we're validating, i.e. those that have an "incorporates" interaction to the same secondLsThing
				if (foundLsThings == null){
					//on the second one, instantiate the HashSet, then add all the foundFirstLsThings.
					foundLsThings = new HashSet<LsThing>();
					foundLsThings.addAll(foundFirstLsThings);
				} else{
					//otherwise, filter the existing list to be the intersection of the results of the most recent query with the previous results
					foundLsThings.retainAll(foundFirstLsThings);
				}
			}
			if (foundLsThings != null && !foundLsThings.isEmpty()){
				//if anything remains, it was found for every interaction so it is a duplicate
				throw new UniqueInteractionsException("Found existing LsThing with identical set of interactions");
			}
		} else{
			//order matters. for each interaction, we will grab the order, then search using it.
			HashSet<LsThing> foundLsThings = null;
			for (ItxLsThingLsThing secondItxLsThing : secondItxLsThings){
				LsThing secondLsThing = secondItxLsThing.getSecondLsThing();
				String lsKind = secondItxLsThing.getLsKind();
				int order = secondItxLsThing.grabItxOrder();
				Collection<LsThing> foundFirstLsThings = LsThing.findFirstLsThingsByItxTypeKindEqualsAndSecondLsThingEqualsAndOrderEquals("incorporates", lsKind, secondLsThing, order).getResultList();
				if (foundLsThings == null){
					foundLsThings = new HashSet<LsThing>();
					foundLsThings.addAll(foundFirstLsThings);
				} else{
					foundLsThings.retainAll(foundFirstLsThings);
				}
			}
			if (foundLsThings != null && !foundLsThings.isEmpty()){
				//if anything remains, it was found for every interaction so it is a duplicate
				throw new UniqueInteractionsException("Found existing LsThing with identical set of interactions with same order");
			}
			if (checkForwardAndReverseAreSame){
				//if we need to check backwards as well, by this point we have already passed "forwards validation"
				//we need to get the total number of "incorporates" interactions
				ArrayList<ItxLsThingLsThing> orderedIncorporatesInteractions = new ArrayList<ItxLsThingLsThing>();
				for (ItxLsThingLsThing secondItxLsThing : secondItxLsThings){
					if (secondItxLsThing.getLsType().equals("incorporates")){
						orderedIncorporatesInteractions.add(secondItxLsThing);
					}
				}
				//then we sort
				Collections.sort(orderedIncorporatesInteractions, new ItxLsThingLsThingComparator());
				//then do the same search as above, but with new "order" parameters:
				int order = 1;
				for (ItxLsThingLsThing secondItxLsThing : orderedIncorporatesInteractions){
					LsThing secondLsThing = secondItxLsThing.getSecondLsThing();
					String lsKind = secondItxLsThing.getLsKind();
					Collection<LsThing> foundFirstLsThings = LsThing.findFirstLsThingsByItxTypeKindEqualsAndSecondLsThingEqualsAndOrderEquals("incorporates", lsKind, secondLsThing, order).getResultList();
					if (foundLsThings == null){
						foundLsThings = new HashSet<LsThing>();
						foundLsThings.addAll(foundFirstLsThings);
					} else{
						foundLsThings.retainAll(foundFirstLsThings);
					}
					order++;
				}
				if (foundLsThings != null && !foundLsThings.isEmpty()){
					//if anything remains, it was found for every interaction so it is a duplicate
					throw new UniqueInteractionsException("Found existing LsThing with identical set of interactions with same order");
				}
			}
		}
	}


	private void checkLsThingUniqueName(LsThing lsThing) throws UniqueNameException{
		String lsKind = lsThing.getLsKind();
		Set<LsThingLabel> lsThingLabels = lsThing.getLsLabels();
		for (LsThingLabel lsThingLabel : lsThingLabels){
			if (!lsThingLabel.isIgnored()){
				String labelText = lsThingLabel.getLabelText();
				Collection<LsThing> foundLsThings = new HashSet<LsThing>();
				try{
					foundLsThings = LsThing.findLsThingByLabelTextAndLsKind(labelText, lsKind).getResultList();
				} catch (EmptyResultDataAccessException e){
					//found nothing
				}
				if (!foundLsThings.isEmpty()){
					for (LsThing foundLsThing: foundLsThings){
						if (lsThing.getId() == null || lsThing.getId().compareTo(foundLsThing.getId()) != 0){
							//we found an lsThing that is not the same as the one being validated that has the same label
							throw new UniqueNameException("LsThing with lsKind "+lsKind+" and with the name "+labelText+" already exists! "+foundLsThing.getCodeName());
						}
					}
				}
			}	
		}
		
	}
	
	@Override
	public Collection<LsThing> sortLsThingsByCodeName(Collection<LsThing> lsThings) {
		List<LsThing> listLsThings = new ArrayList<LsThing>(lsThings);
		Collections.sort(listLsThings, new LsThingComparatorByCodeName());
		return listLsThings;
	}


	@Override
	public Collection<LsThing> sortBatches(Collection<LsThing> lsThings) {
		List<LsThing> listLsThings = new ArrayList<LsThing>(lsThings);
		Collections.sort(listLsThings, new LsThingComparatorByBatchNumber());
		return listLsThings;
	}
}
