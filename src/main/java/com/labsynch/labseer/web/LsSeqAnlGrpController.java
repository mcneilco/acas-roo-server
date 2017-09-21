package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsSeqAnlGrp;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/lsseqanlgrps")
@Controller
@RooWebScaffold(path = "lsseqanlgrps", formBackingObject = LsSeqAnlGrp.class)
public class LsSeqAnlGrpController {
}
