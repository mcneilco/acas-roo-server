package com.labsynch.labseer.web;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.domain.SaltFormAlias;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/saltformaliases")
@Controller
public class SaltFormAliasController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid SaltFormAlias saltFormAlias, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, saltFormAlias);
            return "saltformaliases/create";
        }
        uiModel.asMap().clear();
        saltFormAlias.persist();
        return "redirect:/saltformaliases/" + encodeUrlPathSegment(saltFormAlias.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new SaltFormAlias());
        return "saltformaliases/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("saltformalias", SaltFormAlias.findSaltFormAlias(id));
        uiModel.addAttribute("itemId", id);
        return "saltformaliases/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("saltformaliases", SaltFormAlias.findSaltFormAliasEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) SaltFormAlias.countSaltFormAliases() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("saltformaliases", SaltFormAlias.findAllSaltFormAliases(sortFieldName, sortOrder));
        }
        return "saltformaliases/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid SaltFormAlias saltFormAlias, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, saltFormAlias);
            return "saltformaliases/update";
        }
        uiModel.asMap().clear();
        saltFormAlias.merge();
        return "redirect:/saltformaliases/" + encodeUrlPathSegment(saltFormAlias.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, SaltFormAlias.findSaltFormAlias(id));
        return "saltformaliases/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        SaltFormAlias saltFormAlias = SaltFormAlias.findSaltFormAlias(id);
        saltFormAlias.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/saltformaliases";
    }

	void populateEditForm(Model uiModel, SaltFormAlias saltFormAlias) {
        uiModel.addAttribute("saltFormAlias", saltFormAlias);
        uiModel.addAttribute("saltforms", SaltForm.findAllSaltForms());
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

	@RequestMapping(params = { "find=ByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", "form" }, method = RequestMethod.GET)
    public String findSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEqualsForm(Model uiModel) {
        return "saltformaliases/findSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals";
    }

	@RequestMapping(params = "find=ByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET)
    public String findSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(@RequestParam("aliasName") String aliasName, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("saltformaliases", SaltFormAlias.findSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(aliasName, lsType, lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) SaltFormAlias.countFindSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(aliasName, lsType, lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("saltformaliases", SaltFormAlias.findSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(aliasName, lsType, lsKind, sortFieldName, sortOrder).getResultList());
        }
        return "saltformaliases/list";
    }

	@RequestMapping(params = { "find=BySaltForm", "form" }, method = RequestMethod.GET)
    public String findSaltFormAliasesBySaltFormForm(Model uiModel) {
        uiModel.addAttribute("saltforms", SaltForm.findAllSaltForms());
        return "saltformaliases/findSaltFormAliasesBySaltForm";
    }

	@RequestMapping(params = "find=BySaltForm", method = RequestMethod.GET)
    public String findSaltFormAliasesBySaltForm(@RequestParam("saltForm") SaltForm saltForm, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("saltformaliases", SaltFormAlias.findSaltFormAliasesBySaltForm(saltForm, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) SaltFormAlias.countFindSaltFormAliasesBySaltForm(saltForm) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("saltformaliases", SaltFormAlias.findSaltFormAliasesBySaltForm(saltForm, sortFieldName, sortOrder).getResultList());
        }
        return "saltformaliases/list";
    }
}
