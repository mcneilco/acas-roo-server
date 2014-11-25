package com.labsynch.labseer.api;

import java.util.Collection;
import java.util.List;

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
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.service.AnalysisGroupService;
import com.labsynch.labseer.service.AnalysisGroupStateService;
import com.labsynch.labseer.service.ExperimentService;
import com.labsynch.labseer.service.ExperimentStateService;
import com.labsynch.labseer.service.LsThingStateService;
import com.labsynch.labseer.service.ProtocolStateService;
import com.labsynch.labseer.service.SubjectStateService;
import com.labsynch.labseer.service.TreatmentGroupStateService;

@Controller
@RequestMapping("api/v1")
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

//ListStatesJsonArray

@RequestMapping(value = "/protocolstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listProtocolStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<ProtocolState> result = ProtocolState.findAllProtocolStates();
    for (ProtocolState protocolState: result) {
    	if (protocolState.isIgnored()) result.remove(protocolState);
    }
    return new ResponseEntity<String>(ProtocolState.toJsonArray(result), headers, HttpStatus.OK);
}

@RequestMapping(value = "/experimentstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listExperimentStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<ExperimentState> result = ExperimentState.findAllExperimentStates();
    for (ExperimentState experimentState: result) {
    	if (experimentState.isIgnored()) result.remove(experimentState);
    }
    return new ResponseEntity<String>(ExperimentState.toJsonArray(result), headers, HttpStatus.OK);
}

@RequestMapping(value = "/analysisgroupstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listAnalysisGroupStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<AnalysisGroupState> result = AnalysisGroupState.findAllAnalysisGroupStates();
    for (AnalysisGroupState analysisGroupState: result) {
    	if (analysisGroupState.isIgnored()) result.remove(analysisGroupState);
    }
    return new ResponseEntity<String>(AnalysisGroupState.toJsonArray(result), headers, HttpStatus.OK);
}

@RequestMapping(value = "/treatmentgroupstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listTreatmentGroupStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<TreatmentGroupState> result = TreatmentGroupState.findAllTreatmentGroupStates();
    for (TreatmentGroupState treatmentGroupState: result) {
    	if (treatmentGroupState.isIgnored()) result.remove(treatmentGroupState);
    }
    return new ResponseEntity<String>(TreatmentGroupState.toJsonArray(result), headers, HttpStatus.OK);
}

@RequestMapping(value = "/subjectstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listSubjectStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<SubjectState> result = SubjectState.findAllSubjectStates();
    for (SubjectState subjectState: result) {
    	if (subjectState.isIgnored()) result.remove(subjectState);
    }
    return new ResponseEntity<String>(SubjectState.toJsonArray(result), headers, HttpStatus.OK);
}

@RequestMapping(value = "/lsthingstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listLsThingStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<LsThingState> result = LsThingState.findAllLsThingStates();
    for (LsThingState lsThingState: result) {
    	if (lsThingState.isIgnored()) result.remove(lsThingState);
    }
    return new ResponseEntity<String>(LsThingState.toJsonArray(result), headers, HttpStatus.OK);
}

//showStateJson

@RequestMapping(value = "/protocolstates/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> showProtocolStateJson (@PathVariable("id") Long id) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
ProtocolState protocolState = ProtocolState.findProtocolState(id);
if (protocolState == null || protocolState.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
return new ResponseEntity<String>(protocolState.toJson(), headers, HttpStatus.OK);
}

@RequestMapping(value = "/experimentstates/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> showExperimentStateJson (@PathVariable("id") Long id) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
ExperimentState experimentState = ExperimentState.findExperimentState(id);
if (experimentState == null || experimentState.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
return new ResponseEntity<String>(experimentState.toJson(), headers, HttpStatus.OK);
}

@RequestMapping(value = "/analysisgroupstates/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> showAnalysisGroupStateJson (@PathVariable("id") Long id) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
AnalysisGroupState analysisGroupState = AnalysisGroupState.findAnalysisGroupState(id);
if (analysisGroupState == null || analysisGroupState.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
return new ResponseEntity<String>(analysisGroupState.toJson(), headers, HttpStatus.OK);
}

@RequestMapping(value = "/treatmentgroupstates/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> showTreatmentGroupStateJson (@PathVariable("id") Long id) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
TreatmentGroupState treatmentGroupState = TreatmentGroupState.findTreatmentGroupState(id);
if (treatmentGroupState == null || treatmentGroupState.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
return new ResponseEntity<String>(treatmentGroupState.toJson(), headers, HttpStatus.OK);
}

@RequestMapping(value = "/subjectstates/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> showSubjectStateJson (@PathVariable("id") Long id) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
SubjectState subjectState = SubjectState.findSubjectState(id);
if (subjectState == null || subjectState.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
return new ResponseEntity<String>(subjectState.toJson(), headers, HttpStatus.OK);
}

@RequestMapping(value = "/lsthingstates/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> showLsThingStateJson (@PathVariable("id") Long id) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
LsThingState lsThingState = LsThingState.findLsThingState(id);
if (lsThingState == null || lsThingState.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
return new ResponseEntity<String>(lsThingState.toJson(), headers, HttpStatus.OK);
}	


//Update state from json (PUT)
@RequestMapping(value = "/protocolstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateProtocolStateFromJson (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
ProtocolState protocolState = ProtocolState.fromJsonToProtocolState(json);
if (ProtocolState.findProtocolState(protocolState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
protocolState = protocolStateService.updateProtocolState(protocolState);
return new ResponseEntity<String>(protocolState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/experimentstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateExperimentStateFromJson (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
ExperimentState experimentState = ExperimentState.fromJsonToExperimentState(json);
if (ExperimentState.findExperimentState(experimentState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
experimentState = experimentStateService.updateExperimentState(experimentState);
return new ResponseEntity<String>(experimentState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/analysisgroupstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateAnalysisGroupStateFromJson (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
AnalysisGroupState analysisGroupState = AnalysisGroupState.fromJsonToAnalysisGroupState(json);
if (AnalysisGroupState.findAnalysisGroupState(analysisGroupState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
analysisGroupState = analysisGroupStateService.updateAnalysisGroupState(analysisGroupState);
return new ResponseEntity<String>(analysisGroupState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/treatmentgroupstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateTreatmentGroupStateFromJson (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
TreatmentGroupState treatmentGroupState = TreatmentGroupState.fromJsonToTreatmentGroupState(json);
if (TreatmentGroupState.findTreatmentGroupState(treatmentGroupState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
treatmentGroupState = treatmentGroupStateService.updateTreatmentGroupState(treatmentGroupState);
return new ResponseEntity<String>(treatmentGroupState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/subjectstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateSubjectStateFromJson (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
SubjectState subjectState = SubjectState.fromJsonToSubjectState(json);
if (SubjectState.findSubjectState(subjectState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
subjectState = subjectStateService.updateSubjectState(subjectState);
return new ResponseEntity<String>(subjectState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/lsthingstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateLsThingStateFromJson (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
LsThingState lsThingState = LsThingState.fromJsonToLsThingState(json);
if (LsThingState.findLsThingState(lsThingState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
lsThingState = lsThingStateService.updateLsThingState(lsThingState);
return new ResponseEntity<String>(lsThingState.toJson(),headers, HttpStatus.OK);
}

//Update states from jsonArray (PUT)

@RequestMapping(value = "/protocolstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateProtocolStatesFromJsonArray (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
Collection<ProtocolState> protocolStates = ProtocolState.fromJsonArrayToProtocolStates(json);
protocolStates = protocolStateService.updateProtocolStates(protocolStates);
return new ResponseEntity<String>(ProtocolState.toJsonArray(protocolStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/experimentstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateExperimentStatesFromJsonArray (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
Collection<ExperimentState> experimentStates = ExperimentState.fromJsonArrayToExperimentStates(json);
experimentStates = experimentStateService.updateExperimentStates(experimentStates);
return new ResponseEntity<String>(ExperimentState.toJsonArray(experimentStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/analysisgroupstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateAnalysisGroupStatesFromJsonArray (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
Collection<AnalysisGroupState> analysisGroupStates = AnalysisGroupState.fromJsonArrayToAnalysisGroupStates(json);
analysisGroupStates = analysisGroupStateService.updateAnalysisGroupStates(analysisGroupStates);
return new ResponseEntity<String>(AnalysisGroupState.toJsonArray(analysisGroupStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/treatmentgroupstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateTreatmentGroupStatesFromJsonArray (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
Collection<TreatmentGroupState> treatmentGroupStates = TreatmentGroupState.fromJsonArrayToTreatmentGroupStates(json);
treatmentGroupStates = treatmentGroupStateService.updateTreatmentGroupStates(treatmentGroupStates);
return new ResponseEntity<String>(TreatmentGroupState.toJsonArray(treatmentGroupStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/subjectstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateSubjectStatesFromJsonArray (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
Collection<SubjectState> subjectStates = SubjectState.fromJsonArrayToSubjectStates(json);
subjectStates = subjectStateService.updateSubjectStates(subjectStates);
return new ResponseEntity<String>(SubjectState.toJsonArray(subjectStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/lsthingstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateLsThingStatesFromJsonArray (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
Collection<LsThingState> lsThingStates = LsThingState.fromJsonArrayToLsThingStates(json);
lsThingStates = lsThingStateService.updateLsThingStates(lsThingStates);
return new ResponseEntity<String>(LsThingState.toJsonArray(lsThingStates),headers, HttpStatus.OK);
}


//Create state from json (POST)

@RequestMapping(value = "/protocolstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createProtocolStateFromJson (@RequestBody String json) {
ProtocolState protocolState = ProtocolState.fromJsonToProtocolState(json);
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
protocolState = protocolStateService.saveProtocolState(protocolState);
return new ResponseEntity<String>(protocolState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/experimentstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createExperimentStateFromJson (@RequestBody String json) {
ExperimentState experimentState = ExperimentState.fromJsonToExperimentState(json);
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
experimentState = experimentStateService.saveExperimentState(experimentState);
return new ResponseEntity<String>(experimentState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/analysisgroupstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createAnalysisGroupStateFromJson (@RequestBody String json) {
AnalysisGroupState analysisGroupState = AnalysisGroupState.fromJsonToAnalysisGroupState(json);
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
analysisGroupState = analysisGroupStateService.saveAnalysisGroupState(analysisGroupState);
return new ResponseEntity<String>(analysisGroupState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/treatmentgroupstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createTreatmentGroupStateFromJson (@RequestBody String json) {
TreatmentGroupState treatmentGroupState = TreatmentGroupState.fromJsonToTreatmentGroupState(json);
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
treatmentGroupState = treatmentGroupStateService.saveTreatmentGroupState(treatmentGroupState);
return new ResponseEntity<String>(treatmentGroupState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/subjectstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createSubjectStateFromJson (@RequestBody String json) {
SubjectState subjectState = SubjectState.fromJsonToSubjectState(json);
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
subjectState = subjectStateService.saveSubjectState(subjectState);
return new ResponseEntity<String>(subjectState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/lsthingstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createLsThingStateFromJson (@RequestBody String json) {
LsThingState lsThingState = LsThingState.fromJsonToLsThingState(json);
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
lsThingState = lsThingStateService.saveLsThingState(lsThingState);
return new ResponseEntity<String>(lsThingState.toJson(),headers, HttpStatus.OK);
}

//Create states from jsonArray (POST)

@RequestMapping(value = "/protocolstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createProtocolStatesFromJsonArray (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
Collection<ProtocolState> protocolStates = ProtocolState.fromJsonArrayToProtocolStates(json);
protocolStates = protocolStateService.saveProtocolStates(protocolStates);
return new ResponseEntity<String>(ProtocolState.toJsonArray(protocolStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/experimentstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createExperimentStatesFromJsonArray (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
Collection<ExperimentState> experimentStates = ExperimentState.fromJsonArrayToExperimentStates(json);
experimentStates = experimentStateService.saveExperimentStates(experimentStates);
return new ResponseEntity<String>(ExperimentState.toJsonArray(experimentStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/analysisgroupstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createAnalysisGroupStatesFromJsonArray (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
Collection<AnalysisGroupState> analysisGroupStates = AnalysisGroupState.fromJsonArrayToAnalysisGroupStates(json);
analysisGroupStates = analysisGroupStateService.saveAnalysisGroupStates(analysisGroupStates);
return new ResponseEntity<String>(AnalysisGroupState.toJsonArray(analysisGroupStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/treatmentgroupstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createTreatmentGroupStatesFromJsonArray (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
Collection<TreatmentGroupState> treatmentGroupStates = TreatmentGroupState.fromJsonArrayToTreatmentGroupStates(json);
treatmentGroupStates = treatmentGroupStateService.saveTreatmentGroupStates(treatmentGroupStates);
return new ResponseEntity<String>(TreatmentGroupState.toJsonArray(treatmentGroupStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/subjectstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createSubjectStatesFromJsonArray (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
Collection<SubjectState> subjectStates = SubjectState.fromJsonArrayToSubjectStates(json);
subjectStates = subjectStateService.saveSubjectStates(subjectStates);
return new ResponseEntity<String>(SubjectState.toJsonArray(subjectStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/lsthingstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createLsThingStatesFromJsonArray (@RequestBody String json) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
Collection<LsThingState> lsThingStates = LsThingState.fromJsonArrayToLsThingStates(json);
lsThingStates = lsThingStateService.saveLsThingStates(lsThingStates);
return new ResponseEntity<String>(LsThingState.toJsonArray(lsThingStates),headers, HttpStatus.OK);
}

}
