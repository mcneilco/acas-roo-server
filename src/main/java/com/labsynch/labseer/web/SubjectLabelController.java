package com.labsynch.labseer.web;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
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

import com.labsynch.labseer.domain.SubjectLabel;
import com.labsynch.labseer.utils.PropertiesUtilService;

@RequestMapping("/subjectlabels")
@Controller
@RooWebScaffold(path = "subjectlabels", formBackingObject = SubjectLabel.class)
@RooWebJson(jsonObject = SubjectLabel.class)
@RooWebFinder
public class SubjectLabelController {

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        SubjectLabel subjectLabel = SubjectLabel.findSubjectLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (subjectLabel == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(subjectLabel.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<SubjectLabel> result = SubjectLabel.findAllSubjectLabels();
        return new ResponseEntity<String>(SubjectLabel.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        SubjectLabel subjectLabel = SubjectLabel.fromJsonToSubjectLabel(json);
        subjectLabel.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(subjectLabel.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        Collection<SubjectLabel> savedSubjectLabels = new ArrayList<SubjectLabel>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        BufferedReader br = null;
        StringReader sr = null;
        sr = new StringReader(json);
        br = new BufferedReader(sr);
        for (SubjectLabel subjectLabel : SubjectLabel.fromJsonArrayToSubjectLabels(br)) {
            subjectLabel.persist();
            savedSubjectLabels.add(subjectLabel);
            if (i % batchSize == 0) {
                subjectLabel.flush();
                subjectLabel.clear();
            }
            i++;
        }
        IOUtils.closeQuietly(sr);
        IOUtils.closeQuietly(br);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(SubjectLabel.toJsonArray(savedSubjectLabels), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        SubjectLabel subjectLabel = SubjectLabel.fromJsonToSubjectLabel(json);
        if (subjectLabel.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (SubjectLabel subjectLabel : SubjectLabel.fromJsonArrayToSubjectLabels(json)) {
            if (subjectLabel.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        SubjectLabel subjectLabel = SubjectLabel.findSubjectLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (subjectLabel == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        subjectLabel.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}
