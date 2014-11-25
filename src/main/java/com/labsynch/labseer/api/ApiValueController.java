package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.AbstractState;
import com.labsynch.labseer.domain.AbstractValue;
import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.KeyValueDTO;
import com.labsynch.labseer.dto.StateValueDTO;
import com.labsynch.labseer.service.AnalysisGroupService;
import com.labsynch.labseer.service.AnalysisGroupValueService;
import com.labsynch.labseer.service.ExperimentService;
import com.labsynch.labseer.service.ExperimentStateService;
import com.labsynch.labseer.service.ExperimentValueService;
import com.labsynch.labseer.service.LsThingValueService;
import com.labsynch.labseer.service.ProtocolValueService;
import com.labsynch.labseer.service.SubjectValueService;
import com.labsynch.labseer.service.TreatmentGroupValueService;

@Controller
@RequestMapping("api/v1")
//@RooWebFinder
@Transactional
//@RooWebJson(jsonObject = AbstractValue.class)
public class ApiValueController {

	private static final Logger logger = LoggerFactory.getLogger(ApiValueController.class);
	
	@Autowired
	private ProtocolValueService protocolValueService;
	
	@Autowired
	private ExperimentService experimentService;
	
	@Autowired
	private ExperimentStateService experimentStateService;
	
	@Autowired
	private ExperimentValueService experimentValueService;

	@Autowired
	private AnalysisGroupService analysisGroupService;
	
	@Autowired
	private ExperimentStateService analysisGroupStateService;
	
	@Autowired
	private AnalysisGroupValueService analysisGroupValueService;
	
	@Autowired
	private ExperimentStateService treatmentGroupStateService;

	@Autowired
	private TreatmentGroupValueService treatmentGroupValueService;

	@Autowired
	private ExperimentStateService subjectStateService;
	
	@Autowired
	private SubjectValueService subjectValueService;
	
	@Autowired
	private LsThingValueService lsThingValueService;

	//special path
	
	@RequestMapping(value = "/{entity}/{idOrCodeName}/bystate/{stateType}/{stateKind}/byvalue/{valueType}/{valueKind}/", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> putValueByPath (
			@PathVariable("entity") String entity,
			@PathVariable("idOrCodeName") String idOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("valueType") String valueType,
			@PathVariable("valueKind") String valueKind,
			@RequestBody String value) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		//this if/else if block controls which lsThing is being hit
		logger.debug("ENTITY IS: " + entity);
		if (entity.equals("protocol")) {
			ProtocolValue protocolValue = protocolValueService.updateProtocolValue(idOrCodeName, stateType, stateKind, valueType, valueKind, value);
			return new ResponseEntity<String>(protocolValue.toJson(), headers, HttpStatus.OK);
		}
		if (entity.equals("experiment")) {
			ExperimentValue experimentValue = experimentValueService.updateExperimentValue(idOrCodeName, stateType, stateKind, valueType, valueKind, value);
			return new ResponseEntity<String>(experimentValue.toJson(), headers, HttpStatus.OK);
		}
		if (entity.equals("analysisGroup")) {
			AnalysisGroupValue analysisGroupValue = analysisGroupValueService.updateAnalysisGroupValue(idOrCodeName, stateType, stateKind, valueType, valueKind, value);
			return new ResponseEntity<String>(analysisGroupValue.toJson(), headers, HttpStatus.OK);
		}
		if (entity.equals("treatmentGroup")) {
			TreatmentGroupValue treatmentGroupValue = treatmentGroupValueService.updateTreatmentGroupValue(idOrCodeName, stateType, stateKind, valueType, valueKind, value);
			return new ResponseEntity<String>(treatmentGroupValue.toJson(), headers, HttpStatus.OK);
		}
		if (entity.equals("subject")) {
			SubjectValue subjectValue = subjectValueService.updateSubjectValue(idOrCodeName, stateType, stateKind, valueType, valueKind, value);
			return new ResponseEntity<String>(subjectValue.toJson(), headers, HttpStatus.OK);
		}
		if (entity.equals("lsThing")) {
			LsThingValue lsThingValue = lsThingValueService.updateLsThingValue(idOrCodeName, stateType, stateKind, valueType, valueKind, value);
			return new ResponseEntity<String>(lsThingValue.toJson(), headers, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("INVALID ENTITY", headers, HttpStatus.BAD_REQUEST);
	}
	
	//List values as jsonArray (GET)
	
	@RequestMapping(value = "/protocolvalues", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listProtocolValuesJsonArray() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ProtocolValue> result = ProtocolValue.findAllProtocolValues();
        for (ProtocolValue protocolValue: result) {
        	if (protocolValue.isIgnored()) result.remove(protocolValue);
        }
        return new ResponseEntity<String>(ProtocolValue.toJsonArray(result), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/experimentvalues", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listExperimentValuesJsonArray() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ExperimentValue> result = ExperimentValue.findAllExperimentValues();
        for (ExperimentValue experimentValue: result) {
        	if (experimentValue.isIgnored()) result.remove(experimentValue);
        }
        return new ResponseEntity<String>(ExperimentValue.toJsonArray(result), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/analysisgroupvalues", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listAnalysisGroupValuesJsonArray() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<AnalysisGroupValue> result = AnalysisGroupValue.findAllAnalysisGroupValues();
        for (AnalysisGroupValue analysisGroupValue: result) {
        	if (analysisGroupValue.isIgnored()) result.remove(analysisGroupValue);
        }
        return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(result), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/treatmentgroupvalues", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listTreatmentGroupValuesJsonArray() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<TreatmentGroupValue> result = TreatmentGroupValue.findAllTreatmentGroupValues();
        for (TreatmentGroupValue treatmentGroupValue: result) {
        	if (treatmentGroupValue.isIgnored()) result.remove(treatmentGroupValue);
        }
        return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(result), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/subjectvalues", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listSubjectValuesJsonArray() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<SubjectValue> result = SubjectValue.findAllSubjectValues();
        for (SubjectValue subjectValue: result) {
        	if (subjectValue.isIgnored()) result.remove(subjectValue);
        }
        return new ResponseEntity<String>(SubjectValue.toJsonArray(result), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/lsthingvalues", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listLsThingValuesJsonArray() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<LsThingValue> result = LsThingValue.findAllLsThingValues();
        for (LsThingValue lsThingValue: result) {
        	if (lsThingValue.isIgnored()) result.remove(lsThingValue);
        }
        return new ResponseEntity<String>(LsThingValue.toJsonArray(result), headers, HttpStatus.OK);
    }
	
	//Show value json by id (GET)
	
		@RequestMapping(value = "/protocolvalues/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
		@ResponseBody
		@Transactional
	public ResponseEntity<String> showProtocolValueJson (@PathVariable("id") Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		ProtocolValue protocolValue = ProtocolValue.findProtocolValue(id);
        if (protocolValue == null || protocolValue.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        return new ResponseEntity<String>(protocolValue.toJson(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/experimentvalues/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> showExperimentValueJson (@PathVariable("id") Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		ExperimentValue experimentValue = ExperimentValue.findExperimentValue(id);
        if (experimentValue == null || experimentValue.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        return new ResponseEntity<String>(experimentValue.toJson(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/analysisgroupvalues/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> showAnalysisGroupValueJson (@PathVariable("id") Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.findAnalysisGroupValue(id);
        if (analysisGroupValue == null || analysisGroupValue.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        return new ResponseEntity<String>(analysisGroupValue.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/treatmentgroupvalues/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> showTreatmentGroupValueJson (@PathVariable("id") Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		TreatmentGroupValue treatmentGroupValue = TreatmentGroupValue.findTreatmentGroupValue(id);
        if (treatmentGroupValue == null || treatmentGroupValue.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        return new ResponseEntity<String>(treatmentGroupValue.toJson(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/subjectvalues/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> showSubjectValueJson (@PathVariable("id") Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		SubjectValue subjectValue = SubjectValue.findSubjectValue(id);
        if (subjectValue == null || subjectValue.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        return new ResponseEntity<String>(subjectValue.toJson(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/lsthingvalues/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> showLsThingValueJson (@PathVariable("id") Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		LsThingValue lsThingValue = LsThingValue.findLsThingValue(id);
        if (lsThingValue == null || lsThingValue.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        return new ResponseEntity<String>(lsThingValue.toJson(), headers, HttpStatus.OK);
	}	
	
	
	//Update value from json (PUT)
	@RequestMapping(value = "/protocolvalues", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateProtocolValueFromJson (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		ProtocolValue protocolValue = ProtocolValue.fromJsonToProtocolValue(json);
		if (ProtocolValue.findProtocolValue(protocolValue.getId()) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		protocolValue = protocolValueService.updateProtocolValue(protocolValue);
        return new ResponseEntity<String>(protocolValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/experimentvalues", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateExperimentValueFromJson (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		ExperimentValue experimentValue = ExperimentValue.fromJsonToExperimentValue(json);
		if (ExperimentValue.findExperimentValue(experimentValue.getId()) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		experimentValue = experimentValueService.updateExperimentValue(experimentValue);
        return new ResponseEntity<String>(experimentValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/analysisgroupvalues", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateAnalysisGroupValueFromJson (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.fromJsonToAnalysisGroupValue(json);
		if (AnalysisGroupValue.findAnalysisGroupValue(analysisGroupValue.getId()) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		analysisGroupValue = analysisGroupValueService.updateAnalysisGroupValue(analysisGroupValue);
        return new ResponseEntity<String>(analysisGroupValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/treatmentgroupvalues", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateTreatmentGroupValueFromJson (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		TreatmentGroupValue treatmentGroupValue = TreatmentGroupValue.fromJsonToTreatmentGroupValue(json);
		if (TreatmentGroupValue.findTreatmentGroupValue(treatmentGroupValue.getId()) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		treatmentGroupValue = treatmentGroupValueService.updateTreatmentGroupValue(treatmentGroupValue);
        return new ResponseEntity<String>(treatmentGroupValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/subjectvalues", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateSubjectValueFromJson (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		SubjectValue subjectValue = SubjectValue.fromJsonToSubjectValue(json);
		if (SubjectValue.findSubjectValue(subjectValue.getId()) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		subjectValue = subjectValueService.updateSubjectValue(subjectValue);
        return new ResponseEntity<String>(subjectValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/lsthingvalues", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateLsThingValueFromJson (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		LsThingValue lsThingValue = LsThingValue.fromJsonToLsThingValue(json);
		if (LsThingValue.findLsThingValue(lsThingValue.getId()) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		lsThingValue = lsThingValueService.updateLsThingValue(lsThingValue);
        return new ResponseEntity<String>(lsThingValue.toJson(),headers, HttpStatus.OK);
	}
	
	//Update values from jsonArray (PUT)
	
	@RequestMapping(value = "/protocolvalues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateProtocolValuesFromJsonArray (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<ProtocolValue> protocolValues = ProtocolValue.fromJsonArrayToProtocolValues(json);
		protocolValues = protocolValueService.updateProtocolValues(protocolValues);
        return new ResponseEntity<String>(ProtocolValue.toJsonArray(protocolValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/experimentvalues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateExperimentValuesFromJsonArray (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<ExperimentValue> experimentValues = ExperimentValue.fromJsonArrayToExperimentValues(json);
		experimentValues = experimentValueService.updateExperimentValues(experimentValues);
        return new ResponseEntity<String>(ExperimentValue.toJsonArray(experimentValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/analysisgroupvalues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateAnalysisGroupValuesFromJsonArray (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.fromJsonArrayToAnalysisGroupValues(json);
		analysisGroupValues = analysisGroupValueService.updateAnalysisGroupValues(analysisGroupValues);
        return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(analysisGroupValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/treatmentgroupvalues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateTreatmentGroupValuesFromJsonArray (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<TreatmentGroupValue> treatmentGroupValues = TreatmentGroupValue.fromJsonArrayToTreatmentGroupValues(json);
		treatmentGroupValues = treatmentGroupValueService.updateTreatmentGroupValues(treatmentGroupValues);
        return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(treatmentGroupValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/subjectvalues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateSubjectValuesFromJsonArray (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<SubjectValue> subjectValues = SubjectValue.fromJsonArrayToSubjectValues(json);
		subjectValues = subjectValueService.updateSubjectValues(subjectValues);
        return new ResponseEntity<String>(SubjectValue.toJsonArray(subjectValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/lsthingvalues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateLsThingValuesFromJsonArray (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<LsThingValue> lsThingValues = LsThingValue.fromJsonArrayToLsThingValues(json);
		lsThingValues = lsThingValueService.updateLsThingValues(lsThingValues);
        return new ResponseEntity<String>(LsThingValue.toJsonArray(lsThingValues),headers, HttpStatus.OK);
	}
	
	
	//Create value from json (POST)
	
	@RequestMapping(value = "/protocolvalues", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createProtocolValueFromJson (@RequestBody String json) {
		ProtocolValue protocolValue = ProtocolValue.fromJsonToProtocolValue(json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		protocolValue = protocolValueService.saveProtocolValue(protocolValue);
        return new ResponseEntity<String>(protocolValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/experimentvalues", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createExperimentValueFromJson (@RequestBody String json) {
		ExperimentValue experimentValue = ExperimentValue.fromJsonToExperimentValue(json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		experimentValue = experimentValueService.saveExperimentValue(experimentValue);
        return new ResponseEntity<String>(experimentValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/analysisgroupvalues", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createAnalysisGroupValueFromJson (@RequestBody String json) {
		AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.fromJsonToAnalysisGroupValue(json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		analysisGroupValue = analysisGroupValueService.saveAnalysisGroupValue(analysisGroupValue);
        return new ResponseEntity<String>(analysisGroupValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/treatmentgroupvalues", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createTreatmentGroupValueFromJson (@RequestBody String json) {
		TreatmentGroupValue treatmentGroupValue = TreatmentGroupValue.fromJsonToTreatmentGroupValue(json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		treatmentGroupValue = treatmentGroupValueService.saveTreatmentGroupValue(treatmentGroupValue);
        return new ResponseEntity<String>(treatmentGroupValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/subjectvalues", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createSubjectValueFromJson (@RequestBody String json) {
		SubjectValue subjectValue = SubjectValue.fromJsonToSubjectValue(json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		subjectValue = subjectValueService.saveSubjectValue(subjectValue);
        return new ResponseEntity<String>(subjectValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/lsthingvalues", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createLsThingValueFromJson (@RequestBody String json) {
		LsThingValue lsThingValue = LsThingValue.fromJsonToLsThingValue(json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		lsThingValue = lsThingValueService.saveLsThingValue(lsThingValue);
        return new ResponseEntity<String>(lsThingValue.toJson(),headers, HttpStatus.OK);
	}
	
	//Create values from jsonArray (POST)
	
	@RequestMapping(value = "/protocolvalues/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createProtocolValuesFromJsonArray (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<ProtocolValue> protocolValues = ProtocolValue.fromJsonArrayToProtocolValues(json);
		protocolValues = protocolValueService.saveProtocolValues(protocolValues);
        return new ResponseEntity<String>(ProtocolValue.toJsonArray(protocolValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/experimentvalues/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createExperimentValuesFromJsonArray (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<ExperimentValue> experimentValues = ExperimentValue.fromJsonArrayToExperimentValues(json);
		experimentValues = experimentValueService.saveExperimentValues(experimentValues);
        return new ResponseEntity<String>(ExperimentValue.toJsonArray(experimentValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/analysisgroupvalues/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createAnalysisGroupValuesFromJsonArray (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.fromJsonArrayToAnalysisGroupValues(json);
		analysisGroupValues = analysisGroupValueService.saveAnalysisGroupValues(analysisGroupValues);
        return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(analysisGroupValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/treatmentgroupvalues/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createTreatmentGroupValuesFromJsonArray (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<TreatmentGroupValue> treatmentGroupValues = TreatmentGroupValue.fromJsonArrayToTreatmentGroupValues(json);
		treatmentGroupValues = treatmentGroupValueService.saveTreatmentGroupValues(treatmentGroupValues);
        return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(treatmentGroupValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/subjectvalues/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createSubjectValuesFromJsonArray (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<SubjectValue> subjectValues = SubjectValue.fromJsonArrayToSubjectValues(json);
		subjectValues = subjectValueService.saveSubjectValues(subjectValues);
        return new ResponseEntity<String>(SubjectValue.toJsonArray(subjectValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/lsthingvalues/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createLsThingValuesFromJsonArray (@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<LsThingValue> lsThingValues = LsThingValue.fromJsonArrayToLsThingValues(json);
		lsThingValues = lsThingValueService.saveLsThingValues(lsThingValues);
        return new ResponseEntity<String>(LsThingValue.toJsonArray(lsThingValues),headers, HttpStatus.OK);
	}
	
	
}