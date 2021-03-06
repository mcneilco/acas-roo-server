// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.UnitKind;
import com.labsynch.labseer.domain.UnitType;
import com.labsynch.labseer.web.UnitKindController;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect UnitKindController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String UnitKindController.create(@Valid UnitKind unitKind, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, unitKind);
            return "unitkinds/create";
        }
        uiModel.asMap().clear();
        unitKind.persist();
        return "redirect:/unitkinds/" + encodeUrlPathSegment(unitKind.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String UnitKindController.createForm(Model uiModel) {
        populateEditForm(uiModel, new UnitKind());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (UnitType.countUnitTypes() == 0) {
            dependencies.add(new String[] { "lsType", "unittypes" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "unitkinds/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String UnitKindController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("unitkind", UnitKind.findUnitKind(id));
        uiModel.addAttribute("itemId", id);
        return "unitkinds/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String UnitKindController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("unitkinds", UnitKind.findUnitKindEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) UnitKind.countUnitKinds() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("unitkinds", UnitKind.findAllUnitKinds(sortFieldName, sortOrder));
        }
        return "unitkinds/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String UnitKindController.update(@Valid UnitKind unitKind, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, unitKind);
            return "unitkinds/update";
        }
        uiModel.asMap().clear();
        unitKind.merge();
        return "redirect:/unitkinds/" + encodeUrlPathSegment(unitKind.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String UnitKindController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, UnitKind.findUnitKind(id));
        return "unitkinds/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String UnitKindController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        UnitKind unitKind = UnitKind.findUnitKind(id);
        unitKind.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/unitkinds";
    }
    
    void UnitKindController.populateEditForm(Model uiModel, UnitKind unitKind) {
        uiModel.addAttribute("unitKind", unitKind);
        uiModel.addAttribute("unittypes", UnitType.findAllUnitTypes());
    }
    
    String UnitKindController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
