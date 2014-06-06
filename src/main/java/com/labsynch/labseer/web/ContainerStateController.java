package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.domain.UpdateLog;
import com.labsynch.labseer.dto.ContainerMiniDTO;
import com.labsynch.labseer.dto.ContainerStateMiniDTO;
import com.labsynch.labseer.service.ContainerStateService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import flexjson.JSONTokener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.io.IOUtils;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping("/containerstates")
@RooWebScaffold(path = "containerstates", formBackingObject = ContainerState.class)
@RooWebFinder
public class ContainerStateController {

    private static final Logger logger = LoggerFactory.getLogger(ContainerStateController.class);

    @Autowired
    private PropertiesUtilService propertiesUtilService;

    @Autowired
    private ContainerStateService csService;

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        ContainerState containerState = ContainerState.findContainerState(id);
        logger.info("query id: " + id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (containerState == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(containerState.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ContainerState> result = ContainerState.findAllContainerStates();
        return new ResponseEntity<String>(ContainerState.toJsonArray(result), headers, HttpStatus.OK);
    }

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

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        ContainerState containerState = ContainerState.fromJsonToContainerState(json);
        containerState.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(containerState.toJson(), headers, HttpStatus.CREATED);
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

    @RequestMapping(value = { "/jsonArray", "/jsonArrayParse" }, method = RequestMethod.POST, headers = "Accept=application/json")
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

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ContainerState containerState = ContainerState.fromJsonToContainerState(json);
        if (containerState.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(containerState.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
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

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        ContainerState containerState = ContainerState.findContainerState(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (containerState == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        containerState.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @Transactional
    void populateEditForm(Model uiModel, ContainerState containerState) {
        uiModel.addAttribute("containerState", containerState);
        addDateTimeFormatPatterns(uiModel);
        List<Container> containers = new ArrayList<Container>();
        if (containerState.getId() != null) {
            containers.add(ContainerState.findContainerState(containerState.getId()).getContainer());
        }
        uiModel.addAttribute("containers", containers);
    }

    @RequestMapping(params = "find=ByContainer", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindContainerStatesByContainer(@RequestParam("container") Container container) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ContainerState.toJsonArray(ContainerState.findContainerStatesByContainer(container).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByContainerAndLsKindEqualsAndIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(@RequestParam("container") Container container, @RequestParam("lsKind") String lsKind, @RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ContainerState.toJsonArray(ContainerState.findContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(container, lsKind, ignored).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindContainerStatesByIgnoredNot(@RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ContainerState.toJsonArray(ContainerState.findContainerStatesByIgnoredNot(ignored).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = { "find=ByContainer", "form" }, method = RequestMethod.GET)
    public String findContainerStatesByContainerForm(Model uiModel) {
        uiModel.addAttribute("containers", Container.findAllContainers());
        return "containerstates/findContainerStatesByContainer";
    }

    @RequestMapping(params = "find=ByContainer", method = RequestMethod.GET)
    public String findContainerStatesByContainer(@RequestParam("container") Container container, Model uiModel) {
        uiModel.addAttribute("containerstates", ContainerState.findContainerStatesByContainer(container).getResultList());
        return "containerstates/list";
    }

    @RequestMapping(params = { "find=ByContainerAndLsKindEqualsAndIgnoredNot", "form" }, method = RequestMethod.GET)
    public String findContainerStatesByContainerAndLsKindEqualsAndIgnoredNotForm(Model uiModel) {
        uiModel.addAttribute("containers", Container.findAllContainers());
        return "containerstates/findContainerStatesByContainerAndLsKindEqualsAndIgnoredNot";
    }

    @RequestMapping(params = "find=ByContainerAndLsKindEqualsAndIgnoredNot", method = RequestMethod.GET)
    public String findContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(@RequestParam("container") Container container, @RequestParam("lsKind") String lsKind, @RequestParam(value = "ignored", required = false) boolean ignored, Model uiModel) {
        uiModel.addAttribute("containerstates", ContainerState.findContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(container, lsKind, ignored).getResultList());
        return "containerstates/list";
    }

    @RequestMapping(params = { "find=ByIgnoredNot", "form" }, method = RequestMethod.GET)
    public String findContainerStatesByIgnoredNotForm(Model uiModel) {
        return "containerstates/findContainerStatesByIgnoredNot";
    }

    @RequestMapping(params = "find=ByIgnoredNot", method = RequestMethod.GET)
    public String findContainerStatesByIgnoredNot(@RequestParam(value = "ignored", required = false) boolean ignored, Model uiModel) {
        uiModel.addAttribute("containerstates", ContainerState.findContainerStatesByIgnoredNot(ignored).getResultList());
        return "containerstates/list";
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ContainerState containerState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, containerState);
            return "containerstates/create";
        }
        uiModel.asMap().clear();
        containerState.persist();
        return "redirect:/containerstates/" + encodeUrlPathSegment(containerState.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ContainerState());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Container.countContainers() == 0) {
            dependencies.add(new String[] { "container", "containers" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "containerstates/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("containerstate", ContainerState.findContainerState(id));
        uiModel.addAttribute("itemId", id);
        return "containerstates/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("containerstates", ContainerState.findContainerStateEntries(firstResult, sizeNo));
            float nrOfPages = (float) ContainerState.countContainerStates() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("containerstates", ContainerState.findAllContainerStates());
        }
        addDateTimeFormatPatterns(uiModel);
        return "containerstates/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ContainerState containerState, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, containerState);
            return "containerstates/update";
        }
        uiModel.asMap().clear();
        containerState.merge();
        return "redirect:/containerstates/" + encodeUrlPathSegment(containerState.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ContainerState.findContainerState(id));
        return "containerstates/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ContainerState containerState = ContainerState.findContainerState(id);
        containerState.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/containerstates";
    }

    void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("containerState_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("containerState_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {
        }
        return pathSegment;
    }
}
