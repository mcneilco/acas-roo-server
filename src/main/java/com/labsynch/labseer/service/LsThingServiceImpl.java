package com.labsynch.labseer.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
	public boolean validateComponentName(String componentName, String lsKind) {
		Collection<LsThing> foundLsThings = null;
		boolean isValid = true;
		try{
			foundLsThings = LsThing.findLsThingByLabelTextAndLsKind(componentName, lsKind).getResultList();
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
		String lsKind = lsThing.getLsKind();
		for (LsThingLabel label : lsThingLabels){
			String labelText = label.getLabelText();
			if (!label.isIgnored()) isValid = validateComponentName(labelText, lsKind);
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
			if (interaction.retrieveOrder() == order) assemblies.add(interaction.getFirstLsThing());
		}
		return assemblies;
	}
	
	@Override
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
					itxLsThingLsThing.setSecondLsThing(updatedLsThing);
					updatedItxLsThingLsThing = saveItxLsThingLsThing(itxLsThingLsThing);
					firstLsThings.add(updatedItxLsThingLsThing);
				}else {
					//old itx needs to be updated
					LsThing updatedNestedLsThing;
					if (itxLsThingLsThing.getFirstLsThing().getId() == null){
						//old itx has new nested lsThing
						logger.debug("saving new nested LsThing" + itxLsThingLsThing.getFirstLsThing().toJson());
						try{
							updatedNestedLsThing = saveLsThing(itxLsThingLsThing.getFirstLsThing());
							itxLsThingLsThing.setFirstLsThing(updatedNestedLsThing);
						} catch (UniqueNameException e){
							logger.error("Caught UniqueNameException trying to update nested LsThing");
						}
					}
					else{
						//old itx has old lsThing that needs to be updated
						updatedNestedLsThing = LsThing.update(itxLsThingLsThing.getFirstLsThing());
						updateLsStates(itxLsThingLsThing.getFirstLsThing(), updatedNestedLsThing);
						itxLsThingLsThing.setFirstLsThing(updatedNestedLsThing);
					}
					itxLsThingLsThing.setSecondLsThing(updatedLsThing);
					updatedItxLsThingLsThing = ItxLsThingLsThing.update(itxLsThingLsThing);
					firstLsThings.add(updatedItxLsThingLsThing);
				}
			}
			updatedLsThing.setFirstLsThings(firstLsThings);
		}
		
		if(jsonLsThing.getSecondLsThings() != null){
			//there are itx's
				Set<ItxLsThingLsThing> firstLsThings = new HashSet<ItxLsThingLsThing>();
				for (ItxLsThingLsThing itxLsThingLsThing : jsonLsThing.getSecondLsThings()){
					ItxLsThingLsThing updatedItxLsThingLsThing;
					if (itxLsThingLsThing.getId() == null){
						//need to save a new itx
						logger.debug("saving new itxLsThingLsThing: " + itxLsThingLsThing.toJson());
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
						itxLsThingLsThing.setFirstLsThing(updatedLsThing);
						updatedItxLsThingLsThing = saveItxLsThingLsThing(itxLsThingLsThing);
						firstLsThings.add(updatedItxLsThingLsThing);
					}else {
						//old itx needs to be updated
						LsThing updatedNestedLsThing;
						if (itxLsThingLsThing.getSecondLsThing().getId() == null){
							//old itx has new nested lsThing
							logger.debug("saving new nested LsThing" + itxLsThingLsThing.getSecondLsThing().toJson());
							try{
								updatedNestedLsThing = saveLsThing(itxLsThingLsThing.getSecondLsThing());
								itxLsThingLsThing.setSecondLsThing(updatedNestedLsThing);
							} catch (UniqueNameException e){
								logger.error("Caught UniqueNameException trying to update nested LsThing");
							}
						}
						else{
							//old itx has old lsThing that needs to be updated
							updatedNestedLsThing = LsThing.update(itxLsThingLsThing.getSecondLsThing());
							updateLsStates(itxLsThingLsThing.getSecondLsThing(), updatedNestedLsThing);
							itxLsThingLsThing.setSecondLsThing(updatedNestedLsThing);
						}
						itxLsThingLsThing.setFirstLsThing(updatedLsThing);
						updatedItxLsThingLsThing = ItxLsThingLsThing.update(itxLsThingLsThing);
						firstLsThings.add(updatedItxLsThingLsThing);
					}
				}
				updatedLsThing.setSecondLsThings(firstLsThings);
			}
		
		
		

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
		String[] splitQuery = queryString.split("\\s+");
		logger.debug("Number of search terms: " + splitQuery.length);
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
}
