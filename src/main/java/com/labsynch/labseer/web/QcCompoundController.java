package com.labsynch.labseer.web;
import com.labsynch.labseer.domain.QcCompound;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;

@RequestMapping("/qccompounds")
@Controller
@RooWebScaffold(path = "qccompounds", formBackingObject = QcCompound.class)
@RooWebFinder
public class QcCompoundController {
}
