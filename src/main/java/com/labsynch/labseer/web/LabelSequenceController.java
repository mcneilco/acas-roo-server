package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.LabelSequenceDTO;
import com.labsynch.labseer.service.AutoLabelService;
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
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = LabelSequence.class)
@Controller
@RequestMapping("/labelsequences")
@RooWebScaffold(path = "labelsequences", formBackingObject = LabelSequence.class)
@RooWebFinder
public class LabelSequenceController {

    private static final Logger logger = LoggerFactory.getLogger(LabelSequenceController.class);

    @Autowired
    private AutoLabelService autoLabelService;

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        LabelSequence labelSequence = LabelSequence.findLabelSequence(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (labelSequence == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(labelSequence.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<LabelSequence> result = LabelSequence.findAllLabelSequences();
        return new ResponseEntity<String>(LabelSequence.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        LabelSequence labelSequence = LabelSequence.fromJsonToLabelSequence(json);
        labelSequence.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        for (LabelSequence labelSequence : LabelSequence.fromJsonArrayToLabelSequences(json)) {
            labelSequence.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        LabelSequence labelSequence = LabelSequence.fromJsonToLabelSequence(json);
        if (labelSequence.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (LabelSequence labelSequence : LabelSequence.fromJsonArrayToLabelSequences(json)) {
            if (labelSequence.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        LabelSequence labelSequence = LabelSequence.findLabelSequence(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (labelSequence == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        labelSequence.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/getNextLabelSequences", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateLabelSequence(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        LabelSequenceDTO lsDTO = LabelSequenceDTO.fromJsonToLabelSequenceDTO(json);
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
    public ResponseEntity<java.lang.String> getAutoLabels(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        List<AutoLabelDTO> autoLabels;
        try {
            autoLabels = autoLabelService.getAutoLabels(json);
        } catch (NonUniqueResultException e) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<String>(AutoLabelDTO.toJsonArray(autoLabels), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "getNextLabelSequences", headers = "Accept=application/json")
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
}
