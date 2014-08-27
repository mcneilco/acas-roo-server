package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.service.TreatmentGroupService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import flexjson.JSONTokener;
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
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = TreatmentGroup.class)
@Controller
@RequestMapping("/treatmentgroups")
@RooWebScaffold(path = "treatmentgroups", formBackingObject = TreatmentGroup.class)
@RooWebFinder
@Transactional
public class TreatmentGroupController {

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

    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<TreatmentGroup> savedTreatmentGroups = new ArrayList<TreatmentGroup>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        BufferedReader br = null;
        StringReader sr = null;
        try {
            logger.debug("incoming json: " + json);
            sr = new StringReader(json);
            br = new BufferedReader(sr);
            for (TreatmentGroup treatmentGroup : TreatmentGroup.fromJsonArrayToTreatmentGroups(br)) {
                TreatmentGroup saved = treatmentGroupService.saveLsTreatmentGroup(treatmentGroup);
                savedTreatmentGroups.add(saved);
                if (i % batchSize == 0) {
                    saved.flush();
                    saved.clear();
                }
                i++;
            }
        } catch (Exception e) {
            logger.error("ERROR: " + e);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        } finally {
            IOUtils.closeQuietly(sr);
            IOUtils.closeQuietly(br);
        }
        return new ResponseEntity<String>(TreatmentGroup.toJsonArray(savedTreatmentGroups), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArrayParseOld", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArrayParse(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<TreatmentGroup> savedTreatmentGroups = new ArrayList<TreatmentGroup>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        try {
            JSONTokener jsonTokens = new JSONTokener(json);
            Object token;
            char delimiter;
            char END_OF_ARRAY = ']';
            while (jsonTokens.more()) {
                delimiter = jsonTokens.nextClean();
                if (delimiter != END_OF_ARRAY) {
                    token = jsonTokens.nextValue();
                    TreatmentGroup treatmentGroup = TreatmentGroup.fromJsonToTreatmentGroup(token.toString());
                    treatmentGroup.persist();
                    savedTreatmentGroups.add(treatmentGroup);
                    if (i % batchSize == 0) {
                        treatmentGroup.flush();
                        treatmentGroup.clear();
                    }
                    i++;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("ERROR: " + e);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(TreatmentGroup.toJsonArray(savedTreatmentGroups), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        TreatmentGroup treatmentGroup = TreatmentGroup.fromJsonToTreatmentGroup(json);
        if (treatmentGroup.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (TreatmentGroup treatmentGroup : TreatmentGroup.fromJsonArrayToTreatmentGroups(json)) {
            if (treatmentGroup.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (treatmentGroup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        treatmentGroup.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}
