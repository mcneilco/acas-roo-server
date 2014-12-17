package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.service.ItxContainerContainerService;
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
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = ItxContainerContainer.class)
@Controller
@RequestMapping("/itxcontainercontainers")
@RooWebScaffold(path = "itxcontainercontainers", formBackingObject = ItxContainerContainer.class)
@RooWebFinder
public class ItxContainerContainerController {

//    private static final Logger logger = LoggerFactory.getLogger(ItxContainerContainerController.class);
//
//    @Autowired
//    private ItxContainerContainerService itxContainerContainerService;
//
//    @Autowired
//    private PropertiesUtilService propertiesUtilService;
//
//    @Transactional
//    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
//        ItxContainerContainer itxContainerContainer = ItxContainerContainer.findItxContainerContainer(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (itxContainerContainer == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(itxContainerContainer.toJson(), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<ItxContainerContainer> result = ItxContainerContainer.findAllItxContainerContainers();
//        return new ResponseEntity<String>(ItxContainerContainer.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
//        ItxContainerContainer itxContainerContainer = ItxContainerContainer.fromJsonToItxContainerContainer(json);
//        itxContainerContainer.persist();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(itxContainerContainer.toJson(), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/LsItxContainerContainer", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromLsJson(@RequestBody String json) {
//        ItxContainerContainer itxContainerContainer = ItxContainerContainer.fromJsonToItxContainerContainer(json);
//        itxContainerContainer.persist();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(itxContainerContainer.toJson(), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/LsItxContainerContainer/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromLsJsonArray(@RequestBody String json) {
//        Collection<ItxContainerContainer> savedItxContainerContainers = itxContainerContainerService.saveLsItxContainers(json);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(ItxContainerContainer.toJsonArray(savedItxContainerContainers), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/jsonFile", method = RequestMethod.POST, headers = "Accept=text/plain")
//    public ResponseEntity<java.lang.String> createFromJsonFile(@RequestBody String jsonFile) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        int i = 0;
//        int batchSize = propertiesUtilService.getBatchSize();
//        Collection<ItxContainerContainer> savedItxContainerContainers = new ArrayList<ItxContainerContainer>();
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
//            JSONTokener jsonTokens = new JSONTokener(br);
//            Object token;
//            char delimiter;
//            char END_OF_ARRAY = ']';
//            while (jsonTokens.more()) {
//                delimiter = jsonTokens.nextClean();
//                if (delimiter != END_OF_ARRAY) {
//                    token = jsonTokens.nextValue();
//                    ItxContainerContainer itxContainerContainer = ItxContainerContainer.fromJsonToItxContainerContainer(token.toString());
//                    itxContainerContainer.persist();
//                    savedItxContainerContainers.add(itxContainerContainer);
//                    if (i % batchSize == 0) {
//                        itxContainerContainer.flush();
//                        itxContainerContainer.clear();
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
//        return new ResponseEntity<String>(ItxContainerContainer.toJsonArray(savedItxContainerContainers), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/jsonArrayParseOld", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArrayParse(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        int i = 0;
//        int batchSize = propertiesUtilService.getBatchSize();
//        Collection<ItxContainerContainer> savedItxContainerContainers = new ArrayList<ItxContainerContainer>();
//        try {
//            JSONTokener jsonTokens = new JSONTokener(json);
//            Object token;
//            char delimiter;
//            char END_OF_ARRAY = ']';
//            while (jsonTokens.more()) {
//                delimiter = jsonTokens.nextClean();
//                if (delimiter != END_OF_ARRAY) {
//                    token = jsonTokens.nextValue();
//                    ItxContainerContainer itxContainerContainer = ItxContainerContainer.fromJsonToItxContainerContainer(token.toString());
//                    itxContainerContainer.persist();
//                    savedItxContainerContainers.add(itxContainerContainer);
//                    if (i % batchSize == 0) {
//                        itxContainerContainer.flush();
//                        itxContainerContainer.clear();
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
//        return new ResponseEntity<String>(ItxContainerContainer.toJsonArray(savedItxContainerContainers), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        int i = 0;
//        BufferedReader br = null;
//        StringReader sr = null;
//        int batchSize = propertiesUtilService.getBatchSize();
//        Collection<ItxContainerContainer> savedItxContainerContainers = new ArrayList<ItxContainerContainer>();
//        try {
//            sr = new StringReader(json);
//            br = new BufferedReader(sr);
//            for (ItxContainerContainer itxContainerContainer : ItxContainerContainer.fromJsonArrayToItxContainerContainers(br)) {
//                itxContainerContainer.persist();
//                savedItxContainerContainers.add(itxContainerContainer);
//                if (i % batchSize == 0) {
//                    itxContainerContainer.flush();
//                    itxContainerContainer.clear();
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
//        return new ResponseEntity<String>(ItxContainerContainer.toJsonArray(savedItxContainerContainers), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        ItxContainerContainer itxContainerContainer = ItxContainerContainer.fromJsonToItxContainerContainer(json);
//        if (itxContainerContainer.merge() == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(itxContainerContainer.toJson(), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        Collection<ItxContainerContainer> itxContainerContainers = ItxContainerContainer.fromJsonArrayToItxContainerContainers(json);
//        for (ItxContainerContainer itxContainerContainer : itxContainerContainers) {
//            if (itxContainerContainer.merge() == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(ItxContainerContainer.toJsonArray(itxContainerContainers), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
//        ItxContainerContainer itxContainerContainer = ItxContainerContainer.findItxContainerContainer(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (itxContainerContainer == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        itxContainerContainer.remove();
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
}
