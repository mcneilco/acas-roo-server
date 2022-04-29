package com.labsynch.labseer.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.service.ItxContainerContainerService;
import com.labsynch.labseer.utils.PropertiesUtilService;

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
@RequestMapping("/api/v1/itxcontainercontainers")
public class ApiItxContainerContainerController {

    private static final Logger logger = LoggerFactory.getLogger(ApiItxContainerContainerController.class);

    @Autowired
    private ItxContainerContainerService itxContainerContainerService;

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @Transactional
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        ItxContainerContainer itxContainerContainer = ItxContainerContainer.findItxContainerContainer(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (itxContainerContainer == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(itxContainerContainer.toJson(), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ItxContainerContainer> result = ItxContainerContainer.findAllItxContainerContainers();
        return new ResponseEntity<String>(ItxContainerContainer.toJsonArray(result), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody ItxContainerContainer itxContainerContainer) {
        itxContainerContainer = itxContainerContainerService.saveLsItxContainer(itxContainerContainer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(itxContainerContainer.toJson(), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromLsJsonArray(@RequestBody List<ItxContainerContainer> itxContainerContainers) {
        Collection<ItxContainerContainer> savedItxContainerContainers = itxContainerContainerService.saveLsItxContainers(itxContainerContainers);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ItxContainerContainer.toJsonArray(savedItxContainerContainers), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = { "","/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ItxContainerContainer itxContainerContainer = ItxContainerContainer.fromJsonToItxContainerContainer(json);
        ItxContainerContainer updatedItxContainerContainer = null;
        try{
			updatedItxContainerContainer = itxContainerContainerService.updateItxContainerContainer(itxContainerContainer);
	        return new ResponseEntity<String>(updatedItxContainerContainer.toJson(), headers, HttpStatus.OK);
        } catch (Exception e) {
			logger.error("Caught error updating ItxContainerContainer from JSON",e);
			return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ItxContainerContainer> updatedItxContainerContainers = new HashSet<ItxContainerContainer>();
        try{
            for (ItxContainerContainer itxContainerContainer: ItxContainerContainer.fromJsonArrayToItxContainerContainers(json)) {
            	ItxContainerContainer updatedItxContainerContainer = itxContainerContainerService.updateItxContainerContainer(itxContainerContainer);
            	updatedItxContainerContainers.add(updatedItxContainerContainer);
            }
	        return new ResponseEntity<String>(ItxContainerContainer.toJsonArray(updatedItxContainerContainers), headers, HttpStatus.OK);
        } catch (Exception e) {
			logger.error("Caught error updating ItxContainerContainers from JSON",e);
			return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        ItxContainerContainer itxContainerContainer = ItxContainerContainer.findItxContainerContainer(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (itxContainerContainer == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        itxContainerContainer.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}
