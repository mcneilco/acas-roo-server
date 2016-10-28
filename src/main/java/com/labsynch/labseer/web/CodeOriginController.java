package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.CodeOrigin;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/codeorigins")
@Controller
@RooWebScaffold(path = "codeorigins", formBackingObject = CodeOrigin.class)
public class CodeOriginController {
}
