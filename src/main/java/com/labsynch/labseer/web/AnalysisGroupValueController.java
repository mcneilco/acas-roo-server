package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;
import flexjson.JSONDeserializer;
import flexjson.JSONTokener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

@Controller
@RequestMapping("/analysisgroupvalues")
@Transactional
public class AnalysisGroupValueController {

    private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupValueController.class);

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid AnalysisGroupValue analysisGroupValue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, analysisGroupValue);
            return "analysisgroupvalues/create";
        }
        uiModel.asMap().clear();
        analysisGroupValue.persist();
        return "redirect:/analysisgroupvalues/" + encodeUrlPathSegment(analysisGroupValue.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new AnalysisGroupValue());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (AnalysisGroupState.countAnalysisGroupStates() == 0) {
            dependencies.add(new String[] { "analysisgroupstate", "analysisgroupstates" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "analysisgroupvalues/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("analysisgroupvalue", AnalysisGroupValue.findAnalysisGroupValue(id));
        uiModel.addAttribute("itemId", id);
        return "analysisgroupvalues/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgroupvalues", AnalysisGroupValue.findAnalysisGroupValueEntries(firstResult, sizeNo));
            float nrOfPages = (float) AnalysisGroupValue.countAnalysisGroupValues() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgroupvalues", AnalysisGroupValue.findAllAnalysisGroupValues());
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgroupvalues/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid AnalysisGroupValue analysisGroupValue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, analysisGroupValue);
            return "analysisgroupvalues/update";
        }
        uiModel.asMap().clear();
        analysisGroupValue.merge();
        return "redirect:/analysisgroupvalues/" + encodeUrlPathSegment(analysisGroupValue.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, AnalysisGroupValue.findAnalysisGroupValue(id));
        return "analysisgroupvalues/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.findAnalysisGroupValue(id);
        analysisGroupValue.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/analysisgroupvalues";
    }

    void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("analysisGroupValue_datevalue_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("analysisGroupValue_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("analysisGroupValue_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

    void populateEditForm(Model uiModel, AnalysisGroupValue analysisGroupValue) {
        uiModel.addAttribute("analysisGroupValue", analysisGroupValue);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("analysisgroupstates", AnalysisGroupState.findAllAnalysisGroupStates());
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

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.findAnalysisGroupValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (analysisGroupValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(analysisGroupValue.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<AnalysisGroupValue> result = AnalysisGroupValue.findAllAnalysisGroupValues();
        return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.fromJsonToAnalysisGroupValue(json);
        analysisGroupValue.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        for (AnalysisGroupValue analysisGroupValue : AnalysisGroupValue.fromJsonArrayToAnalysisGroupValues(json)) {
            analysisGroupValue.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.fromJsonToAnalysisGroupValue(json);
        if (analysisGroupValue.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (AnalysisGroupValue analysisGroupValue : AnalysisGroupValue.fromJsonArrayToAnalysisGroupValues(json)) {
            if (analysisGroupValue.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.findAnalysisGroupValue(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (analysisGroupValue == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        analysisGroupValue.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByCodeValueEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindAnalysisGroupValuesByCodeValueEquals(@RequestParam("codeValue") String codeValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(AnalysisGroupValue.findAnalysisGroupValuesByCodeValueEquals(codeValue).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByIgnoredNotAndCodeValueEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindAnalysisGroupValuesByIgnoredNotAndCodeValueEquals(@RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam("codeValue") String codeValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(AnalysisGroupValue.findAnalysisGroupValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByLsState", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindAnalysisGroupValuesByLsState(@RequestParam("lsState") AnalysisGroupState lsState) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(AnalysisGroupValue.findAnalysisGroupValuesByLsState(lsState).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindAnalysisGroupValuesByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(AnalysisGroupValue.findAnalysisGroupValuesByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindAnalysisGroupValuesByLsTypeEqualsAndLsKindEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(AnalysisGroupValue.findAnalysisGroupValuesByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindAnalysisGroupValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam("stringValue") String stringValue, @RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(AnalysisGroupValue.toJsonArray(AnalysisGroupValue.findAnalysisGroupValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot(lsType, lsKind, stringValue, ignored).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = { "find=ByCodeValueEquals", "form" }, method = RequestMethod.GET)
    public String findAnalysisGroupValuesByCodeValueEqualsForm(Model uiModel) {
        return "analysisgroupvalues/findAnalysisGroupValuesByCodeValueEquals";
    }

	@RequestMapping(params = "find=ByCodeValueEquals", method = RequestMethod.GET)
    public String findAnalysisGroupValuesByCodeValueEquals(@RequestParam("codeValue") String codeValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgroupvalues", AnalysisGroupValue.findAnalysisGroupValuesByCodeValueEquals(codeValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) AnalysisGroupValue.countFindAnalysisGroupValuesByCodeValueEquals(codeValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgroupvalues", AnalysisGroupValue.findAnalysisGroupValuesByCodeValueEquals(codeValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgroupvalues/list";
    }

	@RequestMapping(params = { "find=ByIgnoredNotAndCodeValueEquals", "form" }, method = RequestMethod.GET)
    public String findAnalysisGroupValuesByIgnoredNotAndCodeValueEqualsForm(Model uiModel) {
        return "analysisgroupvalues/findAnalysisGroupValuesByIgnoredNotAndCodeValueEquals";
    }

	@RequestMapping(params = "find=ByIgnoredNotAndCodeValueEquals", method = RequestMethod.GET)
    public String findAnalysisGroupValuesByIgnoredNotAndCodeValueEquals(@RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam("codeValue") String codeValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgroupvalues", AnalysisGroupValue.findAnalysisGroupValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) AnalysisGroupValue.countFindAnalysisGroupValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgroupvalues", AnalysisGroupValue.findAnalysisGroupValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgroupvalues/list";
    }

	@RequestMapping(params = { "find=ByLsState", "form" }, method = RequestMethod.GET)
    public String findAnalysisGroupValuesByLsStateForm(Model uiModel) {
        uiModel.addAttribute("analysisgroupstates", AnalysisGroupState.findAllAnalysisGroupStates());
        return "analysisgroupvalues/findAnalysisGroupValuesByLsState";
    }

	@RequestMapping(params = "find=ByLsState", method = RequestMethod.GET)
    public String findAnalysisGroupValuesByLsState(@RequestParam("lsState") AnalysisGroupState lsState, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgroupvalues", AnalysisGroupValue.findAnalysisGroupValuesByLsState(lsState, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) AnalysisGroupValue.countFindAnalysisGroupValuesByLsState(lsState) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgroupvalues", AnalysisGroupValue.findAnalysisGroupValuesByLsState(lsState, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgroupvalues/list";
    }

	@RequestMapping(params = { "find=ByLsTransactionEquals", "form" }, method = RequestMethod.GET)
    public String findAnalysisGroupValuesByLsTransactionEqualsForm(Model uiModel) {
        return "analysisgroupvalues/findAnalysisGroupValuesByLsTransactionEquals";
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET)
    public String findAnalysisGroupValuesByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgroupvalues", AnalysisGroupValue.findAnalysisGroupValuesByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) AnalysisGroupValue.countFindAnalysisGroupValuesByLsTransactionEquals(lsTransaction) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgroupvalues", AnalysisGroupValue.findAnalysisGroupValuesByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgroupvalues/list";
    }

	@RequestMapping(params = { "find=ByLsTypeEqualsAndLsKindEquals", "form" }, method = RequestMethod.GET)
    public String findAnalysisGroupValuesByLsTypeEqualsAndLsKindEqualsForm(Model uiModel) {
        return "analysisgroupvalues/findAnalysisGroupValuesByLsTypeEqualsAndLsKindEquals";
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET)
    public String findAnalysisGroupValuesByLsTypeEqualsAndLsKindEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgroupvalues", AnalysisGroupValue.findAnalysisGroupValuesByLsTypeEqualsAndLsKindEquals(lsType, lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) AnalysisGroupValue.countFindAnalysisGroupValuesByLsTypeEqualsAndLsKindEquals(lsType, lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgroupvalues", AnalysisGroupValue.findAnalysisGroupValuesByLsTypeEqualsAndLsKindEquals(lsType, lsKind, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgroupvalues/list";
    }

	@RequestMapping(params = { "find=ByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot", "form" }, method = RequestMethod.GET)
    public String findAnalysisGroupValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNotForm(Model uiModel) {
        return "analysisgroupvalues/findAnalysisGroupValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot";
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot", method = RequestMethod.GET)
    public String findAnalysisGroupValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam("stringValue") String stringValue, @RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("analysisgroupvalues", AnalysisGroupValue.findAnalysisGroupValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot(lsType, lsKind, stringValue, ignored, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) AnalysisGroupValue.countFindAnalysisGroupValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot(lsType, lsKind, stringValue, ignored) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("analysisgroupvalues", AnalysisGroupValue.findAnalysisGroupValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot(lsType, lsKind, stringValue, ignored, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "analysisgroupvalues/list";
    }
}
