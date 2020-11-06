package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupLabel;
import com.labsynch.labseer.utils.PropertiesUtilService;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
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

@RooWebJson(jsonObject = AnalysisGroupLabel.class)
@Controller
@RequestMapping("/analysisgrouplabels")
@RooWebScaffold(path = "analysisgrouplabels", formBackingObject = AnalysisGroupLabel.class)
@RooWebFinder
public class AnalysisGroupLabelController {

	@RequestMapping(params = { "find=ByAnalysisGroup", "form" }, method = RequestMethod.GET)
    public String findAnalysisGroupLabelsByAnalysisGroupForm(Model uiModel) {
        uiModel.addAttribute("analysisgroups", AnalysisGroup.findAllAnalysisGroups());
        return "analysisgrouplabels/findAnalysisGroupLabelsByAnalysisGroup";
    }

	@RequestMapping(params = "find=ByAnalysisGroup", method = RequestMethod.GET)
    public String findAnalysisGroupLabelsByAnalysisGroup(@RequestParam("analysisGroup") AnalysisGroup analysisGroup, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgrouplabels", AnalysisGroupLabel.findAnalysisGroupLabelsByAnalysisGroup(analysisGroup, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) AnalysisGroupLabel.countFindAnalysisGroupLabelsByAnalysisGroup(analysisGroup) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgrouplabels", AnalysisGroupLabel.findAnalysisGroupLabelsByAnalysisGroup(analysisGroup, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgrouplabels/list";
    }

	@RequestMapping(params = { "find=ByLsTransactionEquals", "form" }, method = RequestMethod.GET)
    public String findAnalysisGroupLabelsByLsTransactionEqualsForm(Model uiModel) {
        return "analysisgrouplabels/findAnalysisGroupLabelsByLsTransactionEquals";
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET)
    public String findAnalysisGroupLabelsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgrouplabels", AnalysisGroupLabel.findAnalysisGroupLabelsByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) AnalysisGroupLabel.countFindAnalysisGroupLabelsByLsTransactionEquals(lsTransaction) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgrouplabels", AnalysisGroupLabel.findAnalysisGroupLabelsByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgrouplabels/list";
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid AnalysisGroupLabel analysisGroupLabel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, analysisGroupLabel);
            return "analysisgrouplabels/create";
        }
        uiModel.asMap().clear();
        analysisGroupLabel.persist();
        return "redirect:/analysisgrouplabels/" + encodeUrlPathSegment(analysisGroupLabel.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new AnalysisGroupLabel());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (AnalysisGroup.countAnalysisGroups() == 0) {
            dependencies.add(new String[] { "analysisGroup", "analysisgroups" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "analysisgrouplabels/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("analysisgrouplabel", AnalysisGroupLabel.findAnalysisGroupLabel(id));
        uiModel.addAttribute("itemId", id);
        return "analysisgrouplabels/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgrouplabels", AnalysisGroupLabel.findAnalysisGroupLabelEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) AnalysisGroupLabel.countAnalysisGroupLabels() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgrouplabels", AnalysisGroupLabel.findAllAnalysisGroupLabels(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgrouplabels/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid AnalysisGroupLabel analysisGroupLabel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, analysisGroupLabel);
            return "analysisgrouplabels/update";
        }
        uiModel.asMap().clear();
        analysisGroupLabel.merge();
        return "redirect:/analysisgrouplabels/" + encodeUrlPathSegment(analysisGroupLabel.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, AnalysisGroupLabel.findAnalysisGroupLabel(id));
        return "analysisgrouplabels/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        AnalysisGroupLabel analysisGroupLabel = AnalysisGroupLabel.findAnalysisGroupLabel(id);
        analysisGroupLabel.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/analysisgrouplabels";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("analysisGroupLabel_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("analysisGroupLabel_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, AnalysisGroupLabel analysisGroupLabel) {
        uiModel.addAttribute("analysisGroupLabel", analysisGroupLabel);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("analysisgroups", AnalysisGroup.findAllAnalysisGroups());
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

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            AnalysisGroupLabel analysisGroupLabel = AnalysisGroupLabel.findAnalysisGroupLabel(id);
            if (analysisGroupLabel == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(analysisGroupLabel.toJson(), headers, HttpStatus.OK);
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
            List<AnalysisGroupLabel> result = AnalysisGroupLabel.findAllAnalysisGroupLabels();
            return new ResponseEntity<String>(AnalysisGroupLabel.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            AnalysisGroupLabel analysisGroupLabel = AnalysisGroupLabel.fromJsonToAnalysisGroupLabel(json);
            analysisGroupLabel.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+analysisGroupLabel.getId().toString()).build().toUriString());
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
            for (AnalysisGroupLabel analysisGroupLabel: AnalysisGroupLabel.fromJsonArrayToAnalysisGroupLabels(json)) {
                analysisGroupLabel.persist();
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
            AnalysisGroupLabel analysisGroupLabel = AnalysisGroupLabel.fromJsonToAnalysisGroupLabel(json);
            analysisGroupLabel.setId(id);
            if (analysisGroupLabel.merge() == null) {
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
            AnalysisGroupLabel analysisGroupLabel = AnalysisGroupLabel.findAnalysisGroupLabel(id);
            if (analysisGroupLabel == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            analysisGroupLabel.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByAnalysisGroup", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindAnalysisGroupLabelsByAnalysisGroup(@RequestParam("analysisGroup") AnalysisGroup analysisGroup) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(AnalysisGroupLabel.toJsonArray(AnalysisGroupLabel.findAnalysisGroupLabelsByAnalysisGroup(analysisGroup).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindAnalysisGroupLabelsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(AnalysisGroupLabel.toJsonArray(AnalysisGroupLabel.findAnalysisGroupLabelsByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
