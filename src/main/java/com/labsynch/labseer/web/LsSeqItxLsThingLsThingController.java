package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsSeqItxLsThingLsThing;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/lsseqitxlsthinglsthings")
@Controller
@RooWebScaffold(path = "lsseqitxlsthinglsthings", formBackingObject = LsSeqItxLsThingLsThing.class)
public class LsSeqItxLsThingLsThingController {
}
