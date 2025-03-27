package com.labsynch.labseer.api;

import java.io.IOException;
import java.util.List;

import javax.persistence.TypedQuery;

import com.labsynch.labseer.domain.StandardizationDryRunCompound;
import com.labsynch.labseer.domain.StandardizationHistory;
import com.labsynch.labseer.dto.StandardizationDryRunSearchDTO;
import com.labsynch.labseer.dto.StandardizationSettingsConfigCheckResponseDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;
import com.labsynch.labseer.service.StandardizationService;

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

@RequestMapping(value = { "/api/v1/standardization" })
@Controller
public class ApiStandardizationServicesController {

	Logger logger = LoggerFactory.getLogger(ApiStandardizationServicesController.class);

	@Autowired
	private StandardizationService standardizationService;

	@Transactional
	@RequestMapping(value = "/resetDryRunTables", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> reset() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		// reset standardization compound tables if the correct code is sent (basic
		// guard)
		logger.info("resetting Dry Run tables");
		standardizationService.reset();
		return new ResponseEntity<String>("Standardization tables reset", headers, HttpStatus.OK);

	}

	@Transactional
	@RequestMapping(value = "/populateDryRunTables", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> populateDryRunTable()
			throws CmpdRegMolFormatException, IOException, StandardizerException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		logger.info("checking parent structs and saving to dry run table");
		int numberOfDisplayChanges = 0;
		numberOfDisplayChanges = standardizationService.populateStandardizationDryRunTable();
		logger.info("number of compounds with display change: " + numberOfDisplayChanges);
		return new ResponseEntity<String>(" Compound check done. " + numberOfDisplayChanges, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/dryRun", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> dryRun(@RequestParam(value = "reportOnly", required = false) Boolean reportOnly)
			throws CmpdRegMolFormatException, IOException, StandardizerException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		boolean onlyReport = true;
		if (reportOnly != null && reportOnly == false) {
			onlyReport = false;
		}
		if (!onlyReport) {
			logger.info("reseting dry run table, populating dryrun table, dupe checking, and returning results");
			standardizationService.executeDryRun();
		}
		// Get most recent standardization history
		String jsonReport = standardizationService.getStandardizationDryRunReport();
		return new ResponseEntity<String>(jsonReport, headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/dryRunSearch", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> dryRunSearch(
			@RequestParam(value = "countOnly", required = false) Boolean countOnly,
			@RequestBody String json) {
		StandardizationDryRunSearchDTO searchCriteria = StandardizationDryRunSearchDTO
				.fromJsonToStandardizationDryRunSearchDTO(json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (countOnly != null && countOnly == true) {
			return new ResponseEntity<String>("{\"count\":"
					+ StandardizationDryRunCompound.searchStandardiationDryRunCount(searchCriteria).getSingleResult()
					+ "}", headers, HttpStatus.OK);
		} else {
			TypedQuery<StandardizationDryRunCompound> dryRunCompounds = StandardizationDryRunCompound
					.searchStandardiationDryRun(searchCriteria);
			return new ResponseEntity<String>(
					StandardizationDryRunCompound.toJsonArray(dryRunCompounds.getResultList()), headers, HttpStatus.OK);
		}
	}

	@Transactional
	@RequestMapping(value = "/dryRunSearchExport", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> dryRunSearchExport(
			@RequestBody String json) throws IOException, CmpdRegMolFormatException {
		logger.debug("incoming json: " + json);
		StandardizationDryRunSearchDTO searchCriteria = StandardizationDryRunSearchDTO
				.fromJsonToStandardizationDryRunSearchDTO(json);
		HttpHeaders headers = new HttpHeaders();
		String outputFilePath = standardizationService.getStandardizationDryRunReportFiles(searchCriteria);
		return new ResponseEntity<String>(outputFilePath, headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/dryRunReportFiles", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> dryRunReportFile(@RequestBody String filePath)
			throws IOException, CmpdRegMolFormatException {
		HttpHeaders headers = new HttpHeaders();
		// Get most recent standardization history
		String outputFilePath = standardizationService.getStandardizationDryRunReportFiles(filePath);
		return new ResponseEntity<String>(outputFilePath, headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/findStandardizationDupeStructs", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> findDryRunDupeStructs() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		logger.info("checking parent structs and saving to stanardization dryrun compound table");
		int numberOfDisplayChanges = 0;
		try {
			numberOfDisplayChanges = standardizationService.dupeCheckStandardizationStructures();
		} catch (CmpdRegMolFormatException e) {
			return new ResponseEntity<String>("Encountered error in searching: " + e.toString(), headers,
					HttpStatus.BAD_REQUEST);
		}
		logger.info("number of compounds with display change: " + numberOfDisplayChanges);
		return new ResponseEntity<String>(
				"Qc Compound check done. Number of display changes: " + numberOfDisplayChanges, headers, HttpStatus.OK);

	}

	@Transactional
	@RequestMapping(value = "/execute", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> execute(@RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "reason", required = true) String reason) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		logger.info("standardizing parent structs");
		try {
			String summary = standardizationService.executeStandardization(username, reason);
			return new ResponseEntity<String>(summary, headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught error trying to standardize parent structures: ", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/singleMol", method = RequestMethod.POST, headers = "Accept=text/plain")
	@ResponseBody
	public ResponseEntity<String> standardizeSingleMol(@RequestBody String mol)
			throws CmpdRegMolFormatException, StandardizerException, IOException {
		logger.debug("incoming json from standardizeMol: " + mol);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain");
		String stndardizedMol = standardizationService.standardizeSingleMol(mol);
		return new ResponseEntity<String>(stndardizedMol, headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/settings", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getCurrentStandardizationSettings() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			StandardizationSettingsConfigCheckResponseDTO standardizationSettingsConfigCheckResponseDTO = standardizationService.checkStandardizationState();
			return new ResponseEntity<String>(standardizationSettingsConfigCheckResponseDTO.toJson(), headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught error trying to get standardization settings: ", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/history", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getStandardizationHistory() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<StandardizationHistory> standardizationHistory = standardizationService.getStandardizationHistory();
		return new ResponseEntity<String>(StandardizationHistory.toJsonArray(standardizationHistory), headers,
				HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/dryRunStats", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getDryRunStats() throws StandardizerException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		String dryRunStats = standardizationService.getDryRunStats();
		return new ResponseEntity<String>(dryRunStats, headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/failRunnningStandardization", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> failRunnningStandardization() {
		HttpHeaders headers = new HttpHeaders();
		standardizationService.failRunnningStandardization();
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

}