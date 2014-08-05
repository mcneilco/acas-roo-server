package com.labsynch.labseer.api;

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
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ExperimentCsvDataDTO;
import com.labsynch.labseer.dto.ExperimentGuiStubDTO;
import com.labsynch.labseer.dto.KeyValueDTO;
import com.labsynch.labseer.dto.StateValueDTO;
import com.labsynch.labseer.service.AnalysisGroupService;
import com.labsynch.labseer.service.AnalysisGroupValueService;
import com.labsynch.labseer.service.ExperimentService;
import com.labsynch.labseer.service.ExperimentStateService;
import com.labsynch.labseer.service.ExperimentValueService;
import com.labsynch.labseer.service.SubjectValueService;
import com.labsynch.labseer.service.TreatmentGroupValueService;

@Controller
@RequestMapping("api/v1/experiments")
@Transactional
@RooWebJson(jsonObject = Experiment.class)
public class ApiExperimentController {
	private static final Logger logger = LoggerFactory.getLogger(ApiExperimentController.class);

	@Autowired
	private ExperimentService experimentService;

	@Autowired
	private ExperimentValueService experimentValueService;

	@Autowired
	private ExperimentStateService experimentStateService;

	@Autowired
	private AnalysisGroupService analysisGroupService;
	
	@Autowired
	private AnalysisGroupValueService analysisGroupValueService;

	@Autowired
	private TreatmentGroupValueService treatmentGroupValueService;

	@Autowired
	private SubjectValueService subjectValueService;

	@Transactional	
	@RequestMapping(value = "/analysisgroup/savefromcsv", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody ResponseEntity<String> saveAnalysisGroupDataFromCsv(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		ExperimentCsvDataDTO experimentCsvDataDTO = ExperimentCsvDataDTO.fromJsonToExperimentCsvDataDTO(json);
		logger.info("loading data from csv files: " + experimentCsvDataDTO.toJson());
		
		String analysisGroupFilePath = experimentCsvDataDTO.getAnalysisGroupCsvFilePath();
		String treatmentGroupFilePath = experimentCsvDataDTO.getTreatmentGroupCsvFilePath();
		String subjectFilePath = experimentCsvDataDTO.getSubjectCsvFilePath();

		boolean dataLoaded = analysisGroupService.saveLsAnalysisGroupFromCsv(analysisGroupFilePath, treatmentGroupFilePath, subjectFilePath);
		logger.info("dataLoaded: " + dataLoaded);
		
		if (dataLoaded){
			return new ResponseEntity<String>(headers, HttpStatus.OK) ;
		} else {
			return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/find=bymetadata", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<java.lang.String> findExperimentsByMetadata(
			@RequestBody String json,
			@RequestParam(value = "with", required = false) String with) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Collection<Experiment> experiments = experimentService.findExperimentsByMetadataJson(json);

		if (with != null) {
			if (with.equalsIgnoreCase("analysisgroups")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayStubWithAG(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(Experiment.toJsonArray(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayPretty(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjsonstub")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayStubPretty(experiments), headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
		}
	}

	
	@RequestMapping(value = "/bytypekind/{lsType}/{lsKind}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<java.lang.String> listExperimentsByTypeKindJson(
			@PathVariable("lsType") String lsType,
			@PathVariable("lsKind") String lsKind,
			@RequestParam(value = "protocolType", required = false) String protocolType,
			@RequestParam(value = "protocolKind", required = false) String protocolKind,
			@RequestParam(value = "with", required = false) String with) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<Experiment> experiments = null;
		if (protocolType != null && protocolKind != null){
//TODO: filter the experiments by protocol type and kind and experiment type and kind else 
//TODO: Greg please implement and test			
//TODO:			experiments = Experiment.findExperimentsByProtocolTypeAndKindAndExperimentTypeAndKind(protocolType, protocolKind, lsType, lsKind).getResultList();
		} else {
			experiments = Experiment.findExperimentsByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList();
		
		}

		if (with != null) {
			if (with.equalsIgnoreCase("analysisgroups")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayStubWithAG(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(Experiment.toJsonArray(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayPretty(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjsonstub")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayStubPretty(experiments), headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
		}
	}

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


	@RequestMapping(value = "/{experimentIdOrCodeName}/exptstates/bytypekind/{stateType}/{stateKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getExperimentStatesByIdOrCodeNameFilter11 (
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind, 
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if(isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {		
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch(Exception ex) {
				experiment = null;
			}
		}

		List<ExperimentState> experimentStates;
		if(experiment != null) {
			Long experimentId = experiment.getId();
			experimentStates = experimentStateService.getExperimentStatesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		} else {
			experimentStates = new ArrayList<ExperimentState>();
		}
		if (format.equalsIgnoreCase("csv")) {
			//getCSvList is just a stub service for now (skip for now) --> FlatThingCsvDTO
			String outputString = experimentStateService.getCsvList(experimentStates);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			//default format is json
			return new ResponseEntity<String>(ExperimentState.toJsonArray(experimentStates), headers, HttpStatus.OK);
		}
	}

	// GET values from different levels

	@RequestMapping(value = "/{experimentIdOrCodeName}/exptvalues/bystate/{stateType}/{stateKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getExperimentValueByIdOrCodeNameFilter1 (
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if(isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {		
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch(Exception ex) {
				experiment = null;
			}
		}

		List<ExperimentValue> experimentValues;
		if(experiment != null) {
			Long experimentId = experiment.getId();
			experimentValues = experimentValueService.getExperimentValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		} else {
			experimentValues = new ArrayList<ExperimentValue>();
		}
		if (format.equalsIgnoreCase("csv")) {
			String outputString = experimentValueService.getCsvList(experimentValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			//default format is json
			return new ResponseEntity<String>(ExperimentValue.toJsonArray(experimentValues), headers, HttpStatus.OK);
		}
	}


	@RequestMapping(value = "/{experimentIdOrCodeName}/exptvalues/bystate/{stateType}/{stateKind}/byvalue/{valueType}/{valueKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getExperimentValueByIdOrCodeNameFilter2 (
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("valueType") String valueType,
			@PathVariable("valueKind") String valueKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if(isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {		
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch(Exception ex) {
				experiment = null;
			}
		}

		List<ExperimentValue> experimentValues;
		if(experiment != null) {
			Long experimentId = experiment.getId();
			experimentValues = experimentValueService.getExperimentValuesByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind, valueType, valueKind);
		} else {
			experimentValues = new ArrayList<ExperimentValue>();
		}

		if (format != null && format.equalsIgnoreCase("csv")) {
//TODO: Gregory - use this as a template
// please write unit tests for the services as well			
			String outputString = experimentValueService.getCsvList(experimentValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else if (format != null && format.equalsIgnoreCase("keyvalue")) {
//TODO: Gregory - use this as a template
// please write unit tests for the services as well			

			List<StateValueDTO> stateValues = experimentValueService.getKeyValueList(experimentValues);
			return new ResponseEntity<String>(StateValueDTO.toJsonArray(stateValues), headers, HttpStatus.OK);
		} else if(format != null && format.equalsIgnoreCase("codeTable")) {
//TODO: Gregory - use this as a template
// please write unit tests for the services as well					
			List<CodeTableDTO> codeTables = experimentValueService.convertToCodeTables(experimentValues);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(codeTables), headers, HttpStatus.OK);
		} else {
			//default format is json
			return new ResponseEntity<String>(ExperimentValue.toJsonArray(experimentValues), headers, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/{experimentIdOrCodeName}/agvalues/bystate/{stateType}/{stateKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getAnalysisGroupValuesByIdOrCodeNameFilter31 (
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if(isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {		
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch(Exception ex) {
				experiment = null;
			}
		}

		List<AnalysisGroupValue> analysisGroupValues;
		if(experiment != null) {
			Long experimentId = experiment.getId();
			analysisGroupValues = analysisGroupValueService.getAnalysisGroupValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		} else {
			analysisGroupValues = new ArrayList<AnalysisGroupValue>();
		}
		if (format.equalsIgnoreCase("csv")) {
			//getCSvList is just a stub service for now
			String outputString = analysisGroupValueService.getCsvList(analysisGroupValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			//default format is json
			return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(analysisGroupValues), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{experimentIdOrCodeName}/agvalues/bystate/{stateType}/{stateKind}/byvalue/{valueType}/{valueKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getAnalysisGroupValueByIdOrCodeNameAndStateTypeKindAndValueTypeKindFilter3 (
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("valueType") String valueType,
			@PathVariable("valueKind") String valueKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if(isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {		
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch(Exception ex) {
				experiment = null;
			}
		}

		List<AnalysisGroupValue> analysisGroupValues;
		if(experiment != null) {
			Long experimentId = experiment.getId();
			analysisGroupValues = analysisGroupValueService.getAnalysisGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind, valueType, valueKind);
		} else {
			analysisGroupValues = new ArrayList<AnalysisGroupValue>();
		}
		if (format.equalsIgnoreCase("csv")) {
			//getCSvList is just a stub service for now
			String outputString = analysisGroupValueService.getCsvList(analysisGroupValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			//default format is json
			return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(analysisGroupValues), headers, HttpStatus.OK);
		}
	}
	@RequestMapping(value = "/{experimentIdOrCodeName}/tgvalues/bystate/{stateType}/{stateKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getTreatmentGroupValuesByIdOrCodeNameAndStateTypeKindFilter41 (
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if(isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {		
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch(Exception ex) {
				experiment = null;
			}
		}

		List<TreatmentGroupValue> treatmentGroupValues;
		if(experiment != null) {
			Long experimentId = experiment.getId();
			treatmentGroupValues = treatmentGroupValueService.getTreatmentGroupValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		} else {
			treatmentGroupValues = new ArrayList<TreatmentGroupValue>();
		}
		if (format.equalsIgnoreCase("csv")) {
			//getCSvList is just a stub service for now
			String outputString = treatmentGroupValueService.getCsvList(treatmentGroupValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			//default format is json
			return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(treatmentGroupValues), headers, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/{experimentIdOrCodeName}/tgvalues/bystate/{stateType}/{stateKind}/byvalue/{valueType}/{valueKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getTreatmentGroupValuesByIdOrCodeNameAndStateTypeKindAndValueTypeKindFilter4 (
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("valueType") String valueType,
			@PathVariable("valueKind") String valueKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if(isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {		
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch(Exception ex) {
				experiment = null;
			}
		}

		List<TreatmentGroupValue> treatmentGroupValues;
		if(experiment != null) {
			Long experimentId = experiment.getId();
			treatmentGroupValues = treatmentGroupValueService.getTreatmentGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId, stateType,
					stateKind, valueType, valueKind);
		} else {
			treatmentGroupValues = new ArrayList<TreatmentGroupValue>();
		}
		if (format.equalsIgnoreCase("csv")) {
			//getCSvList is just a stub service for now
			String outputString = treatmentGroupValueService.getCsvList(treatmentGroupValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			//default format is json
			return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(treatmentGroupValues), headers, HttpStatus.OK);
		}
	}

	//	@RequestMapping(value = "/{experimentIdOrCodeName}/subjects/bystate/{stateType}/{stateKind}/byvalue/{valueType}/{valueKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")

	@RequestMapping(value = "/{experimentIdOrCodeName}/subjectvalues/bystate/{stateType}/{stateKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getExperimentValueByIdOrCodeNameAndStateTypeKindFilter51 (
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if(isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {		
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch(Exception ex) {
				experiment = null;
			}
		}

		List<SubjectValue> subjectValues;
		if(experiment != null) {
			Long experimentId = experiment.getId();
			subjectValues = subjectValueService.getSubjectValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		} else {
			subjectValues = new ArrayList<SubjectValue>();
		}
		if (format.equalsIgnoreCase("csv")) {
			//getCSvList is just a stub service for now
			String outputString = subjectValueService.getCsvList(subjectValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			//default format is json
			return new ResponseEntity<String>(SubjectValue.toJsonArray(subjectValues), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{experimentIdOrCodeName}/subjectvalues/bystate/{stateType}/{stateKind}/byvalue/{valueType}/{valueKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getExperimentValueByIdOrCodeNameAndStateTypeKindAndValueTypeKindFilter5 (
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("valueType") String valueType,
			@PathVariable("valueKind") String valueKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if(isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {		
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch(Exception ex) {
				experiment = null;
			}
		}

		List<SubjectValue> subjectValues;
		if(experiment != null) {
			Long experimentId = experiment.getId();
			subjectValues = subjectValueService.getSubjectValuesByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId, stateType,
					stateKind, valueType, valueKind);
		} else {
			subjectValues = new ArrayList<SubjectValue>();
		}
		if (format.equalsIgnoreCase("csv")) {
			//getCSvList is just a stub service for now
			String outputString = subjectValueService.getCsvList(subjectValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			//default format is json
			return new ResponseEntity<String>(SubjectValue.toJsonArray(subjectValues), headers, HttpStatus.OK);
		}

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
			id = Experiment.findExperimentsByCodeNameEquals(IdOrCodeName).getSingleResult().getId();
		}

		if(id != null) {
			experimentValues = experimentValueService.getExperimentValuesByExperimentId(id);
		}

		return (experimentValues == null) ?
				new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND) :
					new ResponseEntity<String>(ExperimentValue.toJsonArray(experimentValues), headers, HttpStatus.OK);
	}


	@RequestMapping(value = "/{experimentIdOrCodeName}/values/{valueId}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getExperimentValueByIdOrCodeName (
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("valueId") Long valueId) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if(isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {
			experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
		}

		ExperimentValue experimentValue = ExperimentValue.findExperimentValue(valueId);

		if (experimentValue.getLsState().getExperiment().getId() != experiment.getId()){
			new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<String>(experimentValue.toJson(), headers, HttpStatus.OK);
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

	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        Experiment experiment = Experiment.findExperiment(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (experiment == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(experiment.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        Experiment experiment = Experiment.fromJsonToExperiment(json);
        experiment.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (Experiment experiment: Experiment.fromJsonArrayToExperiments(json)) {
            experiment.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Experiment experiment = Experiment.fromJsonToExperiment(json);
        if (experiment.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (Experiment experiment: Experiment.fromJsonArrayToExperiments(json)) {
            if (experiment.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        Experiment experiment = Experiment.findExperiment(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (experiment == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        experiment.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByCodeNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindExperimentsByCodeNameEquals(@RequestParam("codeName") String codeName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Experiment.toJsonArray(Experiment.findExperimentsByCodeNameEquals(codeName).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByLsTransaction", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindExperimentsByLsTransaction(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Experiment.toJsonArray(Experiment.findExperimentsByLsTransaction(lsTransaction).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindExperimentsByLsTypeEqualsAndLsKindEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Experiment.toJsonArray(Experiment.findExperimentsByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByProtocol", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindExperimentsByProtocol(@RequestParam("protocol") Protocol protocol) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Experiment.toJsonArray(Experiment.findExperimentsByProtocol(protocol).getResultList()), headers, HttpStatus.OK);
    }
}
