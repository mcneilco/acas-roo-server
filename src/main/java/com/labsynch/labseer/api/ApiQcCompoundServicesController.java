package com.labsynch.labseer.api;

import java.io.IOException;

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

import com.labsynch.labseer.domain.QcCompound;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.service.QcCmpdService;

@RequestMapping(value = {"/api/v1/qcCompoundServices"})
@Controller
public class ApiQcCompoundServicesController {

	Logger logger = LoggerFactory.getLogger(ApiQcCompoundServicesController.class);

	@Autowired
	private QcCmpdService qcCmpdServ;

	@Autowired
	private ChemStructureService chemStructServ;

	@Transactional
	@RequestMapping(value = "/resetQcCompoundTables", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> reset(@RequestParam String adminCode){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		if (adminCode.equalsIgnoreCase("lajolla-reset")){
			// reset qc compound tables if the correct code is sent (basic guard)
			logger.info("resetting QC Compound tables");
			boolean dropTable = chemStructServ.truncateStructureTable("qc_compound_structure");
			if (dropTable){
				QcCompound.truncateTable();
			} else {
				logger.info("unable to drop jchem table");
			}
			return new ResponseEntity<String>("Qc Compound tables reset", headers, HttpStatus.OK);
		} else {
			//do nothing
			return new ResponseEntity<String>("NO Response", headers, HttpStatus.BAD_REQUEST);
		}
	}


	@Transactional
	@RequestMapping(value = "/qcParentStructs", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> checkParentStructs(@RequestParam String adminCode) throws CmpdRegMolFormatException, IOException{
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		if (adminCode.equalsIgnoreCase("lajolla-check")){
			// populate qc compound tables if the correct code is sent (basic guard)
			logger.info("checking parent structs and saving to QC table");
			int numberOfDisplayChanges = 0;
			numberOfDisplayChanges = qcCmpdServ.qcCheckParentStructures();
			logger.info("number of compounds with display change: " + numberOfDisplayChanges);
			return new ResponseEntity<String>("Qc Compound check done. " + numberOfDisplayChanges, headers, HttpStatus.OK);
		} else {
			//do nothing
			return new ResponseEntity<String>("NO Response", headers, HttpStatus.BAD_REQUEST);
		}
	}


	@Transactional
	@RequestMapping(value = "/findQCDupeStructs", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> findQCDupeStructs(@RequestParam String adminCode){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		if (adminCode.equalsIgnoreCase("lajolla-check")){
			// searches for dupes in the qc compound tables if the correct code is sent (basic guard)
			logger.info("checking parent structs and saving to QC table");
			int numberOfDisplayChanges = 0;
			try {
				numberOfDisplayChanges = qcCmpdServ.dupeCheckQCStructures();
			} catch (CmpdRegMolFormatException e) {
				return new ResponseEntity<String>("Encountered error in searching: "+e.toString(), headers, HttpStatus.BAD_REQUEST);
			}
			logger.info("number of compounds with display change: " + numberOfDisplayChanges);
			return new ResponseEntity<String>("Qc Compound check done. Number of display changes: " + numberOfDisplayChanges, headers, HttpStatus.OK);
		} else {
			//do nothing
			return new ResponseEntity<String>("NO Response", headers, HttpStatus.BAD_REQUEST);
		}
	}

	@Transactional
	@RequestMapping(value = "/reportQCDupeStructs", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> reportQCDupeStructs(@RequestParam String exportType,
			@RequestBody String reportFile){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		try {
			if (reportFile == null || reportFile.equalsIgnoreCase("")){
				reportFile = "/tmp/qcDupeReport.sdf";
			}
			qcCmpdServ.exportQCReport(reportFile, exportType);
		} catch (IOException | CmpdRegMolFormatException e) {
			return new ResponseEntity<String>("ERROR: unable to generate report", headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>("QC Dupe Struct report: " + reportFile, headers, HttpStatus.OK);
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
