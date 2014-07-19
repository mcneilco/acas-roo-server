package com.labsynch.labseer.web;

import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.ItxProtocolProtocolState;

@RequestMapping("/itxprotocolprotocolstates")
@Controller
@RooWebScaffold(path = "itxprotocolprotocolstates", formBackingObject = ItxProtocolProtocolState.class)
@RooWebJson(jsonObject = ItxProtocolProtocolState.class)
public class ItxProtocolProtocolStateController {
}
