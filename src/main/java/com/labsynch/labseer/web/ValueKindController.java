package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ValueKind;
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

@RooWebJson(jsonObject = ValueKind.class)
@Controller
@RequestMapping("/valuekinds")
@RooWebScaffold(path = "valuekinds", formBackingObject = ValueKind.class)
@RooWebFinder
public class ValueKindController {

//    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
//        ValueKind valueKind = ValueKind.findValueKind(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (valueKind == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(valueKind.toJson(), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<ValueKind> result = ValueKind.findAllValueKinds();
//        return new ResponseEntity<String>(ValueKind.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
//        ValueKind valueKind = ValueKind.fromJsonToValueKind(json);
//        valueKind.persist();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
//        for (ValueKind valueKind : ValueKind.fromJsonArrayToValueKinds(json)) {
//            valueKind.persist();
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
//        ValueKind valueKind = ValueKind.fromJsonToValueKind(json);
//        if (valueKind.merge() == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        for (ValueKind valueKind : ValueKind.fromJsonArrayToValueKinds(json)) {
//            if (valueKind.merge() == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
//        ValueKind valueKind = ValueKind.findValueKind(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (valueKind == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        valueKind.remove();
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
}
