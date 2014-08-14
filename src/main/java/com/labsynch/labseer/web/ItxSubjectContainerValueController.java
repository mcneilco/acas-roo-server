package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxSubjectContainerValue;
import com.labsynch.labseer.utils.PropertiesUtilService;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = ItxSubjectContainerValue.class)
@Controller
@RequestMapping("/itxsubjectcontainervalues")
@RooWebScaffold(path = "itxsubjectcontainervalues", formBackingObject = ItxSubjectContainerValue.class)
public class ItxSubjectContainerValueController {

    private static final Logger logger = LoggerFactory.getLogger(ItxSubjectContainerValueController.class);

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        ItxSubjectContainerValue itxSubjectContainerValue = ItxSubjectContainerValue.findItxSubjectContainerValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (itxSubjectContainerValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(itxSubjectContainerValue.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ItxSubjectContainerValue> result = ItxSubjectContainerValue.findAllItxSubjectContainerValues();
        return new ResponseEntity<String>(ItxSubjectContainerValue.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ItxSubjectContainerValue itxSubjectContainerValue = ItxSubjectContainerValue.fromJsonToItxSubjectContainerValue(json);
            itxSubjectContainerValue.persist();
        } catch (Exception e) {
            logger.error("ERROR: " + e);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        BufferedReader br = null;
        StringReader sr = null;
        try {
            sr = new StringReader(json);
            br = new BufferedReader(sr);
            for (ItxSubjectContainerValue itxSubjectContainerValue : ItxSubjectContainerValue.fromJsonArrayToItxSubjectContainerValues(br)) {
                itxSubjectContainerValue.persist();
                if (i % batchSize == 0) {
                    itxSubjectContainerValue.flush();
                    itxSubjectContainerValue.clear();
                }
                i++;
            }
        } catch (Exception e) {
            logger.error("ERROR: " + e);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        } finally {
            IOUtils.closeQuietly(sr);
            IOUtils.closeQuietly(br);
        }
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ItxSubjectContainerValue itxSubjectContainerValue = ItxSubjectContainerValue.fromJsonToItxSubjectContainerValue(json);
        if (itxSubjectContainerValue.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (ItxSubjectContainerValue itxSubjectContainerValue : ItxSubjectContainerValue.fromJsonArrayToItxSubjectContainerValues(json)) {
            if (itxSubjectContainerValue.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        ItxSubjectContainerValue itxSubjectContainerValue = ItxSubjectContainerValue.findItxSubjectContainerValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (itxSubjectContainerValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        itxSubjectContainerValue.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}
