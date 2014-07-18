package com.labsynch.labseer.web;

import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.DDictKind;
import com.labsynch.labseer.domain.DDictType;
import com.labsynch.labseer.domain.DDictValue;

@RequestMapping("/ddictvalues")
@Controller
@RooWebScaffold(path = "ddictvalues", formBackingObject = DDictValue.class)
@RooWebFinder
@RooWebJson(jsonObject = DDictValue.class)
public class DDictValueController {

    void populateEditForm(Model uiModel, DDictValue DDictValue_) {
        uiModel.addAttribute("DDictValue_", DDictValue_);
        uiModel.addAttribute("ddicttypes", DDictType.findAllDDictTypes());
        uiModel.addAttribute("ddictkinds", DDictKind.findAllDDictKinds());
    }
}
