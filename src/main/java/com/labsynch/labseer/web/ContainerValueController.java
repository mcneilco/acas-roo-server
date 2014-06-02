package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = ContainerValue.class)
@Controller
@RequestMapping("/containervalues")
@RooWebScaffold(path = "containervalues", formBackingObject = ContainerValue.class)
@RooWebFinder
public class ContainerValueController {

    private static final Logger logger = LoggerFactory.getLogger(ContainerValueController.class);

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        ContainerValue containerValue = ContainerValue.findContainerValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (containerValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(containerValue.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ContainerValue> result = ContainerValue.findAllContainerValues();
        return new ResponseEntity<String>(ContainerValue.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        ContainerValue containerValue = ContainerValue.fromJsonToContainerValue(json);
        containerValue.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(containerValue.toJson(), headers, HttpStatus.CREATED);
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
            for (ContainerValue containerValue : ContainerValue.fromJsonArrayToContainerValues(br)) {
                containerValue.persist();
                if (i % batchSize == 0) {
                    containerValue.flush();
                    containerValue.clear();
                    logger.debug("flushed the container Value batch: " + i);
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
        return new ResponseEntity<String>("[]", headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArrayParseOld", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArrayParse(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        try {
            JSONTokener jsonTokens = new JSONTokener(json);
            Object token;
            char delimiter;
            char END_OF_ARRAY = ']';
            while (jsonTokens.more()) {
                delimiter = jsonTokens.nextClean();
                if (delimiter != END_OF_ARRAY) {
                    token = jsonTokens.nextValue();
                    ContainerValue containerValue = ContainerValue.fromJsonToContainerValue(token.toString());
                    containerValue.persist();
                    if (i % batchSize == 0) {
                        containerValue.flush();
                        containerValue.clear();
                    }
                    i++;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("ERROR: " + e);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("[]", headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonFile", method = RequestMethod.POST, headers = "Accept=text/plain")
    public ResponseEntity<java.lang.String> createFromJsonFile(@RequestBody String jsonFile) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
            JSONTokener jsonTokens = new JSONTokener(br);
            Object token;
            char delimiter;
            char END_OF_ARRAY = ']';
            while (jsonTokens.more()) {
                delimiter = jsonTokens.nextClean();
                if (delimiter != END_OF_ARRAY) {
                    token = jsonTokens.nextValue();
                    ContainerValue containerValue = ContainerValue.fromJsonToContainerValue(token.toString());
                    containerValue.persist();
                    if (i % batchSize == 0) {
                        containerValue.flush();
                        containerValue.clear();
                    }
                    i++;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("ERROR: " + e);
            return new ResponseEntity<String>(e.toString(), headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("[]", headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ContainerValue containerValue = ContainerValue.fromJsonToContainerValue(json);
        if (containerValue.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(containerValue.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ContainerValue> containerValues = ContainerValue.fromJsonArrayToContainerValues(json);
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        try {
            for (ContainerValue containerValue : containerValues) {
                if (containerValue.merge() == null) {
                    return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
                }
                if (i % batchSize == 0) {
                    containerValue.flush();
                    containerValue.clear();
                }
                i++;
            }
        } catch (Exception e) {
            logger.error("ERROR: " + e);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("[]", headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        ContainerValue containerValue = ContainerValue.findContainerValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (containerValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        containerValue.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    void populateEditForm(Model uiModel, ContainerValue containerValue) {
        uiModel.addAttribute("containerValue", containerValue);
        addDateTimeFormatPatterns(uiModel);
        List<ContainerState> containerStates = new ArrayList<ContainerState>();
        containerStates.add(ContainerState.findContainerState(containerValue.getLsState().getId()));
        uiModel.addAttribute("containerstates", containerStates);
    }
}
