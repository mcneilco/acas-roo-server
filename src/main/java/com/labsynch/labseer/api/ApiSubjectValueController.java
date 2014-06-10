package com.labsynch.labseer.api;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;

@Transactional
@RequestMapping("api/v1/subjectvalues")
@Controller
@RooWebJson(jsonObject = SubjectValue.class)

public class ApiSubjectValueController {

    private static final Logger logger = LoggerFactory.getLogger(ApiSubjectValueController.class);
    
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        SubjectValue subjectValue = SubjectValue.findSubjectValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (subjectValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(subjectValue.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<SubjectValue> result = SubjectValue.findAllSubjectValues();
        return new ResponseEntity<String>(SubjectValue.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        SubjectValue subjectValue = SubjectValue.fromJsonToSubjectValue(json);
        subjectValue.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (SubjectValue subjectValue: SubjectValue.fromJsonArrayToSubjectValues(json)) {
            subjectValue.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        SubjectValue subjectValue = SubjectValue.fromJsonToSubjectValue(json);
        if (subjectValue.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/updatevalue/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<String> updateSubjectValue(
            @RequestParam("lsType") String lsType,
            @RequestParam("lsValue") String lsValue,
            @PathVariable("id") Long id
            ) {
        logger.info("in updateSubjectValue");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        SubjectValue subjectValue = SubjectValue.findSubjectValue(id);
        subjectValue.setStringValue(lsValue);
        if (subjectValue.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(subjectValue.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (SubjectValue subjectValue: SubjectValue.fromJsonArrayToSubjectValues(json)) {
            if (subjectValue.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        SubjectValue subjectValue = SubjectValue.findSubjectValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (subjectValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        subjectValue.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByCodeValueEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindSubjectValuesByCodeValueEquals(@RequestParam("codeValue") String codeValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(SubjectValue.toJsonArray(SubjectValue.findSubjectValuesByCodeValueEquals(codeValue).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByIgnoredNotAndCodeValueEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindSubjectValuesByIgnoredNotAndCodeValueEquals(@RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam("codeValue") String codeValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(SubjectValue.toJsonArray(SubjectValue.findSubjectValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByLsState", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindSubjectValuesByLsState(@RequestParam("lsState") SubjectState lsState) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(SubjectValue.toJsonArray(SubjectValue.findSubjectValuesByLsState(lsState).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindSubjectValuesByLsTypeEqualsAndLsKindEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(SubjectValue.toJsonArray(SubjectValue.findSubjectValuesByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList()), headers, HttpStatus.OK);
    }
}
