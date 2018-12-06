package com.labsynch.labseer.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.labsynch.labseer.service.CorpNameService;

@RequestMapping("/api")
@Controller
public class ApiController {

	private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

	@Autowired
	private CorpNameService corpNameService;
	
    @RequestMapping
    public void get(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }
   
		
    @RequestMapping(value = "/compound/convertBuid/", method = RequestMethod.POST, headers = "Accept=application/text")
    public ResponseEntity<String> post(@RequestBody String aliasList) {
		HttpHeaders headers= new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML); 	
		logger.debug("Input aliasList: " + aliasList);
		String preferredIds = corpNameService.getPreferredNameFromBuid(aliasList);
		
		return new ResponseEntity<String>(preferredIds, headers, HttpStatus.OK);        	
    }
    
    @RequestMapping(value = "/compound/buidLotNumber/", method = RequestMethod.POST, headers = "Accept=application/text")
    public ResponseEntity<String> buidToLotNumber(@RequestBody String aliasList) {
		HttpHeaders headers= new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML); 	
		logger.debug("Input aliasList: " + aliasList);
		String lotNumbers = corpNameService.getLotNumberFromBuid(aliasList);
		
		return new ResponseEntity<String>(lotNumbers, headers, HttpStatus.OK);        	
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
