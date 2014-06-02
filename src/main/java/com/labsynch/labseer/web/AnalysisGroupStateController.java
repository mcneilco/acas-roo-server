package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.service.AnalysisGroupStateService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import flexjson.JSONTokener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
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

@RooWebJson(jsonObject = AnalysisGroupState.class)
@Controller
@RequestMapping("/analysisgroupstates")
@RooWebScaffold(path = "analysisgroupstates", formBackingObject = AnalysisGroupState.class)
@RooWebFinder
@Transactional
public class AnalysisGroupStateController {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupStateController.class);

    @Autowired
    private AnalysisGroupStateService analysisGroupStateService;

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        AnalysisGroupState analysisGroupState = AnalysisGroupState.findAnalysisGroupState(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (analysisGroupState == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(analysisGroupState.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<AnalysisGroupState> result = AnalysisGroupState.findAllAnalysisGroupStates();
        return new ResponseEntity<String>(AnalysisGroupState.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        AnalysisGroupState analysisGroupState = AnalysisGroupState.fromJsonToAnalysisGroupState(json);
        for (AnalysisGroupValue agv : analysisGroupState.getLsValues()) {
            if (agv.getLsState() == null) {
                agv.setLsState(analysisGroupState);
            }
        }
        analysisGroupState.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(analysisGroupState.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArrayFile", method = RequestMethod.POST, headers = "Accept=text/plain")
    public ResponseEntity<java.lang.String> createFromJsonArrayFile(@RequestBody String jsonFile) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<AnalysisGroupState> savedAnalysisGroupStates = new ArrayList<AnalysisGroupState>();
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
                    AnalysisGroupState analysisGroupState = AnalysisGroupState.fromJsonToAnalysisGroupState(token.toString());
                    for (AnalysisGroupValue agv : analysisGroupState.getLsValues()) {
                        if (agv.getLsState() == null) {
                            agv.setLsState(analysisGroupState);
                        }
                    }
                    analysisGroupState.persist();
                    savedAnalysisGroupStates.add(analysisGroupState);
                    if (i % batchSize == 0) {
                        analysisGroupState.flush();
                        analysisGroupState.clear();
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
        return new ResponseEntity<String>(AnalysisGroupState.toJsonArrayStub(savedAnalysisGroupStates), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArrayParseOld", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArrayParse(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<AnalysisGroupState> savedAnalysisGroupStates = new ArrayList<AnalysisGroupState>();
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
                    AnalysisGroupState analysisGroupState = AnalysisGroupState.fromJsonToAnalysisGroupState(token.toString());
                    for (AnalysisGroupValue agv : analysisGroupState.getLsValues()) {
                        if (agv.getLsState() == null) {
                            agv.setLsState(analysisGroupState);
                        }
                    }
                    analysisGroupState.persist();
                    savedAnalysisGroupStates.add(analysisGroupState);
                    if (i % batchSize == 0) {
                        analysisGroupState.flush();
                        analysisGroupState.clear();
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
        return new ResponseEntity<String>(AnalysisGroupState.toJsonArrayStub(savedAnalysisGroupStates), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<AnalysisGroupState> savedAnalysisGroupStates = new ArrayList<AnalysisGroupState>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        BufferedReader br = null;
        StringReader sr = null;
        try {
            sr = new StringReader(json);
            br = new BufferedReader(sr);
            for (AnalysisGroupState analysisGroupState : AnalysisGroupState.fromJsonArrayToAnalysisGroupStates(br)) {
                if (analysisGroupState.getLsValues() != null) {
                    for (AnalysisGroupValue agv : analysisGroupState.getLsValues()) {
                        if (agv.getLsState() == null) {
                            agv.setLsState(analysisGroupState);
                        }
                    }
                }
                analysisGroupState.persist();
                savedAnalysisGroupStates.add(analysisGroupState);
                if (i % batchSize == 0) {
                    analysisGroupState.flush();
                    analysisGroupState.clear();
                    logger.debug("flushing the newAnalysisGroupState state batch: " + i);
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
        return new ResponseEntity<String>(AnalysisGroupState.toJsonArrayStub(savedAnalysisGroupStates), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        AnalysisGroupState analysisGroupState = AnalysisGroupState.fromJsonToAnalysisGroupState(json);
        if (analysisGroupState.getAnalysisGroup() == null) {
            AnalysisGroupState oldAGS = AnalysisGroupState.findAnalysisGroupState(analysisGroupState.getId());
            AnalysisGroup ag = AnalysisGroup.findAnalysisGroup(oldAGS.getAnalysisGroup().getId());
            analysisGroupState.setAnalysisGroup(ag);
        }
        if (analysisGroupState.getLsValues() != null) {
            for (AnalysisGroupValue agv : analysisGroupState.getLsValues()) {
                if (agv.getLsState() == null) {
                    agv.setLsState(analysisGroupState);
                }
                if (analysisGroupState.isIgnored()) {
                    agv.setIgnored(true);
                }
            }
        }
        if (analysisGroupState.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (AnalysisGroupState analysisGroupState : AnalysisGroupState.fromJsonArrayToAnalysisGroupStates(json)) {
            if (analysisGroupState.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "ignoreStateAndValues/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> ignoreStateAndValuesFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<AnalysisGroupState> analysisGroupStates = AnalysisGroupState.fromJsonArrayToAnalysisGroupStates(json);
        if (analysisGroupStates.size() == 0) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        Collection<AnalysisGroupState> results = analysisGroupStateService.ignoreAllAnalysisGroupStates(analysisGroupStates);
        return new ResponseEntity<String>(AnalysisGroupState.toJsonArray(results), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        AnalysisGroupState analysisGroupState = AnalysisGroupState.findAnalysisGroupState(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (analysisGroupState == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        analysisGroupState.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @Transactional
    void populateEditForm(Model uiModel, AnalysisGroupState analysisGroupState) {
        uiModel.addAttribute("analysisGroupState", analysisGroupState);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("analysisgroups", analysisGroupState.getAnalysisGroup());
        uiModel.addAttribute("analysisgroupvalues", analysisGroupState.getLsValues());
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid AnalysisGroupState analysisGroupState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, analysisGroupState);
            return "analysisgroupstates/create";
        }
        uiModel.asMap().clear();
        analysisGroupState.persist();
        return "redirect:/analysisgroupstates/" + encodeUrlPathSegment(analysisGroupState.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new AnalysisGroupState());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (AnalysisGroup.countAnalysisGroups() == 0) {
            dependencies.add(new String[] { "analysisgroup", "analysisgroups" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "analysisgroupstates/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("analysisgroupstate", AnalysisGroupState.findAnalysisGroupState(id));
        uiModel.addAttribute("itemId", id);
        return "analysisgroupstates/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgroupstates", AnalysisGroupState.findAnalysisGroupStateEntries(firstResult, sizeNo));
            float nrOfPages = (float) AnalysisGroupState.countAnalysisGroupStates() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgroupstates", AnalysisGroupState.findAllAnalysisGroupStates());
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgroupstates/list";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid AnalysisGroupState analysisGroupState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, analysisGroupState);
            return "analysisgroupstates/update";
        }
        uiModel.asMap().clear();
        analysisGroupState.merge();
        return "redirect:/analysisgroupstates/" + encodeUrlPathSegment(analysisGroupState.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, AnalysisGroupState.findAnalysisGroupState(id));
        return "analysisgroupstates/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        AnalysisGroupState analysisGroupState = AnalysisGroupState.findAnalysisGroupState(id);
        analysisGroupState.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/analysisgroupstates";
    }

    void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("analysisGroupState_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("analysisGroupState_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }
}
