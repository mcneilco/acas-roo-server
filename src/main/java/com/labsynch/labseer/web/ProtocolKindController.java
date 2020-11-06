package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ProtocolKind;
import com.labsynch.labseer.domain.ProtocolType;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
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

@RooWebJson(jsonObject = ProtocolKind.class)
@RequestMapping("/protocolkinds")
@Controller
@RooWebScaffold(path = "protocolkinds", formBackingObject = ProtocolKind.class)
@RooWebFinder
public class ProtocolKindController {

	@RequestMapping(params = { "find=ByLsTypeEqualsAndKindNameEquals", "form" }, method = RequestMethod.GET)
    public String findProtocolKindsByLsTypeEqualsAndKindNameEqualsForm(Model uiModel) {
        uiModel.addAttribute("protocoltypes", ProtocolType.findAllProtocolTypes());
        return "protocolkinds/findProtocolKindsByLsTypeEqualsAndKindNameEquals";
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndKindNameEquals", method = RequestMethod.GET)
    public String findProtocolKindsByLsTypeEqualsAndKindNameEquals(@RequestParam("lsType") ProtocolType lsType, @RequestParam("kindName") String kindName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocolkinds", ProtocolKind.findProtocolKindsByLsTypeEqualsAndKindNameEquals(lsType, kindName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ProtocolKind.countFindProtocolKindsByLsTypeEqualsAndKindNameEquals(lsType, kindName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocolkinds", ProtocolKind.findProtocolKindsByLsTypeEqualsAndKindNameEquals(lsType, kindName, sortFieldName, sortOrder).getResultList());
        }
        return "protocolkinds/list";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        try {
            ProtocolKind protocolKind = ProtocolKind.findProtocolKind(id);
            if (protocolKind == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(protocolKind.toJson(), headers, HttpStatus.OK);
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
            List<ProtocolKind> result = ProtocolKind.findAllProtocolKinds();
            return new ResponseEntity<String>(ProtocolKind.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            ProtocolKind protocolKind = ProtocolKind.fromJsonToProtocolKind(json);
            protocolKind.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+protocolKind.getId().toString()).build().toUriString());
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
            for (ProtocolKind protocolKind: ProtocolKind.fromJsonArrayToProtocolKinds(json)) {
                protocolKind.persist();
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
            ProtocolKind protocolKind = ProtocolKind.fromJsonToProtocolKind(json);
            protocolKind.setId(id);
            if (protocolKind.merge() == null) {
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
            ProtocolKind protocolKind = ProtocolKind.findProtocolKind(id);
            if (protocolKind == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            protocolKind.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndKindNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolKindsByLsTypeEqualsAndKindNameEquals(@RequestParam("lsType") ProtocolType lsType, @RequestParam("kindName") String kindName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(ProtocolKind.toJsonArray(ProtocolKind.findProtocolKindsByLsTypeEqualsAndKindNameEquals(lsType, kindName).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ProtocolKind protocolKind, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, protocolKind);
            return "protocolkinds/create";
        }
        uiModel.asMap().clear();
        protocolKind.persist();
        return "redirect:/protocolkinds/" + encodeUrlPathSegment(protocolKind.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ProtocolKind());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (ProtocolType.countProtocolTypes() == 0) {
            dependencies.add(new String[] { "lsType", "protocoltypes" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "protocolkinds/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("protocolkind", ProtocolKind.findProtocolKind(id));
        uiModel.addAttribute("itemId", id);
        return "protocolkinds/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("protocolkinds", ProtocolKind.findProtocolKindEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ProtocolKind.countProtocolKinds() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("protocolkinds", ProtocolKind.findAllProtocolKinds(sortFieldName, sortOrder));
        }
        return "protocolkinds/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ProtocolKind protocolKind, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, protocolKind);
            return "protocolkinds/update";
        }
        uiModel.asMap().clear();
        protocolKind.merge();
        return "redirect:/protocolkinds/" + encodeUrlPathSegment(protocolKind.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ProtocolKind.findProtocolKind(id));
        return "protocolkinds/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ProtocolKind protocolKind = ProtocolKind.findProtocolKind(id);
        protocolKind.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/protocolkinds";
    }

	void populateEditForm(Model uiModel, ProtocolKind protocolKind) {
        uiModel.addAttribute("protocolKind", protocolKind);
        uiModel.addAttribute("protocoltypes", ProtocolType.findAllProtocolTypes());
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
