package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LabelSequenceRole;
import com.labsynch.labseer.domain.LsRole;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
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

@RequestMapping("/labelsequenceroles")
@Controller
public class LabelSequenceRoleController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid LabelSequenceRole labelSequenceRole, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, labelSequenceRole);
            return "labelsequenceroles/create";
        }
        uiModel.asMap().clear();
        labelSequenceRole.persist();
        return "redirect:/labelsequenceroles/" + encodeUrlPathSegment(labelSequenceRole.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new LabelSequenceRole());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (LabelSequence.countLabelSequences() == 0) {
            dependencies.add(new String[] { "labelSequenceEntry", "labelsequences" });
        }
        if (LsRole.countLsRoles() == 0) {
            dependencies.add(new String[] { "roleEntry", "lsroles" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "labelsequenceroles/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("labelsequencerole", LabelSequenceRole.findLabelSequenceRole(id));
        uiModel.addAttribute("itemId", id);
        return "labelsequenceroles/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("labelsequenceroles", LabelSequenceRole.findLabelSequenceRoleEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) LabelSequenceRole.countLabelSequenceRoles() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("labelsequenceroles", LabelSequenceRole.findAllLabelSequenceRoles(sortFieldName, sortOrder));
        }
        return "labelsequenceroles/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid LabelSequenceRole labelSequenceRole, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, labelSequenceRole);
            return "labelsequenceroles/update";
        }
        uiModel.asMap().clear();
        labelSequenceRole.merge();
        return "redirect:/labelsequenceroles/" + encodeUrlPathSegment(labelSequenceRole.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, LabelSequenceRole.findLabelSequenceRole(id));
        return "labelsequenceroles/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        LabelSequenceRole labelSequenceRole = LabelSequenceRole.findLabelSequenceRole(id);
        labelSequenceRole.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/labelsequenceroles";
    }

	void populateEditForm(Model uiModel, LabelSequenceRole labelSequenceRole) {
        uiModel.addAttribute("labelSequenceRole", labelSequenceRole);
        uiModel.addAttribute("labelsequences", LabelSequence.findAllLabelSequences());
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
}
