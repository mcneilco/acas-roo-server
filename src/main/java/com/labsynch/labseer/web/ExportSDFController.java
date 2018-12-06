package com.labsynch.labseer.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.labsynch.labseer.service.StructureImageService;

@RequestMapping("/exportSDF")
@Controller
public class ExportSDFController {

	private static final Logger logger = LoggerFactory.getLogger(FileSaveController.class);

	@Autowired
	private StructureImageService structureImageService;

	@RequestMapping
	public void get(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<String> getSDfile( @RequestParam("mols") String mols,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model) {

		String output = structureImageService.convertMolfilesToSDFile(mols);

		logger.debug("output sdfile: " + output);

		HttpHeaders headers= new HttpHeaders();
		headers.setContentType(MediaType.TEXT_HTML);
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Headers", "Content-Type");
		return new ResponseEntity<String>(output, headers, HttpStatus.OK);
	}

//	@RequestMapping(method = RequestMethod.POST, value = "{id}")
//	public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
//	}
//
//	@RequestMapping
//	public String index() {
//		return "exportsdf/index";
//	}
	
}
