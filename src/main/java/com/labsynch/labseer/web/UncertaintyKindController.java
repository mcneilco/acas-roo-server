package com.labsynch.labseer.web;

import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.UncertaintyKind;

@RequestMapping("/uncertaintykinds")
@Controller
@RooWebScaffold(path = "uncertaintykinds", formBackingObject = UncertaintyKind.class)
@RooWebJson(jsonObject = UncertaintyKind.class)
public class UncertaintyKindController {
}
