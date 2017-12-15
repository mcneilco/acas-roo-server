package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.OperatorKind;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/operatorkinds")
@Controller
@RooWebScaffold(path = "operatorkinds", formBackingObject = OperatorKind.class)
@RooWebJson(jsonObject = OperatorKind.class)
public class OperatorKindController {
}
