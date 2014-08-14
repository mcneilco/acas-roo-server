package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsThingState;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = LsThingState.class)
@Controller
@RequestMapping("/lsthingstates")
@RooWebScaffold(path = "lsthingstates", formBackingObject = LsThingState.class)
@RooWebFinder
public class LsThingStateController {
}
