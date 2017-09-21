package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsSeqSubject;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/lsseqsubjects")
@Controller
@RooWebScaffold(path = "lsseqsubjects", formBackingObject = LsSeqSubject.class)
public class LsSeqSubjectController {
}
