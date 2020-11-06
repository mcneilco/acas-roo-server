package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LabelSequenceRole;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.LabelSequenceDTO;
import com.labsynch.labseer.service.AutoLabelService;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import javax.persistence.NonUniqueResultException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@Controller
@RequestMapping("/labelsequences")
public class LabelSequenceController {

	@RequestMapping(params = { "find=ByLabelTypeAndKindEquals", "form" }, method = RequestMethod.GET)
    public String findLabelSequencesByLabelTypeAndKindEqualsForm(Model uiModel) {
        return "labelsequences/findLabelSequencesByLabelTypeAndKindEquals";
    }

	@RequestMapping(params = "find=ByLabelTypeAndKindEquals", method = RequestMethod.GET)
    public String findLabelSequencesByLabelTypeAndKindEquals(@RequestParam("labelTypeAndKind") String labelTypeAndKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("labelsequences", LabelSequence.findLabelSequencesByLabelTypeAndKindEquals(labelTypeAndKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LabelSequence.countFindLabelSequencesByLabelTypeAndKindEquals(labelTypeAndKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("labelsequences", LabelSequence.findLabelSequencesByLabelTypeAndKindEquals(labelTypeAndKind, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "labelsequences/list";
    }

	@RequestMapping(params = { "find=ByThingTypeAndKindEquals", "form" }, method = RequestMethod.GET)
    public String findLabelSequencesByThingTypeAndKindEqualsForm(Model uiModel) {
        return "labelsequences/findLabelSequencesByThingTypeAndKindEquals";
    }

	@RequestMapping(params = "find=ByThingTypeAndKindEquals", method = RequestMethod.GET)
    public String findLabelSequencesByThingTypeAndKindEquals(@RequestParam("thingTypeAndKind") String thingTypeAndKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("labelsequences", LabelSequence.findLabelSequencesByThingTypeAndKindEquals(thingTypeAndKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LabelSequence.countFindLabelSequencesByThingTypeAndKindEquals(thingTypeAndKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("labelsequences", LabelSequence.findLabelSequencesByThingTypeAndKindEquals(thingTypeAndKind, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "labelsequences/list";
    }

	@RequestMapping(params = { "find=ByThingTypeAndKindEqualsAndLabelTypeAndKindEquals", "form" }, method = RequestMethod.GET)
    public String findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsForm(Model uiModel) {
        return "labelsequences/findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals";
    }

	@RequestMapping(params = "find=ByThingTypeAndKindEqualsAndLabelTypeAndKindEquals", method = RequestMethod.GET)
    public String findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(@RequestParam("thingTypeAndKind") String thingTypeAndKind, @RequestParam("labelTypeAndKind") String labelTypeAndKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("labelsequences", LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LabelSequence.countFindLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("labelsequences", LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "labelsequences/list";
    }

	@RequestMapping(params = { "find=ByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals", "form" }, method = RequestMethod.GET)
    public String findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEqualsForm(Model uiModel) {
        return "labelsequences/findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals";
    }

	@RequestMapping(params = "find=ByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals", method = RequestMethod.GET)
    public String findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals(@RequestParam("thingTypeAndKind") String thingTypeAndKind, @RequestParam("labelTypeAndKind") String labelTypeAndKind, @RequestParam("labelPrefix") String labelPrefix, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("labelsequences", LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals(thingTypeAndKind, labelTypeAndKind, labelPrefix, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LabelSequence.countFindLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals(thingTypeAndKind, labelTypeAndKind, labelPrefix) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("labelsequences", LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals(thingTypeAndKind, labelTypeAndKind, labelPrefix, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "labelsequences/list";
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LabelSequence labelSequence, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, labelSequence);
            return "labelsequences/create";
        }
        uiModel.asMap().clear();
        labelSequence.persist();
        return "redirect:/labelsequences/" + encodeUrlPathSegment(labelSequence.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LabelSequence());
        return "labelsequences/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("labelsequence", LabelSequence.findLabelSequence(id));
        uiModel.addAttribute("itemId", id);
        return "labelsequences/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("labelsequences", LabelSequence.findLabelSequenceEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LabelSequence.countLabelSequences() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("labelsequences", LabelSequence.findAllLabelSequences(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "labelsequences/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LabelSequence labelSequence, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, labelSequence);
            return "labelsequences/update";
        }
        uiModel.asMap().clear();
        labelSequence.merge();
        return "redirect:/labelsequences/" + encodeUrlPathSegment(labelSequence.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LabelSequence.findLabelSequence(id));
        return "labelsequences/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LabelSequence labelSequence = LabelSequence.findLabelSequence(id);
        labelSequence.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/labelsequences";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("labelSequence_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, LabelSequence labelSequence) {
        uiModel.addAttribute("labelSequence", labelSequence);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("labelsequenceroles", LabelSequenceRole.findAllLabelSequenceRoles());
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
            LabelSequence labelSequence = LabelSequence.findLabelSequence(id);
            if (labelSequence == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(labelSequence.toJson(), headers, HttpStatus.OK);
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
            List<LabelSequence> result = LabelSequence.findAllLabelSequences();
            return new ResponseEntity<String>(LabelSequence.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            LabelSequence labelSequence = LabelSequence.fromJsonToLabelSequence(json);
            labelSequence.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+labelSequence.getId().toString()).build().toUriString());
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
            for (LabelSequence labelSequence: LabelSequence.fromJsonArrayToLabelSequences(json)) {
                labelSequence.persist();
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
            LabelSequence labelSequence = LabelSequence.fromJsonToLabelSequence(json);
            labelSequence.setId(id);
            if (labelSequence.merge() == null) {
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
            LabelSequence labelSequence = LabelSequence.findLabelSequence(id);
            if (labelSequence == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            labelSequence.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLabelTypeAndKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLabelSequencesByLabelTypeAndKindEquals(@RequestParam("labelTypeAndKind") String labelTypeAndKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LabelSequence.toJsonArray(LabelSequence.findLabelSequencesByLabelTypeAndKindEquals(labelTypeAndKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByThingTypeAndKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLabelSequencesByThingTypeAndKindEquals(@RequestParam("thingTypeAndKind") String thingTypeAndKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LabelSequence.toJsonArray(LabelSequence.findLabelSequencesByThingTypeAndKindEquals(thingTypeAndKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByThingTypeAndKindEqualsAndLabelTypeAndKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(@RequestParam("thingTypeAndKind") String thingTypeAndKind, @RequestParam("labelTypeAndKind") String labelTypeAndKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LabelSequence.toJsonArray(LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals(@RequestParam("thingTypeAndKind") String thingTypeAndKind, @RequestParam("labelTypeAndKind") String labelTypeAndKind, @RequestParam("labelPrefix") String labelPrefix) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LabelSequence.toJsonArray(LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEqualsAndLabelPrefixEquals(thingTypeAndKind, labelTypeAndKind, labelPrefix).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
