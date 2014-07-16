package com.labsynch.labseer.api;

import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.service.SubjectValueService;

@Controller
@RequestMapping("api/v1/subject")
@Transactional
@RooWebJson(jsonObject = Subject.class)
public class ApiSubjectController {
	
	@Autowired
	private SubjectValueService subjectValueService;

	@RequestMapping(value = "/{SubjectIdOrCodeName}/values/{SubjectValueId}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getSubjectValueByIdOrCodeName (
			@PathVariable("SubjectIdOrCodeName") String subjectIdOrCodeName,
			@PathVariable("SubjectValueId") Long subjectValueId) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		SubjectValue result = SubjectValue.findSubjectValue(subjectValueId);

		return (result == null) ?
			new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND) :
			new ResponseEntity<String>(result.toJson(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{SubjectIdOrCodeName}/values", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getSubjectValuesForSubjectByIdOrCodeName (
			@PathVariable("SubjectIdOrCodeName") String subjectIdOrCodeName) {		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		
		List<SubjectValue> subjectValues = null;
		Long subjectId = isNumeric(subjectIdOrCodeName) ?
				Long.valueOf(subjectIdOrCodeName) :
				retrieveSubjectIdFromCodeName(subjectIdOrCodeName);
		if(subjectId != null) {
			subjectValues = subjectValueService.getSubjectValuesBySubjectId(subjectId);
		}
		
		return (subjectValues == null) ?
			new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND) :
			new ResponseEntity<String>(SubjectValue.toJsonArray(subjectValues), headers, HttpStatus.OK);
	}
	
	private static Long retrieveSubjectIdFromCodeName(String codeName) {
		List<Subject> subjects = Subject.findSubjectsByCodeNameEquals(codeName).getResultList();
		return (subjects == null || subjects.size() == 0) ? 
					null :
					subjects.get(0).getId();
	}
	
	@RequestMapping(value = "/values", method = RequestMethod.POST, headers = "Accept=application/json")
    public @ResponseBody ResponseEntity<String> saveSubjectFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
             
        SubjectValue subjectValue = SubjectValue.fromJsonToSubjectValue(json);
        
	    return (subjectValueService.saveSubjectValue(subjectValue) == null) ?
	    	new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) :
	    	new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "{SubjectIdOrCodeName}/values/{SubjectValueId}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody ResponseEntity<String> updateSubjectFromJsonWithId(
    		@RequestBody String json,
    		@PathVariable("SubjectValueId") String subjectValueId,
    		@PathVariable("SubjectIdOrCodeName") String subjectIdOrCodeName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        SubjectValue subjectValue = SubjectValue.fromJsonToSubjectValue(json);
        if(subjectValue.getId() == null) {
        	return (subjectValueService.saveSubjectValue(subjectValue) == null) ?
        	    	new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) :
        	    	new ResponseEntity<String>(headers, HttpStatus.OK);
        }      
        return ((subjectValueService.updateSubjectValue(subjectValue)) == null) ? 
        		new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) : 
        		new ResponseEntity<String>(headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "{SubjectIdOrCodeName}/values", method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody ResponseEntity<String> updateSubjectFromJsonWithId(
    		@RequestBody String json,
    		@PathVariable("SubjectIdOrCodeName") String subjectIdOrCodeName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        SubjectValue subjectValue = SubjectValue.fromJsonToSubjectValue(json);
        if(subjectValue.getId() == null) {
        	return (subjectValueService.saveSubjectValue(subjectValue) == null) ?
        	    	new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) :
        	    	new ResponseEntity<String>(headers, HttpStatus.OK);
        }      
        return ((subjectValueService.updateSubjectValue(subjectValue)) == null) ? 
        		new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) : 
        		new ResponseEntity<String>(headers, HttpStatus.OK);
    }
	
	private static boolean isNumeric(String str) {
	    for (char c : str.toCharArray()) {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
}
