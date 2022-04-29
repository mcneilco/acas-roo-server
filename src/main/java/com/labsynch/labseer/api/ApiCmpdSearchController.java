package com.labsynch.labseer.api;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.dto.CmpdRegStructureSearchDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ChemStructureService.SearchType;
import com.labsynch.labseer.service.RegSearchService;
import com.labsynch.labseer.service.SearchFormService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = { "/api/v1/structuresearch" })
@Controller
public class ApiCmpdSearchController {

	Logger logger = LoggerFactory.getLogger(ApiCmpdSearchController.class);

	@Autowired
	private RegSearchService regSearchService;

	@Autowired
	private SearchFormService searchFormService;

	@RequestMapping(value = "/", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> structureSearch(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache

		List<Integer> searchResults;
		try {
			CmpdRegStructureSearchDTO structureSearchDTO = CmpdRegStructureSearchDTO
					.fromJsonToCmpdRegStructureSearchDTO(json);
			SearchType matchedSearchType = SearchType.getIfPresent(structureSearchDTO.getSearchType())
					.orElse(SearchType.SUBSTRUCTURE);
			searchResults = searchFormService.findParentIds(structureSearchDTO.getMolStructure(),
					structureSearchDTO.getMaxResults(), structureSearchDTO.getPercentSimilarity(), matchedSearchType,
					structureSearchDTO.getProjects());
			// Return the integer list of parent ids as a response Entity array
			return new ResponseEntity<String>(searchResults.toString(), headers, HttpStatus.OK);
		} catch (CmpdRegMolFormatException | IOException e) {
			logger.error("Error in structureSearch: " + e.getMessage());
			return new ResponseEntity<String>("Encountered error with input structure: " + e.toString(), headers,
					HttpStatus.BAD_REQUEST);
		}
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

	@RequestMapping(value = "/parents", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> searchStructures(
			@RequestBody String molStructure,

			// @RequestParam(value = "molStructure", required=true) String molStructure,
			@RequestParam(value = "maxResults", required = false) Integer maxResults,
			@RequestParam(value = "similarity", required = false) Float similarity,
			@RequestParam(value = "searchType", required = false) String searchType,
			@RequestParam(value = "outputFormat", required = false) String outputFormat) throws IOException {

		// options for outputFormat -- corpname, cdid, corpname-cdid, sdf ; default is
		// cdid
		// ./jcsearch --maxResults:$4 -q $9 -f sdf:Tcd_id DB:compound.parent_structure

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache

		try {
			SearchType matchedSearchType = SearchType.getIfPresent(searchType).orElse(SearchType.SUBSTRUCTURE);
			String searchResults = searchFormService.findParentIds(molStructure, maxResults, similarity,
					matchedSearchType, outputFormat);
			return new ResponseEntity<String>(searchResults, headers, HttpStatus.OK);
		} catch (CmpdRegMolFormatException e) {
			return new ResponseEntity<String>("Encountered error with input structure: " + e.toString(), headers,
					HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/parents/form", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> searchStructuresForm(
			@RequestParam(value = "molStructure", required = true) String molStructure,
			@RequestParam(value = "maxResults", required = false) Integer maxResults,
			@RequestParam(value = "similarity", required = false) Float similarity,
			@RequestParam(value = "searchType", required = false) String searchType,
			@RequestParam(value = "outputFormat", required = false) String outputFormat) throws IOException {

		// options for outputFormat -- corpname, cdid, corpname-cdid, sdf ; default is
		// cdid
		// ./jcsearch --maxResults:$4 -q $9 -f sdf:Tcd_id DB:compound.parent_structure

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache

		if (outputFormat == null || outputFormat.equalsIgnoreCase(""))
			outputFormat = "cdid";

		String searchResults;
		try {
			SearchType matchedSearchType = SearchType.getIfPresent(searchType).orElse(SearchType.DEFAULT);
			searchResults = searchFormService.findParentIds(molStructure, maxResults, similarity, matchedSearchType,
					outputFormat);
			return new ResponseEntity<String>(searchResults, headers, HttpStatus.OK);
		} catch (CmpdRegMolFormatException e) {
			return new ResponseEntity<String>("Encountered error with input structure: " + e.toString(), headers,
					HttpStatus.BAD_REQUEST);
		}
	}

}
