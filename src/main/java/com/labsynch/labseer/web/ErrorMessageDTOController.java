package com.labsynch.labseer.web;

import com.labsynch.labseer.dto.ErrorMessageDTO;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebJson(jsonObject = ErrorMessageDTO.class)
@Controller
@RequestMapping("/erroes")
public class ErrorMessageDTOController {
}
