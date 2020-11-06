package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.LsTag;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.ThingPage;
import com.labsynch.labseer.dto.ErrorMessageDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.PreferredNameWithErrorDTO;
import com.labsynch.labseer.service.GeneThingService;
import com.labsynch.labseer.service.LsThingService;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.annotation.DateTimeFormat;
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

@RooWebJson(jsonObject = LsThing.class)
@Controller
@RequestMapping("/lsthings")
@RooWebScaffold(path = "lsthings", formBackingObject = LsThing.class)
@RooWebFinder
public class LsThingController {

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            LsThing lsThing = LsThing.findLsThing(id);
            if (lsThing == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(lsThing.toJson(), headers, HttpStatus.OK);
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
            List<LsThing> result = LsThing.findAllLsThings();
            return new ResponseEntity<String>(LsThing.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            LsThing lsThing = LsThing.fromJsonToLsThing(json);
            lsThing.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+lsThing.getId().toString()).build().toUriString());
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
            for (LsThing lsThing: LsThing.fromJsonArrayToLsThings(json)) {
                lsThing.persist();
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
            LsThing lsThing = LsThing.fromJsonToLsThing(json);
            lsThing.setId(id);
            if (lsThing.merge() == null) {
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
            LsThing lsThing = LsThing.findLsThing(id);
            if (lsThing == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            lsThing.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByCodeNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingsByCodeNameEquals(@RequestParam("codeName") String codeName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThing.toJsonArray(LsThing.findLsThingsByCodeNameEquals(codeName).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByCodeNameLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingsByCodeNameLike(@RequestParam("codeName") String codeName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThing.toJsonArray(LsThing.findLsThingsByCodeNameLike(codeName).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingsByLsKindEquals(@RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThing.toJsonArray(LsThing.findLsThingsByLsKindEquals(lsKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingsByLsKindLike(@RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThing.toJsonArray(LsThing.findLsThingsByLsKindLike(lsKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThing.toJsonArray(LsThing.findLsThingsByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeAndKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingsByLsTypeAndKindEquals(@RequestParam("lsTypeAndKind") String lsTypeAndKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThing.toJsonArray(LsThing.findLsThingsByLsTypeAndKindEquals(lsTypeAndKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingsByLsTypeEquals(@RequestParam("lsType") String lsType) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThing.toJsonArray(LsThing.findLsThingsByLsTypeEquals(lsType).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingsByLsTypeEqualsAndLsKindEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThing.toJsonArray(LsThing.findLsThingsByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByRecordedByLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingsByRecordedByLike(@RequestParam("recordedBy") String recordedBy) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThing.toJsonArray(LsThing.findLsThingsByRecordedByLike(recordedBy).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByRecordedDateGreaterThan", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingsByRecordedDateGreaterThan(@RequestParam("recordedDate") @DateTimeFormat(style = "MM") Date recordedDate) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThing.toJsonArray(LsThing.findLsThingsByRecordedDateGreaterThan(recordedDate).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByRecordedDateLessThan", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingsByRecordedDateLessThan(@RequestParam("recordedDate") @DateTimeFormat(style = "MM") Date recordedDate) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThing.toJsonArray(LsThing.findLsThingsByRecordedDateLessThan(recordedDate).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = { "find=ByCodeNameEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingsByCodeNameEqualsForm(Model uiModel) {
        return "lsthings/findLsThingsByCodeNameEquals";
    }

	@RequestMapping(params = "find=ByCodeNameEquals", method = RequestMethod.GET)
    public String findLsThingsByCodeNameEquals(@RequestParam("codeName") String codeName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByCodeNameEquals(codeName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThing.countFindLsThingsByCodeNameEquals(codeName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByCodeNameEquals(codeName, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthings/list";
    }

	@RequestMapping(params = { "find=ByCodeNameLike", "form" }, method = RequestMethod.GET)
    public String findLsThingsByCodeNameLikeForm(Model uiModel) {
        return "lsthings/findLsThingsByCodeNameLike";
    }

	@RequestMapping(params = "find=ByCodeNameLike", method = RequestMethod.GET)
    public String findLsThingsByCodeNameLike(@RequestParam("codeName") String codeName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByCodeNameLike(codeName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThing.countFindLsThingsByCodeNameLike(codeName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByCodeNameLike(codeName, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthings/list";
    }

	@RequestMapping(params = { "find=ByLsKindEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingsByLsKindEqualsForm(Model uiModel) {
        return "lsthings/findLsThingsByLsKindEquals";
    }

	@RequestMapping(params = "find=ByLsKindEquals", method = RequestMethod.GET)
    public String findLsThingsByLsKindEquals(@RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByLsKindEquals(lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThing.countFindLsThingsByLsKindEquals(lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByLsKindEquals(lsKind, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthings/list";
    }

	@RequestMapping(params = { "find=ByLsKindLike", "form" }, method = RequestMethod.GET)
    public String findLsThingsByLsKindLikeForm(Model uiModel) {
        return "lsthings/findLsThingsByLsKindLike";
    }

	@RequestMapping(params = "find=ByLsKindLike", method = RequestMethod.GET)
    public String findLsThingsByLsKindLike(@RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByLsKindLike(lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThing.countFindLsThingsByLsKindLike(lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByLsKindLike(lsKind, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthings/list";
    }

	@RequestMapping(params = { "find=ByLsTransactionEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingsByLsTransactionEqualsForm(Model uiModel) {
        return "lsthings/findLsThingsByLsTransactionEquals";
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET)
    public String findLsThingsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThing.countFindLsThingsByLsTransactionEquals(lsTransaction) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthings/list";
    }

	@RequestMapping(params = { "find=ByLsTypeAndKindEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingsByLsTypeAndKindEqualsForm(Model uiModel) {
        return "lsthings/findLsThingsByLsTypeAndKindEquals";
    }

	@RequestMapping(params = "find=ByLsTypeAndKindEquals", method = RequestMethod.GET)
    public String findLsThingsByLsTypeAndKindEquals(@RequestParam("lsTypeAndKind") String lsTypeAndKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByLsTypeAndKindEquals(lsTypeAndKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThing.countFindLsThingsByLsTypeAndKindEquals(lsTypeAndKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByLsTypeAndKindEquals(lsTypeAndKind, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthings/list";
    }

	@RequestMapping(params = { "find=ByLsTypeEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingsByLsTypeEqualsForm(Model uiModel) {
        return "lsthings/findLsThingsByLsTypeEquals";
    }

	@RequestMapping(params = "find=ByLsTypeEquals", method = RequestMethod.GET)
    public String findLsThingsByLsTypeEquals(@RequestParam("lsType") String lsType, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByLsTypeEquals(lsType, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThing.countFindLsThingsByLsTypeEquals(lsType) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByLsTypeEquals(lsType, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthings/list";
    }

	@RequestMapping(params = { "find=ByLsTypeEqualsAndLsKindEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingsByLsTypeEqualsAndLsKindEqualsForm(Model uiModel) {
        return "lsthings/findLsThingsByLsTypeEqualsAndLsKindEquals";
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET)
    public String findLsThingsByLsTypeEqualsAndLsKindEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByLsTypeEqualsAndLsKindEquals(lsType, lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThing.countFindLsThingsByLsTypeEqualsAndLsKindEquals(lsType, lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByLsTypeEqualsAndLsKindEquals(lsType, lsKind, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthings/list";
    }

	@RequestMapping(params = { "find=ByRecordedByLike", "form" }, method = RequestMethod.GET)
    public String findLsThingsByRecordedByLikeForm(Model uiModel) {
        return "lsthings/findLsThingsByRecordedByLike";
    }

	@RequestMapping(params = "find=ByRecordedByLike", method = RequestMethod.GET)
    public String findLsThingsByRecordedByLike(@RequestParam("recordedBy") String recordedBy, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByRecordedByLike(recordedBy, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThing.countFindLsThingsByRecordedByLike(recordedBy) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByRecordedByLike(recordedBy, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthings/list";
    }

	@RequestMapping(params = { "find=ByRecordedDateGreaterThan", "form" }, method = RequestMethod.GET)
    public String findLsThingsByRecordedDateGreaterThanForm(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        return "lsthings/findLsThingsByRecordedDateGreaterThan";
    }

	@RequestMapping(params = "find=ByRecordedDateGreaterThan", method = RequestMethod.GET)
    public String findLsThingsByRecordedDateGreaterThan(@RequestParam("recordedDate") @DateTimeFormat(style = "MM") Date recordedDate, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByRecordedDateGreaterThan(recordedDate, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThing.countFindLsThingsByRecordedDateGreaterThan(recordedDate) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByRecordedDateGreaterThan(recordedDate, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthings/list";
    }

	@RequestMapping(params = { "find=ByRecordedDateLessThan", "form" }, method = RequestMethod.GET)
    public String findLsThingsByRecordedDateLessThanForm(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        return "lsthings/findLsThingsByRecordedDateLessThan";
    }

	@RequestMapping(params = "find=ByRecordedDateLessThan", method = RequestMethod.GET)
    public String findLsThingsByRecordedDateLessThan(@RequestParam("recordedDate") @DateTimeFormat(style = "MM") Date recordedDate, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByRecordedDateLessThan(recordedDate, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThing.countFindLsThingsByRecordedDateLessThan(recordedDate) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthings", LsThing.findLsThingsByRecordedDateLessThan(recordedDate, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthings/list";
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LsThing lsThing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsThing);
            return "lsthings/create";
        }
        uiModel.asMap().clear();
        lsThing.persist();
        return "redirect:/lsthings/" + encodeUrlPathSegment(lsThing.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LsThing());
        return "lsthings/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("lsthing", LsThing.findLsThing(id));
        uiModel.addAttribute("itemId", id);
        return "lsthings/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthings", LsThing.findLsThingEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LsThing.countLsThings() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthings", LsThing.findAllLsThings(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthings/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LsThing lsThing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsThing);
            return "lsthings/update";
        }
        uiModel.asMap().clear();
        lsThing.merge();
        return "redirect:/lsthings/" + encodeUrlPathSegment(lsThing.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LsThing.findLsThing(id));
        return "lsthings/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LsThing lsThing = LsThing.findLsThing(id);
        lsThing.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lsthings";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("lsThing_recordeddate_date_format", org.joda.time.format.DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("lsThing_modifieddate_date_format", org.joda.time.format.DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, LsThing lsThing) {
        uiModel.addAttribute("lsThing", lsThing);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itxlsthinglsthings", ItxLsThingLsThing.findAllItxLsThingLsThings());
        uiModel.addAttribute("lstags", LsTag.findAllLsTags());
        uiModel.addAttribute("lsthinglabels", LsThingLabel.findAllLsThingLabels());
        uiModel.addAttribute("lsthingstates", LsThingState.findAllLsThingStates());
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
