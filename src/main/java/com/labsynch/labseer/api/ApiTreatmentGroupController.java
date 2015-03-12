package com.labsynch.labseer.api;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.service.SubjectValueService;
import com.labsynch.labseer.service.TreatmentGroupService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.web.TreatmentGroupController;

import flexjson.JSONTokener;

@Controller
@RequestMapping("api/v1/treatmentgroups")
@Transactional
public class ApiTreatmentGroupController {
	
	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupController.class);

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @Autowired
    private TreatmentGroupService treatmentGroupService;

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (treatmentGroup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(treatmentGroup.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<TreatmentGroup> result = TreatmentGroup.findAllTreatmentGroups();
        return new ResponseEntity<String>(TreatmentGroup.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
    	TreatmentGroup treatmentGroup = TreatmentGroup.fromJsonToTreatmentGroup(json);
        TreatmentGroup savedTreatmentGroup = treatmentGroupService.saveLsTreatmentGroup(treatmentGroup);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(savedTreatmentGroup.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
    	Collection<TreatmentGroup> treatmentGroups = TreatmentGroup.fromJsonArrayToTreatmentGroups(json);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<TreatmentGroup> savedTreatmentGroups = new ArrayList<TreatmentGroup>();
        try {
            for (TreatmentGroup treatmentGroup : treatmentGroups) {
                TreatmentGroup saved = treatmentGroupService.saveLsTreatmentGroup(treatmentGroup);
                savedTreatmentGroups.add(saved);
            }
        } catch (Exception e) {
            logger.error("ERROR: " + e);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(TreatmentGroup.toJsonArray(savedTreatmentGroups), headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody TreatmentGroup treatmentGroup) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        treatmentGroup = treatmentGroupService.updateTreatmentGroup(treatmentGroup);
        if (treatmentGroup.getId() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(treatmentGroup.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody List<TreatmentGroup> treatmentGroups) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (TreatmentGroup treatmentGroup : treatmentGroups) {
            treatmentGroup = treatmentGroupService.updateTreatmentGroup(treatmentGroup);
            if (treatmentGroup.getId() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(TreatmentGroup.toJsonArray(treatmentGroups), headers, HttpStatus.OK);
    }
}
