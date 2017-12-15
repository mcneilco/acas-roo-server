package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsSeqItxExperimentExperiment;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/lsseqitxexperimentexperiments")
@Controller
@RooWebScaffold(path = "lsseqitxexperimentexperiments", formBackingObject = LsSeqItxExperimentExperiment.class)
public class LsSeqItxExperimentExperimentController {
}
