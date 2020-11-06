package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ItxProtocolProtocol;
import com.labsynch.labseer.domain.LsTag;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolLabel;
import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.ThingPage;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.exceptions.UniqueNameException;
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
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping("/protocols")
public class ProtocolController {

	@RequestMapping(params = { "find=ByCodeNameEquals", "form" }, method = RequestMethod.GET)
    public String findProtocolsByCodeNameEqualsForm(Model uiModel) {
        return "protocols/findProtocolsByCodeNameEquals";
    }

	@RequestMapping(params = "find=ByCodeNameEquals", method = RequestMethod.GET)
    public String findProtocolsByCodeNameEquals(@RequestParam("codeName") String codeName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocols", Protocol.findProtocolsByCodeNameEquals(codeName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Protocol.countFindProtocolsByCodeNameEquals(codeName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocols", Protocol.findProtocolsByCodeNameEquals(codeName, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocols/list";
    }

	@RequestMapping(params = { "find=ByCodeNameEqualsAndIgnoredNot", "form" }, method = RequestMethod.GET)
    public String findProtocolsByCodeNameEqualsAndIgnoredNotForm(Model uiModel) {
        return "protocols/findProtocolsByCodeNameEqualsAndIgnoredNot";
    }

	@RequestMapping(params = "find=ByCodeNameEqualsAndIgnoredNot", method = RequestMethod.GET)
    public String findProtocolsByCodeNameEqualsAndIgnoredNot(@RequestParam("codeName") String codeName, @RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocols", Protocol.findProtocolsByCodeNameEqualsAndIgnoredNot(codeName, ignored, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Protocol.countFindProtocolsByCodeNameEqualsAndIgnoredNot(codeName, ignored) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocols", Protocol.findProtocolsByCodeNameEqualsAndIgnoredNot(codeName, ignored, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocols/list";
    }

	@RequestMapping(params = { "find=ByCodeNameLike", "form" }, method = RequestMethod.GET)
    public String findProtocolsByCodeNameLikeForm(Model uiModel) {
        return "protocols/findProtocolsByCodeNameLike";
    }

	@RequestMapping(params = "find=ByCodeNameLike", method = RequestMethod.GET)
    public String findProtocolsByCodeNameLike(@RequestParam("codeName") String codeName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocols", Protocol.findProtocolsByCodeNameLike(codeName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Protocol.countFindProtocolsByCodeNameLike(codeName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocols", Protocol.findProtocolsByCodeNameLike(codeName, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocols/list";
    }

	@RequestMapping(params = { "find=ByIgnoredNot", "form" }, method = RequestMethod.GET)
    public String findProtocolsByIgnoredNotForm(Model uiModel) {
        return "protocols/findProtocolsByIgnoredNot";
    }

	@RequestMapping(params = "find=ByIgnoredNot", method = RequestMethod.GET)
    public String findProtocolsByIgnoredNot(@RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocols", Protocol.findProtocolsByIgnoredNot(ignored, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Protocol.countFindProtocolsByIgnoredNot(ignored) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocols", Protocol.findProtocolsByIgnoredNot(ignored, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocols/list";
    }

	@RequestMapping(params = { "find=ByLsKindEquals", "form" }, method = RequestMethod.GET)
    public String findProtocolsByLsKindEqualsForm(Model uiModel) {
        return "protocols/findProtocolsByLsKindEquals";
    }

	@RequestMapping(params = "find=ByLsKindEquals", method = RequestMethod.GET)
    public String findProtocolsByLsKindEquals(@RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocols", Protocol.findProtocolsByLsKindEquals(lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Protocol.countFindProtocolsByLsKindEquals(lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocols", Protocol.findProtocolsByLsKindEquals(lsKind, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocols/list";
    }

	@RequestMapping(params = { "find=ByLsKindLike", "form" }, method = RequestMethod.GET)
    public String findProtocolsByLsKindLikeForm(Model uiModel) {
        return "protocols/findProtocolsByLsKindLike";
    }

	@RequestMapping(params = "find=ByLsKindLike", method = RequestMethod.GET)
    public String findProtocolsByLsKindLike(@RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocols", Protocol.findProtocolsByLsKindLike(lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Protocol.countFindProtocolsByLsKindLike(lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocols", Protocol.findProtocolsByLsKindLike(lsKind, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocols/list";
    }

	@RequestMapping(params = { "find=ByLsTransactionEquals", "form" }, method = RequestMethod.GET)
    public String findProtocolsByLsTransactionEqualsForm(Model uiModel) {
        return "protocols/findProtocolsByLsTransactionEquals";
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET)
    public String findProtocolsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocols", Protocol.findProtocolsByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Protocol.countFindProtocolsByLsTransactionEquals(lsTransaction) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocols", Protocol.findProtocolsByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocols/list";
    }

	@RequestMapping(params = { "find=ByLsTypeAndKindEquals", "form" }, method = RequestMethod.GET)
    public String findProtocolsByLsTypeAndKindEqualsForm(Model uiModel) {
        return "protocols/findProtocolsByLsTypeAndKindEquals";
    }

	@RequestMapping(params = "find=ByLsTypeAndKindEquals", method = RequestMethod.GET)
    public String findProtocolsByLsTypeAndKindEquals(@RequestParam("lsTypeAndKind") String lsTypeAndKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocols", Protocol.findProtocolsByLsTypeAndKindEquals(lsTypeAndKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Protocol.countFindProtocolsByLsTypeAndKindEquals(lsTypeAndKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocols", Protocol.findProtocolsByLsTypeAndKindEquals(lsTypeAndKind, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocols/list";
    }

	@RequestMapping(params = { "find=ByLsTypeEquals", "form" }, method = RequestMethod.GET)
    public String findProtocolsByLsTypeEqualsForm(Model uiModel) {
        return "protocols/findProtocolsByLsTypeEquals";
    }

	@RequestMapping(params = "find=ByLsTypeEquals", method = RequestMethod.GET)
    public String findProtocolsByLsTypeEquals(@RequestParam("lsType") String lsType, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocols", Protocol.findProtocolsByLsTypeEquals(lsType, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Protocol.countFindProtocolsByLsTypeEquals(lsType) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocols", Protocol.findProtocolsByLsTypeEquals(lsType, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocols/list";
    }

	@RequestMapping(params = { "find=ByLsTypeEqualsAndLsKindEquals", "form" }, method = RequestMethod.GET)
    public String findProtocolsByLsTypeEqualsAndLsKindEqualsForm(Model uiModel) {
        return "protocols/findProtocolsByLsTypeEqualsAndLsKindEquals";
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET)
    public String findProtocolsByLsTypeEqualsAndLsKindEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocols", Protocol.findProtocolsByLsTypeEqualsAndLsKindEquals(lsType, lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Protocol.countFindProtocolsByLsTypeEqualsAndLsKindEquals(lsType, lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocols", Protocol.findProtocolsByLsTypeEqualsAndLsKindEquals(lsType, lsKind, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocols/list";
    }

	@RequestMapping(params = { "find=ByLsTypeLike", "form" }, method = RequestMethod.GET)
    public String findProtocolsByLsTypeLikeForm(Model uiModel) {
        return "protocols/findProtocolsByLsTypeLike";
    }

	@RequestMapping(params = "find=ByLsTypeLike", method = RequestMethod.GET)
    public String findProtocolsByLsTypeLike(@RequestParam("lsType") String lsType, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocols", Protocol.findProtocolsByLsTypeLike(lsType, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Protocol.countFindProtocolsByLsTypeLike(lsType) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocols", Protocol.findProtocolsByLsTypeLike(lsType, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocols/list";
    }

	@RequestMapping(params = { "find=ByRecordedByLike", "form" }, method = RequestMethod.GET)
    public String findProtocolsByRecordedByLikeForm(Model uiModel) {
        return "protocols/findProtocolsByRecordedByLike";
    }

	@RequestMapping(params = "find=ByRecordedByLike", method = RequestMethod.GET)
    public String findProtocolsByRecordedByLike(@RequestParam("recordedBy") String recordedBy, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocols", Protocol.findProtocolsByRecordedByLike(recordedBy, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Protocol.countFindProtocolsByRecordedByLike(recordedBy) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocols", Protocol.findProtocolsByRecordedByLike(recordedBy, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocols/list";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            Protocol protocol = Protocol.findProtocol(id);
            if (protocol == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(protocol.toJson(), headers, HttpStatus.OK);
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
            List<Protocol> result = Protocol.findAllProtocols();
            return new ResponseEntity<String>(Protocol.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            Protocol protocol = Protocol.fromJsonToProtocol(json);
            protocol.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+protocol.getId().toString()).build().toUriString());
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
            for (Protocol protocol: Protocol.fromJsonArrayToProtocols(json)) {
                protocol.persist();
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
            Protocol protocol = Protocol.fromJsonToProtocol(json);
            protocol.setId(id);
            if (protocol.merge() == null) {
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
            Protocol protocol = Protocol.findProtocol(id);
            if (protocol == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            protocol.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByCodeNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByCodeNameEquals(@RequestParam("codeName") String codeName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByCodeNameEquals(codeName).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByCodeNameEqualsAndIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByCodeNameEqualsAndIgnoredNot(@RequestParam("codeName") String codeName, @RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByCodeNameEqualsAndIgnoredNot(codeName, ignored).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByCodeNameLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByCodeNameLike(@RequestParam("codeName") String codeName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByCodeNameLike(codeName).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByIgnoredNot(@RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByIgnoredNot(ignored).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByLsKindEquals(@RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByLsKindEquals(lsKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByLsKindLike(@RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByLsKindLike(lsKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeAndKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByLsTypeAndKindEquals(@RequestParam("lsTypeAndKind") String lsTypeAndKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByLsTypeAndKindEquals(lsTypeAndKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByLsTypeEquals(@RequestParam("lsType") String lsType) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByLsTypeEquals(lsType).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByLsTypeEqualsAndLsKindEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByLsTypeLike(@RequestParam("lsType") String lsType) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByLsTypeLike(lsType).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByRecordedByLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByRecordedByLike(@RequestParam("recordedBy") String recordedBy) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByRecordedByLike(recordedBy).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Protocol());
        return "protocols/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("protocol", Protocol.findProtocol(id));
        uiModel.addAttribute("itemId", id);
        return "protocols/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocols", Protocol.findProtocolEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) Protocol.countProtocols() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocols", Protocol.findAllProtocols(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "protocols/list";
    }

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

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Protocol.findProtocol(id));
        return "protocols/update";
    }

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
        uiModel.addAttribute("experiments", Experiment.findAllExperiments());
        uiModel.addAttribute("itxprotocolprotocols", ItxProtocolProtocol.findAllItxProtocolProtocols());
        uiModel.addAttribute("lstags", LsTag.findAllLsTags());
        uiModel.addAttribute("protocollabels", ProtocolLabel.findAllProtocolLabels());
        uiModel.addAttribute("protocolstates", ProtocolState.findAllProtocolStates());
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
}
