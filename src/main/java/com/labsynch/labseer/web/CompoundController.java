package com.labsynch.labseer.web;
import com.labsynch.labseer.domain.Compound;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;

@RequestMapping("/compounds")
@Controller
@RooWebScaffold(path = "compounds", formBackingObject = Compound.class)
@RooWebFinder
public class CompoundController {
}
