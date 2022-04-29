package com.labsynch.labseer.api;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.labsynch.labseer.domain.PhysicalState;

import com.labsynch.labseer.utils.PropertiesUtilService;
import org.springframework.beans.factory.annotation.Autowired;

@RequestMapping(value = {"/api/v1/physicalStates"})
@Transactional
@Controller
public class ApiPhysicalStateController {
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        PhysicalState physicalstate = PhysicalState.findPhysicalState(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        if (physicalstate == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(physicalstate.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        
		if (propertiesUtilService.getOrderSelectLists()){
	        return new ResponseEntity<String>(PhysicalState.toJsonArray(PhysicalState.findAllPhysicalStates("name", "ASC")), headers, HttpStatus.OK);
		} else {
	        return new ResponseEntity<String>(PhysicalState.toJsonArray(PhysicalState.findAllPhysicalStates()), headers, HttpStatus.OK);
		}
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        PhysicalState ps = PhysicalState.fromJsonToPhysicalState(json);
        ps.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        return showJson(ps.getId());
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (PhysicalState physicalState : PhysicalState.fromJsonArrayToPhysicalStates(json)) {
            physicalState.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        if (PhysicalState.fromJsonToPhysicalState(json).merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        for (PhysicalState physicalState : PhysicalState.fromJsonArrayToPhysicalStates(json)) {
            if (physicalState.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        PhysicalState physicalstate = PhysicalState.findPhysicalState(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        if (physicalstate == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        physicalstate.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getPhysicalStateOptions() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    
}
