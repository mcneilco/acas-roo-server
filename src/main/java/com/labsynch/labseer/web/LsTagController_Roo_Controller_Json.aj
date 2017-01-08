// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsTag;
import com.labsynch.labseer.web.LsTagController;
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

privileged aspect LsTagController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> LsTagController.showJson(@PathVariable("id") Long id) {
        LsTag lsTag = LsTag.findLsTag(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (lsTag == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(lsTag.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> LsTagController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<LsTag> result = LsTag.findAllLsTags();
        return new ResponseEntity<String>(LsTag.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> LsTagController.createFromJson(@RequestBody String json) {
        LsTag lsTag = LsTag.fromJsonToLsTag(json);
        lsTag.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> LsTagController.createFromJsonArray(@RequestBody String json) {
        for (LsTag lsTag: LsTag.fromJsonArrayToLsTags(json)) {
            lsTag.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> LsTagController.updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        LsTag lsTag = LsTag.fromJsonToLsTag(json);
        if (lsTag.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> LsTagController.updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (LsTag lsTag: LsTag.fromJsonArrayToLsTags(json)) {
            if (lsTag.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> LsTagController.deleteFromJson(@PathVariable("id") Long id) {
        LsTag lsTag = LsTag.findLsTag(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (lsTag == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        lsTag.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByTagTextEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> LsTagController.jsonFindLsTagsByTagTextEquals(@RequestParam("tagText") String tagText) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(LsTag.toJsonArray(LsTag.findLsTagsByTagTextEquals(tagText).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByTagTextLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> LsTagController.jsonFindLsTagsByTagTextLike(@RequestParam("tagText") String tagText) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(LsTag.toJsonArray(LsTag.findLsTagsByTagTextLike(tagText).getResultList()), headers, HttpStatus.OK);
    }
    
}