package com.labsynch.labseer.api;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.LotAliasKind;
import com.labsynch.labseer.domain.LotAliasType;
import com.labsynch.labseer.domain.ParentAliasKind;
import com.labsynch.labseer.domain.ParentAliasType;
import com.labsynch.labseer.dto.LotAliasDTO;
import com.labsynch.labseer.dto.ParentAliasDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = { "/api/v1/aliases" })
@Controller
public class ApiAliasNameController {

	Logger logger = LoggerFactory.getLogger(ApiAliasNameController.class);

	@Transactional
	@RequestMapping(value = "/getParentByAliasName", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getParentByAliasName(@RequestParam("aliasName") String aliasName,
			@RequestParam("lsType") String lsType,
			@RequestParam("lsKind") String lsKind) {
		logger.debug("incoming json from getParentByAliasName: " + aliasName);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		ParentAliasDTO queryParentAlias = new ParentAliasDTO();
		queryParentAlias.setAliasName(aliasName);
		queryParentAlias.setLsType(lsType);
		queryParentAlias.setLsKind(lsKind);
		ParentAliasDTO parentAlias = ParentAliasDTO.getParentByAlias(queryParentAlias);
		if (parentAlias != null) {
			return new ResponseEntity<String>(parentAlias.toJson(), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("", headers, HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	@RequestMapping(value = "/getParentsByAliasName", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getParentsByAliasName(@RequestBody String json) {
		logger.debug("incoming json from getParentAliasName: " + json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<ParentAliasDTO> aliasNameDTOs = ParentAliasDTO.fromJsonArrayToParentAliasDTO(json);
		aliasNameDTOs = ParentAliasDTO.getParentsByAlias(aliasNameDTOs);
		return new ResponseEntity<String>(ParentAliasDTO.toJsonArray(aliasNameDTOs), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/parentAliasTypes", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getParentAliasTypes() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<ParentAliasType> aliasTypes = ParentAliasType.findAllParentAliasTypes();

		if (aliasTypes.size() > 0) {
			return new ResponseEntity<String>(ParentAliasType.toJsonArray(aliasTypes), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("[]", headers, HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	@RequestMapping(value = "/parentAliasKinds", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getParentAliasKinds() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<ParentAliasKind> aliasKinds = ParentAliasKind.findAllParentAliasKinds();

		if (aliasKinds.size() > 0) {
			return new ResponseEntity<String>(ParentAliasKind.toJsonArray(aliasKinds), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("[]", headers, HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	@RequestMapping(value = "/getLotByAliasName", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getLotByAliasName(@RequestParam("aliasName") String aliasName,
			@RequestParam("lsType") String lsType,
			@RequestParam("lsKind") String lsKind) {
		logger.debug("incoming json from getLotByAliasName: " + aliasName);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		LotAliasDTO queryLotAlias = new LotAliasDTO();
		queryLotAlias.setAliasName(aliasName);
		queryLotAlias.setLsType(lsType);
		queryLotAlias.setLsKind(lsKind);
		LotAliasDTO lotAlias = LotAliasDTO.getLotByAlias(queryLotAlias);
		if (lotAlias != null) {
			return new ResponseEntity<String>(lotAlias.toJson(), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("", headers, HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	@RequestMapping(value = "/getLotsByAliasName", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getLotsByAliasName(@RequestBody String json) {
		logger.debug("incoming json from getLotAliasName: " + json);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		Collection<LotAliasDTO> aliasNameDTOs = LotAliasDTO.fromJsonArrayToLoes(json);
		aliasNameDTOs = LotAliasDTO.getLotsByAlias(aliasNameDTOs);
		return new ResponseEntity<String>(LotAliasDTO.toJsonArray(aliasNameDTOs), headers, HttpStatus.OK);
	}

	@Transactional
	@RequestMapping(value = "/lotAliasTypes", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getLotAliasTypes() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<LotAliasType> aliasTypes = LotAliasType.findAllLotAliasTypes();

		if (aliasTypes.size() > 0) {
			return new ResponseEntity<String>(LotAliasType.toJsonArray(aliasTypes), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("[]", headers, HttpStatus.NOT_FOUND);
		}
	}

	@Transactional
	@RequestMapping(value = "/lotAliasKinds", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> getLotAliasKinds() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		List<LotAliasKind> aliasKinds = LotAliasKind.findAllLotAliasKinds();

		if (aliasKinds.size() > 0) {
			return new ResponseEntity<String>(LotAliasKind.toJsonArray(aliasKinds), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("[]", headers, HttpStatus.NOT_FOUND);
		}
	}

}
