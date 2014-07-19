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
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.ItxSubjectContainerState;
import com.labsynch.labseer.utils.PropertiesUtilService;

@RooWebJson(jsonObject = ItxSubjectContainerState.class)
@Controller
@RequestMapping("/itxsubjectcontainerstates")
@RooWebScaffold(path = "itxsubjectcontainerstates", formBackingObject = ItxSubjectContainerState.class)
public class ItxSubjectContainerStateController {

    private static final Logger logger = LoggerFactory.getLogger(ItxSubjectContainerStateController.class);

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        ItxSubjectContainerState itxSubjectContainerState = ItxSubjectContainerState.findItxSubjectContainerState(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (itxSubjectContainerState == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(itxSubjectContainerState.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ItxSubjectContainerState> result = ItxSubjectContainerState.findAllItxSubjectContainerStates();
        return new ResponseEntity<String>(ItxSubjectContainerState.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        ItxSubjectContainerState itxSubjectContainerState = ItxSubjectContainerState.fromJsonToItxSubjectContainerState(json);
        itxSubjectContainerState.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(itxSubjectContainerState.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ItxSubjectContainerState> savedItxSubjectContainerStates = new ArrayList<ItxSubjectContainerState>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        BufferedReader br = null;
        StringReader sr = null;
        try {
            sr = new StringReader(json);
            br = new BufferedReader(sr);
            for (ItxSubjectContainerState itxSubjectContainerState : ItxSubjectContainerState.fromJsonArrayToItxSubjectContainerStates(br)) {
                itxSubjectContainerState.persist();
                savedItxSubjectContainerStates.add(itxSubjectContainerState);
                if (i % batchSize == 0) {
                    itxSubjectContainerState.flush();
                    itxSubjectContainerState.clear();
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
        return new ResponseEntity<String>(ItxSubjectContainerState.toJsonArrayStub(savedItxSubjectContainerStates), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ItxSubjectContainerState itxSubjectContainerState = ItxSubjectContainerState.fromJsonToItxSubjectContainerState(json);
        if (itxSubjectContainerState.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (ItxSubjectContainerState itxSubjectContainerState : ItxSubjectContainerState.fromJsonArrayToItxSubjectContainerStates(json)) {
            if (itxSubjectContainerState.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        ItxSubjectContainerState itxSubjectContainerState = ItxSubjectContainerState.findItxSubjectContainerState(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (itxSubjectContainerState == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        itxSubjectContainerState.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}
