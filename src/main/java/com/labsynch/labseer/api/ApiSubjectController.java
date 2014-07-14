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
import com.labsynch.labseer.utils.PropertiesUtilService;

@Controller
@RequestMapping("api/v1/subject")
@Transactional
@RooWebJson(jsonObject = Subject.class)
public class ApiSubjectController {

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private SubjectValueService subjectValueService;

	@RequestMapping(value = "/{IdOrCodeName}/values/{Id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getSubjectValueByIdOrCodeName (
			@PathVariable("IdOrCodeName") String IdOrCodeName,
			@PathVariable("Id") Long Id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		
		List<SubjectValue> subjectValues = null;
		Long id = null;
		if(isNumeric(IdOrCodeName)) {
			id = Long.valueOf(IdOrCodeName);
		} else {
			id = retrieveSubjectIdFromCodeName(IdOrCodeName);
		}
		if(id != null) {
			subjectValues = subjectValueService.getSubjectValuesBySubjectId(Long.valueOf(id));
		} 
		
		SubjectValue result = null;
		
		for(SubjectValue subjectValue : subjectValues) {
			if(subjectValue.getId() == Id) {
				result = subjectValue;
				break;
			}
		}
		return (result == null) ?
			new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND) :
			new ResponseEntity<String>(result.toJson(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{IdOrCodeName}/values", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getSubjectValuesForSubjectByIdOrCodeName (
			@PathVariable("IdOrCodeName") String IdOrCodeName) {		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		
		List<SubjectValue> subjectValues = null;
		Long id = null;
		if(isNumeric(IdOrCodeName)) {
			id = Long.valueOf(IdOrCodeName);
		} else {
			id = retrieveSubjectIdFromCodeName(IdOrCodeName);
		}
		if(id != null) {
			subjectValues = subjectValueService.getSubjectValuesBySubjectId(Long.valueOf(id));
		}
		
		return (subjectValues == null) ?
			new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND) :
			new ResponseEntity<String>(SubjectValue.toJsonArray(subjectValues), headers, HttpStatus.OK);
	}
	
	private static Long retrieveSubjectIdFromCodeName(String codeName) {
		Long id = null;
		List<Subject> subjects = Subject.findAllSubjects();
		for(Subject su : subjects) {
			if(su.getCodeName() != null && su.getCodeName().compareTo(codeName) == 0) {
				id = su.getId();
				break;
			}
		}
		return id;
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

	@RequestMapping(value = "{IdOrCodeName}/values/{Id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody ResponseEntity<String> updateSubjectFromJsonWithId(
    		@RequestBody String json,
    		@PathVariable("Id") String Id,
    		@PathVariable("IdOrCodeName") String IdOrCodeName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        SubjectValue subjectValue = SubjectValue.fromJsonToSubjectValue(json);
        if(subjectValue.getId() == null) {
        	return (subjectValueService.saveSubjectValue(subjectValue) != null) ?
        	    	new ResponseEntity<String>(headers, HttpStatus.OK) :
        	    	new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        }      
        return ((subjectValueService.updateSubjectValue(subjectValue)) == null) ? 
        		new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) : 
        		new ResponseEntity<String>(headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "{IdOrCodeName}/values", method = RequestMethod.PUT, headers = "Accept=application/json")
    public @ResponseBody ResponseEntity<String> updateSubjectFromJsonWithId(
    		@RequestBody String json,
    		@PathVariable("IdOrCodeName") String IdOrCodeName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        
        SubjectValue subjectValue = SubjectValue.fromJsonToSubjectValue(json);
        if(subjectValue.getId() == null) {
        	return (subjectValueService.saveSubjectValue(subjectValue) != null) ?
        	    	new ResponseEntity<String>(headers, HttpStatus.OK) :
        	    	new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
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
