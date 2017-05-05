// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.InteractionType;
import com.labsynch.labseer.web.InteractionTypeController;
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

privileged aspect InteractionTypeController_Roo_Controller_Json {
    
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> InteractionTypeController.showJson(@PathVariable("id") Long id) {
        InteractionType interactionType = InteractionType.findInteractionType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (interactionType == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(interactionType.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> InteractionTypeController.listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<InteractionType> result = InteractionType.findAllInteractionTypes();
        return new ResponseEntity<String>(InteractionType.toJsonArray(result), headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> InteractionTypeController.createFromJson(@RequestBody String json) {
        InteractionType interactionType = InteractionType.fromJsonToInteractionType(json);
        interactionType.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> InteractionTypeController.createFromJsonArray(@RequestBody String json) {
        for (InteractionType interactionType: InteractionType.fromJsonArrayToInteractionTypes(json)) {
            interactionType.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> InteractionTypeController.updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        InteractionType interactionType = InteractionType.fromJsonToInteractionType(json);
        if (interactionType.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> InteractionTypeController.updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (InteractionType interactionType: InteractionType.fromJsonArrayToInteractionTypes(json)) {
            if (interactionType.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> InteractionTypeController.deleteFromJson(@PathVariable("id") Long id) {
        InteractionType interactionType = InteractionType.findInteractionType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (interactionType == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        interactionType.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByTypeNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> InteractionTypeController.jsonFindInteractionTypesByTypeNameEquals(@RequestParam("typeName") String typeName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(InteractionType.toJsonArray(InteractionType.findInteractionTypesByTypeNameEquals(typeName).getResultList()), headers, HttpStatus.OK);
    }
    
    @RequestMapping(params = "find=ByTypeNameEqualsAndTypeVerbEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> InteractionTypeController.jsonFindInteractionTypesByTypeNameEqualsAndTypeVerbEquals(@RequestParam("typeName") String typeName, @RequestParam("typeVerb") String typeVerb) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(InteractionType.toJsonArray(InteractionType.findInteractionTypesByTypeNameEqualsAndTypeVerbEquals(typeName, typeVerb).getResultList()), headers, HttpStatus.OK);
    }
    
}