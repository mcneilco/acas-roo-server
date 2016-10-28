package com.labsynch.labseer.web;

import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.dto.SubjectValueDTO;

@RooWebJson(jsonObject = SubjectValueDTO.class)
@Controller
@RequestMapping("/subjectvaluedto")
public class SubjectValueDTOController {
}
