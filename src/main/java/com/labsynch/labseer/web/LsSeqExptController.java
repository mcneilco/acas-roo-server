package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsSeqExpt;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/lsseqexpts")
@Controller
@RooWebScaffold(path = "lsseqexpts", formBackingObject = LsSeqExpt.class)
public class LsSeqExptController {
}
