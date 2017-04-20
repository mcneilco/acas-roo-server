package com.labsynch.labseer.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.domain.UpdateLog;
import com.labsynch.labseer.dto.ContainerMiniDTO;
import com.labsynch.labseer.dto.ContainerStateMiniDTO;
import com.labsynch.labseer.dto.ContainerValueRequestDTO;
import com.labsynch.labseer.service.ContainerService;
import com.labsynch.labseer.service.ContainerStateService;
import com.labsynch.labseer.utils.PropertiesUtilService;

import flexjson.JSONSerializer;
import flexjson.JSONTokener;

@Controller
@RequestMapping("api/v1/containerstates")
@Transactional
public class ApiContainerStateController {

	private static final Logger logger = LoggerFactory.getLogger(ApiContainerStateController.class);
	
  @Autowired
  private ContainerStateService csService;
	
	@Autowired
    private ContainerService containerService;

    @SuppressWarnings("unused")
    @Autowired
    private PropertiesUtilService propertiesUtilService;

  @Transactional
  @RequestMapping(value = "/findValidContainerStates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
  public ResponseEntity<java.lang.String> getValidContainerStatesFromIDs(@RequestBody String json) {
      Collection<Container> containers = Container.fromJsonArrayToContainers(json);
      logger.debug("number of containers found: " + containers.size());
      List<ContainerState> containerStates = new ArrayList<ContainerState>();
      int counter = 0;
      List<Long> containerIds = new ArrayList<Long>();
      for (Container container : containers) {
          containerIds.add(container.getId());
          counter++;
          if (counter == 990) {
              containerStates.addAll(ContainerState.findValidContainerStates(containerIds));
              counter = 0;
              containerIds.clear();
              logger.debug("number of container States found: " + containerStates.size());
              logger.debug("number of counter: " + counter);
          }
      }
      if (counter > 0) {
          containerStates.addAll(ContainerState.findValidContainerStates(containerIds));
      }
      logger.debug("number of container States found: " + containerStates.size());
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", "application/json");
      return new ResponseEntity<String>(ContainerState.toJsonArray(containerStates), headers, HttpStatus.OK);
  }

  @Transactional
  @RequestMapping(value = "/findContainerStates/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
  public ResponseEntity<java.lang.String> getContainerStatesFromIDs(@RequestBody String json) {
      logger.info("incoming json:" + json);
      Collection<Container> containers = Container.fromJsonArrayToContainers(json);
      List<ContainerState> containerStates = new ArrayList<ContainerState>();
      for (Container container : containers) {
          logger.info("incoming container: " + container);
          for (ContainerState cs : ContainerState.findContainerStatesByContainer(Container.findContainer(container.getId())).getResultList()) {
              containerStates.add(cs);
          }
      }
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", "application/json");
      return new ResponseEntity<String>(ContainerState.toJsonArray(containerStates), headers, HttpStatus.OK);
  }

  @RequestMapping(value = "/jsonFile", method = RequestMethod.POST, headers = "Accept=application/json")
  public ResponseEntity<java.lang.String> createFromJsonFile(@RequestBody String jsonFile) {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", "application/json");
      Collection<ContainerStateMiniDTO> savedContainerStates = new ArrayList<ContainerStateMiniDTO>();
      int batchSize = propertiesUtilService.getBatchSize();
      int i = 0;
      try {
          BufferedReader br = new BufferedReader(new FileReader(jsonFile));
          JSONTokener jsonTokens = new JSONTokener(br);
          Object token;
          char delimiter;
          char END_OF_ARRAY = ']';
          ContainerStateMiniDTO stateDTO;
          ContainerMiniDTO containerDTO;
          while (jsonTokens.more()) {
              delimiter = jsonTokens.nextClean();
              logger.info("delimiter: " + delimiter);
              if (delimiter != END_OF_ARRAY) {
                  token = jsonTokens.nextValue();
                  logger.info(token.toString());
                  ContainerState containerState = ContainerState.fromJsonToContainerState(token.toString());
                  containerState.persist();
                  stateDTO = new ContainerStateMiniDTO(containerState);
                  stateDTO.setId(containerState.getId());
                  stateDTO.setVersion(containerState.getVersion());
                  containerDTO = new ContainerMiniDTO(containerState.getContainer());
                  stateDTO.setContainer(containerDTO);
                  savedContainerStates.add(stateDTO);
                  if (i % batchSize == 0) {
                      containerState.flush();
                      containerState.clear();
                  }
                  i++;
              } else {
                  break;
              }
          }
      } catch (Exception e) {
          logger.error("ERROR: " + e);
          return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<String>(ContainerStateMiniDTO.toJsonArray(savedContainerStates), headers, HttpStatus.CREATED);
  }

  @RequestMapping(value = "/jsonArrayParse", method = RequestMethod.POST, headers = "Accept=application/json")
  public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", "application/json");
      Collection<ContainerStateMiniDTO> savedContainerStates = new ArrayList<ContainerStateMiniDTO>();
      int batchSize = propertiesUtilService.getBatchSize();
      int i = 0;
      ContainerStateMiniDTO stateDTO;
      ContainerMiniDTO containerDTO;
      BufferedReader br = null;
      StringReader sr = null;
      try {
          sr = new StringReader(json);
          br = new BufferedReader(sr);
          for (ContainerState containerState : ContainerState.fromJsonArrayToContainerStates(br)) {
              containerState.persist();
              stateDTO = new ContainerStateMiniDTO(containerState);
              stateDTO.setId(containerState.getId());
              stateDTO.setVersion(containerState.getVersion());
              containerDTO = new ContainerMiniDTO(containerState.getContainer());
              stateDTO.setContainer(containerDTO);
              savedContainerStates.add(stateDTO);
              if (i % batchSize == 0) {
                  containerState.flush();
                  containerState.clear();
              }
              i++;
          }
      } catch (Exception e) {
          logger.error("ERROR: " + e);
          return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
      } finally {
          IOUtils.closeQuietly(sr);
          IOUtils.closeQuietly(br);
      }
      return new ResponseEntity<String>(ContainerStateMiniDTO.toJsonArray(savedContainerStates), headers, HttpStatus.CREATED);
  }

  @RequestMapping(value = "/jsonArrayParse", method = RequestMethod.PUT, headers = "Accept=application/json")
  public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", "application/json");
      int batchSize = propertiesUtilService.getBatchSize();
      int i = 0;
      BufferedReader br = null;
      StringReader sr = null;
      try {
          sr = new StringReader(json);
          br = new BufferedReader(sr);
          for (ContainerState containerState : ContainerState.fromJsonArrayToContainerStates(br)) {
              if (containerState.merge() == null) {
                  return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
              }
              if (i % batchSize == 0) {
                  containerState.flush();
                  containerState.clear();
              }
              i++;
          }
      } catch (Exception e) {
          logger.error("ERROR: " + e);
          return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
      } finally {
          IOUtils.closeQuietly(sr);
          IOUtils.closeQuietly(br);
      }
      return new ResponseEntity<String>("[]", headers, HttpStatus.OK);
  }

  @Transactional
  @RequestMapping(value = "/ignoreByContainer/jsonArray", params = "lsKind", method = RequestMethod.POST, headers = "Accept=application/json")
  public ResponseEntity<java.lang.String> updateIgnoreFromJsonArrayAndKind(@RequestParam("lsKind") String lsKind, @RequestBody String json) {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", "application/json");
      LsTransaction uplogTransaction;
      try {
          uplogTransaction = csService.ignoreByContainer(json, lsKind);
      } catch (Exception e) {
          logger.error("ERROR: " + e);
          return new ResponseEntity<String>(e.toString(), headers, HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<String>(uplogTransaction.toJson(), headers, HttpStatus.OK);
  }

  @Transactional
  @RequestMapping(value = "/ignore/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
  public ResponseEntity<java.lang.String> updateIgnoreFromJsonArray(@RequestBody String json) {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", "application/json");
      LsTransaction lst = new LsTransaction();
      lst.setComments("mark states to ignore");
      lst.setRecordedDate(new Date());
      lst.persist();
      int batchSize = propertiesUtilService.getBatchSize();
      int i = 0;
      BufferedReader br = null;
      StringReader sr = null;
      UpdateLog uplog = null;
      try {
          sr = new StringReader(json);
          br = new BufferedReader(sr);
          for (ContainerState containerState : ContainerState.fromJsonArrayToContainerStates(br)) {
              uplog = new UpdateLog();
              uplog.setThing(containerState.getId());
              uplog.setLsTransaction(lst.getId());
              uplog.setUpdateAction("ignore");
              uplog.setComments("mark states to ignore");
              uplog.setRecordedDate(new Date());
              uplog.persist();
              if (i % batchSize == 0) {
                  uplog.flush();
                  uplog.clear();
              }
              i++;
          }
          int results = ContainerState.ignoreStates(lst.getId());
          logger.debug("number of states marked to ignore: " + results);
      } catch (Exception e) {
          logger.error("ERROR: " + e);
          return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
      } finally {
          IOUtils.closeQuietly(sr);
          IOUtils.closeQuietly(br);
      }
      return new ResponseEntity<String>("[]", headers, HttpStatus.OK);
  }
  
  @Transactional
  @RequestMapping(value = "/getContainerStatesByContainerValue", method = RequestMethod.POST, headers = "Accept=application/json")
  @ResponseBody
  public ResponseEntity<java.lang.String> getContainersByContainerValue(@RequestBody String json, @RequestParam(value = "with", required = false) String with) {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", "application/json; charset=utf-8");
      try{
      	ContainerValueRequestDTO requestDTO = ContainerValueRequestDTO.fromJsonToContainerValueRequestDTO(json);
      	Collection<ContainerState> searchResults = csService.getContainerStatesByContainerValue(requestDTO);
      	if (with != null && with.equalsIgnoreCase("nestedContainer")){
      		return new ResponseEntity<String>(ContainerState.toJsonArrayWithNestedContainers(searchResults), headers, HttpStatus.OK);
      	}
      	return new ResponseEntity<String>(ContainerState.toJsonArray(searchResults), headers, HttpStatus.OK);
      } catch (Exception e){
      	logger.error("Uncaught error in getContainerStatesByContainerValue",e);
          return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

	
}
