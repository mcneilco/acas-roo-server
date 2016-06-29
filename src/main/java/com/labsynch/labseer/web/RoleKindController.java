package com.labsynch.labseer.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.labsynch.labseer.domain.RoleKind;



@RooWebJson(jsonObject = RoleKind.class)
@Controller
@RequestMapping("/rolekinds")
@RooWebScaffold(path = "rolekinds", formBackingObject = RoleKind.class)
@RooWebFinder
public class RoleKindController {

	
//    @RequestMapping(method = RequestMethod.POST, value = "{id}")
//    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
//    }
//
//    @RequestMapping
//    public String index() {
//        return "rolekind/index";
//    }
}
