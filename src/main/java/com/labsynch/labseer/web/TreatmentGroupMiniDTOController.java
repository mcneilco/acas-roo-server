package com.labsynch.labseer.web;

import com.labsynch.labseer.dto.TreatmentGroupMiniDTO;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = TreatmentGroupMiniDTO.class)
@Controller
@RequestMapping("/treatmentgroes")
public class TreatmentGroupMiniDTOController {
}
