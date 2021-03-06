// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.domain.SaltFormAlias;
import com.labsynch.labseer.web.SaltFormAliasController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

privileged aspect SaltFormAliasController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", "form" }, method = RequestMethod.GET)
    public String SaltFormAliasController.findSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEqualsForm(Model uiModel) {
        return "saltformaliases/findSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals";
    }
    
    @RequestMapping(params = "find=ByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET)
    public String SaltFormAliasController.findSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(@RequestParam("aliasName") String aliasName, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("saltformaliases", SaltFormAlias.findSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(aliasName, lsType, lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) SaltFormAlias.countFindSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(aliasName, lsType, lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("saltformaliases", SaltFormAlias.findSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(aliasName, lsType, lsKind, sortFieldName, sortOrder).getResultList());
        }
        return "saltformaliases/list";
    }
    
    @RequestMapping(params = { "find=BySaltForm", "form" }, method = RequestMethod.GET)
    public String SaltFormAliasController.findSaltFormAliasesBySaltFormForm(Model uiModel) {
        uiModel.addAttribute("saltforms", SaltForm.findAllSaltForms());
        return "saltformaliases/findSaltFormAliasesBySaltForm";
    }
    
    @RequestMapping(params = "find=BySaltForm", method = RequestMethod.GET)
    public String SaltFormAliasController.findSaltFormAliasesBySaltForm(@RequestParam("saltForm") SaltForm saltForm, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("saltformaliases", SaltFormAlias.findSaltFormAliasesBySaltForm(saltForm, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) SaltFormAlias.countFindSaltFormAliasesBySaltForm(saltForm) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("saltformaliases", SaltFormAlias.findSaltFormAliasesBySaltForm(saltForm, sortFieldName, sortOrder).getResultList());
        }
        return "saltformaliases/list";
    }
    
}
