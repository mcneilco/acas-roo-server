package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.AuthorState;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/authorstates")
@Controller
@RooWebScaffold(path = "authorstates", formBackingObject = AuthorState.class)
public class AuthorStateController {
}
