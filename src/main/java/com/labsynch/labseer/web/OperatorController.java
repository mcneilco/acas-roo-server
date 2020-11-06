package com.labsynch.labseer.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
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
import com.labsynch.labseer.domain.Operator;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RooWebScaffold(path = "operators", formBackingObject = Operator.class)
@RequestMapping("/operators")
@Transactional
@Controller

@RooWebFinder
public class OperatorController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        Operator operator = Operator.findOperator(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        if (operator == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(operator.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        return new ResponseEntity<String>(Operator.toJsonArray(Operator.findAllOperators()), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        Operator.fromJsonToOperator(json).persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (Operator operator : Operator.fromJsonArrayToOperators(json)) {
            operator.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        if (Operator.fromJsonToOperator(json).merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        for (Operator operator : Operator.fromJsonArrayToOperators(json)) {
            if (operator.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        Operator operator = Operator.findOperator(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        if (operator == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        operator.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getIsotopeOptions() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(params = { "find=ByCodeEquals", "form" }, method = RequestMethod.GET)
    public String findOperatorsByCodeEqualsForm(Model uiModel) {
        return "operators/findOperatorsByCodeEquals";
    }

	@RequestMapping(params = "find=ByCodeEquals", method = RequestMethod.GET)
    public String findOperatorsByCodeEquals(@RequestParam("code") String code, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("operators", Operator.findOperatorsByCodeEquals(code, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Operator.countFindOperatorsByCodeEquals(code) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("operators", Operator.findOperatorsByCodeEquals(code, sortFieldName, sortOrder).getResultList());
        }
        return "operators/list";
    }

	@RequestMapping(params = { "find=ByNameEquals", "form" }, method = RequestMethod.GET)
    public String findOperatorsByNameEqualsForm(Model uiModel) {
        return "operators/findOperatorsByNameEquals";
    }

	@RequestMapping(params = "find=ByNameEquals", method = RequestMethod.GET)
    public String findOperatorsByNameEquals(@RequestParam("name") String name, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("operators", Operator.findOperatorsByNameEquals(name, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Operator.countFindOperatorsByNameEquals(name) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("operators", Operator.findOperatorsByNameEquals(name, sortFieldName, sortOrder).getResultList());
        }
        return "operators/list";
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Operator operator, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, operator);
            return "operators/create";
        }
        uiModel.asMap().clear();
        operator.persist();
        return "redirect:/operators/" + encodeUrlPathSegment(operator.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Operator());
        return "operators/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("operator", Operator.findOperator(id));
        uiModel.addAttribute("itemId", id);
        return "operators/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("operators", Operator.findOperatorEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) Operator.countOperators() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("operators", Operator.findAllOperators(sortFieldName, sortOrder));
        }
        return "operators/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Operator operator, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, operator);
            return "operators/update";
        }
        uiModel.asMap().clear();
        operator.merge();
        return "redirect:/operators/" + encodeUrlPathSegment(operator.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Operator.findOperator(id));
        return "operators/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Operator operator = Operator.findOperator(id);
        operator.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/operators";
    }

	void populateEditForm(Model uiModel, Operator operator) {
        uiModel.addAttribute("operator", operator);
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
