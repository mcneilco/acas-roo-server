package com.labsynch.labseer.web;

import com.labsynch.labseer.dto.ExperimentFilterSearchDTO;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = ExperimentFilterSearchDTO.class)
@Controller
@RequestMapping("/experimentfiltersearchdto")
public class ExperimentFilterSearchDTOController {
}
