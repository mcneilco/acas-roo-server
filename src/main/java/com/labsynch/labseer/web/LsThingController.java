package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.ErrorMessageDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.PreferredNameWithErrorDTO;
import com.labsynch.labseer.service.GeneThingService;
import com.labsynch.labseer.service.LsThingService;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = LsThing.class)
@Controller
@RequestMapping("/lsthings")
@RooWebScaffold(path = "lsthings", formBackingObject = LsThing.class)
@RooWebFinder
public class LsThingController {

//    private static final Logger logger = LoggerFactory.getLogger(LsThingController.class);
//
//    @Autowired
//    private LsThingService lsThingService;
//
//    @Autowired
//    private GeneThingService geneThingService;
//
//    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
//        LsThing lsThing = LsThing.findLsThing(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (lsThing == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(lsThing.toPrettyJsonStub(), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<LsThing> result = LsThing.findAllLsThings();
//        return new ResponseEntity<String>(LsThing.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
//        LsThing lsThing = LsThing.fromJsonToLsThing(json);
//        lsThing.persist();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/projects", method = RequestMethod.GET, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> getProjects() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>(lsThingService.getProjectCodes(), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/{thingType}/{thingKind}", method = RequestMethod.GET, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> getLsThingByTypeAndKindAll(@PathVariable("thingType") String thingType, 
//    		@PathVariable("thingKind") String thingKind, 
//    		@RequestParam(value = "labelText", required = false) String labelText) {
//        List<LsThing> results;
//        if (labelText == null || labelText.equalsIgnoreCase("")) {
//            results = LsThing.findLsThing(thingType, thingKind).getResultList();
//        } else {
//            logger.info("query for thing with labelText");
//            results = LsThing.findLsThingByLabelText(thingType, thingKind, labelText).getResultList();
//        }
//        logger.info("query labelText: " + labelText + " number of results: " + results.size());
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>(LsThing.toJsonArray(results), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/getLsThing/codeName", method = RequestMethod.GET, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> getLsThingCodeName(@RequestParam(value = "thingType", required = true) String thingType, @RequestParam(value = "thingKind", required = true) String thingKind, @RequestParam(value = "labelType", required = false) String labelType, @RequestParam(value = "labelKind", required = false) String labelKind, @RequestParam(value = "labelText", required = true) String labelText) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<LsThing> lsThings;
//        if (labelType != null && labelKind != null) {
//            lsThings = LsThing.findLsThingByLabelText(thingType, thingKind, labelType, labelKind, labelText).getResultList();
//        } else {
//            lsThings = LsThing.findLsThingByLabelText(thingType, thingKind, labelText).getResultList();
//        }
//        PreferredNameWithErrorDTO prefNameDTO = new PreferredNameWithErrorDTO();
//        prefNameDTO.setRequestName(labelText);
//        if (lsThings.size() == 1) {
//            prefNameDTO.setReferenceName(lsThings.get(0).getCodeName());
//            return new ResponseEntity<String>(prefNameDTO.toJson(), headers, HttpStatus.OK);
//        } else if (lsThings.size() > 1) {
//            ErrorMessageDTO error = new ErrorMessageDTO();
//            error.setErrorCode("MULTIPLE RESULTS");
//            error.setErrorMessage("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + labelText);
//            logger.error("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + labelText);
//            prefNameDTO.setErrorMesageDTO(error);
//            prefNameDTO.setReferenceName("");
//            return new ResponseEntity<String>(prefNameDTO.toJson(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
//        } else {
//            logger.info("Did not find a LS_THING WITH THE REQUESTED NAME: " + labelText);
//            prefNameDTO.setReferenceName("");
//            return new ResponseEntity<String>(prefNameDTO.toJson(), headers, HttpStatus.NO_CONTENT);
//        }
//    }
//
//    @RequestMapping(value = "/getLsThing", method = RequestMethod.GET, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> getLsThingByTypeAndKind(@RequestParam(value = "thingType", required = true) String thingType, @RequestParam(value = "thingKind", required = true) String thingKind, @RequestParam(value = "labelText", required = false) String labelText) {
//        List<LsThing> results;
//        if (labelText == null || labelText.equalsIgnoreCase("")) {
//            results = LsThing.findLsThing(thingType, thingKind).getResultList();
//        } else {
//            results = LsThing.findLsThingByLabelText(thingType, thingKind, labelText).getResultList();
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>(LsThing.toJsonArray(results), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/gene/v1/loadGeneEntities", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> loadGeneEntities(@RequestParam(value = "fileName", required = true) String fileName) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        logger.info("loading genes from tab delimited file: " + fileName);
//        try {
//            geneThingService.RegisterGeneThingsFromCSV(fileName);
//        } catch (IOException e) {
//            logger.error("IOException: " + e.toString());
//            return new ResponseEntity<String>("ERROR: IOError. Unable to load file. " + fileName, headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    
//    @RequestMapping(value = "/getCodeNameFromNameRequest", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> getCodeNameFromName(@RequestBody String json, @RequestParam(value = "thingType", required = true) String thingType, @RequestParam(value = "thingKind", required = true) String thingKind, @RequestParam(value = "labelType", required = false) String labelType, @RequestParam(value = "labelKind", required = false) String labelKind) {
//        logger.info("getCodeNameFromNameRequest incoming json: " + json);
//        PreferredNameResultsDTO results = lsThingService.getCodeNameFromName(thingType, thingKind, labelType, labelKind, json);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>(results.toJson(), headers, HttpStatus.OK);
//    }
//
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
//        for (LsThing lsThing : LsThing.fromJsonArrayToLsThings(json)) {
//            lsThing.persist();
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        LsThing lsThing = LsThing.fromJsonToLsThing(json);
//        if (lsThing.merge() == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        for (LsThing lsThing : LsThing.fromJsonArrayToLsThings(json)) {
//            if (lsThing.merge() == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
//        LsThing lsThing = LsThing.findLsThing(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (lsThing == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        lsThing.remove();
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    void populateEditForm(Model uiModel, LsThing lsThing) {
//        uiModel.addAttribute("lsThing", lsThing);
//        addDateTimeFormatPatterns(uiModel);
//        uiModel.addAttribute("lstags", lsThing.getLsTags());
//    }
}
