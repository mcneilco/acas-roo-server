package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

@RooWebJson(jsonObject = LsThingValue.class)
@Controller
@RequestMapping("/lsthingvalues")
@RooWebScaffold(path = "lsthingvalues", formBackingObject = LsThingValue.class)
@RooWebFinder
public class LsThingValueController {

	@RequestMapping(params = { "find=ByCodeValueEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingValuesByCodeValueEqualsForm(Model uiModel) {
        return "lsthingvalues/findLsThingValuesByCodeValueEquals";
    }

	@RequestMapping(params = "find=ByCodeValueEquals", method = RequestMethod.GET)
    public String findLsThingValuesByCodeValueEquals(@RequestParam("codeValue") String codeValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByCodeValueEquals(codeValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingValue.countFindLsThingValuesByCodeValueEquals(codeValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByCodeValueEquals(codeValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/list";
    }

	@RequestMapping(params = { "find=ByIgnoredNotAndCodeValueEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingValuesByIgnoredNotAndCodeValueEqualsForm(Model uiModel) {
        return "lsthingvalues/findLsThingValuesByIgnoredNotAndCodeValueEquals";
    }

	@RequestMapping(params = "find=ByIgnoredNotAndCodeValueEquals", method = RequestMethod.GET)
    public String findLsThingValuesByIgnoredNotAndCodeValueEquals(@RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam("codeValue") String codeValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingValue.countFindLsThingValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/list";
    }

	@RequestMapping(params = { "find=ByLsKindEqualsAndCodeValueLike", "form" }, method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndCodeValueLikeForm(Model uiModel) {
        return "lsthingvalues/findLsThingValuesByLsKindEqualsAndCodeValueLike";
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndCodeValueLike", method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndCodeValueLike(@RequestParam("lsKind") String lsKind, @RequestParam("codeValue") String codeValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndCodeValueLike(lsKind, codeValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingValue.countFindLsThingValuesByLsKindEqualsAndCodeValueLike(lsKind, codeValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndCodeValueLike(lsKind, codeValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/list";
    }

	@RequestMapping(params = { "find=ByLsKindEqualsAndDateValueGreaterThanEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndDateValueGreaterThanEqualsForm(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/findLsThingValuesByLsKindEqualsAndDateValueGreaterThanEquals";
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndDateValueGreaterThanEquals", method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndDateValueGreaterThanEquals(@RequestParam("lsKind") String lsKind, @RequestParam("dateValue") @DateTimeFormat(style = "MM") Date dateValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndDateValueGreaterThanEquals(lsKind, dateValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingValue.countFindLsThingValuesByLsKindEqualsAndDateValueGreaterThanEquals(lsKind, dateValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndDateValueGreaterThanEquals(lsKind, dateValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/list";
    }

	@RequestMapping(params = { "find=ByLsKindEqualsAndDateValueLessThanEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndDateValueLessThanEqualsForm(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/findLsThingValuesByLsKindEqualsAndDateValueLessThanEquals";
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndDateValueLessThanEquals", method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndDateValueLessThanEquals(@RequestParam("lsKind") String lsKind, @RequestParam("dateValue") @DateTimeFormat(style = "MM") Date dateValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndDateValueLessThanEquals(lsKind, dateValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingValue.countFindLsThingValuesByLsKindEqualsAndDateValueLessThanEquals(lsKind, dateValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndDateValueLessThanEquals(lsKind, dateValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/list";
    }

	@RequestMapping(params = { "find=ByLsKindEqualsAndDateValueLike", "form" }, method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndDateValueLikeForm(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/findLsThingValuesByLsKindEqualsAndDateValueLike";
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndDateValueLike", method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndDateValueLike(@RequestParam("lsKind") String lsKind, @RequestParam("dateValue") @DateTimeFormat(style = "MM") Date dateValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndDateValueLike(lsKind, dateValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingValue.countFindLsThingValuesByLsKindEqualsAndDateValueLike(lsKind, dateValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndDateValueLike(lsKind, dateValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/list";
    }

	@RequestMapping(params = { "find=ByLsKindEqualsAndNumericValueEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndNumericValueEqualsForm(Model uiModel) {
        return "lsthingvalues/findLsThingValuesByLsKindEqualsAndNumericValueEquals";
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndNumericValueEquals", method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndNumericValueEquals(@RequestParam("lsKind") String lsKind, @RequestParam("numericValue") BigDecimal numericValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndNumericValueEquals(lsKind, numericValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingValue.countFindLsThingValuesByLsKindEqualsAndNumericValueEquals(lsKind, numericValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndNumericValueEquals(lsKind, numericValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/list";
    }

	@RequestMapping(params = { "find=ByLsKindEqualsAndNumericValueGreaterThanEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndNumericValueGreaterThanEqualsForm(Model uiModel) {
        return "lsthingvalues/findLsThingValuesByLsKindEqualsAndNumericValueGreaterThanEquals";
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndNumericValueGreaterThanEquals", method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndNumericValueGreaterThanEquals(@RequestParam("lsKind") String lsKind, @RequestParam("numericValue") BigDecimal numericValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndNumericValueGreaterThanEquals(lsKind, numericValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingValue.countFindLsThingValuesByLsKindEqualsAndNumericValueGreaterThanEquals(lsKind, numericValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndNumericValueGreaterThanEquals(lsKind, numericValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/list";
    }

	@RequestMapping(params = { "find=ByLsKindEqualsAndNumericValueLessThanEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndNumericValueLessThanEqualsForm(Model uiModel) {
        return "lsthingvalues/findLsThingValuesByLsKindEqualsAndNumericValueLessThanEquals";
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndNumericValueLessThanEquals", method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndNumericValueLessThanEquals(@RequestParam("lsKind") String lsKind, @RequestParam("numericValue") BigDecimal numericValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndNumericValueLessThanEquals(lsKind, numericValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingValue.countFindLsThingValuesByLsKindEqualsAndNumericValueLessThanEquals(lsKind, numericValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndNumericValueLessThanEquals(lsKind, numericValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/list";
    }

	@RequestMapping(params = { "find=ByLsKindEqualsAndStringValueEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndStringValueEqualsForm(Model uiModel) {
        return "lsthingvalues/findLsThingValuesByLsKindEqualsAndStringValueEquals";
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndStringValueEquals", method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndStringValueEquals(@RequestParam("lsKind") String lsKind, @RequestParam("stringValue") String stringValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndStringValueEquals(lsKind, stringValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingValue.countFindLsThingValuesByLsKindEqualsAndStringValueEquals(lsKind, stringValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndStringValueEquals(lsKind, stringValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/list";
    }

	@RequestMapping(params = { "find=ByLsKindEqualsAndStringValueLike", "form" }, method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndStringValueLikeForm(Model uiModel) {
        return "lsthingvalues/findLsThingValuesByLsKindEqualsAndStringValueLike";
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndStringValueLike", method = RequestMethod.GET)
    public String findLsThingValuesByLsKindEqualsAndStringValueLike(@RequestParam("lsKind") String lsKind, @RequestParam("stringValue") String stringValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndStringValueLike(lsKind, stringValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingValue.countFindLsThingValuesByLsKindEqualsAndStringValueLike(lsKind, stringValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsKindEqualsAndStringValueLike(lsKind, stringValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/list";
    }

	@RequestMapping(params = { "find=ByLsState", "form" }, method = RequestMethod.GET)
    public String findLsThingValuesByLsStateForm(Model uiModel) {
        uiModel.addAttribute("lsthingstates", LsThingState.findAllLsThingStates());
        return "lsthingvalues/findLsThingValuesByLsState";
    }

	@RequestMapping(params = "find=ByLsState", method = RequestMethod.GET)
    public String findLsThingValuesByLsState(@RequestParam("lsState") LsThingState lsState, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsState(lsState, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingValue.countFindLsThingValuesByLsState(lsState) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsState(lsState, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/list";
    }

	@RequestMapping(params = { "find=ByLsTransactionEquals", "form" }, method = RequestMethod.GET)
    public String findLsThingValuesByLsTransactionEqualsForm(Model uiModel) {
        return "lsthingvalues/findLsThingValuesByLsTransactionEquals";
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET)
    public String findLsThingValuesByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsThingValue.countFindLsThingValuesByLsTransactionEquals(lsTransaction) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValuesByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/list";
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LsThingValue lsThingValue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsThingValue);
            return "lsthingvalues/create";
        }
        uiModel.asMap().clear();
        lsThingValue.persist();
        return "redirect:/lsthingvalues/" + encodeUrlPathSegment(lsThingValue.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LsThingValue());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (LsThingState.countLsThingStates() == 0) {
            dependencies.add(new String[] { "lsState", "lsthingstates" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "lsthingvalues/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("lsthingvalue", LsThingValue.findLsThingValue(id));
        uiModel.addAttribute("itemId", id);
        return "lsthingvalues/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsthingvalues", LsThingValue.findLsThingValueEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LsThingValue.countLsThingValues() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsthingvalues", LsThingValue.findAllLsThingValues(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "lsthingvalues/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LsThingValue lsThingValue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsThingValue);
            return "lsthingvalues/update";
        }
        uiModel.asMap().clear();
        lsThingValue.merge();
        return "redirect:/lsthingvalues/" + encodeUrlPathSegment(lsThingValue.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LsThingValue.findLsThingValue(id));
        return "lsthingvalues/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LsThingValue lsThingValue = LsThingValue.findLsThingValue(id);
        lsThingValue.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lsthingvalues";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("lsThingValue_datevalue_date_format", org.joda.time.format.DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("lsThingValue_recordeddate_date_format", org.joda.time.format.DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("lsThingValue_modifieddate_date_format", org.joda.time.format.DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, LsThingValue lsThingValue) {
        uiModel.addAttribute("lsThingValue", lsThingValue);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("lsthingstates", LsThingState.findAllLsThingStates());
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
            LsThingValue lsThingValue = LsThingValue.findLsThingValue(id);
            if (lsThingValue == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(lsThingValue.toJson(), headers, HttpStatus.OK);
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
            List<LsThingValue> result = LsThingValue.findAllLsThingValues();
            return new ResponseEntity<String>(LsThingValue.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            LsThingValue lsThingValue = LsThingValue.fromJsonToLsThingValue(json);
            lsThingValue.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+lsThingValue.getId().toString()).build().toUriString());
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
            for (LsThingValue lsThingValue: LsThingValue.fromJsonArrayToLsThingValues(json)) {
                lsThingValue.persist();
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
            LsThingValue lsThingValue = LsThingValue.fromJsonToLsThingValue(json);
            lsThingValue.setId(id);
            if (lsThingValue.merge() == null) {
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
            LsThingValue lsThingValue = LsThingValue.findLsThingValue(id);
            if (lsThingValue == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            lsThingValue.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByCodeValueEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingValuesByCodeValueEquals(@RequestParam("codeValue") String codeValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingValue.toJsonArray(LsThingValue.findLsThingValuesByCodeValueEquals(codeValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByIgnoredNotAndCodeValueEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingValuesByIgnoredNotAndCodeValueEquals(@RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam("codeValue") String codeValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingValue.toJsonArray(LsThingValue.findLsThingValuesByIgnoredNotAndCodeValueEquals(ignored, codeValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndCodeValueLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingValuesByLsKindEqualsAndCodeValueLike(@RequestParam("lsKind") String lsKind, @RequestParam("codeValue") String codeValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingValue.toJsonArray(LsThingValue.findLsThingValuesByLsKindEqualsAndCodeValueLike(lsKind, codeValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndDateValueGreaterThanEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingValuesByLsKindEqualsAndDateValueGreaterThanEquals(@RequestParam("lsKind") String lsKind, @RequestParam("dateValue") @DateTimeFormat(style = "MM") Date dateValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingValue.toJsonArray(LsThingValue.findLsThingValuesByLsKindEqualsAndDateValueGreaterThanEquals(lsKind, dateValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndDateValueLessThanEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingValuesByLsKindEqualsAndDateValueLessThanEquals(@RequestParam("lsKind") String lsKind, @RequestParam("dateValue") @DateTimeFormat(style = "MM") Date dateValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingValue.toJsonArray(LsThingValue.findLsThingValuesByLsKindEqualsAndDateValueLessThanEquals(lsKind, dateValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndDateValueLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingValuesByLsKindEqualsAndDateValueLike(@RequestParam("lsKind") String lsKind, @RequestParam("dateValue") @DateTimeFormat(style = "MM") Date dateValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingValue.toJsonArray(LsThingValue.findLsThingValuesByLsKindEqualsAndDateValueLike(lsKind, dateValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndNumericValueEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingValuesByLsKindEqualsAndNumericValueEquals(@RequestParam("lsKind") String lsKind, @RequestParam("numericValue") BigDecimal numericValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingValue.toJsonArray(LsThingValue.findLsThingValuesByLsKindEqualsAndNumericValueEquals(lsKind, numericValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndNumericValueGreaterThanEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingValuesByLsKindEqualsAndNumericValueGreaterThanEquals(@RequestParam("lsKind") String lsKind, @RequestParam("numericValue") BigDecimal numericValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingValue.toJsonArray(LsThingValue.findLsThingValuesByLsKindEqualsAndNumericValueGreaterThanEquals(lsKind, numericValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndNumericValueLessThanEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingValuesByLsKindEqualsAndNumericValueLessThanEquals(@RequestParam("lsKind") String lsKind, @RequestParam("numericValue") BigDecimal numericValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingValue.toJsonArray(LsThingValue.findLsThingValuesByLsKindEqualsAndNumericValueLessThanEquals(lsKind, numericValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndStringValueEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingValuesByLsKindEqualsAndStringValueEquals(@RequestParam("lsKind") String lsKind, @RequestParam("stringValue") String stringValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingValue.toJsonArray(LsThingValue.findLsThingValuesByLsKindEqualsAndStringValueEquals(lsKind, stringValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndStringValueLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingValuesByLsKindEqualsAndStringValueLike(@RequestParam("lsKind") String lsKind, @RequestParam("stringValue") String stringValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingValue.toJsonArray(LsThingValue.findLsThingValuesByLsKindEqualsAndStringValueLike(lsKind, stringValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsState", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingValuesByLsState(@RequestParam("lsState") LsThingState lsState) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingValue.toJsonArray(LsThingValue.findLsThingValuesByLsState(lsState).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsThingValuesByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsThingValue.toJsonArray(LsThingValue.findLsThingValuesByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
