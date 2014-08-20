package com.labsynch.labseer.api;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.dto.KeyValueDTO;
import com.labsynch.labseer.service.AnalysisGroupValueService;

@Controller
@RequestMapping("api/v1/analysisgroups")
@RooWebFinder
@Transactional
@RooWebJson(jsonObject = AnalysisGroup.class)
public class ApiAnalysisGroupController {
	
    private static final Logger logger = LoggerFactory.getLogger(ApiAnalysisGroupController.class);

    @Autowired
	private AnalysisGroupValueService analysisGroupValueService;
    
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
        List<KeyValueDTO> lsValues = new ArrayList<KeyValueDTO>();
        AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(id);
        List<TreatmentGroup> treatmentGroups = TreatmentGroup.findTreatmentGroupsByAnalysisGroup(analysisGroup).getResultList();
        for (TreatmentGroup treatmentGroup : treatmentGroups){
            List<Subject> subjects = Subject.findSubjectsByTreatmentGroup(treatmentGroup).getResultList();
            for (Subject subject : subjects){
                List<SubjectState> subjectStates = SubjectState.findSubjectStatesByLsTypeEqualsAndLsKindEqualsAndSubject(stateType, stateKind, subject).getResultList();
                for (SubjectState subjectState : subjectStates){
                    List<SubjectValue> subjectValues = SubjectValue.findSubjectValuesByLsStateAndLsTypeEqualsAndLsKindEquals(subjectState, stateValueType, stateValueKind).getResultList();
                    for (SubjectValue subjectValue : subjectValues){
                        //KeyValueDTO kvDTO = new KeyValueDTO();
                        //kvDTO.setKey("lsValue");
                        if (stateValueType.equalsIgnoreCase("stringValue")) {
                            //kvDTO.setValue(subjectValue.getStringValue());
                            values.add(subjectValue.getStringValue());
                        } else if (stateValueType.equalsIgnoreCase("numericValue")) {
                            //kvDTO.setValue(subjectValue.getNumericValue().toString());
                            values.add(subjectValue.getNumericValue().toString());
                        }
                        //lsValues.add(kvDTO);
                    }
                }
            }
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (analysisGroup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        KeyValueDTO transferDTO = new KeyValueDTO();
        transferDTO.setKey("lsValue");
        transferDTO.setValue(values.toString());
        //return new ResponseEntity<String>(KeyValueDTO.toJsonArray(lsValues), headers, HttpStatus.OK);
        return new ResponseEntity<String>(transferDTO.toJson(), headers, HttpStatus.OK);
    }



	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (analysisGroup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(analysisGroup.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<AnalysisGroup> result = AnalysisGroup.findAllAnalysisGroups();
        return new ResponseEntity<String>(AnalysisGroup.toJsonArray(result), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        AnalysisGroup analysisGroup = AnalysisGroup.fromJsonToAnalysisGroup(json);
        analysisGroup.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (AnalysisGroup analysisGroup: AnalysisGroup.fromJsonArrayToAnalysisGroups(json)) {
            analysisGroup.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        AnalysisGroup analysisGroup = AnalysisGroup.fromJsonToAnalysisGroup(json);
        if (analysisGroup.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (AnalysisGroup analysisGroup: AnalysisGroup.fromJsonArrayToAnalysisGroups(json)) {
            if (analysisGroup.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (analysisGroup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        analysisGroup.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByExperiment", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindAnalysisGroupsByExperiment(@RequestParam("experiment") Experiment experiment) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(AnalysisGroup.toJsonArray(AnalysisGroup.findAnalysisGroupsByExperiment(experiment).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindAnalysisGroupsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(AnalysisGroup.toJsonArray(AnalysisGroup.findAnalysisGroupsByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/{analysisGroupIdOrCodeName}/agvalues/bystate/{stateType}/{stateKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> getAnalysisGroupValuesByIdOrCodeNameFilter31 (
			@PathVariable("analysisGroupIdOrCodeName") String analysisGroupIdOrCodeName,
			@PathVariable("stateType") String stateType,
			@PathVariable("stateKind") String stateKind,
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		AnalysisGroup analysisGroup;
		if(isNumeric(analysisGroupIdOrCodeName)) {
			analysisGroup = AnalysisGroup.findAnalysisGroup(Long.valueOf(analysisGroupIdOrCodeName));
		} else {		
			try {
				analysisGroup = AnalysisGroup.findAnalysisGroupsByCodeNameEquals(analysisGroupIdOrCodeName).getSingleResult();
			} catch(Exception ex) {
				analysisGroup = null;
			}
		}

		List<AnalysisGroupValue> analysisGroupValues;
		if(analysisGroup != null) {
			Long analysisGroupId = analysisGroup.getId();
			analysisGroupValues = analysisGroupValueService.getAnalysisGroupValuesByAnalysisGroupIdAndStateTypeKind(analysisGroupId, stateType, stateKind);
		} else {
			analysisGroupValues = new ArrayList<AnalysisGroupValue>();
		}
		if (format.equalsIgnoreCase("tsv")) {
			String outputString = analysisGroupValueService.getCsvList(analysisGroupValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			//default format is json
			return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(analysisGroupValues), headers, HttpStatus.OK);
		}
	}
	
	//TODO: LATER
//	@RequestMapping(value = "/{experimentIdOrCodeName}/agvalues/bystate/{stateType}/{stateKind}/byvalue/{valueType}/{valueKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
//	@ResponseBody
//	@Transactional
//	public ResponseEntity<String> getAnalysisGroupValueByIdOrCodeNameAndStateTypeKindAndValueTypeKindFilter3 (
//			@PathVariable("experimentIdOrCodeName") String experimentIdOrCodeName,
//			@PathVariable("stateType") String stateType,
//			@PathVariable("stateKind") String stateKind,
//			@PathVariable("valueType") String valueType,
//			@PathVariable("valueKind") String valueKind,
//			@PathVariable("format") String format) {
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-Type", "application/json; charset=utf-8");
//
//		Experiment experiment;
//		if(isNumeric(experimentIdOrCodeName)) {
//			experiment = Experiment.findExperiment(Long.valueOf(experimentIdOrCodeName));
//		} else {		
//			try {
//				experiment = Experiment.findExperimentsByCodeNameEquals(experimentIdOrCodeName).getSingleResult();
//			} catch(Exception ex) {
//				experiment = null;
//			}
//		}
//
//		List<AnalysisGroupValue> analysisGroupValues;
//		if(experiment != null) {
//			Long experimentId = experiment.getId();
//			analysisGroupValues = analysisGroupValueService.getAnalysisGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind, valueType, valueKind);
//		} else {
//			analysisGroupValues = new ArrayList<AnalysisGroupValue>();
//		}
//		if (format.equalsIgnoreCase("tsv")) {
//			String outputString = analysisGroupValueService.getCsvList(analysisGroupValues);
//			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
//		} else {
//			//default format is json
//			return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(analysisGroupValues), headers, HttpStatus.OK);
//		}
//	}

	
	private static boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) return false;
		}
		return true;
	}

	
}
