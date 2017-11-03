// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsSeqItxCntrCntr;
import com.labsynch.labseer.web.LsSeqItxCntrCntrController;
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

privileged aspect LsSeqItxCntrCntrController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String LsSeqItxCntrCntrController.create(@Valid LsSeqItxCntrCntr lsSeqItxCntrCntr, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsSeqItxCntrCntr);
            return "lsseqitxcntrcntrs/create";
        }
        uiModel.asMap().clear();
        lsSeqItxCntrCntr.persist();
        return "redirect:/lsseqitxcntrcntrs/" + encodeUrlPathSegment(lsSeqItxCntrCntr.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String LsSeqItxCntrCntrController.createForm(Model uiModel) {
        populateEditForm(uiModel, new LsSeqItxCntrCntr());
        return "lsseqitxcntrcntrs/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String LsSeqItxCntrCntrController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("lsseqitxcntrcntr", LsSeqItxCntrCntr.findLsSeqItxCntrCntr(id));
        uiModel.addAttribute("itemId", id);
        return "lsseqitxcntrcntrs/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String LsSeqItxCntrCntrController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsseqitxcntrcntrs", LsSeqItxCntrCntr.findLsSeqItxCntrCntrEntries(firstResult, sizeNo));
            float nrOfPages = (float) LsSeqItxCntrCntr.countLsSeqItxCntrCntrs() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsseqitxcntrcntrs", LsSeqItxCntrCntr.findAllLsSeqItxCntrCntrs());
        }
        return "lsseqitxcntrcntrs/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String LsSeqItxCntrCntrController.update(@Valid LsSeqItxCntrCntr lsSeqItxCntrCntr, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsSeqItxCntrCntr);
            return "lsseqitxcntrcntrs/update";
        }
        uiModel.asMap().clear();
        lsSeqItxCntrCntr.merge();
        return "redirect:/lsseqitxcntrcntrs/" + encodeUrlPathSegment(lsSeqItxCntrCntr.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String LsSeqItxCntrCntrController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LsSeqItxCntrCntr.findLsSeqItxCntrCntr(id));
        return "lsseqitxcntrcntrs/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String LsSeqItxCntrCntrController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LsSeqItxCntrCntr lsSeqItxCntrCntr = LsSeqItxCntrCntr.findLsSeqItxCntrCntr(id);
        lsSeqItxCntrCntr.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lsseqitxcntrcntrs";
    }
    
    void LsSeqItxCntrCntrController.populateEditForm(Model uiModel, LsSeqItxCntrCntr lsSeqItxCntrCntr) {
        uiModel.addAttribute("lsSeqItxCntrCntr", lsSeqItxCntrCntr);
    }
    
    String LsSeqItxCntrCntrController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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