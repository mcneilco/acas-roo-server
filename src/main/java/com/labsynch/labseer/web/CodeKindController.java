package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.CodeKind;
import com.labsynch.labseer.domain.CodeType;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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

@RequestMapping("/codekinds")
@Controller
@RooWebScaffold(path = "codekinds", formBackingObject = CodeKind.class)
@RooWebFinder
@RooWebJson(jsonObject = CodeKind.class)
public class CodeKindController {

	@RequestMapping(params = { "find=ByKindNameEqualsAndLsType", "form" }, method = RequestMethod.GET)
    public String findCodeKindsByKindNameEqualsAndLsTypeForm(Model uiModel) {
        uiModel.addAttribute("codetypes", CodeType.findAllCodeTypes());
        return "codekinds/findCodeKindsByKindNameEqualsAndLsType";
    }

	@RequestMapping(params = "find=ByKindNameEqualsAndLsType", method = RequestMethod.GET)
    public String findCodeKindsByKindNameEqualsAndLsType(@RequestParam("kindName") String kindName, @RequestParam("lsType") CodeType lsType, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("codekinds", CodeKind.findCodeKindsByKindNameEqualsAndLsType(kindName, lsType, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) CodeKind.countFindCodeKindsByKindNameEqualsAndLsType(kindName, lsType) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("codekinds", CodeKind.findCodeKindsByKindNameEqualsAndLsType(kindName, lsType, sortFieldName, sortOrder).getResultList());
        }
        return "codekinds/list";
    }

	@RequestMapping(params = { "find=ByLsType", "form" }, method = RequestMethod.GET)
    public String findCodeKindsByLsTypeForm(Model uiModel) {
        uiModel.addAttribute("codetypes", CodeType.findAllCodeTypes());
        return "codekinds/findCodeKindsByLsType";
    }

	@RequestMapping(params = "find=ByLsType", method = RequestMethod.GET)
    public String findCodeKindsByLsType(@RequestParam("lsType") CodeType lsType, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("codekinds", CodeKind.findCodeKindsByLsType(lsType, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) CodeKind.countFindCodeKindsByLsType(lsType) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("codekinds", CodeKind.findCodeKindsByLsType(lsType, sortFieldName, sortOrder).getResultList());
        }
        return "codekinds/list";
    }

	@RequestMapping(params = { "find=ByLsTypeAndKindEquals", "form" }, method = RequestMethod.GET)
    public String findCodeKindsByLsTypeAndKindEqualsForm(Model uiModel) {
        return "codekinds/findCodeKindsByLsTypeAndKindEquals";
    }

	@RequestMapping(params = "find=ByLsTypeAndKindEquals", method = RequestMethod.GET)
    public String findCodeKindsByLsTypeAndKindEquals(@RequestParam("lsTypeAndKind") String lsTypeAndKind, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("codekinds", CodeKind.findCodeKindsByLsTypeAndKindEquals(lsTypeAndKind, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) CodeKind.countFindCodeKindsByLsTypeAndKindEquals(lsTypeAndKind) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("codekinds", CodeKind.findCodeKindsByLsTypeAndKindEquals(lsTypeAndKind, sortFieldName, sortOrder).getResultList());
        }
        return "codekinds/list";
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid CodeKind codeKind, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, codeKind);
            return "codekinds/create";
        }
        uiModel.asMap().clear();
        codeKind.persist();
        return "redirect:/codekinds/" + encodeUrlPathSegment(codeKind.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new CodeKind());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (CodeType.countCodeTypes() == 0) {
            dependencies.add(new String[] { "lsType", "codetypes" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "codekinds/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("codekind", CodeKind.findCodeKind(id));
        uiModel.addAttribute("itemId", id);
        return "codekinds/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("codekinds", CodeKind.findCodeKindEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) CodeKind.countCodeKinds() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("codekinds", CodeKind.findAllCodeKinds(sortFieldName, sortOrder));
        }
        return "codekinds/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid CodeKind codeKind, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, codeKind);
            return "codekinds/update";
        }
        uiModel.asMap().clear();
        codeKind.merge();
        return "redirect:/codekinds/" + encodeUrlPathSegment(codeKind.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, CodeKind.findCodeKind(id));
        return "codekinds/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        CodeKind codeKind = CodeKind.findCodeKind(id);
        codeKind.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/codekinds";
    }

	void populateEditForm(Model uiModel, CodeKind codeKind) {
        uiModel.addAttribute("codeKind", codeKind);
        uiModel.addAttribute("codetypes", CodeType.findAllCodeTypes());
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
            CodeKind codeKind = CodeKind.findCodeKind(id);
            if (codeKind == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(codeKind.toJson(), headers, HttpStatus.OK);
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
            List<CodeKind> result = CodeKind.findAllCodeKinds();
            return new ResponseEntity<String>(CodeKind.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            CodeKind codeKind = CodeKind.fromJsonToCodeKind(json);
            codeKind.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+codeKind.getId().toString()).build().toUriString());
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
            for (CodeKind codeKind: CodeKind.fromJsonArrayToCodeKinds(json)) {
                codeKind.persist();
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
            CodeKind codeKind = CodeKind.fromJsonToCodeKind(json);
            codeKind.setId(id);
            if (codeKind.merge() == null) {
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
            CodeKind codeKind = CodeKind.findCodeKind(id);
            if (codeKind == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            codeKind.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByKindNameEqualsAndLsType", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindCodeKindsByKindNameEqualsAndLsType(@RequestParam("kindName") String kindName, @RequestParam("lsType") CodeType lsType) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(CodeKind.toJsonArray(CodeKind.findCodeKindsByKindNameEqualsAndLsType(kindName, lsType).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsType", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindCodeKindsByLsType(@RequestParam("lsType") CodeType lsType) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(CodeKind.toJsonArray(CodeKind.findCodeKindsByLsType(lsType).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeAndKindEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindCodeKindsByLsTypeAndKindEquals(@RequestParam("lsTypeAndKind") String lsTypeAndKind) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(CodeKind.toJsonArray(CodeKind.findCodeKindsByLsTypeAndKindEquals(lsTypeAndKind).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
