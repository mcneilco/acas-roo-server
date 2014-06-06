package com.labsynch.labseer.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.DDictType;

@Transactional
@RequestMapping("api/v1/ddicttypes")
@Controller
@RooWebFinder
@RooWebJson(jsonObject = DDictType.class)

public class ApiDDictTypeController {

	private static final Logger logger = LoggerFactory.getLogger(ApiDDictTypeController.class);


}
