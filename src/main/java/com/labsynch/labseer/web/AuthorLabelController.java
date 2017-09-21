package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.AuthorLabel;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/authorlabels")
@Controller
@RooWebScaffold(path = "authorlabels", formBackingObject = AuthorLabel.class)
public class AuthorLabelController {
}
