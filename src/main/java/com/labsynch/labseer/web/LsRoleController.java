package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.LsRole;
import com.labsynch.labseer.domain.RoleKind;
import com.labsynch.labseer.domain.RoleType;
import com.labsynch.labseer.service.AnalysisGroupServiceImpl;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RooWebJson(jsonObject = LsRole.class)
@RequestMapping("/lsroles")
@Controller
@RooWebScaffold(path = "lsroles", formBackingObject = LsRole.class)
@RooWebFinder
public class LsRoleController {

    private static final Logger logger = LoggerFactory.getLogger(LsRoleController.class);

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LsRole lsRole, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsRole);
            return "lsroles/create";
        }
        uiModel.asMap().clear();
        lsRole.persist();
        return "redirect:/lsroles/" + encodeUrlPathSegment(lsRole.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LsRole());
        uiModel.addAttribute("lstypes", RoleType.findAllRoleTypes());
        uiModel.addAttribute("lskinds", RoleKind.findAllRoleKinds());
        return "lsroles/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("lsrole", LsRole.findLsRole(id));
        uiModel.addAttribute("itemId", id);
        return "lsroles/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsroles", LsRole.findLsRoleEntries(firstResult, sizeNo));
            float nrOfPages = (float) LsRole.countLsRoles() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsroles", LsRole.findAllLsRoles());
        }
        return "lsroles/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LsRole lsRole, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsRole);
            return "lsroles/update";
        }
        uiModel.asMap().clear();
        lsRole.merge();
        return "redirect:/lsroles/" + encodeUrlPathSegment(lsRole.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LsRole.findLsRole(id));
        return "lsroles/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LsRole lsRole = LsRole.findLsRole(id);
        lsRole.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lsroles";
    }

    void populateEditForm(Model uiModel, LsRole lsRole) {
        uiModel.addAttribute("lsRole", lsRole);
        uiModel.addAttribute("authorroles", AuthorRole.findAllAuthorRoles());
        uiModel.addAttribute("collectiontypes", RoleType.findAllRoleTypes());
        uiModel.addAttribute("collectionkinds", RoleKind.findAllRoleKinds());
    }

    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        return pathSegment;
    }
}
