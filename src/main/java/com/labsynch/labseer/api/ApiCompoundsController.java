package com.labsynch.labseer.api;


import com.labsynch.labseer.dto.CmpdRegBatchCodeDTO;

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

@Controller
@RequestMapping("api/v1/compounds")
@Transactional
public class ApiCompoundsController {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiCompoundsController.class);
	
	@Transactional
    @RequestMapping(value = "/checkBatchDependencies", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> checkBatchDependencies(@RequestBody CmpdRegBatchCodeDTO batchDTO) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		batchDTO.checkForDependentData();
		return new ResponseEntity<String>(batchDTO.toJson(), headers, HttpStatus.OK);
	}
	
	
}
