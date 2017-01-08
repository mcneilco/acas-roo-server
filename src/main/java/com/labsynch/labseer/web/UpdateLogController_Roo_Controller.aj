// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.UpdateLog;
import com.labsynch.labseer.web.UpdateLogController;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect UpdateLogController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String UpdateLogController.create(@Valid UpdateLog updateLog, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, updateLog);
            return "updatelogs/create";
        }
        uiModel.asMap().clear();
        updateLog.persist();
        return "redirect:/updatelogs/" + encodeUrlPathSegment(updateLog.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String UpdateLogController.createForm(Model uiModel) {
        populateEditForm(uiModel, new UpdateLog());
        return "updatelogs/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String UpdateLogController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("updatelog", UpdateLog.findUpdateLog(id));
        uiModel.addAttribute("itemId", id);
        return "updatelogs/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String UpdateLogController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("updatelogs", UpdateLog.findUpdateLogEntries(firstResult, sizeNo));
            float nrOfPages = (float) UpdateLog.countUpdateLogs() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("updatelogs", UpdateLog.findAllUpdateLogs());
        }
        addDateTimeFormatPatterns(uiModel);
        return "updatelogs/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String UpdateLogController.update(@Valid UpdateLog updateLog, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, updateLog);
            return "updatelogs/update";
        }
        uiModel.asMap().clear();
        updateLog.merge();
        return "redirect:/updatelogs/" + encodeUrlPathSegment(updateLog.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String UpdateLogController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, UpdateLog.findUpdateLog(id));
        return "updatelogs/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String UpdateLogController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        UpdateLog updateLog = UpdateLog.findUpdateLog(id);
        updateLog.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/updatelogs";
    }
    
    void UpdateLogController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("updateLog_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }
    
    void UpdateLogController.populateEditForm(Model uiModel, UpdateLog updateLog) {
        uiModel.addAttribute("updateLog", updateLog);
        addDateTimeFormatPatterns(uiModel);
    }
    
    String UpdateLogController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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