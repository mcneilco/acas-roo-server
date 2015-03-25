package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxContainerContainerState;
import com.labsynch.labseer.utils.PropertiesUtilService;
import flexjson.JSONTokener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
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

@RooWebJson(jsonObject = ItxContainerContainerState.class)
@Controller
@RequestMapping("/itxcontainercontainerstates")
@RooWebScaffold(path = "itxcontainercontainerstates", formBackingObject = ItxContainerContainerState.class)
public class ItxLsThingLsThingStateController {

//    private static final Logger logger = LoggerFactory.getLogger(ItxContainerContainerStateController.class);
//
//    @Autowired
//    private PropertiesUtilService propertiesUtilService;
//
//    @Transactional
//    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
//        ItxContainerContainerState itxContainerContainerState = ItxContainerContainerState.findItxContainerContainerState(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (itxContainerContainerState == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(itxContainerContainerState.toJson(), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<ItxContainerContainerState> result = ItxContainerContainerState.findAllItxContainerContainerStates();
//        return new ResponseEntity<String>(ItxContainerContainerState.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
//        ItxContainerContainerState itxContainerContainerState = ItxContainerContainerState.fromJsonToItxContainerContainerState(json);
//        itxContainerContainerState.persist();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(itxContainerContainerState.toJson(), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/jsonFile", method = RequestMethod.POST, headers = "Accept=text/plain")
//    public ResponseEntity<java.lang.String> createFromJsonFile(@RequestBody String jsonFile) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        Collection<ItxContainerContainerState> savedItxContainerContainerStates = new ArrayList<ItxContainerContainerState>();
//        int batchSize = propertiesUtilService.getBatchSize();
//        int i = 0;
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
//            JSONTokener jsonTokens = new JSONTokener(br);
//            Object token;
//            char delimiter;
//            char END_OF_ARRAY = ']';
//            while (jsonTokens.more()) {
//                delimiter = jsonTokens.nextClean();
//                logger.warn("delimiter is : " + delimiter);
//                if (delimiter != END_OF_ARRAY) {
//                    token = jsonTokens.nextValue();
//                    ItxContainerContainerState itxContainerContainerState = ItxContainerContainerState.fromJsonToItxContainerContainerState(token.toString());
//                    itxContainerContainerState.persist();
//                    savedItxContainerContainerStates.add(itxContainerContainerState);
//                    if (i % batchSize == 0) {
//                        itxContainerContainerState.flush();
//                        itxContainerContainerState.clear();
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
//        return new ResponseEntity<String>(ItxContainerContainerState.toJsonArrayStub(savedItxContainerContainerStates), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/jsonArrayParseOld", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArrayParse(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        Collection<ItxContainerContainerState> savedItxContainerContainerStates = new ArrayList<ItxContainerContainerState>();
//        int batchSize = propertiesUtilService.getBatchSize();
//        int i = 0;
//        try {
//            JSONTokener jsonTokens = new JSONTokener(json);
//            Object token;
//            char delimiter;
//            char END_OF_ARRAY = ']';
//            while (jsonTokens.more()) {
//                delimiter = jsonTokens.nextClean();
//                if (delimiter != END_OF_ARRAY) {
//                    token = jsonTokens.nextValue();
//                    ItxContainerContainerState itxContainerContainerState = ItxContainerContainerState.fromJsonToItxContainerContainerState(token.toString());
//                    itxContainerContainerState.persist();
//                    savedItxContainerContainerStates.add(itxContainerContainerState);
//                    if (i % batchSize == 0) {
//                        itxContainerContainerState.flush();
//                        itxContainerContainerState.clear();
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
//        return new ResponseEntity<String>(ItxContainerContainerState.toJsonArrayStub(savedItxContainerContainerStates), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        Collection<ItxContainerContainerState> savedItxContainerContainerStates = new ArrayList<ItxContainerContainerState>();
//        int batchSize = propertiesUtilService.getBatchSize();
//        int i = 0;
//        BufferedReader br = null;
//        StringReader sr = null;
//        try {
//            sr = new StringReader(json);
//            br = new BufferedReader(sr);
//            for (ItxContainerContainerState itxContainerContainerState : ItxContainerContainerState.fromJsonArrayToItxContainerContainerStates(br)) {
//                itxContainerContainerState.persist();
//                savedItxContainerContainerStates.add(itxContainerContainerState);
//                if (i % batchSize == 0) {
//                    itxContainerContainerState.flush();
//                    itxContainerContainerState.clear();
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
//        return new ResponseEntity<String>(ItxContainerContainerState.toJsonArrayStub(savedItxContainerContainerStates), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        ItxContainerContainerState itxContainerContainerState = ItxContainerContainerState.fromJsonToItxContainerContainerState(json);
//        if (itxContainerContainerState.merge() == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(itxContainerContainerState.toJson(), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        Collection<ItxContainerContainerState> itxContainerContainerStates = ItxContainerContainerState.fromJsonArrayToItxContainerContainerStates(json);
//        for (ItxContainerContainerState itxContainerContainerState : itxContainerContainerStates) {
//            if (itxContainerContainerState.merge() == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(ItxContainerContainerState.toJsonArray(itxContainerContainerStates), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
//        ItxContainerContainerState itxContainerContainerState = ItxContainerContainerState.findItxContainerContainerState(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (itxContainerContainerState == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        itxContainerContainerState.remove();
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
}
