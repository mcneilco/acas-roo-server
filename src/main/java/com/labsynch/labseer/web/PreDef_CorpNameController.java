package com.labsynch.labseer.web;
import javax.servlet.http.HttpServletRequest;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.PreDef_CorpName;

@RequestMapping("/predef_corpnames")
@Controller
@RooWebScaffold(path = "predef_corpnames", formBackingObject = PreDef_CorpName.class)

public class PreDef_CorpNameController {

}
