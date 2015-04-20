package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.utils.PropertiesUtilService;
import flexjson.JSONTokener;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RooWebJson(jsonObject = SubjectState.class)
@Controller
@RequestMapping("/subjectstates")
@RooWebScaffold(path = "subjectstates", formBackingObject = SubjectState.class)
@RooWebFinder
public class SubjectStateController {

//    private static final Logger logger = LoggerFactory.getLogger(SubjectStateController.class);
//
//    @Autowired
//    private PropertiesUtilService propertiesUtilService;
//
//    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
//        SubjectState subjectState = SubjectState.findSubjectState(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (subjectState == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(subjectState.toJson(), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<SubjectState> result = SubjectState.findSubjectStateEntries(0, 10);
//        return new ResponseEntity<String>(SubjectState.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
//        SubjectState subjectState = SubjectState.fromJsonToSubjectState(json);
//        subjectState.persist();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(subjectState.toJson(), headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/jsonArrayParseOld", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArrayParse(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        List<SubjectState> ssList = new ArrayList<SubjectState>();
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
//                    SubjectState subjectState = SubjectState.fromJsonToSubjectState(token.toString());
//                    subjectState.persist();
//                    ssList.add(subjectState);
//                    if (i % batchSize == 0) {
//                        subjectState.flush();
//                        subjectState.clear();
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
//        return new ResponseEntity<String>(SubjectState.toJsonArrayStub(ssList), headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        List<SubjectState> ssList = new ArrayList<SubjectState>();
//        int batchSize = propertiesUtilService.getBatchSize();
//        int i = 0;
//        BufferedReader br = null;
//        StringReader sr = null;
//        try {
//            sr = new StringReader(json);
//            br = new BufferedReader(sr);
//            for (SubjectState subjectState : SubjectState.fromJsonArrayToSubjectStates(br)) {
//                logger.debug(subjectState.toJson());
//                if (subjectState.getSubject().getId() != null) {
//                    Subject oldSubject = Subject.findSubject(subjectState.getSubject().getId());
//                    subjectState.setSubject(oldSubject);
//                }
//                subjectState.persist();
//                ssList.add(subjectState);
//                if (i % batchSize == 0) {
//                    subjectState.flush();
//                    subjectState.clear();
//                    logger.debug("flushed subjectState batch: " + i);
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
//        return new ResponseEntity<String>(SubjectState.toJsonArrayStub(ssList), headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        SubjectState subjectState = SubjectState.fromJsonToSubjectState(json);
//        if (subjectState.merge() == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        for (SubjectState subjectState : SubjectState.fromJsonArrayToSubjectStates(json)) {
//            if (subjectState.merge() == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
//        SubjectState subjectState = SubjectState.findSubjectState(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (subjectState == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        subjectState.remove();
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
//    public String create(@Valid SubjectState subjectState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
//        if (bindingResult.hasErrors()) {
//            populateEditForm(uiModel, subjectState);
//            return "subjectstates/create";
//        }
//        uiModel.asMap().clear();
//        subjectState.persist();
//        return "redirect:/subjectstates/" + encodeUrlPathSegment(subjectState.getId().toString(), httpServletRequest);
//    }
//
//    @RequestMapping(params = "form", produces = "text/html")
//    public String createForm(Model uiModel) {
//        populateEditForm(uiModel, new SubjectState());
//        List<String[]> dependencies = new ArrayList<String[]>();
//        if (Subject.countSubjects() == 0) {
//            dependencies.add(new String[] { "subject", "subjects" });
//        }
//        uiModel.addAttribute("dependencies", dependencies);
//        return "subjectstates/create";
//    }
//
//    @RequestMapping(value = "/{id}", produces = "text/html")
//    public String show(@PathVariable("id") Long id, Model uiModel) {
//        addDateTimeFormatPatterns(uiModel);
//        uiModel.addAttribute("subjectstate", SubjectState.findSubjectState(id));
//        uiModel.addAttribute("itemId", id);
//        return "subjectstates/show";
//    }
//
//    @RequestMapping(produces = "text/html")
//    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//        if (page != null || size != null) {
//            int sizeNo = size == null ? 10 : size.intValue();
//            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
//            uiModel.addAttribute("subjectstates", SubjectState.findSubjectStateEntries(firstResult, sizeNo));
//            float nrOfPages = (float) SubjectState.countSubjectStates() / sizeNo;
//            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
//        } else {
//            uiModel.addAttribute("subjectstates", SubjectState.findAllSubjectStates());
//        }
//        addDateTimeFormatPatterns(uiModel);
//        return "subjectstates/list";
//    }
//
//    @Transactional
//    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
//    public String update(@Valid SubjectState subjectState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
//        if (bindingResult.hasErrors()) {
//            populateEditForm(uiModel, subjectState);
//            return "subjectstates/update";
//        }
//        uiModel.asMap().clear();
//        subjectState.merge();
//        return "redirect:/subjectstates/" + encodeUrlPathSegment(subjectState.getId().toString(), httpServletRequest);
//    }
//
//    @Transactional
//    @RequestMapping(method = RequestMethod.GET, value = "/{id}", params = "form", produces = "text/html")
//    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
//        populateEditForm(uiModel, SubjectState.findSubjectState(id));
//        return "subjectstates/update";
//    }
//
//    @Transactional
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//        SubjectState subjectState = SubjectState.findSubjectState(id);
//        subjectState.remove();
//        uiModel.asMap().clear();
//        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//        return "redirect:/subjectstates";
//    }
//
//    void addDateTimeFormatPatterns(Model uiModel) {
//        uiModel.addAttribute("subjectState_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
//        uiModel.addAttribute("subjectState_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
//    }
//
//    @Transactional
//    void populateEditForm(Model uiModel, SubjectState subjectState) {
//        uiModel.addAttribute("subjectState", subjectState);
//        addDateTimeFormatPatterns(uiModel);
//        List<Subject> subjects = new ArrayList<Subject>();
//        subjects.add(subjectState.getSubject());
//        List<SubjectValue> subjectValues = new ArrayList<SubjectValue>();
//        subjectValues.addAll(subjectState.getLsValues());
//        uiModel.addAttribute("subjects", subjects);
//    }
//
//    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
//        String enc = httpServletRequest.getCharacterEncoding();
//        if (enc == null) {
//            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
//        }
//        try {
//            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
//        } catch (UnsupportedEncodingException uee) {
//        }
//        return pathSegment;
//    }
}
