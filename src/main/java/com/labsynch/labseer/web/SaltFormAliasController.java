package com.labsynch.labseer.web;
import com.labsynch.labseer.domain.SaltFormAlias;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;

@RequestMapping("/saltformaliases")
@Controller
@RooWebScaffold(path = "saltformaliases", formBackingObject = SaltFormAlias.class)
@RooWebFinder
public class SaltFormAliasController {
}
