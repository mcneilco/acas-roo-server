package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.KeyValueDTO;

@Transactional
@RequestMapping("api/v1/ddictvalues")
@Controller
@RooWebFinder
@RooWebJson(jsonObject = DDictValue.class)

public class ApiDDictValueController {

	private static final Logger logger = LoggerFactory.getLogger(ApiDDictValueController.class);

    @RequestMapping(value = "/getvalues/{format}", method = RequestMethod.POST, headers = "Accept=application/json")
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
        
        if (format.equalsIgnoreCase("json")){
            return new ResponseEntity<String>(DDictValue.toJsonArray(ddictValues), headers, HttpStatus.OK);
        } else if (format.equalsIgnoreCase("list")){
        	List<KeyValueDTO> lsValues = new ArrayList<KeyValueDTO>();
        	for (DDictValue ddict : ddictValues){
        		KeyValueDTO kvDTO = new KeyValueDTO();
        		kvDTO.setKey("lsValue");
        		kvDTO.setValue(ddict.getLabelText());
        		lsValues.add(kvDTO);
        	}
        	return new ResponseEntity<String>(KeyValueDTO.toJsonArray(lsValues), headers, HttpStatus.OK);
        	
        } else {
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        }

    }


	@RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        DDictValue DDictValue_ = DDictValue.findDDictValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (DDictValue_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(DDictValue_.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<DDictValue> result = DDictValue.findAllDDictValues();
        return new ResponseEntity<String>(DDictValue.toJsonArray(result), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        DDictValue DDictValue_ = DDictValue.fromJsonToDDictValue(json);
        DDictValue_.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (DDictValue DDictValue_: DDictValue.fromJsonArrayToDDictValues(json)) {
            DDictValue_.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        DDictValue DDictValue_ = DDictValue.fromJsonToDDictValue(json);
        if (DDictValue_.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (DDictValue DDictValue_: DDictValue.fromJsonArrayToDDictValues(json)) {
            if (DDictValue_.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        DDictValue DDictValue_ = DDictValue.findDDictValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (DDictValue_ == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        DDictValue_.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindDDictValuesByLsKindEquals(@RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DDictValue.toJsonArray(DDictValue.findDDictValuesByLsKindEquals(lsKind).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByLsTypeEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindDDictValuesByLsTypeEquals(@RequestParam("lsType") String lsType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DDictValue.toJsonArray(DDictValue.findDDictValuesByLsTypeEquals(lsType).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindDDictValuesByLsTypeEqualsAndLsKindEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(DDictValue.toJsonArray(DDictValue.findDDictValuesByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList()), headers, HttpStatus.OK);
    }
	
    @Transactional
    @RequestMapping(value = "/codetable", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJsonCodeTable(
    		@RequestParam(value = "with", required = false) String with, 
    		@RequestParam(value = "prettyjson", required = false) String prettyjson, 
    		@RequestParam(value = "lstype", required = false) String lsType, 
    		@RequestParam(value = "lskind", required = false) String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<CodeTableDTO> result;
        if (lsKind != null) {
            result = DDictValue.getDDictValueCodeTableByKindEquals(lsKind);
        } else {
            result = DDictValue.getDDictCodeTable();
        }
        return new ResponseEntity<String>(CodeTableDTO.toJsonArray(result), headers, HttpStatus.OK);
    }
}
