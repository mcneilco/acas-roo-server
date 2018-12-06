package com.labsynch.labseer.web;
import javax.servlet.http.HttpServletRequest;

import org.gvnix.addon.datatables.annotations.GvNIXDatatables;
import org.gvnix.addon.web.mvc.annotations.jquery.GvNIXWebJQuery;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.ParentAlias;

@RequestMapping("/parentaliases")
@Controller
@Transactional
@GvNIXWebJQuery
@GvNIXDatatables(ajax = false)
@RooWebJson(jsonObject = ParentAlias.class)
@RooWebScaffold(path = "parentaliases", formBackingObject = ParentAlias.class)
@RooWebFinder
public class ParentAliasController {
    @RequestMapping(produces = "text/html", value = "/list")
    public String listDatatablesDetail(Model uiModel, HttpServletRequest request, @ModelAttribute ParentAlias parentAlias) {
        // Do common datatables operations: get entity list filtered by request parameters
        listDatatables(uiModel, request, parentAlias);
        // Show only the list fragment (without footer, header, menu, etc.) 
        return "forward:/WEB-INF/views/parentaliases/list.jspx";
    }
    
	
}
