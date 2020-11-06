package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupLabel;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ThingPage;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.dto.IdCollectionDTO;
import com.labsynch.labseer.service.AnalysisGroupService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import flexjson.JSONTokener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RooWebJson(jsonObject = AnalysisGroup.class)
@Controller
@RequestMapping("/analysisgroups")
@RooWebScaffold(path = "analysisgroups", formBackingObject = AnalysisGroup.class)
@RooWebFinder
public class AnalysisGroupController {

	@RequestMapping(params = { "find=ByExperiments", "form" }, method = RequestMethod.GET)
    public String findAnalysisGroupsByExperimentsForm(Model uiModel) {
        uiModel.addAttribute("experiments", Experiment.findAllExperiments());
        return "analysisgroups/findAnalysisGroupsByExperiments";
    }

	@RequestMapping(params = "find=ByExperiments", method = RequestMethod.GET)
    public String findAnalysisGroupsByExperiments(@RequestParam("experiments") Set<Experiment> experiments, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgroups", AnalysisGroup.findAnalysisGroupsByExperiments(experiments, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) AnalysisGroup.countFindAnalysisGroupsByExperiments(experiments) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgroups", AnalysisGroup.findAnalysisGroupsByExperiments(experiments, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgroups/list";
    }

	@RequestMapping(params = { "find=ByExperimentsAndIgnoredNot", "form" }, method = RequestMethod.GET)
    public String findAnalysisGroupsByExperimentsAndIgnoredNotForm(Model uiModel) {
        uiModel.addAttribute("experiments", Experiment.findAllExperiments());
        return "analysisgroups/findAnalysisGroupsByExperimentsAndIgnoredNot";
    }

	@RequestMapping(params = "find=ByExperimentsAndIgnoredNot", method = RequestMethod.GET)
    public String findAnalysisGroupsByExperimentsAndIgnoredNot(@RequestParam("experiments") Set<Experiment> experiments, @RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgroups", AnalysisGroup.findAnalysisGroupsByExperimentsAndIgnoredNot(experiments, ignored, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) AnalysisGroup.countFindAnalysisGroupsByExperimentsAndIgnoredNot(experiments, ignored) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgroups", AnalysisGroup.findAnalysisGroupsByExperimentsAndIgnoredNot(experiments, ignored, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgroups/list";
    }

	@RequestMapping(params = { "find=ByLsTransactionEquals", "form" }, method = RequestMethod.GET)
    public String findAnalysisGroupsByLsTransactionEqualsForm(Model uiModel) {
        return "analysisgroups/findAnalysisGroupsByLsTransactionEquals";
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET)
    public String findAnalysisGroupsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgroups", AnalysisGroup.findAnalysisGroupsByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) AnalysisGroup.countFindAnalysisGroupsByLsTransactionEquals(lsTransaction) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgroups", AnalysisGroup.findAnalysisGroupsByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgroups/list";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(id);
            if (analysisGroup == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(analysisGroup.toJson(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            List<AnalysisGroup> result = AnalysisGroup.findAllAnalysisGroups();
            return new ResponseEntity<String>(AnalysisGroup.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            AnalysisGroup analysisGroup = AnalysisGroup.fromJsonToAnalysisGroup(json);
            analysisGroup.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+analysisGroup.getId().toString()).build().toUriString());
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            for (AnalysisGroup analysisGroup: AnalysisGroup.fromJsonArrayToAnalysisGroups(json)) {
                analysisGroup.persist();
            }
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json, @PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            AnalysisGroup analysisGroup = AnalysisGroup.fromJsonToAnalysisGroup(json);
            analysisGroup.setId(id);
            if (analysisGroup.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(id);
            if (analysisGroup == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            analysisGroup.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByExperiments", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindAnalysisGroupsByExperiments(@RequestParam("experiments") Set<Experiment> experiments) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(AnalysisGroup.toJsonArray(AnalysisGroup.findAnalysisGroupsByExperiments(experiments).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByExperimentsAndIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindAnalysisGroupsByExperimentsAndIgnoredNot(@RequestParam("experiments") Set<Experiment> experiments, @RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(AnalysisGroup.toJsonArray(AnalysisGroup.findAnalysisGroupsByExperimentsAndIgnoredNot(experiments, ignored).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindAnalysisGroupsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(AnalysisGroup.toJsonArray(AnalysisGroup.findAnalysisGroupsByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid AnalysisGroup analysisGroup, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, analysisGroup);
            return "analysisgroups/create";
        }
        uiModel.asMap().clear();
        analysisGroup.persist();
        return "redirect:/analysisgroups/" + encodeUrlPathSegment(analysisGroup.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new AnalysisGroup());
        return "analysisgroups/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("analysisgroup", AnalysisGroup.findAnalysisGroup(id));
        uiModel.addAttribute("itemId", id);
        return "analysisgroups/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgroups", AnalysisGroup.findAnalysisGroupEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) AnalysisGroup.countAnalysisGroups() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgroups", AnalysisGroup.findAllAnalysisGroups(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgroups/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid AnalysisGroup analysisGroup, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, analysisGroup);
            return "analysisgroups/update";
        }
        uiModel.asMap().clear();
        analysisGroup.merge();
        return "redirect:/analysisgroups/" + encodeUrlPathSegment(analysisGroup.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, AnalysisGroup.findAnalysisGroup(id));
        return "analysisgroups/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(id);
        analysisGroup.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/analysisgroups";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("analysisGroup_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("analysisGroup_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, AnalysisGroup analysisGroup) {
        uiModel.addAttribute("analysisGroup", analysisGroup);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("analysisgrouplabels", AnalysisGroupLabel.findAllAnalysisGroupLabels());
        uiModel.addAttribute("analysisgroupstates", AnalysisGroupState.findAllAnalysisGroupStates());
        uiModel.addAttribute("experiments", Experiment.findAllExperiments());
        uiModel.addAttribute("thingpages", ThingPage.findAllThingPages());
        uiModel.addAttribute("treatmentgroups", TreatmentGroup.findAllTreatmentGroups());
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
