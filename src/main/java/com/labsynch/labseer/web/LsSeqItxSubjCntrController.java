package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsSeqItxSubjCntr;
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

@RequestMapping("/lsseqitxsubjcntrs")
@Controller
public class LsSeqItxSubjCntrController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LsSeqItxSubjCntr lsSeqItxSubjCntr, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsSeqItxSubjCntr);
            return "lsseqitxsubjcntrs/create";
        }
        uiModel.asMap().clear();
        lsSeqItxSubjCntr.persist();
        return "redirect:/lsseqitxsubjcntrs/" + encodeUrlPathSegment(lsSeqItxSubjCntr.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LsSeqItxSubjCntr());
        return "lsseqitxsubjcntrs/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("lsseqitxsubjcntr", LsSeqItxSubjCntr.findLsSeqItxSubjCntr(id));
        uiModel.addAttribute("itemId", id);
        return "lsseqitxsubjcntrs/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsseqitxsubjcntrs", LsSeqItxSubjCntr.findLsSeqItxSubjCntrEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LsSeqItxSubjCntr.countLsSeqItxSubjCntrs() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsseqitxsubjcntrs", LsSeqItxSubjCntr.findAllLsSeqItxSubjCntrs(sortFieldName, sortOrder));
        }
        return "lsseqitxsubjcntrs/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LsSeqItxSubjCntr lsSeqItxSubjCntr, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsSeqItxSubjCntr);
            return "lsseqitxsubjcntrs/update";
        }
        uiModel.asMap().clear();
        lsSeqItxSubjCntr.merge();
        return "redirect:/lsseqitxsubjcntrs/" + encodeUrlPathSegment(lsSeqItxSubjCntr.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LsSeqItxSubjCntr.findLsSeqItxSubjCntr(id));
        return "lsseqitxsubjcntrs/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LsSeqItxSubjCntr lsSeqItxSubjCntr = LsSeqItxSubjCntr.findLsSeqItxSubjCntr(id);
        lsSeqItxSubjCntr.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lsseqitxsubjcntrs";
    }

	void populateEditForm(Model uiModel, LsSeqItxSubjCntr lsSeqItxSubjCntr) {
        uiModel.addAttribute("lsSeqItxSubjCntr", lsSeqItxSubjCntr);
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
