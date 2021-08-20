package com.labsynch.labseer.web;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.FileType;

import com.labsynch.labseer.utils.PropertiesUtilService;
import org.springframework.beans.factory.annotation.Autowired;

@RequestMapping({"/filetypes", "/fileTypes"})
@Controller

@RooWebJson(jsonObject = FileType.class)
@RooWebScaffold(path = "filetypes", formBackingObject = FileType.class)
@RooWebFinder
public class FileTypeController {
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache

		if (propertiesUtilService.getOrderSelectLists()){
	        return new ResponseEntity<String>(FileType.toJsonArray(FileType.findAllFileTypes("name", "ASC")), headers, HttpStatus.OK);
		} else {
	        return new ResponseEntity<String>(FileType.toJsonArray(FileType.findAllFileTypes()), headers, HttpStatus.OK);
		}
		
    }
	
	@RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getIsotopeOptions() {
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
        return new ResponseEntity<String>(headers, HttpStatus.OK);
	}

    
}
