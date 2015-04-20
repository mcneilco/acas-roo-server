package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.UnitType;
import java.util.List;
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

@RooWebJson(jsonObject = UnitType.class)
@Controller
@RequestMapping("/unittypes")
@RooWebScaffold(path = "unittypes", formBackingObject = UnitType.class)
@RooWebFinder
public class UnitTypeController {

//    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
//        UnitType unitType = UnitType.findUnitType(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (unitType == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(unitType.toJson(), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<UnitType> result = UnitType.findAllUnitTypes();
//        return new ResponseEntity<String>(UnitType.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
//        UnitType unitType = UnitType.fromJsonToUnitType(json);
//        unitType.persist();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
//        for (UnitType unitType : UnitType.fromJsonArrayToUnitTypes(json)) {
//            unitType.persist();
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        UnitType unitType = UnitType.fromJsonToUnitType(json);
//        if (unitType.merge() == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        for (UnitType unitType : UnitType.fromJsonArrayToUnitTypes(json)) {
//            if (unitType.merge() == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
//        UnitType unitType = UnitType.findUnitType(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (unitType == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        unitType.remove();
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
}
