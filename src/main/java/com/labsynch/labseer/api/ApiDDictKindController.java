package com.labsynch.labseer.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.DDictKind;

@Transactional
@RequestMapping("api/v1/ddictkinds")
@Controller
@RooWebFinder
@RooWebJson(jsonObject = DDictKind.class)

public class ApiDDictKindController {

	private static final Logger logger = LoggerFactory.getLogger(ApiDDictKindController.class);


}
