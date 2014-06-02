package com.labsynch.labseer.web;

import com.labsynch.labseer.dto.ExperimentFilterDTO;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = ExperimentFilterDTO.class)
@Controller
@RequestMapping("/experimentfilterdto")
public class ExperimentFilterDTOController {
}
