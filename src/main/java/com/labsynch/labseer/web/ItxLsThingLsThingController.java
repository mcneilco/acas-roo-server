package com.labsynch.labseer.web;

import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.ItxLsThingLsThing;

@RooWebJson(jsonObject = ItxLsThingLsThing.class)
@Controller
@RequestMapping("/itxlsthinglsthings")
@RooWebScaffold(path = "itxlsthinglsthings", formBackingObject = ItxLsThingLsThing.class)
@RooWebFinder
public class ItxLsThingLsThingController {

}
