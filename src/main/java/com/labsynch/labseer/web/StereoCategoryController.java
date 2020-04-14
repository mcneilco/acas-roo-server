package com.labsynch.labseer.web;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.StereoCategory;
import com.labsynch.labseer.dto.configuration.MainConfigDTO;
import com.labsynch.labseer.utils.Configuration;

@RooWebScaffold(path = "stereocategorys", formBackingObject = StereoCategory.class)
@RequestMapping({ "/stereoCategorys", "/stereocategorys", "/stereoCategories" })
@Transactional
@Controller

@RooWebFinder
public class StereoCategoryController {


	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        StereoCategory stereocategory = StereoCategory.findStereoCategory(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        if (stereocategory == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(stereocategory.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
 
        if (mainConfig.getServerSettings().isOrderSelectLists()){
            return new ResponseEntity<String>(StereoCategory.toJsonArray(StereoCategory.findAllStereoCategorys("name","ASC")), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(StereoCategory.toJsonArray(StereoCategory.findAllStereoCategorys()), headers, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        StereoCategory.fromJsonToStereoCategory(json).persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (StereoCategory stereoCategory : StereoCategory.fromJsonArrayToStereoCategorys(json)) {
            stereoCategory.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        if (StereoCategory.fromJsonToStereoCategory(json).merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        for (StereoCategory stereoCategory : StereoCategory.fromJsonArrayToStereoCategorys(json)) {
            if (stereoCategory.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        StereoCategory stereocategory = StereoCategory.findStereoCategory(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        if (stereocategory == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        stereocategory.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getOptions() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text, text/html");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Max-Age", "86400");
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
}
