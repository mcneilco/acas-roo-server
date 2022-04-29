package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StructureSaveException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.service.ErrorMessage;
import com.labsynch.labseer.service.SaltService;
import com.labsynch.labseer.service.SaltStructureService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping(value = {"/api/v1/salts"})
@Transactional
@Controller
public class ApiSaltController {
	
	Logger logger = LoggerFactory.getLogger(ApiSaltController.class);

	@Autowired
	private SaltService saltService;

	@Autowired
	private SaltStructureService saltStructureService;

	@Autowired
	private ChemStructureService chemStructureService;
		
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

	@ModelAttribute("salts")
	public Collection<Salt> populateSalts() {
		return Salt.findAllSalts();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
		Salt salt = Salt.findSalt(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		if (salt == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(salt.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		return new ResponseEntity<String>(Salt.toJsonArray(Salt.findAllSalts()), headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) throws CmpdRegMolFormatException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		Salt salt = Salt.fromJsonToSalt(json);
		salt.setAbbrev(salt.getAbbrev().trim());
		salt.setName(salt.getName().trim());
		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		boolean validSalt = true;
		List<Salt> saltsByName = Salt.findSaltsByNameEquals(salt.getName()).getResultList();
		if (saltsByName.size() > 0) {
			logger.error("Number of salts found: " + saltsByName.size());
			validSalt = false;
			ErrorMessage error = new ErrorMessage();
			error.setLevel("error");
			error.setMessage("Duplicate salt name. Another salt exist with the same name.");
			errors.add(error);
		}
		List<Salt> saltsByAbbrev = Salt.findSaltsByAbbrevEquals(salt.getAbbrev()).getResultList();
		if (saltsByAbbrev.size() > 0) {
			logger.error("Number of salts found: " + saltsByAbbrev.size());
			validSalt = false;
			ErrorMessage error = new ErrorMessage();
			error.setLevel("error");
			error.setMessage("Duplicate salt abbreviation. Another salt exist with the same abbreviation.");
			errors.add(error);
		}
		if (validSalt) {
			try{
				salt = saltStructureService.saveStructure(salt);
			} catch (CmpdRegMolFormatException e) {
				logger.error("Error saving salt: " + e.getMessage());
				validSalt = false;
				ErrorMessage error = new ErrorMessage();
				error.setLevel("error");
				error.setMessage("Error saving salt: " + e.getMessage());
				errors.add(error);
				return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.BAD_REQUEST);
			}
		}
		if (salt.getCdId() == -1) {
			ErrorMessage error = new ErrorMessage();
			error.setLevel("error");
			error.setMessage("Bad molformat. Please fix the molfile: " + salt.getMolStructure());
			errors.add(error);
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.BAD_REQUEST);
		} else if (salt.getCdId() > 0) {
			salt.persist();
			return showJson(salt.getId());
		} else {
			ErrorMessage error = new ErrorMessage();
			error.setLevel("warning");
			error.setMessage("Duplicate salt found. Please select existing salt.");
			errors.add(error);
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.CONFLICT);
		}
	}

	// @RequestMapping(method = RequestMethod.OPTIONS)
	// public ResponseEntity<String> getSaltOptions() {
	// 	HttpHeaders headers = new HttpHeaders();
	// 	headers.add("Content-Type", "application/text, text/html");
	// 	headers.add("Access-Control-Allow-Headers", "Content-Type");
	// 	headers.add("Access-Control-Allow-Origin", "*");
	// 	headers.add("Access-Control-Max-Age", "86400");
	// 	return new ResponseEntity<String>(headers, HttpStatus.OK);
	// }

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text");
		try {
			Salt updatedSalt = saltStructureService.update(Salt.fromJsonToSalt(json));
			ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
			if (updatedSalt == null) {
				return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.CONFLICT);
			} else {
				return new ResponseEntity<String>(updatedSalt.toJson(), headers, HttpStatus.OK);
			}
		}catch (CmpdRegMolFormatException e) {
			return new ResponseEntity<String>("ERROR: Bad molfile:"+e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/saveMissingStructures", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> saveMissingStructures() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma", "no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		try {
			Collection<Salt> missingSaltStructures = saltStructureService.saveMissingStructures();
			return new ResponseEntity<String>(Salt.toJsonArray(missingSaltStructures), headers, HttpStatus.OK);
		}catch (StructureSaveException e) {
			return new ResponseEntity<String>("ERROR: Saving missing structures:"+e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
