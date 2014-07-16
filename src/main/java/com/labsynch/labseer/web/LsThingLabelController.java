package com.labsynch.labseer.web;

import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.LsThingLabel;

@RooWebJson(jsonObject = LsThingLabel.class)
@Controller
@RequestMapping("/lsthinglabels")
@RooWebScaffold(path = "lsthinglabels", formBackingObject = LsThingLabel.class)
@RooWebFinder
public class LsThingLabelController {
}
