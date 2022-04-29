package com.labsynch.labseer.api;

import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.service.ItxSubjectContainerService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import flexjson.JSONTokener;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.IOUtils;
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
@RequestMapping("/api/v1/itxsubjectcontainers")
public class ApiItxSubjectContainerController {

    @Autowired
    private ItxSubjectContainerService itxSubjectContainerService;

    private static final Logger logger = LoggerFactory.getLogger(ApiItxSubjectContainerController.class);

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        ItxSubjectContainer itxSubjectContainer = ItxSubjectContainer.findItxSubjectContainer(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (itxSubjectContainer == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(itxSubjectContainer.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/getBySubjectIdOrCodeName/{subjectIdOrCodeName}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getBySubjectIdOrCodeName(@PathVariable("subjectIdOrCodeName") String subjectIdOrCodeName) {
        Subject subject = null;
    	if (SimpleUtil.isNumeric(subjectIdOrCodeName)){
        	subject = Subject.findSubject(Long.valueOf(subjectIdOrCodeName));
        }else{
        	subject = Subject.findSubjectByCodeNameEquals(subjectIdOrCodeName);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (subject == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        Collection<ItxSubjectContainer> itxSubjectContainers = subject.getContainers();
        return new ResponseEntity<String>(ItxSubjectContainer.toJsonArray(itxSubjectContainers), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ItxSubjectContainer> result = ItxSubjectContainer.findAllItxSubjectContainers();
        return new ResponseEntity<String>(ItxSubjectContainer.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody ItxSubjectContainer itxSubjectContainer) {
        logger.debug("Incoming itxSubjectContainer JSON: "+itxSubjectContainer.toJson());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try{
        	ItxSubjectContainer savedItx = itxSubjectContainerService.saveLsItxSubjectContainer(itxSubjectContainer);
            return new ResponseEntity<String>(savedItx.toJson(), headers, HttpStatus.CREATED);
        }catch(Exception e){
        	logger.error("Caught error creating itxSubjectContainerContainer from JSON", e);
        	return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody List<ItxSubjectContainer> itxSubjectContainers) {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try{
        	Collection<ItxSubjectContainer> savedItxSubjectContainers = new ArrayList<ItxSubjectContainer>();
            for (ItxSubjectContainer itxSubjectContainer : itxSubjectContainers) {
                savedItxSubjectContainers.add(itxSubjectContainerService.saveLsItxSubjectContainer(itxSubjectContainer));
            }
            return new ResponseEntity<String>(ItxSubjectContainer.toJsonArray(savedItxSubjectContainers), headers, HttpStatus.CREATED);
        }catch(Exception e){
        	logger.error("Caught error creating itxSubjectContainerContainer from JSON Array", e);
        	return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    	
    }

    @Transactional
    @RequestMapping(value = { "","/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ItxSubjectContainer itxSubjectContainer = ItxSubjectContainer.fromJsonToItxSubjectContainer(json);
        ItxSubjectContainer updatedItxSubjectContainer = null;
        try{
			updatedItxSubjectContainer = itxSubjectContainerService.updateItxSubjectContainer(itxSubjectContainer);
	        return new ResponseEntity<String>(updatedItxSubjectContainer.toJson(), headers, HttpStatus.OK);
        } catch (Exception e) {
			logger.error("Caught error updating ItxSubjectContainer from JSON",e);
			return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ItxSubjectContainer> updatedItxSubjectContainers = new HashSet<ItxSubjectContainer>();
        try{
            for (ItxSubjectContainer itxSubjectContainer: ItxSubjectContainer.fromJsonArrayToItxSubjectContainers(json)) {
            	ItxSubjectContainer updatedItxSubjectContainer = itxSubjectContainerService.updateItxSubjectContainer(itxSubjectContainer);
            	updatedItxSubjectContainers.add(updatedItxSubjectContainer);
            }
	        return new ResponseEntity<String>(ItxSubjectContainer.toJsonArray(updatedItxSubjectContainers), headers, HttpStatus.OK);
        } catch (Exception e) {
			logger.error("Caught error updating ItxSubjectContainers from JSON",e);
			return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        ItxSubjectContainer itxSubjectContainer = ItxSubjectContainer.findItxSubjectContainer(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (itxSubjectContainer == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        itxSubjectContainer.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}
