package com.labsynch.labseer.web;
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
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RequestMapping("/parentaliases")
@Controller
@Transactional

public class ParentAliasController {

    
	

	@RequestMapping(params = { "find=ByAliasNameEquals", "form" }, method = RequestMethod.GET)
    public String findParentAliasesByAliasNameEqualsForm(Model uiModel) {
        return "parentaliases/findParentAliasesByAliasNameEquals";
    }

	@RequestMapping(params = "find=ByAliasNameEquals", method = RequestMethod.GET)
    public String findParentAliasesByAliasNameEquals(@RequestParam("aliasName") String aliasName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("parentaliases", ParentAlias.findParentAliasesByAliasNameEquals(aliasName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ParentAlias.countFindParentAliasesByAliasNameEquals(aliasName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("parentaliases", ParentAlias.findParentAliasesByAliasNameEquals(aliasName, sortFieldName, sortOrder).getResultList());
        }
        return "parentaliases/list";
    }

	@RequestMapping(params = { "find=ByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", "form" }, method = RequestMethod.GET)
    public String findParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEqualsForm(Model uiModel) {
        return "parentaliases/findParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals";
    }

	@RequestMapping(params = "find=ByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET)
    public String findParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(@RequestParam("aliasName") String aliasName, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("parentaliases", ParentAlias.findParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(aliasName, lsType, lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ParentAlias.countFindParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(aliasName, lsType, lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("parentaliases", ParentAlias.findParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(aliasName, lsType, lsKind, sortFieldName, sortOrder).getResultList());
        }
        return "parentaliases/list";
    }

	@RequestMapping(params = { "find=ByParent", "form" }, method = RequestMethod.GET)
    public String findParentAliasesByParentForm(Model uiModel) {
        uiModel.addAttribute("parents", Parent.findAllParents());
        return "parentaliases/findParentAliasesByParent";
    }

	@RequestMapping(params = "find=ByParent", method = RequestMethod.GET)
    public String findParentAliasesByParent(@RequestParam("parent") Parent parent, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("parentaliases", ParentAlias.findParentAliasesByParent(parent, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ParentAlias.countFindParentAliasesByParent(parent) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("parentaliases", ParentAlias.findParentAliasesByParent(parent, sortFieldName, sortOrder).getResultList());
        }
        return "parentaliases/list";
    }

	@RequestMapping(params = { "find=ByParentAndLsTypeEqualsAndLsKindEquals", "form" }, method = RequestMethod.GET)
    public String findParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsForm(Model uiModel) {
        uiModel.addAttribute("parents", Parent.findAllParents());
        return "parentaliases/findParentAliasesByParentAndLsTypeEqualsAndLsKindEquals";
    }

	@RequestMapping(params = "find=ByParentAndLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET)
    public String findParentAliasesByParentAndLsTypeEqualsAndLsKindEquals(@RequestParam("parent") Parent parent, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("parentaliases", ParentAlias.findParentAliasesByParentAndLsTypeEqualsAndLsKindEquals(parent, lsType, lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ParentAlias.countFindParentAliasesByParentAndLsTypeEqualsAndLsKindEquals(parent, lsType, lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("parentaliases", ParentAlias.findParentAliasesByParentAndLsTypeEqualsAndLsKindEquals(parent, lsType, lsKind, sortFieldName, sortOrder).getResultList());
        }
        return "parentaliases/list";
    }

	@RequestMapping(params = { "find=ByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals", "form" }, method = RequestMethod.GET)
    public String findParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEqualsForm(Model uiModel) {
        uiModel.addAttribute("parents", Parent.findAllParents());
        return "parentaliases/findParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals";
    }

	@RequestMapping(params = "find=ByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals", method = RequestMethod.GET)
    public String findParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals(@RequestParam("parent") Parent parent, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam("aliasName") String aliasName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("parentaliases", ParentAlias.findParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals(parent, lsType, lsKind, aliasName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ParentAlias.countFindParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals(parent, lsType, lsKind, aliasName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("parentaliases", ParentAlias.findParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals(parent, lsType, lsKind, aliasName, sortFieldName, sortOrder).getResultList());
        }
        return "parentaliases/list";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            ParentAlias parentAlias = ParentAlias.findParentAlias(id);
            if (parentAlias == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(parentAlias.toJson(), headers, HttpStatus.OK);
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
            List<ParentAlias> result = ParentAlias.findAllParentAliases();
            return new ResponseEntity<String>(ParentAlias.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ParentAlias parentAlias = ParentAlias.fromJsonToParentAlias(json);
            parentAlias.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+parentAlias.getId().toString()).build().toUriString());
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
            for (ParentAlias parentAlias: ParentAlias.fromJsonArrayToParentAliases(json)) {
                parentAlias.persist();
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
            ParentAlias parentAlias = ParentAlias.fromJsonToParentAlias(json);
            parentAlias.setId(id);
            if (parentAlias.merge() == null) {
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
            ParentAlias parentAlias = ParentAlias.findParentAlias(id);
            if (parentAlias == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            parentAlias.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByAliasNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindParentAliasesByAliasNameEquals(@RequestParam("aliasName") String aliasName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ParentAlias.toJsonArray(ParentAlias.findParentAliasesByAliasNameEquals(aliasName).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(@RequestParam("aliasName") String aliasName, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ParentAlias.toJsonArray(ParentAlias.findParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(aliasName, lsType, lsKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByParent", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindParentAliasesByParent(@RequestParam("parent") Parent parent) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ParentAlias.toJsonArray(ParentAlias.findParentAliasesByParent(parent).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByParentAndLsTypeEqualsAndLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindParentAliasesByParentAndLsTypeEqualsAndLsKindEquals(@RequestParam("parent") Parent parent, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ParentAlias.toJsonArray(ParentAlias.findParentAliasesByParentAndLsTypeEqualsAndLsKindEquals(parent, lsType, lsKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals(@RequestParam("parent") Parent parent, @RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam("aliasName") String aliasName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ParentAlias.toJsonArray(ParentAlias.findParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals(parent, lsType, lsKind, aliasName).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ParentAlias parentAlias, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, parentAlias);
            return "parentaliases/create";
        }
        uiModel.asMap().clear();
        parentAlias.persist();
        return "redirect:/parentaliases/" + encodeUrlPathSegment(parentAlias.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ParentAlias());
        return "parentaliases/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("parentalias", ParentAlias.findParentAlias(id));
        uiModel.addAttribute("itemId", id);
        return "parentaliases/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("parentaliases", ParentAlias.findParentAliasEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ParentAlias.countParentAliases() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("parentaliases", ParentAlias.findAllParentAliases(sortFieldName, sortOrder));
        }
        return "parentaliases/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ParentAlias parentAlias, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, parentAlias);
            return "parentaliases/update";
        }
        uiModel.asMap().clear();
        parentAlias.merge();
        return "redirect:/parentaliases/" + encodeUrlPathSegment(parentAlias.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ParentAlias.findParentAlias(id));
        return "parentaliases/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ParentAlias parentAlias = ParentAlias.findParentAlias(id);
        parentAlias.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/parentaliases";
    }

	void populateEditForm(Model uiModel, ParentAlias parentAlias) {
        uiModel.addAttribute("parentAlias", parentAlias);
        uiModel.addAttribute("parents", Parent.findAllParents());
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
