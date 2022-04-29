package com.labsynch.labseer.api;

import java.util.Collection;

import com.labsynch.labseer.dto.BatchProjectDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = { "/api/v1/projects" })
@Controller
public class ApiProjectController {

	Logger logger = LoggerFactory.getLogger(ApiProjectController.class);

	@RequestMapping(value = "/getBatchProjects", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getBatchProjects(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
		try {
			Collection<BatchProjectDTO> requestDTOs = BatchProjectDTO.fromJsonArrayToBatchProes(json);
			Collection<BatchProjectDTO> resultDTOs = BatchProjectDTO.getBatchProjects(requestDTOs);
			return new ResponseEntity<String>(BatchProjectDTO.toJsonArray(resultDTOs), headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught exception getting batch projects", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
