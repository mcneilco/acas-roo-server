package com.labsynch.labseer.web;

import com.labsynch.labseer.dto.JSTreeNodeDTO;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = JSTreeNodeDTO.class)
@Controller
@RequestMapping("/jstreenoes")
public class JSTreeNodeDTOController {
}
