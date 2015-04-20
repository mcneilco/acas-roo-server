package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.UnitKind;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = UnitKind.class)
@Controller
@RequestMapping("/unitkinds")
@RooWebScaffold(path = "unitkinds", formBackingObject = UnitKind.class)
public class UnitKindController {

//    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
//        UnitKind unitKind = UnitKind.findUnitKind(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (unitKind == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(unitKind.toJson(), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<UnitKind> result = UnitKind.findAllUnitKinds();
//        return new ResponseEntity<String>(UnitKind.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
//        UnitKind unitKind = UnitKind.fromJsonToUnitKind(json);
//        unitKind.persist();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
//        for (UnitKind unitKind : UnitKind.fromJsonArrayToUnitKinds(json)) {
//            unitKind.persist();
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
//        UnitKind unitKind = UnitKind.fromJsonToUnitKind(json);
//        if (unitKind.merge() == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        for (UnitKind unitKind : UnitKind.fromJsonArrayToUnitKinds(json)) {
//            if (unitKind.merge() == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
//        UnitKind unitKind = UnitKind.findUnitKind(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (unitKind == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        unitKind.remove();
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
}
