package com.labsynch.labseer.web;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.LotAlias;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RequestMapping("/lotaliases")
@Controller
@RooWebScaffold(path = "lotaliases", formBackingObject = LotAlias.class)

@RooWebJson(jsonObject = LotAlias.class)
@RooWebFinder
public class LotAliasController {
	


	@RequestMapping(params = { "find=ByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", "form" }, method = RequestMethod.GET)
    public String findLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEqualsForm(Model uiModel) {
        return "lotaliases/findLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals";
    }

	@RequestMapping(params = "find=ByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET)
    public String findLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(@RequestParam("aliasName") String aliasName, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lotaliases", LotAlias.findLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(aliasName, lsType, lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LotAlias.countFindLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(aliasName, lsType, lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lotaliases", LotAlias.findLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(aliasName, lsType, lsKind, sortFieldName, sortOrder).getResultList());
        }
        return "lotaliases/list";
    }

	@RequestMapping(params = { "find=ByLot", "form" }, method = RequestMethod.GET)
    public String findLotAliasesByLotForm(Model uiModel) {
        uiModel.addAttribute("lots", Lot.findAllLots());
        return "lotaliases/findLotAliasesByLot";
    }

	@RequestMapping(params = "find=ByLot", method = RequestMethod.GET)
    public String findLotAliasesByLot(@RequestParam("lot") Lot lot, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lotaliases", LotAlias.findLotAliasesByLot(lot, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LotAlias.countFindLotAliasesByLot(lot) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lotaliases", LotAlias.findLotAliasesByLot(lot, sortFieldName, sortOrder).getResultList());
        }
        return "lotaliases/list";
    }

	@RequestMapping(params = { "find=ByLotAndLsTypeEqualsAndLsKindEquals", "form" }, method = RequestMethod.GET)
    public String findLotAliasesByLotAndLsTypeEqualsAndLsKindEqualsForm(Model uiModel) {
        uiModel.addAttribute("lots", Lot.findAllLots());
        return "lotaliases/findLotAliasesByLotAndLsTypeEqualsAndLsKindEquals";
    }

	@RequestMapping(params = "find=ByLotAndLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET)
    public String findLotAliasesByLotAndLsTypeEqualsAndLsKindEquals(@RequestParam("lot") Lot lot, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lotaliases", LotAlias.findLotAliasesByLotAndLsTypeEqualsAndLsKindEquals(lot, lsType, lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LotAlias.countFindLotAliasesByLotAndLsTypeEqualsAndLsKindEquals(lot, lsType, lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lotaliases", LotAlias.findLotAliasesByLotAndLsTypeEqualsAndLsKindEquals(lot, lsType, lsKind, sortFieldName, sortOrder).getResultList());
        }
        return "lotaliases/list";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            LotAlias lotAlias = LotAlias.findLotAlias(id);
            if (lotAlias == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(lotAlias.toJson(), headers, HttpStatus.OK);
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
            List<LotAlias> result = LotAlias.findAllLotAliases();
            return new ResponseEntity<String>(LotAlias.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            LotAlias lotAlias = LotAlias.fromJsonToLotAlias(json);
            lotAlias.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+lotAlias.getId().toString()).build().toUriString());
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
            for (LotAlias lotAlias: LotAlias.fromJsonArrayToLotAliases(json)) {
                lotAlias.persist();
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
            LotAlias lotAlias = LotAlias.fromJsonToLotAlias(json);
            lotAlias.setId(id);
            if (lotAlias.merge() == null) {
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
            LotAlias lotAlias = LotAlias.findLotAlias(id);
            if (lotAlias == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            lotAlias.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(@RequestParam("aliasName") String aliasName, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LotAlias.toJsonArray(LotAlias.findLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(aliasName, lsType, lsKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLotAliasesByLot(@RequestParam("lot") Lot lot) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LotAlias.toJsonArray(LotAlias.findLotAliasesByLot(lot).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLotAndLsTypeEqualsAndLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLotAliasesByLotAndLsTypeEqualsAndLsKindEquals(@RequestParam("lot") Lot lot, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LotAlias.toJsonArray(LotAlias.findLotAliasesByLotAndLsTypeEqualsAndLsKindEquals(lot, lsType, lsKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LotAlias lotAlias, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lotAlias);
            return "lotaliases/create";
        }
        uiModel.asMap().clear();
        lotAlias.persist();
        return "redirect:/lotaliases/" + encodeUrlPathSegment(lotAlias.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LotAlias());
        return "lotaliases/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("lotalias", LotAlias.findLotAlias(id));
        uiModel.addAttribute("itemId", id);
        return "lotaliases/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lotaliases", LotAlias.findLotAliasEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LotAlias.countLotAliases() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lotaliases", LotAlias.findAllLotAliases(sortFieldName, sortOrder));
        }
        return "lotaliases/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LotAlias lotAlias, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lotAlias);
            return "lotaliases/update";
        }
        uiModel.asMap().clear();
        lotAlias.merge();
        return "redirect:/lotaliases/" + encodeUrlPathSegment(lotAlias.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LotAlias.findLotAlias(id));
        return "lotaliases/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LotAlias lotAlias = LotAlias.findLotAlias(id);
        lotAlias.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lotaliases";
    }

	void populateEditForm(Model uiModel, LotAlias lotAlias) {
        uiModel.addAttribute("lotAlias", lotAlias);
        uiModel.addAttribute("lots", Lot.findAllLots());
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
