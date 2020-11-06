package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
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

@RooWebJson(jsonObject = LsThingLabel.class)
@Controller
@RequestMapping("/lsthinglabels")
@RooWebScaffold(path = "lsthinglabels", formBackingObject = LsThingLabel.class)
@RooWebFinder
public class LsThingLabelController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LsThingLabel lsThingLabel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsThingLabel);
            return "lsthinglabels/create";
        }
        uiModel.asMap().clear();
        lsThingLabel.persist();
        return "redirect:/lsthinglabels/" + encodeUrlPathSegment(lsThingLabel.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LsThingLabel());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (LsThing.countLsThings() == 0) {
            dependencies.add(new String[] { "lsThing", "lsthings" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "lsthinglabels/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("lsthinglabel", LsThingLabel.findLsThingLabel(id));
        uiModel.addAttribute("itemId", id);
        return "lsthinglabels/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthinglabels", LsThingLabel.findLsThingLabelEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LsThingLabel.countLsThingLabels() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthinglabels", LsThingLabel.findAllLsThingLabels(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthinglabels/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LsThingLabel lsThingLabel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsThingLabel);
            return "lsthinglabels/update";
        }
        uiModel.asMap().clear();
        lsThingLabel.merge();
        return "redirect:/lsthinglabels/" + encodeUrlPathSegment(lsThingLabel.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LsThingLabel.findLsThingLabel(id));
        return "lsthinglabels/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LsThingLabel lsThingLabel = LsThingLabel.findLsThingLabel(id);
        lsThingLabel.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lsthinglabels";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("lsThingLabel_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("lsThingLabel_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, LsThingLabel lsThingLabel) {
        uiModel.addAttribute("lsThingLabel", lsThingLabel);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("lsthings", LsThing.findAllLsThings());
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

	@RequestMapping(params = { "find=ByLabelTextEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingLabelsByLabelTextEqualsForm(Model uiModel) {
        return "lsthinglabels/findLsThingLabelsByLabelTextEquals";
    }

	@RequestMapping(params = "find=ByLabelTextEquals", method = RequestMethod.GET)
    public String findLsThingLabelsByLabelTextEquals(@RequestParam("labelText") String labelText, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthinglabels", LsThingLabel.findLsThingLabelsByLabelTextEquals(labelText, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingLabel.countFindLsThingLabelsByLabelTextEquals(labelText) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthinglabels", LsThingLabel.findLsThingLabelsByLabelTextEquals(labelText, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthinglabels/list";
    }

	@RequestMapping(params = { "find=ByLabelTextEqualsAndIgnoredNot", "form" }, method = RequestMethod.GET)
    public String findLsThingLabelsByLabelTextEqualsAndIgnoredNotForm(Model uiModel) {
        return "lsthinglabels/findLsThingLabelsByLabelTextEqualsAndIgnoredNot";
    }

	@RequestMapping(params = "find=ByLabelTextEqualsAndIgnoredNot", method = RequestMethod.GET)
    public String findLsThingLabelsByLabelTextEqualsAndIgnoredNot(@RequestParam("labelText") String labelText, @RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthinglabels", LsThingLabel.findLsThingLabelsByLabelTextEqualsAndIgnoredNot(labelText, ignored, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingLabel.countFindLsThingLabelsByLabelTextEqualsAndIgnoredNot(labelText, ignored) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthinglabels", LsThingLabel.findLsThingLabelsByLabelTextEqualsAndIgnoredNot(labelText, ignored, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthinglabels/list";
    }

	@RequestMapping(params = { "find=ByLabelTextLike", "form" }, method = RequestMethod.GET)
    public String findLsThingLabelsByLabelTextLikeForm(Model uiModel) {
        return "lsthinglabels/findLsThingLabelsByLabelTextLike";
    }

	@RequestMapping(params = "find=ByLabelTextLike", method = RequestMethod.GET)
    public String findLsThingLabelsByLabelTextLike(@RequestParam("labelText") String labelText, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthinglabels", LsThingLabel.findLsThingLabelsByLabelTextLike(labelText, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingLabel.countFindLsThingLabelsByLabelTextLike(labelText) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthinglabels", LsThingLabel.findLsThingLabelsByLabelTextLike(labelText, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthinglabels/list";
    }

	@RequestMapping(params = { "find=ByLsThing", "form" }, method = RequestMethod.GET)
    public String findLsThingLabelsByLsThingForm(Model uiModel) {
        uiModel.addAttribute("lsthings", LsThing.findAllLsThings());
        return "lsthinglabels/findLsThingLabelsByLsThing";
    }

	@RequestMapping(params = "find=ByLsThing", method = RequestMethod.GET)
    public String findLsThingLabelsByLsThing(@RequestParam("lsThing") LsThing lsThing, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthinglabels", LsThingLabel.findLsThingLabelsByLsThing(lsThing, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingLabel.countFindLsThingLabelsByLsThing(lsThing) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthinglabels", LsThingLabel.findLsThingLabelsByLsThing(lsThing, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthinglabels/list";
    }

	@RequestMapping(params = { "find=ByLsTransactionEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingLabelsByLsTransactionEqualsForm(Model uiModel) {
        return "lsthinglabels/findLsThingLabelsByLsTransactionEquals";
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET)
    public String findLsThingLabelsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthinglabels", LsThingLabel.findLsThingLabelsByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingLabel.countFindLsThingLabelsByLsTransactionEquals(lsTransaction) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthinglabels", LsThingLabel.findLsThingLabelsByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthinglabels/list";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            LsThingLabel lsThingLabel = LsThingLabel.findLsThingLabel(id);
            if (lsThingLabel == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(lsThingLabel.toJson(), headers, HttpStatus.OK);
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
            List<LsThingLabel> result = LsThingLabel.findAllLsThingLabels();
            return new ResponseEntity<String>(LsThingLabel.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            LsThingLabel lsThingLabel = LsThingLabel.fromJsonToLsThingLabel(json);
            lsThingLabel.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+lsThingLabel.getId().toString()).build().toUriString());
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
            for (LsThingLabel lsThingLabel: LsThingLabel.fromJsonArrayToLsThingLabels(json)) {
                lsThingLabel.persist();
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
            LsThingLabel lsThingLabel = LsThingLabel.fromJsonToLsThingLabel(json);
            lsThingLabel.setId(id);
            if (lsThingLabel.merge() == null) {
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
            LsThingLabel lsThingLabel = LsThingLabel.findLsThingLabel(id);
            if (lsThingLabel == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            lsThingLabel.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLabelTextEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingLabelsByLabelTextEquals(@RequestParam("labelText") String labelText) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingLabel.toJsonArray(LsThingLabel.findLsThingLabelsByLabelTextEquals(labelText).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLabelTextEqualsAndIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingLabelsByLabelTextEqualsAndIgnoredNot(@RequestParam("labelText") String labelText, @RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingLabel.toJsonArray(LsThingLabel.findLsThingLabelsByLabelTextEqualsAndIgnoredNot(labelText, ignored).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLabelTextLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingLabelsByLabelTextLike(@RequestParam("labelText") String labelText) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingLabel.toJsonArray(LsThingLabel.findLsThingLabelsByLabelTextLike(labelText).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsThing", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingLabelsByLsThing(@RequestParam("lsThing") LsThing lsThing) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingLabel.toJsonArray(LsThingLabel.findLsThingLabelsByLsThing(lsThing).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingLabelsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingLabel.toJsonArray(LsThingLabel.findLsThingLabelsByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
