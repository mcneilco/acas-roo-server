// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.domain.ValueType;
import com.labsynch.labseer.web.ValueKindController;
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

privileged aspect ValueKindController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ValueKindController.create(@Valid ValueKind valueKind, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, valueKind);
            return "valuekinds/create";
        }
        uiModel.asMap().clear();
        valueKind.persist();
        return "redirect:/valuekinds/" + encodeUrlPathSegment(valueKind.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ValueKindController.createForm(Model uiModel) {
        populateEditForm(uiModel, new ValueKind());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (ValueType.countValueTypes() == 0) {
            dependencies.add(new String[] { "lsType", "valuetypes" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "valuekinds/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ValueKindController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("valuekind", ValueKind.findValueKind(id));
        uiModel.addAttribute("itemId", id);
        return "valuekinds/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ValueKindController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("valuekinds", ValueKind.findValueKindEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ValueKind.countValueKinds() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("valuekinds", ValueKind.findAllValueKinds(sortFieldName, sortOrder));
        }
        return "valuekinds/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ValueKindController.update(@Valid ValueKind valueKind, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, valueKind);
            return "valuekinds/update";
        }
        uiModel.asMap().clear();
        valueKind.merge();
        return "redirect:/valuekinds/" + encodeUrlPathSegment(valueKind.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ValueKindController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ValueKind.findValueKind(id));
        return "valuekinds/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ValueKindController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ValueKind valueKind = ValueKind.findValueKind(id);
        valueKind.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/valuekinds";
    }
    
    void ValueKindController.populateEditForm(Model uiModel, ValueKind valueKind) {
        uiModel.addAttribute("valueKind", valueKind);
        uiModel.addAttribute("valuetypes", ValueType.findAllValueTypes());
    }
    
    String ValueKindController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
