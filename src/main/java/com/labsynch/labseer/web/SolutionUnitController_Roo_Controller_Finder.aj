// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.SolutionUnit;
import com.labsynch.labseer.web.SolutionUnitController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect SolutionUnitController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByCodeEquals", "form" }, method = RequestMethod.GET)
    public String SolutionUnitController.findSolutionUnitsByCodeEqualsForm(Model uiModel) {
        return "solutionunits/findSolutionUnitsByCodeEquals";
    }
    
    @RequestMapping(params = "find=ByCodeEquals", method = RequestMethod.GET)
    public String SolutionUnitController.findSolutionUnitsByCodeEquals(@RequestParam("code") String code, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("solutionunits", SolutionUnit.findSolutionUnitsByCodeEquals(code, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) SolutionUnit.countFindSolutionUnitsByCodeEquals(code) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("solutionunits", SolutionUnit.findSolutionUnitsByCodeEquals(code, sortFieldName, sortOrder).getResultList());
        }
        return "solutionunits/list";
    }
    
    @RequestMapping(params = { "find=ByCodeLike", "form" }, method = RequestMethod.GET)
    public String SolutionUnitController.findSolutionUnitsByCodeLikeForm(Model uiModel) {
        return "solutionunits/findSolutionUnitsByCodeLike";
    }
    
    @RequestMapping(params = "find=ByCodeLike", method = RequestMethod.GET)
    public String SolutionUnitController.findSolutionUnitsByCodeLike(@RequestParam("code") String code, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("solutionunits", SolutionUnit.findSolutionUnitsByCodeLike(code, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) SolutionUnit.countFindSolutionUnitsByCodeLike(code) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("solutionunits", SolutionUnit.findSolutionUnitsByCodeLike(code, sortFieldName, sortOrder).getResultList());
        }
        return "solutionunits/list";
    }
    
    @RequestMapping(params = { "find=ByNameEquals", "form" }, method = RequestMethod.GET)
    public String SolutionUnitController.findSolutionUnitsByNameEqualsForm(Model uiModel) {
        return "solutionunits/findSolutionUnitsByNameEquals";
    }
    
    @RequestMapping(params = "find=ByNameEquals", method = RequestMethod.GET)
    public String SolutionUnitController.findSolutionUnitsByNameEquals(@RequestParam("name") String name, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("solutionunits", SolutionUnit.findSolutionUnitsByNameEquals(name, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) SolutionUnit.countFindSolutionUnitsByNameEquals(name) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("solutionunits", SolutionUnit.findSolutionUnitsByNameEquals(name, sortFieldName, sortOrder).getResultList());
        }
        return "solutionunits/list";
    }
    
}