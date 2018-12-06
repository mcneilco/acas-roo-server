package com.labsynch.labseer.web;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.labsynch.labseer.service.SaltLoader;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;

@RequestMapping("/saltloaders")
@Controller
@RooWebScaffold(path = "saltloaders", formBackingObject = SaltLoader.class)
public class SaltLoaderController {
}
