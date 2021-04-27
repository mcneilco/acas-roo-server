// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.StereoCategory;
import com.labsynch.labseer.web.StereoCategoryController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect StereoCategoryController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByCodeEquals", "form" }, method = RequestMethod.GET)
    public String StereoCategoryController.findStereoCategorysByCodeEqualsForm(Model uiModel) {
        return "stereocategorys/findStereoCategorysByCodeEquals";
    }
    
    @RequestMapping(params = "find=ByCodeEquals", method = RequestMethod.GET)
    public String StereoCategoryController.findStereoCategorysByCodeEquals(@RequestParam("code") String code, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("stereocategorys", StereoCategory.findStereoCategorysByCodeEquals(code, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) StereoCategory.countFindStereoCategorysByCodeEquals(code) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("stereocategorys", StereoCategory.findStereoCategorysByCodeEquals(code, sortFieldName, sortOrder).getResultList());
        }
        return "stereocategorys/list";
    }
    
    @RequestMapping(params = { "find=ByCodeLike", "form" }, method = RequestMethod.GET)
    public String StereoCategoryController.findStereoCategorysByCodeLikeForm(Model uiModel) {
        return "stereocategorys/findStereoCategorysByCodeLike";
    }
    
    @RequestMapping(params = "find=ByCodeLike", method = RequestMethod.GET)
    public String StereoCategoryController.findStereoCategorysByCodeLike(@RequestParam("code") String code, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("stereocategorys", StereoCategory.findStereoCategorysByCodeLike(code, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) StereoCategory.countFindStereoCategorysByCodeLike(code) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("stereocategorys", StereoCategory.findStereoCategorysByCodeLike(code, sortFieldName, sortOrder).getResultList());
        }
        return "stereocategorys/list";
    }
    
}