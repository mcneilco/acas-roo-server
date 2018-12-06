package com.labsynch.labseer.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.labsynch.labseer.service.SaltService;

@RequestMapping(value = {"/api/v1/salts"})
@Controller
public class ApiSaltController {
	
	Logger logger = LoggerFactory.getLogger(ApiSaltController.class);

	@Autowired
	private SaltService saltService;
		
	@RequestMapping(value = "/load", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> loadSalts(
			@RequestParam (value = "saltSD_fileName", required = true) String saltSD_fileName
			) {
		
		logger.debug("hit the controller to register the salts: " + saltSD_fileName);
		
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
		
        int saltsSaved = saltService.loadSalts(saltSD_fileName);
		logger.debug("number of salts saved: " + saltsSaved);
		
        return new ResponseEntity<String>(headers, HttpStatus.OK);

//        example curl to call the method
//        curl -i -X POST -H "Accept: application/json" 'http://localhost:8080/cmpdreg/api/v1/salts/load?saltSD_fileName=/tmp/Initial_Salts.sdf'

	}

	
		
	@RequestMapping(method = RequestMethod.OPTIONS)
	public ResponseEntity<String> getOptions() {
		HttpHeaders headers= new HttpHeaders();
		headers.add("Content-Type", "application/text");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

}
