package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxExperimentExperimentState;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = ItxExperimentExperimentState.class)
@Controller
@RequestMapping("/itxexperimentexperimentstates")
@RooWebScaffold(path = "itxexperimentexperimentstates", formBackingObject = ItxExperimentExperimentState.class)
public class ItxExperimentExperimentStateController {
}
