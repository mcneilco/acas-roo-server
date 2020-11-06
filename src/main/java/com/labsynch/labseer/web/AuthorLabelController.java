package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorLabel;
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

@RequestMapping("/authorlabels")
@Controller
@RooWebScaffold(path = "authorlabels", formBackingObject = AuthorLabel.class)
public class AuthorLabelController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid AuthorLabel authorLabel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, authorLabel);
            return "authorlabels/create";
        }
        uiModel.asMap().clear();
        authorLabel.persist();
        return "redirect:/authorlabels/" + encodeUrlPathSegment(authorLabel.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new AuthorLabel());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Author.countAuthors() == 0) {
            dependencies.add(new String[] { "author", "authors" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "authorlabels/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("authorlabel", AuthorLabel.findAuthorLabel(id));
        uiModel.addAttribute("itemId", id);
        return "authorlabels/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("authorlabels", AuthorLabel.findAuthorLabelEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) AuthorLabel.countAuthorLabels() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("authorlabels", AuthorLabel.findAllAuthorLabels(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "authorlabels/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid AuthorLabel authorLabel, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, authorLabel);
            return "authorlabels/update";
        }
        uiModel.asMap().clear();
        authorLabel.merge();
        return "redirect:/authorlabels/" + encodeUrlPathSegment(authorLabel.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, AuthorLabel.findAuthorLabel(id));
        return "authorlabels/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        AuthorLabel authorLabel = AuthorLabel.findAuthorLabel(id);
        authorLabel.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/authorlabels";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("authorLabel_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("authorLabel_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, AuthorLabel authorLabel) {
        uiModel.addAttribute("authorLabel", authorLabel);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("authors", Author.findAllAuthors());
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
