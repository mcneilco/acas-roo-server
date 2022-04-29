package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.LabelSequenceDTO;
import com.labsynch.labseer.service.AutoLabelService;
import com.labsynch.labseer.service.LabelSequenceService;

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

@Controller
@RequestMapping("/api/v1/labelsequences")
public class ApiLabelSequenceController {

	private static final Logger logger = LoggerFactory.getLogger(ApiLabelSequenceController.class);

	@Autowired
	private AutoLabelService autoLabelService;

	@Autowired
	private LabelSequenceService labelSequenceService;

	//CRUD routes
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> getById(@PathVariable("id") Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		LabelSequence result = LabelSequence.findLabelSequence(id);
		return new ResponseEntity<String>(result.toJson(), headers, HttpStatus.OK);
	}


	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		LabelSequence labelSequence = LabelSequence.fromJsonToLabelSequence(json);
		Collection<LabelSequence> foundLabelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals(labelSequence.getThingTypeAndKind(), labelSequence.getLabelTypeAndKind(), labelSequence.getLabelPrefix()).getResultList();
		if (!foundLabelSequences.isEmpty()) {
			String message = "LabelSequence already exists! " + labelSequence.getLabelPrefix() +" "+labelSequence.getLabelTypeAndKind() + " " + labelSequence.getThingTypeAndKind();
			logger.warn(message);
			return new ResponseEntity<String>(message,headers, HttpStatus.BAD_REQUEST);
		}
		try{
			labelSequence = labelSequence.save();
			return new ResponseEntity<String>(labelSequence.toJson(), headers, HttpStatus.CREATED);
		}catch (Exception e){
			logger.error("Caught exception saving labelSequence",e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			Collection<LabelSequence> labelSequences = LabelSequence.fromJsonArrayToLabelSequences(json);
			Collection<LabelSequence> savedLabelSequences = new ArrayList<LabelSequence>();
			for (LabelSequence labelSequence : labelSequences) {
				Collection<LabelSequence> foundLabelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals(labelSequence.getThingTypeAndKind(), labelSequence.getLabelTypeAndKind(), labelSequence.getLabelPrefix()).getResultList();
				if (!foundLabelSequences.isEmpty()) {
					String message = "LabelSequence already exists! " + labelSequence.getLabelPrefix() +" "+labelSequence.getLabelTypeAndKind() + " " + labelSequence.getThingTypeAndKind();
					logger.warn(message);
				}else {
					LabelSequence savedLabelSequence = labelSequence.save();
					savedLabelSequences.add(savedLabelSequence);
				}	
			}
			return new ResponseEntity<String>(LabelSequence.toJsonArray(savedLabelSequences), headers, HttpStatus.CREATED);
		}catch (Exception e){
			logger.error("Caught exception saving labelSequence",e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Transactional
	@RequestMapping(value = { "/{id}", "/" }, method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
		LabelSequence labelSequence = LabelSequence.fromJsonToLabelSequence(json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		labelSequence = labelSequence.merge();
		if (labelSequence.getId() == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(labelSequence.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			Collection<LabelSequence> labelSequences = LabelSequence.fromJsonArrayToLabelSequences(json);
			Collection<LabelSequence> updatedLabelSequences = new ArrayList<LabelSequence>();
			for (LabelSequence labelSequence : labelSequences) {
				LabelSequence updatedLabelSequence = labelSequence.merge();
				updatedLabelSequences.add(updatedLabelSequence);
			}
			return new ResponseEntity<String>(LabelSequence.toJsonArray(updatedLabelSequences), headers, HttpStatus.OK);
		}catch (Exception e){
			logger.error("Caught exception updating labelSequences",e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
		LabelSequence labelSequence = LabelSequence.findLabelSequence(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (labelSequence == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		labelSequence.remove();
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	//copied only the custom methods from the LabelSequenceController.java class
	@RequestMapping(value = "/getNextLabelSequences", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> updateLabelSequence(@RequestBody LabelSequenceDTO lsDTO) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		logger.info("incoming label seq: " + lsDTO.toJson());
		List<LabelSequence> labelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(lsDTO.getThingTypeAndKind(), lsDTO.getLabelTypeAndKind()).getResultList();
		if (labelSequences.size() != 1) {
			logger.info("did not find the label seq!!! ");
			return new ResponseEntity<String>(headers, HttpStatus.NOT_ACCEPTABLE);
		}
		LabelSequence labelSequence = LabelSequence.findLabelSequence(labelSequences.get(0).getId());
		labelSequence.generateNextLabels(lsDTO.getNumberOfLabels());
		return new ResponseEntity<String>(labelSequence.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/getLabels", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> getAutoLabels(@RequestBody LabelSequenceDTO lsDTO) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		List<AutoLabelDTO> autoLabels;
		try {
			autoLabels = autoLabelService.getAutoLabels(lsDTO);
		} catch (NonUniqueResultException e) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_ACCEPTABLE);
		}
		return new ResponseEntity<String>(AutoLabelDTO.toJsonArray(autoLabels), headers, HttpStatus.OK);
	}

	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(@RequestParam(value="thingTypeAndKind", required=false) String thingTypeAndKind,
			@RequestParam(value="labelTypeAndKind", required=false) String labelTypeAndKind) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<LabelSequence> allLabelSequences;
		if (thingTypeAndKind != null && labelTypeAndKind != null) {
			allLabelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList();
		}else if (thingTypeAndKind != null) {
			allLabelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEquals(thingTypeAndKind).getResultList();
		}else if (labelTypeAndKind != null) {
			allLabelSequences = LabelSequence.findLabelSequencesByLabelTypeAndKindEquals(labelTypeAndKind).getResultList();
		}else {
			allLabelSequences = LabelSequence.findAllLabelSequences();
		}
		return new ResponseEntity<String>(LabelSequence.toJsonArray(allLabelSequences), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/getNextLabelSequences", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> jsonFindLabelSequencesByThingAndLabel(@RequestParam("thingTypeAndKind") String thingTypeAndKind, @RequestParam("labelTypeAndKind") String labelTypeAndKind, @RequestParam("numberOfLabels") long numberOfLabels) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<LabelSequence> labelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList();
		if (labelSequences.size() != 1) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_ACCEPTABLE);
		}
		LabelSequence labelSequence = LabelSequence.findLabelSequence(labelSequences.get(0).getId());
		labelSequence.generateNextLabels(numberOfLabels);
		return new ResponseEntity<String>(labelSequence.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/getAuthorizedLabelSequences", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<java.lang.String> getAuthorizedLabelSequences(@RequestParam(value="thingTypeAndKind", required=false) String thingTypeAndKind, 
			@RequestParam(value="labelTypeAndKind", required=false) String labelTypeAndKind,
			@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<AuthorRole> authorRoles = AuthorRole.fromJsonArrayToAuthorRoles(json);
		List<LabelSequence> labelSequences = labelSequenceService.getAuthorizedLabelSequences(authorRoles, thingTypeAndKind, labelTypeAndKind);
		return new ResponseEntity<String>(LabelSequence.toJsonArray(labelSequences), headers, HttpStatus.OK);
	}
}

