package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StructureSaveException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.service.ErrorMessage;
import com.labsynch.labseer.service.SaltService;
import com.labsynch.labseer.service.SaltStructureService;
import com.labsynch.labseer.service.StandardizationService;
import com.labsynch.labseer.service.StructureImageServiceImpl; 

import com.labsynch.labseer.dto.PurgeSaltDependencyCheckResponseDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = { "/api/v1/salts" })
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

	@Autowired
	private StandardizationService standardizationService;

	private static HttpHeaders getJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
        return headers;
    }

	@RequestMapping(value = "/load", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> loadSalts(
			@RequestParam(value = "saltSD_fileName", required = true) String saltSD_fileName) {

		logger.debug("hit the controller to register the salts: " + saltSD_fileName);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");

		int saltsSaved = saltService.loadSalts(saltSD_fileName);
		logger.debug("number of salts saved: " + saltsSaved);

		return new ResponseEntity<String>(headers, HttpStatus.OK);

		// example curl to call the method
		// curl -i -X POST -H "Accept: application/json"
		// 'http://localhost:8080/cmpdreg/api/v1/salts/load?saltSD_fileName=/tmp/Initial_Salts.sdf'

	}

	@RequestMapping(value = "/search", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> searchBySearchTerms(
			@RequestParam(value = "searchTerm", required = true) String searchTerm) {
		HttpHeaders headers = new HttpHeaders();
		return new ResponseEntity<String>(
				Salt.toJsonArray(Salt.findSaltsBySearchTerm(searchTerm).getResultList()), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
		Salt salt = Salt.findSalt(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
		if (salt == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(salt.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> editSalt(@PathVariable("id") Long id, @RequestBody String json) {
		Salt oldSalt = Salt.findSalt(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
		if (oldSalt == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		else
		{
			try {
				Salt newSalt = Salt.fromJsonToSalt(json);
				newSalt.setAbbrev(newSalt.getAbbrev().trim());
				newSalt.setName(newSalt.getName().trim());
				ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
				boolean validSalt = true;
				List<Salt> saltsByName = Salt.findSaltsByNameEquals(newSalt.getName()).getResultList();
				if (saltsByName.size() > 1) {
					logger.error("Number of salts found: " + saltsByName.size());
					validSalt = false;
					ErrorMessage error = new ErrorMessage();
					error.setLevel("error");
					error.setMessage("Duplicate salt name. Another salt exist with the same name.");
					errors.add(error);
				}
				List<Salt> saltsByAbbrev = Salt.findSaltsByAbbrevEquals(newSalt.getAbbrev()).getResultList();
				if (saltsByAbbrev.size() > 1) {
					logger.error("Number of salts found: " + saltsByAbbrev.size());
					validSalt = false;
					ErrorMessage error = new ErrorMessage();
					error.setLevel("error");
					error.setMessage("Duplicate salt abbreviation. Another salt exist with the same abbreviation.");
					errors.add(error);
				}
				if (validSalt) {
					try {
						oldSalt = saltStructureService.edit(oldSalt, newSalt);
					} catch (Exception e) {
						logger.error("Error updating salt: " + e.getMessage());
						validSalt = false;
						ErrorMessage error = new ErrorMessage();
						error.setLevel("error");
						error.setMessage("Error updating salt: " + e.getMessage());
						errors.add(error);
						return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.BAD_REQUEST);
					}
				}
				try
				{
					// Attempt to Restandardize 
					List<Long> saltID= new ArrayList<Long>();
					saltID.add(oldSalt.getId());
					standardizationService.restandardizeLots(saltID);
					// Return Response
					String saltMol = oldSalt.toJson();
					return new ResponseEntity<String>(saltMol, headers, HttpStatus.OK);
				}
				catch (Exception e)
				{
					return new ResponseEntity<String>("ERROR: Restandardization Issue:" + e.getMessage(), headers,
					HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (Exception e) {
				return new ResponseEntity<String>("ERROR: Bad Salt:" + e.getMessage(), headers,
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> deleteSalt(@PathVariable("id") Long id) {
		Salt salt = Salt.findSalt(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
		if (salt == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		else
		{
			// Need to Do Validation Of No Lot Association Before Final Removal
			boolean lotDependency = false; 
			PurgeSaltDependencyCheckResponseDTO dependencyReport  = salt.checkDependentData();
			lotDependency = ! dependencyReport.isCanPurge();
			// Need to Get Dependency Bool From Report JSON String

			// Query to See If Lot Depends on This Salt ID 
			if(lotDependency){
				return new ResponseEntity<String>(dependencyReport.getSummary(), headers, HttpStatus.EXPECTATION_FAILED);
			}
			else
			{
				salt.remove(); // This needs to be a hard deletion 
				return new ResponseEntity<String>(dependencyReport.getSummary(), headers, HttpStatus.OK);
			}
		}
	}
	
	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
		return new ResponseEntity<String>(Salt.toJsonArray(Salt.findAllSalts()), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/sdf", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listSDF() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
		String SaltJSONStr = saltService.exportSalts(); 
		// Need to Convert JSON Str to SDF Str 
			// Consider Using convertMolfilesToSDFile
			// Though This Might Not Export Properties Fully Desired 
		
		return new ResponseEntity<String>(SaltJSONStr, headers, HttpStatus.OK);
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
			try {
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
	// HttpHeaders headers = new HttpHeaders();
	// headers.add("Content-Type", "application/text, text/html");
	// headers.add("Access-Control-Allow-Headers", "Content-Type");
	// headers.add("Access-Control-Allow-Origin", "*");
	// headers.add("Access-Control-Max-Age", "86400");
	// return new ResponseEntity<String>(headers, HttpStatus.OK);
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
				try
				{
					// Attempt to Restandardize 
					String saltMol = updatedSalt.toJson();
					standardizationService.standardizeSingleMol(saltMol); 
					return new ResponseEntity<String>(saltMol, headers, HttpStatus.OK);
				}
				catch (Exception e)
				{
					return new ResponseEntity<String>("ERROR: Restandardization Issue:" + e.getMessage(), headers,
					HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<String>("ERROR: Bad molfile:" + e.getMessage(), headers,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/saveMissingStructures", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> saveMissingStructures() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache
		try {
			Collection<Salt> missingSaltStructures = saltStructureService.saveMissingStructures();
			return new ResponseEntity<String>(Salt.toJsonArray(missingSaltStructures), headers, HttpStatus.OK);
		} catch (StructureSaveException e) {
			return new ResponseEntity<String>("ERROR: Saving missing structures:" + e.getMessage(), headers,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
