package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxExperimentExperiment;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = ItxExperimentExperiment.class)
@Controller
@RequestMapping("/itxexperimentexperiments")
@RooWebScaffold(path = "itxexperimentexperiments", formBackingObject = ItxExperimentExperiment.class)
@RooWebFinder
public class ItxExperimentExperimentController {
}
