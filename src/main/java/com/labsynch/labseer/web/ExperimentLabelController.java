package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.utils.PropertiesUtilService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = ExperimentLabel.class)
@Controller
@RequestMapping("/experimentlabels")
@RooWebScaffold(path = "experimentlabels", formBackingObject = ExperimentLabel.class)
@RooWebFinder
public class ExperimentLabelController {

//    @Autowired
//    private PropertiesUtilService propertiesUtilService;
//
//    private static final Logger logger = LoggerFactory.getLogger(ExperimentLabelController.class);
//
//    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
//        ExperimentLabel experimentLabel = ExperimentLabel.findExperimentLabel(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (experimentLabel == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(experimentLabel.toJson(), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<ExperimentLabel> result = ExperimentLabel.findAllExperimentLabels();
//        return new ResponseEntity<String>(ExperimentLabel.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
//        ExperimentLabel experimentLabel = ExperimentLabel.fromJsonToExperimentLabel(json);
//        experimentLabel.persist();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(experimentLabel.toJson(), headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
//        Collection<ExperimentLabel> savedExperimentLabels = new ArrayList<ExperimentLabel>();
//        int batchSize = propertiesUtilService.getBatchSize();
//        int i = 0;
//        StringReader sr = new StringReader(json);
//        BufferedReader br = new BufferedReader(sr);
//        for (ExperimentLabel experimentLabel : ExperimentLabel.fromJsonArrayToExperimentLabels(br)) {
//            experimentLabel.persist();
//            savedExperimentLabels.add(experimentLabel);
//            if (i % batchSize == 0) {
//                experimentLabel.flush();
//                experimentLabel.clear();
//            }
//            i++;
//        }
//        IOUtils.closeQuietly(sr);
//        IOUtils.closeQuietly(br);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(ExperimentLabel.toJsonArray(savedExperimentLabels), headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        ExperimentLabel experimentLabel = ExperimentLabel.fromJsonToExperimentLabel(json);
//        if (experimentLabel.merge() == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        for (ExperimentLabel experimentLabel : ExperimentLabel.fromJsonArrayToExperimentLabels(json)) {
//            if (experimentLabel.merge() == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
//        ExperimentLabel experimentLabel = ExperimentLabel.findExperimentLabel(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (experimentLabel == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        experimentLabel.remove();
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(params = "find=ByLabelTextLikeAndLabelTypeAndKindEqualsAndPreferredNotAndIgnoredNot", method = RequestMethod.GET, headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> jsonFindExperimentLabelsByLabelTextLikeAndLabelTypeAndKindEqualsAndPreferredNotAndIgnoredNot(@RequestParam("labelText") String labelText, @RequestParam("labelTypeAndKind") String labelTypeAndKind, @RequestParam(value = "preferred", required = false) boolean preferred, @RequestParam(value = "ignored", required = false) boolean ignored) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>(ExperimentLabel.toJsonArray(ExperimentLabel.findExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(labelText, labelTypeAndKind, preferred, ignored).getResultList()), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(params = "FindByName", method = RequestMethod.GET, headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> jsonFindExperimentLabelByNameGet(@RequestParam("name") String name, @RequestParam(value = "protocolId", required = false) Long protocolId) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<ExperimentLabel> experimentLabels;
//        if (protocolId != null && protocolId != 0) {
//            experimentLabels = ExperimentLabel.findExperimentLabelsByNameAndProtocol(name, protocolId).getResultList();
//        } else {
//            experimentLabels = ExperimentLabel.findExperimentLabelsByName(name).getResultList();
//        }
//        return new ResponseEntity<String>(ExperimentLabel.toJsonArrayStub(experimentLabels), headers, HttpStatus.OK);
//    }
}
