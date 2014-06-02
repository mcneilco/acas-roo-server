package com.labsynch.labseer.web;

import com.labsynch.labseer.dto.SubjectMiniDTO;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = SubjectMiniDTO.class)
@Controller
@RequestMapping("/subjectminidto")
public class SubjectMiniDTOController {
}
