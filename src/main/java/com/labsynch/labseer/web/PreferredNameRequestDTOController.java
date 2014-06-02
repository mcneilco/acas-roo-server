package com.labsynch.labseer.web;

import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = PreferredNameRequestDTO.class)
@Controller
@RequestMapping("/preferrednamerequestdto")
public class PreferredNameRequestDTOController {
}
