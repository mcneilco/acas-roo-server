// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxProtocolProtocolState;
import com.labsynch.labseer.web.ItxProtocolProtocolStateController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect ItxProtocolProtocolStateController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ItxProtocolProtocolStateController.showJson(@PathVariable("id") Long id) {
        ItxProtocolProtocolState itxProtocolProtocolState = ItxProtocolProtocolState.findItxProtocolProtocolState(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (itxProtocolProtocolState == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(itxProtocolProtocolState.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ItxProtocolProtocolStateController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ItxProtocolProtocolState> result = ItxProtocolProtocolState.findAllItxProtocolProtocolStates();
        return new ResponseEntity<String>(ItxProtocolProtocolState.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> ItxProtocolProtocolStateController.createFromJson(@RequestBody String json) {
        ItxProtocolProtocolState itxProtocolProtocolState = ItxProtocolProtocolState.fromJsonToItxProtocolProtocolState(json);
        itxProtocolProtocolState.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> ItxProtocolProtocolStateController.createFromJsonArray(@RequestBody String json) {
        for (ItxProtocolProtocolState itxProtocolProtocolState: ItxProtocolProtocolState.fromJsonArrayToItxProtocolProtocolStates(json)) {
            itxProtocolProtocolState.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> ItxProtocolProtocolStateController.updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ItxProtocolProtocolState itxProtocolProtocolState = ItxProtocolProtocolState.fromJsonToItxProtocolProtocolState(json);
        if (itxProtocolProtocolState.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> ItxProtocolProtocolStateController.updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (ItxProtocolProtocolState itxProtocolProtocolState: ItxProtocolProtocolState.fromJsonArrayToItxProtocolProtocolStates(json)) {
            if (itxProtocolProtocolState.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> ItxProtocolProtocolStateController.deleteFromJson(@PathVariable("id") Long id) {
        ItxProtocolProtocolState itxProtocolProtocolState = ItxProtocolProtocolState.findItxProtocolProtocolState(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (itxProtocolProtocolState == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        itxProtocolProtocolState.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}