package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolLabel;
import com.labsynch.labseer.dto.CodeTableDTO;
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

@RooWebJson(jsonObject = ProtocolLabel.class)
@Controller
@RequestMapping("/protocollabels")
@RooWebScaffold(path = "protocollabels", formBackingObject = ProtocolLabel.class)
@RooWebFinder
public class ProtocolLabelController {

	@RequestMapping(params = { "find=ByLabelTextEqualsAndIgnoredNot", "form" }, method = RequestMethod.GET)
    public String findProtocolLabelsByLabelTextEqualsAndIgnoredNotForm(Model uiModel) {
        return "protocollabels/findProtocolLabelsByLabelTextEqualsAndIgnoredNot";
    }

	@RequestMapping(params = "find=ByLabelTextEqualsAndIgnoredNot", method = RequestMethod.GET)
    public String findProtocolLabelsByLabelTextEqualsAndIgnoredNot(@RequestParam("labelText") String labelText, @RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocollabels", ProtocolLabel.findProtocolLabelsByLabelTextEqualsAndIgnoredNot(labelText, ignored, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ProtocolLabel.countFindProtocolLabelsByLabelTextEqualsAndIgnoredNot(labelText, ignored) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocollabels", ProtocolLabel.findProtocolLabelsByLabelTextEqualsAndIgnoredNot(labelText, ignored, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocollabels/list";
    }

	@RequestMapping(params = { "find=ByLabelTextLike", "form" }, method = RequestMethod.GET)
    public String findProtocolLabelsByLabelTextLikeForm(Model uiModel) {
        return "protocollabels/findProtocolLabelsByLabelTextLike";
    }

	@RequestMapping(params = "find=ByLabelTextLike", method = RequestMethod.GET)
    public String findProtocolLabelsByLabelTextLike(@RequestParam("labelText") String labelText, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocollabels", ProtocolLabel.findProtocolLabelsByLabelTextLike(labelText, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ProtocolLabel.countFindProtocolLabelsByLabelTextLike(labelText) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocollabels", ProtocolLabel.findProtocolLabelsByLabelTextLike(labelText, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocollabels/list";
    }

	@RequestMapping(params = { "find=ByLsTransactionEquals", "form" }, method = RequestMethod.GET)
    public String findProtocolLabelsByLsTransactionEqualsForm(Model uiModel) {
        return "protocollabels/findProtocolLabelsByLsTransactionEquals";
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET)
    public String findProtocolLabelsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocollabels", ProtocolLabel.findProtocolLabelsByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ProtocolLabel.countFindProtocolLabelsByLsTransactionEquals(lsTransaction) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocollabels", ProtocolLabel.findProtocolLabelsByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocollabels/list";
    }

	@RequestMapping(params = { "find=ByProtocol", "form" }, method = RequestMethod.GET)
    public String findProtocolLabelsByProtocolForm(Model uiModel) {
        uiModel.addAttribute("protocols", Protocol.findAllProtocols());
        return "protocollabels/findProtocolLabelsByProtocol";
    }

	@RequestMapping(params = "find=ByProtocol", method = RequestMethod.GET)
    public String findProtocolLabelsByProtocol(@RequestParam("protocol") Protocol protocol, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocollabels", ProtocolLabel.findProtocolLabelsByProtocol(protocol, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ProtocolLabel.countFindProtocolLabelsByProtocol(protocol) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocollabels", ProtocolLabel.findProtocolLabelsByProtocol(protocol, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocollabels/list";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            ProtocolLabel protocolLabel = ProtocolLabel.findProtocolLabel(id);
            if (protocolLabel == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(protocolLabel.toJson(), headers, HttpStatus.OK);
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
            List<ProtocolLabel> result = ProtocolLabel.findAllProtocolLabels();
            return new ResponseEntity<String>(ProtocolLabel.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ProtocolLabel protocolLabel = ProtocolLabel.fromJsonToProtocolLabel(json);
            protocolLabel.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+protocolLabel.getId().toString()).build().toUriString());
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
            for (ProtocolLabel protocolLabel: ProtocolLabel.fromJsonArrayToProtocolLabels(json)) {
                protocolLabel.persist();
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
            ProtocolLabel protocolLabel = ProtocolLabel.fromJsonToProtocolLabel(json);
            protocolLabel.setId(id);
            if (protocolLabel.merge() == null) {
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
            ProtocolLabel protocolLabel = ProtocolLabel.findProtocolLabel(id);
            if (protocolLabel == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            protocolLabel.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLabelTextEqualsAndIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolLabelsByLabelTextEqualsAndIgnoredNot(@RequestParam("labelText") String labelText, @RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ProtocolLabel.toJsonArray(ProtocolLabel.findProtocolLabelsByLabelTextEqualsAndIgnoredNot(labelText, ignored).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLabelTextLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolLabelsByLabelTextLike(@RequestParam("labelText") String labelText) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ProtocolLabel.toJsonArray(ProtocolLabel.findProtocolLabelsByLabelTextLike(labelText).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolLabelsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ProtocolLabel.toJsonArray(ProtocolLabel.findProtocolLabelsByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByProtocol", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolLabelsByProtocol(@RequestParam("protocol") Protocol protocol) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ProtocolLabel.toJsonArray(ProtocolLabel.findProtocolLabelsByProtocol(protocol).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ProtocolLabel protocolLabel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, protocolLabel);
            return "protocollabels/create";
        }
        uiModel.asMap().clear();
        protocolLabel.persist();
        return "redirect:/protocollabels/" + encodeUrlPathSegment(protocolLabel.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ProtocolLabel());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Protocol.countProtocols() == 0) {
            dependencies.add(new String[] { "protocol", "protocols" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "protocollabels/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("protocollabel", ProtocolLabel.findProtocolLabel(id));
        uiModel.addAttribute("itemId", id);
        return "protocollabels/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocollabels", ProtocolLabel.findProtocolLabelEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ProtocolLabel.countProtocolLabels() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocollabels", ProtocolLabel.findAllProtocolLabels(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocollabels/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ProtocolLabel protocolLabel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, protocolLabel);
            return "protocollabels/update";
        }
        uiModel.asMap().clear();
        protocolLabel.merge();
        return "redirect:/protocollabels/" + encodeUrlPathSegment(protocolLabel.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ProtocolLabel.findProtocolLabel(id));
        return "protocollabels/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ProtocolLabel protocolLabel = ProtocolLabel.findProtocolLabel(id);
        protocolLabel.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/protocollabels";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("protocolLabel_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("protocolLabel_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, ProtocolLabel protocolLabel) {
        uiModel.addAttribute("protocolLabel", protocolLabel);
        addDateTimeFormatPatterns(uiModel);
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
}
