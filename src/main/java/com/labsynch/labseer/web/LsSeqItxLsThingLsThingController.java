package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsSeqItxLsThingLsThing;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/lsseqitxlsthinglsthings")
@Controller
public class LsSeqItxLsThingLsThingController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LsSeqItxLsThingLsThing lsSeqItxLsThingLsThing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsSeqItxLsThingLsThing);
            return "lsseqitxlsthinglsthings/create";
        }
        uiModel.asMap().clear();
        lsSeqItxLsThingLsThing.persist();
        return "redirect:/lsseqitxlsthinglsthings/" + encodeUrlPathSegment(lsSeqItxLsThingLsThing.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LsSeqItxLsThingLsThing());
        return "lsseqitxlsthinglsthings/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("lsseqitxlsthinglsthing", LsSeqItxLsThingLsThing.findLsSeqItxLsThingLsThing(id));
        uiModel.addAttribute("itemId", id);
        return "lsseqitxlsthinglsthings/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsseqitxlsthinglsthings", LsSeqItxLsThingLsThing.findLsSeqItxLsThingLsThingEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LsSeqItxLsThingLsThing.countLsSeqItxLsThingLsThings() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsseqitxlsthinglsthings", LsSeqItxLsThingLsThing.findAllLsSeqItxLsThingLsThings(sortFieldName, sortOrder));
        }
        return "lsseqitxlsthinglsthings/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LsSeqItxLsThingLsThing lsSeqItxLsThingLsThing, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsSeqItxLsThingLsThing);
            return "lsseqitxlsthinglsthings/update";
        }
        uiModel.asMap().clear();
        lsSeqItxLsThingLsThing.merge();
        return "redirect:/lsseqitxlsthinglsthings/" + encodeUrlPathSegment(lsSeqItxLsThingLsThing.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LsSeqItxLsThingLsThing.findLsSeqItxLsThingLsThing(id));
        return "lsseqitxlsthinglsthings/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LsSeqItxLsThingLsThing lsSeqItxLsThingLsThing = LsSeqItxLsThingLsThing.findLsSeqItxLsThingLsThing(id);
        lsSeqItxLsThingLsThing.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lsseqitxlsthinglsthings";
    }

	void populateEditForm(Model uiModel, LsSeqItxLsThingLsThing lsSeqItxLsThingLsThing) {
        uiModel.addAttribute("lsSeqItxLsThingLsThing", lsSeqItxLsThingLsThing);
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
