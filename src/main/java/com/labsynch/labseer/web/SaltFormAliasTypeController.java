package com.labsynch.labseer.web;
import com.labsynch.labseer.domain.SaltFormAliasType;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/saltformaliastypes")
@Controller
@RooWebScaffold(path = "saltformaliastypes", formBackingObject = SaltFormAliasType.class)
public class SaltFormAliasTypeController {
}
