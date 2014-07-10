// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.web.DDictValueController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect DDictValueController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByIgnoredNot", "form" }, method = RequestMethod.GET)
    public String DDictValueController.findDDictValuesByIgnoredNotForm(Model uiModel) {
        return "ddictvalues/findDDictValuesByIgnoredNot";
    }
    
    @RequestMapping(params = "find=ByIgnoredNot", method = RequestMethod.GET)
    public String DDictValueController.findDDictValuesByIgnoredNot(@RequestParam(value = "ignored", required = false) boolean ignored, Model uiModel) {
        uiModel.addAttribute("ddictvalues", DDictValue.findDDictValuesByIgnoredNot(ignored).getResultList());
        return "ddictvalues/list";
    }
    
    @RequestMapping(params = { "find=ByLsKindEquals", "form" }, method = RequestMethod.GET)
    public String DDictValueController.findDDictValuesByLsKindEqualsForm(Model uiModel) {
        return "ddictvalues/findDDictValuesByLsKindEquals";
    }
    
    @RequestMapping(params = "find=ByLsKindEquals", method = RequestMethod.GET)
    public String DDictValueController.findDDictValuesByLsKindEquals(@RequestParam("lsKind") String lsKind, Model uiModel) {
        uiModel.addAttribute("ddictvalues", DDictValue.findDDictValuesByLsKindEquals(lsKind).getResultList());
        return "ddictvalues/list";
    }
    
    @RequestMapping(params = { "find=ByLsTypeEquals", "form" }, method = RequestMethod.GET)
    public String DDictValueController.findDDictValuesByLsTypeEqualsForm(Model uiModel) {
        return "ddictvalues/findDDictValuesByLsTypeEquals";
    }
    
    @RequestMapping(params = "find=ByLsTypeEquals", method = RequestMethod.GET)
    public String DDictValueController.findDDictValuesByLsTypeEquals(@RequestParam("lsType") String lsType, Model uiModel) {
        uiModel.addAttribute("ddictvalues", DDictValue.findDDictValuesByLsTypeEquals(lsType).getResultList());
        return "ddictvalues/list";
    }
    
    @RequestMapping(params = { "find=ByLsTypeEqualsAndLsKindEquals", "form" }, method = RequestMethod.GET)
    public String DDictValueController.findDDictValuesByLsTypeEqualsAndLsKindEqualsForm(Model uiModel) {
        return "ddictvalues/findDDictValuesByLsTypeEqualsAndLsKindEquals";
    }
    
    @RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET)
    public String DDictValueController.findDDictValuesByLsTypeEqualsAndLsKindEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, Model uiModel) {
        uiModel.addAttribute("ddictvalues", DDictValue.findDDictValuesByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList());
        return "ddictvalues/list";
    }
    
}
