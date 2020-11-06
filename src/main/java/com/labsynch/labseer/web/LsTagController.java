package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.LsTag;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.Protocol;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashSet;
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

@RooWebJson(jsonObject = LsTag.class)
@Transactional
@RooWebFinder
@RequestMapping("/lstags")
@Controller
@RooWebScaffold(path = "lstags", formBackingObject = LsTag.class)
public class LsTagController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LsTag lsTag, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsTag);
            return "lstags/create";
        }
        uiModel.asMap().clear();
        lsTag.persist();
        return "redirect:/lstags/" + encodeUrlPathSegment(lsTag.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LsTag());
        return "lstags/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("lstag", LsTag.findLsTag(id));
        uiModel.addAttribute("itemId", id);
        return "lstags/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lstags", LsTag.findLsTagEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LsTag.countLsTags() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lstags", LsTag.findAllLsTags(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "lstags/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LsTag lsTag, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsTag);
            return "lstags/update";
        }
        uiModel.asMap().clear();
        lsTag.merge();
        return "redirect:/lstags/" + encodeUrlPathSegment(lsTag.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LsTag.findLsTag(id));
        return "lstags/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LsTag lsTag = LsTag.findLsTag(id);
        lsTag.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lstags";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("lsTag_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, LsTag lsTag) {
        uiModel.addAttribute("lsTag", lsTag);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("experiments", Experiment.findAllExperiments());
        uiModel.addAttribute("lsthings", LsThing.findAllLsThings());
        uiModel.addAttribute("protocols", Protocol.findAllProtocols());
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
            LsTag lsTag = LsTag.findLsTag(id);
            if (lsTag == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(lsTag.toJson(), headers, HttpStatus.OK);
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
            List<LsTag> result = LsTag.findAllLsTags();
            return new ResponseEntity<String>(LsTag.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            LsTag lsTag = LsTag.fromJsonToLsTag(json);
            lsTag.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+lsTag.getId().toString()).build().toUriString());
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
            for (LsTag lsTag: LsTag.fromJsonArrayToLsTags(json)) {
                lsTag.persist();
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
            LsTag lsTag = LsTag.fromJsonToLsTag(json);
            lsTag.setId(id);
            if (lsTag.merge() == null) {
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
            LsTag lsTag = LsTag.findLsTag(id);
            if (lsTag == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            lsTag.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByTagTextEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsTagsByTagTextEquals(@RequestParam("tagText") String tagText) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsTag.toJsonArray(LsTag.findLsTagsByTagTextEquals(tagText).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByTagTextLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsTagsByTagTextLike(@RequestParam("tagText") String tagText) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsTag.toJsonArray(LsTag.findLsTagsByTagTextLike(tagText).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = { "find=ByTagTextEquals", "form" }, method = RequestMethod.GET)
    public String findLsTagsByTagTextEqualsForm(Model uiModel) {
        return "lstags/findLsTagsByTagTextEquals";
    }

	@RequestMapping(params = "find=ByTagTextEquals", method = RequestMethod.GET)
    public String findLsTagsByTagTextEquals(@RequestParam("tagText") String tagText, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lstags", LsTag.findLsTagsByTagTextEquals(tagText, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsTag.countFindLsTagsByTagTextEquals(tagText) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lstags", LsTag.findLsTagsByTagTextEquals(tagText, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lstags/list";
    }

	@RequestMapping(params = { "find=ByTagTextLike", "form" }, method = RequestMethod.GET)
    public String findLsTagsByTagTextLikeForm(Model uiModel) {
        return "lstags/findLsTagsByTagTextLike";
    }

	@RequestMapping(params = "find=ByTagTextLike", method = RequestMethod.GET)
    public String findLsTagsByTagTextLike(@RequestParam("tagText") String tagText, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lstags", LsTag.findLsTagsByTagTextLike(tagText, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsTag.countFindLsTagsByTagTextLike(tagText) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lstags", LsTag.findLsTagsByTagTextLike(tagText, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lstags/list";
    }
}
