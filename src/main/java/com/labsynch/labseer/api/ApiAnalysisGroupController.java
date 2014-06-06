package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.dto.KeyValueDTO;

@Controller
@RequestMapping("api/v1/analysisgroups")
@RooWebFinder
@Transactional
@RooWebJson(jsonObject = AnalysisGroup.class)
public class ApiAnalysisGroupController {

	private static final Logger logger = LoggerFactory.getLogger(ApiAnalysisGroupController.class);

	
    @RequestMapping(value = "/subjectsstatus/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> findSubjectValues(
    		@PathVariable("id") Long id,
    		@RequestParam("stateType") String stateType,
    		@RequestParam("stateKind") String stateKind,
    		@RequestParam("stateValueType") String stateValueType,
    		@RequestParam("stateValueKind") String stateValueKind
    		) {

    	List<KeyValueDTO> lsValues = new ArrayList<KeyValueDTO>();
        AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(id);
    	List<TreatmentGroup> treatmentGroups = TreatmentGroup.findTreatmentGroupsByAnalysisGroup(analysisGroup).getResultList();
    	for (TreatmentGroup treatmentGroup : treatmentGroups){
    	   	List<Subject> subjects = Subject.findSubjectsByTreatmentGroup(treatmentGroup).getResultList();
    	   	for (Subject subject : subjects){
    	   		List<SubjectState> subjectStates = SubjectState.findSubjectStatesByLsTypeEqualsAndLsKindEqualsAndSubject(stateType, stateKind, subject).getResultList();
    	   		for (SubjectState subjectState : subjectStates){
    	   			List<SubjectValue> subjectValues = SubjectValue.findSubjectValuesByLsStateAndLsTypeEqualsAndLsKindEquals(subjectState, stateValueType, stateKind).getResultList();
    	   			for (SubjectValue subjectValue : subjectValues){
    	   				KeyValueDTO kvDTO = new KeyValueDTO();
    	   				kvDTO.setKey("lsValue");
    	   				kvDTO.setValue(subjectValue.getStringValue());
    	   				lsValues.add(kvDTO);
    	   			}
    	   		}
    	   	}
    	       		
    	}
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (analysisGroup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(KeyValueDTO.toJsonArray(lsValues), headers, HttpStatus.OK);
    }


}
