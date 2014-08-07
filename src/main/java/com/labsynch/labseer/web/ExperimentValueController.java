package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.utils.PropertiesUtilService;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = ExperimentValue.class)
@Controller
@RequestMapping("/experimentvalues")
@RooWebScaffold(path = "experimentvalues", formBackingObject = ExperimentValue.class)
@RooWebFinder
@Transactional
public class ExperimentValueController {

    private static final Logger logger = LoggerFactory.getLogger(ExperimentValueController.class);

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @Transactional
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        ExperimentValue experimentValue = ExperimentValue.findExperimentValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (experimentValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(experimentValue.toJson(), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ExperimentValue> result = ExperimentValue.findAllExperimentValues();
        return new ResponseEntity<String>(ExperimentValue.toJsonArray(result), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        ExperimentValue experimentValue = ExperimentValue.fromJsonToExperimentValue(json);
        experimentValue.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(experimentValue.toJson(), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        Collection<ExperimentValue> savedExperimentValues = new ArrayList<ExperimentValue>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        StringReader sr = new StringReader(json);
        BufferedReader br = new BufferedReader(sr);
        for (ExperimentValue experimentValue : ExperimentValue.fromJsonArrayToExperimentValues(br)) {
            logger.debug("save the experiment Value: " + experimentValue.toJson());
            experimentValue.persist();
            savedExperimentValues.add(experimentValue);
            if (i % batchSize == 0) {
                experimentValue.flush();
                experimentValue.clear();
            }
            i++;
        }
        IOUtils.closeQuietly(sr);
        IOUtils.closeQuietly(br);
        logger.debug(ExperimentValue.toJsonArray(savedExperimentValues));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ExperimentValue.toJsonArray(savedExperimentValues), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ExperimentValue experimentValue = ExperimentValue.fromJsonToExperimentValue(json);
        if (experimentValue.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (ExperimentValue experimentValue : ExperimentValue.fromJsonArrayToExperimentValues(json)) {
            if (experimentValue.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        ExperimentValue experimentValue = ExperimentValue.findExperimentValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (experimentValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        experimentValue.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ExperimentValue experimentValue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, experimentValue);
            return "experimentvalues/create";
        }
        uiModel.asMap().clear();
        experimentValue.persist();
        return "redirect:/experimentvalues/" + encodeUrlPathSegment(experimentValue.getId().toString(), httpServletRequest);
    }

    @Transactional
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ExperimentValue());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (ExperimentState.countExperimentStates() == 0) {
            dependencies.add(new String[] { "experimentstate", "experimentstates" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "experimentvalues/create";
    }

    @Transactional
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("experimentvalue", ExperimentValue.findExperimentValue(id));
        uiModel.addAttribute("itemId", id);
        return "experimentvalues/show";
    }

    @Transactional
    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("experimentvalues", ExperimentValue.findExperimentValueEntries(firstResult, sizeNo));
            float nrOfPages = (float) ExperimentValue.countExperimentValues() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("experimentvalues", ExperimentValue.findAllExperimentValues());
        }
        addDateTimeFormatPatterns(uiModel);
        return "experimentvalues/list";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ExperimentValue experimentValue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, experimentValue);
            return "experimentvalues/update";
        }
        uiModel.asMap().clear();
        experimentValue.merge();
        return "redirect:/experimentvalues/" + encodeUrlPathSegment(experimentValue.getId().toString(), httpServletRequest);
    }

    @Transactional
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ExperimentValue.findExperimentValue(id));
        return "experimentvalues/update";
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ExperimentValue experimentValue = ExperimentValue.findExperimentValue(id);
        experimentValue.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/experimentvalues";
    }
}
