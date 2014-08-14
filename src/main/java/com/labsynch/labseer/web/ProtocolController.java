package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.service.ProtocolService;
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
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RooWebJson(jsonObject = Protocol.class)
@Controller
@RequestMapping("/protocols")
@RooWebScaffold(path = "protocols", formBackingObject = Protocol.class)
@RooWebFinder
public class ProtocolController {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(ProtocolController.class);

    @Autowired
    private ProtocolService protocolService;

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        Protocol protocol = Protocol.findProtocol(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (protocol == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(protocol.toJson(), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{lstype}/{lskind}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJsonByPath(@PathVariable("lstype") String lsType, @PathVariable("lskind") String lsKind, @RequestParam(value = "with", required = false) String with, @RequestParam(value = "prettyjson", required = false) String prettyjson) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        boolean prettyJson = false;
        if (prettyjson != null) {
            if (prettyjson.equalsIgnoreCase("true")) {
                prettyJson = true;
            }
        }
        boolean includeExperiments = false;
        if (with != null) {
            if (with.equalsIgnoreCase("experiments") || with.equalsIgnoreCase("fullobject")) {
                includeExperiments = true;
            }
        }
        List<Protocol> protocols = null;
        if (lsKind != null) {
            if (lsType != null) {
                protocols = Protocol.findProtocolsByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList();
            } else {
                protocols = Protocol.findProtocolsByLsKindEquals(lsKind).getResultList();
            }
        } else {
            protocols = Protocol.findAllProtocols();
        }
        return new ResponseEntity<String>(Protocol.toJsonArray(protocols, prettyJson, includeExperiments), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson(@RequestParam(value = "with", required = false) String with, @RequestParam(value = "prettyjson", required = false) String prettyjson, @RequestParam(value = "lstype", required = false) String lsType, @RequestParam(value = "lskind", required = false) String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        logger.debug("value for prettyjson: " + prettyjson);
        boolean prettyJson = false;
        if (prettyjson != null) {
            if (prettyjson.equalsIgnoreCase("true")) {
                prettyJson = true;
            }
        }
        boolean includeExperiments = false;
        if (with != null) {
            if (with.equalsIgnoreCase("experiments") || with.equalsIgnoreCase("fullobject")) {
                includeExperiments = true;
            }
        }
        List<Protocol> protocols = null;
        if (lsKind != null) {
            logger.debug("incoming lsKind is: " + lsKind);
            if (lsType != null) {
                protocols = Protocol.findProtocolsByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList();
            } else {
                protocols = Protocol.findProtocolsByLsKindEquals(lsKind).getResultList();
            }
        } else {
            protocols = Protocol.findAllProtocols();
        }
        return new ResponseEntity<String>(Protocol.toJsonArray(protocols, prettyJson, includeExperiments), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/codetable", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJsonCodeTable(@RequestParam(value = "with", required = false) String with, @RequestParam(value = "prettyjson", required = false) String prettyjson, @RequestParam(value = "lstype", required = false) String lsType, @RequestParam(value = "lskind", required = false) String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<CodeTableDTO> result;
        if (lsKind != null) {
            result = Protocol.getProtocolCodeTableByKindEquals(lsKind);
        } else {
            result = Protocol.getProtocolCodeTable();
        }
        return new ResponseEntity<String>(CodeTableDTO.toJsonArray(result), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        Protocol protocol = protocolService.saveLsProtocol(Protocol.fromJsonToProtocol(json));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(protocol.toJson(), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        Collection<Protocol> savedProtocols = new ArrayList<Protocol>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        StringReader sr = new StringReader(json);
        BufferedReader br = new BufferedReader(sr);
        for (Protocol protocol : Protocol.fromJsonArrayToProtocols(br)) {
            protocol = protocolService.saveLsProtocol(protocol);
            savedProtocols.add(protocol);
            if (i % batchSize == 0) {
                protocol.flush();
                protocol.clear();
            }
            i++;
        }
        IOUtils.closeQuietly(sr);
        IOUtils.closeQuietly(br);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(Protocol.toJsonArray(savedProtocols), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Protocol protocol = protocolService.updateProtocol(Protocol.fromJsonToProtocol(json));
        if (protocol.getId() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(protocol.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<Protocol> updatedProtocols = new ArrayList<Protocol>();
        for (Protocol protocol : Protocol.fromJsonArrayToProtocols(json)) {
            updatedProtocols.add(protocolService.updateProtocol(protocol));
        }
        return new ResponseEntity<String>(Protocol.toJsonArray(updatedProtocols), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        Protocol protocol = Protocol.findProtocol(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (protocol == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        protocol.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/lsprotocols", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createLsProtocolFromJson(@RequestBody String json) {
        Protocol protocol = protocolService.saveLsProtocol(Protocol.fromJsonToProtocol(json));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(protocol.toJson(), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = "/codename/{codeName}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindProtocolsByCodeNameEqualsRoute(@PathVariable("codeName") String codeName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByCodeNameEquals(codeName).getResultList()), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(params = "FindByCodeName", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindProtocolsByCodeNameEquals(@RequestParam("codeName") String codeName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByCodeNameEquals(codeName).getResultList()), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/protocolname/**", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindProtocolsByProtocolNameEqualsRoute(HttpServletRequest request) {
        String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String protocolName = restOfTheUrl.split("protocolname\\/")[1].replaceAll("/$", "");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolByProtocolName(protocolName)), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(params = "FindByProtocolName", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindProtocolsByProtocolNameEqualsGet(@RequestParam("protocolName") String protocolName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolByProtocolName(protocolName)), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(params = "FindByName", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindProtocolByNameGet(@RequestParam("name") String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolByName(name)), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Protocol protocol, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, protocol);
            return "protocols/create";
        }
        uiModel.asMap().clear();
        protocol.persist();
        return "redirect:/protocols/" + encodeUrlPathSegment(protocol.getId().toString(), httpServletRequest);
    }

    @Transactional
    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Protocol());
        return "protocols/create";
    }

    @Transactional
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("protocol", Protocol.findProtocol(id));
        uiModel.addAttribute("itemId", id);
        return "protocols/show";
    }

    @Transactional
    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocols", Protocol.findProtocolEntries(firstResult, sizeNo));
            float nrOfPages = (float) Protocol.countProtocols() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocols", Protocol.findAllProtocols());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocols/list";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Protocol protocol, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, protocol);
            return "protocols/update";
        }
        uiModel.asMap().clear();
        protocol.merge();
        return "redirect:/protocols/" + encodeUrlPathSegment(protocol.getId().toString(), httpServletRequest);
    }

    @Transactional
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Protocol.findProtocol(id));
        return "protocols/update";
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Protocol protocol = Protocol.findProtocol(id);
        protocol.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/protocols";
    }

    void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("protocol_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("protocol_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

    void populateEditForm(Model uiModel, Protocol protocol) {
        uiModel.addAttribute("protocol", protocol);
        addDateTimeFormatPatterns(uiModel);
    }

    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }
}
