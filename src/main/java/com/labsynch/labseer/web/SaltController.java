package com.labsynch.labseer.web;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
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

import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StructureSaveException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.service.ErrorMessage;
import com.labsynch.labseer.service.SaltStructureService;
import com.labsynch.labseer.utils.MoleculeUtil;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.path.PathBuilder;

@RooWebScaffold(path = "salts", formBackingObject = Salt.class)
@RequestMapping("/salts")
@Controller
@Transactional

@RooWebFinder
public class SaltController {

	private static final Logger logger = LoggerFactory.getLogger(SaltController.class);

	@Autowired
	private SaltStructureService saltStructureService;

	@Autowired
	private ChemStructureService chemStructureService;

	@RequestMapping(method = RequestMethod.POST)
	public String create(@Valid Salt salt, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws CmpdRegMolFormatException {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("salt", salt);
			return "salts/create";
		}
		uiModel.asMap().clear();
		salt = saltStructureService.saveStructure(salt);
		if (salt.getCdId() > 0 && salt.getCdId() != -1) {
			salt.persist();
			return "redirect:/salts/" + encodeUrlPathSegment(salt.getId().toString(), httpServletRequest);
		} else {
			//remove the salt from the Salt_Structure table??
			uiModel.addAttribute("salt", salt);
			return "salts/create";
		}
	}

	@ModelAttribute("salts")
	public Collection<Salt> populateSalts() {
		return Salt.findAllSalts();
	}

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {
		}
		return pathSegment;
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
			salt = saltStructureService.saveStructure(salt);
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

	@RequestMapping(method = RequestMethod.OPTIONS)
	public ResponseEntity<String> getSaltOptions() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/text, text/html");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Max-Age", "86400");
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String update(@Valid Salt salt, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			uiModel.addAttribute("salt", salt);
			return "salts/update";
		}
		uiModel.asMap().clear();
		try {
			logger.debug("Salt weight: " + chemStructureService.getMolWeight(salt.getMolStructure()));
		} catch (CmpdRegMolFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Salt updatedSalt = null;
		try{
			updatedSalt = saltStructureService.update(salt);
		}catch (CmpdRegMolFormatException e) {
			logger.error("Bad mol format",e);
			updatedSalt = null;
		}
		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		if (updatedSalt == null) {
			ErrorMessage error = new ErrorMessage();
			error.setLevel("error");
			error.setMessage("Bad molformat. Please fix the molfile: " + salt.getMolStructure());
			errors.add(error);
			return "redirect:/salts/";
		}
		return "redirect:/salts/" + encodeUrlPathSegment(salt.getId().toString(), httpServletRequest);
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
