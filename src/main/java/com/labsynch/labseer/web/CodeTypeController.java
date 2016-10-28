package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.CodeType;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/codetypes")
@Controller
@RooWebScaffold(path = "codetypes", formBackingObject = CodeType.class)
@RooWebFinder
@RooWebJson(jsonObject = CodeType.class)
public class CodeTypeController {
}
