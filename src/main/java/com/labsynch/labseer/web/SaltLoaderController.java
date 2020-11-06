package com.labsynch.labseer.web;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import com.labsynch.labseer.service.SaltLoader;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@RequestMapping("/saltloaders")
@Controller
@RooWebScaffold(path = "saltloaders", formBackingObject = SaltLoader.class)
public class SaltLoaderController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid SaltLoader saltLoader, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, saltLoader);
            return "saltloaders/create";
        }
        uiModel.asMap().clear();
        saltLoader.persist();
        return "redirect:/saltloaders/" + encodeUrlPathSegment(saltLoader.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new SaltLoader());
        return "saltloaders/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("saltloader", SaltLoader.findSaltLoader(id));
        uiModel.addAttribute("itemId", id);
        return "saltloaders/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("saltloaders", SaltLoader.findSaltLoaderEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) SaltLoader.countSaltLoaders() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("saltloaders", SaltLoader.findAllSaltLoaders(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "saltloaders/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid SaltLoader saltLoader, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, saltLoader);
            return "saltloaders/update";
        }
        uiModel.asMap().clear();
        saltLoader.merge();
        return "redirect:/saltloaders/" + encodeUrlPathSegment(saltLoader.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, SaltLoader.findSaltLoader(id));
        return "saltloaders/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        SaltLoader saltLoader = SaltLoader.findSaltLoader(id);
        saltLoader.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/saltloaders";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("saltLoader_loadeddate_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, SaltLoader saltLoader) {
        uiModel.addAttribute("saltLoader", saltLoader);
        addDateTimeFormatPatterns(uiModel);
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
