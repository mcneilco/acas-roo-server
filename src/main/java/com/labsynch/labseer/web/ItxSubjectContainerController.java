package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.ItxSubjectContainerState;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.ThingPage;
import com.labsynch.labseer.service.ItxSubjectContainerService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import flexjson.JSONTokener;
import java.io.BufferedReader;
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
import org.springframework.stereotype.Controller;
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
@RequestMapping("/itxsubjectcontainers")
public class ItxSubjectContainerController {

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            ItxSubjectContainer itxSubjectContainer = ItxSubjectContainer.findItxSubjectContainer(id);
            if (itxSubjectContainer == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(itxSubjectContainer.toJson(), headers, HttpStatus.OK);
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
            List<ItxSubjectContainer> result = ItxSubjectContainer.findAllItxSubjectContainers();
            return new ResponseEntity<String>(ItxSubjectContainer.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ItxSubjectContainer itxSubjectContainer = ItxSubjectContainer.fromJsonToItxSubjectContainer(json);
            itxSubjectContainer.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+itxSubjectContainer.getId().toString()).build().toUriString());
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
            for (ItxSubjectContainer itxSubjectContainer: ItxSubjectContainer.fromJsonArrayToItxSubjectContainers(json)) {
                itxSubjectContainer.persist();
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
            ItxSubjectContainer itxSubjectContainer = ItxSubjectContainer.fromJsonToItxSubjectContainer(json);
            itxSubjectContainer.setId(id);
            if (itxSubjectContainer.merge() == null) {
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
            ItxSubjectContainer itxSubjectContainer = ItxSubjectContainer.findItxSubjectContainer(id);
            if (itxSubjectContainer == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            itxSubjectContainer.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByCodeNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindItxSubjectContainersByCodeNameEquals(@RequestParam("codeName") String codeName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ItxSubjectContainer.toJsonArray(ItxSubjectContainer.findItxSubjectContainersByCodeNameEquals(codeName).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByContainer", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindItxSubjectContainersByContainer(@RequestParam("container") Container container) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ItxSubjectContainer.toJsonArray(ItxSubjectContainer.findItxSubjectContainersByContainer(container).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindItxSubjectContainersByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ItxSubjectContainer.toJsonArray(ItxSubjectContainer.findItxSubjectContainersByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=BySubject", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindItxSubjectContainersBySubject(@RequestParam("subject") Subject subject) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ItxSubjectContainer.toJsonArray(ItxSubjectContainer.findItxSubjectContainersBySubject(subject).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = { "find=ByCodeNameEquals", "form" }, method = RequestMethod.GET)
    public String findItxSubjectContainersByCodeNameEqualsForm(Model uiModel) {
        return "itxsubjectcontainers/findItxSubjectContainersByCodeNameEquals";
    }

	@RequestMapping(params = "find=ByCodeNameEquals", method = RequestMethod.GET)
    public String findItxSubjectContainersByCodeNameEquals(@RequestParam("codeName") String codeName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxsubjectcontainers", ItxSubjectContainer.findItxSubjectContainersByCodeNameEquals(codeName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ItxSubjectContainer.countFindItxSubjectContainersByCodeNameEquals(codeName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxsubjectcontainers", ItxSubjectContainer.findItxSubjectContainersByCodeNameEquals(codeName, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxsubjectcontainers/list";
    }

	@RequestMapping(params = { "find=ByContainer", "form" }, method = RequestMethod.GET)
    public String findItxSubjectContainersByContainerForm(Model uiModel) {
        uiModel.addAttribute("containers", Container.findAllContainers());
        return "itxsubjectcontainers/findItxSubjectContainersByContainer";
    }

	@RequestMapping(params = "find=ByContainer", method = RequestMethod.GET)
    public String findItxSubjectContainersByContainer(@RequestParam("container") Container container, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxsubjectcontainers", ItxSubjectContainer.findItxSubjectContainersByContainer(container, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ItxSubjectContainer.countFindItxSubjectContainersByContainer(container) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxsubjectcontainers", ItxSubjectContainer.findItxSubjectContainersByContainer(container, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxsubjectcontainers/list";
    }

	@RequestMapping(params = { "find=ByLsTransactionEquals", "form" }, method = RequestMethod.GET)
    public String findItxSubjectContainersByLsTransactionEqualsForm(Model uiModel) {
        return "itxsubjectcontainers/findItxSubjectContainersByLsTransactionEquals";
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET)
    public String findItxSubjectContainersByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxsubjectcontainers", ItxSubjectContainer.findItxSubjectContainersByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ItxSubjectContainer.countFindItxSubjectContainersByLsTransactionEquals(lsTransaction) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxsubjectcontainers", ItxSubjectContainer.findItxSubjectContainersByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxsubjectcontainers/list";
    }

	@RequestMapping(params = { "find=BySubject", "form" }, method = RequestMethod.GET)
    public String findItxSubjectContainersBySubjectForm(Model uiModel) {
        uiModel.addAttribute("subjects", Subject.findAllSubjects());
        return "itxsubjectcontainers/findItxSubjectContainersBySubject";
    }

	@RequestMapping(params = "find=BySubject", method = RequestMethod.GET)
    public String findItxSubjectContainersBySubject(@RequestParam("subject") Subject subject, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxsubjectcontainers", ItxSubjectContainer.findItxSubjectContainersBySubject(subject, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ItxSubjectContainer.countFindItxSubjectContainersBySubject(subject) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxsubjectcontainers", ItxSubjectContainer.findItxSubjectContainersBySubject(subject, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxsubjectcontainers/list";
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ItxSubjectContainer itxSubjectContainer, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itxSubjectContainer);
            return "itxsubjectcontainers/create";
        }
        uiModel.asMap().clear();
        itxSubjectContainer.persist();
        return "redirect:/itxsubjectcontainers/" + encodeUrlPathSegment(itxSubjectContainer.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ItxSubjectContainer());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Subject.countSubjects() == 0) {
            dependencies.add(new String[] { "subject", "subjects" });
        }
        if (Container.countContainers() == 0) {
            dependencies.add(new String[] { "container", "containers" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "itxsubjectcontainers/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itxsubjectcontainer", ItxSubjectContainer.findItxSubjectContainer(id));
        uiModel.addAttribute("itemId", id);
        return "itxsubjectcontainers/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxsubjectcontainers", ItxSubjectContainer.findItxSubjectContainerEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ItxSubjectContainer.countItxSubjectContainers() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxsubjectcontainers", ItxSubjectContainer.findAllItxSubjectContainers(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxsubjectcontainers/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ItxSubjectContainer itxSubjectContainer, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itxSubjectContainer);
            return "itxsubjectcontainers/update";
        }
        uiModel.asMap().clear();
        itxSubjectContainer.merge();
        return "redirect:/itxsubjectcontainers/" + encodeUrlPathSegment(itxSubjectContainer.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ItxSubjectContainer.findItxSubjectContainer(id));
        return "itxsubjectcontainers/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ItxSubjectContainer itxSubjectContainer = ItxSubjectContainer.findItxSubjectContainer(id);
        itxSubjectContainer.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/itxsubjectcontainers";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("itxSubjectContainer_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("itxSubjectContainer_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, ItxSubjectContainer itxSubjectContainer) {
        uiModel.addAttribute("itxSubjectContainer", itxSubjectContainer);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("containers", Container.findAllContainers());
        uiModel.addAttribute("itxsubjectcontainerstates", ItxSubjectContainerState.findAllItxSubjectContainerStates());
        uiModel.addAttribute("subjects", Subject.findAllSubjects());
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
}
