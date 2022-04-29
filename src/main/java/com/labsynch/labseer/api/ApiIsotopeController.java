package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.labsynch.labseer.domain.Isotope;
import com.labsynch.labseer.service.ErrorList;
import com.labsynch.labseer.service.ErrorMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

@RequestMapping(value = {"/api/v1/isotopes"})
@Controller
public class ApiIsotopeController {
	
    private static final Logger logger = LoggerFactory.getLogger(ApiIsotopeController.class);

    @ModelAttribute("isotopes")
    public Collection<Isotope> populateIsotopes() {
        return Isotope.findAllIsotopes();
    }

    String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
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
