package com.labsynch.labseer.api;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.dto.ContainerSubjectsDTO;
import com.labsynch.labseer.dto.SubjectCodeNameDTO;
import com.labsynch.labseer.dto.SubjectCsvDataDTO;
import com.labsynch.labseer.dto.TempThingDTO;
import com.labsynch.labseer.service.SubjectService;
import com.labsynch.labseer.service.SubjectValueService;
import com.labsynch.labseer.utils.SimpleUtil;

@Controller
@RequestMapping("api/v1/subjects")
@Transactional
//@RooWebJson(jsonObject = Subject.class)
public class ApiSubjectController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiSubjectController.class);
	
	@Autowired
	private SubjectValueService subjectValueService;
	
	@Autowired
	private SubjectService subjectService;
	

//	@RequestMapping(value = "/{SubjectIdOrCodeName}/values/{SubjectValueId}", method = RequestMethod.GET, headers = "Accept=application/json")
//	@ResponseBody
//	public ResponseEntity<String> getSubjectValueByIdOrCodeName (
//			@PathVariable("SubjectIdOrCodeName") String subjectIdOrCodeName,
//			@PathVariable("SubjectValueId") Long subjectValueId) {
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-Type", "application/json; charset=utf-8");
//
//		SubjectValue result = SubjectValue.findSubjectValue(subjectValueId);
//
//		return (result == null) ?
//			new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND) :
//			new ResponseEntity<String>(result.toJson(), headers, HttpStatus.OK);
//	}
	
	@RequestMapping(value = "/{SubjectIdOrCodeName}/values", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getSubjectValuesForSubjectByIdOrCodeName (
			@PathVariable("SubjectIdOrCodeName") String subjectIdOrCodeName) {		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		
		List<SubjectValue> subjectValues = null;
		Long subjectId = SimpleUtil.isNumeric(subjectIdOrCodeName) ?
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
	
//	@RequestMapping(value = "/values", method = RequestMethod.POST, headers = "Accept=application/json")
//    public @ResponseBody ResponseEntity<String> saveSubjectFromJson(@RequestBody SubjectValue subjectValue) {
//		HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//                     
//	    return (subjectValueService.saveSubjectValue(subjectValue) == null) ?
//	    	new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) :
//	    	new ResponseEntity<String>(headers, HttpStatus.OK);
//    }

//	@RequestMapping(value = "{SubjectIdOrCodeName}/values/{SubjectValueId}", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public @ResponseBody ResponseEntity<String> updateSubjectFromJsonWithId(
//    		@RequestBody SubjectValue subjectValue,
//    		@PathVariable("SubjectValueId") String subjectValueId,
//    		@PathVariable("SubjectIdOrCodeName") String subjectIdOrCodeName) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        
//        if(subjectValue.getId() == null) {
//        	return (subjectValueService.saveSubjectValue(subjectValue) == null) ?
//        	    	new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) :
//        	    	new ResponseEntity<String>(headers, HttpStatus.OK);
//        }      
//        return ((subjectValueService.updateSubjectValue(subjectValue)) == null) ? 
//        		new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) : 
//        		new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
	
//	@RequestMapping(value = "{SubjectIdOrCodeName}/values", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public @ResponseBody ResponseEntity<String> updateSubjectFromJsonWithId(
//    		@RequestBody SubjectValue subjectValue,
//    		@PathVariable("SubjectIdOrCodeName") String subjectIdOrCodeName) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        
//        if(subjectValue.getId() == null) {
//        	return (subjectValueService.saveSubjectValue(subjectValue) == null) ?
//        	    	new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) :
//        	    	new ResponseEntity<String>(headers, HttpStatus.OK);
//        }      
//        return ((subjectValueService.updateSubjectValue(subjectValue)) == null) ? 
//        		new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST) : 
//        		new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
	

	@Transactional
    @RequestMapping(method = RequestMethod.GET, value = "/{idOrCodeName}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("idOrCodeName") String idOrCodeName) {
    	Subject subject;
    	if(SimpleUtil.isNumeric(idOrCodeName)) {
    	    	subject = Subject.findSubject(Long.valueOf(idOrCodeName));
 		} else {
 			subject = Subject.findSubjectByCodeNameEquals(idOrCodeName);
 		}
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (subject == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(subject.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Subject> result = Subject.findAllSubjects();
        return new ResponseEntity<String>(Subject.toJsonArray(result), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        Subject subject = Subject.fromJsonToSubject(json);
        subject = subjectService.saveSubject(subject);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(subject.toJson(), headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        Collection<Subject> savedSubjects = new HashSet<Subject>();
		for (Subject subject: Subject.fromJsonArrayToSubjects(json)) {
            savedSubjects.add(subjectService.saveSubject(subject));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(Subject.toJsonArray(savedSubjects), headers, HttpStatus.CREATED);
    }

	@RequestMapping(value={"/","/{id}"}, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Subject subject = Subject.fromJsonToSubject(json);
        try{
        	Subject foundSubject = Subject.findSubject(subject.getId());
        }catch(EmptyResultDataAccessException e){
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        subject = subjectService.updateSubject(subject);
        return new ResponseEntity<String>(subject.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        Collection<Subject> updatedSubjects = new HashSet<Subject>();
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (Subject subject: Subject.fromJsonArrayToSubjects(json)) {
        	try{
            	Subject foundSubject = Subject.findSubject(subject.getId());
            }catch(EmptyResultDataAccessException e){
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            updatedSubjects.add(subjectService.updateSubject(subject));
        }
        return new ResponseEntity<String>(Subject.toJsonArray(updatedSubjects), headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        Subject subject = Subject.findSubject(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (subject == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        subject.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByCodeNameEquals", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindSubjectsByCodeNameEquals(@RequestParam("codeName") String codeName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Subject.toJsonArray(Subject.findSubjectsByCodeNameEquals(codeName).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindSubjectsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Subject.toJsonArray(Subject.findSubjectsByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
    }

//	@RequestMapping(params = "find=ByTreatmentGroup", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<String> jsonFindSubjectsByTreatmentGroup(@RequestParam("treatmentGroup") TreatmentGroup treatmentGroup) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>(Subject.toJsonArray(Subject.findSubjectsByTreatmentGroups(treatmentGroup).getResultList()), headers, HttpStatus.OK);
//    }
	
    @Transactional
    @RequestMapping(value = "/getSubjectsByCodeNames", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getSubjectsByCodeNames(@RequestBody List<String> codeNames) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	Collection<SubjectCodeNameDTO> searchResults = subjectService.getSubjectsByCodeNames(codeNames);
        	return new ResponseEntity<String>(SubjectCodeNameDTO.toJsonArray(searchResults), headers, HttpStatus.OK);
        } catch (Exception e){
        	logger.error("Uncaught error in getSubjectsByCodeNames",e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional	
	@RequestMapping(value = "/savefromtsv", method = RequestMethod.POST, headers = "Accept=application/json")
	public @ResponseBody ResponseEntity<String> saveSubjectsFromCsv(@RequestBody SubjectCsvDataDTO subjectCsvDataDTO) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		logger.info("loading data from tsv files: " + subjectCsvDataDTO.toJson());
		
		String subjectFilePath = subjectCsvDataDTO.getSubjectCsvFilePath();
		try{
			long startTime = new Date().getTime();
			HashMap<String, TempThingDTO> resultMap = subjectService.createOnlySubjectsFromCSV(subjectFilePath, subjectCsvDataDTO.getTreatmentGroupIds());
			long endTime = new Date().getTime();
			long totalTime = endTime - startTime;
			logger.info("dataLoaded: " + "true" + "   total elapsed time: " + totalTime);
			Collection<TempThingDTO> resultArray = resultMap.values();
			return new ResponseEntity<String>(TempThingDTO.toJsonArray(resultArray), headers, HttpStatus.OK) ;
		}catch (Exception e){
			logger.error("Error in api/v1/subjects/savefromtsv",e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    
    @Transactional
    @RequestMapping(value = "/getSubjectsByContainerInteractions", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getSubjectsByContainerInteractions(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	Collection<ContainerSubjectsDTO> requests = ContainerSubjectsDTO.fromJsonArrayToCoes(json);
        	Collection<ContainerSubjectsDTO> results = subjectService.getSubjectsByContainerAndInteraction(requests);
        	return new ResponseEntity<String>(ContainerSubjectsDTO.toJsonArray(results), headers, HttpStatus.OK);
        } catch (Exception e){
        	logger.error("Uncaught error in getSubjectsByCodeNames",e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
