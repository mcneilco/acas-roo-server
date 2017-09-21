package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsSeqItxSubjCntr;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/lsseqitxsubjcntrs")
@Controller
@RooWebScaffold(path = "lsseqitxsubjcntrs", formBackingObject = LsSeqItxSubjCntr.class)
public class LsSeqItxSubjCntrController {
}
