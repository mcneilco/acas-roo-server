package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LabelKind;
import com.labsynch.labseer.domain.LabelType;
import java.io.UnsupportedEncodingException;
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

@RooWebJson(jsonObject = LabelKind.class)
@Controller
@RequestMapping("/labelkinds")
@RooWebScaffold(path = "labelkinds", formBackingObject = LabelKind.class)
@RooWebFinder
public class LabelKindController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LabelKind labelKind, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, labelKind);
            return "labelkinds/create";
        }
        uiModel.asMap().clear();
        labelKind.persist();
        return "redirect:/labelkinds/" + encodeUrlPathSegment(labelKind.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LabelKind());
        return "labelkinds/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("labelkind", LabelKind.findLabelKind(id));
        uiModel.addAttribute("itemId", id);
        return "labelkinds/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("labelkinds", LabelKind.findLabelKindEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LabelKind.countLabelKinds() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("labelkinds", LabelKind.findAllLabelKinds(sortFieldName, sortOrder));
        }
        return "labelkinds/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LabelKind labelKind, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, labelKind);
            return "labelkinds/update";
        }
        uiModel.asMap().clear();
        labelKind.merge();
        return "redirect:/labelkinds/" + encodeUrlPathSegment(labelKind.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LabelKind.findLabelKind(id));
        return "labelkinds/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LabelKind labelKind = LabelKind.findLabelKind(id);
        labelKind.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/labelkinds";
    }

	void populateEditForm(Model uiModel, LabelKind labelKind) {
        uiModel.addAttribute("labelKind", labelKind);
        uiModel.addAttribute("labeltypes", LabelType.findAllLabelTypes());
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
            LabelKind labelKind = LabelKind.findLabelKind(id);
            if (labelKind == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(labelKind.toJson(), headers, HttpStatus.OK);
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
            List<LabelKind> result = LabelKind.findAllLabelKinds();
            return new ResponseEntity<String>(LabelKind.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            LabelKind labelKind = LabelKind.fromJsonToLabelKind(json);
            labelKind.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+labelKind.getId().toString()).build().toUriString());
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
            for (LabelKind labelKind: LabelKind.fromJsonArrayToLabelKinds(json)) {
                labelKind.persist();
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
            LabelKind labelKind = LabelKind.fromJsonToLabelKind(json);
            labelKind.setId(id);
            if (labelKind.merge() == null) {
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
            LabelKind labelKind = LabelKind.findLabelKind(id);
            if (labelKind == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            labelKind.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByKindNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLabelKindsByKindNameEquals(@RequestParam("kindName") String kindName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LabelKind.toJsonArray(LabelKind.findLabelKindsByKindNameEquals(kindName).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByKindNameEqualsAndLsType", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLabelKindsByKindNameEqualsAndLsType(@RequestParam("kindName") String kindName, @RequestParam("lsType") LabelType lsType) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LabelKind.toJsonArray(LabelKind.findLabelKindsByKindNameEqualsAndLsType(kindName, lsType).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByLsType", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLabelKindsByLsType(@RequestParam("lsType") LabelType lsType) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LabelKind.toJsonArray(LabelKind.findLabelKindsByLsType(lsType).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = { "find=ByKindNameEquals", "form" }, method = RequestMethod.GET)
    public String findLabelKindsByKindNameEqualsForm(Model uiModel) {
        return "labelkinds/findLabelKindsByKindNameEquals";
    }

	@RequestMapping(params = "find=ByKindNameEquals", method = RequestMethod.GET)
    public String findLabelKindsByKindNameEquals(@RequestParam("kindName") String kindName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("labelkinds", LabelKind.findLabelKindsByKindNameEquals(kindName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LabelKind.countFindLabelKindsByKindNameEquals(kindName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("labelkinds", LabelKind.findLabelKindsByKindNameEquals(kindName, sortFieldName, sortOrder).getResultList());
        }
        return "labelkinds/list";
    }

	@RequestMapping(params = { "find=ByKindNameEqualsAndLsType", "form" }, method = RequestMethod.GET)
    public String findLabelKindsByKindNameEqualsAndLsTypeForm(Model uiModel) {
        uiModel.addAttribute("labeltypes", LabelType.findAllLabelTypes());
        return "labelkinds/findLabelKindsByKindNameEqualsAndLsType";
    }

	@RequestMapping(params = "find=ByKindNameEqualsAndLsType", method = RequestMethod.GET)
    public String findLabelKindsByKindNameEqualsAndLsType(@RequestParam("kindName") String kindName, @RequestParam("lsType") LabelType lsType, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("labelkinds", LabelKind.findLabelKindsByKindNameEqualsAndLsType(kindName, lsType, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LabelKind.countFindLabelKindsByKindNameEqualsAndLsType(kindName, lsType) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("labelkinds", LabelKind.findLabelKindsByKindNameEqualsAndLsType(kindName, lsType, sortFieldName, sortOrder).getResultList());
        }
        return "labelkinds/list";
    }

	@RequestMapping(params = { "find=ByLsType", "form" }, method = RequestMethod.GET)
    public String findLabelKindsByLsTypeForm(Model uiModel) {
        uiModel.addAttribute("labeltypes", LabelType.findAllLabelTypes());
        return "labelkinds/findLabelKindsByLsType";
    }

	@RequestMapping(params = "find=ByLsType", method = RequestMethod.GET)
    public String findLabelKindsByLsType(@RequestParam("lsType") LabelType lsType, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("labelkinds", LabelKind.findLabelKindsByLsType(lsType, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LabelKind.countFindLabelKindsByLsType(lsType) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("labelkinds", LabelKind.findLabelKindsByLsType(lsType, sortFieldName, sortOrder).getResultList());
        }
        return "labelkinds/list";
    }
}
