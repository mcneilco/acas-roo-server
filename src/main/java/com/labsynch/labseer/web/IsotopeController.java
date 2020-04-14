package com.labsynch.labseer.web;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.labsynch.labseer.domain.Isotope;
import com.labsynch.labseer.service.ErrorList;
import com.labsynch.labseer.service.ErrorMessage;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.path.PathBuilder;

@RooWebScaffold(path = "isotopes", formBackingObject = Isotope.class)
@RequestMapping("/isotopes")
@Transactional
@Controller

@RooWebFinder
public class IsotopeController {

    private static final Logger logger = LoggerFactory.getLogger(IsotopeController.class);

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid Isotope isotope, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("isotope", isotope);
            return "isotopes/create";
        }
        uiModel.asMap().clear();
        isotope.persist();
        return "redirect:/isotopes/" + encodeUrlPathSegment(isotope.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("isotope", new Isotope());
        return "isotopes/create";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("isotope", Isotope.findIsotope(id));
        uiModel.addAttribute("itemId", id);
        return "isotopes/show";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("isotopes", Isotope.findIsotopeEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Isotope.countIsotopes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("isotopes", Isotope.findAllIsotopes());
        }
        return "isotopes/list";
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid Isotope isotope, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("isotope", isotope);
            return "isotopes/update";
        }
        uiModel.asMap().clear();
        isotope.merge();
        return "redirect:/isotopes/" + encodeUrlPathSegment(isotope.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("isotope", Isotope.findIsotope(id));
        return "isotopes/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Isotope.findIsotope(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/isotopes";
    }

    @ModelAttribute("isotopes")
    public Collection<Isotope> populateIsotopes() {
        return Isotope.findAllIsotopes();
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        Isotope isotope = Isotope.findIsotope(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        if (isotope == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(isotope.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        return new ResponseEntity<String>(Isotope.toJsonArray(Isotope.findAllIsotopes()), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        ErrorList errorList = new ErrorList();
        Isotope isotope = Isotope.fromJsonToIsotope(json);
        isotope.setAbbrev(isotope.getAbbrev().trim());
        isotope.setName(isotope.getName().trim());
        boolean validIsotope = true;
        List<Isotope> isotopesByName = Isotope.findIsotopesByNameEquals(isotope.getName()).getResultList();
        if (isotopesByName.size() > 0) {
            logger.error("Number of istopes found: " + isotopesByName.size());
            validIsotope = false;
            ErrorMessage error = new ErrorMessage();
            error.setLevel("error");
            error.setMessage("Duplicate isotope name. Another isotope exist with the same name.");
            errors.add(error);
        }
        List<Isotope> isotopesByAbbrev = Isotope.findIsotopesByAbbrevEquals(isotope.getAbbrev()).getResultList();
        if (isotopesByAbbrev.size() > 0) {
            logger.error("Number of istopes found: " + isotopesByAbbrev.size());
            validIsotope = false;
            ErrorMessage error = new ErrorMessage();
            error.setLevel("error");
            error.setMessage("Duplicate isotope abbreviation. Another isotope exist with the same abbreviation.");
            errors.add(error);
        }
        if (validIsotope) {
            isotope.persist();
            return showJson(isotope.getId());
        } else {
            ErrorMessage error = new ErrorMessage();
            String errorMessage = "Duplicate isotope found. Please select existing isotope.";
            error.setLevel("warning");
            error.setMessage(errorMessage);
            errors.add(error);
            errorList.setErrors(errors);
            logger.debug(errorList.toJson());
            return new ResponseEntity<String>(errorList.toJson(), headers, HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (Isotope isotope : Isotope.fromJsonArrayToIsotopes(json)) {
            isotope.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        if (Isotope.fromJsonToIsotope(json).merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        for (Isotope isotope : Isotope.fromJsonArrayToIsotopes(json)) {
            if (isotope.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        Isotope isotope = Isotope.findIsotope(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        if (isotope == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        isotope.remove();
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

}
