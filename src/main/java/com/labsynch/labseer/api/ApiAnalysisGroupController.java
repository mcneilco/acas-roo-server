package com.labsynch.labseer.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.service.ExperimentService;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Controller
@RequestMapping("api/v1/analysisgroups")
//@RooWebScaffold(path = "analysisgroups", formBackingObject = AnalysisGroup.class)
@RooWebFinder
@Transactional
@RooWebJson(jsonObject = AnalysisGroup.class)
public class ApiAnalysisGroupController {

	private static final Logger logger = LoggerFactory.getLogger(ApiAnalysisGroupController.class);

	@Autowired
	private ExperimentService experimentService;

	@Autowired
	private PropertiesUtilService propertiesUtilService;


}
