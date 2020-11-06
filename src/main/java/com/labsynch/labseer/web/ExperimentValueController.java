package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
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
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping("/experimentvalues")
@Transactional
public class ExperimentValueController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ExperimentValue experimentValue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, experimentValue);
            return "experimentvalues/create";
        }
        uiModel.asMap().clear();
        experimentValue.persist();
        return "redirect:/experimentvalues/" + encodeUrlPathSegment(experimentValue.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ExperimentValue());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (ExperimentState.countExperimentStates() == 0) {
            dependencies.add(new String[] { "lsState", "experimentstates" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "experimentvalues/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("experimentvalue", ExperimentValue.findExperimentValue(id));
        uiModel.addAttribute("itemId", id);
        return "experimentvalues/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("experimentvalues", ExperimentValue.findExperimentValueEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ExperimentValue.countExperimentValues() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("experimentvalues", ExperimentValue.findAllExperimentValues(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "experimentvalues/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ExperimentValue experimentValue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, experimentValue);
            return "experimentvalues/update";
        }
        uiModel.asMap().clear();
        experimentValue.merge();
        return "redirect:/experimentvalues/" + encodeUrlPathSegment(experimentValue.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ExperimentValue.findExperimentValue(id));
        return "experimentvalues/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ExperimentValue experimentValue = ExperimentValue.findExperimentValue(id);
        experimentValue.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/experimentvalues";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("experimentValue_datevalue_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("experimentValue_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("experimentValue_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, ExperimentValue experimentValue) {
        uiModel.addAttribute("experimentValue", experimentValue);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("experimentstates", ExperimentState.findAllExperimentStates());
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

	@RequestMapping(params = { "find=ByLsKindEqualsAndCodeValueLike", "form" }, method = RequestMethod.GET)
    public String findExperimentValuesByLsKindEqualsAndCodeValueLikeForm(Model uiModel) {
        return "experimentvalues/findExperimentValuesByLsKindEqualsAndCodeValueLike";
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndCodeValueLike", method = RequestMethod.GET)
    public String findExperimentValuesByLsKindEqualsAndCodeValueLike(@RequestParam("lsKind") String lsKind, @RequestParam("codeValue") String codeValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("experimentvalues", ExperimentValue.findExperimentValuesByLsKindEqualsAndCodeValueLike(lsKind, codeValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ExperimentValue.countFindExperimentValuesByLsKindEqualsAndCodeValueLike(lsKind, codeValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("experimentvalues", ExperimentValue.findExperimentValuesByLsKindEqualsAndCodeValueLike(lsKind, codeValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "experimentvalues/list";
    }

	@RequestMapping(params = { "find=ByLsKindEqualsAndStringValueLike", "form" }, method = RequestMethod.GET)
    public String findExperimentValuesByLsKindEqualsAndStringValueLikeForm(Model uiModel) {
        return "experimentvalues/findExperimentValuesByLsKindEqualsAndStringValueLike";
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndStringValueLike", method = RequestMethod.GET)
    public String findExperimentValuesByLsKindEqualsAndStringValueLike(@RequestParam("lsKind") String lsKind, @RequestParam("stringValue") String stringValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("experimentvalues", ExperimentValue.findExperimentValuesByLsKindEqualsAndStringValueLike(lsKind, stringValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ExperimentValue.countFindExperimentValuesByLsKindEqualsAndStringValueLike(lsKind, stringValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("experimentvalues", ExperimentValue.findExperimentValuesByLsKindEqualsAndStringValueLike(lsKind, stringValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "experimentvalues/list";
    }

	@RequestMapping(params = { "find=ByLsState", "form" }, method = RequestMethod.GET)
    public String findExperimentValuesByLsStateForm(Model uiModel) {
        uiModel.addAttribute("experimentstates", ExperimentState.findAllExperimentStates());
        return "experimentvalues/findExperimentValuesByLsState";
    }

	@RequestMapping(params = "find=ByLsState", method = RequestMethod.GET)
    public String findExperimentValuesByLsState(@RequestParam("lsState") ExperimentState lsState, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("experimentvalues", ExperimentValue.findExperimentValuesByLsState(lsState, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ExperimentValue.countFindExperimentValuesByLsState(lsState) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("experimentvalues", ExperimentValue.findExperimentValuesByLsState(lsState, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "experimentvalues/list";
    }

	@RequestMapping(params = { "find=ByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEquals", "form" }, method = RequestMethod.GET)
    public String findExperimentValuesByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEqualsForm(Model uiModel) {
        uiModel.addAttribute("experimentstates", ExperimentState.findAllExperimentStates());
        return "experimentvalues/findExperimentValuesByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEquals";
    }

	@RequestMapping(params = "find=ByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEquals", method = RequestMethod.GET)
    public String findExperimentValuesByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEquals(@RequestParam("lsState") ExperimentState lsState, @RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam("lsKind") String lsKind, @RequestParam("lsType") String lsType, @RequestParam("stringValue") String stringValue, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("experimentvalues", ExperimentValue.findExperimentValuesByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEquals(lsState, ignored, lsKind, lsType, stringValue, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ExperimentValue.countFindExperimentValuesByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEquals(lsState, ignored, lsKind, lsType, stringValue) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("experimentvalues", ExperimentValue.findExperimentValuesByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEquals(lsState, ignored, lsKind, lsType, stringValue, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "experimentvalues/list";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            ExperimentValue experimentValue = ExperimentValue.findExperimentValue(id);
            if (experimentValue == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(experimentValue.toJson(), headers, HttpStatus.OK);
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
            List<ExperimentValue> result = ExperimentValue.findAllExperimentValues();
            return new ResponseEntity<String>(ExperimentValue.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ExperimentValue experimentValue = ExperimentValue.fromJsonToExperimentValue(json);
            experimentValue.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+experimentValue.getId().toString()).build().toUriString());
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
            for (ExperimentValue experimentValue: ExperimentValue.fromJsonArrayToExperimentValues(json)) {
                experimentValue.persist();
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
            ExperimentValue experimentValue = ExperimentValue.fromJsonToExperimentValue(json);
            experimentValue.setId(id);
            if (experimentValue.merge() == null) {
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
            ExperimentValue experimentValue = ExperimentValue.findExperimentValue(id);
            if (experimentValue == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            experimentValue.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndCodeValueLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindExperimentValuesByLsKindEqualsAndCodeValueLike(@RequestParam("lsKind") String lsKind, @RequestParam("codeValue") String codeValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ExperimentValue.toJsonArray(ExperimentValue.findExperimentValuesByLsKindEqualsAndCodeValueLike(lsKind, codeValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindEqualsAndStringValueLike", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindExperimentValuesByLsKindEqualsAndStringValueLike(@RequestParam("lsKind") String lsKind, @RequestParam("stringValue") String stringValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ExperimentValue.toJsonArray(ExperimentValue.findExperimentValuesByLsKindEqualsAndStringValueLike(lsKind, stringValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsState", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindExperimentValuesByLsState(@RequestParam("lsState") ExperimentState lsState) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ExperimentValue.toJsonArray(ExperimentValue.findExperimentValuesByLsState(lsState).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindExperimentValuesByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEquals(@RequestParam("lsState") ExperimentState lsState, @RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam("lsKind") String lsKind, @RequestParam("lsType") String lsType, @RequestParam("stringValue") String stringValue) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ExperimentValue.toJsonArray(ExperimentValue.findExperimentValuesByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEquals(lsState, ignored, lsKind, lsType, stringValue).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
