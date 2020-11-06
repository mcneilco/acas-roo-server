package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxProtocolProtocol;
import com.labsynch.labseer.domain.ItxProtocolProtocolState;
import com.labsynch.labseer.domain.ItxProtocolProtocolValue;
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

@RequestMapping("/itxprotocolprotocolstates")
@Controller
@RooWebScaffold(path = "itxprotocolprotocolstates", formBackingObject = ItxProtocolProtocolState.class)
@RooWebJson(jsonObject = ItxProtocolProtocolState.class)
public class ItxProtocolProtocolStateController {

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            ItxProtocolProtocolState itxProtocolProtocolState = ItxProtocolProtocolState.findItxProtocolProtocolState(id);
            if (itxProtocolProtocolState == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(itxProtocolProtocolState.toJson(), headers, HttpStatus.OK);
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
            List<ItxProtocolProtocolState> result = ItxProtocolProtocolState.findAllItxProtocolProtocolStates();
            return new ResponseEntity<String>(ItxProtocolProtocolState.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ItxProtocolProtocolState itxProtocolProtocolState = ItxProtocolProtocolState.fromJsonToItxProtocolProtocolState(json);
            itxProtocolProtocolState.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+itxProtocolProtocolState.getId().toString()).build().toUriString());
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
            for (ItxProtocolProtocolState itxProtocolProtocolState: ItxProtocolProtocolState.fromJsonArrayToItxProtocolProtocolStates(json)) {
                itxProtocolProtocolState.persist();
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
            ItxProtocolProtocolState itxProtocolProtocolState = ItxProtocolProtocolState.fromJsonToItxProtocolProtocolState(json);
            itxProtocolProtocolState.setId(id);
            if (itxProtocolProtocolState.merge() == null) {
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
            ItxProtocolProtocolState itxProtocolProtocolState = ItxProtocolProtocolState.findItxProtocolProtocolState(id);
            if (itxProtocolProtocolState == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            itxProtocolProtocolState.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ItxProtocolProtocolState itxProtocolProtocolState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itxProtocolProtocolState);
            return "itxprotocolprotocolstates/create";
        }
        uiModel.asMap().clear();
        itxProtocolProtocolState.persist();
        return "redirect:/itxprotocolprotocolstates/" + encodeUrlPathSegment(itxProtocolProtocolState.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ItxProtocolProtocolState());
        return "itxprotocolprotocolstates/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itxprotocolprotocolstate", ItxProtocolProtocolState.findItxProtocolProtocolState(id));
        uiModel.addAttribute("itemId", id);
        return "itxprotocolprotocolstates/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxprotocolprotocolstates", ItxProtocolProtocolState.findItxProtocolProtocolStateEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ItxProtocolProtocolState.countItxProtocolProtocolStates() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxprotocolprotocolstates", ItxProtocolProtocolState.findAllItxProtocolProtocolStates(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxprotocolprotocolstates/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ItxProtocolProtocolState itxProtocolProtocolState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itxProtocolProtocolState);
            return "itxprotocolprotocolstates/update";
        }
        uiModel.asMap().clear();
        itxProtocolProtocolState.merge();
        return "redirect:/itxprotocolprotocolstates/" + encodeUrlPathSegment(itxProtocolProtocolState.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ItxProtocolProtocolState.findItxProtocolProtocolState(id));
        return "itxprotocolprotocolstates/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ItxProtocolProtocolState itxProtocolProtocolState = ItxProtocolProtocolState.findItxProtocolProtocolState(id);
        itxProtocolProtocolState.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/itxprotocolprotocolstates";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("itxProtocolProtocolState_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("itxProtocolProtocolState_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, ItxProtocolProtocolState itxProtocolProtocolState) {
        uiModel.addAttribute("itxProtocolProtocolState", itxProtocolProtocolState);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itxprotocolprotocols", ItxProtocolProtocol.findAllItxProtocolProtocols());
        uiModel.addAttribute("itxprotocolprotocolvalues", ItxProtocolProtocolValue.findAllItxProtocolProtocolValues());
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
