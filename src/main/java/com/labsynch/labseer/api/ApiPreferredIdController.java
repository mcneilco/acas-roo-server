package com.labsynch.labseer.api;

import java.util.Collection;
import java.util.HashSet;

import com.labsynch.labseer.dto.PreferredNameDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = { "/api/v1" })
@Controller
public class ApiPreferredIdController {

	Logger logger = LoggerFactory.getLogger(ApiPreferredIdController.class);

	@Transactional
	@RequestMapping(value = "/getPreferredName", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonArrayCheckCorpNameExists(@RequestBody String json) {
		logger.debug("incoming json from getPreferredName: " + json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<PreferredNameDTO> preferredNameDTOs = PreferredNameDTO.fromJsonArrayToPreferredNameDTO(json);
		preferredNameDTOs = PreferredNameDTO.getPreferredNames(preferredNameDTOs);
		return new ResponseEntity<String>(PreferredNameDTO.toJsonArray(preferredNameDTOs), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/getPreferredName/parent", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonArrayCheckParentCorpNameExists(@RequestBody String json) {
		logger.debug("incoming json from getPreferredName: " + json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<PreferredNameDTO> preferredNameDTOs = PreferredNameDTO.fromJsonArrayToPreferredNameDTO(json);
		preferredNameDTOs = PreferredNameDTO.getParentPreferredNames(preferredNameDTOs);
		return new ResponseEntity<String>(PreferredNameDTO.toJsonArray(preferredNameDTOs), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/getPreferredName", method = RequestMethod.GET, headers = "Accept=application/json, application/text, text/html")
	@ResponseBody
	public ResponseEntity<String> checkCorpNameExists(@RequestParam String requestName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
		Collection<PreferredNameDTO> preferredNameDTOs = new HashSet<PreferredNameDTO>();
		PreferredNameDTO preferredNameDTO = new PreferredNameDTO();
		preferredNameDTO.setRequestName(requestName);
		preferredNameDTOs.add(preferredNameDTO);
		preferredNameDTOs = PreferredNameDTO.getPreferredNames(preferredNameDTOs);
		return new ResponseEntity<String>(PreferredNameDTO.toJsonArray(preferredNameDTOs), headers, HttpStatus.OK);
	}

}
