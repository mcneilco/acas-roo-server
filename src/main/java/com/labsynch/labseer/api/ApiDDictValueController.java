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

import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.service.DataDictionaryService;
import com.labsynch.labseer.utils.SimpleUtil;

@Transactional
@RequestMapping("api/v1/ddictvalues")
@Controller


public class ApiDDictValueController {

	private static final Logger logger = LoggerFactory.getLogger(ApiDDictValueController.class);

	@Autowired
	private DataDictionaryService dataDictionaryService;

	@RequestMapping(value = "/getvalues/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<String> findByTypeAndKind(
			@PathVariable (value = "format") String format,
			@RequestParam (value = "lsType", required = false) String lsType,
			@RequestParam (value = "lsKind", required = false) String lsKind) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		logger.debug("hit the controller: " + lsType);

		List<DDictValue> ddictValues = null;

		if (lsType != null && lsKind == null){
			ddictValues = DDictValue.findDDictValuesByLsTypeEquals(lsType).getResultList();
		} else if (lsType == null && lsKind != null){
			ddictValues = DDictValue.findDDictValuesByLsKindEquals(lsKind).getResultList();
		} else if (lsType != null && lsKind != null){
			ddictValues = DDictValue.findDDictValuesByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList();
		} else {
			ddictValues = DDictValue.findAllDDictValues();
		}

		if(format.equalsIgnoreCase("codeTable")) {
			List<CodeTableDTO> results = dataDictionaryService.convertToCodeTables(ddictValues); 
			results = CodeTableDTO.sortCodeTables(results);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(results), headers, HttpStatus.OK);
		} else if (format.equalsIgnoreCase("csv")) {
			String outputString = dataDictionaryService.getCsvList(ddictValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			//default format is json
			return new ResponseEntity<String>(DDictValue.toJsonArray(ddictValues), headers, HttpStatus.OK);
		}
	}


	@Transactional
	@RequestMapping(method = RequestMethod.GET, value = "/{idOrCodeName}", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJson(
			@PathVariable("idOrCodeName") String idOrCodeName,
			@RequestParam (value = "format", required = false) String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		
		DDictValue dDictValue = null;
		List<DDictValue> dDictValues = null;
		if (SimpleUtil.isNumeric(idOrCodeName)){
			dDictValue = DDictValue.findDDictValue(Long.valueOf(idOrCodeName));
		} else {
			dDictValues = DDictValue.findDDictValuesByCodeNameEquals(idOrCodeName).getResultList();
			if (dDictValues.size() == 1){
				dDictValue = dDictValues.get(0);
				logger.info("found a value " + dDictValue.toJson());
			} else if (dDictValues.size() > 1) {
				logger.error("found multiple entries");
			}
		}

		if (dDictValue == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		if(format != null &&  format.equalsIgnoreCase("codeTable")) {
			CodeTableDTO results = dataDictionaryService.getDataDictionaryCodeTable(dDictValue); 
			return new ResponseEntity<String>(results.toJson(), headers, HttpStatus.OK);
		} else if (format != null &&  format.equalsIgnoreCase("csv")){
			dDictValues.add(dDictValue);
			String outputString = dataDictionaryService.getCsvList(dDictValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			logger.info("default fromat is JSON " + dDictValue.toJson());
			return new ResponseEntity<String>(dDictValue.toJson(), headers, HttpStatus.OK);
		}
	}

	@Transactional
	@RequestMapping(method = RequestMethod.GET, value = "/{idOrCodeName}/{format}", headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> showJsonWithFormat(
			@PathVariable("idOrCodeName") String idOrCodeName,
			@PathVariable (value = "format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		
		DDictValue dDictValue = null;
		List<DDictValue> dDictValues = null;
		if (SimpleUtil.isNumeric(idOrCodeName)){
			dDictValue = DDictValue.findDDictValue(Long.valueOf(idOrCodeName));
		} else {
			dDictValues = DDictValue.findDDictValuesByCodeNameEquals(idOrCodeName).getResultList();
			if (dDictValues.size() == 1){
				dDictValue = dDictValues.get(0);
				logger.info("found a value " + dDictValue.toJson());
			} else if (dDictValues.size() > 1) {
				logger.error("found multiple entries");
			}
		}

		if (dDictValue == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}

		if(format != null &&  format.equalsIgnoreCase("codeTable")) {
			CodeTableDTO results = dataDictionaryService.getDataDictionaryCodeTable(dDictValue); 
			return new ResponseEntity<String>(results.toJson(), headers, HttpStatus.OK);
		} else if (format != null &&  format.equalsIgnoreCase("csv")){
			dDictValues.add(dDictValue);
			String outputString = dataDictionaryService.getCsvList(dDictValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			logger.info("default fromat is JSON " + dDictValue.toJson());
			return new ResponseEntity<String>(dDictValue.toJson(), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJson(
			@RequestBody DDictValue dDictValue,
			@RequestParam(value = "createTypeKind", required = false) String createTypeKindString) {
		
		Boolean createTypeKind = false;
		if (createTypeKindString.equalsIgnoreCase("true")) createTypeKind = true;
		dDictValue = dataDictionaryService.saveDataDictionaryValue(dDictValue, createTypeKind);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");		
		if (dDictValue == null){
			return new ResponseEntity<String>("ERROR: Multiple DDictValue already exists", headers, HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<String>(dDictValue.toJson(), headers, HttpStatus.CREATED);
		} 
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createFromJsonArray(
			@RequestBody Collection<DDictValue> dDictValues,
			@RequestParam(value = "createTypeKind", required = false) String createTypeKindString) {
		
		Boolean createTypeKind = false;
		if (createTypeKindString.equalsIgnoreCase("true")) createTypeKind = true;
		Collection<DDictValue> savedDDictValues = dataDictionaryService.saveDataDictionaryValues(dDictValues, createTypeKind);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(DDictValue.toJsonArray(savedDDictValues), headers, HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "/codetable/{lsType}/{lsKind}", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createCodeTableFromJson(
			@RequestBody CodeTableDTO codeTableDTO,
			@PathVariable("lsType") String lsType, 
			@PathVariable("lsKind") String lsKind,
			@RequestParam(value = "createTypeKind", required = false) String createTypeKindString) {
		
		Boolean createTypeKind = false;
		if (createTypeKindString.equalsIgnoreCase("true")) createTypeKind = true;
		
		CodeTableDTO codeTableValue = dataDictionaryService.saveCodeTableValue(lsType, lsKind, codeTableDTO, createTypeKind);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (codeTableValue == null){
			return new ResponseEntity<String>("ERROR: unable to create new entry", headers, HttpStatus.BAD_REQUEST);			
		} 
		return new ResponseEntity<String>(codeTableValue.toJson(), headers, HttpStatus.CREATED);			

	}

	@RequestMapping(value = "/codetable/{lsType}/{lsKind}/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> createCodeTablesFromJsonArray(@RequestBody List<CodeTableDTO> codeTableDTOs,
			@PathVariable("lsType") String lsType, 
			@PathVariable("lsKind") String lsKind,
			@RequestParam(value = "createTypeKind", required = false) String createTypeKindString) {
		
		Boolean createTypeKind = false;
		if (createTypeKindString.equalsIgnoreCase("true")) createTypeKind = true;
		
		Collection<CodeTableDTO> savedCodeTableValues = dataDictionaryService.saveCodeTableValueArray(lsType, lsKind, codeTableDTOs, createTypeKind);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<String>(CodeTableDTO.toJsonArray(savedCodeTableValues), headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/codetable/{lsType}/{lsKind}/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateCodeTableFromJson(@RequestBody CodeTableDTO codeTableDTO,
			@PathVariable("lsType") String lsType, 
			@PathVariable("lsKind") String lsKind) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		CodeTableDTO codeTableValue = dataDictionaryService.updateCodeTableValue(lsType, lsKind, codeTableDTO);
		if (codeTableValue == null){
			return new ResponseEntity<String>("ERROR: unable to update entry", headers, HttpStatus.NOT_FOUND);			
		} 
		return new ResponseEntity<String>(codeTableValue.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/codetable/{lsType}/{lsKind}/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateCodeTablesFromJsonArray(@RequestBody List<CodeTableDTO> codeTableDTOs,
			@PathVariable("lsType") String lsType, 
			@PathVariable("lsKind") String lsKind) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		Collection<CodeTableDTO> updatedCodeTableValues = dataDictionaryService.updateCodeTableValueArray(lsType, lsKind, codeTableDTOs);
		if (updatedCodeTableValues == null){
			return new ResponseEntity<String>("ERROR: unable to update entry", headers, HttpStatus.NOT_FOUND);			
		} 
		return new ResponseEntity<String>(CodeTableDTO.toJsonArray(updatedCodeTableValues), headers, HttpStatus.OK);
	}




	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJson(@RequestBody DDictValue dDictValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (dDictValue.merge() == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<String>(dDictValue.toJson(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		Collection<DDictValue> savedDDictValues = new ArrayList<DDictValue>();
		for (DDictValue dDictValue: DDictValue.fromJsonArrayToDDictValues(json)) {
			if (dDictValue.merge() == null) {
				return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
			}
			savedDDictValues.add(dDictValue);
		}
		return new ResponseEntity<String>(DDictValue.toJsonArray(savedDDictValues), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
		DDictValue dDictValue = DDictValue.findDDictValue(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (dDictValue == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		dDictValue.remove();
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}


	@RequestMapping(value = "", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJson(
			@RequestParam (value = "format", required = false) String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<DDictValue> dDictResults = DDictValue.findAllDDictValues();

		if(format != null && format.equalsIgnoreCase("codeTable")) {
			List<CodeTableDTO> codeTables = dataDictionaryService.convertToCodeTables(dDictResults);
			codeTables = CodeTableDTO.sortCodeTables(codeTables);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(codeTables), headers, HttpStatus.OK);
		} else if (format != null && format.equalsIgnoreCase("csv")) {
			String outputString = dataDictionaryService.getCsvList(dDictResults);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(DDictValue.toJsonArray(dDictResults), headers, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> listJsonWithFormat(
			@PathVariable (value = "format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		List<DDictValue> dDictResults = DDictValue.findAllDDictValues();

		if(format != null && format.equalsIgnoreCase("codeTable")) {
			List<CodeTableDTO> codeTables = dataDictionaryService.convertToCodeTables(dDictResults);
			codeTables = CodeTableDTO.sortCodeTables(codeTables);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(codeTables), headers, HttpStatus.OK);
		} else if (format != null && format.equalsIgnoreCase("csv")) {
			String outputString = dataDictionaryService.getCsvList(dDictResults);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(DDictValue.toJsonArray(dDictResults), headers, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/all/{lsType}/{lsKind}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> getDDictValuesByTypeKindFormat(
			@PathVariable("lsType") String lsType, 
			@PathVariable("lsKind") String lsKind, 
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		if (lsKind == null || lsType == null) {
			return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
		}

		List<DDictValue> dDictResults = DDictValue.findDDictValuesByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList();

		if(format != null && format.equalsIgnoreCase("codeTable")) {
			List<CodeTableDTO> codeTables = dataDictionaryService.convertToCodeTables(dDictResults);
			codeTables = CodeTableDTO.sortCodeTables(codeTables);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(codeTables), headers, HttpStatus.OK);
		} else if (format != null && format.equalsIgnoreCase("csv")) {
			String outputString = dataDictionaryService.getCsvList(dDictResults);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(DDictValue.toJsonArray(dDictResults), headers, HttpStatus.OK);
		}
	}


	@RequestMapping(value = "/bytype/{lsType}/{format}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<java.lang.String> getDDictValuesByTypeFormat(
			@PathVariable("lsType") String lsType, 
			@PathVariable("format") String format) {

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");

		if (lsType == null) {
			return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
		}

		List<DDictValue> dDictResults = DDictValue.findDDictValuesByLsTypeEquals(lsType).getResultList();

		if(format != null && format.equalsIgnoreCase("codeTable")) {
			List<CodeTableDTO> results = dataDictionaryService.getDataDictionaryCodeTableListByType(lsType); 
			results = CodeTableDTO.sortCodeTables(results);
			return new ResponseEntity<String>(CodeTableDTO.toJsonArray(results), headers, HttpStatus.OK);
		} else if (format != null && format.equalsIgnoreCase("csv")) {
			String outputString = dataDictionaryService.getCsvList(dDictResults);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			// if user wants to know an error code for empty results; user will normally inspect the size of the result array to on their end
			//			if(dDictResults.size() == 0) {
			//				return new ResponseEntity<String>(DDictValue.toJsonArray(dDictResults), headers, HttpStatus.NO_CONTENT);
			//			}
			return new ResponseEntity<String>(DDictValue.toJsonArray(dDictResults), headers, HttpStatus.OK);
		}

	}

}


