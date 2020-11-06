package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ItxExperimentExperiment;
import com.labsynch.labseer.domain.ItxExperimentExperimentState;
import com.labsynch.labseer.domain.ThingPage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
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

@RooWebJson(jsonObject = ItxExperimentExperiment.class)
@Controller
@RequestMapping("/itxexperimentexperiments")
@RooWebScaffold(path = "itxexperimentexperiments", formBackingObject = ItxExperimentExperiment.class)
@RooWebFinder
public class ItxExperimentExperimentController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ItxExperimentExperiment itxExperimentExperiment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itxExperimentExperiment);
            return "itxexperimentexperiments/create";
        }
        uiModel.asMap().clear();
        itxExperimentExperiment.persist();
        return "redirect:/itxexperimentexperiments/" + encodeUrlPathSegment(itxExperimentExperiment.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ItxExperimentExperiment());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Experiment.countExperiments() == 0) {
            dependencies.add(new String[] { "firstExperiment", "experiments" });
        }
        if (Experiment.countExperiments() == 0) {
            dependencies.add(new String[] { "secondExperiment", "experiments" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "itxexperimentexperiments/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("itxexperimentexperiment", ItxExperimentExperiment.findItxExperimentExperiment(id));
        uiModel.addAttribute("itemId", id);
        return "itxexperimentexperiments/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxexperimentexperiments", ItxExperimentExperiment.findItxExperimentExperimentEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ItxExperimentExperiment.countItxExperimentExperiments() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxexperimentexperiments", ItxExperimentExperiment.findAllItxExperimentExperiments(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxexperimentexperiments/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ItxExperimentExperiment itxExperimentExperiment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, itxExperimentExperiment);
            return "itxexperimentexperiments/update";
        }
        uiModel.asMap().clear();
        itxExperimentExperiment.merge();
        return "redirect:/itxexperimentexperiments/" + encodeUrlPathSegment(itxExperimentExperiment.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ItxExperimentExperiment.findItxExperimentExperiment(id));
        return "itxexperimentexperiments/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ItxExperimentExperiment itxExperimentExperiment = ItxExperimentExperiment.findItxExperimentExperiment(id);
        itxExperimentExperiment.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/itxexperimentexperiments";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("itxExperimentExperiment_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
        uiModel.addAttribute("itxExperimentExperiment_modifieddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, ItxExperimentExperiment itxExperimentExperiment) {
        uiModel.addAttribute("itxExperimentExperiment", itxExperimentExperiment);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("experiments", Experiment.findAllExperiments());
        uiModel.addAttribute("itxexperimentexperimentstates", ItxExperimentExperimentState.findAllItxExperimentExperimentStates());
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

	@RequestMapping(params = { "find=ByFirstExperiment", "form" }, method = RequestMethod.GET)
    public String findItxExperimentExperimentsByFirstExperimentForm(Model uiModel) {
        uiModel.addAttribute("experiments", Experiment.findAllExperiments());
        return "itxexperimentexperiments/findItxExperimentExperimentsByFirstExperiment";
    }

	@RequestMapping(params = "find=ByFirstExperiment", method = RequestMethod.GET)
    public String findItxExperimentExperimentsByFirstExperiment(@RequestParam("firstExperiment") Experiment firstExperiment, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxexperimentexperiments", ItxExperimentExperiment.findItxExperimentExperimentsByFirstExperiment(firstExperiment, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ItxExperimentExperiment.countFindItxExperimentExperimentsByFirstExperiment(firstExperiment) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxexperimentexperiments", ItxExperimentExperiment.findItxExperimentExperimentsByFirstExperiment(firstExperiment, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxexperimentexperiments/list";
    }

	@RequestMapping(params = { "find=ByLsTransactionEquals", "form" }, method = RequestMethod.GET)
    public String findItxExperimentExperimentsByLsTransactionEqualsForm(Model uiModel) {
        return "itxexperimentexperiments/findItxExperimentExperimentsByLsTransactionEquals";
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET)
    public String findItxExperimentExperimentsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxexperimentexperiments", ItxExperimentExperiment.findItxExperimentExperimentsByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ItxExperimentExperiment.countFindItxExperimentExperimentsByLsTransactionEquals(lsTransaction) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxexperimentexperiments", ItxExperimentExperiment.findItxExperimentExperimentsByLsTransactionEquals(lsTransaction, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxexperimentexperiments/list";
    }

	@RequestMapping(params = { "find=BySecondExperiment", "form" }, method = RequestMethod.GET)
    public String findItxExperimentExperimentsBySecondExperimentForm(Model uiModel) {
        uiModel.addAttribute("experiments", Experiment.findAllExperiments());
        return "itxexperimentexperiments/findItxExperimentExperimentsBySecondExperiment";
    }

	@RequestMapping(params = "find=BySecondExperiment", method = RequestMethod.GET)
    public String findItxExperimentExperimentsBySecondExperiment(@RequestParam("secondExperiment") Experiment secondExperiment, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("itxexperimentexperiments", ItxExperimentExperiment.findItxExperimentExperimentsBySecondExperiment(secondExperiment, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ItxExperimentExperiment.countFindItxExperimentExperimentsBySecondExperiment(secondExperiment) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("itxexperimentexperiments", ItxExperimentExperiment.findItxExperimentExperimentsBySecondExperiment(secondExperiment, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "itxexperimentexperiments/list";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            ItxExperimentExperiment itxExperimentExperiment = ItxExperimentExperiment.findItxExperimentExperiment(id);
            if (itxExperimentExperiment == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(itxExperimentExperiment.toJson(), headers, HttpStatus.OK);
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
            List<ItxExperimentExperiment> result = ItxExperimentExperiment.findAllItxExperimentExperiments();
            return new ResponseEntity<String>(ItxExperimentExperiment.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ItxExperimentExperiment itxExperimentExperiment = ItxExperimentExperiment.fromJsonToItxExperimentExperiment(json);
            itxExperimentExperiment.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+itxExperimentExperiment.getId().toString()).build().toUriString());
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
            for (ItxExperimentExperiment itxExperimentExperiment: ItxExperimentExperiment.fromJsonArrayToItxExperimentExperiments(json)) {
                itxExperimentExperiment.persist();
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
            ItxExperimentExperiment itxExperimentExperiment = ItxExperimentExperiment.fromJsonToItxExperimentExperiment(json);
            itxExperimentExperiment.setId(id);
            if (itxExperimentExperiment.merge() == null) {
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
            ItxExperimentExperiment itxExperimentExperiment = ItxExperimentExperiment.findItxExperimentExperiment(id);
            if (itxExperimentExperiment == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            itxExperimentExperiment.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByFirstExperiment", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindItxExperimentExperimentsByFirstExperiment(@RequestParam("firstExperiment") Experiment firstExperiment) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ItxExperimentExperiment.toJsonArray(ItxExperimentExperiment.findItxExperimentExperimentsByFirstExperiment(firstExperiment).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindItxExperimentExperimentsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ItxExperimentExperiment.toJsonArray(ItxExperimentExperiment.findItxExperimentExperimentsByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=BySecondExperiment", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindItxExperimentExperimentsBySecondExperiment(@RequestParam("secondExperiment") Experiment secondExperiment) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ItxExperimentExperiment.toJsonArray(ItxExperimentExperiment.findItxExperimentExperimentsBySecondExperiment(secondExperiment).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
