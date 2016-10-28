package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsThingValue;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = LsThingValue.class)
@Controller
@RequestMapping("/lsthingvalues")
@RooWebScaffold(path = "lsthingvalues", formBackingObject = LsThingValue.class)
@RooWebFinder
public class LsThingValueController {
}
