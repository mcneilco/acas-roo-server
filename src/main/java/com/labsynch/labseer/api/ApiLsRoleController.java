package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.LsRole;
import com.labsynch.labseer.dto.CodeTableDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/v1/lsRoles")
public class ApiLsRoleController {

	private static final Logger logger = LoggerFactory.getLogger(ApiLsRoleController.class);

	//CRUD routes
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> getById(@PathVariable("id") Long id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		LsRole result = LsRole.findLsRole(id);
		return new ResponseEntity<String>(result.toJson(), headers, HttpStatus.OK);
	}
	
	@RequestMapping(headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(@RequestParam(value="lsType", required=false) String lsType,
			@RequestParam(value="lsKind", required=false) String lsKind,
			@RequestParam(value="format", required=false) String format) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<LsRole> allLsRoles;
		if (lsType != null && lsKind != null) {
			allLsRoles = LsRole.findLsRolesByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList();
		}else if (lsType != null) {
			allLsRoles = LsRole.findLsRolesByLsTypeEquals(lsType).getResultList();
		}else if (lsKind != null) {
			allLsRoles = LsRole.findLsRolesByLsKindEquals(lsKind).getResultList();
		}else {
			allLsRoles = LsRole.findAllLsRoles();
		}
		if (format != null && format.equalsIgnoreCase("codetable")) {
			Collection<CodeTableDTO> codeTables = LsRole.toCodeTables(allLsRoles);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(codeTables), headers, HttpStatus.OK);
		}
		return new ResponseEntity<String>(LsRole.toJsonArray(allLsRoles), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			LsRole lsRole = LsRole.fromJsonToLsRole(json);
			lsRole.persist();
			return new ResponseEntity<String>(lsRole.toJson(), headers, HttpStatus.CREATED);
		}catch (Exception e){
			logger.error("Caught exception saving lsRole",e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			Collection<LsRole> lsRoles = LsRole.fromJsonArrayToLsRoles(json);
			Collection<LsRole> savedLsRoles = new ArrayList<LsRole>();
			for (LsRole lsRole : lsRoles) {
				lsRole.persist();
				savedLsRoles.add(lsRole);
			}
			return new ResponseEntity<String>(LsRole.toJsonArray(savedLsRoles), headers, HttpStatus.CREATED);
		}catch (Exception e){
			logger.error("Caught exception saving lsRole",e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Transactional
	@RequestMapping(value = { "/{id}", "/" }, method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
		LsRole lsRole = LsRole.fromJsonToLsRole(json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		lsRole = lsRole.merge();
		if (lsRole.getId() == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(lsRole.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			Collection<LsRole> lsRoles = LsRole.fromJsonArrayToLsRoles(json);
			Collection<LsRole> updatedLsRoles = new ArrayList<LsRole>();
			for (LsRole lsRole : lsRoles) {
				LsRole updatedLsRole = lsRole.merge();
				updatedLsRoles.add(updatedLsRole);
			}
			return new ResponseEntity<String>(LsRole.toJsonArray(updatedLsRoles), headers, HttpStatus.OK);
		}catch (Exception e){
			logger.error("Caught exception updating lsRoles",e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
		LsRole lsRole = LsRole.findLsRole(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (lsRole == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		lsRole.remove();
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}
}

