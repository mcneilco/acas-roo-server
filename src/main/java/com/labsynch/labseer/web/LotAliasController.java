package com.labsynch.labseer.web;
import javax.servlet.http.HttpServletRequest;


import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.LotAlias;

@RequestMapping("/lotaliases")
@Controller
@RooWebScaffold(path = "lotaliases", formBackingObject = LotAlias.class)

@RooWebJson(jsonObject = LotAlias.class)
@RooWebFinder
public class LotAliasController {
	

}
