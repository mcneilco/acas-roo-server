package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.AuthorState;
import com.labsynch.labseer.domain.AuthorValue;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/authorvalues")
@Controller
@RooWebScaffold(path = "authorvalues", formBackingObject = AuthorValue.class)
public class AuthorValueController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid AuthorValue authorValue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, authorValue);
            return "authorvalues/create";
        }
        uiModel.asMap().clear();
        authorValue.persist();
        return "redirect:/authorvalues/" + encodeUrlPathSegment(authorValue.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new AuthorValue());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (AuthorState.countAuthorStates() == 0) {
            dependencies.add(new String[] { "lsState", "authorstates" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "authorvalues/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("authorvalue", AuthorValue.findAuthorValue(id));
        uiModel.addAttribute("itemId", id);
        return "authorvalues/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("authorvalues", AuthorValue.findAuthorValueEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) AuthorValue.countAuthorValues() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("authorvalues", AuthorValue.findAllAuthorValues(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "authorvalues/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid AuthorValue authorValue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, authorValue);
            return "authorvalues/update";
        }
        uiModel.asMap().clear();
        authorValue.merge();
        return "redirect:/authorvalues/" + encodeUrlPathSegment(authorValue.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, AuthorValue.findAuthorValue(id));
        return "authorvalues/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        AuthorValue authorValue = AuthorValue.findAuthorValue(id);
        authorValue.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/authorvalues";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("authorValue_datevalue_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("authorValue_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("authorValue_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, AuthorValue authorValue) {
        uiModel.addAttribute("authorValue", authorValue);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("authorstates", AuthorState.findAllAuthorStates());
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
