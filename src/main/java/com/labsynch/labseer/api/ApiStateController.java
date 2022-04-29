package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ItxContainerContainerState;
import com.labsynch.labseer.domain.ItxExperimentExperimentState;
import com.labsynch.labseer.domain.ItxProtocolProtocolState;
import com.labsynch.labseer.domain.ItxSubjectContainerState;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.dto.AnalysisGroupStatePathDTO;
import com.labsynch.labseer.dto.ContainerStatePathDTO;
import com.labsynch.labseer.dto.ExperimentStatePathDTO;
import com.labsynch.labseer.dto.GenericStatePathRequest;
import com.labsynch.labseer.dto.LsThingStatePathDTO;
import com.labsynch.labseer.dto.ProtocolStatePathDTO;
import com.labsynch.labseer.dto.SubjectStatePathDTO;
import com.labsynch.labseer.dto.TreatmentGroupStatePathDTO;
import com.labsynch.labseer.service.AnalysisGroupStateService;
import com.labsynch.labseer.service.ContainerStateService;
import com.labsynch.labseer.service.ExperimentStateService;
import com.labsynch.labseer.service.ItxContainerContainerStateService;
import com.labsynch.labseer.service.ItxExperimentExperimentStateService;
import com.labsynch.labseer.service.ItxProtocolProtocolStateService;
import com.labsynch.labseer.service.ItxSubjectContainerStateService;
import com.labsynch.labseer.service.LsThingStateService;
import com.labsynch.labseer.service.ProtocolStateService;
import com.labsynch.labseer.service.SubjectStateService;
import com.labsynch.labseer.service.TreatmentGroupStateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api/v1")
@Transactional
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

@Autowired
private ContainerStateService containerStateService;

@Autowired
private ItxContainerContainerStateService itxContainerContainerStateService;

@Autowired
private ItxExperimentExperimentStateService itxExperimentExperimentStateService;

@Autowired
private ItxProtocolProtocolStateService itxProtocolProtocolStateService;

@Autowired
private ItxSubjectContainerStateService itxSubjectContainerStateService;

@RequestMapping(value = "/states/{entity}/{idOrCodeName}/bystate/{stateType}/{stateKind}", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> getStateByPath (
		@PathVariable("entity") String entity,
		@PathVariable("idOrCodeName") String idOrCodeName,
		@PathVariable("stateType") String stateType,
		@PathVariable("stateKind") String stateKind) {

	HttpHeaders headers = new HttpHeaders();
	headers.add("Content-Type", "application/json; charset=utf-8");
	//this if/else if block controls which lsThing is being hit
	logger.debug("ENTITY IS: " + entity);
	if (entity.equals("protocol")) {
		ProtocolState protocolState = protocolStateService.getProtocolState(idOrCodeName, stateType, stateKind);
		if (protocolState==null) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		else return new ResponseEntity<String>(protocolState.toJson(), headers, HttpStatus.OK);
	}
	if (entity.equals("experiment")) {
		ExperimentState experimentState = experimentStateService.getExperimentState(idOrCodeName, stateType, stateKind);
		if (experimentState==null) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		else return new ResponseEntity<String>(experimentState.toJson(), headers, HttpStatus.OK);
	}
	if (entity.equals("analysisGroup")) {
		AnalysisGroupState analysisGroupState = analysisGroupStateService.getAnalysisGroupState(idOrCodeName, stateType, stateKind);
		if (analysisGroupState==null) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		else return new ResponseEntity<String>(analysisGroupState.toJson(), headers, HttpStatus.OK);
	}
	if (entity.equals("treatmentGroup")) {
		TreatmentGroupState treatmentGroupState = treatmentGroupStateService.getTreatmentGroupState(idOrCodeName, stateType, stateKind);
		if (treatmentGroupState==null) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		else return new ResponseEntity<String>(treatmentGroupState.toJson(), headers, HttpStatus.OK);
	}
	if (entity.equals("subject")) {
		SubjectState subjectState = subjectStateService.getSubjectState(idOrCodeName, stateType, stateKind);
		if (subjectState==null) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		else return new ResponseEntity<String>(subjectState.toJson(), headers, HttpStatus.OK);
	}
	if (entity.equals("lsThing")) {
		LsThingState lsThingState = lsThingStateService.getLsThingState(idOrCodeName, stateType, stateKind);
		if (lsThingState==null) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		else return new ResponseEntity<String>(lsThingState.toJson(), headers, HttpStatus.OK);
	}
	if (entity.equals("container")) {
		ContainerState containerState = containerStateService.getContainerState(idOrCodeName, stateType, stateKind);
		if (containerState==null) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		else return new ResponseEntity<String>(containerState.toJson(), headers, HttpStatus.OK);
	}
	
	return new ResponseEntity<String>("INVALID ENTITY", headers, HttpStatus.BAD_REQUEST);
}

@RequestMapping(value = "/states/{entity}/getStatesByIdOrCodeNameAndStateTypeAndKind", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> getStatesByPaths (
		@PathVariable("entity") String entity,
		@RequestBody String json) {
	Collection<GenericStatePathRequest> genericRequests = GenericStatePathRequest.fromJsonArrayToGenericStatePathRequests(json);
	HttpHeaders headers = new HttpHeaders();
	headers.add("Content-Type", "application/json; charset=utf-8");
	//this if/else if block controls which lsThing is being hit
	logger.debug("ENTITY IS: " + entity);
	if (entity.equals("protocol")) {
		Collection<ProtocolStatePathDTO> results = protocolStateService.getProtocolStates(genericRequests);
		return new ResponseEntity<String>(ProtocolStatePathDTO.toJsonArray(results), headers, HttpStatus.OK);
	}
	if (entity.equals("experiment")) {
		Collection<ExperimentStatePathDTO> results = experimentStateService.getExperimentStates(genericRequests);
		return new ResponseEntity<String>(ExperimentStatePathDTO.toJsonArray(results), headers, HttpStatus.OK);
	}
	if (entity.equals("analysisGroup")) {
		Collection<AnalysisGroupStatePathDTO> results = analysisGroupStateService.getAnalysisGroupStates(genericRequests);
		return new ResponseEntity<String>(AnalysisGroupStatePathDTO.toJsonArray(results), headers, HttpStatus.OK);
	}
	if (entity.equals("treatmentGroup")) {
		Collection<TreatmentGroupStatePathDTO> results = treatmentGroupStateService.getTreatmentGroupStates(genericRequests);
		return new ResponseEntity<String>(TreatmentGroupStatePathDTO.toJsonArray(results), headers, HttpStatus.OK);
	}
	if (entity.equals("subject")) {
		Collection<SubjectStatePathDTO> results = subjectStateService.getSubjectStates(genericRequests);
		return new ResponseEntity<String>(SubjectStatePathDTO.toJsonArray(results), headers, HttpStatus.OK);
	}
	if (entity.equals("lsThing")) {
		Collection<LsThingStatePathDTO> results = lsThingStateService.getLsThingStates(genericRequests);
		return new ResponseEntity<String>(LsThingStatePathDTO.toJsonArray(results), headers, HttpStatus.OK);
	}
	if (entity.equals("container")) {
		Collection<ContainerStatePathDTO> results = containerStateService.getContainerStates(genericRequests);
		return new ResponseEntity<String>(ContainerStatePathDTO.toJsonArray(results), headers, HttpStatus.OK);
	}
	
	return new ResponseEntity<String>("INVALID ENTITY", headers, HttpStatus.BAD_REQUEST);
}

//ListStatesJsonArray

@RequestMapping(value = "/protocolstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listProtocolStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<ProtocolState> result = ProtocolState.findAllProtocolStates();
    List<ProtocolState> finalResult = new ArrayList<ProtocolState>();
    for (ProtocolState protocolState: result) {
    	if (!protocolState.isIgnored()) finalResult.add(protocolState);
    }
    return new ResponseEntity<String>(ProtocolState.toJsonArray(finalResult), headers, HttpStatus.OK);
}

@RequestMapping(value = "/experimentstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listExperimentStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<ExperimentState> result = ExperimentState.findAllExperimentStates();
    List<ExperimentState> finalResult = new ArrayList<ExperimentState>();
    for (ExperimentState experimentState: result) {
    	if (!experimentState.isIgnored()) finalResult.add(experimentState);
    }
    return new ResponseEntity<String>(ExperimentState.toJsonArray(finalResult), headers, HttpStatus.OK);
}

@RequestMapping(value = "/analysisgroupstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listAnalysisGroupStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<AnalysisGroupState> result = AnalysisGroupState.findAllAnalysisGroupStates();
    List<AnalysisGroupState> finalResult = new ArrayList<AnalysisGroupState>();
    for (AnalysisGroupState analysisGroupState: result) {
    	if (!analysisGroupState.isIgnored()) finalResult.add(analysisGroupState);
    }
    return new ResponseEntity<String>(AnalysisGroupState.toJsonArray(finalResult), headers, HttpStatus.OK);
}

@RequestMapping(value = "/treatmentgroupstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listTreatmentGroupStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<TreatmentGroupState> result = TreatmentGroupState.findAllTreatmentGroupStates();
    List<TreatmentGroupState> finalResult = new ArrayList<TreatmentGroupState>();
    for (TreatmentGroupState treatmentGroupState: result) {
    	if (!treatmentGroupState.isIgnored()) finalResult.add(treatmentGroupState);
    }
    return new ResponseEntity<String>(TreatmentGroupState.toJsonArray(finalResult), headers, HttpStatus.OK);
}

@RequestMapping(value = "/subjectstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listSubjectStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<SubjectState> result = SubjectState.findAllSubjectStates();
    List<SubjectState> finalResult = new ArrayList<SubjectState>();
    for (SubjectState subjectState: result) {
    	if (!subjectState.isIgnored()) finalResult.add(subjectState);
    }
    return new ResponseEntity<String>(SubjectState.toJsonArray(finalResult), headers, HttpStatus.OK);
}

@RequestMapping(value = "/lsthingstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listLsThingStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<LsThingState> result = LsThingState.findAllLsThingStates();
    List<LsThingState> finalResult = new ArrayList<LsThingState>();
    for (LsThingState lsThingState: result) {
    	if (!lsThingState.isIgnored()) finalResult.add(lsThingState);
    }
    return new ResponseEntity<String>(LsThingState.toJsonArray(finalResult), headers, HttpStatus.OK);
}

@RequestMapping(value = "/containerstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listContainerStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<ContainerState> result = ContainerState.findAllContainerStates();
    List<ContainerState> finalResult = new ArrayList<ContainerState>();
    for (ContainerState containerState: result) {
    	if (!containerState.isIgnored()) finalResult.add(containerState);
    }
    return new ResponseEntity<String>(ContainerState.toJsonArray(finalResult), headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxcontainercontainerstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listItxContainerContainerStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<ItxContainerContainerState> result = ItxContainerContainerState.findAllItxContainerContainerStates();
    List<ItxContainerContainerState> finalResult = new ArrayList<ItxContainerContainerState>();
    for (ItxContainerContainerState itxContainerContainerState: result) {
    	if (!itxContainerContainerState.isIgnored()) finalResult.add(itxContainerContainerState);
    }
    return new ResponseEntity<String>(ItxContainerContainerState.toJsonArray(finalResult), headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxexperimentexperimentstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listItxExperimentExperimentStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<ItxExperimentExperimentState> result = ItxExperimentExperimentState.findAllItxExperimentExperimentStates();
    List<ItxExperimentExperimentState> finalResult = new ArrayList<ItxExperimentExperimentState>();
    for (ItxExperimentExperimentState itxExperimentExperimentState: result) {
    	if (!itxExperimentExperimentState.isIgnored()) finalResult.add(itxExperimentExperimentState);
    }
    return new ResponseEntity<String>(ItxExperimentExperimentState.toJsonArray(finalResult), headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxprotocolprotocolstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listItxProtocolProtocolStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<ItxProtocolProtocolState> result = ItxProtocolProtocolState.findAllItxProtocolProtocolStates();
    List<ItxProtocolProtocolState> finalResult = new ArrayList<ItxProtocolProtocolState>();
    for (ItxProtocolProtocolState itxProtocolProtocolState: result) {
    	if (!itxProtocolProtocolState.isIgnored()) finalResult.add(itxProtocolProtocolState);
    }
    return new ResponseEntity<String>(ItxProtocolProtocolState.toJsonArray(finalResult), headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxsubjectcontainerstates", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> listItxSubjectContainerStatesJsonArray() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json; charset=utf-8");
    List<ItxSubjectContainerState> result = ItxSubjectContainerState.findAllItxSubjectContainerStates();
    List<ItxSubjectContainerState> finalResult = new ArrayList<ItxSubjectContainerState>();
    for (ItxSubjectContainerState itxSubjectContainerState: result) {
    	if (!itxSubjectContainerState.isIgnored()) finalResult.add(itxSubjectContainerState);
    }
    return new ResponseEntity<String>(ItxSubjectContainerState.toJsonArray(finalResult), headers, HttpStatus.OK);
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

@RequestMapping(value = "/containerstates/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> showContainerStateJson (@PathVariable("id") Long id) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
ContainerState containerState = ContainerState.findContainerState(id);
if (containerState == null || containerState.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
return new ResponseEntity<String>(containerState.toJson(), headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxcontainercontainerstates/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> showItxContainerContainerStateJson (@PathVariable("id") Long id) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
ItxContainerContainerState itxContainerContainerState = ItxContainerContainerState.findItxContainerContainerState(id);
if (itxContainerContainerState == null || itxContainerContainerState.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
return new ResponseEntity<String>(itxContainerContainerState.toJson(), headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxexperimentexperimentstates/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> showItxExperimentExperimentStateJson (@PathVariable("id") Long id) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
ItxExperimentExperimentState itxExperimentExperimentState = ItxExperimentExperimentState.findItxExperimentExperimentState(id);
if (itxExperimentExperimentState == null || itxExperimentExperimentState.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
return new ResponseEntity<String>(itxExperimentExperimentState.toJson(), headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxprotocolprotocolstates/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> showItxProtocolProtocolStateJson (@PathVariable("id") Long id) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
ItxProtocolProtocolState itxProtocolProtocolState = ItxProtocolProtocolState.findItxProtocolProtocolState(id);
if (itxProtocolProtocolState == null || itxProtocolProtocolState.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
return new ResponseEntity<String>(itxProtocolProtocolState.toJson(), headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxsubjectcontainerstates/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> showItxSubjectContainerStateJson (@PathVariable("id") Long id) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
ItxSubjectContainerState itxSubjectContainerState = ItxSubjectContainerState.findItxSubjectContainerState(id);
if (itxSubjectContainerState == null || itxSubjectContainerState.isIgnored()) return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
return new ResponseEntity<String>(itxSubjectContainerState.toJson(), headers, HttpStatus.OK);
}


//Update state from json (PUT)
@RequestMapping(value = "/protocolstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateProtocolStateFromJson (@RequestBody ProtocolState protocolState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
if (ProtocolState.findProtocolState(protocolState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
protocolState = protocolStateService.updateProtocolState(protocolState);
return new ResponseEntity<String>(protocolState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/experimentstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateExperimentStateFromJson (@RequestBody ExperimentState experimentState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
if (ExperimentState.findExperimentState(experimentState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
experimentState = experimentStateService.updateExperimentState(experimentState);
return new ResponseEntity<String>(experimentState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/analysisgroupstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateAnalysisGroupStateFromJson (@RequestBody AnalysisGroupState analysisGroupState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
if (AnalysisGroupState.findAnalysisGroupState(analysisGroupState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
analysisGroupState = analysisGroupStateService.updateAnalysisGroupState(analysisGroupState);
return new ResponseEntity<String>(analysisGroupState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/treatmentgroupstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateTreatmentGroupStateFromJson (@RequestBody TreatmentGroupState treatmentGroupState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
if (TreatmentGroupState.findTreatmentGroupState(treatmentGroupState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
treatmentGroupState = treatmentGroupStateService.updateTreatmentGroupState(treatmentGroupState);
return new ResponseEntity<String>(treatmentGroupState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/subjectstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateSubjectStateFromJson (@RequestBody SubjectState subjectState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
if (SubjectState.findSubjectState(subjectState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
subjectState = subjectStateService.updateSubjectState(subjectState);
return new ResponseEntity<String>(subjectState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/lsthingstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateLsThingStateFromJson (@RequestBody LsThingState lsThingState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
if (LsThingState.findLsThingState(lsThingState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
lsThingState = lsThingStateService.updateLsThingState(lsThingState);
return new ResponseEntity<String>(lsThingState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/containerstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> updateContainerStateFromJson (@RequestBody ContainerState containerState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
try{
	containerState = containerStateService.updateContainerState(containerState);
	return new ResponseEntity<String>(containerState.toJson(),headers, HttpStatus.OK);
}catch (Exception e){
	logger.error("Caught exception in update container state", e);
	return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
}

}

@RequestMapping(value = "/itxcontainercontainerstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateItxContainerContainerStateFromJson (@RequestBody ItxContainerContainerState itxContainerContainerState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
if (ItxContainerContainerState.findItxContainerContainerState(itxContainerContainerState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
itxContainerContainerState = itxContainerContainerStateService.updateItxContainerContainerState(itxContainerContainerState);
return new ResponseEntity<String>(itxContainerContainerState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxexperimentexperimentstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateItxExperimentExperimentStateFromJson (@RequestBody ItxExperimentExperimentState itxExperimentExperimentState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
if (ItxExperimentExperimentState.findItxExperimentExperimentState(itxExperimentExperimentState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
itxExperimentExperimentState = itxExperimentExperimentStateService.updateItxExperimentExperimentState(itxExperimentExperimentState);
return new ResponseEntity<String>(itxExperimentExperimentState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxprotocolprotocolstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateItxProtocolProtocolStateFromJson (@RequestBody ItxProtocolProtocolState itxProtocolProtocolState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
if (ItxProtocolProtocolState.findItxProtocolProtocolState(itxProtocolProtocolState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
itxProtocolProtocolState = itxProtocolProtocolStateService.updateItxProtocolProtocolState(itxProtocolProtocolState);
return new ResponseEntity<String>(itxProtocolProtocolState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxsubjectcontainerstates", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateItxSubjectContainerStateFromJson (@RequestBody ItxSubjectContainerState itxSubjectContainerState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
if (ItxSubjectContainerState.findItxSubjectContainerState(itxSubjectContainerState.getId()) == null) {
	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
}
itxSubjectContainerState = itxSubjectContainerStateService.updateItxSubjectContainerState(itxSubjectContainerState);
return new ResponseEntity<String>(itxSubjectContainerState.toJson(),headers, HttpStatus.OK);
}

//Update states from jsonArray (PUT)

@RequestMapping(value = "/protocolstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateProtocolStatesFromJsonArray (@RequestBody List<ProtocolState> protocolStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
protocolStates = (List<ProtocolState>) protocolStateService.updateProtocolStates(protocolStates);
return new ResponseEntity<String>(ProtocolState.toJsonArray(protocolStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/experimentstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateExperimentStatesFromJsonArray (@RequestBody List<ExperimentState> experimentStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
experimentStates = (List<ExperimentState>) experimentStateService.updateExperimentStates(experimentStates);
return new ResponseEntity<String>(ExperimentState.toJsonArray(experimentStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/analysisgroupstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateAnalysisGroupStatesFromJsonArray (@RequestBody List<AnalysisGroupState> analysisGroupStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
analysisGroupStates = (List<AnalysisGroupState>) analysisGroupStateService.updateAnalysisGroupStates(analysisGroupStates);
return new ResponseEntity<String>(AnalysisGroupState.toJsonArray(analysisGroupStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/treatmentgroupstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateTreatmentGroupStatesFromJsonArray (@RequestBody List<TreatmentGroupState> treatmentGroupStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
treatmentGroupStates = (List<TreatmentGroupState>) treatmentGroupStateService.updateTreatmentGroupStates(treatmentGroupStates);
return new ResponseEntity<String>(TreatmentGroupState.toJsonArray(treatmentGroupStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/subjectstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateSubjectStatesFromJsonArray (@RequestBody List<SubjectState> subjectStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
subjectStates = (List<SubjectState>) subjectStateService.updateSubjectStates(subjectStates);
return new ResponseEntity<String>(SubjectState.toJsonArray(subjectStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/lsthingstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateLsThingStatesFromJsonArray (@RequestBody List<LsThingState> lsThingStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
lsThingStates = (List<LsThingState>) lsThingStateService.updateLsThingStates(lsThingStates);
return new ResponseEntity<String>(LsThingState.toJsonArray(lsThingStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/containerstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> updateContainerStatesFromJsonArray (@RequestBody List<ContainerState> containerStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
try{
	containerStates = (List<ContainerState>) containerStateService.updateContainerStates(containerStates);
	return new ResponseEntity<String>(ContainerState.toJsonArray(containerStates),headers, HttpStatus.OK);
}catch (Exception e){
	logger.error("Caught exception in update container state", e);
	return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
}

}

@RequestMapping(value = "/itxcontainercontainerstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateItxContainerContainerStatesFromJsonArray (@RequestBody List<ItxContainerContainerState> itxContainerContainerStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
itxContainerContainerStates = (List<ItxContainerContainerState>) itxContainerContainerStateService.updateItxContainerContainerStates(itxContainerContainerStates);
return new ResponseEntity<String>(ItxContainerContainerState.toJsonArray(itxContainerContainerStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxexperimentexperimentstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateItxExperimentExperimentStatesFromJsonArray (@RequestBody List<ItxExperimentExperimentState> itxExperimentExperimentStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
itxExperimentExperimentStates = (List<ItxExperimentExperimentState>) itxExperimentExperimentStateService.updateItxExperimentExperimentStates(itxExperimentExperimentStates);
return new ResponseEntity<String>(ItxExperimentExperimentState.toJsonArray(itxExperimentExperimentStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxprotocolprotocolstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateItxProtocolProtocolStatesFromJsonArray (@RequestBody List<ItxProtocolProtocolState> itxProtocolProtocolStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
itxProtocolProtocolStates = (List<ItxProtocolProtocolState>) itxProtocolProtocolStateService.updateItxProtocolProtocolStates(itxProtocolProtocolStates);
return new ResponseEntity<String>(ItxProtocolProtocolState.toJsonArray(itxProtocolProtocolStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxsubjectcontainerstates/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> updateItxSubjectContainerStatesFromJsonArray (@RequestBody List<ItxSubjectContainerState> itxSubjectContainerStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
itxSubjectContainerStates = (List<ItxSubjectContainerState>) itxSubjectContainerStateService.updateItxSubjectContainerStates(itxSubjectContainerStates);
return new ResponseEntity<String>(ItxSubjectContainerState.toJsonArray(itxSubjectContainerStates),headers, HttpStatus.OK);
}

//Create state from json (POST)

@RequestMapping(value = "/protocolstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createProtocolStateFromJson (@RequestBody ProtocolState protocolState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
protocolState = protocolStateService.saveProtocolState(protocolState);
return new ResponseEntity<String>(protocolState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/experimentstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createExperimentStateFromJson (@RequestBody ExperimentState experimentState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
experimentState = experimentStateService.saveExperimentState(experimentState);
return new ResponseEntity<String>(experimentState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/analysisgroupstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createAnalysisGroupStateFromJson (@RequestBody AnalysisGroupState analysisGroupState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
analysisGroupState = analysisGroupStateService.saveAnalysisGroupState(analysisGroupState);
return new ResponseEntity<String>(analysisGroupState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/treatmentgroupstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createTreatmentGroupStateFromJson (@RequestBody TreatmentGroupState treatmentGroupState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
treatmentGroupState = treatmentGroupStateService.saveTreatmentGroupState(treatmentGroupState);
return new ResponseEntity<String>(treatmentGroupState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/subjectstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createSubjectStateFromJson (@RequestBody SubjectState subjectState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
subjectState = subjectStateService.saveSubjectState(subjectState);
return new ResponseEntity<String>(subjectState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/lsthingstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createLsThingStateFromJson (@RequestBody LsThingState lsThingState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
lsThingState = lsThingStateService.saveLsThingState(lsThingState);
return new ResponseEntity<String>(lsThingState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/containerstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> createContainerStateFromJson (@RequestBody ContainerState containerState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
try{
	containerState = containerStateService.saveContainerState(containerState);
	return new ResponseEntity<String>(containerState.toJson(),headers, HttpStatus.OK);
}catch (Exception e){
	logger.error("Caught exception in create container state", e);
	return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
}
}

@RequestMapping(value = "/itxcontainercontainerstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createItxContainerContainerStateFromJson (@RequestBody ItxContainerContainerState itxContainerContainerState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
itxContainerContainerState = itxContainerContainerStateService.saveItxContainerContainerState(itxContainerContainerState);
return new ResponseEntity<String>(itxContainerContainerState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxexperimentexperimentstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createItxExperimentExperimentStateFromJson (@RequestBody ItxExperimentExperimentState itxExperimentExperimentState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
itxExperimentExperimentState = itxExperimentExperimentStateService.saveItxExperimentExperimentState(itxExperimentExperimentState);
return new ResponseEntity<String>(itxExperimentExperimentState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxprotocolprotocolstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createItxProtocolProtocolStateFromJson (@RequestBody ItxProtocolProtocolState itxProtocolProtocolState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
itxProtocolProtocolState = itxProtocolProtocolStateService.saveItxProtocolProtocolState(itxProtocolProtocolState);
return new ResponseEntity<String>(itxProtocolProtocolState.toJson(),headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxsubjectcontainerstates", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createItxSubjectContainerStateFromJson (@RequestBody ItxSubjectContainerState itxSubjectContainerState) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
itxSubjectContainerState = itxSubjectContainerStateService.saveItxSubjectContainerState(itxSubjectContainerState);
return new ResponseEntity<String>(itxSubjectContainerState.toJson(),headers, HttpStatus.OK);
}

//Create states from jsonArray (POST)

@RequestMapping(value = "/protocolstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createProtocolStatesFromJsonArray (@RequestBody List<ProtocolState> protocolStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
protocolStates = (List<ProtocolState>) protocolStateService.saveProtocolStates(protocolStates);
return new ResponseEntity<String>(ProtocolState.toJsonArray(protocolStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/experimentstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createExperimentStatesFromJsonArray (@RequestBody List<ExperimentState> experimentStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
experimentStates = (List<ExperimentState>) experimentStateService.saveExperimentStates(experimentStates);
return new ResponseEntity<String>(ExperimentState.toJsonArray(experimentStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/analysisgroupstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createAnalysisGroupStatesFromJsonArray (@RequestBody List<AnalysisGroupState> analysisGroupStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
analysisGroupStates = (List<AnalysisGroupState>) analysisGroupStateService.saveAnalysisGroupStates(analysisGroupStates);
return new ResponseEntity<String>(AnalysisGroupState.toJsonArray(analysisGroupStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/treatmentgroupstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createTreatmentGroupStatesFromJsonArray (@RequestBody List<TreatmentGroupState> treatmentGroupStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
treatmentGroupStates = (List<TreatmentGroupState>) treatmentGroupStateService.saveTreatmentGroupStates(treatmentGroupStates);
return new ResponseEntity<String>(TreatmentGroupState.toJsonArray(treatmentGroupStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/subjectstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createSubjectStatesFromJsonArray (@RequestBody List<SubjectState> subjectStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
subjectStates = (List<SubjectState>) subjectStateService.saveSubjectStates(subjectStates);
return new ResponseEntity<String>(SubjectState.toJsonArray(subjectStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/lsthingstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createLsThingStatesFromJsonArray (@RequestBody List<LsThingState> lsThingStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
lsThingStates = (List<LsThingState>) lsThingStateService.saveLsThingStates(lsThingStates);
return new ResponseEntity<String>(LsThingState.toJsonArray(lsThingStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/containerstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
public ResponseEntity<String> createContainerStatesFromJsonArray (@RequestBody List<ContainerState> containerStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
try{
	containerStates = (List<ContainerState>) containerStateService.saveContainerStates(containerStates);
	return new ResponseEntity<String>(ContainerState.toJsonArray(containerStates),headers, HttpStatus.OK);
}catch (Exception e){
	logger.error("Caught exception in create container state", e);
	return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
}
}

@RequestMapping(value = "/itxcontainercontainerstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createItxContainerContainerStatesFromJsonArray (@RequestBody List<ItxContainerContainerState> itxContainerContainerStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
itxContainerContainerStates = (List<ItxContainerContainerState>) itxContainerContainerStateService.saveItxContainerContainerStates(itxContainerContainerStates);
return new ResponseEntity<String>(ItxContainerContainerState.toJsonArray(itxContainerContainerStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxexperimentexperimentstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createItxExperimentExperimentStatesFromJsonArray (@RequestBody List<ItxExperimentExperimentState> itxExperimentExperimentStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
itxExperimentExperimentStates = (List<ItxExperimentExperimentState>) itxExperimentExperimentStateService.saveItxExperimentExperimentStates(itxExperimentExperimentStates);
return new ResponseEntity<String>(ItxExperimentExperimentState.toJsonArray(itxExperimentExperimentStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxprotocolprotocolstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createItxProtocolProtocolStatesFromJsonArray (@RequestBody List<ItxProtocolProtocolState> itxProtocolProtocolStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
itxProtocolProtocolStates = (List<ItxProtocolProtocolState>) itxProtocolProtocolStateService.saveItxProtocolProtocolStates(itxProtocolProtocolStates);
return new ResponseEntity<String>(ItxProtocolProtocolState.toJsonArray(itxProtocolProtocolStates),headers, HttpStatus.OK);
}

@RequestMapping(value = "/itxsubjectcontainerstates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
@ResponseBody
@Transactional
public ResponseEntity<String> createItxSubjectContainerStatesFromJsonArray (@RequestBody List<ItxSubjectContainerState> itxSubjectContainerStates) {
HttpHeaders headers = new HttpHeaders();
headers.add("Content-Type", "application/json; charset=utf-8");
itxSubjectContainerStates = (List<ItxSubjectContainerState>) itxSubjectContainerStateService.saveItxSubjectContainerStates(itxSubjectContainerStates);
return new ResponseEntity<String>(ItxSubjectContainerState.toJsonArray(itxSubjectContainerStates),headers, HttpStatus.OK);
}

}
