package com.labsynch.labseer.web;

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

@RooWebJson(jsonObject = LabelType.class)
@Controller
@RequestMapping("/labeltypes")
@RooWebScaffold(path = "labeltypes", formBackingObject = LabelType.class)
@RooWebFinder
public class LabelTypeController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LabelType labelType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, labelType);
            return "labeltypes/create";
        }
        uiModel.asMap().clear();
        labelType.persist();
        return "redirect:/labeltypes/" + encodeUrlPathSegment(labelType.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LabelType());
        return "labeltypes/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("labeltype", LabelType.findLabelType(id));
        uiModel.addAttribute("itemId", id);
        return "labeltypes/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("labeltypes", LabelType.findLabelTypeEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LabelType.countLabelTypes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("labeltypes", LabelType.findAllLabelTypes(sortFieldName, sortOrder));
        }
        return "labeltypes/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LabelType labelType, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, labelType);
            return "labeltypes/update";
        }
        uiModel.asMap().clear();
        labelType.merge();
        return "redirect:/labeltypes/" + encodeUrlPathSegment(labelType.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LabelType.findLabelType(id));
        return "labeltypes/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LabelType labelType = LabelType.findLabelType(id);
        labelType.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/labeltypes";
    }

	void populateEditForm(Model uiModel, LabelType labelType) {
        uiModel.addAttribute("labelType", labelType);
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
            LabelType labelType = LabelType.findLabelType(id);
            if (labelType == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(labelType.toJson(), headers, HttpStatus.OK);
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
            List<LabelType> result = LabelType.findAllLabelTypes();
            return new ResponseEntity<String>(LabelType.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try {
            LabelType labelType = LabelType.fromJsonToLabelType(json);
            labelType.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location",uriBuilder.path(a.value()[0]+"/"+labelType.getId().toString()).build().toUriString());
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
            for (LabelType labelType: LabelType.fromJsonArrayToLabelTypes(json)) {
                labelType.persist();
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
            LabelType labelType = LabelType.fromJsonToLabelType(json);
            labelType.setId(id);
            if (labelType.merge() == null) {
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
            LabelType labelType = LabelType.findLabelType(id);
            if (labelType == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            labelType.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = "find=ByTypeNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindLabelTypesByTypeNameEquals(@RequestParam("typeName") String typeName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", "application/json; charset=utf-8");
            return new ResponseEntity<String>(LabelType.toJsonArray(LabelType.findLabelTypesByTypeNameEquals(typeName).getResultList()), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":"+e.getMessage()+"\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@RequestMapping(params = { "find=ByTypeNameEquals", "form" }, method = RequestMethod.GET)
    public String findLabelTypesByTypeNameEqualsForm(Model uiModel) {
        return "labeltypes/findLabelTypesByTypeNameEquals";
    }

	@RequestMapping(params = "find=ByTypeNameEquals", method = RequestMethod.GET)
    public String findLabelTypesByTypeNameEquals(@RequestParam("typeName") String typeName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("labeltypes", LabelType.findLabelTypesByTypeNameEquals(typeName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) LabelType.countFindLabelTypesByTypeNameEquals(typeName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("labeltypes", LabelType.findLabelTypesByTypeNameEquals(typeName, sortFieldName, sortOrder).getResultList());
        }
        return "labeltypes/list";
    }
}
