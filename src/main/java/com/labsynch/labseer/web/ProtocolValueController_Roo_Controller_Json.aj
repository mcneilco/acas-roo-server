// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.web.ProtocolValueController;
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

privileged aspect ProtocolValueController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ProtocolValueController.showJson(@PathVariable("id") Long id) {
        ProtocolValue protocolValue = ProtocolValue.findProtocolValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (protocolValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(protocolValue.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ProtocolValueController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ProtocolValue> result = ProtocolValue.findAllProtocolValues();
        return new ResponseEntity<String>(ProtocolValue.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> ProtocolValueController.createFromJson(@RequestBody String json) {
        ProtocolValue protocolValue = ProtocolValue.fromJsonToProtocolValue(json);
        protocolValue.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> ProtocolValueController.createFromJsonArray(@RequestBody String json) {
        for (ProtocolValue protocolValue: ProtocolValue.fromJsonArrayToProtocolValues(json)) {
            protocolValue.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> ProtocolValueController.updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ProtocolValue protocolValue = ProtocolValue.fromJsonToProtocolValue(json);
        if (protocolValue.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> ProtocolValueController.updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (ProtocolValue protocolValue: ProtocolValue.fromJsonArrayToProtocolValues(json)) {
            if (protocolValue.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> ProtocolValueController.deleteFromJson(@PathVariable("id") Long id) {
        ProtocolValue protocolValue = ProtocolValue.findProtocolValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (protocolValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        protocolValue.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByLsKindEqualsAndCodeValueLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ProtocolValueController.jsonFindProtocolValuesByLsKindEqualsAndCodeValueLike(@RequestParam("lsKind") String lsKind, @RequestParam("codeValue") String codeValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ProtocolValue.toJsonArray(ProtocolValue.findProtocolValuesByLsKindEqualsAndCodeValueLike(lsKind, codeValue).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByLsKindEqualsAndStringValueLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ProtocolValueController.jsonFindProtocolValuesByLsKindEqualsAndStringValueLike(@RequestParam("lsKind") String lsKind, @RequestParam("stringValue") String stringValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ProtocolValue.toJsonArray(ProtocolValue.findProtocolValuesByLsKindEqualsAndStringValueLike(lsKind, stringValue).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByLsState", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ProtocolValueController.jsonFindProtocolValuesByLsState(@RequestParam("lsState") ProtocolState lsState) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ProtocolValue.toJsonArray(ProtocolValue.findProtocolValuesByLsState(lsState).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> ProtocolValueController.jsonFindProtocolValuesByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ProtocolValue.toJsonArray(ProtocolValue.findProtocolValuesByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
    }
    
}