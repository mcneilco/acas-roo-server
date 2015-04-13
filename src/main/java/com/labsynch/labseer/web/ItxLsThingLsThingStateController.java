package com.labsynch.labseer.web;

import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.ItxLsThingLsThingState;

@RooWebJson(jsonObject = ItxLsThingLsThingState.class)
@Controller
@RequestMapping("/itxlsthinglsthingstates")
@RooWebScaffold(path = "itxlsthinglsthingstates", formBackingObject = ItxLsThingLsThingState.class)
public class ItxLsThingLsThingStateController {

}
