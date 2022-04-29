package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.labsynch.labseer.dto.SearchFormDTO;
import com.labsynch.labseer.dto.SearchFormReturnDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ErrorMessage;
import com.labsynch.labseer.service.SearchFormService;

@RequestMapping(value = {"/api/v1/searchforms", "/api/v1/search"})
@Controller
public class ApiSearchFormController {
	
	Logger logger = LoggerFactory.getLogger(ApiSearchFormController.class);

	
	
	@Autowired
	private SearchFormService searchFormService;

	@RequestMapping(value = "/cmpds", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> searchCmpdsByParams(@RequestParam String searchParams) {
		HttpHeaders headers= new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		
		SearchFormDTO searchParamsJson = SearchFormDTO.fromJsonToSearchFormDTO(searchParams);
		System.out.println("incoming search params: " + searchParamsJson.toJson());

		try{
			SearchFormReturnDTO foundCompounds = searchFormService.findQuerySaltForms(searchParamsJson);
			if (foundCompounds.getFoundCompounds().size() > 1000){
				ErrorMessage searchError = new ErrorMessage();
				searchError.setLevel("error");
				searchError.setMessage("Too many search results returned. Found " + foundCompounds.getFoundCompounds().size() + " compounds. The search limit is 1000.");
				List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
				errors.add(searchError);
				return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.OK);						
			} else {
//				logger.debug("here is the search return: " + SearchCompoundReturnDTO.toJsonArray(foundCompounds));
				return new ResponseEntity<String>(foundCompounds.toJson(), headers, HttpStatus.OK);			
			}
		}catch (Exception e){
			logger.error("Uncaught error in searchCmpdsByParams",e);
			ErrorMessage searchError = new ErrorMessage();
			searchError.setLevel("error");
			searchError.setMessage("An internal error has occurred. Please contact your system administrator.");
			List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
			errors.add(searchError);
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		
	}

	@RequestMapping(value = "/cmpds", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> searchCmpdsByParamsPost(@RequestBody String searchParams) {
		HttpHeaders headers= new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		
		SearchFormDTO searchParamsJson = SearchFormDTO.fromJsonToSearchFormDTO(searchParams);
		System.out.println("incoming search params: " + searchParamsJson.toJson());
		
		try{
			SearchFormReturnDTO foundCompounds = searchFormService.findQuerySaltForms(searchParamsJson);
			if (foundCompounds.getFoundCompounds().size() > 1000){
				ErrorMessage searchError = new ErrorMessage();
				searchError.setLevel("error");
				searchError.setMessage("Too many search results returned. Found " + foundCompounds.getFoundCompounds().size() + " compounds. The search limit is 1000.");
				List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
				errors.add(searchError);
				return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.OK);						
			} else {
//				logger.debug("here is the search return: " + SearchCompoundReturnDTO.toJsonArray(foundCompounds));
				return new ResponseEntity<String>(foundCompounds.toJson(), headers, HttpStatus.OK);			
			}
		} catch (CmpdRegMolFormatException e){
			ErrorMessage searchError = new ErrorMessage();
			searchError.setLevel("error");
			searchError.setMessage(e.getMessage());
			List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
			errors.add(searchError);
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.BAD_REQUEST); 
		} catch (Exception e){
			logger.error("Uncaught error in searchCmpdsByParams",e);
			ErrorMessage searchError = new ErrorMessage();
			searchError.setLevel("error");
			searchError.setMessage("An internal error has occurred. Please contact your system administrator.");
			List<ErrorMessage> errors = new ArrayList<ErrorMessage>();
			errors.add(searchError);
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.INTERNAL_SERVER_ERROR); 
		}	
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
