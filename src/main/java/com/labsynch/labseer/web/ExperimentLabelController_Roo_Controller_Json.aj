// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.web.ExperimentLabelController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect ExperimentLabelController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ExperimentLabelController.showJson(@PathVariable("id") Long id) {
        ExperimentLabel experimentLabel = ExperimentLabel.findExperimentLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (experimentLabel == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(experimentLabel.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ExperimentLabelController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ExperimentLabel> result = ExperimentLabel.findAllExperimentLabels();
        return new ResponseEntity<String>(ExperimentLabel.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> ExperimentLabelController.createFromJson(@RequestBody String json) {
        ExperimentLabel experimentLabel = ExperimentLabel.fromJsonToExperimentLabel(json);
        experimentLabel.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> ExperimentLabelController.createFromJsonArray(@RequestBody String json) {
        for (ExperimentLabel experimentLabel: ExperimentLabel.fromJsonArrayToExperimentLabels(json)) {
            experimentLabel.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> ExperimentLabelController.updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ExperimentLabel experimentLabel = ExperimentLabel.fromJsonToExperimentLabel(json);
        if (experimentLabel.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> ExperimentLabelController.updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (ExperimentLabel experimentLabel: ExperimentLabel.fromJsonArrayToExperimentLabels(json)) {
            if (experimentLabel.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> ExperimentLabelController.deleteFromJson(@PathVariable("id") Long id) {
        ExperimentLabel experimentLabel = ExperimentLabel.findExperimentLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (experimentLabel == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        experimentLabel.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByExperiment", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ExperimentLabelController.jsonFindExperimentLabelsByExperiment(@RequestParam("experiment") Experiment experiment) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ExperimentLabel.toJsonArray(ExperimentLabel.findExperimentLabelsByExperiment(experiment).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByExperimentAndIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ExperimentLabelController.jsonFindExperimentLabelsByExperimentAndIgnoredNot(@RequestParam("experiment") Experiment experiment, @RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ExperimentLabel.toJsonArray(ExperimentLabel.findExperimentLabelsByExperimentAndIgnoredNot(experiment, ignored).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByLabelTextLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ExperimentLabelController.jsonFindExperimentLabelsByLabelTextLike(@RequestParam("labelText") String labelText) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ExperimentLabel.toJsonArray(ExperimentLabel.findExperimentLabelsByLabelTextLike(labelText).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ExperimentLabelController.jsonFindExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(@RequestParam("labelText") String labelText, @RequestParam("lsTypeAndKind") String lsTypeAndKind, @RequestParam(value = "preferred", required = false) boolean preferred, @RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ExperimentLabel.toJsonArray(ExperimentLabel.findExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(labelText, lsTypeAndKind, preferred, ignored).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ExperimentLabelController.jsonFindExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(@RequestParam("lsTypeAndKind") String lsTypeAndKind, @RequestParam(value = "preferred", required = false) boolean preferred, @RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ExperimentLabel.toJsonArray(ExperimentLabel.findExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(lsTypeAndKind, preferred, ignored).getResultList()), headers, HttpStatus.OK);
    }
    
}