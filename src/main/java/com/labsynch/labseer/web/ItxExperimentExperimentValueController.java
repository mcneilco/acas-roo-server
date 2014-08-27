package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxExperimentExperimentValue;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = ItxExperimentExperimentValue.class)
@Controller
@RequestMapping("/itxexperimentexperimentvalues")
@RooWebScaffold(path = "itxexperimentexperimentvalues", formBackingObject = ItxExperimentExperimentValue.class)
public class ItxExperimentExperimentValueController {
}
