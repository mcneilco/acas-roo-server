package com.labsynch.labseer.api;

import java.util.ArrayList;
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
@RequestMapping("api/v1/values")
@RooWebFinder
@Transactional
@RooWebJson(jsonObject = AbstractValue.class)
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

	@RequestMapping(value = "/{entity}/{idOrCodeName}/bystate/{stateType}/{stateKind}/byvalue/{valueType}/{valueKind}/", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> putlsThingValueByJson (
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
	
	
	
	
	public static boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) return false;
		}
		return true;
	}
	
}