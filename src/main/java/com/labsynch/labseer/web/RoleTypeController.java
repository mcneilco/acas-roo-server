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

import com.labsynch.labseer.domain.RoleType;

@RooWebJson(jsonObject = RoleType.class)
@Controller
@RequestMapping("/roletypes")
@RooWebScaffold(path = "roletypes", formBackingObject = RoleType.class)
@RooWebFinder
public class RoleTypeController {

//    @RequestMapping(method = RequestMethod.POST, value = "{id}")
//    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
//    }
//
//    @RequestMapping
//    public String index() {
//        return "roletype/index";
//    }
}
