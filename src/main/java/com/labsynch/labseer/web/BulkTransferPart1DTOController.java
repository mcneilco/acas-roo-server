package com.labsynch.labseer.web;

import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.dto.BulkTransferPart1DTO;

@RooWebJson(jsonObject = BulkTransferPart1DTO.class)
@Controller
@RequestMapping("/bulktransferpart1dto")
public class BulkTransferPart1DTOController {
}
