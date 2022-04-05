package com.labsynch.labseer.web;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Isotope;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.domain.SaltForm;

@RooWebScaffold(path = "isosalts", formBackingObject = IsoSalt.class)
@RequestMapping("/isosalts")
@Transactional
@Controller

@RooWebFinder
public class IsoSaltController {

	
	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid IsoSalt isoSalt, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("isoSalt", isoSalt);
            return "isosalts/create";
        }
        uiModel.asMap().clear();
        isoSalt.persist();
        return "redirect:/isosalts/" + encodeUrlPathSegment(isoSalt.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        uiModel.addAttribute("isoSalt", new IsoSalt());
        return "isosalts/create";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("isosalt", IsoSalt.findIsoSalt(id));
        uiModel.addAttribute("itemId", id);
        return "isosalts/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("isosalts", IsoSalt.findIsoSaltEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) IsoSalt.countIsoSalts() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("isosalts", IsoSalt.findIsoSaltEntries(0, 100));
        }
        return "isosalts/list";
    }

	@RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid IsoSalt isoSalt, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("isoSalt", isoSalt);
            return "isosalts/update";
        }
        uiModel.asMap().clear();
        isoSalt.merge();
        return "redirect:/isosalts/" + encodeUrlPathSegment(isoSalt.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("isoSalt", IsoSalt.findIsoSalt(id));
        return "isosalts/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        IsoSalt.findIsoSalt(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/isosalts";
    }

	
//	@ModelAttribute("isosalts")
//    public Collection<IsoSalt> populateIsoSalts() {
//        return IsoSalt.findAllIsoSalts();
//    }

	@ModelAttribute("isotopes")
    public Collection<Isotope> populateIsotopes() {
        return Isotope.findAllIsotopes();
    }

	@ModelAttribute("salts")
    public Collection<Salt> populateSalts() {
        return Salt.findAllSalts();
    }

//	@ModelAttribute("saltforms")
//    public Collection<SaltForm> populateSaltForms() {
//        return SaltForm.findAllSaltForms();
//    }

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
        IsoSalt isosalt = IsoSalt.findIsoSalt(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        if (isosalt == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(isosalt.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/text; charset=utf-8");
        return new ResponseEntity<String>(IsoSalt.toJsonArray(IsoSalt.findAllIsoSalts()), headers, HttpStatus.OK);
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        IsoSalt.fromJsonToIsoSalt(json).persist();
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        for (IsoSalt isoSalt: IsoSalt.fromJsonArrayToIsoSalts(json)) {
            isoSalt.persist();
        }
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

	@RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        if (IsoSalt.fromJsonToIsoSalt(json).merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        for (IsoSalt isoSalt: IsoSalt.fromJsonArrayToIsoSalts(json)) {
            if (isoSalt.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        IsoSalt isosalt = IsoSalt.findIsoSalt(id);
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/text");
        if (isosalt == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        isosalt.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }



	@RequestMapping(params = { "find=BySaltForm", "form" }, method = RequestMethod.GET)
    public String findIsoSaltsBySaltFormForm(Model uiModel) {
        uiModel.addAttribute("saltforms", SaltForm.findAllSaltForms());
        return "isosalts/findIsoSaltsBySaltForm";
    }


	@RequestMapping(params = { "find=BySaltFormAndType", "form" }, method = RequestMethod.GET)
    public String findIsoSaltsBySaltFormAndTypeForm(Model uiModel) {
        uiModel.addAttribute("saltforms", SaltForm.findAllSaltForms());
        return "isosalts/findIsoSaltsBySaltFormAndType";
    }
	

}
