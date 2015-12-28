package com.labsynch.labseer.api;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.dto.CodeLabelDTO;
import com.labsynch.labseer.dto.ContainerLocationDTO;
import com.labsynch.labseer.dto.IdCollectionDTO;
import com.labsynch.labseer.dto.PlateWellDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.WellContentDTO;
import com.labsynch.labseer.service.ContainerService;
import com.labsynch.labseer.service.GeneThingService;
import com.labsynch.labseer.service.LsThingService;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Controller
@RequestMapping("api/v1/containers")
@Transactional
public class ApiContainerController {

	@Autowired
    private ContainerService containerService;

    @SuppressWarnings("unused")
    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @Transactional
    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        Container container = Container.findContainer(id);
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (container == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(container.toJson(), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/stub/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJsonStub(@PathVariable("id") Long id) {
        Container container = Container.findContainer(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (container == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(container.toJsonStub(), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Container> containers = Container.findAllContainers();
        return new ResponseEntity<String>(Container.toJsonArray(containers), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/stub/jsonArray", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJsonStubs() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Container> containers = Container.findAllContainers();
        return new ResponseEntity<String>(Container.toJsonArrayStub(containers), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/findByIdsDTO/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getContainersFromIDsTwo(@RequestBody List<IdCollectionDTO> idCollections) {
        Collection<Container> foundContainers = new ArrayList<Container>();
        for (IdCollectionDTO idCollection : idCollections) {
            Container container = Container.findContainer(idCollection.getId());
            foundContainers.add(container);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(Container.toJsonArray(foundContainers), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/findByIds/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getContainersFromIDs(@RequestBody List<Container> containers) {
        Collection<Container> foundContainers = new ArrayList<Container>();
        for (Container container : containers) {
            foundContainers.add(Container.findContainer(container.getId()));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(Container.toJsonArray(foundContainers), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/findByIds/jsonArrayStub", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getContainerStubsFromIDs(ArrayList<IdCollectionDTO> idCollections) {
        Collection<Container> foundContainers = new ArrayList<Container>();
        for (IdCollectionDTO idCollection : idCollections) {
            Container container = Container.findContainer(idCollection.getId());
            foundContainers.add(container);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(Container.toJsonArrayStub(foundContainers), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/findByIds/states/jsonArrayStub", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getContainerStateStubsFromIDs(@RequestBody List<IdCollectionDTO> idCollections) {
        Collection<Container> foundContainers = new ArrayList<Container>();
        for (IdCollectionDTO idCollection : idCollections) {
            Container container = Container.findContainer(idCollection.getId());
            foundContainers.add(container);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(Container.toJsonArrayStatesStub(foundContainers), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/findIdsByLabels/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getIdsFromJsonLabels(@RequestBody List<ContainerLabel> labels) {
        Collection<IdCollectionDTO> foundContainers = new ArrayList<IdCollectionDTO>();
        for (ContainerLabel label : labels) {
            List<Container> containers = Container.findContainerByContainerLabel(label.getLabelText());
            for (Container query : containers) {
                IdCollectionDTO queryID = new IdCollectionDTO();
                queryID.setId(query.getId());
                foundContainers.add(queryID);
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(IdCollectionDTO.toJsonArray(foundContainers), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/findByLabels/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getFromJsonLabels(ArrayList<ContainerLabel> labels) {
        Collection<Container> foundContainers = new ArrayList<Container>();
        for (ContainerLabel label : labels) {
            List<Container> containers = Container.findContainerByContainerLabel(label.getLabelText());
            for (Container query : containers) {
                foundContainers.add(query);
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(Container.toJsonArray(foundContainers), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/findByLabels/list", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getFromLabels(@RequestBody String labelList) {
        Collection<Container> foundContainers = new ArrayList<Container>();
        String[] labels = labelList.split(",");
        for (String label : labels) {
            List<Container> containers = Container.findContainerByContainerLabel(label);
            for (Container query : containers) {
                foundContainers.add(query);
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(Container.toJsonArray(foundContainers), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody Container container) {
        container = containerService.saveLsContainer(container);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(container.toJson(), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody List<Container> containers) {
        Collection<Container> savedContainers = containerService.saveLsContainers(containers);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(Container.toJsonArray(savedContainers), headers, HttpStatus.CREATED);
    }

//    @Transactional
//    @RequestMapping(value = "/LsContainer", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createLSContainerFromJson(@RequestBody String json) {
//        Container container = Container.fromJsonToContainer(json);
//        container = containerService.saveLsContainer(container);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(container.toJson(), headers, HttpStatus.CREATED);
//    }

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

    @Transactional
    @RequestMapping(value = { "/{id}", "/" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody Container container) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        container = containerService.updateContainer(container);
        if (container.getId() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(container.toJson(), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody List<Container> containers) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<Container> updatedContainers = new ArrayList<Container>();
        for (Container container : containers) {
            updatedContainers.add(containerService.updateContainer(container));
        }
        return new ResponseEntity<String>(Container.toJsonArray(updatedContainers), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        Container container = Container.findContainer(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (container == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        container.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(params = "FindByContainerLabelList", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindContainersByContainerNameEqualsGet(@RequestParam("containerNamesList") String containerNamesList) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Container.toJsonArray(Container.findContainersByContainerLabels(containerNamesList)), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(params = "FindByContainerLabel", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindContainersByNameGet(@RequestParam("labelText") String labelText) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Container.toJsonArray(Container.findContainerByContainerLabel(labelText)), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/getContainersInLocation", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getContainersInLocation(@RequestBody List<String> locationCodeNames,
    		@RequestParam(value="containerType", required=false) String containerType,
    		@RequestParam(value="containerKind", required=false) String containerKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
            Collection<ContainerLocationDTO> searchResults = containerService.getContainersInLocation(locationCodeNames, containerType, containerKind);
            return new ResponseEntity<String>(ContainerLocationDTO.toJsonArray(searchResults), headers, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/getWellCodesByPlateBarcodes", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getWellCodesByPlateBarcodes(@RequestBody List<String> plateBarcodes) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	Collection<PlateWellDTO> searchResults = containerService.getWellCodesByPlateBarcodes(plateBarcodes);
            return new ResponseEntity<String>(PlateWellDTO.toJsonArray(searchResults), headers, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/getContainerCodesByLabels", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getContainerCodesByLabels(@RequestBody List<String> labelTexts,
    		@RequestParam(value="containerType", required=false) String containerType,
    		@RequestParam(value="containerKind", required=false) String containerKind,
    		@RequestParam(value="labelType", required=false) String labelType,
    		@RequestParam(value="labelKind", required=false) String labelKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	Collection<CodeLabelDTO> searchResults = containerService.getContainerCodesByLabels(labelTexts, containerType, containerKind, labelType, labelKind);
            return new ResponseEntity<String>(CodeLabelDTO.toJsonArray(searchResults), headers, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/getWellContent", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getWellContent(@RequestBody List<String> wellCodes) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	Collection<WellContentDTO> searchResults = containerService.getWellContent(wellCodes);
            return new ResponseEntity<String>(WellContentDTO.toJsonArray(searchResults), headers, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
