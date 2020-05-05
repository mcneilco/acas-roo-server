package com.labsynch.labseer.web;
import javax.servlet.http.HttpServletRequest;

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

@RooWebJson(jsonObject = ParentAlias.class)
@RooWebScaffold(path = "parentaliases", formBackingObject = ParentAlias.class)
@RooWebFinder
public class ParentAliasController {

    
	
}
