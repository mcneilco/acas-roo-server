package com.labsynch.labseer.web;

import com.labsynch.labseer.dto.BatchCodeDTO;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = BatchCodeDTO.class)
@Controller
@RequestMapping("/batchcoes")
public class BatchCodeDTOController {
}
