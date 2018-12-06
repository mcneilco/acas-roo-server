package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Scientist;
import com.labsynch.labseer.dto.configuration.MainConfigDTO;
import com.labsynch.labseer.service.ErrorMessage;
import com.labsynch.labseer.utils.Configuration;

@RequestMapping(value = {"/api/v1/scientists"})
@Controller
public class ApiScientistController {

	Logger logger = LoggerFactory.getLogger(ApiScientistController.class);

	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	@RequestMapping(value = "/validateBeforeSave", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> validateNewScientist(@RequestBody String json) {
		logger.info("validateNewScientist -- incoming json: " + json);
		Scientist queryScientist = Scientist.fromJsonToScientist(json);
		if (queryScientist.getCode() == null || queryScientist.getCode().equalsIgnoreCase("")) {
			logger.info("creating the missing code name");
			queryScientist.setCode(queryScientist.getName().toLowerCase());
		}
		logger.info("validateNewScientist -- query scientist: " + queryScientist.toJson());

		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		// boolean errorsFound = false;

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

		Long scientistByCodeCount = 0L;
		List<Scientist> queryScientists = Scientist.findScientistsByCodeEquals(queryScientist.getCode()).getResultList();
		for (Scientist scientist : queryScientists){
			if (queryScientist.getId() == null || scientist.getId().longValue() != queryScientist.getId().longValue()){
				++scientistByCodeCount;
				logger.debug("current vendor: " + scientist.toJson());
				logger.debug("vendor id: " + scientist.getId());
				logger.debug("query vendor ID: " + queryScientist.getId());
				logger.debug("incrementing the vendor count " + scientistByCodeCount);
			}
		}

		if (scientistByCodeCount > 0  ){
			ErrorMessage error = new ErrorMessage();
			error.setLevel("ERROR");
			error.setMessage("Found another scientist with the same code name");
			errors.add(error);
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers,HttpStatus.CONFLICT );
		} else {
			return new ResponseEntity<String>(queryScientist.toJson(), headers,HttpStatus.OK );	        	
		}
	}

	@RequestMapping(value = "/findByCodeEquals", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> findByCodeEquals(@RequestParam(value="code", required = true) String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		List<Scientist> scientists = Scientist.findScientistsByCodeEquals(code).getResultList();
		logger.debug("number of scientists found: " + scientists.size());
		if (scientists.size() != 1){
			return new ResponseEntity<String>("[]", headers, HttpStatus.CONFLICT);       	
		} else {
			return new ResponseEntity<String>(scientists.get(0).toJson(), headers, HttpStatus.OK);
		}

	}	 

	@RequestMapping(value = "/findByCodeLike", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> findByCodeLike(@RequestParam(value="code", required = true) String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		return new ResponseEntity<String>(Scientist.toJsonArray(Scientist.findScientistsByCodeLike(code).getResultList()), headers, HttpStatus.OK);
	}	 

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
		Scientist scientist = Scientist.findScientist(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		if (scientist == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(scientist.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(@RequestParam(value = "withLots", required=false) Boolean withLots) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		if (withLots != null && withLots) {
			List<Scientist> foundScientists = Scientist.findScientistsWithLots();
			if (mainConfig.getServerSettings().isOrderSelectLists()) {
				foundScientists.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
			}
			return new ResponseEntity<String>(Scientist.toJsonArray(foundScientists), headers, HttpStatus.OK);
		}

		if (mainConfig.getServerSettings().isOrderSelectLists()){
			return new ResponseEntity<String>(Scientist.toJsonArray(Scientist.findAllScientists("name", "ASC")), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(Scientist.toJsonArray(Scientist.findAllScientists()), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		Scientist scientist = Scientist.fromJsonToScientist(json);
		if (scientist.getCode() == null || scientist.getCode().equalsIgnoreCase("")) {
			logger.info("creating the missing code name");
			scientist.setCode(scientist.getName().toLowerCase());
		}
		scientist.persist();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		return new ResponseEntity<String>(scientist.toJson(), headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		for (Scientist scientist : Scientist.fromJsonArrayToScientists(json)) {
			if (scientist.getCode() == null || scientist.getCode().equalsIgnoreCase("")) {
				logger.info("creating the missing code name");
				scientist.setCode(scientist.getName().toLowerCase());
			}
			scientist.persist();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		if (Scientist.fromJsonToScientist(json).merge() == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		for (Scientist scientist : Scientist.fromJsonArrayToScientists(json)) {
			if (scientist.merge() == null) {
				return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
			}
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		Scientist scientist = Scientist.findScientist(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		if (scientist == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		try {
			scientist.remove();
		} catch (Exception e){
			logger.info("Hit an exception: " + e);
			Long lotCount = Lot.countLotsByRegisteredBy(scientist);
			logger.info("Unable to delete the registered by scientists. " + lotCount + " lots associated with the scientist " + scientist.getName());
			ErrorMessage error = new ErrorMessage();
			error.setLevel("ERROR");
			error.setMessage("Unable to delete the scientist. " + lotCount + " lots associated with the scientist " + scientist.getName());
			errors.add(error);
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers,HttpStatus.CONFLICT );
		}

		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> searchBySearchTerms(@RequestParam(value="searchTerm", required = true) String searchTerm) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		return new ResponseEntity<String>(Scientist.toJsonArray(Scientist.findScientistsBySearchTerm(searchTerm).getResultList()), headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.OPTIONS)
	public ResponseEntity<String> getOptions() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}	 

}
