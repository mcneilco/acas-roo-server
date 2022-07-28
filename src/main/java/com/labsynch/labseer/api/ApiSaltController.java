package com.labsynch.labseer.api;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.dto.PurgeSaltDependencyCheckResponseDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StructureSaveException;
import com.labsynch.labseer.service.ErrorMessage;
import com.labsynch.labseer.service.SaltService;
import com.labsynch.labseer.service.SaltStructureService;
import com.labsynch.labseer.service.StandardizationService;

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
	private StandardizationService standardizationService;

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
			return new ResponseEntity<String>("Salt could not be found", headers, HttpStatus.NOT_FOUND);
		}
		else
		{
			try {
				ObjectMapper objectMapper = new ObjectMapper();

				JsonNode rootNode = objectMapper.readTree(json);
				JsonNode dryRunNode = rootNode.path("dryrun");
				boolean dryrun = dryRunNode.asBoolean();

				rootNode = objectMapper.readTree(json);
				JsonNode saltNode = rootNode.path("saltJSON");
				Salt newSalt = Salt.fromJsonToSalt(saltNode.toString());

				ArrayList<ErrorMessage> warnings = new ArrayList<ErrorMessage>();

				if(!newSalt.getAbbrev().equals(oldSalt.getAbbrev())){
					ErrorMessage warning = new ErrorMessage();
					warning.setLevel("warning");
					warning.setMessage("The abbreviation of this salt will be changed.");
					warnings.add(warning);
				}
				if(!newSalt.getName().equals(oldSalt.getName())){
					ErrorMessage warning = new ErrorMessage();
					warning.setLevel("warning");
					warning.setMessage("The name of this salt will be changed.");
					warnings.add(warning);
				}

				newSalt.setAbbrev(newSalt.getAbbrev().trim());
				newSalt.setName(newSalt.getName().trim());
				newSalt.setFormula(saltStructureService.calculateFormula(newSalt));

				// This doesn't always work; Might need to convert to mol and then compare
				if(!newSalt.getMolStructure().equals(oldSalt.getMolStructure())){
					ErrorMessage warning = new ErrorMessage();
					warning.setLevel("warning");
					warning.setMessage("The structure of this salt might be changed. The weight and charge might be affected.");
					warnings.add(warning);
				}

				boolean validSalt = true;
				List<Salt> saltsByName = Salt.findSaltsByNameEquals(newSalt.getName()).getResultList();
				if (saltsByName.size() > 1) {
					logger.error("Number of salts found: " + saltsByName.size());
					validSalt = false;
					ErrorMessage error = new ErrorMessage();
					error.setLevel("error");
					error.setMessage("Duplicate salt name. Another salt exist with the same name.");
					warnings.add(error);
				}
				List<Salt> saltsByAbbrev = Salt.findSaltsByAbbrevEquals(newSalt.getAbbrev()).getResultList();
				if (saltsByAbbrev.size() > 1) {
					logger.error("Number of salts found: " + saltsByAbbrev.size());
					validSalt = false;
					ErrorMessage error = new ErrorMessage();
					error.setLevel("error");
					error.setMessage("Duplicate salt abbreviation. Another salt exist with the same abbreviation.");
					warnings.add(error);
				}
				List<Salt> saltsByFormula = Salt.findSaltsByFormulaEquals(newSalt.getFormula()).getResultList();
				if (saltsByFormula.size() > 1) {
					logger.error("Number of salts found: " + saltsByName.size());
					validSalt = false;
					ErrorMessage error = new ErrorMessage();
					error.setLevel("error");
					error.setMessage("Duplicate salt formula. Another salt exist with the same formula.");
					warnings.add(error);
				}
				try { 
					PurgeSaltDependencyCheckResponseDTO dependencyReport  = saltService.checkDependentData(oldSalt);
					boolean hasDependencies = ! dependencyReport.isCanPurge();
					if(hasDependencies){
						ErrorMessage warning = new ErrorMessage();
						warning.setLevel("warning");
						warning.setMessage("This salt has experiment/lot dependencies. An attempt will be made to restandardize.");
						warnings.add(warning);
						// Need to Iterate Through Dependency Report and Individually Add As Warnings
					}
				}
				catch (Exception e)
				{
					return new ResponseEntity<String>("ERROR: Depedency Check Issue:" + e.getMessage(), headers,
						HttpStatus.INTERNAL_SERVER_ERROR);
				}
				if (validSalt && !dryrun) {
					try {
						oldSalt = saltStructureService.edit(oldSalt, newSalt);
						try
							{
								// Attempt to Restandardize 
								List<Long> saltID= new ArrayList<Long>();
								saltID.add(oldSalt.getId());
								standardizationService.restandardizeLots(saltID);


								// Return Response of Any Warnings / Errors from Validations / Dependencies Done Prior 
								return new ResponseEntity<String>(ErrorMessage.toJsonArray(warnings), headers, HttpStatus.OK);
							}
						catch (Exception e)
							{
								return new ResponseEntity<String>("ERROR: Restandardization Issue:" + e.getMessage(), headers,
								HttpStatus.INTERNAL_SERVER_ERROR);
							}
					} catch (Exception e) {
						logger.error("Error updating salt: " + e.getMessage());

						validSalt = false;

						ErrorMessage error = new ErrorMessage();
						error.setLevel("error");
						error.setMessage("Error updating salt: " + e.getMessage());
						warnings.add(error);

						return new ResponseEntity<String>(ErrorMessage.toJsonArray(warnings), headers, HttpStatus.BAD_REQUEST);
					}
				}
				else if (!validSalt)
				{
					return new ResponseEntity<String>(ErrorMessage.toJsonArray(warnings), headers,
					HttpStatus.EXPECTATION_FAILED);
				}
				else // Valid Salt Dryrun Case
				{
					return new ResponseEntity<String>(ErrorMessage.toJsonArray(warnings), headers, HttpStatus.OK);
				}
			} catch (Exception e) { // Assuming Catch Here is For Salt Formatting Problem
				e.printStackTrace();
				return new ResponseEntity<String>("ERROR: Bad Salt:" + e.getMessage(), headers,
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}		
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> deleteSalt(@PathVariable("id") Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text; charset=utf-8");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache

		Salt salt = Salt.findSalt(id);

		if (salt == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		else
		{
			// Need to Do Validation Of No Lot Association Before Final Removal
			boolean lotDependency = false; 
			PurgeSaltDependencyCheckResponseDTO dependencyReport  = saltService.checkDependentData(salt);
			lotDependency = ! dependencyReport.isCanPurge();
			// Need to Get Dependency Bool From Report JSON String

			// Query to See If Lot Depends on This Salt ID 
			if(lotDependency){
				return new ResponseEntity<String>(dependencyReport.getSummary(), headers, HttpStatus.EXPECTATION_FAILED);
			}
			else
			{
				salt.remove(); 
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
		String SaltSDFStr = saltService.exportSalts(); // Returns SDF String of All Salts Stored
		return new ResponseEntity<String>(SaltSDFStr, headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(@RequestBody String json) throws CmpdRegMolFormatException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); // HTTP 1.1
		headers.add("Pragma", "no-cache"); // HTTP 1.0
		headers.setExpires(0); // Expire the cache

		Salt salt = null; 
		boolean dryrun = true; 
		try
		{
			ObjectMapper objectMapper = new ObjectMapper();

			JsonNode rootNode = objectMapper.readTree(json);
			JsonNode dryRunNode = rootNode.path("dryrun");
			dryrun = dryRunNode.asBoolean();

			rootNode = objectMapper.readTree(json);
			JsonNode saltNode = rootNode.path("saltJSON");
			salt = Salt.fromJsonToSalt(saltNode.toString());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<String>(e.toString(), headers, HttpStatus.BAD_REQUEST);
		}

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
		if (validSalt & !dryrun) {
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
		} else if (salt.getCdId() > 0 || (validSalt & dryrun)) { // Continue If Valid Registered or Valid Salt in Dryrun
			String saltStr = salt.toJson();
			if(!dryrun) {
				salt.persist();
			}
			else
			{
				// Need to Manually Calculate These Properties to Check w/ User to Confirm 
				// These Are Normally Calculated During Registration Process
				salt.setFormula(saltStructureService.calculateFormula(salt));
				salt.setMolWeight(saltStructureService.calculateWeight(salt));
				salt.setCharge(saltStructureService.calculateCharge(salt));;
				saltStr = salt.toJson();
			}
			return new ResponseEntity<String>(saltStr, headers, HttpStatus.OK);
		} else {
			ErrorMessage error = new ErrorMessage();
			error.setLevel("warning");
			error.setMessage("Duplicate salt found. Please select existing salt.");
			errors.add(error);
			return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.CONFLICT);
		}
	}

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
