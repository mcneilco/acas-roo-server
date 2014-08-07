package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.DDictType;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/ddicttypes")
@Controller
@RooWebScaffold(path = "ddicttypes", formBackingObject = DDictType.class)
@RooWebFinder
@RooWebJson(jsonObject = DDictType.class)
public class DDictTypeController {
}
