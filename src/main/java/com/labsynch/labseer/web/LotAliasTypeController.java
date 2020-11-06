package com.labsynch.labseer.web;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import com.labsynch.labseer.domain.LotAliasType;

@RequestMapping("/lotaliastypes")
@Controller
@Transactional

public class LotAliasTypeController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Kind", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        try {
            LotAliasType lotAliasType = LotAliasType.findLotAliasType(id);
            if (lotAliasType == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(lotAliasType.toJson(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Content-Kind", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        try {
            List<LotAliasType> result = LotAliasType.findAllLotAliasTypes();
            return new ResponseEntity<String>(LotAliasType.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Content-Kind", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        try {
            LotAliasType lotAliasType = LotAliasType.fromJsonToLotAliasType(json);
            lotAliasType.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location", uriBuilder.path(a.value()[0] + "/" + lotAliasType.getId().toString()).build().toUriString());
            return new ResponseEntity<String>(lotAliasType.toJson(), headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Content-Kind", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        Collection<LotAliasType> lotAliasTypes = new HashSet<LotAliasType>();
        try {
            for (LotAliasType lotAliasType : LotAliasType.fromJsonArrayToLotAliasTypes(json)) {
                lotAliasType.persist();
                lotAliasTypes.add(lotAliasType);
            }
            return new ResponseEntity<String>(LotAliasType.toJsonArray(lotAliasTypes), headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json, @PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Content-Kind", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        try {
            LotAliasType lotAliasType = LotAliasType.fromJsonToLotAliasType(json);
            lotAliasType.setId(id);
            if (lotAliasType.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(lotAliasType.toJson(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Content-Kind", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        try {
            LotAliasType lotAliasType = LotAliasType.findLotAliasType(id);
            if (lotAliasType == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            lotAliasType.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getIsotopeOptions() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Kind", "application/json");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LotAliasType lotAliasType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lotAliasType);
            return "lotaliastypes/create";
        }
        uiModel.asMap().clear();
        lotAliasType.persist();
        return "redirect:/lotaliastypes/" + encodeUrlPathSegment(lotAliasType.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LotAliasType());
        return "lotaliastypes/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("lotaliastype", LotAliasType.findLotAliasType(id));
        uiModel.addAttribute("itemId", id);
        return "lotaliastypes/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lotaliastypes", LotAliasType.findLotAliasTypeEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LotAliasType.countLotAliasTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lotaliastypes", LotAliasType.findAllLotAliasTypes(sortFieldName, sortOrder));
        }
        return "lotaliastypes/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LotAliasType lotAliasType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lotAliasType);
            return "lotaliastypes/update";
        }
        uiModel.asMap().clear();
        lotAliasType.merge();
        return "redirect:/lotaliastypes/" + encodeUrlPathSegment(lotAliasType.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LotAliasType.findLotAliasType(id));
        return "lotaliastypes/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LotAliasType lotAliasType = LotAliasType.findLotAliasType(id);
        lotAliasType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lotaliastypes";
    }

	void populateEditForm(Model uiModel, LotAliasType lotAliasType) {
        uiModel.addAttribute("lotAliasType", lotAliasType);
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
