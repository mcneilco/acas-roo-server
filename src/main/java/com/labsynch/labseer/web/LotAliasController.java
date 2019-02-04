package com.labsynch.labseer.web;
import javax.servlet.http.HttpServletRequest;

import org.gvnix.addon.datatables.annotations.GvNIXDatatables;
import org.gvnix.addon.web.mvc.annotations.jquery.GvNIXWebJQuery;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.LotAlias;

@RequestMapping("/lotaliases")
@Controller
@RooWebScaffold(path = "lotaliases", formBackingObject = LotAlias.class)
@GvNIXWebJQuery
@GvNIXDatatables(ajax = false)
@RooWebJson(jsonObject = LotAlias.class)
@RooWebFinder
public class LotAliasController {
	
    @RequestMapping(produces = "text/html", value = "/list")
    public String listDatatablesDetail(Model uiModel, HttpServletRequest request, @ModelAttribute LotAlias lotAlias) {
        // Do common datatables operations: get entity list filtered by request parameters
        listDatatables(uiModel, request, lotAlias);
        // Show only the list fragment (without footer, header, menu, etc.) 
        return "forward:/WEB-INF/views/lotaliases/list.jspx";
    }
    
}
