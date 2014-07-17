package com.labsynch.labseer.web;

import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.CodeKind;

@RequestMapping("/codekinds")
@Controller
@RooWebScaffold(path = "codekinds", formBackingObject = CodeKind.class)
@RooWebFinder
@RooWebJson(jsonObject = CodeKind.class)
public class CodeKindController {
}
