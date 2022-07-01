package com.labsynch.labseer.api;

import com.labsynch.labseer.dto.ExportResultDTO;
import com.labsynch.labseer.dto.SearchResultExportRequestDTO;
import com.labsynch.labseer.service.ExportService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = { "/api/v1/export" })
@Controller
public class ApiExportController {

	Logger logger = LoggerFactory.getLogger(ApiExportController.class);

	@Autowired
	private ExportService exportService;

	@RequestMapping(value = "/searchResults", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> molconvert(@RequestBody String json) {
		if (logger.isDebugEnabled())
			logger.debug("incoming json from molconvert: " + json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		SearchResultExportRequestDTO searchResultExportRequestDTO = SearchResultExportRequestDTO
				.fromJsonToSearchResultExportRequestDTO(json);
		ExportResultDTO resultDTO;
		try {
			resultDTO = exportService.exportSearchResults(searchResultExportRequestDTO);
		} catch (Exception e) {
			logger.error("Caught error trying to export search results", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>(resultDTO.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/lotCorpNames", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<InputStreamResource> exportLotsByCorpName(@RequestBody List<String> codeNames) {
		HttpHeaders headers = new HttpHeaders();
		try{
			File outputFile = exportService.exportLots(codeNames);
			InputStream inputStream = new FileInputStream(outputFile);
			headers.add("Content-Type", "application/octet-stream");
			headers.add("Content-Disposition", "attachment; filename=" + outputFile.getName());
			headers.add("Content-Length", String.valueOf(outputFile.length()));
			return new ResponseEntity<InputStreamResource>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught error trying to export lots", e);
			return new ResponseEntity<InputStreamResource>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
