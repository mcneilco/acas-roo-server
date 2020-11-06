package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
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
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@Controller
@RequestMapping("/containerstates")
@RooWebScaffold(path = "containerstates", formBackingObject = ContainerState.class)
@RooWebFinder
@RooWebJson(jsonObject = ContainerState.class)
public class ContainerStateController {

	@RequestMapping(params = { "find=ByContainer", "form" }, method = RequestMethod.GET)
    public String findContainerStatesByContainerForm(Model uiModel) {
        uiModel.addAttribute("containers", Container.findAllContainers());
        return "containerstates/findContainerStatesByContainer";
    }

	@RequestMapping(params = "find=ByContainer", method = RequestMethod.GET)
    public String findContainerStatesByContainer(@RequestParam("container") Container container, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("containerstates", ContainerState.findContainerStatesByContainer(container, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ContainerState.countFindContainerStatesByContainer(container) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("containerstates", ContainerState.findContainerStatesByContainer(container, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "containerstates/list";
    }

	@RequestMapping(params = { "find=ByContainerAndLsKindEqualsAndIgnoredNot", "form" }, method = RequestMethod.GET)
    public String findContainerStatesByContainerAndLsKindEqualsAndIgnoredNotForm(Model uiModel) {
        uiModel.addAttribute("containers", Container.findAllContainers());
        return "containerstates/findContainerStatesByContainerAndLsKindEqualsAndIgnoredNot";
    }

	@RequestMapping(params = "find=ByContainerAndLsKindEqualsAndIgnoredNot", method = RequestMethod.GET)
    public String findContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(@RequestParam("container") Container container, @RequestParam("lsKind") String lsKind, @RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("containerstates", ContainerState.findContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(container, lsKind, ignored, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ContainerState.countFindContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(container, lsKind, ignored) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("containerstates", ContainerState.findContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(container, lsKind, ignored, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "containerstates/list";
    }

	@RequestMapping(params = { "find=ByIgnoredNot", "form" }, method = RequestMethod.GET)
    public String findContainerStatesByIgnoredNotForm(Model uiModel) {
        return "containerstates/findContainerStatesByIgnoredNot";
    }

	@RequestMapping(params = "find=ByIgnoredNot", method = RequestMethod.GET)
    public String findContainerStatesByIgnoredNot(@RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("containerstates", ContainerState.findContainerStatesByIgnoredNot(ignored, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ContainerState.countFindContainerStatesByIgnoredNot(ignored) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("containerstates", ContainerState.findContainerStatesByIgnoredNot(ignored, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
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
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("containerstates", ContainerState.findContainerStateEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ContainerState.countContainerStates() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("containerstates", ContainerState.findAllContainerStates(sortFieldName, sortOrder));
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

	void populateEditForm(Model uiModel, ContainerState containerState) {
        uiModel.addAttribute("containerState", containerState);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("containers", Container.findAllContainers());
        uiModel.addAttribute("containervalues", ContainerValue.findAllContainerValues());
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            ContainerState containerState = ContainerState.findContainerState(id);
            if (containerState == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(containerState.toJson(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            List<ContainerState> result = ContainerState.findAllContainerStates();
            return new ResponseEntity<String>(ContainerState.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ContainerState containerState = ContainerState.fromJsonToContainerState(json);
            containerState.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+containerState.getId().toString()).build().toUriString());
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            for (ContainerState containerState: ContainerState.fromJsonArrayToContainerStates(json)) {
                containerState.persist();
            }
            return new ResponseEntity<String>(headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json, @PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ContainerState containerState = ContainerState.fromJsonToContainerState(json);
            containerState.setId(id);
            if (containerState.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ContainerState containerState = ContainerState.findContainerState(id);
            if (containerState == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            containerState.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByContainer", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindContainerStatesByContainer(@RequestParam("container") Container container) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ContainerState.toJsonArray(ContainerState.findContainerStatesByContainer(container).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByContainerAndLsKindEqualsAndIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(@RequestParam("container") Container container, @RequestParam("lsKind") String lsKind, @RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ContainerState.toJsonArray(ContainerState.findContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(container, lsKind, ignored).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindContainerStatesByIgnoredNot(@RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ContainerState.toJsonArray(ContainerState.findContainerStatesByIgnoredNot(ignored).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
