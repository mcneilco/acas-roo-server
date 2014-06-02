package com.labsynch.labseer.web;

import com.labsynch.labseer.dto.AnalysisGroupValueBaseDTO;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = AnalysisGroupValueBaseDTO.class)
@Controller
@RequestMapping("/analysisgroes")
public class AnalysisGroupValueBaseDTOController {
}
