package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Protocol;
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
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RooWebJson(jsonObject = ProtocolState.class)
@Controller
@RequestMapping("/protocolstates")
@RooWebScaffold(path = "protocolstates", formBackingObject = ProtocolState.class)
@RooWebFinder
@Transactional
public class ProtocolStateController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ProtocolState protocolState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, protocolState);
            return "protocolstates/create";
        }
        uiModel.asMap().clear();
        protocolState.persist();
        return "redirect:/protocolstates/" + encodeUrlPathSegment(protocolState.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ProtocolState());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Protocol.countProtocols() == 0) {
            dependencies.add(new String[] { "protocol", "protocols" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "protocolstates/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("protocolstate", ProtocolState.findProtocolState(id));
        uiModel.addAttribute("itemId", id);
        return "protocolstates/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocolstates", ProtocolState.findProtocolStateEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ProtocolState.countProtocolStates() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocolstates", ProtocolState.findAllProtocolStates(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocolstates/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ProtocolState protocolState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, protocolState);
            return "protocolstates/update";
        }
        uiModel.asMap().clear();
        protocolState.merge();
        return "redirect:/protocolstates/" + encodeUrlPathSegment(protocolState.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ProtocolState.findProtocolState(id));
        return "protocolstates/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ProtocolState protocolState = ProtocolState.findProtocolState(id);
        protocolState.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/protocolstates";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("protocolState_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("protocolState_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, ProtocolState protocolState) {
        uiModel.addAttribute("protocolState", protocolState);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("protocols", Protocol.findAllProtocols());
        uiModel.addAttribute("protocolvalues", ProtocolValue.findAllProtocolValues());
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
            ProtocolState protocolState = ProtocolState.findProtocolState(id);
            if (protocolState == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(protocolState.toJson(), headers, HttpStatus.OK);
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
            List<ProtocolState> result = ProtocolState.findAllProtocolStates();
            return new ResponseEntity<String>(ProtocolState.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ProtocolState protocolState = ProtocolState.fromJsonToProtocolState(json);
            protocolState.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+protocolState.getId().toString()).build().toUriString());
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
            for (ProtocolState protocolState: ProtocolState.fromJsonArrayToProtocolStates(json)) {
                protocolState.persist();
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
            ProtocolState protocolState = ProtocolState.fromJsonToProtocolState(json);
            protocolState.setId(id);
            if (protocolState.merge() == null) {
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
            ProtocolState protocolState = ProtocolState.findProtocolState(id);
            if (protocolState == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            protocolState.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolStatesByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ProtocolState.toJsonArray(ProtocolState.findProtocolStatesByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByProtocol", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolStatesByProtocol(@RequestParam("protocol") Protocol protocol) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ProtocolState.toJsonArray(ProtocolState.findProtocolStatesByProtocol(protocol).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = { "find=ByLsTransactionEquals", "form" }, method = RequestMethod.GET)
    public String findProtocolStatesByLsTransactionEqualsForm(Model uiModel) {
        return "protocolstates/findProtocolStatesByLsTransactionEquals";
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET)
    public String findProtocolStatesByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocolstates", ProtocolState.findProtocolStatesByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ProtocolState.countFindProtocolStatesByLsTransactionEquals(lsTransaction) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocolstates", ProtocolState.findProtocolStatesByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocolstates/list";
    }

	@RequestMapping(params = { "find=ByProtocol", "form" }, method = RequestMethod.GET)
    public String findProtocolStatesByProtocolForm(Model uiModel) {
        uiModel.addAttribute("protocols", Protocol.findAllProtocols());
        return "protocolstates/findProtocolStatesByProtocol";
    }

	@RequestMapping(params = "find=ByProtocol", method = RequestMethod.GET)
    public String findProtocolStatesByProtocol(@RequestParam("protocol") Protocol protocol, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocolstates", ProtocolState.findProtocolStatesByProtocol(protocol, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ProtocolState.countFindProtocolStatesByProtocol(protocol) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocolstates", ProtocolState.findProtocolStatesByProtocol(protocol, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocolstates/list";
    }
}
