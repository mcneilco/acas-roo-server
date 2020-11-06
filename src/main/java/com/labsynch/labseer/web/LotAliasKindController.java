package com.labsynch.labseer.web;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import com.labsynch.labseer.domain.LotAliasKind;
import com.labsynch.labseer.domain.LotAliasType;

@RequestMapping("/lotaliaskinds")
@Controller
@Transactional

public class LotAliasKindController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Content-Kind", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        try {
            LotAliasKind lotAliasKind = LotAliasKind.findLotAliasKind(id);
            if (lotAliasKind == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(lotAliasKind.toJson(), headers, HttpStatus.OK);
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
        try {
            List<LotAliasKind> result = LotAliasKind.findAllLotAliasKinds();
            return new ResponseEntity<String>(LotAliasKind.toJsonArray(result), headers, HttpStatus.OK);
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
            LotAliasKind lotAliasKind = LotAliasKind.fromJsonToLotAliasKind(json);
            lotAliasKind.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location", uriBuilder.path(a.value()[0] + "/" + lotAliasKind.getId().toString()).build().toUriString());
            return new ResponseEntity<String>(lotAliasKind.toJson(), headers, HttpStatus.CREATED);
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
        Collection<LotAliasKind> lotAliasKinds = new HashSet<LotAliasKind>();
        try {
            for (LotAliasKind lotAliasKind : LotAliasKind.fromJsonArrayToLotAliasKinds(json)) {
                lotAliasKind.persist();
                lotAliasKinds.add(lotAliasKind);
            }
            return new ResponseEntity<String>(LotAliasKind.toJsonArray(lotAliasKinds), headers, HttpStatus.CREATED);
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
            LotAliasKind lotAliasKind = LotAliasKind.fromJsonToLotAliasKind(json);
            lotAliasKind.setId(id);
            if (lotAliasKind.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(lotAliasKind.toJson(), headers, HttpStatus.OK);
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
            LotAliasKind lotAliasKind = LotAliasKind.findLotAliasKind(id);
            if (lotAliasKind == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            lotAliasKind.remove();
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
    public String create(@Valid LotAliasKind lotAliasKind, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lotAliasKind);
            return "lotaliaskinds/create";
        }
        uiModel.asMap().clear();
        lotAliasKind.persist();
        return "redirect:/lotaliaskinds/" + encodeUrlPathSegment(lotAliasKind.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LotAliasKind());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (LotAliasType.countLotAliasTypes() == 0) {
            dependencies.add(new String[] { "lsType", "lotaliastypes" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "lotaliaskinds/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("lotaliaskind", LotAliasKind.findLotAliasKind(id));
        uiModel.addAttribute("itemId", id);
        return "lotaliaskinds/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lotaliaskinds", LotAliasKind.findLotAliasKindEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LotAliasKind.countLotAliasKinds() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lotaliaskinds", LotAliasKind.findAllLotAliasKinds(sortFieldName, sortOrder));
        }
        return "lotaliaskinds/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LotAliasKind lotAliasKind, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lotAliasKind);
            return "lotaliaskinds/update";
        }
        uiModel.asMap().clear();
        lotAliasKind.merge();
        return "redirect:/lotaliaskinds/" + encodeUrlPathSegment(lotAliasKind.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LotAliasKind.findLotAliasKind(id));
        return "lotaliaskinds/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LotAliasKind lotAliasKind = LotAliasKind.findLotAliasKind(id);
        lotAliasKind.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lotaliaskinds";
    }

	void populateEditForm(Model uiModel, LotAliasKind lotAliasKind) {
        uiModel.addAttribute("lotAliasKind", lotAliasKind);
        uiModel.addAttribute("lotaliastypes", LotAliasType.findAllLotAliasTypes());
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
