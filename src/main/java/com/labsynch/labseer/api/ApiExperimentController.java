package com.labsynch.labseer.api;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;

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
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.BatchCodeDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.DateValueComparisonRequest;
import com.labsynch.labseer.dto.ExperimentCsvDataDTO;
import com.labsynch.labseer.dto.ExperimentDataDTO;
import com.labsynch.labseer.dto.ExperimentErrorMessageDTO;
import com.labsynch.labseer.dto.ExperimentFilterDTO;
import com.labsynch.labseer.dto.ExperimentGuiStubDTO;
import com.labsynch.labseer.dto.ExperimentSearchRequestDTO;
import com.labsynch.labseer.dto.JSTreeNodeDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.StateValueDTO;
import com.labsynch.labseer.dto.StringCollectionDTO;
import com.labsynch.labseer.dto.SubjectStateValueDTO;
import com.labsynch.labseer.dto.TsvLoaderResponseDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
import com.labsynch.labseer.exceptions.NotFoundException;
import com.labsynch.labseer.exceptions.TooManyResultsException;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.service.AnalysisGroupService;
import com.labsynch.labseer.service.AnalysisGroupValueService;
import com.labsynch.labseer.service.ExperimentService;
import com.labsynch.labseer.service.ExperimentStateService;
import com.labsynch.labseer.service.ExperimentValueService;
import com.labsynch.labseer.service.GeneThingService;
import com.labsynch.labseer.service.LsThingService;
import com.labsynch.labseer.service.SubjectValueService;
import com.labsynch.labseer.service.TreatmentGroupValueService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Controller
@RequestMapping("api/v1/experiments")
// @Transactional
public class ApiExperimentController {
	private static final Logger logger = LoggerFactory.getLogger(ApiExperimentController.class);

	@Autowired
	private LsThingService lsThingService;

	@Autowired
	private GeneThingService geneThingService;

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

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Transactional
	@RequestMapping(value = "/analysisgroup/savefromtsv", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody ResponseEntity<String> saveAnalysisGroupDataFromCsv(
			@RequestBody ExperimentCsvDataDTO experimentCsvDataDTO) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		logger.info("loading data from csv files: " + experimentCsvDataDTO.toJson());

		String analysisGroupFilePath = experimentCsvDataDTO.getAnalysisGroupCsvFilePath();
		String treatmentGroupFilePath = experimentCsvDataDTO.getTreatmentGroupCsvFilePath();
		String subjectFilePath = experimentCsvDataDTO.getSubjectCsvFilePath();

		try {
			long startTime = new Date().getTime();
			TsvLoaderResponseDTO responseDTO = analysisGroupService.saveLsAnalysisGroupFromCsv(analysisGroupFilePath,
					treatmentGroupFilePath, subjectFilePath);
			long endTime = new Date().getTime();
			long totalTime = endTime - startTime;
			logger.info("dataLoaded: " + "true" + "   total elapsed time: " + totalTime);
			return new ResponseEntity<String>(responseDTO.toJson(), headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/find=bymetadata", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<java.lang.String> findExperimentsByMetadata(
			@RequestBody List<StringCollectionDTO> metaDataList,
			@RequestParam(value = "with", required = false) String with) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Collection<Experiment> experiments = experimentService.findExperimentsByMetadataJson(metaDataList);

		if (with != null) {
			if (with.equalsIgnoreCase("analysisgroups")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayStubWithAG(experiments), headers,
						HttpStatus.OK);
			} else if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(Experiment.toJsonArray(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayPretty(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjsonstub")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayStubPretty(experiments), headers,
						HttpStatus.OK);
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
		if (protocolType != null && protocolKind != null) {
			experiments = Experiment.findExperimentsByProtocolTypeAndKindAndExperimentTypeAndKind(protocolType,
					protocolKind, lsType, lsKind).getResultList();
		} else {
			experiments = Experiment.findExperimentsByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList();

		}

		if (with != null) {
			if (with.equalsIgnoreCase("analysisgroups")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayStubWithAG(experiments), headers,
						HttpStatus.OK);
			} else if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(Experiment.toJsonArray(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayPretty(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjsonstub")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayStubPretty(experiments), headers,
						HttpStatus.OK);
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

		if (protocolKind != null && protocolName != null) {
			List<Protocol> protocols = Protocol.findProtocolsByLsKindEquals(protocolKind).getResultList();
			for (Protocol protocol : protocols) {
				if (protocol.findPreferredName().equals(protocolName)) {
					List<Experiment> experiments = Experiment.findExperimentsByProtocol(protocol).getResultList();
					for (Experiment experiment : experiments) {
						ExperimentGuiStubDTO exptGui = new ExperimentGuiStubDTO(experiment);
						result.add(exptGui);
					}
				}
			}
		} else if (protocolKind != null && protocolName == null) {
			List<Protocol> protocols = Protocol.findProtocolsByLsKindEquals(protocolKind).getResultList();
			for (Protocol protocol : protocols) {
				if (protocol.findPreferredName().equals(protocolName)) {
					List<Experiment> experiments = Experiment.findExperimentsByProtocol(protocol).getResultList();
					for (Experiment experiment : experiments) {
						ExperimentGuiStubDTO exptGui = new ExperimentGuiStubDTO(experiment);
						result.add(exptGui);
					}
				}
			}
		} else if (protocolKind == null && protocolName != null) {
			List<Protocol> protocols = Protocol.findProtocolByProtocolName(protocolName);
			for (Protocol protocol : protocols) {
				List<Experiment> experiments = Experiment.findExperimentsByProtocol(protocol).getResultList();
				for (Experiment experiment : experiments) {
					ExperimentGuiStubDTO exptGui = new ExperimentGuiStubDTO(experiment);
					result.add(exptGui);
				}
			}
		} else {
			List<Experiment> experiments = Experiment.findAllExperiments();
			for (Experiment experiment : experiments) {
				ExperimentGuiStubDTO exptGui = new ExperimentGuiStubDTO(experiment);
				result.add(exptGui);
			}
		}

		return new ResponseEntity<String>(ExperimentGuiStubDTO.toJsonArray(result), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/codetables", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<java.lang.String> listExperimentCodeTables(
			@RequestParam(value = "lsType", required = false) String lsType,
			@RequestParam(value = "lsKind", required = false) String lsKind) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			List<CodeTableDTO> result = experimentService.getExperimentsAsCodeTables(lsType, lsKind);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(result), headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("caught exception getting experiments as codetables", e);
			return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@RequestMapping(value = "/subjectsstatus/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> findSubjectValues(
			@PathVariable("id") Long id,
			@RequestParam("stateType") String stateType,
			@RequestParam("stateKind") String stateKind,
			@RequestParam("stateValueType") String stateValueType,
			@RequestParam("stateValueKind") String stateValueKind) {

		List<SubjectStateValueDTO> lsValues = new ArrayList<SubjectStateValueDTO>();

		Experiment experiment = Experiment.findExperiment(id);
		Set<AnalysisGroup> analysisGroups = experiment.getAnalysisGroups();
		for (AnalysisGroup analysisGroup : analysisGroups) {
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
							SubjectStateValueDTO svDTO = new SubjectStateValueDTO();
							svDTO.setSubjectId(subject.getId());
							if (stateValueType.equalsIgnoreCase("stringValue")) {
								svDTO.setSubjectValue(subjectValue.getStringValue());
							} else if (stateValueType.equalsIgnoreCase("numericValue")) {
								svDTO.setSubjectValue(subjectValue.getNumericValue().toString());
							}
							lsValues.add(svDTO);
						}
					}
				}
			}
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(SubjectStateValueDTO.toJsonArray(lsValues), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{experimentIdOrCodeName}/exptstates/bytypekind/{stateType}/{stateKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getExperimentStatesByIdOrCodeNameFilter11(
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if (isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch (Exception ex) {
				experiment = null;
			}
		}

		List<ExperimentState> experimentStates;
		if (experiment != null) {
			Long experimentId = experiment.getId();
			experimentStates = experimentStateService.getExperimentStatesByExperimentIdAndStateTypeKind(experimentId,
					stateType, stateKind);
		} else {
			experimentStates = new ArrayList<ExperimentState>();
		}
		if (format.equalsIgnoreCase("tsv")) {
			// getCSvList is just a stub service for now (skip for now) --> FlatThingCsvDTO
			String outputString = experimentStateService.getCsvList(experimentStates);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			// default format is json
			return new ResponseEntity<String>(ExperimentState.toJsonArray(experimentStates), headers, HttpStatus.OK);
		}
	}

	// GET values from different levels

	@RequestMapping(value = "/{experimentIdOrCodeName}/exptvalues/bystate/{stateType}/{stateKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getExperimentValueByIdOrCodeNameFilter1(
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if (isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch (Exception ex) {
				experiment = null;
			}
		}

		List<ExperimentValue> experimentValues;
		if (experiment != null) {
			Long experimentId = experiment.getId();
			experimentValues = experimentValueService.getExperimentValuesByExperimentIdAndStateTypeKind(experimentId,
					stateType, stateKind);
		} else {
			experimentValues = new ArrayList<ExperimentValue>();
		}
		if (format.equalsIgnoreCase("tsv")) {
			String outputString = experimentValueService.getCsvList(experimentValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			// default format is json
			return new ResponseEntity<String>(ExperimentValue.toJsonArray(experimentValues), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{experimentIdOrCodeName}/exptvalues/bystate/{stateType}/{stateKind}/byvalue/{valueType}/{valueKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getExperimentValueByIdOrCodeNameFilter2(
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("valueType") String valueType,
			@PathVariable("valueKind") String valueKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if (isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch (Exception ex) {
				experiment = null;
			}
		}

		List<ExperimentValue> experimentValues;
		if (experiment != null) {
			Long experimentId = experiment.getId();
			experimentValues = experimentValueService.getExperimentValuesByExperimentIdAndStateTypeKindAndValueTypeKind(
					experimentId, stateType, stateKind, valueType, valueKind);
		} else {
			experimentValues = new ArrayList<ExperimentValue>();
		}

		if (format != null && format.equalsIgnoreCase("tsv")) {
			String outputString = experimentValueService.getCsvList(experimentValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else if (format != null && format.equalsIgnoreCase("keyvalue")) {
			List<StateValueDTO> stateValues = experimentValueService.getKeyValueList(experimentValues);
			return new ResponseEntity<String>(StateValueDTO.toJsonArray(stateValues), headers, HttpStatus.OK);
		} else if (format != null && format.equalsIgnoreCase("codeTable")) {
			List<CodeTableDTO> codeTables = experimentValueService.convertToCodeTables(experimentValues);
			codeTables = CodeTableDTO.sortCodeTables(codeTables);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(codeTables), headers, HttpStatus.OK);
		} else {
			// default format is json
			return new ResponseEntity<String>(ExperimentValue.toJsonArray(experimentValues), headers, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/{experimentIdOrCodeName}/agvalues/bystate/{stateType}/{stateKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getAnalysisGroupValuesByIdOrCodeNameFilter31(
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if (isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch (Exception ex) {
				experiment = null;
			}
		}

		List<AnalysisGroupValue> analysisGroupValues;
		if (experiment != null) {
			Long experimentId = experiment.getId();
			analysisGroupValues = analysisGroupValueService
					.getAnalysisGroupValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		} else {
			analysisGroupValues = new ArrayList<AnalysisGroupValue>();
		}
		if (format.equalsIgnoreCase("tsv")) {
			String outputString = analysisGroupValueService.getCsvList(analysisGroupValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			// default format is json
			return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(analysisGroupValues), headers,
					HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{experimentIdOrCodeName}/agvalues/bystate/{stateType}/{stateKind}/byvalue/{valueType}/{valueKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getAnalysisGroupValueByIdOrCodeNameAndStateTypeKindAndValueTypeKindFilter3(
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("valueType") String valueType,
			@PathVariable("valueKind") String valueKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if (isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch (Exception ex) {
				experiment = null;
			}
		}

		List<AnalysisGroupValue> analysisGroupValues;
		if (experiment != null) {
			Long experimentId = experiment.getId();
			analysisGroupValues = analysisGroupValueService
					.getAnalysisGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId, stateType,
							stateKind, valueType, valueKind);
		} else {
			analysisGroupValues = new ArrayList<AnalysisGroupValue>();
		}
		if (format.equalsIgnoreCase("tsv")) {
			String outputString = analysisGroupValueService.getCsvList(analysisGroupValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			// default format is json
			return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(analysisGroupValues), headers,
					HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{experimentIdOrCodeName}/tgvalues/bystate/{stateType}/{stateKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getTreatmentGroupValuesByIdOrCodeNameAndStateTypeKindFilter41(
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if (isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch (Exception ex) {
				experiment = null;
			}
		}

		List<TreatmentGroupValue> treatmentGroupValues;
		if (experiment != null) {
			Long experimentId = experiment.getId();
			treatmentGroupValues = treatmentGroupValueService
					.getTreatmentGroupValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		} else {
			treatmentGroupValues = new ArrayList<TreatmentGroupValue>();
		}
		if (format.equalsIgnoreCase("tsv")) {
			String outputString = treatmentGroupValueService.getCsvList(treatmentGroupValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			// default format is json
			return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(treatmentGroupValues), headers,
					HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/{experimentIdOrCodeName}/tgvalues/bystate/{stateType}/{stateKind}/byvalue/{valueType}/{valueKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getTreatmentGroupValuesByIdOrCodeNameAndStateTypeKindAndValueTypeKindFilter4(
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("valueType") String valueType,
			@PathVariable("valueKind") String valueKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if (isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch (Exception ex) {
				experiment = null;
			}
		}

		List<TreatmentGroupValue> treatmentGroupValues;
		if (experiment != null) {
			Long experimentId = experiment.getId();
			treatmentGroupValues = treatmentGroupValueService
					.getTreatmentGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId, stateType,
							stateKind, valueType, valueKind);
		} else {
			treatmentGroupValues = new ArrayList<TreatmentGroupValue>();
		}
		if (format.equalsIgnoreCase("tsv")) {
			String outputString = treatmentGroupValueService.getCsvList(treatmentGroupValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			// default format is json
			return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(treatmentGroupValues), headers,
					HttpStatus.OK);
		}
	}

	// @RequestMapping(value =
	// "/{experimentIdOrCodeName}/subjects/bystate/{stateType}/{stateKind}/byvalue/{valueType}/{valueKind}/{format}",
	// method = RequestMethod.GET, headers = "Accept=application/json")

	@RequestMapping(value = "/{experimentIdOrCodeName}/subjectvalues/bystate/{stateType}/{stateKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getExperimentValueByIdOrCodeNameAndStateTypeKindFilter51(
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if (isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch (Exception ex) {
				experiment = null;
			}
		}

		List<SubjectValue> subjectValues;
		if (experiment != null) {
			Long experimentId = experiment.getId();
			subjectValues = subjectValueService.getSubjectValuesByExperimentIdAndStateTypeKind(experimentId, stateType,
					stateKind);
		} else {
			subjectValues = new ArrayList<SubjectValue>();
		}
		if (format.equalsIgnoreCase("tsv")) {
			String outputString = subjectValueService.getCsvList(subjectValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			// default format is json
			return new ResponseEntity<String>(SubjectValue.toJsonArray(subjectValues), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/{experimentIdOrCodeName}/subjectvalues/bystate/{stateType}/{stateKind}/byvalue/{valueType}/{valueKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getExperimentValueByIdOrCodeNameAndStateTypeKindAndValueTypeKindFilter5(
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("valueType") String valueType,
			@PathVariable("valueKind") String valueKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if (isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
			} catch (Exception ex) {
				experiment = null;
			}
		}

		List<SubjectValue> subjectValues;
		if (experiment != null) {
			Long experimentId = experiment.getId();
			subjectValues = subjectValueService.getSubjectValuesByExperimentIdAndStateTypeKindAndValueTypeKind(
					experimentId, stateType,
					stateKind, valueType, valueKind);
		} else {
			subjectValues = new ArrayList<SubjectValue>();
		}
		if (format.equalsIgnoreCase("tsv")) {
			String outputString = subjectValueService.getCsvList(subjectValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			// default format is json
			return new ResponseEntity<String>(SubjectValue.toJsonArray(subjectValues), headers, HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/{idOrCodeName}/values", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getExperimentValuesForExperimentByIdOrCodeName(
			@PathVariable("idOrCodeName") String idOrCodeName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<ExperimentValue> experimentValues = null;
		Long id = null;
		if (SimpleUtil.isNumeric(idOrCodeName)) {
			id = Long.valueOf(idOrCodeName);
		} else {
			id = Experiment.findExperimentsByCodeNameEquals(idOrCodeName).getSingleResult().getId();
		}

		if (id != null) {
			experimentValues = experimentValueService.getExperimentValuesByExperimentId(id);
		}

		return (experimentValues == null) ? new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND)
				: new ResponseEntity<String>(ExperimentValue.toJsonArray(experimentValues), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{experimentIdOrCodeName}/values/{valueId}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getExperimentValueByIdOrCodeName(
			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
			@PathVariable("valueId") Long valueId) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		Experiment experiment;
		if (isNumeric(experimentIdOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
		} else {
			experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
		}

		ExperimentValue experimentValue = ExperimentValue.findExperimentValue(valueId);

		if (experimentValue.getLsState().getExperiment().getId() != experiment.getId()) {
			new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<String>(experimentValue.toJson(), headers, HttpStatus.OK);
	}

	// @Transactional
	// @RequestMapping(value = "/values", method = RequestMethod.POST, headers =
	// "Accept=application/json")
	// public @ResponseBody ResponseEntity<String>
	// saveExperimentFromJson(@RequestBody String json) {
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	//
	// ExperimentValue experimentValue =
	// ExperimentValue.fromJsonToExperimentValue(json);
	//
	// return (experimentValueService.saveExperimentValue(experimentValue) == null)
	// ?
	// new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) :
	// new ResponseEntity<String>(headers, HttpStatus.OK);
	// }

	// @Transactional
	// @RequestMapping(value = "{IdOrCodeName}/values/{Id}", method =
	// RequestMethod.PUT, headers = "Accept=application/json")
	// public @ResponseBody ResponseEntity<String> updateExperimentFromJsonWithId(
	// @RequestBody String json,
	// @PathVariable("Id") String Id,
	// @PathVariable("IdOrCodeName") String IdOrCodeName) {
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	//
	// ExperimentValue experimentValue =
	// ExperimentValue.fromJsonToExperimentValue(json);
	// if(experimentValue.getId() == null) {
	// return (experimentValueService.saveExperimentValue(experimentValue) != null)
	// ?
	// new ResponseEntity<String>(headers, HttpStatus.OK) :
	// new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
	// }
	// return ((experimentValueService.updateExperimentValue(experimentValue)) ==
	// null) ?
	// new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) :
	// new ResponseEntity<String>(headers, HttpStatus.OK);
	// }
	//
	// @Transactional
	// @RequestMapping(value = "{IdOrCodeName}/values", method = RequestMethod.PUT,
	// headers = "Accept=application/json")
	// public @ResponseBody ResponseEntity<String> updateExperimentFromJsonWithId(
	// @RequestBody String json,
	// @PathVariable("IdOrCodeName") String IdOrCodeName) {
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	//
	// ExperimentValue experimentValue =
	// ExperimentValue.fromJsonToExperimentValue(json);
	// if(experimentValue.getId() == null) {
	// return (experimentValueService.saveExperimentValue(experimentValue) != null)
	// ?
	// new ResponseEntity<String>(headers, HttpStatus.OK) :
	// new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
	// }
	// return ((experimentValueService.updateExperimentValue(experimentValue)) ==
	// null) ?
	// new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) :
	// new ResponseEntity<String>(headers, HttpStatus.OK);
	// }

	private static boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
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

	// @Transactional
	@RequestMapping(value = {"", "/"}, method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
		Experiment experiment = Experiment.fromJsonToExperiment(json);
		logger.debug("----from the Experiment POST controller----");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		boolean errorsFound = false;
		Experiment savedExperiment = null;
		try {
			Set<AnalysisGroup> inputAnalysisGroups = new HashSet<AnalysisGroup>();
			for (AnalysisGroup analysisGroup : experiment.getAnalysisGroups()) {
				inputAnalysisGroups.add(analysisGroup);
			}

			// Set<Long> hashAnalysisGroupIds = new HashSet<Long>();
			// for(AnalysisGroup analysisGroup : experiment.getAnalysisGroups()){
			// hashAnalysisGroupIds.add(analysisGroup.getId());
			// }
			//
			// List<Long> analysisGroupIds = new
			// ArrayList<Long>(hashAnalysisGroupIds.size());
			// for(Long analysisGroupId : hashAnalysisGroupIds){
			// analysisGroupIds.add(analysisGroupId);
			// }
			// Collections.sort(analysisGroupIds);
			//
			// List<AnalysisGroup> inputAnalysisGroups = new ArrayList<AnalysisGroup>();
			// for(Long analysisGroupId : analysisGroupIds){
			// inputAnalysisGroups.add(AnalysisGroup.findAnalysisGroup(analysisGroupId));
			// }

			savedExperiment = experimentService.saveLsExperiment(experiment);

			AnalysisGroup savedAnalysisGroup = null;
			int i = 0;
			for (AnalysisGroup analysisGroup : inputAnalysisGroups) {
				try {
					analysisGroup.getExperiments().add(savedExperiment);
					savedAnalysisGroup = analysisGroupService.saveLsAnalysisGroup(analysisGroup);
					if (i % propertiesUtilService.getBatchSize() == 0) {
						savedAnalysisGroup.flush();
						savedAnalysisGroup.clear();
					}
					i++;
				} catch (Exception e) {
					logger.error("Error saving the analysisGroup -------" + e.toString());
					throw new RuntimeException("Error saving the analysisGroup" + e);
				}
			}

			// experiment.setAnalysisGroups(inputAnalysisGroups);

		} catch (UniqueNameException e) {
			logger.error("----from the controller UniqueNameException ----" + e.getMessage().toString()
					+ " whole message  " + e.toString());
			ErrorMessage error = new ErrorMessage();
			error.setErrorLevel("error");
			error.setMessage("not unique experiment name");
			errors.add(error);
			errorsFound = true;
		} catch (NotFoundException e) {
			logger.error("----from the controller NotFoundException ----" + e.getMessage().toString()
					+ " whole message  " + e.toString());
			// TODO: Fix this to do this logic lower in the code if possible.
			// Assuming that we are unable to find the analysisgroups in the experiment
			// Want to tell the users all of the missing analysisgroups
			Set<AnalysisGroup> analysisGroups = experiment.getAnalysisGroups();
			for (AnalysisGroup analysisGroup : analysisGroups) {
				if (analysisGroup.getId() != null && AnalysisGroup.findAnalysisGroup(analysisGroup.getId()) == null) {
					ErrorMessage error = new ErrorMessage();
					error.setErrorLevel("error");
					error.setMessage("analysis group not found: " + analysisGroup.getId());
					errors.add(error);
					errorsFound = true;
				}
			}
		}
		if (errorsFound) {
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<String>(savedExperiment.toJson(), headers, HttpStatus.CREATED);
		}
	}

	@Transactional
	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
		Collection<Experiment> experiments = Experiment.fromJsonArrayToExperiments(json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			Collection<Experiment> savedExperiments = experimentService.saveLsExperiments(experiments);
			return new ResponseEntity<String>(Experiment.toJsonArrayStub(savedExperiments), headers,
					HttpStatus.CREATED);
		} catch (UniqueNameException uniqueNameException) {
			logger.error("Found existing experiment with that name", uniqueNameException);
			return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Caught exception in createFromJsonArray", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Transactional
	@RequestMapping(value = { "/{id}", "/" }, method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
		Experiment experiment = Experiment.fromJsonToExperiment(json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		boolean errorsFound = false;
		try {
			experiment = experimentService.updateExperiment(experiment);
		} catch (UniqueNameException e) {
			logger.error("----from the controller----" + e.getMessage().toString() + " whole message  " + e.toString());
			ErrorMessage error = new ErrorMessage();
			error.setErrorLevel("error");
			error.setMessage("not unique experiment name");
			errors.add(error);
			errorsFound = true;
		}
		if (errorsFound) {
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.CONFLICT);
		}
		if (experiment.getId() == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(Experiment.findExperiment(experiment.getId()).toJsonStub(), headers,
				HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
		Collection<Experiment> experiments = Experiment.fromJsonArrayToExperiments(json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<Experiment> updatedExperiments = new ArrayList<Experiment>();
		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		boolean errorsFound = false;
		for (Experiment experiment : experiments) {
			try {
				updatedExperiments.add(experimentService.updateExperiment(experiment));
			} catch (UniqueNameException e) {
				logger.error(
						"----from the controller----" + e.getMessage().toString() + " whole message  " + e.toString());
				ErrorMessage error = new ErrorMessage();
				error.setErrorLevel("error");
				error.setMessage("not unique experiment name");
				errors.add(error);
				errorsFound = true;
			}
		}
		if (errorsFound) {
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.CONFLICT);
		}
		return new ResponseEntity<String>(Experiment.toJsonArrayStub(updatedExperiments), headers, HttpStatus.OK);
	}

	// @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers =
	// "Accept=application/json")
	// public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
	// Experiment experiment = Experiment.findExperiment(id);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// if (experiment == null) {
	// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	// }
	// experiment.remove();
	// return new ResponseEntity<String>(headers, HttpStatus.OK);
	// }

	@RequestMapping(value = "/seldelete/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> selDeleteById(@PathVariable("id") Long id) {
		Experiment experiment = Experiment.findExperiment(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (experiment == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		// Set experiment status to "overwritten"
		ExperimentValue experimentValue = experimentValueService.updateExperimentValue(experiment.getCodeName(),
				"metadata", "experiment metadata", "codeValue", "experiment status", "overwritten");
		experiment.logicalDelete();
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/browser/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> softDeleteById(@PathVariable("id") Long id) {
		Experiment experiment = Experiment.findExperiment(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (experiment == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		ExperimentValue experimentValue = experimentValueService.updateExperimentValue(experiment.getCodeName(),
				"metadata", "experiment metadata", "codeValue", "experiment status", "deleted");
		experiment.setIgnored(true);
		return new ResponseEntity<String>(experimentValue.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(params = "find=ByCodeNameEquals", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonFindExperimentsByCodeNameEquals(@RequestParam("codeName") String codeName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(
				Experiment.toJsonArray(Experiment.findExperimentsByCodeNameEquals(codeName).getResultList()), headers,
				HttpStatus.OK);
	}

	@RequestMapping(params = "find=ByLsTransaction", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonFindExperimentsByLsTransaction(
			@RequestParam("lsTransaction") Long lsTransaction) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(
				Experiment.toJsonArray(Experiment.findExperimentsByLsTransaction(lsTransaction).getResultList()),
				headers, HttpStatus.OK);
	}

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonFindExperimentsByLsTypeEqualsAndLsKindEquals(
			@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(
				Experiment.toJsonArray(
						Experiment.findExperimentsByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList()),
				headers, HttpStatus.OK);
	}

	@RequestMapping(params = "find=ByProtocol", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonFindExperimentsByProtocol(@RequestParam("protocol") Protocol protocol) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(
				Experiment.toJsonArray(Experiment.findExperimentsByProtocol(protocol).getResultList()), headers,
				HttpStatus.OK);
	}

	////////////// copied from experiment controller

	@Transactional
	@RequestMapping(value = "/agdata/batchcodelist", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> getGeneCodeData(@RequestBody String json,
			@RequestParam(value = "format", required = false) String format,
			@RequestParam(value = "onlyPublicData", required = false) String onlyPublicData) {
		logger.debug("incoming json: " + json);
		ExperimentSearchRequestDTO searchRequest = ExperimentSearchRequestDTO
				.fromJsonToExperimentSearchRequestDTO(json);
		List<AnalysisGroupValueDTO> agValues = null;

		Boolean publicData = false;
		if (onlyPublicData != null && onlyPublicData.equalsIgnoreCase("true")) {
			publicData = true;
		}

		try {
			if (publicData) {
				agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(searchRequest.getBatchCodeList(), publicData)
						.getResultList();
			} else {
				agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(searchRequest.getBatchCodeList())
						.getResultList();
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (agValues == null || agValues.size() == 0) {
			return new ResponseEntity<String>("[]", headers, HttpStatus.EXPECTATION_FAILED);
		}
		if (format != null && format.equalsIgnoreCase("csv")) {
			StringWriter outFile = new StringWriter();
			ICsvBeanWriter beanWriter = null;
			try {
				beanWriter = new CsvBeanWriter(outFile, CsvPreference.STANDARD_PREFERENCE);
				final String[] header = AnalysisGroupValueDTO.getColumns();
				final CellProcessor[] processors = AnalysisGroupValueDTO.getProcessors();
				beanWriter.writeHeader(header);
				for (final AnalysisGroupValueDTO agValue : agValues) {
					beanWriter.write(agValue, header, processors);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (beanWriter != null) {
					try {
						beanWriter.close();
						outFile.flush();
						outFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return new ResponseEntity<String>(outFile.toString(), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(AnalysisGroupValueDTO.toJsonArray(agValues), headers, HttpStatus.OK);
		}
	}

	@Transactional
	@RequestMapping(value = "/filters/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> getExperimentFilters(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<String> experimentCodes = new JSONDeserializer<List<String>>().use(null, ArrayList.class)
				.use("values", String.class).deserialize(json);
		Collection<ExperimentFilterDTO> results = experimentService.getExperimentFilters(experimentCodes);
		return new ResponseEntity<String>(ExperimentFilterDTO.toJsonArray(results), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/get/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> getExperimentFiltersNew(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<String> experimentCodes = new JSONDeserializer<List<String>>().use(null, ArrayList.class)
				.use("values", String.class).deserialize(json);
		Collection<ExperimentFilterDTO> results = experimentService.getExperimentFilters(experimentCodes);
		return new ResponseEntity<String>(ExperimentFilterDTO.toJsonArray(results), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/jstreenodes/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> getJsTreeNodes(@RequestBody List<BatchCodeDTO> batchCodes) {
		logger.debug("getting tree nodes: " + BatchCodeDTO.toJsonArray(batchCodes));
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<String> codeValues = new HashSet<String>();
		for (BatchCodeDTO batchCode : batchCodes) {
			codeValues.add(batchCode.getBatchCode());
		}
		Collection<JSTreeNodeDTO> results = experimentService.getExperimentNodes(codeValues);
		return new ResponseEntity<String>(JSTreeNodeDTO.toJsonArray(results), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/exptData/byGeneID", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> getAgDataByGeneId(
			@RequestParam(value = "geneID", required = true) String geneID,
			@RequestParam(value = "onlyPublicData", required = false) String onlyPublicData) {
		boolean showOnlyPublicData = false;
		if (onlyPublicData != null && onlyPublicData.trim().equalsIgnoreCase("true")) {
			showOnlyPublicData = true;
		}

		PreferredNameRequestDTO requestDTO = new PreferredNameRequestDTO();
		PreferredNameDTO prefDTO = new PreferredNameDTO();
		prefDTO.setRequestName(geneID);
		Collection<PreferredNameDTO> requests = new HashSet<PreferredNameDTO>();
		requests.add(prefDTO);
		requestDTO.setRequests(requests);
		String thingType = "gene";
		String thingKind = "entrez gene";
		String labelType = "name";
		String labelKind = "Entrez Gene ID";
		logger.info("getGeneCodeNameFromNameRequest incoming json: " + requestDTO.toJson());
		Collection<PreferredNameDTO> batchCodeResults = lsThingService
				.getPreferredNameFromName(thingType, thingKind, labelType, labelKind, requestDTO).getResults();
		String batchCode = null;
		for (PreferredNameDTO batchCodeResult : batchCodeResults) {
			batchCode = batchCodeResult.getReferenceName();
		}

		List<ExperimentDataDTO> experimentData = experimentService.getExperimentData(batchCode, showOnlyPublicData);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		headers.add("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate"); // HTTP 1.1
		return new ResponseEntity<String>(ExperimentDataDTO.toJsonArray(experimentData), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/exptData/byBatchCode", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> getAgDataByBatchCode(
			@RequestParam(value = "batchCode", required = true) String batchCode,
			@RequestParam(value = "onlyPublicData", required = false) String onlyPublicData) {
		boolean showOnlyPublicData = false;
		if (onlyPublicData != null && onlyPublicData.trim().equalsIgnoreCase("true")) {
			showOnlyPublicData = true;
		}

		batchCode = batchCode.trim();
		List<ExperimentDataDTO> experimentData = experimentService.getExperimentData(batchCode, showOnlyPublicData);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		headers.add("Cache-Control", "no-store, max-age=0, no-cache, must-revalidate"); // HTTP 1.1
		return new ResponseEntity<String>(ExperimentDataDTO.toJsonArray(experimentData), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/agdata/batchcodelist/experimentcodelist", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> getAGDataByBatchAndExperiment(
			@RequestBody String searchRequestJSON,
			@RequestParam(value = "format", required = false) String format,
			@RequestParam(value = "onlyPublicData", required = false) String onlyPublicData) {

		Boolean publicData = false;
		if (onlyPublicData != null && onlyPublicData.equalsIgnoreCase("true")) {
			publicData = true;
		}

		ExperimentSearchRequestDTO searchRequest = ExperimentSearchRequestDTO
				.fromJsonToExperimentSearchRequestDTO(searchRequestJSON);
		if (logger.isDebugEnabled())
			logger.debug("converted json: " + searchRequest.toJson());
		List<AnalysisGroupValueDTO> agValues = null;
		try {
			agValues = experimentService.getFilteredAGData(searchRequest, publicData);
			if (logger.isDebugEnabled())
				logger.debug("number of agvalues found: " + agValues.size());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (agValues == null || agValues.size() == 0) {
			return new ResponseEntity<String>("[]", headers, HttpStatus.EXPECTATION_FAILED);
		}
		if (format != null && format.equalsIgnoreCase("csv")) {
			StringWriter outFile = new StringWriter();
			ICsvBeanWriter beanWriter = null;
			try {
				beanWriter = new CsvBeanWriter(outFile, CsvPreference.STANDARD_PREFERENCE);
				final String[] header = AnalysisGroupValueDTO.getColumns();
				final CellProcessor[] processors = AnalysisGroupValueDTO.getProcessors();
				beanWriter.writeHeader(header);
				for (final AnalysisGroupValueDTO agValue : agValues) {
					beanWriter.write(agValue, header, processors);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (beanWriter != null) {
					try {
						beanWriter.close();
						outFile.flush();
						outFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return new ResponseEntity<String>(outFile.toString(), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(AnalysisGroupValueDTO.toJsonArray(agValues), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/full/{id}", headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<java.lang.String> showFullJson(@PathVariable("id") Long id) {
		Experiment experiment = Experiment.findExperiment(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (experiment == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(experiment.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/stub/{id}", headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<java.lang.String> showJsonStubWith(@PathVariable("id") Long id,
			@RequestParam(value = "with", required = false) String with) {
		Experiment experiment = Experiment.findExperiment(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (experiment == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		if (with != null) {
			if (with.equalsIgnoreCase("analysisgroups")) {
				return new ResponseEntity<String>(experiment.toJsonStubWithAnalysisGroups(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("analysisgroupstates")) {
				return new ResponseEntity<String>(experiment.toJsonStubWithAnalysisGroupStates(), headers,
						HttpStatus.OK);
			} else if (with.equalsIgnoreCase("analysisgroupvalues")) {
				return new ResponseEntity<String>(experiment.toJsonStubWithAnalysisGroups(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(experiment.toJson(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(experiment.toPrettyJson(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjsonstub")) {
				return new ResponseEntity<String>(experiment.toPrettyJsonStub(), headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("ERROR: with" + with + " route is not implemented. ", headers,
						HttpStatus.NOT_IMPLEMENTED);
			}
		} else {
			return new ResponseEntity<String>(experiment.toJsonStub(), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<java.lang.String> listJsonByProtocol(@RequestParam Map<String, String> requestParams) {
		// Filter parameters supported: type, kind, name, codeName, protocolKind,
		// protocolType, protocolName, protocolCodeName
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Set<Experiment> result = new HashSet<Experiment>();

		result = experimentService.findExperimentsByRequestMetadata(requestParams);

		return new ResponseEntity<String>(Experiment.toJsonArrayStubWithProt(result), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/protocolKind/{protocolKind}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<java.lang.String> listJsonByProtocolKind(@PathVariable("protocolKind") String protocolKind) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Experiment> result = new ArrayList<Experiment>();
		if (protocolKind != null) {
			List<Protocol> protocols = Protocol.findProtocolsByLsKindEquals(protocolKind).getResultList();
			for (Protocol protocol : protocols) {
				List<Experiment> experiments = Experiment.findExperimentsByProtocol(protocol).getResultList();
				result.addAll(experiments);
			}
		} else {
			result.addAll(Experiment.findAllExperiments());
		}
		return new ResponseEntity<String>(Experiment.toJsonArrayStub(result), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id,
			@RequestParam(value = "with", required = false) String with) {
		Experiment experiment = Experiment.findExperiment(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (experiment == null) {
			logger.info("Did not find the experiment before delete");
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		} else {
			logger.info("deleting the experiment: " + id);
			experiment.logicalDelete();
			if (Experiment.findExperiment(id) == null || Experiment.findExperiment(id).isIgnored()) {
				logger.info("Did not find the experiment after delete");
				return new ResponseEntity<String>(headers, HttpStatus.OK);
			} else {
				logger.info("Found the experiment after delete");
				return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@Transactional
	@RequestMapping(value = "/{id}/deleteChildren", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteAnalysisGroupsByExperiment(@PathVariable("id") Long id) {
		Experiment experiment = Experiment.findExperiment(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (experiment == null) {
			logger.info("Did not find the experiment before delete");
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		} else if (experimentService.deleteAnalysisGroupsByExperiment(experiment)) {
			return new ResponseEntity<String>(headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/codename/{codeName}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindExperimentsByCodeNameEqualsRoute(
			@PathVariable("codeName") String codeName, @RequestParam(value = "with", required = false) String with) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Experiment experiment;
		try {
			experiment = Experiment.findExperimentsByCodeNameEquals(codeName).getSingleResult();
		} catch (NoResultException e) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		if (with != null) {
			if (with.equalsIgnoreCase("analysisgroups")) {
				return new ResponseEntity<String>(experiment.toJsonStubWithAnalysisGroups(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("analysisgroupstates")) {
				return new ResponseEntity<String>(experiment.toJsonStubWithAnalysisGroupStates(), headers,
						HttpStatus.OK);
			} else if (with.equalsIgnoreCase("analysisgroupvalues")) {
				return new ResponseEntity<String>(experiment.toJsonStubWithAnalysisGroupValues(), headers,
						HttpStatus.OK);
			} else if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(experiment.toJson(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(experiment.toPrettyJson(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjsonstub")) {
				return new ResponseEntity<String>(experiment.toPrettyJsonStub(), headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("ERROR: with" + with + " route is not implemented. ", headers,
						HttpStatus.NOT_IMPLEMENTED);
			}
		} else {
			return new ResponseEntity<String>(experiment.toJsonStub(), headers, HttpStatus.OK);
		}
	}

	@Transactional
	@RequestMapping(value = "/codename/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> findExperimentsByCodeNames(@RequestBody List<String> codeNames,
			@RequestParam(value = "with", required = false) String with) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<ExperimentErrorMessageDTO> foundExperiments;
		try {
			foundExperiments = experimentService.findExperimentsByCodeNames(codeNames);
		} catch (NoResultException e) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		if (with != null) {
			if (with.equalsIgnoreCase("analysisgroups")) {
				return new ResponseEntity<String>(ExperimentErrorMessageDTO.toJsonArrayStubWithAG(foundExperiments),
						headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("analysisgroupstates")) {
				return new ResponseEntity<String>(
						ExperimentErrorMessageDTO.toJsonArrayStubWithAGStates(foundExperiments), headers,
						HttpStatus.OK);
			} else if (with.equalsIgnoreCase("analysisgroupvalues")) {
				return new ResponseEntity<String>(
						ExperimentErrorMessageDTO.toJsonArrayStubWithAGValues(foundExperiments), headers,
						HttpStatus.OK);
			} else if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(ExperimentErrorMessageDTO.toJsonArray(foundExperiments), headers,
						HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(ExperimentErrorMessageDTO.toJsonArrayPretty(foundExperiments),
						headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjsonstub")) {
				return new ResponseEntity<String>(ExperimentErrorMessageDTO.toJsonArrayStubPretty(foundExperiments),
						headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("stubwithprot")) {
				return new ResponseEntity<String>(ExperimentErrorMessageDTO.toJsonArrayStubWithProt(foundExperiments),
						headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("ERROR: with" + with + " route is not implemented. ", headers,
						HttpStatus.NOT_IMPLEMENTED);
			}
		} else {
			return new ResponseEntity<String>(ExperimentErrorMessageDTO.toJsonArrayStub(foundExperiments), headers,
					HttpStatus.OK);
		}
	}

	@Transactional
	@RequestMapping(params = "FindByCodeName", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindExperimentsByCodeNameEquals(
			@RequestParam("codeName") String codeName,
			@RequestParam(value = "with", required = false) String with) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Experiment> experiments = Experiment.findExperimentsByCodeNameEquals(codeName).getResultList();
		if (with != null) {
			if (with.equalsIgnoreCase("analysisgroups")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayStubWithAG(experiments), headers,
						HttpStatus.OK);
			} else if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(Experiment.toJsonArray(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayPretty(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjsonstub")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayStubPretty(experiments), headers,
						HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
		}
	}

	@Transactional
	@RequestMapping(value = "/experimentname/**", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindProtocolByExperimentNameEqualsRoute(HttpServletRequest request) {
		String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String experimentName = restOfTheUrl.split("experimentname\\/")[1].replaceAll("/$", "");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(
				Experiment.toJsonArrayStub(Experiment.findExperimentListByExperimentNameAndIgnoredNot(experimentName)),
				headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(params = "FindByExperimentName", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindExperimentByExperimentNameEqualsGet(
			@RequestParam("experimentName") String experimentName,
			@RequestParam(value = "with", required = false) String with,
			@RequestParam(value = "protocolId", required = false) Long protocolId) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		logger.debug("incoming experiment name is " + experimentName);
		List<Experiment> experiments;
		if (protocolId != null && protocolId != 0) {
			experiments = Experiment.findExperimentListByExperimentNameAndProtocolIdAndIgnoredNot(experimentName,
					protocolId);
		} else {
			experiments = Experiment.findExperimentListByExperimentNameAndIgnoredNot(experimentName);
		}
		if (with != null) {
			logger.debug("incoming with param is " + with);
			if (with.equalsIgnoreCase("analysisgroups")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayStubWithAG(experiments), headers,
						HttpStatus.OK);
			} else if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(Experiment.toJsonArray(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayPretty(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjsonstub")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayStubPretty(experiments), headers,
						HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
		}
	}

	@Transactional
	@RequestMapping(params = "FindByName", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindExperimentByNameGet(@RequestParam("name") String name,
			@RequestParam(value = "with", required = false) String with,
			@RequestParam(value = "protocolId", required = false) Long protocolId) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<Experiment> experiments;
		if (protocolId != null && protocolId != 0) {
			experiments = Experiment.findExperimentListByExperimentNameAndProtocolIdAndIgnoredNot(name, protocolId);
		} else {
			experiments = Experiment.findExperimentListByExperimentNameAndIgnoredNot(name);
		}
		if (with != null) {
			if (with.equalsIgnoreCase("analysisgroups")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayStubWithAG(experiments), headers,
						HttpStatus.OK);
			} else if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(Experiment.toJsonArray(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayPretty(experiments), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjsonstub")) {
				return new ResponseEntity<String>(Experiment.toJsonArrayStubPretty(experiments), headers,
						HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("ERROR: with" + with + " route is not implemented. ", headers,
						HttpStatus.NOT_IMPLEMENTED);
			}
		} else {
			return new ResponseEntity<String>(Experiment.toJsonArrayStub(experiments), headers, HttpStatus.OK);
		}
	}

	@Transactional
	@RequestMapping(value = "/protocol/{codeName}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindExperimentsByProtocolCodeName(
			@PathVariable("codeName") String codeName, @RequestParam(value = "projects", required = false) List<String> projects, @RequestParam(value = "with", required = false) String with) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			Collection<Experiment> experiments = experimentService.findExperimentsByProtocolCodeName(codeName, projects);
			return new ResponseEntity<String>(
					Experiment.toJsonArrayStub(experiments),
					headers, HttpStatus.OK);
		} catch (NoResultException e) {
			return new ResponseEntity<String>("[ ]", headers, HttpStatus.NOT_FOUND);

		} catch (TooManyResultsException e) {
			return new ResponseEntity<String>("[ ]", headers, HttpStatus.CONFLICT);
		}
	}

	@Transactional
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> experimentBrowserSearch(
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "projects", required = false) List<String> projects,
			@RequestParam(value = "includeDeleted", required = false) Boolean includeDeleted,
			@RequestParam(value = "q", required = true) String searchQuery) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		logger.debug("############# the search query string is: " + searchQuery);

		try {
			String result;
			if (includeDeleted == null) {
				includeDeleted = false;
			}
			if (userName == null && projects == null) {
				logger.info("--------- accesing experiment search without userName ---------------");
				result = Experiment
						.toJsonArrayStubWithProt(experimentService.findExperimentsByGenericMetaDataSearch(searchQuery, includeDeleted));
				return new ResponseEntity<String>(result, headers, HttpStatus.OK);
			} else if (projects == null) {
				result = Experiment.toJsonArrayStubWithProt(
						experimentService.findExperimentsByGenericMetaDataSearch(searchQuery, userName, includeDeleted));
				return new ResponseEntity<String>(result, headers, HttpStatus.OK);
			} else {
				result = Experiment.toJsonArrayStubWithProt(
						experimentService.findExperimentsByGenericMetaDataSearch(searchQuery, projects, includeDeleted));
				return new ResponseEntity<String>(result, headers, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Caught error in experiment search", e);
			return new ResponseEntity<String>(e.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(params = "find=ByMetadata", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<java.lang.String> listJsonByMetadata(@RequestParam Map<String, String> requestParams) {
		// example url:
		// http://localhost:8080/acas/api/v1/experiments?protocolName=PAMPA%20Buffer%20A&protocolType=default&protocolKind=default&protocolCodeName=PROT-00000001&name=Buffer%20A%20Test01&type=default&kind=default&codeName=EXPT-00000001
		// Filter parameters supported: type, kind, name, codeName
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Set<Experiment> result = new HashSet<Experiment>();

		result = experimentService.findExperimentsByRequestMetadata(requestParams);

		return new ResponseEntity<String>(Experiment.toJsonArrayStubWithProt(result), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/getCodeNameFromNameRequest", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> getCodeNameFromName(@RequestBody String json,
			@RequestParam(value = "experimentType", required = true) String experimentType,
			@RequestParam(value = "experimentKind", required = true) String experimentKind,
			@RequestParam(value = "labelType", required = false) String labelType,
			@RequestParam(value = "labelKind", required = false) String labelKind) {
		PreferredNameRequestDTO requestDTO = PreferredNameRequestDTO.fromJsonToPreferredNameRequestDTO(json);
		logger.info("getCodeNameFromNameRequest incoming json: " + requestDTO.toJson());
		PreferredNameResultsDTO results = experimentService.getCodeNameFromName(experimentType, experimentKind,
				labelType, labelKind, requestDTO);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(results.toJson(), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/getExperimentCodesByDateValueComparison", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> getExperimentCodesByDateValueComparison(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		DateValueComparisonRequest requestDTO = DateValueComparisonRequest.fromJsonToDateValueComparisonRequest(json);
		try {
			Collection<String> results = experimentService.getExperimentCodesByDateValueComparison(requestDTO);
			return new ResponseEntity<String>(new JSONSerializer().exclude("*.class").serialize(results), headers,
					HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught error in getExperimentCodesByDateValueComparison", e);
			return new ResponseEntity<String>(e.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
