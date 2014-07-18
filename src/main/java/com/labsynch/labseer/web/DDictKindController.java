package com.labsynch.labseer.web;

import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.DDictKind;
import com.labsynch.labseer.domain.DDictType;

@RequestMapping("/ddictkinds")
@Controller
@RooWebScaffold(path = "ddictkinds", formBackingObject = DDictKind.class)
@RooWebFinder
@RooWebJson(jsonObject = DDictKind.class)
public class DDictKindController {

    void populateEditForm(Model uiModel, DDictKind DDictKind_) {
        uiModel.addAttribute("DDictKind_", DDictKind_);
        uiModel.addAttribute("ddicttypes", DDictType.findAllDDictTypes());
    }
}
