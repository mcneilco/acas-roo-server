// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ProtocolType;
import com.labsynch.labseer.web.ProtocolTypeController;
import java.io.UnsupportedEncodingException;
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

privileged aspect ProtocolTypeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ProtocolTypeController.create(@Valid ProtocolType protocolType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, protocolType);
            return "protocoltypes/create";
        }
        uiModel.asMap().clear();
        protocolType.persist();
        return "redirect:/protocoltypes/" + encodeUrlPathSegment(protocolType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ProtocolTypeController.createForm(Model uiModel) {
        populateEditForm(uiModel, new ProtocolType());
        return "protocoltypes/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ProtocolTypeController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("protocoltype", ProtocolType.findProtocolType(id));
        uiModel.addAttribute("itemId", id);
        return "protocoltypes/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ProtocolTypeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocoltypes", ProtocolType.findProtocolTypeEntries(firstResult, sizeNo));
            float nrOfPages = (float) ProtocolType.countProtocolTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocoltypes", ProtocolType.findAllProtocolTypes());
        }
        return "protocoltypes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ProtocolTypeController.update(@Valid ProtocolType protocolType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, protocolType);
            return "protocoltypes/update";
        }
        uiModel.asMap().clear();
        protocolType.merge();
        return "redirect:/protocoltypes/" + encodeUrlPathSegment(protocolType.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ProtocolTypeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ProtocolType.findProtocolType(id));
        return "protocoltypes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ProtocolTypeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ProtocolType protocolType = ProtocolType.findProtocolType(id);
        protocolType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/protocoltypes";
    }
    
    void ProtocolTypeController.populateEditForm(Model uiModel, ProtocolType protocolType) {
        uiModel.addAttribute("protocolType", protocolType);
    }
    
    String ProtocolTypeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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