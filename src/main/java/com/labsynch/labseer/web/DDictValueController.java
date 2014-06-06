package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.DDictValue;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/ddictvalues")
@Controller
@RooWebScaffold(path = "ddictvalues", formBackingObject = DDictValue.class)
@RooWebFinder
public class DDictValueController {
}
