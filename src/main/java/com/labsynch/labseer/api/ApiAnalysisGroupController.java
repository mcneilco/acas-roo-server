package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.KeyValueDTO;
import com.labsynch.labseer.exceptions.NotFoundException;
import com.labsynch.labseer.service.AnalysisGroupService;
import com.labsynch.labseer.service.AnalysisGroupValueService;
import com.labsynch.labseer.service.ExperimentStateService;
import com.labsynch.labseer.service.ExperimentValueService;
import com.labsynch.labseer.service.SubjectValueService;
import com.labsynch.labseer.service.TreatmentGroupValueService;
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

@Controller
@RequestMapping("api/v1/analysisgroups")
@Transactional
public class ApiAnalysisGroupController {

	private static final Logger logger = LoggerFactory.getLogger(ApiAnalysisGroupController.class);

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

	@RequestMapping(value = "/subjectsstatus/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> findSubjectValues(
			@PathVariable("id") Long id,
			@RequestParam("stateType") String stateType,
			@RequestParam("stateKind") String stateKind,
			@RequestParam("stateValueType") String stateValueType,
			@RequestParam("stateValueKind") String stateValueKind) {
		List<String> values = new ArrayList<String>();
		AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(id);

		if (analysisGroup != null) {
			Set<TreatmentGroup> treatmentGroups = analysisGroup.getTreatmentGroups();
			for (TreatmentGroup treatmentGroup : treatmentGroups) {
				Set<Subject> subjects = treatmentGroup.getSubjects();
				for (Subject subject : subjects) {
					List<SubjectState> subjectStates = SubjectState
							.findSubjectStatesByLsTypeEqualsAndLsKindEqualsAndSubject(stateType, stateKind, subject)
							.getResultList();
					for (SubjectState subjectState : subjectStates) {
						List<SubjectValue> subjectValues = SubjectValue
								.findSubjectValuesByLsStateAndLsTypeEqualsAndLsKindEquals(subjectState, stateValueType,
										stateValueKind)
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

	// @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers =
	// "Accept=application/json")
	// @ResponseBody
	// public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
	// AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(id);
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// if (analysisGroup == null) {
	// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
	// }
	// return new ResponseEntity<String>(analysisGroup.toJson(), headers,
	// HttpStatus.OK);
	// }

	@Transactional
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id,
			@RequestParam(value = "with", required = false) String with) {
		AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (analysisGroup == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		if (with != null) {
			if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(analysisGroup.toFullJson(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(analysisGroup.toPrettyJson(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjsonstub")) {
				return new ResponseEntity<String>(analysisGroup.toPrettyJsonStub(), headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("ERROR: with" + with + " route is not implemented. ", headers,
						HttpStatus.NOT_IMPLEMENTED);
			}
		} else {
			return new ResponseEntity<String>(analysisGroup.toJson(), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<AnalysisGroup> result = AnalysisGroup.findAllAnalysisGroups();
		return new ResponseEntity<String>(AnalysisGroup.toJsonArray(result), headers, HttpStatus.OK);
	}

	// @RequestMapping(method = RequestMethod.POST, headers =
	// "Accept=application/json")
	// public ResponseEntity<String> createFromJson(@RequestBody String json) {
	// AnalysisGroup analysisGroup = AnalysisGroup.fromJsonToAnalysisGroup(json);
	// analysisGroup.persist();
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	// }

	@Transactional
	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
		AnalysisGroup analysisGroup = AnalysisGroup.fromJsonToAnalysisGroup(json);
		AnalysisGroup newAnalysisGroup = null;
		try {
			newAnalysisGroup = analysisGroupService.saveLsAnalysisGroup(analysisGroup);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(newAnalysisGroup.toJson(), headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		Collection<AnalysisGroup> analysisGroups = AnalysisGroup.fromJsonArrayToAnalysisGroups(json);
		Collection<AnalysisGroup> savedAnalysisGroups = new HashSet<AnalysisGroup>();
		for (AnalysisGroup analysisGroup : analysisGroups) {
			try {
				AnalysisGroup savedAnalysisGroup = analysisGroupService.saveLsAnalysisGroup(analysisGroup);
				savedAnalysisGroups.add(savedAnalysisGroup);
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(AnalysisGroup.toJsonArray(savedAnalysisGroups), headers, HttpStatus.CREATED);
	}

	@Transactional
	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody AnalysisGroup analysisGroup) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		AnalysisGroup updatedAnalysisGroup = analysisGroupService.updateLsAnalysisGroup(analysisGroup);
		// if (analysisGroup.merge() == null) {
		// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		// }
		return new ResponseEntity<String>(updatedAnalysisGroup.toJson(), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJsonArray(@RequestBody List<AnalysisGroup> analysisGroups) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<AnalysisGroup> updatedAnalysisGroups = new HashSet<AnalysisGroup>();
		for (AnalysisGroup analysisGroup : analysisGroups) {
			AnalysisGroup updatedAnalysisGroup = analysisGroupService.updateLsAnalysisGroup(analysisGroup);
			updatedAnalysisGroups.add(updatedAnalysisGroup);
			// if (analysisGroup.merge() == null) {
			// return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
			// }
		}
		return new ResponseEntity<String>(AnalysisGroup.toJsonArray(updatedAnalysisGroups), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (analysisGroup == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		analysisGroup.remove();
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	// TODO: work out a different strategy with the many to many
	// @RequestMapping(params = "find=ByExperiment", headers =
	// "Accept=application/json")
	// @ResponseBody
	// public ResponseEntity<String>
	// jsonFindAnalysisGroupsByExperiment(@RequestParam("experiment") Experiment
	// experiment) {
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/json; charset=utf-8");
	// return new
	// ResponseEntity<String>(AnalysisGroup.toJsonArray(AnalysisGroup.findAnalysisGroupsByExperiments(experiment).getResultList()),
	// headers, HttpStatus.OK);
	// }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonFindAnalysisGroupsByLsTransactionEquals(
			@RequestParam("lsTransaction") Long lsTransaction) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(
				AnalysisGroup.toJsonArray(
						AnalysisGroup.findAnalysisGroupsByLsTransactionEquals(lsTransaction).getResultList()),
				headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{analysisGroupIdOrCodeName}/agvalues/bystate/{stateType}/{stateKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getAnalysisGroupValuesByIdOrCodeNameFilter31(
			@PathVariable("analysisGroupIdOrCodeName") String analysisGroupIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		AnalysisGroup analysisGroup;
		if (SimpleUtil.isNumeric(analysisGroupIdOrCodeName)) {
			analysisGroup = AnalysisGroup.findAnalysisGroup(Long.valueOf(analysisGroupIdOrCodeName));
		} else {
			try {
				analysisGroup = AnalysisGroup.findAnalysisGroupsByCodeNameEquals(analysisGroupIdOrCodeName)
						.getSingleResult();
			} catch (Exception ex) {
				analysisGroup = null;
			}
		}

		List<AnalysisGroupValue> analysisGroupValues;
		if (analysisGroup != null) {
			Long analysisGroupId = analysisGroup.getId();
			analysisGroupValues = analysisGroupValueService
					.getAnalysisGroupValuesByAnalysisGroupIdAndStateTypeKind(analysisGroupId, stateType, stateKind);
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

	@RequestMapping(value = "/{analysisGroupIdOrCodeName}/subjectvalues/bystate/{stateType}/{stateKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getSubjectValuesByAnalysisGroupIdOrCodeNameFilter31(
			@PathVariable("analysisGroupIdOrCodeName") String analysisGroupIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		AnalysisGroup analysisGroup;
		if (SimpleUtil.isNumeric(analysisGroupIdOrCodeName)) {
			analysisGroup = AnalysisGroup.findAnalysisGroup(Long.valueOf(analysisGroupIdOrCodeName));
		} else {
			try {
				analysisGroup = AnalysisGroup.findAnalysisGroupsByCodeNameEquals(analysisGroupIdOrCodeName)
						.getSingleResult();
			} catch (Exception ex) {
				analysisGroup = null;
			}
		}

		List<SubjectValue> subjectValues;
		if (analysisGroup != null) {
			Long analysisGroupId = analysisGroup.getId();
			subjectValues = subjectValueService.getSubjectValuesByAnalysisGroupIdAndStateTypeKind(analysisGroupId,
					stateType, stateKind);
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

	@RequestMapping(value = "/{analysisGroupIdOrCodeName}/tgvalues/bystate/{stateType}/{stateKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getTreatmentGroupValuesByAnalysisGroupIdOrCodeNameAndStateTypeKindFilter41(
			@PathVariable("analysisGroupIdOrCodeName") String analysisGroupIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		AnalysisGroup analysisGroup;
		if (SimpleUtil.isNumeric(analysisGroupIdOrCodeName)) {
			analysisGroup = AnalysisGroup.findAnalysisGroup(Long.valueOf(analysisGroupIdOrCodeName));
		} else {
			try {
				analysisGroup = AnalysisGroup.findAnalysisGroupsByCodeNameEquals(analysisGroupIdOrCodeName)
						.getSingleResult();
			} catch (Exception ex) {
				analysisGroup = null;
			}
		}

		List<TreatmentGroupValue> treatmentGroupValues;
		if (analysisGroup != null) {
			Long analysisGroupId = analysisGroup.getId();
			treatmentGroupValues = treatmentGroupValueService
					.getTreatmentGroupValuesByAnalysisGroupIdAndStateTypeKind(analysisGroupId, stateType, stateKind);
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

	@RequestMapping(value = "/{analysisGroupIdOrCodeName}/agvalues/bystate/{stateType}/{stateKind}/byvalue/{valueType}/{valueKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getAnalysisGroupValueByIdOrCodeNameAndStateTypeKindAndValueTypeKindFilter3(
			@PathVariable("analysisGroupIdOrCodeName") String analysisGroupIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("valueType") String valueType,
			@PathVariable("valueKind") String valueKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		AnalysisGroup analysisGroup;
		if (SimpleUtil.isNumeric(analysisGroupIdOrCodeName)) {
			analysisGroup = AnalysisGroup.findAnalysisGroup(Long.valueOf(analysisGroupIdOrCodeName));
		} else {
			try {
				analysisGroup = AnalysisGroup.findAnalysisGroupsByCodeNameEquals(analysisGroupIdOrCodeName)
						.getSingleResult();
			} catch (Exception ex) {
				analysisGroup = null;
			}
		}

		List<AnalysisGroupValue> analysisGroupValues;
		if (analysisGroup != null) {
			Long analysisGroupId = analysisGroup.getId();
			analysisGroupValues = analysisGroupValueService
					.getAnalysisGroupValuesByAnalysisGroupIdAndStateTypeKindAndValueTypeKind(analysisGroupId, stateType,
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

}
