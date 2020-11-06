package com.labsynch.labseer.web;
import com.labsynch.labseer.domain.QcCompound;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;

@RequestMapping("/qccompounds")
@Controller
@RooWebScaffold(path = "qccompounds", formBackingObject = QcCompound.class)
@RooWebFinder
public class QcCompoundController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid QcCompound qcCompound, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, qcCompound);
            return "qccompounds/create";
        }
        uiModel.asMap().clear();
        qcCompound.persist();
        return "redirect:/qccompounds/" + encodeUrlPathSegment(qcCompound.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new QcCompound());
        return "qccompounds/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("qccompound", QcCompound.findQcCompound(id));
        uiModel.addAttribute("itemId", id);
        return "qccompounds/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("qccompounds", QcCompound.findQcCompoundEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) QcCompound.countQcCompounds() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("qccompounds", QcCompound.findAllQcCompounds(sortFieldName, sortOrder));
        }
        return "qccompounds/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid QcCompound qcCompound, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, qcCompound);
            return "qccompounds/update";
        }
        uiModel.asMap().clear();
        qcCompound.merge();
        return "redirect:/qccompounds/" + encodeUrlPathSegment(qcCompound.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, QcCompound.findQcCompound(id));
        return "qccompounds/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        QcCompound qcCompound = QcCompound.findQcCompound(id);
        qcCompound.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/qccompounds";
    }

	void populateEditForm(Model uiModel, QcCompound qcCompound) {
        uiModel.addAttribute("qcCompound", qcCompound);
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

	@RequestMapping(params = { "find=ByCdId", "form" }, method = RequestMethod.GET)
    public String findQcCompoundsByCdIdForm(Model uiModel) {
        return "qccompounds/findQcCompoundsByCdId";
    }

	@RequestMapping(params = "find=ByCdId", method = RequestMethod.GET)
    public String findQcCompoundsByCdId(@RequestParam("cdId") int CdId, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("qccompounds", QcCompound.findQcCompoundsByCdId(CdId, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) QcCompound.countFindQcCompoundsByCdId(CdId) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("qccompounds", QcCompound.findQcCompoundsByCdId(CdId, sortFieldName, sortOrder).getResultList());
        }
        return "qccompounds/list";
    }
}
