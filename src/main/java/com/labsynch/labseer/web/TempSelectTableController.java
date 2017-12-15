package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.TempSelectTable;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/tempselecttables")
@Controller
@RooWebScaffold(path = "tempselecttables", formBackingObject = TempSelectTable.class)
public class TempSelectTableController {
}
