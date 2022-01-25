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

import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.StereoCategory;

import com.labsynch.labseer.service.ErrorMessage;
import com.labsynch.labseer.utils.PropertiesUtilService;
import org.springframework.beans.factory.annotation.Autowired;

@RequestMapping(value = {"/api/v1/stereoCategories"})
@Controller
public class ApiStereoCategoryController {

	Logger logger = LoggerFactory.getLogger(ApiStereoCategoryController.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@RequestMapping(value = "/validate", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<Boolean> validate(@RequestParam(value="code", required = true) String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

		if (StereoCategory.countFindStereoCategorysByCodeEquals(code) > 0){
			return new ResponseEntity<Boolean>(false, headers,HttpStatus.CONFLICT );
		} else {
			return new ResponseEntity<Boolean>(true, headers,HttpStatus.ACCEPTED );	        	
		}
	}	

	@RequestMapping(value = "/validateBeforeSave", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> validateNewStereoCategory(@RequestBody String json) {
		logger.info("validateNewStereoCategory -- incoming json: " + json);
		StereoCategory queryStereoCategory = StereoCategory.fromJsonToStereoCategory(json);
		if (queryStereoCategory.getCode() == null || queryStereoCategory.getCode().equalsIgnoreCase("")) {
			logger.info("creating the missing code name");
			queryStereoCategory.setCode(queryStereoCategory.getName().toLowerCase());
		}
		logger.info("validateNewStereoCategory -- query stereoCategory: " + queryStereoCategory.toJson());

		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		// boolean errorsFound = false;

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

		Long stereoCategoryByCodeCount = 0L;
		List<StereoCategory> queryStereoCategorys = StereoCategory.findStereoCategorysByCodeEquals(queryStereoCategory.getCode()).getResultList();
		for (StereoCategory stereoCategory : queryStereoCategorys){
			if (queryStereoCategory.getId() == null || stereoCategory.getId().longValue() != queryStereoCategory.getId().longValue()){
				++stereoCategoryByCodeCount;
				logger.debug("current stereoCategory: " + stereoCategory.toJson());
				logger.debug("stereoCategory id: " + stereoCategory.getId());
				logger.debug("query stereoCategory ID: " + queryStereoCategory.getId());
				logger.debug("incrementing the stereoCategory count " + stereoCategoryByCodeCount);
			}
		}

		if (stereoCategoryByCodeCount > 0  ){
			ErrorMessage error = new ErrorMessage();
			error.setLevel("ERROR");
			error.setMessage("Found another stereo category with the same code name");
			errors.add(error);
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers,HttpStatus.CONFLICT );
		} else {
			return new ResponseEntity<String>(queryStereoCategory.toJson(), headers,HttpStatus.OK );	        	
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
		List<StereoCategory> stereoCategorys = StereoCategory.findStereoCategorysByCodeEquals(code).getResultList();
		logger.debug("number of stereoCategorys found: " + stereoCategorys.size());
		if (stereoCategorys.size() != 1){
			return new ResponseEntity<String>("[]", headers, HttpStatus.CONFLICT);       	
		} else {
			return new ResponseEntity<String>(stereoCategorys.get(0).toJson(), headers, HttpStatus.OK);
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
		return new ResponseEntity<String>(StereoCategory.toJsonArray(StereoCategory.findStereoCategorysByCodeLike(code).getResultList()), headers, HttpStatus.OK);
	}	 

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
		StereoCategory stereoCategory = StereoCategory.findStereoCategory(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		if (stereoCategory == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(stereoCategory.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

		if (propertiesUtilService.getOrderSelectLists()){
			return new ResponseEntity<String>(StereoCategory.toJsonArray(StereoCategory.findAllStereoCategorys("name", "ASC")), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(StereoCategory.toJsonArray(StereoCategory.findAllStereoCategorys()), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) {
		StereoCategory newStereoCategory = StereoCategory.fromJsonToStereoCategory(json);
		if (newStereoCategory.getCode() == null || newStereoCategory.getCode().equalsIgnoreCase("")) {
			logger.info("creating the missing code name");
			newStereoCategory.setCode(newStereoCategory.getName().toLowerCase());
		}
		newStereoCategory.persist();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		return new ResponseEntity<String>(newStereoCategory.toJson(), headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
		for (StereoCategory stereoCategory : StereoCategory.fromJsonArrayToStereoCategorys(json)) {
			if (stereoCategory.getCode() == null || stereoCategory.getCode().equalsIgnoreCase("")) {
				logger.info("creating the missing code name");
				stereoCategory.setCode(stereoCategory.getName().toLowerCase());
			}
			stereoCategory.persist();
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
		if (StereoCategory.fromJsonToStereoCategory(json).merge() == null) {
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
		for (StereoCategory stereoCategory : StereoCategory.fromJsonArrayToStereoCategorys(json)) {
			if (stereoCategory.merge() == null) {
				return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
			}
		}
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		StereoCategory stereoCategory = StereoCategory.findStereoCategory(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		if (stereoCategory == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		try {
			stereoCategory.remove();
		} catch (Exception e){
			logger.info("Hit an exception: " + e);
			Long parentCount = Parent.countParentsByStereoCategory(stereoCategory);
			logger.info("Unable to delete the stereoCategory. " + parentCount + " parents associated with the stereoCategory " + stereoCategory.getName());
			ErrorMessage error = new ErrorMessage();
			error.setLevel("ERROR");
			error.setMessage("Unable to delete the stereoCategory. " + parentCount + " parents associated with the stereoCategory " + stereoCategory.getName());
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
		return new ResponseEntity<String>(StereoCategory.toJsonArray(StereoCategory.findStereoCategoriesBySearchTerm(searchTerm).getResultList()), headers, HttpStatus.OK);
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
