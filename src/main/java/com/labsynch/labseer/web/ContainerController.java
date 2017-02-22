package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.dto.IdCollectionDTO;
import com.labsynch.labseer.service.ContainerService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RooWebJson(jsonObject = Container.class)
@Controller
@RequestMapping("/containers")
@RooWebScaffold(path = "containers", formBackingObject = Container.class)
public class ContainerController {

//    @Autowired
//    private ContainerService containerService;
//
//    @SuppressWarnings("unused")
//    @Autowired
//    private PropertiesUtilService propertiesUtilService;
//
//    @Transactional
//    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
//        Container container = Container.findContainer(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (container == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(container.toJson(), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/stub/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> showJsonStub(@PathVariable("id") Long id) {
//        Container container = Container.findContainer(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (container == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(container.toJsonStub(), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<Container> containers = Container.findAllContainers();
//        return new ResponseEntity<String>(Container.toJsonArray(containers), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/stub/jsonArray", method = RequestMethod.GET, headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> listJsonStubs() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<Container> containers = Container.findAllContainers();
//        return new ResponseEntity<String>(Container.toJsonArrayStub(containers), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/findByIdsDTO/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> getContainersFromIDsTwo(@RequestBody String json) {
//        Collection<IdCollectionDTO> idCollections = IdCollectionDTO.fromJsonArrayToIdCollectioes(json);
//        Collection<Container> foundContainers = new ArrayList<Container>();
//        for (IdCollectionDTO idCollection : idCollections) {
//            Container container = Container.findContainer(idCollection.getId());
//            foundContainers.add(container);
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(Container.toJsonArray(foundContainers), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/findByIds/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> getContainersFromIDs(@RequestBody String json) {
//        Collection<Container> containers = Container.fromJsonArrayToContainers(json);
//        Collection<Container> foundContainers = new ArrayList<Container>();
//        for (Container container : containers) {
//            foundContainers.add(Container.findContainer(container.getId()));
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(Container.toJsonArray(foundContainers), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/findByIds/jsonArrayStub", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> getContainerStubsFromIDs(@RequestBody String json) {
//        Collection<IdCollectionDTO> idCollections = IdCollectionDTO.fromJsonArrayToIdCollectioes(json);
//        Collection<Container> foundContainers = new ArrayList<Container>();
//        for (IdCollectionDTO idCollection : idCollections) {
//            Container container = Container.findContainer(idCollection.getId());
//            foundContainers.add(container);
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(Container.toJsonArrayStub(foundContainers), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/findByIds/states/jsonArrayStub", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> getContainerStateStubsFromIDs(@RequestBody String json) {
//        Collection<IdCollectionDTO> idCollections = IdCollectionDTO.fromJsonArrayToIdCollectioes(json);
//        Collection<Container> foundContainers = new ArrayList<Container>();
//        for (IdCollectionDTO idCollection : idCollections) {
//            Container container = Container.findContainer(idCollection.getId());
//            foundContainers.add(container);
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(Container.toJsonArrayStatesStub(foundContainers), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/findIdsByLabels/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> getIdsFromJsonLabels(@RequestBody String json) {
//        Collection<ContainerLabel> labels = ContainerLabel.fromJsonArrayToContainerLabels(json);
//        Collection<IdCollectionDTO> foundContainers = new ArrayList<IdCollectionDTO>();
//        for (ContainerLabel label : labels) {
//            List<Container> containers = Container.findContainerByContainerLabel(label.getLabelText());
//            for (Container query : containers) {
//                IdCollectionDTO queryID = new IdCollectionDTO();
//                queryID.setId(query.getId());
//                foundContainers.add(queryID);
//            }
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(IdCollectionDTO.toJsonArray(foundContainers), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/findByLabels/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> getFromJsonLabels(@RequestBody String json) {
//        Collection<ContainerLabel> labels = ContainerLabel.fromJsonArrayToContainerLabels(json);
//        Collection<Container> foundContainers = new ArrayList<Container>();
//        for (ContainerLabel label : labels) {
//            List<Container> containers = Container.findContainerByContainerLabel(label.getLabelText());
//            for (Container query : containers) {
//                foundContainers.add(query);
//            }
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(Container.toJsonArray(foundContainers), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/findByLabels/list", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> getFromLabels(@RequestBody String labelList) {
//        Collection<Container> foundContainers = new ArrayList<Container>();
//        String[] labels = labelList.split(",");
//        for (String label : labels) {
//            List<Container> containers = Container.findContainerByContainerLabel(label);
//            for (Container query : containers) {
//                foundContainers.add(query);
//            }
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(Container.toJsonArray(foundContainers), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
//        Container container = containerService.saveLsContainer(json);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(container.toJson(), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/jsonFile", method = RequestMethod.POST, headers = "Accept=text/plain")
//    public ResponseEntity<java.lang.String> createFromJsonFile(@RequestBody String jsonFile) {
//        Collection<Container> containers = containerService.saveLsContainersFile(jsonFile);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(Container.toJsonArray(containers), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
//        Collection<Container> containers = containerService.saveLsContainers(json);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(Container.toJsonArray(containers), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/LsContainer", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createLSContainerFromJson(@RequestBody String json) {
//        Container container = Container.fromJsonToContainer(json);
//        container = containerService.saveLsContainer(container);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(container.toJson(), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/LsContainer/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createLSContainersFromJsonArray(@RequestBody String json) {
//        Collection<Container> savedContainers = new ArrayList<Container>();
//        BufferedReader br = null;
//        StringReader sr = null;
//        sr = new StringReader(json);
//        br = new BufferedReader(sr);
//        for (Container container : Container.fromJsonArrayToContainers(br)) {
//            savedContainers.add(containerService.saveLsContainer(container));
//        }
//        IOUtils.closeQuietly(sr);
//        IOUtils.closeQuietly(br);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(Container.toJsonArray(savedContainers), headers, HttpStatus.CREATED);
//    }
//
//    @Transactional
//    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        Container container = containerService.updateContainer(Container.fromJsonToContainer(json));
//        if (container.getId() == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(container.toJson(), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        Collection<Container> containers = Container.fromJsonArrayToContainers(json);
//        Collection<Container> updatedContainers = new ArrayList<Container>();
//        for (Container container : containers) {
//            updatedContainers.add(containerService.updateContainer(container));
//        }
//        return new ResponseEntity<String>(Container.toJsonArray(updatedContainers), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
//        Container container = Container.findContainer(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (container == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        container.remove();
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(params = "FindByContainerLabelList", method = RequestMethod.GET, headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> jsonFindContainersByContainerNameEqualsGet(@RequestParam("containerNamesList") String containerNamesList) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>(Container.toJsonArray(Container.findContainersByContainerLabels(containerNamesList)), headers, HttpStatus.OK);
//    }
//
//    @Transactional
//    @RequestMapping(params = "FindByContainerLabel", method = RequestMethod.GET, headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> jsonFindContainersByNameGet(@RequestParam("labelText") String labelText) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>(Container.toJsonArray(Container.findContainerByContainerLabel(labelText)), headers, HttpStatus.OK);
//    }
//
//    void populateEditForm(Model uiModel, Container container) {
//        uiModel.addAttribute("container", container);
//        addDateTimeFormatPatterns(uiModel);
//    }
}