package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.utils.PropertiesUtilService;
import flexjson.JSONTokener;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
@RequestMapping("/treatmentgroupvalues")
public class TreatmentGroupValueController {

	@RequestMapping(params = { "find=ByCodeValueEquals", "form" }, method = RequestMethod.GET)
    public String findTreatmentGroupValuesByCodeValueEqualsForm(Model uiModel) {
        return "treatmentgroupvalues/findTreatmentGroupValuesByCodeValueEquals";
    }

	@RequestMapping(params = "find=ByCodeValueEquals", method = RequestMethod.GET)
    public String findTreatmentGroupValuesByCodeValueEquals(@RequestParam("codeValue") String codeValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("treatmentgroupvalues", TreatmentGroupValue.findTreatmentGroupValuesByCodeValueEquals(codeValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) TreatmentGroupValue.countFindTreatmentGroupValuesByCodeValueEquals(codeValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("treatmentgroupvalues", TreatmentGroupValue.findTreatmentGroupValuesByCodeValueEquals(codeValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "treatmentgroupvalues/list";
    }

	@RequestMapping(params = { "find=ByIgnoredNotAndCodeValueEquals", "form" }, method = RequestMethod.GET)
    public String findTreatmentGroupValuesByIgnoredNotAndCodeValueEqualsForm(Model uiModel) {
        return "treatmentgroupvalues/findTreatmentGroupValuesByIgnoredNotAndCodeValueEquals";
    }

	@RequestMapping(params = "find=ByIgnoredNotAndCodeValueEquals", method = RequestMethod.GET)
    public String findTreatmentGroupValuesByIgnoredNotAndCodeValueEquals(@RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam("codeValue") String codeValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("treatmentgroupvalues", TreatmentGroupValue.findTreatmentGroupValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) TreatmentGroupValue.countFindTreatmentGroupValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("treatmentgroupvalues", TreatmentGroupValue.findTreatmentGroupValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "treatmentgroupvalues/list";
    }

	@RequestMapping(params = { "find=ByLsState", "form" }, method = RequestMethod.GET)
    public String findTreatmentGroupValuesByLsStateForm(Model uiModel) {
        uiModel.addAttribute("treatmentgroupstates", TreatmentGroupState.findAllTreatmentGroupStates());
        return "treatmentgroupvalues/findTreatmentGroupValuesByLsState";
    }

	@RequestMapping(params = "find=ByLsState", method = RequestMethod.GET)
    public String findTreatmentGroupValuesByLsState(@RequestParam("lsState") TreatmentGroupState lsState, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("treatmentgroupvalues", TreatmentGroupValue.findTreatmentGroupValuesByLsState(lsState, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) TreatmentGroupValue.countFindTreatmentGroupValuesByLsState(lsState) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("treatmentgroupvalues", TreatmentGroupValue.findTreatmentGroupValuesByLsState(lsState, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "treatmentgroupvalues/list";
    }

	@RequestMapping(params = { "find=ByLsTransactionEquals", "form" }, method = RequestMethod.GET)
    public String findTreatmentGroupValuesByLsTransactionEqualsForm(Model uiModel) {
        return "treatmentgroupvalues/findTreatmentGroupValuesByLsTransactionEquals";
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET)
    public String findTreatmentGroupValuesByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("treatmentgroupvalues", TreatmentGroupValue.findTreatmentGroupValuesByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) TreatmentGroupValue.countFindTreatmentGroupValuesByLsTransactionEquals(lsTransaction) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("treatmentgroupvalues", TreatmentGroupValue.findTreatmentGroupValuesByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "treatmentgroupvalues/list";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            TreatmentGroupValue treatmentGroupValue = TreatmentGroupValue.findTreatmentGroupValue(id);
            if (treatmentGroupValue == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(treatmentGroupValue.toJson(), headers, HttpStatus.OK);
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
            List<TreatmentGroupValue> result = TreatmentGroupValue.findAllTreatmentGroupValues();
            return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            TreatmentGroupValue treatmentGroupValue = TreatmentGroupValue.fromJsonToTreatmentGroupValue(json);
            treatmentGroupValue.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+treatmentGroupValue.getId().toString()).build().toUriString());
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
            for (TreatmentGroupValue treatmentGroupValue: TreatmentGroupValue.fromJsonArrayToTreatmentGroupValues(json)) {
                treatmentGroupValue.persist();
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
            TreatmentGroupValue treatmentGroupValue = TreatmentGroupValue.fromJsonToTreatmentGroupValue(json);
            treatmentGroupValue.setId(id);
            if (treatmentGroupValue.merge() == null) {
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
            TreatmentGroupValue treatmentGroupValue = TreatmentGroupValue.findTreatmentGroupValue(id);
            if (treatmentGroupValue == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            treatmentGroupValue.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByCodeValueEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindTreatmentGroupValuesByCodeValueEquals(@RequestParam("codeValue") String codeValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(TreatmentGroupValue.findTreatmentGroupValuesByCodeValueEquals(codeValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByIgnoredNotAndCodeValueEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindTreatmentGroupValuesByIgnoredNotAndCodeValueEquals(@RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam("codeValue") String codeValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(TreatmentGroupValue.findTreatmentGroupValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsState", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindTreatmentGroupValuesByLsState(@RequestParam("lsState") TreatmentGroupState lsState) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(TreatmentGroupValue.findTreatmentGroupValuesByLsState(lsState).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindTreatmentGroupValuesByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(TreatmentGroupValue.toJsonArray(TreatmentGroupValue.findTreatmentGroupValuesByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid TreatmentGroupValue treatmentGroupValue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, treatmentGroupValue);
            return "treatmentgroupvalues/create";
        }
        uiModel.asMap().clear();
        treatmentGroupValue.persist();
        return "redirect:/treatmentgroupvalues/" + encodeUrlPathSegment(treatmentGroupValue.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new TreatmentGroupValue());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (TreatmentGroupState.countTreatmentGroupStates() == 0) {
            dependencies.add(new String[] { "lsState", "treatmentgroupstates" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "treatmentgroupvalues/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("treatmentgroupvalue", TreatmentGroupValue.findTreatmentGroupValue(id));
        uiModel.addAttribute("itemId", id);
        return "treatmentgroupvalues/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("treatmentgroupvalues", TreatmentGroupValue.findTreatmentGroupValueEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) TreatmentGroupValue.countTreatmentGroupValues() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("treatmentgroupvalues", TreatmentGroupValue.findAllTreatmentGroupValues(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "treatmentgroupvalues/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid TreatmentGroupValue treatmentGroupValue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, treatmentGroupValue);
            return "treatmentgroupvalues/update";
        }
        uiModel.asMap().clear();
        treatmentGroupValue.merge();
        return "redirect:/treatmentgroupvalues/" + encodeUrlPathSegment(treatmentGroupValue.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, TreatmentGroupValue.findTreatmentGroupValue(id));
        return "treatmentgroupvalues/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        TreatmentGroupValue treatmentGroupValue = TreatmentGroupValue.findTreatmentGroupValue(id);
        treatmentGroupValue.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/treatmentgroupvalues";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("treatmentGroupValue_datevalue_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("treatmentGroupValue_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("treatmentGroupValue_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, TreatmentGroupValue treatmentGroupValue) {
        uiModel.addAttribute("treatmentGroupValue", treatmentGroupValue);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("treatmentgroupstates", TreatmentGroupState.findAllTreatmentGroupStates());
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
