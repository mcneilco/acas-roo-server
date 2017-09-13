package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsSeqContainer;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/lsseqcontainers")
@Controller
@RooWebScaffold(path = "lsseqcontainers", formBackingObject = LsSeqContainer.class)
public class LsSeqContainerController {
}
