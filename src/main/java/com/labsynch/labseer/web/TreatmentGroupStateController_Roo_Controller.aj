// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.web.TreatmentGroupStateController;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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

privileged aspect TreatmentGroupStateController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String TreatmentGroupStateController.create(@Valid TreatmentGroupState treatmentGroupState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, treatmentGroupState);
            return "treatmentgroupstates/create";
        }
        uiModel.asMap().clear();
        treatmentGroupState.persist();
        return "redirect:/treatmentgroupstates/" + encodeUrlPathSegment(treatmentGroupState.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String TreatmentGroupStateController.createForm(Model uiModel) {
        populateEditForm(uiModel, new TreatmentGroupState());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (TreatmentGroup.countTreatmentGroups() == 0) {
            dependencies.add(new String[] { "treatmentgroup", "treatmentgroups" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "treatmentgroupstates/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String TreatmentGroupStateController.show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("treatmentgroupstate", TreatmentGroupState.findTreatmentGroupState(id));
        uiModel.addAttribute("itemId", id);
        return "treatmentgroupstates/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String TreatmentGroupStateController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("treatmentgroupstates", TreatmentGroupState.findTreatmentGroupStateEntries(firstResult, sizeNo));
            float nrOfPages = (float) TreatmentGroupState.countTreatmentGroupStates() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("treatmentgroupstates", TreatmentGroupState.findAllTreatmentGroupStates());
        }
        addDateTimeFormatPatterns(uiModel);
        return "treatmentgroupstates/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String TreatmentGroupStateController.update(@Valid TreatmentGroupState treatmentGroupState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, treatmentGroupState);
            return "treatmentgroupstates/update";
        }
        uiModel.asMap().clear();
        treatmentGroupState.merge();
        return "redirect:/treatmentgroupstates/" + encodeUrlPathSegment(treatmentGroupState.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String TreatmentGroupStateController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, TreatmentGroupState.findTreatmentGroupState(id));
        return "treatmentgroupstates/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String TreatmentGroupStateController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        TreatmentGroupState treatmentGroupState = TreatmentGroupState.findTreatmentGroupState(id);
        treatmentGroupState.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/treatmentgroupstates";
    }
    
    void TreatmentGroupStateController.addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("treatmentGroupState_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("treatmentGroupState_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }
    
    void TreatmentGroupStateController.populateEditForm(Model uiModel, TreatmentGroupState treatmentGroupState) {
        uiModel.addAttribute("treatmentGroupState", treatmentGroupState);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("treatmentgroups", TreatmentGroup.findAllTreatmentGroups());
        uiModel.addAttribute("treatmentgroupvalues", TreatmentGroupValue.findAllTreatmentGroupValues());
    }
    
    String TreatmentGroupStateController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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