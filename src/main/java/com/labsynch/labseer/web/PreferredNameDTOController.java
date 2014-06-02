package com.labsynch.labseer.web;

import com.labsynch.labseer.dto.PreferredNameDTO;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = PreferredNameDTO.class)
@Controller
@RequestMapping("/preferrednamedto")
public class PreferredNameDTOController {
}
