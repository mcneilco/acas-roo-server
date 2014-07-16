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

import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.service.ItxSubjectContainerService;
import com.labsynch.labseer.utils.PropertiesUtilService;

import flexjson.JSONTokener;

@RooWebJson(jsonObject = ItxSubjectContainer.class)
@Controller
@RequestMapping("/itxsubjectcontainers")
@RooWebScaffold(path = "itxsubjectcontainers", formBackingObject = ItxSubjectContainer.class)
@RooWebFinder
public class ItxSubjectContainerController {

    @Autowired
    private ItxSubjectContainerService itxSubjectContainerService;

    private static final Logger logger = LoggerFactory.getLogger(ItxSubjectContainerController.class);

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

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ItxSubjectContainer> result = ItxSubjectContainer.findAllItxSubjectContainers();
        return new ResponseEntity<String>(ItxSubjectContainer.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        ItxSubjectContainer itxSubjectContainer = ItxSubjectContainer.fromJsonToItxSubjectContainer(json);
        itxSubjectContainer.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(itxSubjectContainer.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArrayParseOld", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArrayParse(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ItxSubjectContainer> savedItxSubjectContainers = new ArrayList<ItxSubjectContainer>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        try {
            JSONTokener jsonTokens = new JSONTokener(json);
            Object token;
            char delimiter;
            char END_OF_ARRAY = ']';
            while (jsonTokens.more()) {
                delimiter = jsonTokens.nextClean();
                logger.debug("delimiter is : " + delimiter);
                if (delimiter != END_OF_ARRAY) {
                    token = jsonTokens.nextValue();
                    ItxSubjectContainer itxSubjectContainer = ItxSubjectContainer.fromJsonToItxSubjectContainer(token.toString());
                    itxSubjectContainer.persist();
                    savedItxSubjectContainers.add(itxSubjectContainer);
                    if (i % batchSize == 0) {
                        itxSubjectContainer.flush();
                        itxSubjectContainer.clear();
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
        return new ResponseEntity<String>(ItxSubjectContainer.toJsonArray(savedItxSubjectContainers), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        Collection<ItxSubjectContainer> savedItxSubjectContainers = new ArrayList<ItxSubjectContainer>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        StringReader sr = new StringReader(json);
        BufferedReader br = new BufferedReader(sr);
        for (ItxSubjectContainer itxSubjectContainer : ItxSubjectContainer.fromJsonArrayToItxSubjectContainers(br)) {
            savedItxSubjectContainers.add(itxSubjectContainerService.saveLsItxSubjectContainer(itxSubjectContainer));
            if (i % batchSize == 0) {
                itxSubjectContainer.flush();
                itxSubjectContainer.clear();
            }
            i++;
        }
        IOUtils.closeQuietly(sr);
        IOUtils.closeQuietly(br);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ItxSubjectContainer.toJsonArray(savedItxSubjectContainers), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ItxSubjectContainer itxSubjectContainer = ItxSubjectContainer.fromJsonToItxSubjectContainer(json);
        if (itxSubjectContainer.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(itxSubjectContainer.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ItxSubjectContainer> itxSubjectContainers = ItxSubjectContainer.fromJsonArrayToItxSubjectContainers(json);
        for (ItxSubjectContainer itxSubjectContainer : itxSubjectContainers) {
            if (itxSubjectContainer.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(ItxSubjectContainer.toJsonArray(itxSubjectContainers), headers, HttpStatus.OK);
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
