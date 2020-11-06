package com.labsynch.labseer.web;
import com.labsynch.labseer.domain.SaltFormAliasType;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

@RequestMapping("/saltformaliastypes")
@Controller
@RooWebScaffold(path = "saltformaliastypes", formBackingObject = SaltFormAliasType.class)
public class SaltFormAliasTypeController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid SaltFormAliasType saltFormAliasType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, saltFormAliasType);
            return "saltformaliastypes/create";
        }
        uiModel.asMap().clear();
        saltFormAliasType.persist();
        return "redirect:/saltformaliastypes/" + encodeUrlPathSegment(saltFormAliasType.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new SaltFormAliasType());
        return "saltformaliastypes/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("saltformaliastype", SaltFormAliasType.findSaltFormAliasType(id));
        uiModel.addAttribute("itemId", id);
        return "saltformaliastypes/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("saltformaliastypes", SaltFormAliasType.findSaltFormAliasTypeEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) SaltFormAliasType.countSaltFormAliasTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("saltformaliastypes", SaltFormAliasType.findAllSaltFormAliasTypes(sortFieldName, sortOrder));
        }
        return "saltformaliastypes/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid SaltFormAliasType saltFormAliasType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, saltFormAliasType);
            return "saltformaliastypes/update";
        }
        uiModel.asMap().clear();
        saltFormAliasType.merge();
        return "redirect:/saltformaliastypes/" + encodeUrlPathSegment(saltFormAliasType.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, SaltFormAliasType.findSaltFormAliasType(id));
        return "saltformaliastypes/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        SaltFormAliasType saltFormAliasType = SaltFormAliasType.findSaltFormAliasType(id);
        saltFormAliasType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/saltformaliastypes";
    }

	void populateEditForm(Model uiModel, SaltFormAliasType saltFormAliasType) {
        uiModel.addAttribute("saltFormAliasType", saltFormAliasType);
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
