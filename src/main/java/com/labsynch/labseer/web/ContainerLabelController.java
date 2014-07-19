package com.labsynch.labseer.web;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.IOUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.utils.PropertiesUtilService;

@RooWebJson(jsonObject = ContainerLabel.class)
@Controller
@RequestMapping("/containerlabels")
@RooWebScaffold(path = "containerlabels", formBackingObject = ContainerLabel.class)
@RooWebFinder
public class ContainerLabelController {

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    private static final Logger logger = LoggerFactory.getLogger(ContainerLabelController.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        ContainerLabel containerLabel = ContainerLabel.findContainerLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (containerLabel == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(containerLabel.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ContainerLabel> result = ContainerLabel.findAllContainerLabels();
        return new ResponseEntity<String>(ContainerLabel.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        ContainerLabel containerLabel = ContainerLabel.fromJsonToContainerLabel(json);
        containerLabel.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(containerLabel.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        StringReader sr = new StringReader(json);
        BufferedReader br = new BufferedReader(sr);
        for (ContainerLabel containerLabel : ContainerLabel.fromJsonArrayToContainerLabels(br)) {
            containerLabel.persist();
            if (i % batchSize == 0) {
                containerLabel.flush();
                containerLabel.clear();
            }
            i++;
        }
        IOUtils.closeQuietly(sr);
        IOUtils.closeQuietly(br);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>("[]", headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ContainerLabel containerLabel = ContainerLabel.fromJsonToContainerLabel(json);
        logger.debug("attempting to update the container label " + containerLabel.getId());
        ContainerLabel updatedContainerLabel = ContainerLabel.update(containerLabel);
        if (updatedContainerLabel.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ContainerLabel.findContainerLabel(updatedContainerLabel.getId()).toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ContainerLabel> containerLabels = ContainerLabel.fromJsonArrayToContainerLabels(json);
        for (ContainerLabel containerLabel : containerLabels) {
            ContainerLabel updatedContainerLabel = ContainerLabel.update(containerLabel);
            if (updatedContainerLabel.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>("[]", headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        ContainerLabel containerLabel = ContainerLabel.findContainerLabel(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (containerLabel == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        containerLabel.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    void populateEditForm(Model uiModel, ContainerLabel containerLabel) {
        uiModel.addAttribute("containerLabel", containerLabel);
        addDateTimeFormatPatterns(uiModel);
        List<Container> containers = Container.findContainerByContainerLabel(containerLabel.getLabelText());
        Collection<Container> containerSet = new HashSet<Container>();
        containerSet.addAll(containers);
        uiModel.addAttribute("containers", containerSet);
    }
}
