package com.labsynch.labseer.web;

import com.labsynch.labseer.dto.ValueTypeKindDTO;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = ValueTypeKindDTO.class)
@Controller
@RequestMapping("/valuetypekinddto")
public class ValueTypeKindDTOController {
}
