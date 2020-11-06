package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxExperimentExperiment;
import com.labsynch.labseer.domain.ItxExperimentExperimentState;
import com.labsynch.labseer.domain.ItxExperimentExperimentValue;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RooWebJson(jsonObject = ItxExperimentExperimentState.class)
@Controller
@RequestMapping("/itxexperimentexperimentstates")
@RooWebScaffold(path = "itxexperimentexperimentstates", formBackingObject = ItxExperimentExperimentState.class)
public class ItxExperimentExperimentStateController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ItxExperimentExperimentState itxExperimentExperimentState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itxExperimentExperimentState);
            return "itxexperimentexperimentstates/create";
        }
        uiModel.asMap().clear();
        itxExperimentExperimentState.persist();
        return "redirect:/itxexperimentexperimentstates/" + encodeUrlPathSegment(itxExperimentExperimentState.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ItxExperimentExperimentState());
        return "itxexperimentexperimentstates/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itxexperimentexperimentstate", ItxExperimentExperimentState.findItxExperimentExperimentState(id));
        uiModel.addAttribute("itemId", id);
        return "itxexperimentexperimentstates/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxexperimentexperimentstates", ItxExperimentExperimentState.findItxExperimentExperimentStateEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ItxExperimentExperimentState.countItxExperimentExperimentStates() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxexperimentexperimentstates", ItxExperimentExperimentState.findAllItxExperimentExperimentStates(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxexperimentexperimentstates/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ItxExperimentExperimentState itxExperimentExperimentState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itxExperimentExperimentState);
            return "itxexperimentexperimentstates/update";
        }
        uiModel.asMap().clear();
        itxExperimentExperimentState.merge();
        return "redirect:/itxexperimentexperimentstates/" + encodeUrlPathSegment(itxExperimentExperimentState.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ItxExperimentExperimentState.findItxExperimentExperimentState(id));
        return "itxexperimentexperimentstates/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ItxExperimentExperimentState itxExperimentExperimentState = ItxExperimentExperimentState.findItxExperimentExperimentState(id);
        itxExperimentExperimentState.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/itxexperimentexperimentstates";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("itxExperimentExperimentState_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("itxExperimentExperimentState_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, ItxExperimentExperimentState itxExperimentExperimentState) {
        uiModel.addAttribute("itxExperimentExperimentState", itxExperimentExperimentState);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itxexperimentexperiments", ItxExperimentExperiment.findAllItxExperimentExperiments());
        uiModel.addAttribute("itxexperimentexperimentvalues", ItxExperimentExperimentValue.findAllItxExperimentExperimentValues());
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
            ItxExperimentExperimentState itxExperimentExperimentState = ItxExperimentExperimentState.findItxExperimentExperimentState(id);
            if (itxExperimentExperimentState == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(itxExperimentExperimentState.toJson(), headers, HttpStatus.OK);
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
            List<ItxExperimentExperimentState> result = ItxExperimentExperimentState.findAllItxExperimentExperimentStates();
            return new ResponseEntity<String>(ItxExperimentExperimentState.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ItxExperimentExperimentState itxExperimentExperimentState = ItxExperimentExperimentState.fromJsonToItxExperimentExperimentState(json);
            itxExperimentExperimentState.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+itxExperimentExperimentState.getId().toString()).build().toUriString());
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
            for (ItxExperimentExperimentState itxExperimentExperimentState: ItxExperimentExperimentState.fromJsonArrayToItxExperimentExperimentStates(json)) {
                itxExperimentExperimentState.persist();
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
            ItxExperimentExperimentState itxExperimentExperimentState = ItxExperimentExperimentState.fromJsonToItxExperimentExperimentState(json);
            itxExperimentExperimentState.setId(id);
            if (itxExperimentExperimentState.merge() == null) {
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
            ItxExperimentExperimentState itxExperimentExperimentState = ItxExperimentExperimentState.findItxExperimentExperimentState(id);
            if (itxExperimentExperimentState == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            itxExperimentExperimentState.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
