package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.dto.CodeLabelDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ContainerDependencyCheckDTO;
import com.labsynch.labseer.dto.ContainerErrorMessageDTO;
import com.labsynch.labseer.dto.ContainerLocationDTO;
import com.labsynch.labseer.dto.ContainerRequestDTO;
import com.labsynch.labseer.dto.ContainerSearchRequestDTO;
import com.labsynch.labseer.dto.ContainerValueRequestDTO;
import com.labsynch.labseer.dto.ContainerWellCodeDTO;
import com.labsynch.labseer.dto.CreatePlateRequestDTO;
import com.labsynch.labseer.dto.IdCollectionDTO;
import com.labsynch.labseer.dto.PlateStubDTO;
import com.labsynch.labseer.dto.PlateWellDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.WellContentDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
import com.labsynch.labseer.service.ContainerService;
import com.labsynch.labseer.utils.ExcludeNulls;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;
import com.wordnik.swagger.annotations.ApiOperation;

import flexjson.JSONSerializer;

@Controller
@RequestMapping("api/v1/containers")
@Transactional
public class ApiContainerController {

    private static final Logger logger = LoggerFactory.getLogger(ApiContainerController.class);
	
	@Autowired
    private ContainerService containerService;

    @SuppressWarnings("unused")
    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @Transactional
    @RequestMapping(value = "/{idOrCodeName}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("idOrCodeName") String idOrCodeName) {
    	Container container;
    	if(SimpleUtil.isNumeric(idOrCodeName)) {
    	    	container = Container.findContainer(Long.valueOf(idOrCodeName));
 		} else {
 			container = Container.findContainerByCodeNameEquals(idOrCodeName);
 		}
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (container == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(container.toJson(), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/stub/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
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
    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson(@RequestParam(value = "lsType", required = false) String lsType,
    		@RequestParam(value = "lsKind", required = false) String lsKind,
    		@RequestParam(value = "format", required = false) String format) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Container> containers = new ArrayList<Container>();
        if (lsType != null && lsType.length() > 0 && lsKind != null && lsKind.length() > 0){
        	containers = Container.findContainersByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList();
        }else if (lsType != null && lsType.length() > 0 && (lsKind == null || lsKind.length() == 0)){
        	containers = Container.findContainersByLsTypeEquals(lsType).getResultList();
        }else if((lsType == null || lsType.length()==0) && lsKind != null && lsKind.length() > 0){
        	containers = Container.findContainersByLsKindEquals(lsKind).getResultList();
        }else{
        	containers = Container.findAllContainers();
        }
        if (format != null && format.equalsIgnoreCase("codeTable")){
        	List<CodeTableDTO> codeTableContainers = containerService.convertToCodeTables(containers);
        	return new ResponseEntity<String>(CodeTableDTO.toJsonArray(codeTableContainers), headers, HttpStatus.OK);
        }
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
    public ResponseEntity<java.lang.String> getContainersFromIDsTwo(@RequestBody String json) {
        Collection<IdCollectionDTO> idCollections = IdCollectionDTO.fromJsonArrayToIdCollectioes(json);
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
    public ResponseEntity<java.lang.String> getContainersFromIDs(@RequestBody String json) {
        Collection<Container> containers = Container.fromJsonArrayToContainers(json);
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
    public ResponseEntity<java.lang.String> getContainerStubsFromIDs(@RequestBody String json) {
        Collection<IdCollectionDTO> idCollections = IdCollectionDTO.fromJsonArrayToIdCollectioes(json);
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
    public ResponseEntity<java.lang.String> getContainerStateStubsFromIDs(@RequestBody String json) {
        Collection<IdCollectionDTO> idCollections = IdCollectionDTO.fromJsonArrayToIdCollectioes(json);
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
    public ResponseEntity<java.lang.String> getIdsFromJsonLabels(@RequestBody String json) {
        Collection<ContainerLabel> labels = ContainerLabel.fromJsonArrayToContainerLabels(json);
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
    public ResponseEntity<java.lang.String> getFromJsonLabels(@RequestBody String json) {
        Collection<ContainerLabel> labels = ContainerLabel.fromJsonArrayToContainerLabels(json);
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
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
    	Container container = Container.fromJsonToContainer(json);
        container = containerService.saveLsContainer(container);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(container.toJson(), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        Collection<Container> containers  = Container.fromJsonArrayToContainers(json);
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
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        Container container = Container.fromJsonToContainer(json);
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
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        Collection<Container> containers  = Container.fromJsonArrayToContainers(json);
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<Container> updatedContainers = new ArrayList<Container>();
        for (Container container : containers) {
            updatedContainers.add(containerService.updateContainer(container));
        }
        return new ResponseEntity<String>(Container.toJsonArray(updatedContainers), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        Container container = Container.findContainer(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (container == null || (container.isIgnored() && container.isDeleted())) {
            logger.info("Did not find the container before delete");
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        } else {
            logger.info("deleting the container: " + id);
            container.logicalDelete();
            if (Container.findContainer(id) == null || Container.findContainer(id).isIgnored()) {
                logger.info("Did not find the container after delete");
                return new ResponseEntity<String>(headers, HttpStatus.OK);
            } else {
                logger.info("Found the container after delete");
                return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
    
    @ApiOperation(value = "Checks for dependent containers that would preclude deletion",
    		notes="A container will fail the dependency check if: \n"
    				+ "A) It has other containers stored in it (added to interaction) \n"
    				+ "B) It has referenced Subjects (Subject-Container interactions) \n"
    				+ "C) Any members (member interaction) have referenced Subjects")
    @RequestMapping(value = "/checkDependencies/{idOrCodeName}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<String> checkDependencies(@PathVariable("idOrCodeName") String idOrCodeName) {
  	  HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        boolean errorsFound = false;
        Container container;
        if(SimpleUtil.isNumeric(idOrCodeName)) {
        	container = Container.findContainer(Long.valueOf(idOrCodeName));
  		} else {		
  			try {
  				container = Container.findContainerByCodeNameEquals(idOrCodeName);
  			} catch(Exception ex) {
  				container = null;
  				ErrorMessage error = new ErrorMessage();
  	            error.setErrorLevel("error");
  	            error.setMessage("parent:" + idOrCodeName +" not found");
  	            errors.add(error);
  	            errorsFound = true;
  			}
  		}
        ContainerDependencyCheckDTO result = containerService.checkDependencies(container);
        if (errorsFound) {
            return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<String>(result.toJson(), headers, HttpStatus.OK);
        }
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
    
    @ApiOperation(value="Validates container name is unique", notes="Name is determined by label lsType=name."
    		+ "Search is across all containers, for other lsType=name labels, with an exact string match."
    		+ "Successful validation (name is unique) gives HTTP Status 202: Accepted"
    		+ "Failure on validation gives HTTP Status 409: Conflict")
    @RequestMapping(value = "/validate", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> validateContainer(
    		@RequestBody String json) {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        
    	Container container = Container.fromJsonToContainer(json);
    	logger.debug("FROM THE Container VALIDATE CONTROLLER: "+container.toJson());
        ArrayList<ErrorMessage> errorMessages = containerService.validateContainer(container);
        if (!errorMessages.isEmpty()){
        	return new ResponseEntity<String>(ErrorMessage.toJsonArray(errorMessages), headers, HttpStatus.CONFLICT);
        }
        else{
        	return new ResponseEntity<String>(headers, HttpStatus.ACCEPTED);
        }
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
        	Collection<WellContentDTO> results = containerService.getWellContent(wellCodes);
        	boolean success = true;
        	for (WellContentDTO result: results){
        		if (result.getLevel() != null) success = false;
        	}
        	if (success) return new ResponseEntity<String>(WellContentDTO.toJsonArray(results), headers, HttpStatus.OK);
        	else return new ResponseEntity<String>(WellContentDTO.toJsonArray(results), headers, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/getCodeNameFromNameRequest", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getCodeNameFromName(@RequestBody String json, 
    		@RequestParam(value = "containerType", required = true) String containerType, 
    		@RequestParam(value = "containerKind", required = true) String containerKind, 
    		@RequestParam(value = "labelType", required = false) String labelType, 
    		@RequestParam(value = "labelKind", required = false) String labelKind) {
    	PreferredNameRequestDTO requestDTO = PreferredNameRequestDTO.fromJsonToPreferredNameRequestDTO(json);
        logger.info("getCodeNameFromNameRequest incoming json: " + requestDTO.toJson());
        PreferredNameResultsDTO results = containerService.getCodeNameFromName(containerType, containerKind, labelType, labelKind, requestDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(results.toJson(), headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value = "/throwInTrash", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> throwInTrash(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	Collection<ContainerRequestDTO> containersToTrash = ContainerRequestDTO.fromJsonArrayToCoes(json);
        	Collection<ContainerErrorMessageDTO> results = containerService.throwInTrash(containersToTrash);
        	boolean success = true;
        	for (ContainerErrorMessageDTO result: results){
        		if (result.getLevel() != null) success = false;
        	}
        	if (success) return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        	else return new ResponseEntity<String>(ContainerErrorMessageDTO.toJsonArray(results), headers, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/updateAmountInWell", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> updateAmountInWell(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	Collection<ContainerRequestDTO> wellsToUpdate = ContainerRequestDTO.fromJsonArrayToCoes(json);
        	Collection<ContainerErrorMessageDTO> results = containerService.updateAmountInWell(wellsToUpdate);
        	boolean success = true;
        	for (ContainerErrorMessageDTO result: results){
        		if (result.getLevel() != null) success = false;
        	}
        	if (success) return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        	else return new ResponseEntity<String>(ContainerErrorMessageDTO.toJsonArray(results), headers, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/createPlate", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> createPlate(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
    	CreatePlateRequestDTO plateRequest = CreatePlateRequestDTO.fromJsonToCreatePlateRequestDTO(json);
        try{
        	Container dupeContainer = Container.findContainerByLabelTextAndLabelTypeKind("barcode", "barcode", plateRequest.getBarcode()).getSingleResult();
        	if (dupeContainer != null){
        		return new ResponseEntity<String>("Barcode already exists", headers, HttpStatus.BAD_REQUEST);
        	}
        }
        catch (EmptyResultDataAccessException e){
        	//barcode is unique, proceed to plate creation
        }catch (IncorrectResultSizeDataAccessException e){
    		return new ResponseEntity<String>("More than one of this barcode already exists!!", headers, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
        	logger.error("Uncaught error in createPlate service", e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try{
        	PlateStubDTO result = containerService.createPlate(plateRequest);
        	return new ResponseEntity<String>(result.toJson(), headers, HttpStatus.OK);
        } catch (Exception e){
        	logger.error("Uncaught error in createPlate service", e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/createPlates", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> createPlates(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
    	Collection<CreatePlateRequestDTO> plateRequests = CreatePlateRequestDTO.fromJsonArrayToCreatePlateRequestDTO(json);
       for (CreatePlateRequestDTO plateRequest : plateRequests){
    	   try{
           		Container dupeContainer = Container.findContainerByLabelTextAndLabelTypeKind("barcode", "barcode", plateRequest.getBarcode()).getSingleResult();
           	if (dupeContainer != null){
           		return new ResponseEntity<String>("Barcode already exists: "+plateRequest.getBarcode(), headers, HttpStatus.BAD_REQUEST);
           	}
    	   }catch (EmptyResultDataAccessException e){
           	//barcode is unique, proceed to plate creation
           }catch (IncorrectResultSizeDataAccessException e){
       		return new ResponseEntity<String>("More than one of this barcode already exists!!: "+plateRequest.getBarcode(), headers, HttpStatus.BAD_REQUEST);
           }catch (Exception e){
           	logger.error("Uncaught error in createPlates service", e);
               return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
           }
	   }
        try{
        	Collection<PlateStubDTO> results = containerService.createPlates(plateRequests);
        	return new ResponseEntity<String>(PlateStubDTO.toJsonArray(results), headers, HttpStatus.OK);
        } catch (Exception e){
        	logger.error("Uncaught error in createPlates service", e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/createTube", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> createTube(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
    	CreatePlateRequestDTO plateRequest = CreatePlateRequestDTO.fromJsonToCreatePlateRequestDTO(json);
        try{
        	Container dupeContainer = Container.findContainerByLabelTextAndLabelTypeKind("barcode", "barcode", plateRequest.getBarcode()).getSingleResult();
        	if (dupeContainer != null){
        		return new ResponseEntity<String>("Barcode already exists", headers, HttpStatus.BAD_REQUEST);
        	}
        }
        catch (EmptyResultDataAccessException e){
        	//barcode is unique, proceed to plate creation
        }catch (IncorrectResultSizeDataAccessException e){
    		return new ResponseEntity<String>("More than one of this barcode already exists!!", headers, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
        	logger.error("Uncaught error in createTube service", e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try{
        	PlateStubDTO result = containerService.createTube(plateRequest);
        	return new ResponseEntity<String>(result.toJson(), headers, HttpStatus.OK);
        } catch (Exception e){
        	logger.error("Uncaught error in createTube service", e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/createTubes", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> createTubes(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
    	Collection<CreatePlateRequestDTO> plateRequests = CreatePlateRequestDTO.fromJsonArrayToCreatePlateRequestDTO(json);
       for (CreatePlateRequestDTO plateRequest : plateRequests){
    	   try{
           		Container dupeContainer = Container.findContainerByLabelTextAndLabelTypeKind("barcode", "barcode", plateRequest.getBarcode()).getSingleResult();
           	if (dupeContainer != null){
           		return new ResponseEntity<String>("Barcode already exists: "+plateRequest.getBarcode(), headers, HttpStatus.BAD_REQUEST);
           	}
    	   }catch (EmptyResultDataAccessException e){
           	//barcode is unique, proceed to plate creation
           }catch (IncorrectResultSizeDataAccessException e){
       		return new ResponseEntity<String>("More than one of this barcode already exists!! "+plateRequest.getBarcode(), headers, HttpStatus.BAD_REQUEST);
           }catch (Exception e){
           	logger.error("Uncaught error in createTubes service", e);
               return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
           }
	   }
        try{
        	Collection<PlateStubDTO> results = containerService.createTubes(plateRequests);
        	return new ResponseEntity<String>(PlateStubDTO.toJsonArray(results), headers, HttpStatus.OK);
        } catch (Exception e){
        	logger.error("Uncaught error in createTubes service", e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/getWellContentByPlateBarcode/{plateBarcode}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getWellContentByPlateBarcode(@PathVariable("plateBarcode") String plateBarcode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	Collection<WellContentDTO> results = containerService.getWellContentByPlateBarcode(plateBarcode);
        	boolean success = true;
        	for (WellContentDTO result: results){
        		if (result.getLevel() != null) success = false;
        	}
        	if (success) return new ResponseEntity<String>(WellContentDTO.toJsonArray(results), headers, HttpStatus.OK);
        	else return new ResponseEntity<String>(WellContentDTO.toJsonArray(results), headers, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/getPlateTypeByPlateBarcode/{plateBarcode}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getPlateTypeByPlateBarcode(@PathVariable("plateBarcode") String plateBarcode) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	PlateStubDTO result = containerService.getPlateTypeByPlateBarcode(plateBarcode);
        	if (result != null){
        		return new ResponseEntity<String>(result.toJson(), headers, HttpStatus.OK);
        	}
        	else return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/updateWellContent", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> updateWellContent(@RequestParam(value="copyPreviousValues", required=false) Boolean copyPreviousValues,
    		@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	Collection<WellContentDTO> wellsToUpdate = WellContentDTO.fromJsonArrayToWellCoes(json);
        	if (copyPreviousValues==null) copyPreviousValues=true;
        	Collection<ContainerErrorMessageDTO> results = containerService.updateWellContent(wellsToUpdate, copyPreviousValues);
        	boolean success = true;
        	for (ContainerErrorMessageDTO result: results){
        		if (result.getLevel() != null) success = false;
        	}
        	if (success) return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        	else return new ResponseEntity<String>(ContainerErrorMessageDTO.toJsonArray(results), headers, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
        	logger.error("Caught error in updateWellContent",e);
            return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/getContainersByCodeNames", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getContainersByCodeNames(@RequestBody List<String> codeNames) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	Collection<ContainerErrorMessageDTO> searchResults = containerService.getContainersByCodeNames(codeNames);
        	boolean success = true;
        	for (ContainerErrorMessageDTO result: searchResults){
        		if (result.getLevel() != null) success = false;
        	}
        	if (success) return new ResponseEntity<String>(ContainerErrorMessageDTO.toJsonArray(searchResults), headers, HttpStatus.OK);
        	else return new ResponseEntity<String>(ContainerErrorMessageDTO.toJsonArray(searchResults), headers, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
        	logger.error("Uncaught error in getContainersByCodeNames",e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/getDefinitionContainersByContainerCodeNames", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getDefinitionContainersByContainerCodeNames(@RequestBody List<String> codeNames) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	Collection<ContainerErrorMessageDTO> searchResults = containerService.getDefinitionContainersByContainerCodeNames(codeNames);
        	boolean success = true;
        	for (ContainerErrorMessageDTO result: searchResults){
        		if (result.getLevel() != null) success = false;
        	}
        	if (success) return new ResponseEntity<String>(ContainerErrorMessageDTO.toJsonArray(searchResults), headers, HttpStatus.OK);
        	else return new ResponseEntity<String>(ContainerErrorMessageDTO.toJsonArray(searchResults), headers, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
        	logger.error("Uncaught error in getDefinitionContainersByContainerCodeNames",e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/getWellCodesByContainerCodes", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getWellCodesByContainerCodes(@RequestBody List<String> codeNames) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	Collection<ContainerWellCodeDTO> searchResults = containerService.getWellCodesByContainerCodes(codeNames);
        	return new ResponseEntity<String>(ContainerWellCodeDTO.toJsonArray(searchResults), headers, HttpStatus.OK);
        } catch (Exception e){
        	logger.error("Uncaught error in getWellCodesByContainerCodes",e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/moveToLocation", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> moveToLocation(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	Collection<ContainerLocationDTO> requests = ContainerLocationDTO.fromJsonArrayToContainerLocatioes(json);
        	Collection<ContainerLocationDTO> results = containerService.moveToLocation(requests);
        	boolean success = true;
        	for (ContainerLocationDTO result: results){
        		if (result.getLevel() != null) success = false;
        	}
        	if (success) return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        	else return new ResponseEntity<String>(ContainerLocationDTO.toJsonArray(results), headers, HttpStatus.BAD_REQUEST);
        } catch (Exception e){
        	logger.error("Uncaught error in moveToLocation",e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/searchContainers", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> searchContainers(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
    	ContainerSearchRequestDTO searchRequest = ContainerSearchRequestDTO.fromJsonToContainerSearchRequestDTO(json);
        try{
        	Collection<Container> results = containerService.searchContainers(searchRequest);
        	logger.debug(Container.toJsonArray(results));
        	return new ResponseEntity<String>(Container.toJsonArray(results), headers, HttpStatus.OK);
        } catch (Exception e){
        	logger.error("Uncaught error in searchContainers service", e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/getContainerCodeNamesByContainerValue", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getContainersByContainerValue(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try{
        	ContainerValueRequestDTO requestDTO = ContainerValueRequestDTO.fromJsonToContainerValueRequestDTO(json);
        	Collection<String> searchResults = containerService.getContainersByContainerValue(requestDTO);
        	return new ResponseEntity<String>(new JSONSerializer().serialize(searchResults), headers, HttpStatus.OK);
        } catch (Exception e){
        	logger.error("Uncaught error in getContainersByCodeNames",e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
