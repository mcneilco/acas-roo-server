package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsSeqItxProtocolProtocol;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/lsseqitxprotocolprotocols")
@Controller
@RooWebScaffold(path = "lsseqitxprotocolprotocols", formBackingObject = LsSeqItxProtocolProtocol.class)
public class LsSeqItxProtocolProtocolController {
}
