package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.DDictType;

@Transactional
@RequestMapping("api/v1/ddicttypes")
@Controller
//@RooWebFinder
//@RooWebJson(jsonObject = DDictType.class)

public class ApiDDictTypeController {

	private static final Logger logger = LoggerFactory.getLogger(ApiDDictTypeController.class);



	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        DDictType DDictType_ = DDictType.findDDictType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (DDictType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(DDictType_.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DDictType> result = DDictType.findAllDDictTypes();
        return new ResponseEntity<String>(DDictType.toJsonArray(result), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        DDictType DDictType_ = DDictType.fromJsonToDDictType(json);
        DDictType_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(DDictType_.toJson(), headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		Collection<DDictType> dDictTypes = new ArrayList<DDictType>();
        for (DDictType DDictType_: DDictType.fromJsonArrayToDDictTypes(json)) {
            DDictType_.persist();
            dDictTypes.add(DDictType_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(DDictType.toJsonArray(dDictTypes), headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DDictType DDictType_ = DDictType.fromJsonToDDictType(json);
        if (DDictType_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(DDictType_.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
		Collection<DDictType> dDictTypes = new ArrayList<DDictType>();
        for (DDictType DDictType_: DDictType.fromJsonArrayToDDictTypes(json)) {
            if (DDictType_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            dDictTypes.add(DDictType_);
        }
        return new ResponseEntity<String>(DDictType.toJsonArray(dDictTypes), headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        DDictType DDictType_ = DDictType.findDDictType(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (DDictType_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        DDictType_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}
