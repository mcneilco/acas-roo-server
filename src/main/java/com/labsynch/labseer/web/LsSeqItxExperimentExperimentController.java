package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LsSeqItxExperimentExperiment;
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

@RequestMapping("/lsseqitxexperimentexperiments")
@Controller
public class LsSeqItxExperimentExperimentController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LsSeqItxExperimentExperiment lsSeqItxExperimentExperiment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsSeqItxExperimentExperiment);
            return "lsseqitxexperimentexperiments/create";
        }
        uiModel.asMap().clear();
        lsSeqItxExperimentExperiment.persist();
        return "redirect:/lsseqitxexperimentexperiments/" + encodeUrlPathSegment(lsSeqItxExperimentExperiment.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LsSeqItxExperimentExperiment());
        return "lsseqitxexperimentexperiments/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("lsseqitxexperimentexperiment", LsSeqItxExperimentExperiment.findLsSeqItxExperimentExperiment(id));
        uiModel.addAttribute("itemId", id);
        return "lsseqitxexperimentexperiments/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("lsseqitxexperimentexperiments", LsSeqItxExperimentExperiment.findLsSeqItxExperimentExperimentEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LsSeqItxExperimentExperiment.countLsSeqItxExperimentExperiments() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("lsseqitxexperimentexperiments", LsSeqItxExperimentExperiment.findAllLsSeqItxExperimentExperiments(sortFieldName, sortOrder));
        }
        return "lsseqitxexperimentexperiments/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LsSeqItxExperimentExperiment lsSeqItxExperimentExperiment, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, lsSeqItxExperimentExperiment);
            return "lsseqitxexperimentexperiments/update";
        }
        uiModel.asMap().clear();
        lsSeqItxExperimentExperiment.merge();
        return "redirect:/lsseqitxexperimentexperiments/" + encodeUrlPathSegment(lsSeqItxExperimentExperiment.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LsSeqItxExperimentExperiment.findLsSeqItxExperimentExperiment(id));
        return "lsseqitxexperimentexperiments/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LsSeqItxExperimentExperiment lsSeqItxExperimentExperiment = LsSeqItxExperimentExperiment.findLsSeqItxExperimentExperiment(id);
        lsSeqItxExperimentExperiment.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/lsseqitxexperimentexperiments";
    }

	void populateEditForm(Model uiModel, LsSeqItxExperimentExperiment lsSeqItxExperimentExperiment) {
        uiModel.addAttribute("lsSeqItxExperimentExperiment", lsSeqItxExperimentExperiment);
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
