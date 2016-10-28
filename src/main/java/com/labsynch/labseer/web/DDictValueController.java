package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.DDictKind;
import com.labsynch.labseer.domain.DDictType;
import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.service.AutoLabelService;
import com.labsynch.labseer.service.DataDictionaryService;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/ddictvalues")
@Controller
@RooWebScaffold(path = "ddictvalues", formBackingObject = DDictValue.class)
@RooWebFinder
@RooWebJson(jsonObject = DDictValue.class)
public class DDictValueController {
	
//	@Autowired
//	private DataDictionaryService dDictionaryService;
//	
//	@Autowired
//	private AutoLabelService autoLabelService;
//
    void populateEditForm(Model uiModel, DDictValue DDictValue_) {
        uiModel.addAttribute("DDictValue_", DDictValue_);
        uiModel.addAttribute("ddicttypes", DDictType.findAllDDictTypes());
        uiModel.addAttribute("ddictkinds", DDictKind.findAllDDictKinds());
    }
//
//	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
//    public String create(@Valid DDictValue DDictValue_, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
//        if (bindingResult.hasErrors()) {
//            populateEditForm(uiModel, DDictValue_);
//            return "ddictvalues/create";
//        }
//        uiModel.asMap().clear();
//        String codeName = autoLabelService.getDataDictionaryCodeName();
//        DDictValue_.setCodeName(codeName);
//        DDictValue_.persist();
//        return "redirect:/ddictvalues/" + encodeUrlPathSegment(DDictValue_.getId().toString(), httpServletRequest);
//    }
//
//	@RequestMapping(params = "form", produces = "text/html")
//    public String createForm(Model uiModel) {
//        populateEditForm(uiModel, new DDictValue());
//        return "ddictvalues/create";
//    }
//
//	@RequestMapping(value = "/{id}", produces = "text/html")
//    public String show(@PathVariable("id") Long id, Model uiModel) {
//        uiModel.addAttribute("ddictvalue_", DDictValue.findDDictValue(id));
//        uiModel.addAttribute("itemId", id);
//        return "ddictvalues/show";
//    }
//
//	@RequestMapping(produces = "text/html")
//    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//        if (page != null || size != null) {
//            int sizeNo = size == null ? 10 : size.intValue();
//            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
//            uiModel.addAttribute("ddictvalues", DDictValue.findDDictValueEntries(firstResult, sizeNo));
//            float nrOfPages = (float) DDictValue.countDDictValues() / sizeNo;
//            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
//        } else {
//            uiModel.addAttribute("ddictvalues", DDictValue.findAllDDictValues());
//        }
//        return "ddictvalues/list";
//    }
//
//	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
//    public String update(@Valid DDictValue DDictValue_, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
//        if (bindingResult.hasErrors()) {
//            populateEditForm(uiModel, DDictValue_);
//            return "ddictvalues/update";
//        }
//        uiModel.asMap().clear();
//        DDictValue_.merge();
//        return "redirect:/ddictvalues/" + encodeUrlPathSegment(DDictValue_.getId().toString(), httpServletRequest);
//    }
//
//	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
//    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
//        populateEditForm(uiModel, DDictValue.findDDictValue(id));
//        return "ddictvalues/update";
//    }
//
//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//        DDictValue DDictValue_ = DDictValue.findDDictValue(id);
//        DDictValue_.remove();
//        uiModel.asMap().clear();
//        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//        return "redirect:/ddictvalues";
//    }
//
//	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
//        String enc = httpServletRequest.getCharacterEncoding();
//        if (enc == null) {
//            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
//        }
//        try {
//            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
//        } catch (UnsupportedEncodingException uee) {}
//        return pathSegment;
//    }
}
