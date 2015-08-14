package com.labsynch.labseer.api;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.service.ItxLsThingLsThingService;
import com.labsynch.labseer.utils.PropertiesUtilService;

import flexjson.JSONTokener;

import java.io.BufferedReader;
import java.io.FileReader;
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
@RequestMapping("/api/v1/itxLsThingLsThings")
public class ApiItxLsThingLsThingController {

    private static final Logger logger = LoggerFactory.getLogger(ApiItxLsThingLsThingController.class);

    @Autowired
    private ItxLsThingLsThingService itxLsThingLsThingService;

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @Transactional
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        ItxLsThingLsThing itxLsThingLsThing = ItxLsThingLsThing.findItxLsThingLsThing(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (itxLsThingLsThing == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(itxLsThingLsThing.toJson(), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ItxLsThingLsThing> result = ItxLsThingLsThing.findAllItxLsThingLsThings();
        return new ResponseEntity<String>(ItxLsThingLsThing.toJsonArray(result), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
    	ItxLsThingLsThing itxLsThingLsThing = ItxLsThingLsThing.fromJsonToItxLsThingLsThing(json);
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try{
        	ItxLsThingLsThing savedItxLsThingLsThing = itxLsThingLsThingService.saveItxLsThingLsThing(itxLsThingLsThing);
            return new ResponseEntity<String>(savedItxLsThingLsThing.toJson(), headers, HttpStatus.CREATED);
        }catch(Exception e){
        	logger.error("Caught error saving ItxLsThingLsThing from JSON",e);
        	return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromLsJsonArray(@RequestBody String json) {
    	Collection<ItxLsThingLsThing> itxLsThingLsThings = ItxLsThingLsThing.fromJsonArrayToItxLsThingLsThings(json);
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try{
        	Collection<ItxLsThingLsThing> savedItxLsThingLsThings = new HashSet<ItxLsThingLsThing>();
        	for (ItxLsThingLsThing itxLsThingLsThing : itxLsThingLsThings){
            	ItxLsThingLsThing savedItxLsThingLsThing = itxLsThingLsThingService.saveItxLsThingLsThing(itxLsThingLsThing);
            	savedItxLsThingLsThings.add(savedItxLsThingLsThing);
            }
            return new ResponseEntity<String>(ItxLsThingLsThing.toJsonArray(savedItxLsThingLsThings), headers, HttpStatus.CREATED);
        }catch(Exception e){
        	logger.error("Caught error saving ItxLsThingLsThings from JSON",e);
        	return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
    	ItxLsThingLsThing itxLsThingLsThing = ItxLsThingLsThing.fromJsonToItxLsThingLsThing(json);
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try{
        	ItxLsThingLsThing updatedItxLsThingLsThing = itxLsThingLsThingService.updateItxLsThingLsThing(itxLsThingLsThing);
            return new ResponseEntity<String>(updatedItxLsThingLsThing.toJson(), headers, HttpStatus.OK);
        }catch(Exception e){
        	logger.error("Caught error updating ItxLsThingLsThing from JSON",e);
        	return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody List<ItxLsThingLsThing> itxLsThingLsThings) {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try{
        	Collection<ItxLsThingLsThing> updatedItxLsThingLsThings = new HashSet<ItxLsThingLsThing>();
        	for (ItxLsThingLsThing itxLsThingLsThing : itxLsThingLsThings){
            	ItxLsThingLsThing updatedItxLsThingLsThing = itxLsThingLsThingService.updateItxLsThingLsThing(itxLsThingLsThing);
            	updatedItxLsThingLsThings.add(updatedItxLsThingLsThing);
            }
            return new ResponseEntity<String>(ItxLsThingLsThing.toJsonArray(updatedItxLsThingLsThings), headers, HttpStatus.OK);
        }catch(Exception e){
        	logger.error("Caught error saving ItxLsThingLsThings from JSON",e);
        	return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
