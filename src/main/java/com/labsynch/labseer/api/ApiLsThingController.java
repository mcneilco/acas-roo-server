package com.labsynch.labseer.api;

import java.util.List;

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

import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.service.GeneThingService;
import com.labsynch.labseer.service.LsThingService;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Controller
@RequestMapping("api/v1/lsthings")
@Transactional
public class ApiLsThingController {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(ApiLsThingController.class);

    @Autowired
    private LsThingService lsThingService;

    @Autowired
    private GeneThingService geneThingService;

    @Autowired
    private PropertiesUtilService propertiesUtilService;
    
    @RequestMapping(value = "/getGeneCodeNameFromNameRequest", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getGeneCodeNameFromName(@RequestBody PreferredNameRequestDTO requestDTO) {
        String thingType = "gene";
        String thingKind = "entrez gene";
        String labelType = "name";
        String labelKind = "Entrez Gene ID";
        logger.info("getGeneCodeNameFromNameRequest incoming json: " + requestDTO.toJson());
        PreferredNameResultsDTO results = lsThingService.getPreferredNameFromName(thingType, thingKind, labelType, labelKind, requestDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(results.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/getCodeNameFromNameRequest", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getCodeNameFromName(@RequestBody PreferredNameRequestDTO requestDTO, 
    		@RequestParam(value = "thingType", required = true) String thingType, 
    		@RequestParam(value = "thingKind", required = true) String thingKind, 
    		@RequestParam(value = "labelType", required = false) String labelType, 
    		@RequestParam(value = "labelKind", required = false) String labelKind) {
        logger.info("getCodeNameFromNameRequest incoming json: " + requestDTO.toJson());
        PreferredNameResultsDTO results = lsThingService.getCodeNameFromName(thingType, thingKind, labelType, labelKind, requestDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(results.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/getPreferredNameFromNameRequest", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getPreferredNameFromName(@RequestBody PreferredNameRequestDTO requestDTO, @RequestParam(value = "thingType", required = true) String thingType, @RequestParam(value = "thingKind", required = true) String thingKind, @RequestParam(value = "labelType", required = false) String labelType, @RequestParam(value = "labelKind", required = false) String labelKind) {
        PreferredNameResultsDTO results = lsThingService.getPreferredNameFromName(thingType, thingKind, labelType, labelKind, requestDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(results.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getProjects() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(lsThingService.getProjectCodes(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{thingType}/{thingKind}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getLsThingByTypeAndKindAll(@PathVariable("thingType") String thingType, 
    		@PathVariable("thingKind") String thingKind, 
    		@RequestParam(value = "labelText", required = false) String labelText,
    		@RequestParam(value = "labelType", required = false) String labelType,
    		@RequestParam(value = "labelKind", required = false) String labelKind,
    		@RequestParam(value = "with", required = false) String with) {
    	List<LsThing> results;
    	boolean withLabelText = (labelText != null && !labelText.equalsIgnoreCase(""));
    	boolean withLabelType = (labelType != null && !labelType.equalsIgnoreCase(""));
    	boolean withLabelKind = (labelKind != null && !labelKind.equalsIgnoreCase(""));
    	if (withLabelText) {
    		if (withLabelType){	
    			if (withLabelKind){
    				results = LsThing.findLsThingByLabelText(thingType, thingKind, labelType, labelKind, labelText).getResultList();
    			} else results = LsThing.findLsThingByLabelTypeAndLabelText(thingType, thingKind, labelType, labelText).getResultList();
    		} else if (withLabelKind) {
    			results = LsThing.findLsThingByLabelKindAndLabelText(thingType, thingKind, labelKind, labelText).getResultList();
    		} else results = LsThing.findLsThingByLabelText(thingType, thingKind, labelText).getResultList();
    	}
    	else if (withLabelType) {
    		if (withLabelKind){
    			results = LsThing.findLsThingByLabelTypeAndKind(thingType, thingKind, labelType, labelKind).getResultList();
    		} else results = LsThing.findLsThingByLabelType(thingType, thingKind, labelType).getResultList();
    	}
    	else if (withLabelKind) {
    		results = LsThing.findLsThingByLabelKind(thingType, thingKind, labelKind).getResultList();
    	}
    	else {
    		results = LsThing.findLsThing(thingType, thingKind).getResultList();
    		
    	}
    	logger.info("query labelText: " + labelText + " number of results: " + results.size());
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Content-Type", "application/json; charset=utf-8");
    	if (with != null) {
			if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(LsThing.toJsonArray(results), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(LsThing.toJsonArrayPretty(results), headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(LsThing.toJsonArrayStub(results), headers, HttpStatus.OK);
			}
		}
    	return new ResponseEntity<String>(LsThing.toJsonArray(results), headers, HttpStatus.OK);
    }

	
}
