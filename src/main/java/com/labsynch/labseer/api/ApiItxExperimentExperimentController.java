package com.labsynch.labseer.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ItxExperimentExperiment;
import com.labsynch.labseer.service.ItxExperimentExperimentService;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/v1/itxexperimentexperiments")
public class ApiItxExperimentExperimentController {

    private static final Logger logger = LoggerFactory.getLogger(ApiItxExperimentExperimentController.class);

    @Autowired
    private ItxExperimentExperimentService itxExperimentExperimentService;

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @Transactional
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        ItxExperimentExperiment itxExperimentExperiment = ItxExperimentExperiment.findItxExperimentExperiment(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (itxExperimentExperiment == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(itxExperimentExperiment.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ItxExperimentExperiment> result = ItxExperimentExperiment.findAllItxExperimentExperiments();
        return new ResponseEntity<String>(ItxExperimentExperiment.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ItxExperimentExperiment itxExperimentExperiment = ItxExperimentExperiment
                    .fromJsonToItxExperimentExperiment(json);
            itxExperimentExperiment = itxExperimentExperimentService.saveLsItxExperiment(itxExperimentExperiment);
            return new ResponseEntity<String>(itxExperimentExperiment.toJson(), headers, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Uncaught exception in createFromJson", e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        Collection<ItxExperimentExperiment> itxExperimentExperiments = ItxExperimentExperiment
                .fromJsonArrayToItxExperimentExperiments(json);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            Collection<ItxExperimentExperiment> savedItxExperimentExperiments = itxExperimentExperimentService
                    .saveLsItxExperiments(itxExperimentExperiments);
            return new ResponseEntity<String>(ItxExperimentExperiment.toJsonArray(savedItxExperimentExperiments),
                    headers, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Uncaught exception in createFromJsonArray", e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ItxExperimentExperiment itxExperimentExperiment = ItxExperimentExperiment
                .fromJsonToItxExperimentExperiment(json);
        ItxExperimentExperiment updatedItxExperimentExperiment = null;
        try {
            updatedItxExperimentExperiment = itxExperimentExperimentService
                    .updateItxExperimentExperiment(itxExperimentExperiment);
            return new ResponseEntity<String>(updatedItxExperimentExperiment.toJson(), headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Caught error updating ItxExperimentExperiment from JSON", e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ItxExperimentExperiment> updatedItxExperimentExperiments = new HashSet<ItxExperimentExperiment>();
        try {
            for (ItxExperimentExperiment itxExperimentExperiment : ItxExperimentExperiment
                    .fromJsonArrayToItxExperimentExperiments(json)) {
                ItxExperimentExperiment updatedItxExperimentExperiment = itxExperimentExperimentService
                        .updateItxExperimentExperiment(itxExperimentExperiment);
                updatedItxExperimentExperiments.add(updatedItxExperimentExperiment);
            }
            return new ResponseEntity<String>(ItxExperimentExperiment.toJsonArray(updatedItxExperimentExperiments),
                    headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Caught error updating ItxExperimentExperiments from JSON", e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @RequestMapping(value = "/findByFirstExperiment/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> findItxExperimentExperimentsByFirstExperiment(
            @PathVariable("id") Long firstExperimentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Experiment firstExperiment;
        try {
            firstExperiment = Experiment.findExperiment(firstExperimentId);
        } catch (Exception e) {
            logger.error("Error in findItxExperimentExperimentsByFirstExperiment: firstExperiment "
                    + firstExperimentId.toString() + " not found");
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        Collection<ItxExperimentExperiment> itxExperimentExperiments = ItxExperimentExperiment
                .findItxExperimentExperimentsByFirstExperiment(firstExperiment).getResultList();
        for (ItxExperimentExperiment itx : itxExperimentExperiments) {
            logger.debug(itx.getCodeName() + " " + itx.getId().toString());
            logger.debug(itx.toJson());
        }
        return new ResponseEntity<String>(ItxExperimentExperiment.toJsonArray(itxExperimentExperiments), headers,
                HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/findBySecondExperiment/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> findItxExperimentExperimentsBySecondExperiment(
            @PathVariable("id") Long secondExperimentId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Experiment secondExperiment;
        try {
            secondExperiment = Experiment.findExperiment(secondExperimentId);
        } catch (Exception e) {
            logger.error("Error in findItxExperimentExperimentsBySecondExperiment: secondExperiment "
                    + secondExperimentId.toString() + " not found");
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        Collection<ItxExperimentExperiment> itxExperimentExperiments = ItxExperimentExperiment
                .findItxExperimentExperimentsBySecondExperiment(secondExperiment).getResultList();
        return new ResponseEntity<String>(ItxExperimentExperiment.toJsonArray(itxExperimentExperiments), headers,
                HttpStatus.OK);
    }
}
