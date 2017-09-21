package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.CronJob;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/cronjobs")
@Controller
@RooWebScaffold(path = "cronjobs", formBackingObject = CronJob.class)
public class CronJobController {
}
