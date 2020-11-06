package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.ItxSubjectContainerState;
import com.labsynch.labseer.domain.ItxSubjectContainerValue;
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

@RooWebJson(jsonObject = ItxSubjectContainerState.class)
@Controller
@RequestMapping("/itxsubjectcontainerstates")
@RooWebScaffold(path = "itxsubjectcontainerstates", formBackingObject = ItxSubjectContainerState.class)
public class ItxSubjectContainerStateController {

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            ItxSubjectContainerState itxSubjectContainerState = ItxSubjectContainerState.findItxSubjectContainerState(id);
            if (itxSubjectContainerState == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(itxSubjectContainerState.toJson(), headers, HttpStatus.OK);
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
            List<ItxSubjectContainerState> result = ItxSubjectContainerState.findAllItxSubjectContainerStates();
            return new ResponseEntity<String>(ItxSubjectContainerState.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ItxSubjectContainerState itxSubjectContainerState = ItxSubjectContainerState.fromJsonToItxSubjectContainerState(json);
            itxSubjectContainerState.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+itxSubjectContainerState.getId().toString()).build().toUriString());
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
            for (ItxSubjectContainerState itxSubjectContainerState: ItxSubjectContainerState.fromJsonArrayToItxSubjectContainerStates(json)) {
                itxSubjectContainerState.persist();
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
            ItxSubjectContainerState itxSubjectContainerState = ItxSubjectContainerState.fromJsonToItxSubjectContainerState(json);
            itxSubjectContainerState.setId(id);
            if (itxSubjectContainerState.merge() == null) {
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
            ItxSubjectContainerState itxSubjectContainerState = ItxSubjectContainerState.findItxSubjectContainerState(id);
            if (itxSubjectContainerState == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            itxSubjectContainerState.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ItxSubjectContainerState itxSubjectContainerState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itxSubjectContainerState);
            return "itxsubjectcontainerstates/create";
        }
        uiModel.asMap().clear();
        itxSubjectContainerState.persist();
        return "redirect:/itxsubjectcontainerstates/" + encodeUrlPathSegment(itxSubjectContainerState.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ItxSubjectContainerState());
        return "itxsubjectcontainerstates/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itxsubjectcontainerstate", ItxSubjectContainerState.findItxSubjectContainerState(id));
        uiModel.addAttribute("itemId", id);
        return "itxsubjectcontainerstates/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxsubjectcontainerstates", ItxSubjectContainerState.findItxSubjectContainerStateEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ItxSubjectContainerState.countItxSubjectContainerStates() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxsubjectcontainerstates", ItxSubjectContainerState.findAllItxSubjectContainerStates(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxsubjectcontainerstates/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ItxSubjectContainerState itxSubjectContainerState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itxSubjectContainerState);
            return "itxsubjectcontainerstates/update";
        }
        uiModel.asMap().clear();
        itxSubjectContainerState.merge();
        return "redirect:/itxsubjectcontainerstates/" + encodeUrlPathSegment(itxSubjectContainerState.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ItxSubjectContainerState.findItxSubjectContainerState(id));
        return "itxsubjectcontainerstates/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ItxSubjectContainerState itxSubjectContainerState = ItxSubjectContainerState.findItxSubjectContainerState(id);
        itxSubjectContainerState.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/itxsubjectcontainerstates";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("itxSubjectContainerState_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("itxSubjectContainerState_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, ItxSubjectContainerState itxSubjectContainerState) {
        uiModel.addAttribute("itxSubjectContainerState", itxSubjectContainerState);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itxsubjectcontainers", ItxSubjectContainer.findAllItxSubjectContainers());
        uiModel.addAttribute("itxsubjectcontainervalues", ItxSubjectContainerValue.findAllItxSubjectContainerValues());
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
