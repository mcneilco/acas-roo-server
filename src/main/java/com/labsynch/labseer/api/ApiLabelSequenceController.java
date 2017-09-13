package com.labsynch.labseer.api;

import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.LabelSequenceDTO;
import com.labsynch.labseer.service.AutoLabelService;
import com.labsynch.labseer.service.LabelSequenceService;

import java.util.Date;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

//@RooWebJson(jsonObject = LabelSequence.class)
@Controller
@RequestMapping("/api/v1/labelsequences")
//@RooWebFinder
public class ApiLabelSequenceController {

    private static final Logger logger = LoggerFactory.getLogger(ApiLabelSequenceController.class);

    @Autowired
    private AutoLabelService autoLabelService;
    
    @Autowired
    private LabelSequenceService labelSequenceService;

    //copied only the custom methods from the LabelSequenceController.java class
    @RequestMapping(value = "/getNextLabelSequences", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateLabelSequence(@RequestBody LabelSequenceDTO lsDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        logger.info("incoming label seq: " + lsDTO.toJson());
        List<LabelSequence> labelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(lsDTO.getThingTypeAndKind(), lsDTO.getLabelTypeAndKind()).getResultList();
        if (labelSequences.size() != 1) {
            logger.info("did not find the label seq!!! ");
            return new ResponseEntity<String>(headers, HttpStatus.NOT_ACCEPTABLE);
        }
        LabelSequence labelSequence = LabelSequence.findLabelSequence(labelSequences.get(0).getId());
        labelSequence.setLatestNumber(labelSequence.getLatestNumber() + lsDTO.getNumberOfLabels());
        labelSequence.setModifiedDate(new Date());
        labelSequence.merge();
        return new ResponseEntity<String>(labelSequence.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/getLabels", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getAutoLabels(@RequestBody LabelSequenceDTO lsDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        List<AutoLabelDTO> autoLabels;
        try {
            autoLabels = autoLabelService.getAutoLabels(lsDTO);
        } catch (NonUniqueResultException e) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<String>(AutoLabelDTO.toJsonArray(autoLabels), headers, HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<LabelSequence> result = LabelSequence.findAllLabelSequences();
        return new ResponseEntity<String>(LabelSequence.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/getNextLabelSequences", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindLabelSequencesByThingAndLabel(@RequestParam("thingTypeAndKind") String thingTypeAndKind, @RequestParam("labelTypeAndKind") String labelTypeAndKind, @RequestParam("numberOfLabels") long numberOfLabels) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<LabelSequence> labelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList();
        if (labelSequences.size() != 1) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_ACCEPTABLE);
        }
        LabelSequence labelSequence = LabelSequence.findLabelSequence(labelSequences.get(0).getId());
        labelSequence.setLatestNumber(labelSequence.getLatestNumber() + numberOfLabels);
        labelSequence.merge();
        return new ResponseEntity<String>(labelSequence.toJson(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/getAuthorizedLabelSequences", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<java.lang.String> getAuthorizedLabelSequences(@RequestParam(value="userName", required=true) String userName,
    		@RequestParam(value="thingTypeAndKind", required=false) String thingTypeAndKind, 
    		@RequestParam(value="labelTypeAndKind", required=false) String labelTypeAndKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<LabelSequence> labelSequences = labelSequenceService.getAuthorizedLabelSequences(userName, thingTypeAndKind, labelTypeAndKind);
        return new ResponseEntity<String>(LabelSequence.toJsonArray(labelSequences), headers, HttpStatus.OK);
    }
}

