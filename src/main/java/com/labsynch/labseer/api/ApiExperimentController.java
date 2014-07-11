package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.dto.ExperimentGuiStubDTO;
import com.labsynch.labseer.dto.KeyValueDTO;
import com.labsynch.labseer.service.ExperimentService;
import com.labsynch.labseer.service.ExperimentValueService;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Controller
@RequestMapping("api/v1/experiments")
@Transactional
@RooWebJson(jsonObject = Experiment.class)
public class ApiExperimentController {
	private static final Logger logger = LoggerFactory.getLogger(ApiExperimentController.class);

	@Autowired
	private ExperimentService experimentService;

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private ExperimentValueService experimentValueService;

	@RequestMapping(value = "/dto", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<java.lang.String> listJson(
			@RequestParam(value = "protocolKind", required = false) String protocolKind,
			@RequestParam(value = "protocolName", required = false) String protocolName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<ExperimentGuiStubDTO> result = new ArrayList<ExperimentGuiStubDTO>();
		
		if (protocolKind != null && protocolName != null){
			List<Protocol> protocols = Protocol.findProtocolsByLsKindEquals(protocolKind).getResultList();
			for (Protocol protocol:protocols){
				if (protocol.findPreferredName().equals(protocolName)){
					List<Experiment> experiments = Experiment.findExperimentsByProtocol(protocol).getResultList();
					for (Experiment experiment:experiments){
						ExperimentGuiStubDTO exptGui = new ExperimentGuiStubDTO(experiment);
						result.add(exptGui);
					}
				}
			}
		} else if (protocolKind != null && protocolName == null){
			List<Protocol> protocols = Protocol.findProtocolsByLsKindEquals(protocolKind).getResultList();
			for (Protocol protocol:protocols){
				if (protocol.findPreferredName().equals(protocolName)){
					List<Experiment> experiments = Experiment.findExperimentsByProtocol(protocol).getResultList();
					for (Experiment experiment:experiments){
						ExperimentGuiStubDTO exptGui = new ExperimentGuiStubDTO(experiment);
						result.add(exptGui);
					}
				}
			}
		}  else if (protocolKind == null && protocolName != null){
			List<Protocol> protocols = Protocol.findProtocolByProtocolName(protocolName);
			for (Protocol protocol:protocols){
				List<Experiment> experiments = Experiment.findExperimentsByProtocol(protocol).getResultList();
				for (Experiment experiment:experiments){
					ExperimentGuiStubDTO exptGui = new ExperimentGuiStubDTO(experiment);
					result.add(exptGui);
				}
			}
		} else {
			List<Experiment> experiments = Experiment.findAllExperiments();
			for (Experiment experiment:experiments){
				ExperimentGuiStubDTO exptGui = new ExperimentGuiStubDTO(experiment);
				result.add(exptGui);
			}
		}

		return new ResponseEntity<String>(ExperimentGuiStubDTO.toJsonArray(result), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/subjectsstatus/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> findSubjectValues(
    		@PathVariable("id") Long id,
    		@RequestParam("stateType") String stateType,
    		@RequestParam("stateKind") String stateKind,
    		@RequestParam("stateValueType") String stateValueType,
    		@RequestParam("stateValueKind") String stateValueKind
    		) {
		
    	List<String> values = new ArrayList<String>();
    	Experiment experiment = Experiment.findExperiment(id);
    	Set<Experiment> experiments = new HashSet<Experiment>();
    	experiments.add(experiment);
    	Set<AnalysisGroup> analysisGroups = experiment.getAnalysisGroups();
        for (AnalysisGroup analysisGroup: analysisGroups) {
			Set<TreatmentGroup> treatmentGroups = analysisGroup.getTreatmentGroups();
			for (TreatmentGroup treatmentGroup : treatmentGroups) {
				Set<Subject> subjects = treatmentGroup.getSubjects();
				for (Subject subject : subjects) {
					List<SubjectState> subjectStates = SubjectState
							.findSubjectStatesByLsTypeEqualsAndLsKindEqualsAndSubject(
									stateType, stateKind, subject)
							.getResultList();
					for (SubjectState subjectState : subjectStates) {
						List<SubjectValue> subjectValues = SubjectValue
								.findSubjectValuesByLsStateAndLsTypeEqualsAndLsKindEquals(
										subjectState, stateValueType, stateValueKind)
								.getResultList();
						for (SubjectValue subjectValue : subjectValues) {
							if (stateValueType.equalsIgnoreCase("stringValue")) {
	    	   					values.add(subjectValue.getStringValue());
	    	   				} else if (stateValueType.equalsIgnoreCase("numericValue")) {
	    	   					values.add(subjectValue.getNumericValue().toString());
	    	   				}
						}
					}
				}
			}
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        KeyValueDTO transferDTO = new KeyValueDTO();
        transferDTO.setKey("lsValue");
        transferDTO.setValue(values.toString());
        return new ResponseEntity<String>(transferDTO.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{IdOrCodeName}/values/{Id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getExperimentValueByIdOrCodeName (
			@PathVariable("IdOrCodeName") String IdOrCodeName,
			@PathVariable("Id") Long Id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		
		List<ExperimentValue> experimentValues = null;
		Long id = null;
		if(isNumeric(IdOrCodeName)) {
			id = Long.valueOf(IdOrCodeName);
		} else {
			id = retrieveExperimentIdFromCodeName(IdOrCodeName);
		}
		if(id != null) {
			experimentValues = experimentValueService.getExperimentValuesByExperimentId(Long.valueOf(id));
		} 
		
		ExperimentValue result = null;
		
		for(ExperimentValue experimentValue : experimentValues) {
			if(experimentValue.getId() == Id) {
				result = experimentValue;
				break;
			}
		}
		return (result == null) ?
			new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND) :
			new ResponseEntity<String>(result.toJson(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{IdOrCodeName}/values", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getExperimentValuesForExperimentByIdOrCodeName (
			@PathVariable("IdOrCodeName") String IdOrCodeName) {		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		
		List<ExperimentValue> experimentValues = null;
		Long id = null;
		if(isNumeric(IdOrCodeName)) {
			id = Long.valueOf(IdOrCodeName);
		} else {
			id = retrieveExperimentIdFromCodeName(IdOrCodeName);
		}
		if(id != null) {
			experimentValues = experimentValueService.getExperimentValuesByExperimentId(Long.valueOf(id));
		}
		
		return (experimentValues == null) ?
			new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND) :
			new ResponseEntity<String>(ExperimentValue.toJsonArray(experimentValues), headers, HttpStatus.OK);
	}
	
	private static Long retrieveExperimentIdFromCodeName(String codeName) {
		Long id = null;
		List<Experiment> experiments = Experiment.findAllExperiments();
		for(Experiment ex : experiments) {
			if(ex.getCodeName() != null && ex.getCodeName().compareTo(codeName) == 0) {
				id = ex.getId();
				break;
			}
		}
		return id;
	}
	
	@RequestMapping(value = "/values", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody ResponseEntity<String> saveExperimentFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
             
        ExperimentValue experimentValue = ExperimentValue.fromJsonToExperimentValue(json);
        
	    return (experimentValueService.saveExperimentValue(experimentValue) == null) ?
	    	new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) :
	    	new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "{IdOrCodeName}/values/{Id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody ResponseEntity<String> updateExperimentFromJsonWithId(
    		@RequestBody String json,
    		@PathVariable("Id") String Id,
    		@PathVariable("IdOrCodeName") String IdOrCodeName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        ExperimentValue experimentValue = ExperimentValue.fromJsonToExperimentValue(json);
        if(experimentValue.getId() == null) {
        	return (experimentValueService.saveExperimentValue(experimentValue) != null) ?
        	    	new ResponseEntity<String>(headers, HttpStatus.OK) :
        	    	new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        }      
        return ((experimentValueService.updateExperimentValue(experimentValue)) == null) ? 
        		new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) : 
        		new ResponseEntity<String>(headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "{IdOrCodeName}/values", method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody ResponseEntity<String> updateExperimentFromJsonWithId(
    		@RequestBody String json,
    		@PathVariable("IdOrCodeName") String IdOrCodeName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        ExperimentValue experimentValue = ExperimentValue.fromJsonToExperimentValue(json);
        if(experimentValue.getId() == null) {
        	return (experimentValueService.saveExperimentValue(experimentValue) != null) ?
        	    	new ResponseEntity<String>(headers, HttpStatus.OK) :
        	    	new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        }      
        return ((experimentValueService.updateExperimentValue(experimentValue)) == null) ? 
        		new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) : 
        		new ResponseEntity<String>(headers, HttpStatus.OK);
    }
	
	private static boolean isNumeric(String str) {
	    for (char c : str.toCharArray()) {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
}
