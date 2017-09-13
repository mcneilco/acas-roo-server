package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ItxProtocolProtocol;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/itxprotocolprotocols")
@Controller
@RooWebScaffold(path = "itxprotocolprotocols", formBackingObject = ItxProtocolProtocol.class)
@RooWebJson(jsonObject = ItxProtocolProtocol.class)
@RooWebFinder
public class ItxProtocolProtocolController {
}
