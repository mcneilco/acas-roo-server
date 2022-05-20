package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.List;


import com.labsynch.labseer.domain.PhysicalState;
import com.labsynch.labseer.service.ErrorMessage;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = { "/api/v1/physicalStates" })
@Transactional
@Controller
public class ApiPhysicalStateController {

    Logger logger = LoggerFactory.getLogger(ApiPhysicalStateController.class);

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    private static HttpHeaders getTextHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
        headers.add("Pragma", "no-cache"); // HTTP 1.0
        headers.setExpires(0); // Expire the cache
        return headers;
    }

    private static HttpHeaders getJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
        return headers;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        PhysicalState physicalstate = PhysicalState.findPhysicalState(id);
        HttpHeaders headers = getTextHeaders();
        if (physicalstate == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(physicalstate.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = getTextHeaders();

        if (propertiesUtilService.getOrderSelectLists()) {
            return new ResponseEntity<String>(
                    PhysicalState.toJsonArray(PhysicalState.findAllPhysicalStates("name", "ASC")), headers,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(PhysicalState.toJsonArray(PhysicalState.findAllPhysicalStates()), headers,
                    HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        PhysicalState ps = PhysicalState.fromJsonToPhysicalState(json);
        ps.persist();
        return showJson(ps.getId());
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (PhysicalState physicalState : PhysicalState.fromJsonArrayToPhysicalStates(json)) {
            physicalState.persist();
        }
        HttpHeaders headers = getTextHeaders();
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = getTextHeaders();
        if (PhysicalState.fromJsonToPhysicalState(json).merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = getTextHeaders();
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
        HttpHeaders headers = getTextHeaders();
        if (physicalstate == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        physicalstate.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getPhysicalStateOptions() {
        HttpHeaders headers = getTextHeaders();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    // validate physicalState before saving
	// is physicalState code still unique?
	// is physicalState name unique? (optional)
	@RequestMapping(value = "/validateBeforeSave", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> validateNewPhysicalState(@RequestBody String json) {
		logger.info("validateNewPhysicalState -- incoming json: " + json);
		PhysicalState queryPhysicalState = PhysicalState.fromJsonToPhysicalState(json);
		if (queryPhysicalState.getCode() == null || queryPhysicalState.getCode().equalsIgnoreCase("")) {
			logger.info("creating the missing code name");
			queryPhysicalState.setCode(queryPhysicalState.getName().toLowerCase());
		}
		logger.info("validateNewPhysicalState -- query physicalState: " + queryPhysicalState.toJson());

		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		// boolean errorsFound = false;

		HttpHeaders headers = getJsonHeaders(); // Expire the cache

		Long physicalStateByCodeCount = 0L;
		List<PhysicalState> queryPhysicalStates = PhysicalState.findPhysicalStatesByCodeEquals(queryPhysicalState.getCode()).getResultList();
		for (PhysicalState physicalState : queryPhysicalStates) {
			if (queryPhysicalState.getId() == null || physicalState.getId().longValue() != queryPhysicalState.getId().longValue()) {
				++physicalStateByCodeCount;
				logger.debug("current physicalState: " + physicalState.toJson());
				logger.debug("physicalState id: " + physicalState.getId());
				logger.debug("query physicalState ID: " + queryPhysicalState.getId());
				logger.debug("incrementing the physicalState count " + physicalStateByCodeCount);
			}
		}

		if (physicalStateByCodeCount > 0) {
			ErrorMessage error = new ErrorMessage();
			error.setLevel("ERROR");
			error.setMessage("Found another physicalState with the same code name");
			errors.add(error);
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<String>(queryPhysicalState.toJson(), headers, HttpStatus.OK);
		}
	}

}
