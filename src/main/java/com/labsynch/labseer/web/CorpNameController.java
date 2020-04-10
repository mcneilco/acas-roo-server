package com.labsynch.labseer.web;
import javax.servlet.http.HttpServletRequest;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.CorpName;

@RooWebScaffold(path = "corpnames", formBackingObject = CorpName.class)
@RequestMapping("/corpnames")
@Controller

public class CorpNameController {
	

}
