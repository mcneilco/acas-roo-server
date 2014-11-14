package com.labsynch.labseer.api;

import java.util.Collection;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.AbstractState;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.service.AnalysisGroupService;
import com.labsynch.labseer.service.AnalysisGroupStateService;
import com.labsynch.labseer.service.ExperimentService;
import com.labsynch.labseer.service.ExperimentStateService;
import com.labsynch.labseer.service.LsThingStateService;
import com.labsynch.labseer.service.ProtocolStateService;
import com.labsynch.labseer.service.SubjectStateService;
import com.labsynch.labseer.service.TreatmentGroupStateService;

@Controller
@RequestMapping("api/v1/states")
//@RooWebFinder
@Transactional
//@RooWebJson(jsonObject = AbstractState.class)
public class ApiStateController {
private static final Logger logger = LoggerFactory.getLogger(ApiStateController.class);

@Autowired
private ProtocolStateService protocolStateService;

@Autowired
private ExperimentStateService experimentStateService;

@Autowired
private AnalysisGroupStateService analysisGroupStateService;

@Autowired
private TreatmentGroupStateService treatmentGroupStateService;

@Autowired
private SubjectStateService subjectStateService;

@Autowired
private LsThingStateService lsThingStateService;

	@RequestMapping(value = "/{entity}/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getStateById (
			@PathVariable("entity") String entity,
			@PathVariable("id") Long id) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		logger.debug("ENTITY IS: " + entity);
		if (entity.equals("protocol")) {
			ProtocolState protocolState = ProtocolState.findProtocolState(id);
	        if (protocolState == null || protocolState.isIgnored()) {
	            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<String>(protocolState.toJson(), headers, HttpStatus.OK);
		}
		if (entity.equals("experiment")) {
			ExperimentState experimentState = ExperimentState.findExperimentState(id);
	        if (experimentState == null || experimentState.isIgnored()) {
	            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<String>(experimentState.toJson(), headers, HttpStatus.OK);
		}
		if (entity.equals("analysisGroup")) {
			AnalysisGroupState analysisGroupState = AnalysisGroupState.findAnalysisGroupState(id);
	        if (analysisGroupState == null || analysisGroupState.isIgnored()) {
	            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<String>(analysisGroupState.toJson(), headers, HttpStatus.OK);
		}
		if (entity.equals("treatmentGroup")) {
			TreatmentGroupState treatmentGroupState = TreatmentGroupState.findTreatmentGroupState(id);
	        if (treatmentGroupState == null || treatmentGroupState.isIgnored()) {
	            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<String>(treatmentGroupState.toJson(), headers, HttpStatus.OK);
		}
		if (entity.equals("subject")) {
			SubjectState subjectState = SubjectState.findSubjectState(id);
	        if (subjectState == null || subjectState.isIgnored()) {
	            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<String>(subjectState.toJson(), headers, HttpStatus.OK);
		}
		if (entity.equals("lsThing")) {
			LsThingState lsThingState = LsThingState.findLsThingState(id);
	        if (lsThingState == null || lsThingState.isIgnored()) {
	            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	        }
	        return new ResponseEntity<String>(lsThingState.toJson(), headers, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("INVALID ENTITY", headers, HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/{entity}", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> postStateByJson (
			@PathVariable("entity") String entity,
			@RequestBody String json) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		//this if/else if block controls which lsThing is being hit
		logger.debug("ENTITY IS: " + entity);
		if (entity.equals("protocol")) {
			ProtocolState protocolState = ProtocolState.fromJsonToProtocolState(json);
			protocolState = protocolStateService.saveProtocolState(protocolState);
	        return new ResponseEntity<String>(protocolState.toJson(),headers, HttpStatus.OK);
		}
		if (entity.equals("experiment")) {
			ExperimentState experimentState = ExperimentState.fromJsonToExperimentState(json);
			experimentState = experimentStateService.saveExperimentState(experimentState);
	        return new ResponseEntity<String>(experimentState.toJson(),headers, HttpStatus.OK);
		}
		if (entity.equals("analysisGroup")) {
			AnalysisGroupState analysisGroupState = AnalysisGroupState.fromJsonToAnalysisGroupState(json);
			analysisGroupState = analysisGroupStateService.saveAnalysisGroupState(analysisGroupState);
	        return new ResponseEntity<String>(analysisGroupState.toJson(),headers, HttpStatus.OK);
		}
		if (entity.equals("treatmentGroup")) {
			TreatmentGroupState treatmentGroupState = TreatmentGroupState.fromJsonToTreatmentGroupState(json);
			treatmentGroupState = treatmentGroupStateService.saveTreatmentGroupState(treatmentGroupState);
	        return new ResponseEntity<String>(treatmentGroupState.toJson(),headers, HttpStatus.OK);
		}
		if (entity.equals("subject")) {
			SubjectState subjectState = SubjectState.fromJsonToSubjectState(json);
			subjectState = subjectStateService.saveSubjectState(subjectState);
	        return new ResponseEntity<String>(subjectState.toJson(),headers, HttpStatus.OK);
		}
		if (entity.equals("lsThing")) {
			LsThingState lsThingState = LsThingState.fromJsonToLsThingState(json);
			lsThingState = lsThingStateService.saveLsThingState(lsThingState);
	        return new ResponseEntity<String>(lsThingState.toJson(),headers, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("INVALID ENTITY", headers, HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/{entity}/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> postStateByJsonArray (
			@PathVariable("entity") String entity,
			@RequestBody String json) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		//this if/else if block controls which lsThing is being hit
		logger.debug("ENTITY IS: " + entity);
		if (entity.equals("protocol")) {
			Collection<ProtocolState> protocolStates = ProtocolState.fromJsonArrayToProtocolStates(json);
			protocolStates = protocolStateService.saveProtocolStates(protocolStates);
	        return new ResponseEntity<String>(ProtocolState.toJsonArray(protocolStates),headers, HttpStatus.OK);
		}
		if (entity.equals("experiment")) {
			Collection<ExperimentState> experimentStates = ExperimentState.fromJsonArrayToExperimentStates(json);
			experimentStates = experimentStateService.saveExperimentStates(experimentStates);
	        return new ResponseEntity<String>(ExperimentState.toJsonArray(experimentStates),headers, HttpStatus.OK);
		}
		if (entity.equals("analysisGroup")) {
			Collection<AnalysisGroupState> analysisGroupStates = AnalysisGroupState.fromJsonArrayToAnalysisGroupStates(json);
			analysisGroupStates = analysisGroupStateService.saveAnalysisGroupStates(analysisGroupStates);
	        return new ResponseEntity<String>(AnalysisGroupState.toJsonArray(analysisGroupStates),headers, HttpStatus.OK);
		}
		if (entity.equals("treatmentGroup")) {
			Collection<TreatmentGroupState> treatmentGroupStates = TreatmentGroupState.fromJsonArrayToTreatmentGroupStates(json);
			treatmentGroupStates = treatmentGroupStateService.saveTreatmentGroupStates(treatmentGroupStates);
	        return new ResponseEntity<String>(TreatmentGroupState.toJsonArray(treatmentGroupStates),headers, HttpStatus.OK);
		}
		if (entity.equals("subject")) {
			Collection<SubjectState> subjectStates = SubjectState.fromJsonArrayToSubjectStates(json);
			subjectStates = subjectStateService.saveSubjectStates(subjectStates);
	        return new ResponseEntity<String>(SubjectState.toJsonArray(subjectStates),headers, HttpStatus.OK);
		}
		if (entity.equals("lsThing")) {
			Collection<LsThingState> lsThingStates = LsThingState.fromJsonArrayToLsThingStates(json);
			lsThingStates = lsThingStateService.saveLsThingStates(lsThingStates);
	        return new ResponseEntity<String>(LsThingState.toJsonArray(lsThingStates),headers, HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("INVALID ENTITY", headers, HttpStatus.BAD_REQUEST);
	}
}
