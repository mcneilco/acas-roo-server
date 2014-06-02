package com.labsynch.labseer.web;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.dto.SubjectDTO;
import com.labsynch.labseer.service.SubjectService;
import com.labsynch.labseer.utils.PropertiesUtilService;

import flexjson.JSONTokener;

@RooWebJson(jsonObject = Subject.class)
@Controller
@RequestMapping("/subjects")
@RooWebScaffold(path = "subjects", formBackingObject = Subject.class)
@RooWebFinder
public class SubjectController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @RequestMapping(value = "oldroute/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showOldJson(@PathVariable("id") Long id) {
        Subject subject = Subject.findSubject(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (subject == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(subject.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "stub/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJsonStub(@PathVariable("id") Long id) {
        Subject subject = Subject.findSubject(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (subject == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(subject.toJsonStub(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(
    		@PathVariable("id") Long id,
    		@RequestParam(value = "with", required = false) String with) {
    	Subject subject = null;
    	try {
            subject = Subject.findSubject(id);
    	} catch (Exception e){
    		logger.error(e.toString());
    		subject = null;
    	}
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        
        if (subject == null) {
        	logger.info("did not find the query subject: " + id);
            return new ResponseEntity<String>("subject not found", headers, HttpStatus.CONFLICT);
        }
        
		if (with != null) {
			if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(subject.toJson(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(subject.toPrettyJson(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjsonstub")) {
				return new ResponseEntity<String>(subject.toPrettyJsonStub(), headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("ERROR: with" + with + " route is not implemented. ", headers, HttpStatus.NOT_IMPLEMENTED);
			}
		} else {
        	//not sure why I wanted to retrieve the subjectDTO
        	//SubjectDTO subjectDTO = subjectService.getSubject(subject);
			return new ResponseEntity<String>(subject.toJsonStub(), headers, HttpStatus.OK);
		}
        
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Subject> result = Subject.findAllSubjects();
        return new ResponseEntity<String>(Subject.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        Subject subject = Subject.fromJsonToSubject(json);
        subject.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(subject.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArrayParseOld", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArrayParse(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<Subject> savedSubjects = new ArrayList<Subject>();
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
                    Subject subject = Subject.fromJsonToSubject(token.toString());
                    subject.persist();
                    savedSubjects.add(subject);
                    if (i % batchSize == 0) {
                        subject.flush();
                        subject.clear();
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
        return new ResponseEntity<String>(Subject.toJsonArray(savedSubjects), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<Subject> savedSubjects = new ArrayList<Subject>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        BufferedReader br = null;
        StringReader sr = null;
        try {
            sr = new StringReader(json);
            br = new BufferedReader(sr);
            for (Subject subject : Subject.fromJsonArrayToSubjects(br)) {
                Subject saved = subjectService.saveSubject(subject);
                savedSubjects.add(saved);
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
        return new ResponseEntity<String>(Subject.toJsonArray(savedSubjects), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Subject subject = Subject.fromJsonToSubject(json);
        if (subject.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (Subject subject : Subject.fromJsonArrayToSubjects(json)) {
            if (subject.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "ignoreStatesAndValues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateSubjectStatesFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<Subject> subjects = Subject.fromJsonArrayToSubjects(json);
        Set<Subject> subjectSets = subjectService.ignoreAllSubjectStates((Set<Subject>) subjects);
        if (subjectSets.size() == 0) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(Subject.toJsonArray(subjectSets), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        Subject subject = Subject.findSubject(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (subject == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        subject.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/stateTypeAndKind/{stateTypeAndKind}/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getSingleSubjectStatesFromJson(@PathVariable("stateTypeAndKind") String stateTypeAndKind, @PathVariable("id") Long id) {
        Subject querySubject = Subject.findSubject(id);
        Collection<Subject> querySubjects = new HashSet<Subject>();
        querySubjects.add(querySubject);
        Set<SubjectDTO> subjects = subjectService.getSubjectsWithStateTypeAndKind(querySubjects, stateTypeAndKind);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(SubjectDTO.toJsonArray(subjects), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/stateTypeAndKind/{stateTypeAndKind}/idList", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getSubjectStatesFromIdList(@PathVariable("stateTypeAndKind") String stateTypeAndKind, @RequestBody String idListString) {
        Collection<Subject> querySubjects = new HashSet<Subject>();
        logger.info("incoming stateTypeAndKind: " + stateTypeAndKind);
        logger.info("incoming idListString: " + idListString);
        String[] idList = idListString.split(",");
        for (String id : idList) {
            Subject querySubject = new Subject();
            querySubject.setId(Long.valueOf(id.trim()));
            logger.info("querySubject: " + querySubject.getId());
            querySubjects.add(querySubject);
        }
        Set<SubjectDTO> subjects = subjectService.getSubjectsWithStateTypeAndKind(querySubjects, stateTypeAndKind);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(SubjectDTO.toJsonArray(subjects), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/stateTypeAndKind/{stateTypeAndKind}/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getSubjectStatesFromJson(@PathVariable("stateTypeAndKind") String stateTypeAndKind, @RequestBody String json) {
        logger.info("incoming json: " + json);
        logger.info("incoming stateTypeAndKind: " + stateTypeAndKind);
        Collection<Subject> querySubjects = Subject.fromJsonArrayToSubjects(json);
        Set<SubjectDTO> subjects = subjectService.getSubjectsWithStateTypeAndKind(querySubjects, stateTypeAndKind);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(SubjectDTO.toJsonArray(subjects), headers, HttpStatus.OK);
    }
}
