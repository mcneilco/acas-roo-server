package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.DDictKind;
import com.labsynch.labseer.domain.DDictType;
import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.service.AutoLabelService;
import com.labsynch.labseer.service.DataDictionaryService;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

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
