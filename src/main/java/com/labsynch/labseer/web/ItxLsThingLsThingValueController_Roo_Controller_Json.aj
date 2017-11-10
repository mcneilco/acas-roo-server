// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.web.ItxLsThingLsThingValueController;
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

privileged aspect ItxLsThingLsThingValueController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ItxLsThingLsThingValueController.showJson(@PathVariable("id") Long id) {
        ItxLsThingLsThingValue itxLsThingLsThingValue = ItxLsThingLsThingValue.findItxLsThingLsThingValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (itxLsThingLsThingValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(itxLsThingLsThingValue.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ItxLsThingLsThingValueController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ItxLsThingLsThingValue> result = ItxLsThingLsThingValue.findAllItxLsThingLsThingValues();
        return new ResponseEntity<String>(ItxLsThingLsThingValue.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> ItxLsThingLsThingValueController.createFromJson(@RequestBody String json) {
        ItxLsThingLsThingValue itxLsThingLsThingValue = ItxLsThingLsThingValue.fromJsonToItxLsThingLsThingValue(json);
        itxLsThingLsThingValue.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> ItxLsThingLsThingValueController.createFromJsonArray(@RequestBody String json) {
        for (ItxLsThingLsThingValue itxLsThingLsThingValue: ItxLsThingLsThingValue.fromJsonArrayToItxLsThingLsThingValues(json)) {
            itxLsThingLsThingValue.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> ItxLsThingLsThingValueController.updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ItxLsThingLsThingValue itxLsThingLsThingValue = ItxLsThingLsThingValue.fromJsonToItxLsThingLsThingValue(json);
        if (itxLsThingLsThingValue.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> ItxLsThingLsThingValueController.updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (ItxLsThingLsThingValue itxLsThingLsThingValue: ItxLsThingLsThingValue.fromJsonArrayToItxLsThingLsThingValues(json)) {
            if (itxLsThingLsThingValue.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> ItxLsThingLsThingValueController.deleteFromJson(@PathVariable("id") Long id) {
        ItxLsThingLsThingValue itxLsThingLsThingValue = ItxLsThingLsThingValue.findItxLsThingLsThingValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (itxLsThingLsThingValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        itxLsThingLsThingValue.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ItxLsThingLsThingValueController.jsonFindItxLsThingLsThingValuesByLsKindEquals(@RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ItxLsThingLsThingValue.toJsonArray(ItxLsThingLsThingValue.findItxLsThingLsThingValuesByLsKindEquals(lsKind).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ItxLsThingLsThingValueController.jsonFindItxLsThingLsThingValuesByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ItxLsThingLsThingValue.toJsonArray(ItxLsThingLsThingValue.findItxLsThingLsThingValuesByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
    }
    
}
