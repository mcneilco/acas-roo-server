package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxContainerContainerValue;
import com.labsynch.labseer.utils.PropertiesUtilService;
import flexjson.JSONTokener;
import java.io.BufferedReader;
import java.io.FileReader;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = ItxContainerContainerValue.class)
@Controller
@RequestMapping("/itxcontainercontainervalues")
@RooWebScaffold(path = "itxcontainercontainervalues", formBackingObject = ItxContainerContainerValue.class)
public class ItxLsThingLsThingValueController {

//    private static final Logger logger = LoggerFactory.getLogger(ItxContainerContainerValueController.class);
//
//    @Autowired
//    private PropertiesUtilService propertiesUtilService;
//
//    @Transactional
//    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
//        ItxContainerContainerValue itxContainerContainerValue = ItxContainerContainerValue.findItxContainerContainerValue(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (itxContainerContainerValue == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(itxContainerContainerValue.toJson(), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<ItxContainerContainerValue> result = ItxContainerContainerValue.findAllItxContainerContainerValues();
//        return new ResponseEntity<String>(ItxContainerContainerValue.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
//        ItxContainerContainerValue itxContainerContainerValue = ItxContainerContainerValue.fromJsonToItxContainerContainerValue(json);
//        itxContainerContainerValue.persist();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(itxContainerContainerValue.toJson(), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/jsonFile", method = RequestMethod.POST, headers = "Accept=text/plain")
//    public ResponseEntity<java.lang.String> createFromJsonFile(@RequestBody String jsonFile) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        int batchSize = propertiesUtilService.getBatchSize();
//        int i = 0;
//        logger.debug("jsonFile is : " + jsonFile);
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
//            JSONTokener jsonTokens = new JSONTokener(br);
//            Object token;
//            char delimiter;
//            char END_OF_ARRAY = ']';
//            while (jsonTokens.more()) {
//                delimiter = jsonTokens.nextClean();
//                logger.debug("delimiter is : " + delimiter);
//                if (delimiter != END_OF_ARRAY) {
//                    token = jsonTokens.nextValue();
//                    ItxContainerContainerValue itxContainerContainerValue = ItxContainerContainerValue.fromJsonToItxContainerContainerValue(token.toString());
//                    itxContainerContainerValue.persist();
//                    if (i % batchSize == 0) {
//                        itxContainerContainerValue.flush();
//                        itxContainerContainerValue.clear();
//                    }
//                    i++;
//                } else {
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            logger.error("ERROR: " + e);
//            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<String>("[]", headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/jsonArrayParseOld", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArrayParse(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        int batchSize = propertiesUtilService.getBatchSize();
//        int i = 0;
//        try {
//            JSONTokener jsonTokens = new JSONTokener(json);
//            Object token;
//            char delimiter;
//            char END_OF_ARRAY = ']';
//            while (jsonTokens.more()) {
//                delimiter = jsonTokens.nextClean();
//                logger.debug("delimiter is : " + delimiter);
//                if (delimiter != END_OF_ARRAY) {
//                    token = jsonTokens.nextValue();
//                    ItxContainerContainerValue itxContainerContainerValue = ItxContainerContainerValue.fromJsonToItxContainerContainerValue(token.toString());
//                    itxContainerContainerValue.persist();
//                    if (i % batchSize == 0) {
//                        itxContainerContainerValue.flush();
//                        itxContainerContainerValue.clear();
//                    }
//                    i++;
//                } else {
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            logger.error("ERROR: " + e);
//            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<String>("[]", headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        int batchSize = propertiesUtilService.getBatchSize();
//        int i = 0;
//        BufferedReader br = null;
//        StringReader sr = null;
//        try {
//            sr = new StringReader(json);
//            br = new BufferedReader(sr);
//            for (ItxContainerContainerValue itxContainerContainerValue : ItxContainerContainerValue.fromJsonArrayToItxContainerContainerValues(br)) {
//                itxContainerContainerValue.persist();
//                if (i % batchSize == 0) {
//                    itxContainerContainerValue.flush();
//                    itxContainerContainerValue.clear();
//                }
//                i++;
//            }
//        } catch (Exception e) {
//            logger.error("ERROR: " + e);
//            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
//        } finally {
//            IOUtils.closeQuietly(sr);
//            IOUtils.closeQuietly(br);
//        }
//        return new ResponseEntity<String>("[]", headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        ItxContainerContainerValue itxContainerContainerValue = ItxContainerContainerValue.fromJsonToItxContainerContainerValue(json);
//        if (itxContainerContainerValue.merge() == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        for (ItxContainerContainerValue itxContainerContainerValue : ItxContainerContainerValue.fromJsonArrayToItxContainerContainerValues(json)) {
//            if (itxContainerContainerValue.merge() == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
//        ItxContainerContainerValue itxContainerContainerValue = ItxContainerContainerValue.findItxContainerContainerValue(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (itxContainerContainerValue == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        itxContainerContainerValue.remove();
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
}
