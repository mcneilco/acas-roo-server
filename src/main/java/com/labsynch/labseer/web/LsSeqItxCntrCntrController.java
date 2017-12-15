package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsSeqItxCntrCntr;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/lsseqitxcntrcntrs")
@Controller
@RooWebScaffold(path = "lsseqitxcntrcntrs", formBackingObject = LsSeqItxCntrCntr.class)
public class LsSeqItxCntrCntrController {
}
