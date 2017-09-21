package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LabelSequenceRole;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/labelsequenceroles")
@Controller
@RooWebScaffold(path = "labelsequenceroles", formBackingObject = LabelSequenceRole.class)
public class LabelSequenceRoleController {
}
