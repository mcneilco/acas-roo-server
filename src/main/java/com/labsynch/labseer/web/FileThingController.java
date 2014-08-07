package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.FileThing;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/filethings")
@Controller
@RooWebScaffold(path = "filethings", formBackingObject = FileThing.class)
@RooWebJson(jsonObject = FileThing.class)
public class FileThingController {
}
