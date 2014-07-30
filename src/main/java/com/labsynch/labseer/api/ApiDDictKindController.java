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

import com.labsynch.labseer.domain.DDictKind;

@Transactional
@RequestMapping("api/v1/ddictkinds")
@Controller
@RooWebFinder
@RooWebJson(jsonObject = DDictKind.class)

public class ApiDDictKindController {

	private static final Logger logger = LoggerFactory.getLogger(ApiDDictKindController.class);



	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        DDictKind DDictKind_ = DDictKind.findDDictKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (DDictKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(DDictKind_.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DDictKind> result = DDictKind.findAllDDictKinds();
        return new ResponseEntity<String>(DDictKind.toJsonArray(result), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        DDictKind DDictKind_ = DDictKind.fromJsonToDDictKind(json);
        DDictKind_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(DDictKind_.toJson(), headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		Collection<DDictKind> dDictKinds = new ArrayList<DDictKind>();
        for (DDictKind DDictKind_: DDictKind.fromJsonArrayToDDictKinds(json)) {
            DDictKind_.persist();
            dDictKinds.add(DDictKind_);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(DDictKind.toJsonArray(dDictKinds), headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DDictKind DDictKind_ = DDictKind.fromJsonToDDictKind(json);
        if (DDictKind_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(DDictKind_.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
		Collection<DDictKind> dDictKinds = new ArrayList<DDictKind>();
        for (DDictKind DDictKind_: DDictKind.fromJsonArrayToDDictKinds(json)) {
            if (DDictKind_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            dDictKinds.add(DDictKind_);
        }
        return new ResponseEntity<String>(DDictKind.toJsonArray(dDictKinds), headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        DDictKind DDictKind_ = DDictKind.findDDictKind(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (DDictKind_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        DDictKind_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}
