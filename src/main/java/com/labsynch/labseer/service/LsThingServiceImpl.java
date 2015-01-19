package com.labsynch.labseer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ErrorMessageDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.utils.PropertiesUtilService;

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
				error.setErrorCode("MULTIPLE RESULTS");
				error.setErrorMessage("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName() );	
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
			List<LsThing> lsThings = LsThing.findLsThingByLabelText(thingType, thingKind, labelType, labelKind, request.getRequestName()).getResultList();
			if (lsThings.size() == 1){
				request.setPreferredName(lsThings.get(0).getCodeName());
				request.setReferenceName(lsThings.get(0).getCodeName());
			} else if (lsThings.size() > 1){
				responseOutput.setError(true);
				ErrorMessageDTO error = new ErrorMessageDTO();
				error.setErrorCode("MULTIPLE RESULTS");
				error.setErrorMessage("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName() );	
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
	public boolean validateComponentName(String componentName) {
		Collection<LsThing> foundLsThings = null;
		boolean isValid = true;
		try{
			foundLsThings = LsThing.findLsThingByLabelText(componentName).getResultList();
		} catch (EmptyResultDataAccessException e){
			return true;
		}
		if (foundLsThings!=null && !foundLsThings.isEmpty()){
			return false;
		}
		return isValid;
	}
	
	@Override
	public boolean validateComponentName(LsThing lsThing) {
		boolean isValid = true;
		Set<LsThingLabel> lsThingLabels = lsThing.getLsLabels();
		for (LsThingLabel label : lsThingLabels){
			String labelText = label.getLabelText();
			if (!label.isIgnored()) isValid = validateComponentName(labelText);
		}
		return isValid;
	}


	@Override
	public boolean validateAssembly(List<String> componentCodeNames) {
		HashSet<LsThing> assemblySet = null;
		int order = 0;
		boolean isValid = true;
		for (String componentCodeName : componentCodeNames){
			LsThing component = LsThing.findLsThingsByCodeNameEquals(componentCodeName).getSingleResult();
			order+=1;
			if (assemblySet == null){
				//on the first component, instantiate the HashSet, add all the assemblies with component and order (order in list, starting with 1)
				assemblySet = new HashSet<LsThing>();
				assemblySet.addAll(findAssembliesByComponentAndOrder(component, order));
			} else{
				//otherwise, filter the existing list to be the intersection of the results of this component/order with those from the last
				assemblySet.retainAll(findAssembliesByComponentAndOrder(component, order));
			}
		}
		if (assemblySet != null & !assemblySet.isEmpty()){
			//if anything remains, it was found for every component with the correct order.
			//It's a match, which means the assembly described by componentCodeNames is not unique
			return false;
		}
		//iterate through the list backwards and do the same, in case the order was simply reversed (not a unique assembly)
		order = 0;
		ListIterator<String> li = componentCodeNames.listIterator(componentCodeNames.size());
		while(li.hasPrevious()) {
			String componentCodeName = li.previous();
			LsThing component = LsThing.findLsThingsByCodeNameEquals(componentCodeName).getSingleResult();
			order+=1;
			if (assemblySet == null){
				assemblySet = new HashSet<LsThing>();
				assemblySet.addAll(findAssembliesByComponentAndOrder(component, order));
			} else{
				assemblySet.retainAll(findAssembliesByComponentAndOrder(component, order));
			}
		}
		if (assemblySet != null & !assemblySet.isEmpty()){
			return false;
		}
		
		return isValid;
	}
	
	@Override
	public boolean validateAssembly(LsThing assembly){
		return validateAssembly(getComponentCodeNamesFromNewAssembly(assembly));
	}
	
	private static Collection<LsThing> findAssembliesByComponentAndOrder(LsThing component, int order){
		Collection<ItxLsThingLsThing> interactions = ItxLsThingLsThing.findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndSecondLsThingEquals("incorporates", "assembly_component", component).getResultList();
		Collection<LsThing> assemblies = new HashSet<LsThing>();
		for (ItxLsThingLsThing interaction : interactions){
			if (interaction.getOrder() == order) assemblies.add(interaction.getFirstLsThing());
		}
		return assemblies;
	}
	
	public List<String> getComponentCodeNamesFromNewAssembly(LsThing assembly){
		String componentsClob = getComponentsClobFromAssembly(assembly);
		List<String> componentCodeNames = parseComponentsClob(componentsClob);
		return componentCodeNames;
	}
	
	private List<String> parseComponentsClob(String componentsClob) {
		// TODO Auto-generated method stub
		return null;
	}


	private static String getComponentsClobFromAssembly(LsThing assembly){
		Collection<LsThingState> states = assembly.getLsStates();
		Collection<LsThingValue> values = new HashSet<LsThingValue>();
		for (LsThingState state : states ){
			if (state.getLsType().equals("metadata")){
				values.addAll(state.getLsValues());
			}
		}
		for (LsThingValue value : values){
			if (value.getLsType().equals("clobValue") && value.getLsKind().equals("components")){
				String componentsClob = value.getClobValue();
				return componentsClob;
			}
		}
		return null;
	}
	
	@Override
	@Transactional
	public LsThing updateLsThing(LsThing jsonLsThing){
		logger.debug("incoming meta lsThing: " + jsonLsThing.toPrettyJson());
		logger.debug("recorded by: " + jsonLsThing.getRecordedBy());

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
					logger.debug("updated lsThing label " + updatedLabel.getId());
				}
			}			
		} else {
			logger.debug("No lsThing labels to update");
		}

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
					logger.debug("updated lsThing state " + lsThingState.getId());

				}
				if (lsThingState.getLsValues() != null){
					for(LsThingValue lsThingValue : lsThingState.getLsValues()){
						LsThingValue updatedLsThingValue;
						if (lsThingValue.getId() == null){
							updatedLsThingValue = LsThingValue.create(lsThingValue);
							updatedLsThingValue.setLsState(LsThingState.findLsThingState(lsThingState.getId()));
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

		logger.debug("updatedLsThing: " + updatedLsThing.toPrettyJson());
		return updatedLsThing;

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

		return newLsThing;
	}
	
	@Override
	@Transactional
	public LsThing saveLsThing(LsThing lsThing, boolean isParent, boolean isBatch, boolean isAssembly, boolean isComponent, Long parentId) throws UniqueNameException{
		//only check that the name is unique upon save if it's a parent and a component
		boolean checkUniqueLsThingName = (isParent & isComponent);
		LsThing savedLsThing = saveLsThing(lsThing, checkUniqueLsThingName);
		//after saving the lsThing, save the necessary interactions
		if (isBatch){
			LsThing parent = LsThing.findLsThing(parentId);
			saveItxLsThingLsThing("instantiates", "batch_parent", savedLsThing, parent, lsThing.getRecordedBy(), lsThing.getRecordedDate());
		}
		if (isAssembly){
			List<String> componentCodeNames = getComponentCodeNamesFromNewAssembly(lsThing);
			int order = 1;
			for (String componentCodeName: componentCodeNames){
				LsThing component = LsThing.findLsThingsByCodeNameEquals(componentCodeName).getSingleResult();
				saveItxLsThingLsThing("incorporates", "assembly_component", savedLsThing, component, order, lsThing.getRecordedBy(), lsThing.getRecordedDate());
			}
		}
		return savedLsThing;
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
}
