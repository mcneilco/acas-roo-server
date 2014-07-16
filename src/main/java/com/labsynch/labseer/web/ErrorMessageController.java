package com.labsynch.labseer.web;

import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.exceptions.ErrorMessage;

@RooWebJson(jsonObject = ErrorMessage.class)
@Controller
@RequestMapping("/errormessages")
public class ErrorMessageController {
}
