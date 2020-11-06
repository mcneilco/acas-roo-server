package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.ItxContainerContainerState;
import com.labsynch.labseer.domain.ThingPage;
import com.labsynch.labseer.service.ItxContainerContainerService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import flexjson.JSONTokener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
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

@RooWebJson(jsonObject = ItxContainerContainer.class)
@Controller
@RequestMapping("/itxcontainercontainers")
@RooWebScaffold(path = "itxcontainercontainers", formBackingObject = ItxContainerContainer.class)
@RooWebFinder
public class ItxContainerContainerController {

	@RequestMapping(params = { "find=ByLsTransactionEquals", "form" }, method = RequestMethod.GET)
    public String findItxContainerContainersByLsTransactionEqualsForm(Model uiModel) {
        return "itxcontainercontainers/findItxContainerContainersByLsTransactionEquals";
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET)
    public String findItxContainerContainersByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxcontainercontainers", ItxContainerContainer.findItxContainerContainersByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ItxContainerContainer.countFindItxContainerContainersByLsTransactionEquals(lsTransaction) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxcontainercontainers", ItxContainerContainer.findItxContainerContainersByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxcontainercontainers/list";
    }

	@RequestMapping(params = { "find=ByLsTypeEqualsAndFirstContainerEquals", "form" }, method = RequestMethod.GET)
    public String findItxContainerContainersByLsTypeEqualsAndFirstContainerEqualsForm(Model uiModel) {
        uiModel.addAttribute("containers", Container.findAllContainers());
        return "itxcontainercontainers/findItxContainerContainersByLsTypeEqualsAndFirstContainerEquals";
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndFirstContainerEquals", method = RequestMethod.GET)
    public String findItxContainerContainersByLsTypeEqualsAndFirstContainerEquals(@RequestParam("lsType") String lsType, @RequestParam("firstContainer") Container firstContainer, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxcontainercontainers", ItxContainerContainer.findItxContainerContainersByLsTypeEqualsAndFirstContainerEquals(lsType, firstContainer, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ItxContainerContainer.countFindItxContainerContainersByLsTypeEqualsAndFirstContainerEquals(lsType, firstContainer) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxcontainercontainers", ItxContainerContainer.findItxContainerContainersByLsTypeEqualsAndFirstContainerEquals(lsType, firstContainer, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxcontainercontainers/list";
    }

	@RequestMapping(params = { "find=ByLsTypeEqualsAndSecondContainerEquals", "form" }, method = RequestMethod.GET)
    public String findItxContainerContainersByLsTypeEqualsAndSecondContainerEqualsForm(Model uiModel) {
        uiModel.addAttribute("containers", Container.findAllContainers());
        return "itxcontainercontainers/findItxContainerContainersByLsTypeEqualsAndSecondContainerEquals";
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndSecondContainerEquals", method = RequestMethod.GET)
    public String findItxContainerContainersByLsTypeEqualsAndSecondContainerEquals(@RequestParam("lsType") String lsType, @RequestParam("secondContainer") Container secondContainer, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxcontainercontainers", ItxContainerContainer.findItxContainerContainersByLsTypeEqualsAndSecondContainerEquals(lsType, secondContainer, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ItxContainerContainer.countFindItxContainerContainersByLsTypeEqualsAndSecondContainerEquals(lsType, secondContainer) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxcontainercontainers", ItxContainerContainer.findItxContainerContainersByLsTypeEqualsAndSecondContainerEquals(lsType, secondContainer, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxcontainercontainers/list";
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ItxContainerContainer itxContainerContainer, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itxContainerContainer);
            return "itxcontainercontainers/create";
        }
        uiModel.asMap().clear();
        itxContainerContainer.persist();
        return "redirect:/itxcontainercontainers/" + encodeUrlPathSegment(itxContainerContainer.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ItxContainerContainer());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Container.countContainers() == 0) {
            dependencies.add(new String[] { "firstContainer", "containers" });
        }
        if (Container.countContainers() == 0) {
            dependencies.add(new String[] { "secondContainer", "containers" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "itxcontainercontainers/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itxcontainercontainer", ItxContainerContainer.findItxContainerContainer(id));
        uiModel.addAttribute("itemId", id);
        return "itxcontainercontainers/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxcontainercontainers", ItxContainerContainer.findItxContainerContainerEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ItxContainerContainer.countItxContainerContainers() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxcontainercontainers", ItxContainerContainer.findAllItxContainerContainers(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxcontainercontainers/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ItxContainerContainer itxContainerContainer, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itxContainerContainer);
            return "itxcontainercontainers/update";
        }
        uiModel.asMap().clear();
        itxContainerContainer.merge();
        return "redirect:/itxcontainercontainers/" + encodeUrlPathSegment(itxContainerContainer.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ItxContainerContainer.findItxContainerContainer(id));
        return "itxcontainercontainers/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ItxContainerContainer itxContainerContainer = ItxContainerContainer.findItxContainerContainer(id);
        itxContainerContainer.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/itxcontainercontainers";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("itxContainerContainer_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("itxContainerContainer_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, ItxContainerContainer itxContainerContainer) {
        uiModel.addAttribute("itxContainerContainer", itxContainerContainer);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("containers", Container.findAllContainers());
        uiModel.addAttribute("itxcontainercontainerstates", ItxContainerContainerState.findAllItxContainerContainerStates());
        uiModel.addAttribute("thingpages", ThingPage.findAllThingPages());
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
            ItxContainerContainer itxContainerContainer = ItxContainerContainer.findItxContainerContainer(id);
            if (itxContainerContainer == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(itxContainerContainer.toJson(), headers, HttpStatus.OK);
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
            List<ItxContainerContainer> result = ItxContainerContainer.findAllItxContainerContainers();
            return new ResponseEntity<String>(ItxContainerContainer.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ItxContainerContainer itxContainerContainer = ItxContainerContainer.fromJsonToItxContainerContainer(json);
            itxContainerContainer.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+itxContainerContainer.getId().toString()).build().toUriString());
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
            for (ItxContainerContainer itxContainerContainer: ItxContainerContainer.fromJsonArrayToItxContainerContainers(json)) {
                itxContainerContainer.persist();
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
            ItxContainerContainer itxContainerContainer = ItxContainerContainer.fromJsonToItxContainerContainer(json);
            itxContainerContainer.setId(id);
            if (itxContainerContainer.merge() == null) {
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
            ItxContainerContainer itxContainerContainer = ItxContainerContainer.findItxContainerContainer(id);
            if (itxContainerContainer == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            itxContainerContainer.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindItxContainerContainersByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ItxContainerContainer.toJsonArray(ItxContainerContainer.findItxContainerContainersByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndFirstContainerEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindItxContainerContainersByLsTypeEqualsAndFirstContainerEquals(@RequestParam("lsType") String lsType, @RequestParam("firstContainer") Container firstContainer) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ItxContainerContainer.toJsonArray(ItxContainerContainer.findItxContainerContainersByLsTypeEqualsAndFirstContainerEquals(lsType, firstContainer).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndSecondContainerEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindItxContainerContainersByLsTypeEqualsAndSecondContainerEquals(@RequestParam("lsType") String lsType, @RequestParam("secondContainer") Container secondContainer) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ItxContainerContainer.toJsonArray(ItxContainerContainer.findItxContainerContainersByLsTypeEqualsAndSecondContainerEquals(lsType, secondContainer).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
