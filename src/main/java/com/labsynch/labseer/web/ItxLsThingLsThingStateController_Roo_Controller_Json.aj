// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.web.ItxLsThingLsThingStateController;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

privileged aspect ItxLsThingLsThingStateController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ItxLsThingLsThingStateController.showJson(@PathVariable("id") Long id) {
        ItxLsThingLsThingState itxLsThingLsThingState = ItxLsThingLsThingState.findItxLsThingLsThingState(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (itxLsThingLsThingState == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(itxLsThingLsThingState.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ItxLsThingLsThingStateController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ItxLsThingLsThingState> result = ItxLsThingLsThingState.findAllItxLsThingLsThingStates();
        return new ResponseEntity<String>(ItxLsThingLsThingState.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> ItxLsThingLsThingStateController.createFromJson(@RequestBody String json) {
        ItxLsThingLsThingState itxLsThingLsThingState = ItxLsThingLsThingState.fromJsonToItxLsThingLsThingState(json);
        itxLsThingLsThingState.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> ItxLsThingLsThingStateController.createFromJsonArray(@RequestBody String json) {
        for (ItxLsThingLsThingState itxLsThingLsThingState: ItxLsThingLsThingState.fromJsonArrayToItxLsThingLsThingStates(json)) {
            itxLsThingLsThingState.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> ItxLsThingLsThingStateController.updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ItxLsThingLsThingState itxLsThingLsThingState = ItxLsThingLsThingState.fromJsonToItxLsThingLsThingState(json);
        if (itxLsThingLsThingState.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> ItxLsThingLsThingStateController.updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (ItxLsThingLsThingState itxLsThingLsThingState: ItxLsThingLsThingState.fromJsonArrayToItxLsThingLsThingStates(json)) {
            if (itxLsThingLsThingState.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> ItxLsThingLsThingStateController.deleteFromJson(@PathVariable("id") Long id) {
        ItxLsThingLsThingState itxLsThingLsThingState = ItxLsThingLsThingState.findItxLsThingLsThingState(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (itxLsThingLsThingState == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        itxLsThingLsThingState.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}