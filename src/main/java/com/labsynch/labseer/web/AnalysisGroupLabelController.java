package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.AnalysisGroupLabel;
import com.labsynch.labseer.utils.PropertiesUtilService;
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

@RooWebJson(jsonObject = AnalysisGroupLabel.class)
@Controller
@RequestMapping("/analysisgrouplabels")
@RooWebScaffold(path = "analysisgrouplabels", formBackingObject = AnalysisGroupLabel.class)
@RooWebFinder
public class AnalysisGroupLabelController {

//    @Autowired
//    private PropertiesUtilService propertiesUtilService;
//
//    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
//        AnalysisGroupLabel analysisGroupLabel = AnalysisGroupLabel.findAnalysisGroupLabel(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (analysisGroupLabel == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(analysisGroupLabel.toJson(), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<AnalysisGroupLabel> result = AnalysisGroupLabel.findAllAnalysisGroupLabels();
//        return new ResponseEntity<String>(AnalysisGroupLabel.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
//        AnalysisGroupLabel analysisGroupLabel = AnalysisGroupLabel.fromJsonToAnalysisGroupLabel(json);
//        analysisGroupLabel.persist();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(analysisGroupLabel.toJson(), headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
//        int batchSize = propertiesUtilService.getBatchSize();
//        int i = 0;
//        BufferedReader br = null;
//        StringReader sr = null;
//        Collection<AnalysisGroupLabel> savedAnalysisGroupLabels = new ArrayList<AnalysisGroupLabel>();
//        sr = new StringReader(json);
//        br = new BufferedReader(sr);
//        for (AnalysisGroupLabel analysisGroupLabel : AnalysisGroupLabel.fromJsonArrayToAnalysisGroupLabels(br)) {
//            analysisGroupLabel.persist();
//            savedAnalysisGroupLabels.add(analysisGroupLabel);
//            if (i % batchSize == 0) {
//                analysisGroupLabel.flush();
//                analysisGroupLabel.clear();
//            }
//            i++;
//        }
//        IOUtils.closeQuietly(sr);
//        IOUtils.closeQuietly(br);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(AnalysisGroupLabel.toJsonArray(savedAnalysisGroupLabels), headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        AnalysisGroupLabel analysisGroupLabel = AnalysisGroupLabel.fromJsonToAnalysisGroupLabel(json);
//        if (analysisGroupLabel.merge() == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(analysisGroupLabel.toJson(), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        Collection<AnalysisGroupLabel> savedAnalysisGroupLabels = new ArrayList<AnalysisGroupLabel>();
//        for (AnalysisGroupLabel analysisGroupLabel : AnalysisGroupLabel.fromJsonArrayToAnalysisGroupLabels(json)) {
//            if (analysisGroupLabel.merge() == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//            savedAnalysisGroupLabels.add(analysisGroupLabel);
//        }
//        return new ResponseEntity<String>(AnalysisGroupLabel.toJsonArray(savedAnalysisGroupLabels), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
//        AnalysisGroupLabel analysisGroupLabel = AnalysisGroupLabel.findAnalysisGroupLabel(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (analysisGroupLabel == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        analysisGroupLabel.remove();
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
}
