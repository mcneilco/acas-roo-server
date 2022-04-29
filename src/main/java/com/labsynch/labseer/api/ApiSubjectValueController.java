package com.labsynch.labseer.api;

import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Transactional
@RequestMapping("api/v1/subjectvalues")
@Controller

public class ApiSubjectValueController {

    private static final Logger logger = LoggerFactory.getLogger(ApiSubjectValueController.class);

    @RequestMapping(value = "/updatevalue/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<String> updateSubjectValue(
            @RequestParam("lsType") String lsType,
            @RequestParam("lsValue") String lsValue,
            @PathVariable("id") Long id) {
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

    @RequestMapping(params = "find=ByCodeValueEquals", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindSubjectValuesByCodeValueEquals(@RequestParam("codeValue") String codeValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(
                SubjectValue.toJsonArray(SubjectValue.findSubjectValuesByCodeValueEquals(codeValue).getResultList()),
                headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByIgnoredNotAndCodeValueEquals", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindSubjectValuesByIgnoredNotAndCodeValueEquals(
            @RequestParam(value = "ignored", required = false) boolean ignored,
            @RequestParam("codeValue") String codeValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(
                SubjectValue.toJsonArray(SubjectValue
                        .findSubjectValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue).getResultList()),
                headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByLsState", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindSubjectValuesByLsState(@RequestParam("lsState") SubjectState lsState) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(
                SubjectValue.toJsonArray(SubjectValue.findSubjectValuesByLsState(lsState).getResultList()), headers,
                HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindSubjectValuesByLsTypeEqualsAndLsKindEquals(
            @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(
                SubjectValue.toJsonArray(
                        SubjectValue.findSubjectValuesByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList()),
                headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByLsStateAndLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindSubjectValuesByLsStateAndLsTypeEqualsAndLsKindEquals(
            @RequestParam("lsState") SubjectState lsState, @RequestParam("lsType") String lsType,
            @RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(SubjectValue.toJsonArray(SubjectValue
                .findSubjectValuesByLsStateAndLsTypeEqualsAndLsKindEquals(lsState, lsType, lsKind).getResultList()),
                headers, HttpStatus.OK);
    }
}
