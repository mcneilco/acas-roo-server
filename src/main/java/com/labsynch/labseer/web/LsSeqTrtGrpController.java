package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsSeqTrtGrp;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/lsseqtrtgrps")
@Controller
@RooWebScaffold(path = "lsseqtrtgrps", formBackingObject = LsSeqTrtGrp.class)
public class LsSeqTrtGrpController {
}
