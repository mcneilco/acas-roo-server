package com.labsynch.labseer.api;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.IdSetDTO;
import com.labsynch.labseer.service.AnalysisGroupValueService;

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
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import flexjson.JSONDeserializer;

@Controller
@RequestMapping("api/v1/analysisgroupvalues")
@Transactional
public class ApiAnalysisGroupValueController {

	private static final Logger logger = LoggerFactory.getLogger(ApiAnalysisGroupValueController.class);

	@Autowired
	private AnalysisGroupValueService analysisGroupValueService;

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
		AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.findAnalysisGroupValue(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (analysisGroupValue == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		analysisGroupValue.remove();
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

	@RequestMapping(params = "find=ByCodeValueEquals", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonFindAnalysisGroupValuesByCodeValueEquals(
			@RequestParam("codeValue") String codeValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(
				AnalysisGroupValue.toJsonArray(
						AnalysisGroupValue.findAnalysisGroupValuesByCodeValueEquals(codeValue).getResultList()),
				headers, HttpStatus.OK);
	}

	@RequestMapping(params = "find=ByIgnoredNotAndCodeValueEquals", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonFindAnalysisGroupValuesByIgnoredNotAndCodeValueEquals(
			@RequestParam(value = "ignored", required = false) boolean ignored,
			@RequestParam("codeValue") String codeValue) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(
				AnalysisGroupValue.toJsonArray(AnalysisGroupValue
						.findAnalysisGroupValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue).getResultList()),
				headers, HttpStatus.OK);
	}

	@RequestMapping(params = "find=ByLsState", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonFindAnalysisGroupValuesByLsState(
			@RequestParam("lsState") AnalysisGroupState lsState) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(
				AnalysisGroupValue
						.toJsonArray(AnalysisGroupValue.findAnalysisGroupValuesByLsState(lsState).getResultList()),
				headers, HttpStatus.OK);
	}

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonFindAnalysisGroupValuesByLsTransactionEquals(
			@RequestParam("lsTransaction") Long lsTransaction) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(
				AnalysisGroupValue.toJsonArray(
						AnalysisGroupValue.findAnalysisGroupValuesByLsTransactionEquals(lsTransaction).getResultList()),
				headers, HttpStatus.OK);
	}

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public ResponseEntity<String> jsonFindAnalysisGroupValuesByLsTypeEqualsAndLsKindEquals(
			@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		return new ResponseEntity<String>(
				AnalysisGroupValue.toJsonArray(AnalysisGroupValue
						.findAnalysisGroupValuesByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList()),
				headers, HttpStatus.OK);
	}

	// custom code for gene ID queries
	@RequestMapping(value = "/geneCodeData", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> getGeneCodeData(
			@RequestBody String json,
			@RequestParam(value = "format", required = false) String format,
			@RequestParam(value = "onlyPublicData", required = false) String onlyPublicData) {
		logger.debug("incoming json: " + json);
		Collection<String> batchCodes = new JSONDeserializer<List<String>>().use(null, ArrayList.class)
				.use("values", String.class).deserialize(json);
		Set<String> geneCodeList = new HashSet<String>();
		geneCodeList.addAll(batchCodes);

		Boolean publicData = false;
		if (onlyPublicData != null && onlyPublicData.equalsIgnoreCase("true")) {
			publicData = true;
		}

		List<AnalysisGroupValueDTO> agValues = null;
		try {
			if (publicData) {
				agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(geneCodeList, publicData).getResultList();
			} else {
				agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(geneCodeList).getResultList();
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (agValues == null || agValues.size() == 0) {
			return new ResponseEntity<String>("[]", headers, HttpStatus.EXPECTATION_FAILED);
		}
		if (format != null && format.equalsIgnoreCase("csv")) {
			return new ResponseEntity<String>(AnalysisGroupValueDTO.toCsv(agValues), headers, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(AnalysisGroupValueDTO.toJsonArray(agValues), headers, HttpStatus.OK);
		}
	}

	// custom code for agv queries
	@Transactional
	@RequestMapping(value = "/getValues/byIdList", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> getAgValues(
			@RequestBody String json,
			@RequestParam(value = "format", required = false) String format,
			@RequestParam(value = "onlyPublicData", required = false) String onlyPublicData) {
		logger.debug("incoming json: " + json);
		IdSetDTO idSet = IdSetDTO.fromJsonToIdSetDTO(json);
		boolean onlyPublicDataBoolean = false;
		if (onlyPublicData.trim().equalsIgnoreCase("true")) {
			onlyPublicDataBoolean = true;
		}
		List<AnalysisGroupValue> agValues = AnalysisGroupValue
				.findAnalysisGroupValuesByIdList(idSet.getIdSet(), onlyPublicDataBoolean).getResultList();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		if (format.equalsIgnoreCase("tsv")) {
			String outputString = analysisGroupValueService.getCsvList(agValues);
			return new ResponseEntity<String>(outputString, headers, HttpStatus.OK);
		} else {
			// default format is json
			return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(agValues), headers, HttpStatus.OK);
		}
	}
}
