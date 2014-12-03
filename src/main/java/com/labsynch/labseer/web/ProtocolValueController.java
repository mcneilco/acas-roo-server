package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.ProtocolValue;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RooWebJson(jsonObject = ProtocolValue.class)
@Controller
@RequestMapping("/protocolvalues")
@RooWebScaffold(path = "protocolvalues", formBackingObject = ProtocolValue.class)
@RooWebFinder
@Transactional
public class ProtocolValueController {

	
    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @Transactional
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        ProtocolValue protocolValue = ProtocolValue.findProtocolValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (protocolValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(protocolValue.toJson(), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ProtocolValue> result = ProtocolValue.findAllProtocolValues();
        return new ResponseEntity<String>(ProtocolValue.toJsonArray(result), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        ProtocolValue protocolValue = ProtocolValue.fromJsonToProtocolValue(json);
        protocolValue.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(protocolValue.toJson(), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        Collection<ProtocolValue> savedProtocolValues = new ArrayList<ProtocolValue>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        StringReader sr = new StringReader(json);
        BufferedReader br = new BufferedReader(sr);
        for (ProtocolValue protocolValue : ProtocolValue.fromJsonArrayToProtocolValues(br)) {
            protocolValue.persist();
            savedProtocolValues.add(protocolValue);
            if (i % batchSize == 0) {
                protocolValue.flush();
                protocolValue.clear();
            }
            i++;
        }
        IOUtils.closeQuietly(sr);
        IOUtils.closeQuietly(br);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(ProtocolValue.toJsonArray(savedProtocolValues), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ProtocolValue protocolValue = ProtocolValue.fromJsonToProtocolValue(json);
        if (protocolValue.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (ProtocolValue protocolValue : ProtocolValue.fromJsonArrayToProtocolValues(json)) {
            if (protocolValue.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        ProtocolValue protocolValue = ProtocolValue.findProtocolValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (protocolValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        protocolValue.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @Transactional
    void populateEditForm(Model uiModel, ProtocolValue protocolValue) {
        uiModel.addAttribute("protocolValue", protocolValue);
        addDateTimeFormatPatterns(uiModel);
        List<ProtocolState> protocolStates = new ArrayList<ProtocolState>();
        protocolStates.add(protocolValue.getLsState());
        uiModel.addAttribute("protocolstates", protocolStates);
    }

    @Transactional
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ProtocolValue protocolValue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, protocolValue);
            return "protocolvalues/create";
        }
        uiModel.asMap().clear();
        protocolValue.persist();
        return "redirect:/protocolvalues/" + encodeUrlPathSegment(protocolValue.getId().toString(), httpServletRequest);
    }

	@Transactional
	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ProtocolValue());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (ProtocolState.countProtocolStates() == 0) {
            dependencies.add(new String[] { "protocolstate", "protocolstates" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "protocolvalues/create";
    }

	@Transactional
	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("protocolvalue", ProtocolValue.findProtocolValue(id));
        uiModel.addAttribute("itemId", id);
        return "protocolvalues/show";
    }

	@Transactional
	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocolvalues", ProtocolValue.findProtocolValueEntries(firstResult, sizeNo));
            float nrOfPages = (float) ProtocolValue.countProtocolValues() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocolvalues", ProtocolValue.findAllProtocolValues());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocolvalues/list";
    }

	@Transactional
	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ProtocolValue protocolValue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, protocolValue);
            return "protocolvalues/update";
        }
        uiModel.asMap().clear();
        protocolValue.merge();
        return "redirect:/protocolvalues/" + encodeUrlPathSegment(protocolValue.getId().toString(), httpServletRequest);
    }

	@Transactional
	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ProtocolValue.findProtocolValue(id));
        return "protocolvalues/update";
    }

	@Transactional
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ProtocolValue protocolValue = ProtocolValue.findProtocolValue(id);
        protocolValue.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/protocolvalues";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("protocolValue_datevalue_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("protocolValue_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("protocolValue_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
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
