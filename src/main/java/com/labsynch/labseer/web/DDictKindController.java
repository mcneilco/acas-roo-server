package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.DDictKind;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/ddictkinds")
@Controller
@RooWebScaffold(path = "ddictkinds", formBackingObject = DDictKind.class)
@RooWebFinder
public class DDictKindController {
}
