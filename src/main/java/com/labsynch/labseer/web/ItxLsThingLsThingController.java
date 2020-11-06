package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.ThingPage;
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
@RequestMapping("/itxlsthinglsthings")
public class ItxLsThingLsThingController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ItxLsThingLsThing itxLsThingLsThing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itxLsThingLsThing);
            return "itxlsthinglsthings/create";
        }
        uiModel.asMap().clear();
        itxLsThingLsThing.persist();
        return "redirect:/itxlsthinglsthings/" + encodeUrlPathSegment(itxLsThingLsThing.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ItxLsThingLsThing());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (LsThing.countLsThings() == 0) {
            dependencies.add(new String[] { "firstLsThing", "lsthings" });
        }
        if (LsThing.countLsThings() == 0) {
            dependencies.add(new String[] { "secondLsThing", "lsthings" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "itxlsthinglsthings/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itxlsthinglsthing", ItxLsThingLsThing.findItxLsThingLsThing(id));
        uiModel.addAttribute("itemId", id);
        return "itxlsthinglsthings/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxlsthinglsthings", ItxLsThingLsThing.findItxLsThingLsThingEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ItxLsThingLsThing.countItxLsThingLsThings() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxlsthinglsthings", ItxLsThingLsThing.findAllItxLsThingLsThings(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxlsthinglsthings/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ItxLsThingLsThing itxLsThingLsThing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itxLsThingLsThing);
            return "itxlsthinglsthings/update";
        }
        uiModel.asMap().clear();
        itxLsThingLsThing.merge();
        return "redirect:/itxlsthinglsthings/" + encodeUrlPathSegment(itxLsThingLsThing.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ItxLsThingLsThing.findItxLsThingLsThing(id));
        return "itxlsthinglsthings/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ItxLsThingLsThing itxLsThingLsThing = ItxLsThingLsThing.findItxLsThingLsThing(id);
        itxLsThingLsThing.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/itxlsthinglsthings";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("itxLsThingLsThing_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("itxLsThingLsThing_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, ItxLsThingLsThing itxLsThingLsThing) {
        uiModel.addAttribute("itxLsThingLsThing", itxLsThingLsThing);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itxlsthinglsthingstates", ItxLsThingLsThingState.findAllItxLsThingLsThingStates());
        uiModel.addAttribute("lsthings", LsThing.findAllLsThings());
        uiModel.addAttribute("thingpages", ThingPage.findAllThingPages());
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

	@RequestMapping(params = { "find=ByCodeNameEquals", "form" }, method = RequestMethod.GET)
    public String findItxLsThingLsThingsByCodeNameEqualsForm(Model uiModel) {
        return "itxlsthinglsthings/findItxLsThingLsThingsByCodeNameEquals";
    }

	@RequestMapping(params = "find=ByCodeNameEquals", method = RequestMethod.GET)
    public String findItxLsThingLsThingsByCodeNameEquals(@RequestParam("codeName") String codeName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxlsthinglsthings", ItxLsThingLsThing.findItxLsThingLsThingsByCodeNameEquals(codeName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ItxLsThingLsThing.countFindItxLsThingLsThingsByCodeNameEquals(codeName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxlsthinglsthings", ItxLsThingLsThing.findItxLsThingLsThingsByCodeNameEquals(codeName, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxlsthinglsthings/list";
    }

	@RequestMapping(params = { "find=ByFirstLsThing", "form" }, method = RequestMethod.GET)
    public String findItxLsThingLsThingsByFirstLsThingForm(Model uiModel) {
        uiModel.addAttribute("lsthings", LsThing.findAllLsThings());
        return "itxlsthinglsthings/findItxLsThingLsThingsByFirstLsThing";
    }

	@RequestMapping(params = "find=ByFirstLsThing", method = RequestMethod.GET)
    public String findItxLsThingLsThingsByFirstLsThing(@RequestParam("firstLsThing") LsThing firstLsThing, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxlsthinglsthings", ItxLsThingLsThing.findItxLsThingLsThingsByFirstLsThing(firstLsThing, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ItxLsThingLsThing.countFindItxLsThingLsThingsByFirstLsThing(firstLsThing) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxlsthinglsthings", ItxLsThingLsThing.findItxLsThingLsThingsByFirstLsThing(firstLsThing, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxlsthinglsthings/list";
    }

	@RequestMapping(params = { "find=BySecondLsThing", "form" }, method = RequestMethod.GET)
    public String findItxLsThingLsThingsBySecondLsThingForm(Model uiModel) {
        uiModel.addAttribute("lsthings", LsThing.findAllLsThings());
        return "itxlsthinglsthings/findItxLsThingLsThingsBySecondLsThing";
    }

	@RequestMapping(params = "find=BySecondLsThing", method = RequestMethod.GET)
    public String findItxLsThingLsThingsBySecondLsThing(@RequestParam("secondLsThing") LsThing secondLsThing, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxlsthinglsthings", ItxLsThingLsThing.findItxLsThingLsThingsBySecondLsThing(secondLsThing, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ItxLsThingLsThing.countFindItxLsThingLsThingsBySecondLsThing(secondLsThing) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxlsthinglsthings", ItxLsThingLsThing.findItxLsThingLsThingsBySecondLsThing(secondLsThing, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxlsthinglsthings/list";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            ItxLsThingLsThing itxLsThingLsThing = ItxLsThingLsThing.findItxLsThingLsThing(id);
            if (itxLsThingLsThing == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(itxLsThingLsThing.toJson(), headers, HttpStatus.OK);
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
            List<ItxLsThingLsThing> result = ItxLsThingLsThing.findAllItxLsThingLsThings();
            return new ResponseEntity<String>(ItxLsThingLsThing.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ItxLsThingLsThing itxLsThingLsThing = ItxLsThingLsThing.fromJsonToItxLsThingLsThing(json);
            itxLsThingLsThing.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+itxLsThingLsThing.getId().toString()).build().toUriString());
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
            for (ItxLsThingLsThing itxLsThingLsThing: ItxLsThingLsThing.fromJsonArrayToItxLsThingLsThings(json)) {
                itxLsThingLsThing.persist();
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
            ItxLsThingLsThing itxLsThingLsThing = ItxLsThingLsThing.fromJsonToItxLsThingLsThing(json);
            itxLsThingLsThing.setId(id);
            if (itxLsThingLsThing.merge() == null) {
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
            ItxLsThingLsThing itxLsThingLsThing = ItxLsThingLsThing.findItxLsThingLsThing(id);
            if (itxLsThingLsThing == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            itxLsThingLsThing.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByCodeNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindItxLsThingLsThingsByCodeNameEquals(@RequestParam("codeName") String codeName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ItxLsThingLsThing.toJsonArray(ItxLsThingLsThing.findItxLsThingLsThingsByCodeNameEquals(codeName).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByFirstLsThing", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindItxLsThingLsThingsByFirstLsThing(@RequestParam("firstLsThing") LsThing firstLsThing) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ItxLsThingLsThing.toJsonArray(ItxLsThingLsThing.findItxLsThingLsThingsByFirstLsThing(firstLsThing).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=BySecondLsThing", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindItxLsThingLsThingsBySecondLsThing(@RequestParam("secondLsThing") LsThing secondLsThing) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ItxLsThingLsThing.toJsonArray(ItxLsThingLsThing.findItxLsThingLsThingsBySecondLsThing(secondLsThing).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
