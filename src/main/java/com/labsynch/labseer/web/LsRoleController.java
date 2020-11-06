package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.LsRole;
import com.labsynch.labseer.domain.RoleKind;
import com.labsynch.labseer.domain.RoleType;
import com.labsynch.labseer.service.AnalysisGroupServiceImpl;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RequestMapping("/lsroles")
@Controller
public class LsRoleController {

    private static final Logger logger = LoggerFactory.getLogger(LsRoleController.class);

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LsRole lsRole, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsRole);
            return "lsroles/create";
        }
        uiModel.asMap().clear();
        lsRole.persist();
        return "redirect:/lsroles/" + encodeUrlPathSegment(lsRole.getId().toString(), httpServletRequest);
    }

    @RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LsRole());
        uiModel.addAttribute("lstypes", RoleType.findAllRoleTypes());
        uiModel.addAttribute("lskinds", RoleKind.findAllRoleKinds());
        return "lsroles/create";
    }

    @RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("lsrole", LsRole.findLsRole(id));
        uiModel.addAttribute("itemId", id);
        return "lsroles/show";
    }

    @RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsroles", LsRole.findLsRoleEntries(firstResult, sizeNo));
            float nrOfPages = (float) LsRole.countLsRoles() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsroles", LsRole.findAllLsRoles());
        }
        return "lsroles/list";
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LsRole lsRole, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsRole);
            return "lsroles/update";
        }
        uiModel.asMap().clear();
        lsRole.merge();
        return "redirect:/lsroles/" + encodeUrlPathSegment(lsRole.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LsRole.findLsRole(id));
        return "lsroles/update";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LsRole lsRole = LsRole.findLsRole(id);
        lsRole.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lsroles";
    }

    void populateEditForm(Model uiModel, LsRole lsRole) {
        uiModel.addAttribute("lsRole", lsRole);
        uiModel.addAttribute("authorroles", AuthorRole.findAllAuthorRoles());
        uiModel.addAttribute("collectiontypes", RoleType.findAllRoleTypes());
        uiModel.addAttribute("collectionkinds", RoleKind.findAllRoleKinds());
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

	@RequestMapping(params = { "find=ByLsKindEquals", "form" }, method = RequestMethod.GET)
    public String findLsRolesByLsKindEqualsForm(Model uiModel) {
        return "lsroles/findLsRolesByLsKindEquals";
    }

	@RequestMapping(params = "find=ByLsKindEquals", method = RequestMethod.GET)
    public String findLsRolesByLsKindEquals(@RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsroles", LsRole.findLsRolesByLsKindEquals(lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsRole.countFindLsRolesByLsKindEquals(lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsroles", LsRole.findLsRolesByLsKindEquals(lsKind, sortFieldName, sortOrder).getResultList());
        }
        return "lsroles/list";
    }

	@RequestMapping(params = { "find=ByLsTypeEquals", "form" }, method = RequestMethod.GET)
    public String findLsRolesByLsTypeEqualsForm(Model uiModel) {
        return "lsroles/findLsRolesByLsTypeEquals";
    }

	@RequestMapping(params = "find=ByLsTypeEquals", method = RequestMethod.GET)
    public String findLsRolesByLsTypeEquals(@RequestParam("lsType") String lsType, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsroles", LsRole.findLsRolesByLsTypeEquals(lsType, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsRole.countFindLsRolesByLsTypeEquals(lsType) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsroles", LsRole.findLsRolesByLsTypeEquals(lsType, sortFieldName, sortOrder).getResultList());
        }
        return "lsroles/list";
    }

	@RequestMapping(params = { "find=ByLsTypeEqualsAndLsKindEquals", "form" }, method = RequestMethod.GET)
    public String findLsRolesByLsTypeEqualsAndLsKindEqualsForm(Model uiModel) {
        return "lsroles/findLsRolesByLsTypeEqualsAndLsKindEquals";
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET)
    public String findLsRolesByLsTypeEqualsAndLsKindEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsroles", LsRole.findLsRolesByLsTypeEqualsAndLsKindEquals(lsType, lsKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsRole.countFindLsRolesByLsTypeEqualsAndLsKindEquals(lsType, lsKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsroles", LsRole.findLsRolesByLsTypeEqualsAndLsKindEquals(lsType, lsKind, sortFieldName, sortOrder).getResultList());
        }
        return "lsroles/list";
    }

	@RequestMapping(params = { "find=ByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals", "form" }, method = RequestMethod.GET)
    public String findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEqualsForm(Model uiModel) {
        return "lsroles/findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals";
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals", method = RequestMethod.GET)
    public String findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam("roleName") String roleName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsroles", LsRole.findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals(lsType, lsKind, roleName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsRole.countFindLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals(lsType, lsKind, roleName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsroles", LsRole.findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals(lsType, lsKind, roleName, sortFieldName, sortOrder).getResultList());
        }
        return "lsroles/list";
    }

	@RequestMapping(params = { "find=ByLsTypeEqualsAndRoleNameEquals", "form" }, method = RequestMethod.GET)
    public String findLsRolesByLsTypeEqualsAndRoleNameEqualsForm(Model uiModel) {
        return "lsroles/findLsRolesByLsTypeEqualsAndRoleNameEquals";
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndRoleNameEquals", method = RequestMethod.GET)
    public String findLsRolesByLsTypeEqualsAndRoleNameEquals(@RequestParam("lsType") String lsType, @RequestParam("roleName") String roleName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsroles", LsRole.findLsRolesByLsTypeEqualsAndRoleNameEquals(lsType, roleName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsRole.countFindLsRolesByLsTypeEqualsAndRoleNameEquals(lsType, roleName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsroles", LsRole.findLsRolesByLsTypeEqualsAndRoleNameEquals(lsType, roleName, sortFieldName, sortOrder).getResultList());
        }
        return "lsroles/list";
    }

	@RequestMapping(params = { "find=ByRoleNameEquals", "form" }, method = RequestMethod.GET)
    public String findLsRolesByRoleNameEqualsForm(Model uiModel) {
        return "lsroles/findLsRolesByRoleNameEquals";
    }

	@RequestMapping(params = "find=ByRoleNameEquals", method = RequestMethod.GET)
    public String findLsRolesByRoleNameEquals(@RequestParam("roleName") String roleName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsroles", LsRole.findLsRolesByRoleNameEquals(roleName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LsRole.countFindLsRolesByRoleNameEquals(roleName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsroles", LsRole.findLsRolesByRoleNameEquals(roleName, sortFieldName, sortOrder).getResultList());
        }
        return "lsroles/list";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            LsRole lsRole = LsRole.findLsRole(id);
            if (lsRole == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(lsRole.toJson(), headers, HttpStatus.OK);
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
            List<LsRole> result = LsRole.findAllLsRoles();
            return new ResponseEntity<String>(LsRole.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            LsRole lsRole = LsRole.fromJsonToLsRole(json);
            lsRole.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+lsRole.getId().toString()).build().toUriString());
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
            for (LsRole lsRole: LsRole.fromJsonArrayToLsRoles(json)) {
                lsRole.persist();
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
            LsRole lsRole = LsRole.fromJsonToLsRole(json);
            lsRole.setId(id);
            if (lsRole.merge() == null) {
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
            LsRole lsRole = LsRole.findLsRole(id);
            if (lsRole == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            lsRole.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsRolesByLsKindEquals(@RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsRole.toJsonArray(LsRole.findLsRolesByLsKindEquals(lsKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsRolesByLsTypeEquals(@RequestParam("lsType") String lsType) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsRole.toJsonArray(LsRole.findLsRolesByLsTypeEquals(lsType).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsRolesByLsTypeEqualsAndLsKindEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsRole.toJsonArray(LsRole.findLsRolesByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind, @RequestParam("roleName") String roleName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsRole.toJsonArray(LsRole.findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals(lsType, lsKind, roleName).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndRoleNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsRolesByLsTypeEqualsAndRoleNameEquals(@RequestParam("lsType") String lsType, @RequestParam("roleName") String roleName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsRole.toJsonArray(LsRole.findLsRolesByLsTypeEqualsAndRoleNameEquals(lsType, roleName).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByRoleNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLsRolesByRoleNameEquals(@RequestParam("roleName") String roleName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LsRole.toJsonArray(LsRole.findLsRolesByRoleNameEquals(roleName).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
