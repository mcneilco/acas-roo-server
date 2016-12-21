package com.labsynch.labseer.api;

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
        itxSubjectContainer.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(itxSubjectContainer.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody List<ItxSubjectContainer> itxSubjectContainers) {
        Collection<ItxSubjectContainer> savedItxSubjectContainers = new ArrayList<ItxSubjectContainer>();
        for (ItxSubjectContainer itxSubjectContainer : itxSubjectContainers) {
            savedItxSubjectContainers.add(itxSubjectContainerService.saveLsItxSubjectContainer(itxSubjectContainer));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ItxSubjectContainer.toJsonArray(savedItxSubjectContainers), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody ItxSubjectContainer itxSubjectContainer) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (itxSubjectContainer.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(itxSubjectContainer.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody List<ItxSubjectContainer> itxSubjectContainers) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
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
