package com.labsynch.labseer.api;

import java.util.Collection;
import java.util.HashMap;

import flexjson.JSONSerializer;

import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ParentEditDTO;
import com.labsynch.labseer.dto.ParentSwapStructuresDTO;
import com.labsynch.labseer.dto.ParentValidationDTO;
import com.labsynch.labseer.service.ParentService;
import com.labsynch.labseer.service.ParentSwapStructuresService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = { "/api/v1/parents" })
@Controller
public class ApiParentController {

	Logger logger = LoggerFactory.getLogger(ApiParentController.class);

	@Autowired
	public ParentService parentService;

	@Autowired
	public ParentSwapStructuresService parentSwapStructuresService;

	@Transactional
	@RequestMapping(value = "/validateParent", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> validateParent(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			Parent queryParent = Parent.fromJsonToParent(json);
			ParentValidationDTO validationDTO = parentService.validateUniqueParent(queryParent);
			if (validationDTO.isParentUnique() && validationDTO.getErrors().isEmpty()) {
				return new ResponseEntity<String>(CodeTableDTO.toJsonArray(validationDTO.getAffectedLots()), headers,
						HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(validationDTO.toJson(), headers, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			logger.error("Caught error trying to validate parent", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/updateParent", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> updateParent(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			Parent parent = Parent.fromJsonToParent(json);
			Collection<CodeTableDTO> affectedLots = parentService.updateParent(parent);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(affectedLots), headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught error trying to update parent", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/swapParentStructures", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> swapParentStructures(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		ParentSwapStructuresDTO parentSwapStructuresDTO = ParentSwapStructuresDTO.fromJSONToParentSwapStructuresDTO(json);

		String errorMessage;
		try {
			errorMessage = parentSwapStructuresService.swapParentStructures(parentSwapStructuresDTO);
		} catch (Exception e) {
			logger.error("Caught error trying to swap parent structures", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		boolean hasError = !errorMessage.isEmpty();
		HashMap<String,Object> response = new HashMap<>();
		response.put("hasError", hasError);
		response.put("errorMessage", errorMessage);
		HttpStatus status = (hasError)? HttpStatus.BAD_REQUEST : HttpStatus.OK;
		
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(new JSONSerializer().serialize(response), headers, status);
}

	@RequestMapping(value = "/updateParent/metadata/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> updateParentMetaArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			String results = parentService.updateParentMetaArray(json);
			return new ResponseEntity<String>(results, headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught error trying to update parent", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Transactional
	@RequestMapping(value = "/updateParent/metadata", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> updateParentMeta(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		try {
			ParentEditDTO parentDTO = ParentEditDTO.fromJsonToParentEditDTO(json);
			Parent parent = parentService.updateParentMeta(parentDTO);
			return new ResponseEntity<String>(parent.toJson(), headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Caught error trying to update parent", e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
