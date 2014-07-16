package com.labsynch.labseer.web;

import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.FileThing;

@RequestMapping("/filethings")
@Controller
@RooWebScaffold(path = "filethings", formBackingObject = FileThing.class)
@RooWebJson(jsonObject = FileThing.class)
public class FileThingController {
}
