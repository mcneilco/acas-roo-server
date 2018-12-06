package com.labsynch.labseer.web;
import javax.servlet.http.HttpServletRequest;

import org.gvnix.addon.datatables.annotations.GvNIXDatatables;
import org.gvnix.addon.web.mvc.annotations.jquery.GvNIXWebJQuery;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.PreDef_CorpName;

@RequestMapping("/predef_corpnames")
@Controller
@RooWebScaffold(path = "predef_corpnames", formBackingObject = PreDef_CorpName.class)
@GvNIXWebJQuery
@GvNIXDatatables(ajax = false)
public class PreDef_CorpNameController {

    @RequestMapping(produces = "text/html", value = "/list")
    public String listDatatablesDetail(Model uiModel, HttpServletRequest request, @ModelAttribute PreDef_CorpName preDef_CorpName) {
        // Do common datatables operations: get entity list filtered by request parameters
        listDatatables(uiModel, request, preDef_CorpName);
        // Show only the list fragment (without footer, header, menu, etc.) 
        return "forward:/WEB-INF/views/predef_corpnames/list.jspx";
    }
}
