package com.labsynch.labseer.web;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.utils.PropertiesUtilService;

import flexjson.JSONTokener;

@RooWebJson(jsonObject = TreatmentGroupState.class)
@Controller
@RequestMapping("/treatmentgroupstates")
@RooWebScaffold(path = "treatmentgroupstates", formBackingObject = TreatmentGroupState.class)
@RooWebFinder
public class TreatmentGroupStateController {

    private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupStateController.class);

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        TreatmentGroupState treatmentGroupState = TreatmentGroupState.findTreatmentGroupState(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (treatmentGroupState == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(treatmentGroupState.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<TreatmentGroupState> result = TreatmentGroupState.findAllTreatmentGroupStates();
        return new ResponseEntity<String>(TreatmentGroupState.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        TreatmentGroupState treatmentGroupState = TreatmentGroupState.fromJsonToTreatmentGroupState(json);
        treatmentGroupState.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(treatmentGroupState.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArrayParseOld", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArrayParse(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<TreatmentGroupState> savedTreatmentGroupStates = new ArrayList<TreatmentGroupState>();
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
                    TreatmentGroupState treatmentGroupState = TreatmentGroupState.fromJsonToTreatmentGroupState(token.toString());
                    treatmentGroupState.persist();
                    savedTreatmentGroupStates.add(treatmentGroupState);
                    if (i % batchSize == 0) {
                        treatmentGroupState.flush();
                        treatmentGroupState.clear();
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
        return new ResponseEntity<String>(TreatmentGroupState.toJsonArrayStub(savedTreatmentGroupStates), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<TreatmentGroupState> savedTreatmentGroupStates = new ArrayList<TreatmentGroupState>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        BufferedReader br = null;
        StringReader sr = null;
        try {
            sr = new StringReader(json);
            br = new BufferedReader(sr);
            for (TreatmentGroupState treatmentGroupState : TreatmentGroupState.fromJsonArrayToTreatmentGroupStates(br)) {
                treatmentGroupState.persist();
                savedTreatmentGroupStates.add(treatmentGroupState);
                if (i % batchSize == 0) {
                    treatmentGroupState.flush();
                    treatmentGroupState.clear();
                    logger.debug("flushing the treatmentgroup state batch: " + i);
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
        return new ResponseEntity<String>(TreatmentGroupState.toJsonArrayStub(savedTreatmentGroupStates), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        TreatmentGroupState treatmentGroupState = TreatmentGroupState.fromJsonToTreatmentGroupState(json);
        if (treatmentGroupState.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (TreatmentGroupState treatmentGroupState : TreatmentGroupState.fromJsonArrayToTreatmentGroupStates(json)) {
            if (treatmentGroupState.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        TreatmentGroupState treatmentGroupState = TreatmentGroupState.findTreatmentGroupState(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (treatmentGroupState == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        treatmentGroupState.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}
