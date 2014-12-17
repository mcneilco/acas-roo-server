package com.labsynch.labseer.api;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.AbstractState;
import com.labsynch.labseer.domain.AbstractValue;
import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.ContainerValue;
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
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.KeyValueDTO;
import com.labsynch.labseer.dto.StateValueDTO;
import com.labsynch.labseer.service.AnalysisGroupService;
import com.labsynch.labseer.service.AnalysisGroupValueService;
import com.labsynch.labseer.service.ContainerValueService;
import com.labsynch.labseer.service.ExperimentService;
import com.labsynch.labseer.service.ExperimentStateService;
import com.labsynch.labseer.service.ExperimentValueService;
import com.labsynch.labseer.service.LsThingValueService;
import com.labsynch.labseer.service.ProtocolValueService;
import com.labsynch.labseer.service.SubjectValueService;
import com.labsynch.labseer.service.TreatmentGroupValueService;

import flexjson.JSONDeserializer;

@Controller
@RequestMapping("api/v1")
@Transactional
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
	
	@Autowired
	private ContainerValueService containerValueService;

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
        List<ProtocolValue> finalResult = new ArrayList<ProtocolValue>();
        for (ProtocolValue protocolValue: result) {
        	if (!protocolValue.isIgnored()) finalResult.add(protocolValue);
        }
        return new ResponseEntity<String>(ProtocolValue.toJsonArray(finalResult), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/experimentvalues", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listExperimentValuesJsonArray() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ExperimentValue> result = ExperimentValue.findAllExperimentValues();
        List<ExperimentValue> finalResult = new ArrayList<ExperimentValue>();
        for (ExperimentValue experimentValue: result) {
        	if (!experimentValue.isIgnored()) finalResult.add(experimentValue);
        }
        return new ResponseEntity<String>(ExperimentValue.toJsonArray(finalResult), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/analysisgroupvalues", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listAnalysisGroupValuesJsonArray() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<AnalysisGroupValue> result = AnalysisGroupValue.findAllAnalysisGroupValues();
        List<AnalysisGroupValue> finalResult = new ArrayList<AnalysisGroupValue>();
        for (AnalysisGroupValue analysisGroupValue: result) {
        	if (!analysisGroupValue.isIgnored()) finalResult.add(analysisGroupValue);
        }
        return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(finalResult), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/treatmentgroupvalues", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listTreatmentGroupValuesJsonArray() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<TreatmentGroupValue> result = TreatmentGroupValue.findAllTreatmentGroupValues();
        List<TreatmentGroupValue> finalResult = new ArrayList<TreatmentGroupValue>();
        for (TreatmentGroupValue treatmentGroupValue: result) {
        	if (!treatmentGroupValue.isIgnored()) finalResult.add(treatmentGroupValue);
        }
        return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(finalResult), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/subjectvalues", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listSubjectValuesJsonArray() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<SubjectValue> result = SubjectValue.findAllSubjectValues();
        List<SubjectValue> finalResult = new ArrayList<SubjectValue>();
        for (SubjectValue subjectValue: result) {
        	if (!subjectValue.isIgnored()) finalResult.add(subjectValue);
        }
        return new ResponseEntity<String>(SubjectValue.toJsonArray(finalResult), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/lsthingvalues", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listLsThingValuesJsonArray() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<LsThingValue> result = LsThingValue.findAllLsThingValues();
        List<LsThingValue> finalResult = new ArrayList<LsThingValue>();
        for (LsThingValue lsThingValue: result) {
        	if (!lsThingValue.isIgnored()) finalResult.add(lsThingValue);
        }
        return new ResponseEntity<String>(LsThingValue.toJsonArray(finalResult), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/containervalues", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listContainerValuesJsonArray() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ContainerValue> result = ContainerValue.findAllContainerValues();
        List<ContainerValue> finalResult = new ArrayList<ContainerValue>();
        for (ContainerValue containerValue: result) {
        	if (!containerValue.isIgnored()) finalResult.add(containerValue);
        }
        return new ResponseEntity<String>(ContainerValue.toJsonArray(finalResult), headers, HttpStatus.OK);
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
	
	@RequestMapping(value = "/containervalues/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> showContainerValueJson (@PathVariable("id") Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		ContainerValue containerValue = ContainerValue.findContainerValue(id);
        if (containerValue == null || containerValue.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        return new ResponseEntity<String>(containerValue.toJson(), headers, HttpStatus.OK);
	}
	
	//Update value from json (PUT)
	@RequestMapping(value = "/protocolvalues", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateProtocolValueFromJson (@RequestBody ProtocolValue protocolValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (ProtocolValue.findProtocolValue(protocolValue.getId()) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		protocolValue = protocolValueService.updateProtocolValue(protocolValue);
        return new ResponseEntity<String>(protocolValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/experimentvalues", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateExperimentValueFromJson (@RequestBody ExperimentValue experimentValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (ExperimentValue.findExperimentValue(experimentValue.getId()) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		experimentValue = experimentValueService.updateExperimentValue(experimentValue);
        return new ResponseEntity<String>(experimentValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/analysisgroupvalues", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateAnalysisGroupValueFromJson (@RequestBody AnalysisGroupValue analysisGroupValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (AnalysisGroupValue.findAnalysisGroupValue(analysisGroupValue.getId()) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		analysisGroupValue = analysisGroupValueService.updateAnalysisGroupValue(analysisGroupValue);
        return new ResponseEntity<String>(analysisGroupValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/treatmentgroupvalues", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateTreatmentGroupValueFromJson (@RequestBody TreatmentGroupValue treatmentGroupValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (TreatmentGroupValue.findTreatmentGroupValue(treatmentGroupValue.getId()) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		treatmentGroupValue = treatmentGroupValueService.updateTreatmentGroupValue(treatmentGroupValue);
        return new ResponseEntity<String>(treatmentGroupValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/subjectvalues", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateSubjectValueFromJson (@RequestBody SubjectValue subjectValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (SubjectValue.findSubjectValue(subjectValue.getId()) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		subjectValue = subjectValueService.updateSubjectValue(subjectValue);
        return new ResponseEntity<String>(subjectValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/lsthingvalues", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateLsThingValueFromJson (@RequestBody LsThingValue lsThingValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (LsThingValue.findLsThingValue(lsThingValue.getId()) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		lsThingValue = lsThingValueService.updateLsThingValue(lsThingValue);
        return new ResponseEntity<String>(lsThingValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/containervalues", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateContainerValueFromJson (@RequestBody ContainerValue containerValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (ContainerValue.findContainerValue(containerValue.getId()) == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		containerValue = containerValueService.updateContainerValue(containerValue);
        return new ResponseEntity<String>(containerValue.toJson(),headers, HttpStatus.OK);
	}
	
	//Update values from jsonArray (PUT)
	
	@RequestMapping(value = "/protocolvalues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateProtocolValuesFromJsonArray (@RequestBody List<ProtocolValue> protocolValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		protocolValues = (List<ProtocolValue>) protocolValueService.updateProtocolValues(protocolValues);
        return new ResponseEntity<String>(ProtocolValue.toJsonArray(protocolValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/experimentvalues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateExperimentValuesFromJsonArray (@RequestBody List<ExperimentValue> experimentValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		experimentValues = (List<ExperimentValue>) experimentValueService.updateExperimentValues(experimentValues);
        return new ResponseEntity<String>(ExperimentValue.toJsonArray(experimentValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/analysisgroupvalues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateAnalysisGroupValuesFromJsonArray (@RequestBody List<AnalysisGroupValue> analysisGroupValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		analysisGroupValues = (List<AnalysisGroupValue>) analysisGroupValueService.updateAnalysisGroupValues(analysisGroupValues);
        return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(analysisGroupValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/treatmentgroupvalues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateTreatmentGroupValuesFromJsonArray (@RequestBody List<TreatmentGroupValue> treatmentGroupValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		treatmentGroupValues = (List<TreatmentGroupValue>) treatmentGroupValueService.updateTreatmentGroupValues(treatmentGroupValues);
        return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(treatmentGroupValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/subjectvalues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateSubjectValuesFromJsonArray (@RequestBody List<SubjectValue> subjectValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		subjectValues = (List<SubjectValue>) subjectValueService.updateSubjectValues(subjectValues);
        return new ResponseEntity<String>(SubjectValue.toJsonArray(subjectValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/lsthingvalues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateLsThingValuesFromJsonArray (@RequestBody List<LsThingValue> lsThingValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		lsThingValues = (List<LsThingValue>) lsThingValueService.updateLsThingValues(lsThingValues);
        return new ResponseEntity<String>(LsThingValue.toJsonArray(lsThingValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/containervalues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> updateContainerValuesFromJsonArray (@RequestBody List<ContainerValue> containerValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		containerValues = (List<ContainerValue>) containerValueService.updateContainerValues(containerValues);
        return new ResponseEntity<String>(ContainerValue.toJsonArray(containerValues),headers, HttpStatus.OK);
	}
	
	//Create value from json (POST)
	
	@RequestMapping(value = "/protocolvalues", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createProtocolValueFromJson (@RequestBody ProtocolValue protocolValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		protocolValue = protocolValueService.saveProtocolValue(protocolValue);
        return new ResponseEntity<String>(protocolValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/experimentvalues", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createExperimentValueFromJson (@RequestBody ExperimentValue experimentValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		experimentValue = experimentValueService.saveExperimentValue(experimentValue);
        return new ResponseEntity<String>(experimentValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/analysisgroupvalues", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createAnalysisGroupValueFromJson (@RequestBody AnalysisGroupValue analysisGroupValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		analysisGroupValue = analysisGroupValueService.saveAnalysisGroupValue(analysisGroupValue);
        return new ResponseEntity<String>(analysisGroupValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/treatmentgroupvalues", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createTreatmentGroupValueFromJson (@RequestBody TreatmentGroupValue treatmentGroupValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		treatmentGroupValue = treatmentGroupValueService.saveTreatmentGroupValue(treatmentGroupValue);
        return new ResponseEntity<String>(treatmentGroupValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/subjectvalues", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createSubjectValueFromJson (@RequestBody SubjectValue subjectValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		subjectValue = subjectValueService.saveSubjectValue(subjectValue);
        return new ResponseEntity<String>(subjectValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/lsthingvalues", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createLsThingValueFromJson (@RequestBody LsThingValue lsThingValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		lsThingValue = lsThingValueService.saveLsThingValue(lsThingValue);
        return new ResponseEntity<String>(lsThingValue.toJson(),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/containervalues", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createContainerValueFromJson (@RequestBody ContainerValue containerValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		containerValue = containerValueService.saveContainerValue(containerValue);
        return new ResponseEntity<String>(containerValue.toJson(),headers, HttpStatus.OK);
	}
	
	//Create values from jsonArray (POST)
	
	@RequestMapping(value = "/protocolvalues/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createProtocolValuesFromJsonArray (@RequestBody List<ProtocolValue> protocolValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		protocolValues = (List<ProtocolValue>) protocolValueService.saveProtocolValues(protocolValues);
        return new ResponseEntity<String>(ProtocolValue.toJsonArray(protocolValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/experimentvalues/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createExperimentValuesFromJsonArray (@RequestBody List<ExperimentValue> experimentValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		experimentValues = (List<ExperimentValue>) experimentValueService.saveExperimentValues(experimentValues);
        return new ResponseEntity<String>(ExperimentValue.toJsonArray(experimentValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/analysisgroupvalues/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createAnalysisGroupValuesFromJsonArray (@RequestBody List<AnalysisGroupValue> analysisGroupValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		analysisGroupValues = (List<AnalysisGroupValue>) analysisGroupValueService.saveAnalysisGroupValues(analysisGroupValues);
        return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(analysisGroupValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/treatmentgroupvalues/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createTreatmentGroupValuesFromJsonArray (@RequestBody List<TreatmentGroupValue> treatmentGroupValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		treatmentGroupValues = (List<TreatmentGroupValue>) treatmentGroupValueService.saveTreatmentGroupValues(treatmentGroupValues);
        return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(treatmentGroupValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/subjectvalues/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createSubjectValuesFromJsonArray (@RequestBody List<SubjectValue> subjectValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		subjectValues = (List<SubjectValue>) subjectValueService.saveSubjectValues(subjectValues);
        return new ResponseEntity<String>(SubjectValue.toJsonArray(subjectValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/lsthingvalues/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createLsThingValuesFromJsonArray (@RequestBody List<LsThingValue> lsThingValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		lsThingValues = (List<LsThingValue>) lsThingValueService.saveLsThingValues(lsThingValues);
        return new ResponseEntity<String>(LsThingValue.toJsonArray(lsThingValues),headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/containervalues/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> createContainerValuesFromJsonArray (@RequestBody List<ContainerValue> containerValues) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		containerValues = (List<ContainerValue>) containerValueService.saveContainerValues(containerValues);
        return new ResponseEntity<String>(ContainerValue.toJsonArray(containerValues),headers, HttpStatus.OK);
	}
		
	
}