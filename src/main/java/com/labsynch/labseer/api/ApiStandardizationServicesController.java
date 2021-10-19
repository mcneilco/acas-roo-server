package com.labsynch.labseer.api;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.labsynch.labseer.domain.StandardizationHistory;
import com.labsynch.labseer.domain.StandardizationSettings;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.service.StandardizationService;

@RequestMapping(value = {"/api/v1/standardization"})
@Controller
public class ApiStandardizationServicesController {

	Logger logger = LoggerFactory.getLogger(ApiStandardizationServicesController.class);

	@Autowired
	private StandardizationService standardizationService;

	@Autowired
	private ChemStructureService chemStructServ;

	@Transactional
	@RequestMapping(value = "/resetDryRunTables", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> reset(){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		// reset standardization compound tables if the correct code is sent (basic guard)
		logger.info("resetting Dry Run tables");
		standardizationService.reset();
		return new ResponseEntity<String>("Standardization tables reset", headers, HttpStatus.OK);

	}


	@Transactional
	@RequestMapping(value = "/populateDryRunTables", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> populateDryRunTable() throws CmpdRegMolFormatException, IOException, StandardizerException{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		// populate qc compound tables if the correct code is sent (basic guard)
		logger.info("checking parent structs and saving to dry run table");
		int numberOfDisplayChanges = 0;
		numberOfDisplayChanges = standardizationService.populateStanardizationDryRunTable();
		logger.info("number of compounds with display change: " + numberOfDisplayChanges);
		return new ResponseEntity<String>(" Compound check done. " + numberOfDisplayChanges, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/dryRun", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> dryRun(@RequestParam(value="reportOnly", required = false) Boolean reportOnly) throws CmpdRegMolFormatException, IOException, StandardizerException{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		int numberOfDisplayChanges = 0;
		boolean onlyReport = true;
		if (reportOnly != null && reportOnly == false){
			onlyReport = false;
		}
		if(!onlyReport) {
			logger.info("reseting dry run table, populating dryrun table, dupe checking, and returning results");
			standardizationService.executeDryRun();
		}
		String jsonReport = standardizationService.getStandardizationDryRunReport();
		return new ResponseEntity<String>(jsonReport, headers, HttpStatus.OK);
	}


	@Transactional
	@RequestMapping(value = "/findStanardizationDupeStructs", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> findQCDupeStructs(){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		// searches for dupes in the qc compound tables if the correct code is sent (basic guard)
		logger.info("checking parent structs and saving to stanardization dryrun compound table");
		int numberOfDisplayChanges = 0;
		try {
			numberOfDisplayChanges = standardizationService.dupeCheckStandardizationStructures();
		} catch (CmpdRegMolFormatException e) {
			return new ResponseEntity<String>("Encountered error in searching: "+e.toString(), headers, HttpStatus.BAD_REQUEST);
		}
		logger.info("number of compounds with display change: " + numberOfDisplayChanges);
		return new ResponseEntity<String>("Qc Compound check done. Number of display changes: " + numberOfDisplayChanges, headers, HttpStatus.OK);

	}

	@RequestMapping(value = "/execute", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> execute(@RequestParam(value="username", required = true) String username, @RequestParam(value="reason", required = true) String reason){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		logger.info("standardizing parent structs");
		try{
			String summary = standardizationService.executeStandardization(username, reason);
			return new ResponseEntity<String>(summary, headers, HttpStatus.OK);
		}catch(Exception e){
			logger.error("Caught error trying to standardize parent structures: ",e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@Transactional
	@RequestMapping(value = "/singleMol", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public ResponseEntity<String> standardizeSingleMol(@RequestBody String mol) throws CmpdRegMolFormatException, StandardizerException, IOException{
		logger.debug("incoming json from standardizeMol: " + mol);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain");
		String stndardizedMol = standardizationService.standardizeSingleMol(mol);
		return new ResponseEntity<String>(stndardizedMol, headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/settings", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getCurrentStandardizationSettings(){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		StandardizationSettings stndardizationSettings = standardizationService.getStandardizationSettings();
		return new ResponseEntity<String>(stndardizationSettings.toJson(), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/history", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getStandardizationHistory(){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		List<StandardizationHistory> standardizationHistory = standardizationService.getStanardizationHistory();
		return new ResponseEntity<String>(StandardizationHistory.toJsonArray(standardizationHistory), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/dryRunStats", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getDryRunStats(){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		String dryRunStats = standardizationService.getDryRunStats();
		return new ResponseEntity<String>(dryRunStats, headers, HttpStatus.OK);
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