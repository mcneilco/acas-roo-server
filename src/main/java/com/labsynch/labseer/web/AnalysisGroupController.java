package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.dto.IdCollectionDTO;
import com.labsynch.labseer.service.AnalysisGroupService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = AnalysisGroup.class)
@Controller
@RequestMapping("/analysisgroups")
@RooWebScaffold(path = "analysisgroups", formBackingObject = AnalysisGroup.class)
@RooWebFinder
public class AnalysisGroupController {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupController.class);

    @Autowired
    private AnalysisGroupService analysisGroupService;

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @Transactional
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(
    		@PathVariable("id") Long id,
    		@RequestParam(value = "with", required = false) String with) {
        AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (analysisGroup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        
		if (with != null) {
			if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(analysisGroup.toFullJson(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(analysisGroup.toPrettyJson(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjsonstub")) {
				return new ResponseEntity<String>(analysisGroup.toPrettyJsonStub(), headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("ERROR: with" + with + " route is not implemented. ", headers, HttpStatus.NOT_IMPLEMENTED);
			}
		} else {
			return new ResponseEntity<String>(analysisGroup.toJson(), headers, HttpStatus.OK);
		}
    }

    @Transactional
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<AnalysisGroup> result = AnalysisGroup.findAllAnalysisGroups();
        return new ResponseEntity<String>(AnalysisGroup.toJsonArray(result), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        AnalysisGroup analysisGroup = analysisGroupService.saveLsAnalysisGroup(AnalysisGroup.fromJsonToAnalysisGroup(json));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(analysisGroup.toJson(), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = "/jsonArrayFile", method = RequestMethod.POST, headers = "Accept=text/plain")
    public ResponseEntity<java.lang.String> createFromJsonArrayFile(@RequestBody String jsonFile) {
        logger.debug("incoming jsonFile: " + jsonFile);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<AnalysisGroup> savedanalysisGroups = new ArrayList<AnalysisGroup>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(jsonFile));
            JSONTokener jsonTokens = new JSONTokener(br);
            Object token;
            char delimiter;
            char END_OF_ARRAY = ']';
            while (jsonTokens.more()) {
                delimiter = jsonTokens.nextClean();
                if (delimiter != END_OF_ARRAY) {
                    token = jsonTokens.nextValue();
                    AnalysisGroup analysisGroup = AnalysisGroup.fromJsonToAnalysisGroup(token.toString());
                    AnalysisGroup saved = analysisGroupService.saveLsAnalysisGroup(analysisGroup);
                    savedanalysisGroups.add(saved);
                    if (i % batchSize == 0) {
                        saved.flush();
                        saved.clear();
                    }
                    i++;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("ERROR: " + e);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        } finally {
            IOUtils.closeQuietly(br);
        }
        return new ResponseEntity<String>(AnalysisGroup.toJsonArray(savedanalysisGroups), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = "/jsonArrayParseOld", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArrayParse(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<AnalysisGroup> savedanalysisGroups = new ArrayList<AnalysisGroup>();
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
                    AnalysisGroup analysisGroup = AnalysisGroup.fromJsonToAnalysisGroup(token.toString());
                    AnalysisGroup saved = analysisGroupService.saveLsAnalysisGroup(analysisGroup);
                    savedanalysisGroups.add(saved);
                    if (i % batchSize == 0) {
                        saved.flush();
                        saved.clear();
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
        return new ResponseEntity<String>(AnalysisGroup.toJsonArray(savedanalysisGroups), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        int batchSize = propertiesUtilService.getBatchSize();
        
        List<IdCollectionDTO> idList = new ArrayList<IdCollectionDTO>();
        IdCollectionDTO idDTO = null;
        int i = 0;
        BufferedReader br = null;
        StringReader sr = null;
        try {
            sr = new StringReader(json);
            br = new BufferedReader(sr);
            for (AnalysisGroup analysisGroup : AnalysisGroup.fromJsonArrayToAnalysisGroups(br)) {
                logger.debug("saving analysis group number: " + i);
                AnalysisGroup saved = analysisGroupService.saveLsAnalysisGroup(analysisGroup);
                idDTO = new IdCollectionDTO();
                idDTO.setId(saved.getId());
                idDTO.setVersion(saved.getVersion());
                idList.add(idDTO);
                if (i % batchSize == 0) {
                    saved.flush();
                    saved.clear();
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
        return new ResponseEntity<String>(IdCollectionDTO.toJsonArray(idList), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        AnalysisGroup analysisGroup = AnalysisGroup.fromJsonToAnalysisGroup(json);
        if (analysisGroup.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(analysisGroup.toJson(), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<AnalysisGroup> analysisGroups = AnalysisGroup.fromJsonArrayToAnalysisGroups(json);
        for (AnalysisGroup analysisGroup : analysisGroups) {
            if (analysisGroup.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(AnalysisGroup.toJsonArray(analysisGroups), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (analysisGroup == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        analysisGroup.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
}
