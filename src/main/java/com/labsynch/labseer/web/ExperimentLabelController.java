package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentLabel;
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

@RooWebJson(jsonObject = ExperimentLabel.class)
@Controller
@RequestMapping("/experimentlabels")
@RooWebScaffold(path = "experimentlabels", formBackingObject = ExperimentLabel.class)
@RooWebFinder
public class ExperimentLabelController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ExperimentLabel experimentLabel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, experimentLabel);
            return "experimentlabels/create";
        }
        uiModel.asMap().clear();
        experimentLabel.persist();
        return "redirect:/experimentlabels/" + encodeUrlPathSegment(experimentLabel.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ExperimentLabel());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Experiment.countExperiments() == 0) {
            dependencies.add(new String[] { "experiment", "experiments" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "experimentlabels/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("experimentlabel", ExperimentLabel.findExperimentLabel(id));
        uiModel.addAttribute("itemId", id);
        return "experimentlabels/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("experimentlabels", ExperimentLabel.findExperimentLabelEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ExperimentLabel.countExperimentLabels() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("experimentlabels", ExperimentLabel.findAllExperimentLabels(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "experimentlabels/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ExperimentLabel experimentLabel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, experimentLabel);
            return "experimentlabels/update";
        }
        uiModel.asMap().clear();
        experimentLabel.merge();
        return "redirect:/experimentlabels/" + encodeUrlPathSegment(experimentLabel.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ExperimentLabel.findExperimentLabel(id));
        return "experimentlabels/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ExperimentLabel experimentLabel = ExperimentLabel.findExperimentLabel(id);
        experimentLabel.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/experimentlabels";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("experimentLabel_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("experimentLabel_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, ExperimentLabel experimentLabel) {
        uiModel.addAttribute("experimentLabel", experimentLabel);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("experiments", Experiment.findAllExperiments());
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

	@RequestMapping(params = { "find=ByExperiment", "form" }, method = RequestMethod.GET)
    public String findExperimentLabelsByExperimentForm(Model uiModel) {
        uiModel.addAttribute("experiments", Experiment.findAllExperiments());
        return "experimentlabels/findExperimentLabelsByExperiment";
    }

	@RequestMapping(params = "find=ByExperiment", method = RequestMethod.GET)
    public String findExperimentLabelsByExperiment(@RequestParam("experiment") Experiment experiment, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("experimentlabels", ExperimentLabel.findExperimentLabelsByExperiment(experiment, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ExperimentLabel.countFindExperimentLabelsByExperiment(experiment) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("experimentlabels", ExperimentLabel.findExperimentLabelsByExperiment(experiment, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "experimentlabels/list";
    }

	@RequestMapping(params = { "find=ByExperimentAndIgnoredNot", "form" }, method = RequestMethod.GET)
    public String findExperimentLabelsByExperimentAndIgnoredNotForm(Model uiModel) {
        uiModel.addAttribute("experiments", Experiment.findAllExperiments());
        return "experimentlabels/findExperimentLabelsByExperimentAndIgnoredNot";
    }

	@RequestMapping(params = "find=ByExperimentAndIgnoredNot", method = RequestMethod.GET)
    public String findExperimentLabelsByExperimentAndIgnoredNot(@RequestParam("experiment") Experiment experiment, @RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("experimentlabels", ExperimentLabel.findExperimentLabelsByExperimentAndIgnoredNot(experiment, ignored, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ExperimentLabel.countFindExperimentLabelsByExperimentAndIgnoredNot(experiment, ignored) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("experimentlabels", ExperimentLabel.findExperimentLabelsByExperimentAndIgnoredNot(experiment, ignored, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "experimentlabels/list";
    }

	@RequestMapping(params = { "find=ByLabelTextLike", "form" }, method = RequestMethod.GET)
    public String findExperimentLabelsByLabelTextLikeForm(Model uiModel) {
        return "experimentlabels/findExperimentLabelsByLabelTextLike";
    }

	@RequestMapping(params = "find=ByLabelTextLike", method = RequestMethod.GET)
    public String findExperimentLabelsByLabelTextLike(@RequestParam("labelText") String labelText, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("experimentlabels", ExperimentLabel.findExperimentLabelsByLabelTextLike(labelText, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ExperimentLabel.countFindExperimentLabelsByLabelTextLike(labelText) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("experimentlabels", ExperimentLabel.findExperimentLabelsByLabelTextLike(labelText, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "experimentlabels/list";
    }

	@RequestMapping(params = { "find=ByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot", "form" }, method = RequestMethod.GET)
    public String findExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNotForm(Model uiModel) {
        return "experimentlabels/findExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot";
    }

	@RequestMapping(params = "find=ByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot", method = RequestMethod.GET)
    public String findExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(@RequestParam("labelText") String labelText, @RequestParam("lsTypeAndKind") String lsTypeAndKind, @RequestParam(value = "preferred", required = false) boolean preferred, @RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("experimentlabels", ExperimentLabel.findExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(labelText, lsTypeAndKind, preferred, ignored, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ExperimentLabel.countFindExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(labelText, lsTypeAndKind, preferred, ignored) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("experimentlabels", ExperimentLabel.findExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(labelText, lsTypeAndKind, preferred, ignored, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "experimentlabels/list";
    }

	@RequestMapping(params = { "find=ByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot", "form" }, method = RequestMethod.GET)
    public String findExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNotForm(Model uiModel) {
        return "experimentlabels/findExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot";
    }

	@RequestMapping(params = "find=ByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot", method = RequestMethod.GET)
    public String findExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(@RequestParam("lsTypeAndKind") String lsTypeAndKind, @RequestParam(value = "preferred", required = false) boolean preferred, @RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("experimentlabels", ExperimentLabel.findExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(lsTypeAndKind, preferred, ignored, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ExperimentLabel.countFindExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(lsTypeAndKind, preferred, ignored) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("experimentlabels", ExperimentLabel.findExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(lsTypeAndKind, preferred, ignored, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "experimentlabels/list";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            ExperimentLabel experimentLabel = ExperimentLabel.findExperimentLabel(id);
            if (experimentLabel == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(experimentLabel.toJson(), headers, HttpStatus.OK);
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
            List<ExperimentLabel> result = ExperimentLabel.findAllExperimentLabels();
            return new ResponseEntity<String>(ExperimentLabel.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ExperimentLabel experimentLabel = ExperimentLabel.fromJsonToExperimentLabel(json);
            experimentLabel.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+experimentLabel.getId().toString()).build().toUriString());
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
            for (ExperimentLabel experimentLabel: ExperimentLabel.fromJsonArrayToExperimentLabels(json)) {
                experimentLabel.persist();
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
            ExperimentLabel experimentLabel = ExperimentLabel.fromJsonToExperimentLabel(json);
            experimentLabel.setId(id);
            if (experimentLabel.merge() == null) {
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
            ExperimentLabel experimentLabel = ExperimentLabel.findExperimentLabel(id);
            if (experimentLabel == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            experimentLabel.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByExperiment", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindExperimentLabelsByExperiment(@RequestParam("experiment") Experiment experiment) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ExperimentLabel.toJsonArray(ExperimentLabel.findExperimentLabelsByExperiment(experiment).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByExperimentAndIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindExperimentLabelsByExperimentAndIgnoredNot(@RequestParam("experiment") Experiment experiment, @RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ExperimentLabel.toJsonArray(ExperimentLabel.findExperimentLabelsByExperimentAndIgnoredNot(experiment, ignored).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLabelTextLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindExperimentLabelsByLabelTextLike(@RequestParam("labelText") String labelText) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ExperimentLabel.toJsonArray(ExperimentLabel.findExperimentLabelsByLabelTextLike(labelText).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(@RequestParam("labelText") String labelText, @RequestParam("lsTypeAndKind") String lsTypeAndKind, @RequestParam(value = "preferred", required = false) boolean preferred, @RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ExperimentLabel.toJsonArray(ExperimentLabel.findExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(labelText, lsTypeAndKind, preferred, ignored).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(@RequestParam("lsTypeAndKind") String lsTypeAndKind, @RequestParam(value = "preferred", required = false) boolean preferred, @RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ExperimentLabel.toJsonArray(ExperimentLabel.findExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(lsTypeAndKind, preferred, ignored).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
