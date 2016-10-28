package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.TreatmentGroupLabel;
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

@RequestMapping("/treatmentgrouplabels")
@Controller
@RooWebScaffold(path = "treatmentgrouplabels", formBackingObject = TreatmentGroupLabel.class)
@RooWebJson(jsonObject = TreatmentGroupLabel.class)
@RooWebFinder
public class TreatmentGroupLabelController {

//    @Autowired
//    private PropertiesUtilService propertiesUtilService;
//
//    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
//        TreatmentGroupLabel treatmentGroupLabel = TreatmentGroupLabel.findTreatmentGroupLabel(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (treatmentGroupLabel == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(treatmentGroupLabel.toJson(), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<TreatmentGroupLabel> result = TreatmentGroupLabel.findAllTreatmentGroupLabels();
//        return new ResponseEntity<String>(TreatmentGroupLabel.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
//        TreatmentGroupLabel treatmentGroupLabel = TreatmentGroupLabel.fromJsonToTreatmentGroupLabel(json);
//        treatmentGroupLabel.persist();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(treatmentGroupLabel.toJson(), headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
//        Collection<TreatmentGroupLabel> savedTreatmentGroupLabels = new ArrayList<TreatmentGroupLabel>();
//        int batchSize = propertiesUtilService.getBatchSize();
//        int i = 0;
//        StringReader sr = new StringReader(json);
//        BufferedReader br = new BufferedReader(sr);
//        for (TreatmentGroupLabel treatmentGroupLabel : TreatmentGroupLabel.fromJsonArrayToTreatmentGroupLabels(br)) {
//            treatmentGroupLabel.persist();
//            savedTreatmentGroupLabels.add(treatmentGroupLabel);
//            if (i % batchSize == 0) {
//                treatmentGroupLabel.flush();
//                treatmentGroupLabel.clear();
//            }
//            i++;
//        }
//        IOUtils.closeQuietly(sr);
//        IOUtils.closeQuietly(br);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(TreatmentGroupLabel.toJsonArray(savedTreatmentGroupLabels), headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        TreatmentGroupLabel treatmentGroupLabel = TreatmentGroupLabel.fromJsonToTreatmentGroupLabel(json);
//        if (treatmentGroupLabel.merge() == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        for (TreatmentGroupLabel treatmentGroupLabel : TreatmentGroupLabel.fromJsonArrayToTreatmentGroupLabels(json)) {
//            if (treatmentGroupLabel.merge() == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
//        TreatmentGroupLabel treatmentGroupLabel = TreatmentGroupLabel.findTreatmentGroupLabel(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (treatmentGroupLabel == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        treatmentGroupLabel.remove();
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
}
