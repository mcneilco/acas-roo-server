package com.labsynch.labseer.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.map.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.DependencyCheckDTO;
import com.labsynch.labseer.dto.ErrorMessageDTO;
import com.labsynch.labseer.dto.LsThingValidationDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.ValuePathDTO;
import com.labsynch.labseer.dto.ValueRuleDTO;
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
	public PreferredNameResultsDTO getCodeNameFromName(String thingType, String thingKind, String labelType, String labelKind, String json){

		PreferredNameRequestDTO requestDTO = PreferredNameRequestDTO.fromJsonToPreferredNameRequestDTO(json);	
		logger.info("number of requests: " + requestDTO.getRequests().size());
		Collection<PreferredNameDTO> requests = requestDTO.getRequests();

		PreferredNameResultsDTO responseOutput = new PreferredNameResultsDTO();
		Collection<ErrorMessageDTO> errors = new HashSet<ErrorMessageDTO>();

		for (PreferredNameDTO request : requests){
			request.setPreferredName("");
			request.setReferenceName("");
			List<LsThing> lsThings = LsThing.findLsThingByLabelText(thingType, thingKind, labelType, labelKind, request.getRequestName()).getResultList();
			if (lsThings.size() == 1){
				request.setPreferredName(lsThings.get(0).getCodeName());
				request.setReferenceName(lsThings.get(0).getCodeName());
			} else if (lsThings.size() > 1){
				responseOutput.setError(true);
				ErrorMessageDTO error = new ErrorMessageDTO();
				error.setLevel("error");
				error.setMessage("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName() );	
				logger.error("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName());
				errors.add(error);
			} else {
				logger.info("Did not find a LS_THING WITH THE REQUESTED NAME: " + request.getRequestName());
			}
		}
		responseOutput.setResults(requests);
		responseOutput.setErrorMessages(errors);

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
				error.setLevel("MULTIPLE RESULTS");
				error.setMessage("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName() );	
				logger.error("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName());
				errors.add(error);
			} else {
				try{
					LsThing codeNameMatch = LsThing.findLsThingsByCodeNameEquals(request.getRequestName()).getSingleResult();
					if (codeNameMatch.getLsKind().equals(thingKind) && codeNameMatch.getLsType().equals(thingType)){
						logger.info("Made it to the codeMatch");
						request.setPreferredName(pickBestLabel(codeNameMatch));
	 					request.setReferenceName(codeNameMatch.getCodeName());
					}else{
						logger.info("Did not find a LS_THING WITH THE REQUESTED NAME: " + request.getRequestName());
					}
				}catch (EmptyResultDataAccessException e){
					logger.info("Did not find a LS_THING WITH THE REQUESTED NAME: " + request.getRequestName());
				}
			}
		}
		responseOutput.setResults(requests);
		responseOutput.setErrorMessages(errors);

		return responseOutput;
	}
	
	
	public PreferredNameResultsDTO getCodeNameFromName(PreferredNameRequestDTO requestDTO){

		logger.info("number of requests: " + requestDTO.getRequests().size());
		Collection<PreferredNameDTO> requests = requestDTO.getRequests();

		PreferredNameResultsDTO responseOutput = new PreferredNameResultsDTO();
		Collection<ErrorMessageDTO> errors = new HashSet<ErrorMessageDTO>();

		for (PreferredNameDTO request : requests){
			request.setPreferredName("");
			request.setReferenceName("");
			List<LsThing> lsThings = LsThing.findLsThingByLabelText(request.getRequestName()).getResultList();
			if (lsThings.size() == 1){
				request.setPreferredName(lsThings.get(0).getCodeName());
				request.setReferenceName(lsThings.get(0).getCodeName());
			} else if (lsThings.size() > 1){
				responseOutput.setError(true);
				ErrorMessageDTO error = new ErrorMessageDTO();
				error.setLevel("error");
				error.setMessage("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName() );	
				logger.error("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName());
				errors.add(error);
			} else {
				logger.info("Did not find a LS_THING WITH THE REQUESTED NAME: " + request.getRequestName());
			}
		}
		responseOutput.setResults(requests);
		responseOutput.setErrorMessages(errors);

		return responseOutput;
	}


	@Override
	public PreferredNameResultsDTO getPreferredNameFromName(String thingType, String thingKind, String labelType, String labelKind, String json){

		logger.info("in getPreferredNameFromName with input json");

		PreferredNameRequestDTO requestDTO = PreferredNameRequestDTO.fromJsonToPreferredNameRequestDTO(json);	

		logger.info("number of requests: " + requestDTO.getRequests().size());

		List<PreferredNameDTO> requests = (List<PreferredNameDTO>) requestDTO.getRequests();

		PreferredNameResultsDTO responseOutput = new PreferredNameResultsDTO();
		Collection<ErrorMessageDTO> errors = new HashSet<ErrorMessageDTO>();

		List<String> requestNameList = new ArrayList<String>();
		for (PreferredNameDTO request : requests){
			requestNameList.add(request.getRequestName());
		}

		List<PreferredNameDTO> lsThingLabelsList = new ArrayList<PreferredNameDTO>();	
		int batchSize = 999;
		int i = 1;
		List<String> requestNamesSubset = new ArrayList<String>();
		List<PreferredNameDTO> lsThingLabelsFound;
		for (String requestName : requestNameList){
//			requestNamesSubset.add(requestName);
//			List<PreferredNameDTO> lsThingLabels = LsThingLabel.findLsThingPreferredName(thingType, thingKind, labelType, labelKind, requestNamesSubset).getResultList();
//			lsThingLabelsList.addAll(lsThingLabels);
//			requestNamesSubset.clear();

			requestNamesSubset.add(requestName);
			if (i % batchSize  == 0 ) { 
				lsThingLabelsFound = LsThingLabel.findLsThingPreferredName(thingType, thingKind, labelType, labelKind, requestNamesSubset).getResultList();
				lsThingLabelsList.addAll(lsThingLabelsFound);
				requestNamesSubset.clear();
				logger.debug("searching batch of names " + i);
			}
			i++;			
			
		}
		
		if (requestNamesSubset.size() > 0){
			lsThingLabelsFound = LsThingLabel.findLsThingPreferredName(thingType, thingKind, labelType, labelKind, requestNamesSubset).getResultList();
			lsThingLabelsList.addAll(lsThingLabelsFound);
			logger.debug("searching last batch of names " + requestNamesSubset.size());

		}

		
		logger.info("number of thing labels found: " + lsThingLabelsList.size());
		for (PreferredNameDTO preferredName : lsThingLabelsList){
			logger.info(preferredName.toJson());
		}
		
		MultiValueMap mvm = new MultiValueMap();
		for (PreferredNameDTO pn : lsThingLabelsList){
			mvm.put(pn.getRequestName(), pn);
		}

		for (PreferredNameDTO request : requests){
			request.setPreferredName("");
			request.setReferenceName("");
			@SuppressWarnings("unchecked")
			List<PreferredNameDTO> lsThingLabels = (List<PreferredNameDTO>) mvm.get(request.getRequestName());

			if (lsThingLabels != null && lsThingLabels.size() == 1){
				request.setPreferredName(lsThingLabels.get(0).getPreferredName());
				request.setReferenceName(lsThingLabels.get(0).getReferenceName());
			} else if (lsThingLabels != null && lsThingLabels.size() > 1){
				responseOutput.setError(true);
				ErrorMessageDTO error = new ErrorMessageDTO();
				error.setLevel("MULTIPLE RESULTS");
				error.setMessage("FOUND MULTIPLE LS_THINGS WITH THE SAME NAME: " + request.getRequestName() );	
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
	
	public PreferredNameResultsDTO getPreferredNameFromName(String json){

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
		
		

		List<PreferredNameDTO> lsThingLabelsList =LsThingLabel.findLsThingPreferredName(requestNameList).getResultList();
		
	
		
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
				error.setLevel("error");
				error.setMessage("FOUND MULTIPLE LS_THINGS WITH THE SAME NAME: " + request.getRequestName() );	
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

		logger.info("in getPreferredNameFromName with PreferredNameRequestDTO");

		logger.info("number of requests: " + requestDTO.getRequests().size());

		Collection<PreferredNameDTO> requests = requestDTO.getRequests();
		PreferredNameResultsDTO responseOutput = new PreferredNameResultsDTO();
		Collection<ErrorMessageDTO> errors = new HashSet<ErrorMessageDTO>();

		List<String> requestNameList = new ArrayList<String>();
		for (PreferredNameDTO request : requests){
			requestNameList.add(request.getRequestName());
		}

		List<PreferredNameDTO> lsThingLabelsList = new ArrayList<PreferredNameDTO>();	
		int batchSize = 999;
		int i = 1;
		List<String> requestNamesSubset = new ArrayList<String>();
		for (String requestName : requestNameList){
			requestNamesSubset.add(requestName);
			if (i % batchSize  == 0 ) { 
				List<PreferredNameDTO> lsThingLabels = LsThingLabel.findLsThingPreferredName(thingType, thingKind, labelType, labelKind, requestNamesSubset).getResultList();
				lsThingLabelsList.addAll(lsThingLabels);
				requestNamesSubset.clear();
				logger.debug("searching batch of names " + i);

			}
			i++;			
			
		}
		
		if (requestNamesSubset.size() > 0){
			List<PreferredNameDTO> lsThingLabels = LsThingLabel.findLsThingPreferredName(thingType, thingKind, labelType, labelKind, requestNamesSubset).getResultList();
			lsThingLabelsList.addAll(lsThingLabels);
			logger.debug("searching last batch of names " + requestNamesSubset.size());

		}				
		logger.info("number of thing labels found: " + lsThingLabelsList.size());
		
		MultiValueMap mvm = new MultiValueMap();
		for (PreferredNameDTO pn : lsThingLabelsList){
			mvm.put(pn.getRequestName(), pn);
		}

		for (PreferredNameDTO request : requests){
			request.setPreferredName("");
			request.setReferenceName("");
			@SuppressWarnings("unchecked")
			List<PreferredNameDTO> lsThingLabels = (List<PreferredNameDTO>) mvm.get(request.getRequestName());

			if (lsThingLabels != null && lsThingLabels.size() == 1){
				request.setPreferredName(lsThingLabels.get(0).getPreferredName());
				request.setReferenceName(lsThingLabels.get(0).getReferenceName());
			} else if (lsThingLabels != null && lsThingLabels.size() > 1){
				responseOutput.setError(true);
				ErrorMessageDTO error = new ErrorMessageDTO();
				error.setLevel("MULTIPLE RESULTS");
				error.setMessage("FOUND MULTIPLE LS_THINGS WITH THE SAME NAME: " + request.getRequestName() );	
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
	
	public PreferredNameResultsDTO getPreferredNameFromName(PreferredNameRequestDTO requestDTO){

		logger.info("in getPreferredNameFromName -- right route");

		logger.info("number of requests: " + requestDTO.getRequests().size());

		Collection<PreferredNameDTO> requests = requestDTO.getRequests();

		PreferredNameResultsDTO responseOutput = new PreferredNameResultsDTO();
		Collection<ErrorMessageDTO> errors = new HashSet<ErrorMessageDTO>();

		Set<String> requestNameList = new HashSet<String>();
		for (PreferredNameDTO request : requests){
			requestNameList.add(request.getRequestName());
		}

		logger.info("about to run hybernate query");
		List<PreferredNameDTO> lsThingLabelsList = LsThingLabel.findLsThingPreferredName(requestNameList).getResultList();
		logger.info("returned from hybernate query");
		
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
//				responseOutput.setError(true);
//				ErrorMessageDTO error = new ErrorMessageDTO();
//				error.setErrorCode("MULTIPLE RESULTS");
//				error.setErrorMessage("FOUND MULTIPLE LS_THINGS WITH THE SAME NAME: " + request.getRequestName() );	
				logger.info("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME, RETURNING ONLY THE FIRST: " + request.getRequestName());
//				errors.add(error);
				request.setPreferredName(lsThingLabels.get(0).getPreferredName());
				request.setReferenceName(lsThingLabels.get(0).getReferenceName());
				
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
		LsThing updatedLsThing = LsThing.update(jsonLsThing);
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
					updatedLsThing.getLsLabels().add(updatedLabel);
					logger.debug("updated lsThing label " + updatedLabel.getId());
				}
			}			
		} else {
			logger.debug("No lsThing labels to update");
		}
		updateLsStates(jsonLsThing, updatedLsThing);
		
		//updated itx and nested LsThings
		// assume that the client may not send all of the nested data but wants all of the nested data back 
		
		Set<ItxLsThingLsThing> firstLsThings = new HashSet<ItxLsThingLsThing>();
		firstLsThings.addAll(updatedLsThing.getFirstLsThings());
		logger.debug("found number of first interactions: " + firstLsThings.size());
		
		if(jsonLsThing.getFirstLsThings() != null){
			//there are itx's
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
					updatedItxLsThingLsThing = ItxLsThingLsThing.update(itxLsThingLsThing);
					updateItxLsStates(itxLsThingLsThing, updatedItxLsThingLsThing);
					firstLsThings.add(updatedItxLsThingLsThing);
				}
			}
			updatedLsThing.setFirstLsThings(firstLsThings);
		}
		
		Set<ItxLsThingLsThing> secondLsThings = new HashSet<ItxLsThingLsThing>();
		secondLsThings.addAll(updatedLsThing.getSecondLsThings());
//		secondLsThings.addAll(ItxLsThingLsThing.findItxLsThingLsThingsBySecondLsThing(updatedLsThing).getResultList());
		logger.debug("found number of second interactions: " + secondLsThings.size());

		
		if(jsonLsThing.getSecondLsThings() != null){
			//there are itx's
			for (ItxLsThingLsThing itxLsThingLsThing : jsonLsThing.getSecondLsThings()){
				logger.debug("updating itxLsThingLsThing");
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
		int batchNumber = getNextBatchNumber(parent);
		String batchCodeName = parentCodeName.concat("-"+ String.valueOf(batchNumber));
		return batchCodeName;
	}


	@Override
	public int getBatchNumber(LsThing parent) {
		LsThingValue batchNumberValue = LsThingValue.findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(parent.getId(), "metadata", parent.getLsKind() + " " + parent.getLsType(), "numericValue", "batch number").getSingleResult();
		int batchNumber = batchNumberValue.getNumericValue().intValue();
		return batchNumber;
	}
	
	private int getNextBatchNumber(LsThing parent) {
		LsThingValue batchNumberValue = LsThingValue.findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(parent.getId(), "metadata", parent.getLsKind() + " " + parent.getLsType(), "numericValue", "batch number").getSingleResult();
		int batchNumber = batchNumberValue.getNumericValue().intValue();
		batchNumber += 1;
		batchNumberValue.setNumericValue(new BigDecimal(batchNumber));
		batchNumberValue.merge();
		return batchNumber;
	}
	
	private int decrementBatchNumber(LsThing parent) {
		LsThingValue batchNumberValue = LsThingValue.findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(parent.getId(), "metadata", parent.getLsKind() + " " + parent.getLsType(), "numericValue", "batch number").getSingleResult();
		int batchNumber = batchNumberValue.getNumericValue().intValue();
		batchNumber -= 1;
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
			String searchQuery) {
		return findLsThingsByGenericMetaDataSearch(searchQuery, null);
	}

	@Override
	public Collection<LsThing> findLsThingsByGenericMetaDataSearch(
			String queryString, String lsType){
		List<Long> lsThingIdList = new ArrayList<Long>();
		queryString = queryString.replaceAll("\\*", "%");
		List<String> splitQuery = SimpleUtil.splitSearchString(queryString);
		logger.debug("Number of search terms: " + splitQuery.size());
		EntityManager em = LsThing.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
		Root<LsThing> lsThingRoot = criteria.from(LsThing.class);
//		Join<LsThing, ItxLsThingLsThing> lsThingFirstItx = lsThingRoot.join("firstLsThings", JoinType.LEFT);
//		Join<ItxLsThingLsThing, LsThing> lsThingFirstLsThing = lsThingRoot.join("firstLsThings", JoinType.LEFT).join("firstLsThing", JoinType.LEFT);
		Join<LsThing, ItxLsThingLsThing> lsThingSecondItx = lsThingRoot.join("secondLsThings", JoinType.LEFT);
		Join<ItxLsThingLsThing, LsThing> lsThingSecondLsThing = lsThingRoot.join("secondLsThings", JoinType.LEFT).join("secondLsThing", JoinType.LEFT);
		Join<LsThing, LsThingLabel> lsThingSecondLsThingLabel = lsThingRoot.join("secondLsThings", JoinType.LEFT).join("secondLsThing", JoinType.LEFT).join("lsLabels", JoinType.LEFT);
		Join<LsThing, LsThingState> lsThingState = lsThingRoot.join("lsStates", JoinType.LEFT);
		Join<LsThingState, LsThingValue> lsThingValue = lsThingRoot.join("lsStates", JoinType.LEFT).join("lsValues", JoinType.LEFT);
		
		criteria.select(lsThingRoot.<Long>get("id"));
		criteria.distinct(true);
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		if (lsType != null && lsType.length() > 0){
			Predicate predicate = criteriaBuilder.equal(lsThingRoot.<String>get("lsType"), lsType);
			predicateList.add(predicate);
		}
		for (String term : splitQuery){
			Predicate[] predicatesByTerm = new Predicate[0];
			List<Predicate> predicateListByTerm = new ArrayList<Predicate>();
			
			//Reusable predicates
			Predicate lsThingValueNotIgnored = criteriaBuilder.not(lsThingValue.<Boolean>get("ignored"));
			Predicate lsThingStateNotIgnored = criteriaBuilder.not(lsThingState.<Boolean>get("ignored"));
			
			//CodeName
			Predicate codeNamePredicate = criteriaBuilder.like(lsThingRoot.<String>get("codeName"), term);
			predicateListByTerm.add(codeNamePredicate);
			
			//parent name
			Predicate parentNameItxTypePredicate = criteriaBuilder.equal(lsThingSecondItx.<String>get("lsType"), "instantiates");
			Predicate parentNameItxKindPredicate = criteriaBuilder.equal(lsThingSecondItx.<String>get("lsKind"), "batch_parent");
			Predicate parentNameLabelPredicate = criteriaBuilder.equal(lsThingSecondLsThingLabel.<String>get("labelText"), term);
			Predicate lsThingSecondItxNotIgnored = criteriaBuilder.not(lsThingSecondItx.<Boolean>get("ignored"));
			Predicate lsThingSecondLsThingNotIgnored = criteriaBuilder.not(lsThingSecondLsThing.<Boolean>get("ignored"));
			Predicate lsThingSecondLsThingLabelNotIgnored = criteriaBuilder.not(lsThingSecondLsThingLabel.<Boolean>get("ignored"));
			Predicate parentNamePredicate = criteriaBuilder.and(parentNameItxTypePredicate, 
					lsThingSecondItxNotIgnored, 
					parentNameItxKindPredicate,
					lsThingSecondLsThingNotIgnored,
					parentNameLabelPredicate,
					lsThingSecondLsThingLabelNotIgnored);
			predicateListByTerm.add(parentNamePredicate);
			
			//recordedby
			Predicate recordedByPredicate = criteriaBuilder.like(lsThingRoot.<String>get("recordedBy"), term);
			predicateListByTerm.add(recordedByPredicate);
			
			//scientist
			Predicate scientistPredicate1 = criteriaBuilder.like(lsThingValue.<String>get("codeValue"), term);
			Predicate scientistPredicate2 = criteriaBuilder.equal(lsThingValue.<String>get("lsKind"), "scientist");
			Predicate scientistPredicate = criteriaBuilder.and(scientistPredicate1, scientistPredicate2, lsThingValueNotIgnored, lsThingStateNotIgnored);
			predicateListByTerm.add(scientistPredicate);
			
			//lskind
			Predicate lsKindPredicate = criteriaBuilder.equal(lsThingRoot.<String>get("lsKind"), term);
			predicateListByTerm.add(lsKindPredicate);
			
			//date
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
			try {
				Date date = df.parse(queryString);
				Date beforeDate = new Date(date.getTime());
				beforeDate.setHours(0);
				beforeDate.setMinutes(0);
				beforeDate.setSeconds(0);
				Date afterDate = new Date(date.getTime());
				afterDate.setDate(afterDate.getDate()+1);
				afterDate.setHours(0);
				afterDate.setMinutes(0);
				afterDate.setSeconds(0);
				Predicate datePredicate1 = criteriaBuilder.between(lsThingValue.<Date>get("dateValue"), beforeDate, afterDate);
				Predicate datePredicate2 = criteriaBuilder.equal(lsThingValue.<String>get("lsKind"), "completion date");
				Predicate datePredicate = criteriaBuilder.and(datePredicate1, datePredicate2, lsThingValueNotIgnored, lsThingStateNotIgnored);
				predicateListByTerm.add(datePredicate);
			} catch (Exception e) {
				try {
					Date date = df2.parse(queryString);
					Date beforeDate = new Date(date.getTime());
					beforeDate.setHours(0);
					beforeDate.setMinutes(0);
					beforeDate.setSeconds(0);
					Date afterDate = new Date(date.getTime());
					afterDate.setDate(afterDate.getDate()+1);
					afterDate.setHours(0);
					afterDate.setMinutes(0);
					afterDate.setSeconds(0);
					Predicate datePredicate1 = criteriaBuilder.between(lsThingValue.<Date>get("dateValue"), beforeDate, afterDate);
					Predicate datePredicate2 = criteriaBuilder.equal(lsThingValue.<String>get("lsKind"), "completion date");
					Predicate datePredicate = criteriaBuilder.and(datePredicate1, datePredicate2, lsThingValueNotIgnored, lsThingStateNotIgnored);
					predicateListByTerm.add(datePredicate);
				} catch (Exception e2) {
					//do nothing
				}
			}
			
			//notebook
			Predicate notebookPredicate1 = criteriaBuilder.like(lsThingValue.<String>get("stringValue"), term);
			Predicate notebookPredicate2 = criteriaBuilder.equal(lsThingValue.<String>get("lsKind"), "notebook");
			Predicate notebookPredicate = criteriaBuilder.and(notebookPredicate1, notebookPredicate2, lsThingValueNotIgnored, lsThingStateNotIgnored);
			predicateListByTerm.add(notebookPredicate);
			
			//join all the predicatesByTerm with OR
			predicatesByTerm = predicateListByTerm.toArray(predicatesByTerm);
			predicateList.add(criteriaBuilder.or(predicatesByTerm));
		}
		//make sure lsThing is not ignored. All of the other layers of not ignored are in predicates above
		Predicate lsThingNotIgnored = criteriaBuilder.not(lsThingRoot.<Boolean>get("ignored"));		
		predicateList.add(lsThingNotIgnored);

		
		predicates = predicateList.toArray(predicates);
		criteria.where(criteriaBuilder.and(predicates));
		TypedQuery<Long> q = em.createQuery(criteria);
		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		lsThingIdList = q.getResultList();
		logger.debug("Found "+lsThingIdList.size()+" results.");
		Collection<LsThing> result = new HashSet<LsThing>();
		for (Long lsThingId: lsThingIdList) {
			LsThing lsThing = LsThing.findLsThing(lsThingId);
			//For LsThing Browser, we want to see soft deleted (ignored=true, deleted=false), but not hard deleted (ignored=deleted=true)
			if (lsThing.isDeleted()){
				logger.debug("removing a deleted lsThing from the results");
			} else {
				//Inject parent preferred label to all batch lsThings
				if (lsThing.getLsType().equals("batch")){
					LsThingLabel bestParentLabel = findParentByBatchEquals(lsThing).pickBestName();
					lsThing.getLsLabels().add(bestParentLabel);
				}
				result.add(lsThing);
			}
		}
		return result;
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
	
	public void updateLsStates(LsThing jsonLsThing, LsThing updatedLsThing){
		if(jsonLsThing.getLsStates() != null){
			for(LsThingState lsThingState : jsonLsThing.getLsStates()){
				LsThingState updatedLsThingState;
				if (lsThingState.getId() == null){
					updatedLsThingState = new LsThingState(lsThingState);
					updatedLsThingState.setLsThing(updatedLsThing);
					updatedLsThingState.persist();
					updatedLsThing.getLsStates().add(updatedLsThingState);
					logger.debug("persisted new lsThing state " + updatedLsThingState.getId());

				} else {
					updatedLsThingState = LsThingState.update(lsThingState);
					updatedLsThing.getLsStates().add(updatedLsThingState);

					logger.debug("updated lsThing state " + updatedLsThingState.getId());

				}
				if (lsThingState.getLsValues() != null){
					for(LsThingValue lsThingValue : lsThingState.getLsValues()){
						if (lsThingValue.getLsState() == null) lsThingValue.setLsState(updatedLsThingState);
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
						logger.debug("checking lsThingValue " + updatedLsThingValue.toJson());

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
					updatedItxLsThingLsThingState.merge();
					updatedItxLsThingLsThing.getLsStates().add(updatedItxLsThingLsThingState);
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
							updatedItxLsThingLsThingValue.merge();
							updatedItxLsThingLsThingState.getLsValues().add(updatedItxLsThingLsThingValue);
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
	public Collection<LsThing> searchForDocumentThings(Map<String, String> searchParamsMap){
		List<Long> lsThingIdList = new ArrayList<Long>();
		EntityManager em = LsThing.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
		Root<LsThing> document = criteria.from(LsThing.class);
		Join<LsThing, LsThingState> documentState = document.join("lsStates", JoinType.LEFT);
		Join<LsThing, LsThingLabel> documentLabel = document.join("lsLabels", JoinType.LEFT);
		
		criteria.select(document.<Long>get("id"));
		criteria.distinct(true);
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		
		//always present predicates
		Predicate documentType = criteriaBuilder.equal(document.<String>get("lsType"), "legalDocument");
		Predicate documentNotIgnored = criteriaBuilder.not(document.<Boolean>get("ignored"));
		Predicate documentTypePredicate = criteriaBuilder.and(documentType, documentNotIgnored);
		predicateList.add(documentTypePredicate);
		//Reusable predicates
		Predicate documentStateNotIgnored = criteriaBuilder.not(documentState.<Boolean>get("ignored"));
//		Predicate lsThingValueNotIgnored = criteriaBuilder.not(lsThingValue.<Boolean>get("ignored"));
//		Predicate lsThingStateNotIgnored = criteriaBuilder.not(lsThingState.<Boolean>get("ignored"));
		
		//documentCode : LsThing CodeName LIKE
		if (searchParamsMap.keySet().contains("documentCode")){
			Predicate documentCode = criteriaBuilder.like(document.<String>get("codeName"), "%"+searchParamsMap.get("documentCode")+"%");
			predicateList.add(documentCode);
		}
		//documentType : LsThing LsKind EQUALS
		if (searchParamsMap.keySet().contains("documentType")){
			Predicate documentKind = criteriaBuilder.equal(document.<String>get("lsKind"), searchParamsMap.get("documentType"));
			predicateList.add(documentKind);
		}
		//titleContains : LsThingLabel LabelText LIKE
		if (searchParamsMap.keySet().contains("titleContains")){
			Predicate titleContains = criteriaBuilder.like(documentLabel.<String>get("labelText"), "%"+searchParamsMap.get("titleContains")+"%");
			Predicate documentLabelNotIgnored = criteriaBuilder.not(documentLabel.<Boolean>get("ignored"));
			Predicate titleContainsPredicate = criteriaBuilder.and(titleContains, documentLabelNotIgnored);
			predicateList.add(titleContainsPredicate);
		}
		//company : SecondLsThingsItx type/kind EQUALS "incorporates"/"documentCompany", SecondLsThings.SecondLsThing codeName EQUALS
		if (searchParamsMap.keySet().contains("company")){
			Join<LsThing, ItxLsThingLsThing> companyItx = document.join("secondLsThings", JoinType.LEFT);
			Join<ItxLsThingLsThing, LsThing> company = companyItx.join("secondLsThing", JoinType.LEFT);
			Predicate companyItxType = criteriaBuilder.equal(companyItx.<String>get("lsType"), "incorporates");
			Predicate companyItxKind = criteriaBuilder.equal(companyItx.<String>get("lsKind"), "documentCompany");
			Predicate companyCode = criteriaBuilder.equal(company.<String>get("codeName"), searchParamsMap.get("company"));
			Predicate companyItxNotIgnored = criteriaBuilder.not(companyItx.<Boolean>get("ignored"));
			Predicate companyNotIgnored = criteriaBuilder.not(company.<Boolean>get("ignored"));
			Predicate companyPredicate = criteriaBuilder.and(companyItxType, companyItxKind, companyCode, companyItxNotIgnored, companyNotIgnored);
			predicateList.add(companyPredicate);
		}
		//project : SecondLsThingsItx type/kind EQUALS "incorporates"/"documentProject", SecondLsThings.SecondLsThing codeName EQUALS
		if (searchParamsMap.keySet().contains("project")){
			Join<LsThing, ItxLsThingLsThing> projectItx = document.join("secondLsThings", JoinType.LEFT);
			Join<ItxLsThingLsThing, LsThing> project = projectItx.join("secondLsThing", JoinType.LEFT);
			Predicate projectItxType = criteriaBuilder.equal(projectItx.<String>get("lsType"), "incorporates");
			Predicate projectItxKind = criteriaBuilder.equal(projectItx.<String>get("lsKind"), "documentProject");
			Predicate projectCode = criteriaBuilder.equal(project.<String>get("codeName"), searchParamsMap.get("project"));
			Predicate projectItxNotIgnored = criteriaBuilder.not(projectItx.<Boolean>get("ignored"));
			Predicate projectNotIgnored = criteriaBuilder.not(project.<Boolean>get("ignored"));
			Predicate projectPredicate = criteriaBuilder.and(projectItxType, projectItxKind, projectCode, projectItxNotIgnored, projectNotIgnored);
			predicateList.add(projectPredicate);
		}
		//owner : SecondLsThingsItx type/kind EQUALS "incorporates"/"documentOwner", SecondLsThings.SecondLsThing codeName EQUALS
		if (searchParamsMap.keySet().contains("owner")){
			Join<LsThing, ItxLsThingLsThing> ownerItx = document.join("secondLsThings", JoinType.LEFT);
			Join<ItxLsThingLsThing, LsThing> owner = ownerItx.join("secondLsThing", JoinType.LEFT);
			Predicate ownerItxType = criteriaBuilder.equal(ownerItx.<String>get("lsType"), "incorporates");
			Predicate ownerItxKind = criteriaBuilder.equal(ownerItx.<String>get("lsKind"), "documentOwner");
			Predicate ownerCode = criteriaBuilder.equal(owner.<String>get("codeName"), searchParamsMap.get("owner"));
			Predicate ownerItxNotIgnored = criteriaBuilder.not(ownerItx.<Boolean>get("ignored"));
			Predicate ownerNotIgnored = criteriaBuilder.not(owner.<Boolean>get("ignored"));
			Predicate ownerPredicate = criteriaBuilder.and(ownerItxType, ownerItxKind, ownerCode, ownerItxNotIgnored, ownerNotIgnored);
			predicateList.add(ownerPredicate);
		}
		//amountBetween
		if (searchParamsMap.keySet().contains("amountTo") && searchParamsMap.keySet().contains("amountFrom")){
			try{
				Join<LsThingState, LsThingValue> amountValue = documentState.join("lsValues", JoinType.LEFT);
				Predicate amountType = criteriaBuilder.equal(amountValue.<String>get("lsType"), "numericValue");
				Predicate amountKind = criteriaBuilder.equal(amountValue.<String>get("lsKind"), "amount");
				Predicate amountBetween = criteriaBuilder.between(amountValue.<BigDecimal>get("numericValue"), new BigDecimal(searchParamsMap.get("amountFrom")), new BigDecimal(searchParamsMap.get("amountTo")));
				Predicate amountValueNotIgnored = criteriaBuilder.not(amountValue.<Boolean>get("ignored"));
				Predicate amountBetweenPredicate = criteriaBuilder.and(amountType, amountKind, amountBetween, documentStateNotIgnored, amountValueNotIgnored);
				predicateList.add(amountBetweenPredicate);
			}catch (Exception e){
				logger.error("Caught exception trying to parse "+searchParamsMap.get("amountFrom")+" or "+searchParamsMap.get("amountTo")+" as a number.",e);
				//TODO:throw Exception, catch exception at higher levels. Ask about desired behavior on this error.
			}
		}
		//amountFrom : LsThingState.LsThingValue type/kind EQUALS "numericValue"/"amount", numericValue >=
		else if (searchParamsMap.keySet().contains("amountFrom")){
			try{
				Join<LsThingState, LsThingValue> amountValue = documentState.join("lsValues", JoinType.LEFT);
				Predicate amountType = criteriaBuilder.equal(amountValue.<String>get("lsType"), "numericValue");
				Predicate amountKind = criteriaBuilder.equal(amountValue.<String>get("lsKind"), "amount");
				Predicate amountFrom = criteriaBuilder.greaterThanOrEqualTo(amountValue.<BigDecimal>get("numericValue"), new BigDecimal(searchParamsMap.get("amountFrom")));
				Predicate amountValueNotIgnored = criteriaBuilder.not(amountValue.<Boolean>get("ignored"));
				Predicate amountFromPredicate = criteriaBuilder.and(amountType, amountKind, amountFrom, documentStateNotIgnored, amountValueNotIgnored);
				predicateList.add(amountFromPredicate);
			}catch (Exception e){
				logger.error("Caught exception trying to parse "+searchParamsMap.get("amountFrom")+" as a number.",e);
				//TODO:throw Exception, catch exception at higher levels. Ask about desired behavior on this error.
			}
		}
		//amountTo : LsThingState.LsThingValue type/kind EQUALS "numericValue"/"amount", numericValue <=
		else if (searchParamsMap.keySet().contains("amountTo")){
			try{
				Join<LsThingState, LsThingValue> amountValue = documentState.join("lsValues", JoinType.LEFT);
				Predicate amountType = criteriaBuilder.equal(amountValue.<String>get("lsType"), "numericValue");
				Predicate amountKind = criteriaBuilder.equal(amountValue.<String>get("lsKind"), "amount");
				Predicate amountTo = criteriaBuilder.lessThanOrEqualTo(amountValue.<BigDecimal>get("numericValue"), new BigDecimal(searchParamsMap.get("amountTo")));
				Predicate amountValueNotIgnored = criteriaBuilder.not(amountValue.<Boolean>get("ignored"));
				Predicate amountToPredicate = criteriaBuilder.and(amountType, amountKind, amountTo, documentStateNotIgnored, amountValueNotIgnored);
				predicateList.add(amountToPredicate);
			}catch (Exception e){
				logger.error("Caught exception trying to parse "+searchParamsMap.get("amountTo")+" as a number.",e);
				//TODO:throw Exception, catch exception at higher levels. Ask about desired behavior on this error.
			}
		}
		//createdDateBetween
		if (searchParamsMap.keySet().contains("createdDateTo") && searchParamsMap.keySet().contains("createdDateFrom")){
			try{
				Predicate createdDateBetween = criteriaBuilder.between(document.<Date>get("recordedDate"), new Date(new Long(searchParamsMap.get("createdDateFrom"))), new Date(new Long(searchParamsMap.get("createdDateTo"))));
				predicateList.add(createdDateBetween);
			}catch (Exception e){
				logger.error("Caught exception trying to parse "+searchParamsMap.get("createdDateFrom")+" or "+searchParamsMap.get("createdDateTo")+" as a date.",e);
				//TODO:throw Exception, catch exception at higher levels. Ask about desired behavior on this error.
			}
		}
		//createdDateFrom : LsThing recordedDate >= (date in Long)
		else if (searchParamsMap.keySet().contains("createdDateFrom")){
			try{
				Predicate createdDateFrom = criteriaBuilder.greaterThanOrEqualTo(document.<Date>get("recordedDate"), new Date(new Long(searchParamsMap.get("createdDateFrom"))));
				predicateList.add(createdDateFrom);
			}catch (Exception e){
				logger.error("Caught exception trying to parse "+searchParamsMap.get("createdDateFrom")+" as a date.",e);
				//TODO:throw Exception, catch exception at higher levels. Ask about desired behavior on this error.
			}
		}
		//createdDateTo : LsThing recordedDate <= (date in Long)
		else if (searchParamsMap.keySet().contains("createdDateTo")){
			try{
				Predicate createdDateTo = criteriaBuilder.greaterThanOrEqualTo(document.<Date>get("recordedDate"), new Date(new Long(searchParamsMap.get("createdDateTo"))));
				predicateList.add(createdDateTo);
			}catch (Exception e){
				logger.error("Caught exception trying to parse "+searchParamsMap.get("createdDateTo")+" as a date.",e);
				//TODO:throw Exception, catch exception at higher levels. Ask about desired behavior on this error.
			}
		}		
		//active : LsThingState.LsThingValue type/kind EQUALS "stringValue"/"active", stringValue LIKE
		if (searchParamsMap.keySet().contains("active")){
			Join<LsThingState, LsThingValue> activeValue = documentState.join("lsValues", JoinType.LEFT);
			Predicate activeType = criteriaBuilder.equal(activeValue.<String>get("lsType"), "stringValue");
			Predicate activeKind = criteriaBuilder.equal(activeValue.<String>get("lsKind"), "active");
			Predicate active = criteriaBuilder.like(activeValue.<String>get("stringValue"), "%"+searchParamsMap.get("active")+"%");
			Predicate activeValueNotIgnored = criteriaBuilder.not(activeValue.<Boolean>get("ignored"));
			Predicate activePredicate = criteriaBuilder.and(activeType, activeKind, active, documentStateNotIgnored, activeValueNotIgnored);
			predicateList.add(activePredicate);
		}
		//collect all term predicates together
		if (searchParamsMap.keySet().contains("termType") || searchParamsMap.keySet().contains("daysBeforeTerm") || searchParamsMap.keySet().contains("termDateTo") || searchParamsMap.keySet().contains("termDateFrom")){
			List<Predicate> termPredicateList = new ArrayList<Predicate>();
			Join<LsThing, ItxLsThingLsThing> termItx = document.join("secondLsThings", JoinType.LEFT);
			Join<ItxLsThingLsThing, LsThing> term = termItx.join("secondLsThing", JoinType.LEFT);
			Join<LsThing, LsThingState> termState = term.join("lsStates", JoinType.LEFT);
			//SecondLsThingsItx type/kind EQUALS "incorporates"/"documentTerm", secondLsThing => Term
			Predicate termItxType = criteriaBuilder.equal(termItx.<String>get("lsType"), "incorporates");
			Predicate termItxKind = criteriaBuilder.equal(termItx.<String>get("lsKind"), "documentTerm");
			Predicate termItxNotIgnored = criteriaBuilder.not(termItx.<Boolean>get("ignored"));
			Predicate termLsKind = criteriaBuilder.equal(term.<String>get("lsKind"), "term");
			Predicate termNotIgnored = criteriaBuilder.not(term.<Boolean>get("ignored"));
			termPredicateList.add(termItxType);
			termPredicateList.add(termItxKind);
			termPredicateList.add(termItxNotIgnored);
			termPredicateList.add(termLsKind);
			termPredicateList.add(termNotIgnored);
			//termType : TermValue type/kind = "codeValue, "termType", codeValue EQUALS
			if (searchParamsMap.keySet().contains("termType")){
				Join<LsThingState, LsThingValue> termTypeValue = termState.join("lsValues", JoinType.LEFT);
				Predicate termTypeType = criteriaBuilder.equal(termTypeValue.<String>get("lsType"), "codeValue");
				Predicate termTypeKind = criteriaBuilder.equal(termTypeValue.<String>get("lsKind"), "termType");
				Predicate termType = criteriaBuilder.equal(termTypeValue.<String>get("codeValue"), searchParamsMap.get("termType"));
				Predicate termTypeNotIgnored = criteriaBuilder.not(termTypeValue.<Boolean>get("ignored"));
				Predicate termTypePredicate = criteriaBuilder.and(termTypeType, termTypeKind, termType, termTypeNotIgnored);
				termPredicateList.add(termTypePredicate);
			}
			//daysBeforeTerm : TermValue type/kind = "numericValue"/"daysBefore", numericValue =
			if (searchParamsMap.keySet().contains("daysBeforeTerm")){
				try{
					Join<LsThingState, LsThingValue> daysBeforeTermValue = termState.join("lsValues", JoinType.LEFT);
					Predicate daysBeforeTermType = criteriaBuilder.equal(daysBeforeTermValue.<String>get("lsType"), "numericValue");
					Predicate daysBeforeTermKind = criteriaBuilder.equal(daysBeforeTermValue.<String>get("lsKind"), "daysBefore");
					Predicate daysBeforeTerm = criteriaBuilder.equal(daysBeforeTermValue.<BigDecimal>get("numericValue"), new BigDecimal(searchParamsMap.get("daysBeforeTerm")));
					Predicate daysBeforeTermNotIgnored = criteriaBuilder.not(daysBeforeTermValue.<Boolean>get("ignored"));
					Predicate daysBeforeTermPredicate = criteriaBuilder.and(daysBeforeTermType, daysBeforeTermKind, daysBeforeTerm, daysBeforeTermNotIgnored);
					termPredicateList.add(daysBeforeTermPredicate);
				}catch (Exception e){
					logger.error("Caught exception trying to parse "+searchParamsMap.get("daysBeforeTerm")+" as a number.",e);
					//TODO:throw Exception, catch exception at higher levels. Ask about desired behavior on this error.
				}
			}
			//termDateBetween
			if (searchParamsMap.keySet().contains("termDateTo") && searchParamsMap.keySet().contains("termDateFrom")){
				try{
					Join<LsThingState, LsThingValue> termDateValue = termState.join("lsValues", JoinType.LEFT);
					Predicate termDateType = criteriaBuilder.equal(termDateValue.<String>get("lsType"), "dateValue");
					Predicate termDateKind = criteriaBuilder.equal(termDateValue.<String>get("lsKind"), "date");
					Predicate termDateBetween = criteriaBuilder.between(termDateValue.<Date>get("dateValue"), new Date(new Long(searchParamsMap.get("termDateFrom"))), new Date(new Long(searchParamsMap.get("termDateTo"))));
					Predicate termDateNotIgnored = criteriaBuilder.not(termDateValue.<Boolean>get("ignored"));
					Predicate termDatePredicate = criteriaBuilder.and(termDateType, termDateKind, termDateBetween, termDateNotIgnored);
					termPredicateList.add(termDatePredicate);
				}catch (Exception e){
					logger.error("Caught exception trying to parse "+searchParamsMap.get("termDateFrom")+" or "+searchParamsMap.get("termDateTo")+" as a date.",e);
					//TODO:throw Exception, catch exception at higher levels. Ask about desired behavior on this error.
				}
			}
			//termDateFrom : TermValue type/kind = "dateValue"/"date", dateValue >
			else if (searchParamsMap.keySet().contains("termDateFrom")){
				try{
					Join<LsThingState, LsThingValue> termDateValue = termState.join("lsValues", JoinType.LEFT);
					Predicate termDateType = criteriaBuilder.equal(termDateValue.<String>get("lsType"), "dateValue");
					Predicate termDateKind = criteriaBuilder.equal(termDateValue.<String>get("lsKind"), "date");
					Predicate termDateFrom = criteriaBuilder.greaterThanOrEqualTo(termDateValue.<Date>get("dateValue"), new Date(new Long(searchParamsMap.get("termDateFrom"))));
					Predicate termDateNotIgnored = criteriaBuilder.not(termDateValue.<Boolean>get("ignored"));
					Predicate termDatePredicate = criteriaBuilder.and(termDateType, termDateKind, termDateFrom, termDateNotIgnored);
					termPredicateList.add(termDatePredicate);
				}catch (Exception e){
					logger.error("Caught exception trying to parse "+searchParamsMap.get("termDateFrom")+" as a date.",e);
					//TODO:throw Exception, catch exception at higher levels. Ask about desired behavior on this error.
				}
				
			}
			//termDateTo : TermValue type/kind = "dateValue"/"date", dateValue <
			else if (searchParamsMap.keySet().contains("termDateTo")){
				try{
					Join<LsThingState, LsThingValue> termDateValue = termState.join("lsValues", JoinType.LEFT);
					Predicate termDateType = criteriaBuilder.equal(termDateValue.<String>get("lsType"), "dateValue");
					Predicate termDateKind = criteriaBuilder.equal(termDateValue.<String>get("lsKind"), "date");
					Predicate termDateTo = criteriaBuilder.lessThanOrEqualTo(termDateValue.<Date>get("dateValue"), new Date(new Long(searchParamsMap.get("termDateTo"))));
					Predicate termDateNotIgnored = criteriaBuilder.not(termDateValue.<Boolean>get("ignored"));
					Predicate termDatePredicate = criteriaBuilder.and(termDateType, termDateKind, termDateTo, termDateNotIgnored);
					termPredicateList.add(termDatePredicate);
				}catch (Exception e){
					logger.error("Caught exception trying to parse "+searchParamsMap.get("termDateFrom")+" as a date.",e);
					//TODO:throw Exception, catch exception at higher levels. Ask about desired behavior on this error.
				}
			}
			Predicate[] termPredicates = new Predicate[0];
			termPredicates = termPredicateList.toArray(termPredicates);
			predicateList.add(criteriaBuilder.and(termPredicates));
		}
		//nonSolicit : LsThingValue type/kind = "stringValue"/"nonSolicit", stringValue EQUALS
		if (searchParamsMap.keySet().contains("nonSolicit")){
			Join<LsThingState, LsThingValue> nonSolicitValue = documentState.join("lsValues", JoinType.LEFT);
			Predicate nonSolicitType = criteriaBuilder.equal(nonSolicitValue.<String>get("lsType"), "stringValue");
			Predicate nonSolicitKind = criteriaBuilder.equal(nonSolicitValue.<String>get("lsKind"), "nonSolicit");
			Predicate nonSolicit = criteriaBuilder.like(nonSolicitValue.<String>get("stringValue"), searchParamsMap.get("nonSolicit"));
			Predicate nonSolicitNotIgnored = criteriaBuilder.not(nonSolicitValue.<Boolean>get("ignored"));
			Predicate nonSolicitPredicate = criteriaBuilder.and(nonSolicitType, nonSolicitKind, nonSolicit, nonSolicitNotIgnored);
			predicateList.add(nonSolicitPredicate);
		}
		//nonTransfer : LsThingValue type/kind = "stringValue"/"nonTransfer", stringValue EQUALS
		if (searchParamsMap.keySet().contains("nonTransfer")){
			Join<LsThingState, LsThingValue> nonTransferValue = documentState.join("lsValues", JoinType.LEFT);
			Predicate nonTransferType = criteriaBuilder.equal(nonTransferValue.<String>get("lsType"), "stringValue");
			Predicate nonTransferKind = criteriaBuilder.equal(nonTransferValue.<String>get("lsKind"), "nonTransfer");
			Predicate nonTransfer = criteriaBuilder.like(nonTransferValue.<String>get("stringValue"), searchParamsMap.get("nonTransfer"));
			Predicate nonTransferNotIgnored = criteriaBuilder.not(nonTransferValue.<Boolean>get("ignored"));
			Predicate nonTransferPredicate = criteriaBuilder.and(nonTransferType, nonTransferKind, nonTransfer, nonTransferNotIgnored);
			predicateList.add(nonTransferPredicate);
		}
		//restrictedMaterialContains : LsThingValue type/kind = "stringValue"/"restrictedMaterialName", equals ignore case
		if (searchParamsMap.keySet().contains("restrictedMaterialContains")){
			Join<LsThingState, LsThingValue> restrictedMaterialValue = documentState.join("lsValues", JoinType.LEFT);
			Predicate restrictedMaterialType = criteriaBuilder.equal(restrictedMaterialValue.<String>get("lsType"), "stringValue");
			Predicate restrictedMaterialKind = criteriaBuilder.equal(restrictedMaterialValue.<String>get("lsKind"), "restrictedMaterialName");
			Predicate restrictedMaterialContains = criteriaBuilder.like(criteriaBuilder.upper(restrictedMaterialValue.<String>get("stringValue")), "%"+searchParamsMap.get("restrictedMaterialContains").toUpperCase()+"%");
			Predicate restrictedMaterialNotIgnored = criteriaBuilder.not(restrictedMaterialValue.<Boolean>get("ignored"));
			Predicate restrictedMaterialPredicate = criteriaBuilder.and(restrictedMaterialType, restrictedMaterialKind, restrictedMaterialContains, restrictedMaterialNotIgnored);
			predicateList.add(restrictedMaterialPredicate);
		}

		
		predicates = predicateList.toArray(predicates);
		criteria.where(criteriaBuilder.and(predicates));
		TypedQuery<Long> q = em.createQuery(criteria);
		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		lsThingIdList = q.getResultList();
		logger.debug("Found "+lsThingIdList.size()+" results.");
		Collection<LsThing> result = new HashSet<LsThing>();
		for (Long lsThingId: lsThingIdList) {
			LsThing lsThing = LsThing.findLsThing(lsThingId);
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


	@Override
	public ArrayList<ErrorMessage> validateLsThing(LsThingValidationDTO validationDTO) {
		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		if (validationDTO.isUniqueName()){
			try{
				checkLsThingUniqueName(validationDTO.getLsThing());
			} catch (UniqueNameException e){
				logger.error("Caught UniqueNameException validating LsThing: " + e.getMessage().toString() + " whole message  " + e.toString());
	            ErrorMessage error = new ErrorMessage();
	            error.setErrorLevel("error");
	            error.setMessage(e.getMessage());
	            errors.add(error);
			}
		}
		if (validationDTO.isUniqueInteractions()){
			try{
				checkLsThingUniqueInteractions(validationDTO);
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


	private boolean checkLsThingUniqueValueByRules(LsThingValidationDTO validationDTO) {
		if (validationDTO.getValueRules() == null || validationDTO.getValueRules().isEmpty()) return false;
		else{
			for (ValueRuleDTO valueRule : validationDTO.getValueRules()){
				if (valueRule.getValue().getEntity().equalsIgnoreCase("LsThing")){
					ValuePathDTO valuePath = valueRule.getValue();
					Collection<LsThingValue> foundValues = LsThingValue.findLsThingValuesByTypeKindFullPath(valuePath.getEntityType(), valuePath.getEntityKind(), valuePath.getStateType(), valuePath.getStateKind(), valuePath.getValueType(), valuePath.getValueKind()).getResultList();
					for (LsThingValue foundValue : foundValues){
						LsThing foundThing = foundValue.getLsState().getLsThing();
						if (valueRule.matchLsThings(validationDTO.getLsThing(), foundThing)) return true;
					}
				}
			}
		}
		return false;
		
	}


	private void checkLsThingUniqueInteractions(LsThingValidationDTO validationDTO) throws UniqueInteractionsException {
		LsThing lsThing = validationDTO.getLsThing();
		boolean checkOrderMatters = validationDTO.isOrderMatters();
		boolean checkForwardAndReverseAreSame = validationDTO.isForwardAndReverseAreSame();
		Set<ItxLsThingLsThing> secondItxLsThings = lsThing.getSecondLsThings();
		logger.debug("IN checkLsThingUniqueInteractions: "+validationDTO.toJson());
		if (!checkOrderMatters){
			//order doesn't matter. We're just checking for a unique set of "incorporates" interactions
			HashSet<LsThing> foundLsThings = null;
			for (ItxLsThingLsThing secondItxLsThing : secondItxLsThings){
				logger.debug("checking interaction:"+secondItxLsThing.toJson());
				LsThing secondLsThing = secondItxLsThing.getSecondLsThing();
				String lsType = "incorporates";
				String lsKind = secondItxLsThing.getLsKind();
				//first find the set of interactions that look like the one we're searching on
				Collection<ItxLsThingLsThing> foundItxLsThingLsThings = ItxLsThingLsThing.findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndSecondLsThingEquals(lsType, lsKind, secondLsThing).getResultList();
				Collection<LsThing> foundFirstLsThings = findFirstLsThings(validationDTO, secondItxLsThing, foundItxLsThingLsThings);
				if (foundLsThings == null){
					//on the second one, instantiate the HashSet, then add all the foundFirstLsThings.
					foundLsThings = new HashSet<LsThing>();
					foundLsThings.addAll(foundFirstLsThings);
				} else{
					//otherwise, filter the existing list to be the intersection of the results of the most recent query with the previous results
					foundLsThings.retainAll(foundFirstLsThings);
				}
				logger.debug("Currently have " + foundLsThings.size() +" potential LsThing matches: "+LsThing.toJsonArray(foundLsThings));
			}
			//We check if the set of "incorporates" interactions of our query is just a subset of those of the potential matches, and remove matches that are supersets
			foundLsThings = removeSuperSets(lsThing, foundLsThings);
			if (foundLsThings != null && !foundLsThings.isEmpty()){
				//if anything remains, it was found for every interaction so it may be a duplicate
				//then we check for LsThing value rules
				if (!checkLsThingUniqueValueByRules(validationDTO)){
					logger.debug("Found matches:");
					for (LsThing foundLsThing : foundLsThings){
						logger.debug(foundLsThing.getCodeName());
						}
					throw new UniqueInteractionsException("Found existing LsThing with identical set of interactions with same order");
					}
				}
		} else{
			//order matters. for each interaction, we will grab the order, then search using it.
			HashSet<LsThing> foundLsThings = null;
			logger.debug("Order Matters. Checking interactions forwards.");
			for (ItxLsThingLsThing secondItxLsThing : secondItxLsThings){
				logger.debug("checking interaction:"+secondItxLsThing.toJson());
				LsThing secondLsThing = secondItxLsThing.getSecondLsThing();
				String lsType = "incorporates";
				String lsKind = secondItxLsThing.getLsKind();
				int order = secondItxLsThing.grabItxOrder();
				//first find the set of interactions that look like the one we're searching on, including by order
				Collection<ItxLsThingLsThing> foundItxLsThingLsThings = ItxLsThingLsThing.findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndSecondLsThingEqualsAndOrderEquals(lsType, lsKind, secondLsThing, order).getResultList();
				Collection<LsThing> foundFirstLsThings = findFirstLsThings(validationDTO, secondItxLsThing, foundItxLsThingLsThings);
				if (foundLsThings == null){
					foundLsThings = new HashSet<LsThing>();
					foundLsThings.addAll(foundFirstLsThings);
				} else{
					foundLsThings.retainAll(foundFirstLsThings);
				}
			}
			//We check if the set of "incorporates" interactions of our query is just a subset of those of the potential matches, and remove matches that are supersets
			foundLsThings = removeSuperSets(lsThing, foundLsThings);
			if (foundLsThings != null && !foundLsThings.isEmpty()){
				//if anything remains, it was found for every interaction so it may be a duplicate
				//then we check for LsThing value rules
				if (!checkLsThingUniqueValueByRules(validationDTO)){
					logger.debug("Found matches:");
					for (LsThing foundLsThing : foundLsThings){
						logger.debug(foundLsThing.getCodeName());
					}
					throw new UniqueInteractionsException("Found existing LsThing with identical set of interactions with same order");
				}
			}
			if (checkForwardAndReverseAreSame){
				//if we need to check backwards as well, by this point we have already passed "forwards validation"
				//we need to get the total number of "incorporates" interactions
				logger.debug("Forward and Reverse are the same. Checking interactions backwards.");
				foundLsThings = null;
				ArrayList<ItxLsThingLsThing> orderedIncorporatesInteractions = new ArrayList<ItxLsThingLsThing>();
				for (ItxLsThingLsThing secondItxLsThing : secondItxLsThings){
					if (secondItxLsThing.getLsType().equals("incorporates")){
						orderedIncorporatesInteractions.add(secondItxLsThing);
					}
				}
				//then we sort
				Collections.sort(orderedIncorporatesInteractions, new ItxLsThingLsThingComparator());
				//then do the same search as above, but with new "order" parameters:
				int order = orderedIncorporatesInteractions.size();
				for (ItxLsThingLsThing secondItxLsThing : orderedIncorporatesInteractions){
					logger.debug("checking interaction:"+secondItxLsThing.toJson());
					LsThing secondLsThing = secondItxLsThing.getSecondLsThing();
					String lsType = "incorporates";
					String lsKind = secondItxLsThing.getLsKind();
					Collection<ItxLsThingLsThing> foundItxLsThingLsThings = ItxLsThingLsThing.findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndSecondLsThingEqualsAndOrderEquals(lsType, lsKind, secondLsThing, order).getResultList();
					Collection<LsThing> foundFirstLsThings = findFirstLsThings(validationDTO, secondItxLsThing, foundItxLsThingLsThings);
					logger.debug("Found these " + foundFirstLsThings.size() + " lsThing matches for current itx: "+LsThing.toJsonArray(foundFirstLsThings));
					if (foundLsThings == null){
						foundLsThings = new HashSet<LsThing>();
						foundLsThings.addAll(foundFirstLsThings);
					} else{
						foundLsThings.retainAll(foundFirstLsThings);
					}
					order--;
				}
				//We check if the set of "incorporates" interactions of our query is just a subset of those of the potential matches, and remove matches that are supersets
				foundLsThings = removeSuperSets(lsThing, foundLsThings);
				if (foundLsThings != null && !foundLsThings.isEmpty()){
					//if anything remains, it was found for every interaction so it may be a duplicate
					//then we check for LsThing value rules
					if (!checkLsThingUniqueValueByRules(validationDTO)){
						logger.debug("Found matches:");
						for (LsThing foundLsThing : foundLsThings){
							logger.debug(foundLsThing.getCodeName());
							}
						throw new UniqueInteractionsException("Found existing LsThing with identical set of interactions with reversed order");
						}
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
	
	private static Collection<LsThing> findFirstLsThings(LsThingValidationDTO validationDTO, ItxLsThingLsThing secondItxLsThing, Collection<ItxLsThingLsThing> foundItxLsThingLsThings){
//		logger.debug("found interactions:"+ItxLsThingLsThing.toJsonArray(foundItxLsThingLsThings));
		//then if we have value criteria to compare, compare them and pare down the list of matching interactions
		Collection<ItxLsThingLsThing> matchingItxLsThingLsThings = new HashSet<ItxLsThingLsThing>();
		//first we check if any of our value rules apply to the current Itx. If not, we skip ahead to "No value criteria"
		boolean anyValueRulesApply = false;
		if (validationDTO.getValueRules() != null && !validationDTO.getValueRules().isEmpty()){
			for (ValueRuleDTO valueRule : validationDTO.getValueRules()){
				if (valueRule.checkRuleApplies(secondItxLsThing)) anyValueRulesApply = true;
			}
		}
		if (anyValueRulesApply){
			for (ValueRuleDTO valueRule : validationDTO.getValueRules()){
//				logger.debug("Checking value rule:"+valueRule.toJson());
				for (ItxLsThingLsThing foundItxLsThingLsThing : foundItxLsThingLsThings){
					boolean isAMatch = valueRule.matchItxLsThingLsThings(secondItxLsThing, foundItxLsThingLsThing);
					if (isAMatch) matchingItxLsThingLsThings.add(foundItxLsThingLsThing);
					if (isAMatch) logger.debug("Found a match for value rule:"+valueRule.toJson());
				}
				
			}
		}else{
			logger.debug("No value criteria");
			//if there are no value criteria to compare, then all the interactions are matching
			matchingItxLsThingLsThings.addAll(foundItxLsThingLsThings);
		}
		Collection<LsThing> foundFirstLsThings = new HashSet<LsThing>();
		for (ItxLsThingLsThing matchingItx : matchingItxLsThingLsThings){
			if (!matchingItx.getFirstLsThing().isIgnored()) foundFirstLsThings.add(matchingItx.getFirstLsThing());
		}
		logger.debug("Found these " + foundFirstLsThings.size() + " lsThing matches for current itx: "+LsThing.toJsonArray(foundFirstLsThings));
		return foundFirstLsThings;
	}
	
	private static HashSet<LsThing> removeSuperSets(LsThing lsThing, Collection<LsThing> foundLsThings){
		HashSet<LsThing> filteredFoundLsThings = new HashSet<LsThing>();
		HashSet<Long> lsThingReferencedComponentIds = new HashSet<Long>();
		for (ItxLsThingLsThing itx : lsThing.getSecondLsThings()){
			if (itx.getLsType().equals("incorporates") && itx.getLsKind().equals("assembly_component")){
				lsThingReferencedComponentIds.add(itx.getSecondLsThing().getId());
			}
		}
		//we compare the list of referenced components of the query lsThing and the foundLsThings
		//and filter out foundLsThings that don't have the same list as the query
		for (LsThing foundLsThing : foundLsThings){
			HashSet<Long> foundLsThingReferencedComponentIds = new HashSet<Long>();
			for (ItxLsThingLsThing itx : foundLsThing.getSecondLsThings()){
				if (itx.getLsType().equals("incorporates") && itx.getLsKind().equals("assembly_component")){
					foundLsThingReferencedComponentIds.add(itx.getSecondLsThing().getId());
				}
			}
			for (Long id : lsThingReferencedComponentIds){
				foundLsThingReferencedComponentIds.remove(id);
			}
			//if the query lsThing and this foundLsThing are truly a match, then there will be no ids left.
			if (foundLsThingReferencedComponentIds.isEmpty()) filteredFoundLsThings.add(foundLsThing);
		}
		return filteredFoundLsThings;
	}
	
	@Override
	public DependencyCheckDTO checkBatchDependencies(LsThing batch){
		DependencyCheckDTO result = new DependencyCheckDTO();
		result.getQueryCodeNames().add(batch.getCodeName());
		Collection<LsThing> assemblies = findCompositesByComponentEquals(batch);
		if (assemblies != null && !assemblies.isEmpty()){
			for (LsThing assembly : assemblies){
				LsThingLabel corpNameLabel = assembly.pickBestCorpName();
				if (corpNameLabel != null) result.getDependentCorpNames().add(corpNameLabel.getLabelText());
			}
		}
		if (!result.getDependentCorpNames().isEmpty()) result.setLinkedDataExists(true);
		result.checkForDependentData();
		return result;
	}
	
	@Override
	public DependencyCheckDTO checkParentDependencies(LsThing parent){
		DependencyCheckDTO result = new DependencyCheckDTO();
		result.getQueryCodeNames().add(parent.getCodeName());
		Collection<LsThing> assemblies = findCompositesByComponentEquals(parent);
		if (assemblies != null && !assemblies.isEmpty()){
			for (LsThing assembly : assemblies){
				LsThingLabel corpNameLabel = assembly.pickBestCorpName();
				if (corpNameLabel != null) result.getDependentCorpNames().add(corpNameLabel.getLabelText());
			}
		}
		Collection<LsThing> batches = findBatchesByParentEquals(parent);
		if (batches != null && !batches.isEmpty()){
			for (LsThing batch : batches){
				result.getQueryCodeNames().add(batch.getCodeName());
			}
		}
		if (!result.getDependentCorpNames().isEmpty()) result.setLinkedDataExists(true);
		result.checkForDependentData();
		return result;
	}
	
	private void logicalDeleteLsThingAndInteractions(LsThing lsThing){
		lsThing.logicalDelete();
		lsThing.merge();
		if (lsThing.getFirstLsThings() != null && !lsThing.getFirstLsThings().isEmpty()){
			for (ItxLsThingLsThing itx : lsThing.getFirstLsThings()){
				itx.logicalDelete();
				itx.merge();
			}
		}
		if (lsThing.getSecondLsThings() != null && !lsThing.getSecondLsThings().isEmpty()){
			for (ItxLsThingLsThing itx : lsThing.getSecondLsThings()){
				itx.logicalDelete();
				itx.merge();
			}
		}
	}


	@Override
	public boolean deleteBatch(LsThing batch) {
		LsThing parent = findParentByBatchEquals(batch);
		int lastBatchNumber = getBatchNumber(parent);
		boolean isLastBatch = false;
		if (batch.pickBestCorpName().getLabelText().equals(parent.pickBestCorpName().getLabelText()+"-"+lastBatchNumber)) isLastBatch = true;
		logicalDeleteLsThingAndInteractions(batch);
		if (isLastBatch){
			decrementBatchNumber(parent);
		}
		return true;
	}


	@Override
	public boolean deleteParent(LsThing parent) {
		String lastParentCorpName = autoLabelService.getLastLabel(parent.getLsTypeAndKind(), "corpName_ACAS LsThing").getAutoLabel();
		boolean isLastParent = false;
		if (parent.pickBestCorpName().getLabelText().equals(lastParentCorpName)) isLastParent = true;
		Collection<LsThing> batches = findBatchesByParentEquals(parent);
		if (batches != null && !batches.isEmpty()){
			for (LsThing batch : batches){
				logicalDeleteLsThingAndInteractions(batch);
			}
		}
		logicalDeleteLsThingAndInteractions(parent);
		if (isLastParent){
			autoLabelService.decrementLabelSequence(parent.getLsTypeAndKind(), "corpName_ACAS LsThing");
		}
		return true;
	}
}
