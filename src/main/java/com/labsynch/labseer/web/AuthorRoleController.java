package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.LsRole;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RooWebJson(jsonObject = AuthorRole.class)
@RooWebFinder
@RequestMapping("/authorroles")
@Controller
@RooWebScaffold(path = "authorroles", formBackingObject = AuthorRole.class)
public class AuthorRoleController {

    private static final Logger logger = LoggerFactory.getLogger(AuthorRoleController.class);

	@RequestMapping(params = { "find=ByRoleEntry", "form" }, method = RequestMethod.GET)
    public String findAuthorRolesByRoleEntryForm(Model uiModel) {
        uiModel.addAttribute("lsroles", LsRole.findAllLsRoles());
        return "authorroles/findAuthorRolesByRoleEntry";
    }

	@RequestMapping(params = "find=ByRoleEntry", method = RequestMethod.GET)
    public String findAuthorRolesByRoleEntry(@RequestParam("roleEntry") LsRole roleEntry, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("authorroles", AuthorRole.findAuthorRolesByRoleEntry(roleEntry, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) AuthorRole.countFindAuthorRolesByRoleEntry(roleEntry) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("authorroles", AuthorRole.findAuthorRolesByRoleEntry(roleEntry, sortFieldName, sortOrder).getResultList());
        }
        return "authorroles/list";
    }

	@RequestMapping(params = { "find=ByRoleEntryAndUserEntry", "form" }, method = RequestMethod.GET)
    public String findAuthorRolesByRoleEntryAndUserEntryForm(Model uiModel) {
        uiModel.addAttribute("lsroles", LsRole.findAllLsRoles());
        uiModel.addAttribute("authors", Author.findAllAuthors());
        return "authorroles/findAuthorRolesByRoleEntryAndUserEntry";
    }

	@RequestMapping(params = "find=ByRoleEntryAndUserEntry", method = RequestMethod.GET)
    public String findAuthorRolesByRoleEntryAndUserEntry(@RequestParam("roleEntry") LsRole roleEntry, @RequestParam("userEntry") Author userEntry, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("authorroles", AuthorRole.findAuthorRolesByRoleEntryAndUserEntry(roleEntry, userEntry, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) AuthorRole.countFindAuthorRolesByRoleEntryAndUserEntry(roleEntry, userEntry) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("authorroles", AuthorRole.findAuthorRolesByRoleEntryAndUserEntry(roleEntry, userEntry, sortFieldName, sortOrder).getResultList());
        }
        return "authorroles/list";
    }

	@RequestMapping(params = { "find=ByUserEntry", "form" }, method = RequestMethod.GET)
    public String findAuthorRolesByUserEntryForm(Model uiModel) {
        uiModel.addAttribute("authors", Author.findAllAuthors());
        return "authorroles/findAuthorRolesByUserEntry";
    }

	@RequestMapping(params = "find=ByUserEntry", method = RequestMethod.GET)
    public String findAuthorRolesByUserEntry(@RequestParam("userEntry") Author userEntry, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("authorroles", AuthorRole.findAuthorRolesByUserEntry(userEntry, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) AuthorRole.countFindAuthorRolesByUserEntry(userEntry) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("authorroles", AuthorRole.findAuthorRolesByUserEntry(userEntry, sortFieldName, sortOrder).getResultList());
        }
        return "authorroles/list";
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid AuthorRole authorRole, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, authorRole);
            return "authorroles/create";
        }
        uiModel.asMap().clear();
        authorRole.persist();
        return "redirect:/authorroles/" + encodeUrlPathSegment(authorRole.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new AuthorRole());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Author.countAuthors() == 0) {
            dependencies.add(new String[] { "userEntry", "authors" });
        }
        if (LsRole.countLsRoles() == 0) {
            dependencies.add(new String[] { "roleEntry", "lsroles" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "authorroles/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("authorrole", AuthorRole.findAuthorRole(id));
        uiModel.addAttribute("itemId", id);
        return "authorroles/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("authorroles", AuthorRole.findAuthorRoleEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) AuthorRole.countAuthorRoles() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("authorroles", AuthorRole.findAllAuthorRoles(sortFieldName, sortOrder));
        }
        return "authorroles/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid AuthorRole authorRole, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, authorRole);
            return "authorroles/update";
        }
        uiModel.asMap().clear();
        authorRole.merge();
        return "redirect:/authorroles/" + encodeUrlPathSegment(authorRole.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, AuthorRole.findAuthorRole(id));
        return "authorroles/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        AuthorRole authorRole = AuthorRole.findAuthorRole(id);
        authorRole.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/authorroles";
    }

	void populateEditForm(Model uiModel, AuthorRole authorRole) {
        uiModel.addAttribute("authorRole", authorRole);
        uiModel.addAttribute("authors", Author.findAllAuthors());
        uiModel.addAttribute("lsroles", LsRole.findAllLsRoles());
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

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            AuthorRole authorRole = AuthorRole.findAuthorRole(id);
            if (authorRole == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(authorRole.toJson(), headers, HttpStatus.OK);
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
            List<AuthorRole> result = AuthorRole.findAllAuthorRoles();
            return new ResponseEntity<String>(AuthorRole.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            AuthorRole authorRole = AuthorRole.fromJsonToAuthorRole(json);
            authorRole.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+authorRole.getId().toString()).build().toUriString());
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
            for (AuthorRole authorRole: AuthorRole.fromJsonArrayToAuthorRoles(json)) {
                authorRole.persist();
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
            AuthorRole authorRole = AuthorRole.fromJsonToAuthorRole(json);
            authorRole.setId(id);
            if (authorRole.merge() == null) {
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
            AuthorRole authorRole = AuthorRole.findAuthorRole(id);
            if (authorRole == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            authorRole.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByRoleEntry", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindAuthorRolesByRoleEntry(@RequestParam("roleEntry") LsRole roleEntry) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(AuthorRole.toJsonArray(AuthorRole.findAuthorRolesByRoleEntry(roleEntry).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByRoleEntryAndUserEntry", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindAuthorRolesByRoleEntryAndUserEntry(@RequestParam("roleEntry") LsRole roleEntry, @RequestParam("userEntry") Author userEntry) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(AuthorRole.toJsonArray(AuthorRole.findAuthorRolesByRoleEntryAndUserEntry(roleEntry, userEntry).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByUserEntry", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindAuthorRolesByUserEntry(@RequestParam("userEntry") Author userEntry) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(AuthorRole.toJsonArray(AuthorRole.findAuthorRolesByUserEntry(userEntry).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
