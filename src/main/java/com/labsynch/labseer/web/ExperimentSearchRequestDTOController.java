package com.labsynch.labseer.web;

import com.labsynch.labseer.dto.ExperimentSearchRequestDTO;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = ExperimentSearchRequestDTO.class)
@Controller
@RequestMapping("/experimentsearchrequestdto")
public class ExperimentSearchRequestDTOController {
}
