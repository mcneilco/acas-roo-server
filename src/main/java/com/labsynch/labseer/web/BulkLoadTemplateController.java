package com.labsynch.labseer.web;
import com.labsynch.labseer.domain.BulkLoadTemplate;
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

@RequestMapping("/bulkloadtemplates")
@Controller
@RooWebScaffold(path = "bulkloadtemplates", formBackingObject = BulkLoadTemplate.class)
@RooWebFinder
public class BulkLoadTemplateController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid BulkLoadTemplate bulkLoadTemplate, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bulkLoadTemplate);
            return "bulkloadtemplates/create";
        }
        uiModel.asMap().clear();
        bulkLoadTemplate.persist();
        return "redirect:/bulkloadtemplates/" + encodeUrlPathSegment(bulkLoadTemplate.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new BulkLoadTemplate());
        return "bulkloadtemplates/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("bulkloadtemplate", BulkLoadTemplate.findBulkLoadTemplate(id));
        uiModel.addAttribute("itemId", id);
        return "bulkloadtemplates/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bulkloadtemplates", BulkLoadTemplate.findBulkLoadTemplateEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) BulkLoadTemplate.countBulkLoadTemplates() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bulkloadtemplates", BulkLoadTemplate.findAllBulkLoadTemplates(sortFieldName, sortOrder));
        }
        return "bulkloadtemplates/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid BulkLoadTemplate bulkLoadTemplate, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bulkLoadTemplate);
            return "bulkloadtemplates/update";
        }
        uiModel.asMap().clear();
        bulkLoadTemplate.merge();
        return "redirect:/bulkloadtemplates/" + encodeUrlPathSegment(bulkLoadTemplate.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, BulkLoadTemplate.findBulkLoadTemplate(id));
        return "bulkloadtemplates/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        BulkLoadTemplate bulkLoadTemplate = BulkLoadTemplate.findBulkLoadTemplate(id);
        bulkLoadTemplate.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/bulkloadtemplates";
    }

	void populateEditForm(Model uiModel, BulkLoadTemplate bulkLoadTemplate) {
        uiModel.addAttribute("bulkLoadTemplate", bulkLoadTemplate);
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

	@RequestMapping(params = { "find=ByRecordedByEquals", "form" }, method = RequestMethod.GET)
    public String findBulkLoadTemplatesByRecordedByEqualsForm(Model uiModel) {
        return "bulkloadtemplates/findBulkLoadTemplatesByRecordedByEquals";
    }

	@RequestMapping(params = "find=ByRecordedByEquals", method = RequestMethod.GET)
    public String findBulkLoadTemplatesByRecordedByEquals(@RequestParam("recordedBy") String recordedBy, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bulkloadtemplates", BulkLoadTemplate.findBulkLoadTemplatesByRecordedByEquals(recordedBy, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) BulkLoadTemplate.countFindBulkLoadTemplatesByRecordedByEquals(recordedBy) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bulkloadtemplates", BulkLoadTemplate.findBulkLoadTemplatesByRecordedByEquals(recordedBy, sortFieldName, sortOrder).getResultList());
        }
        return "bulkloadtemplates/list";
    }

	@RequestMapping(params = { "find=ByTemplateNameEquals", "form" }, method = RequestMethod.GET)
    public String findBulkLoadTemplatesByTemplateNameEqualsForm(Model uiModel) {
        return "bulkloadtemplates/findBulkLoadTemplatesByTemplateNameEquals";
    }

	@RequestMapping(params = "find=ByTemplateNameEquals", method = RequestMethod.GET)
    public String findBulkLoadTemplatesByTemplateNameEquals(@RequestParam("templateName") String templateName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bulkloadtemplates", BulkLoadTemplate.findBulkLoadTemplatesByTemplateNameEquals(templateName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) BulkLoadTemplate.countFindBulkLoadTemplatesByTemplateNameEquals(templateName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bulkloadtemplates", BulkLoadTemplate.findBulkLoadTemplatesByTemplateNameEquals(templateName, sortFieldName, sortOrder).getResultList());
        }
        return "bulkloadtemplates/list";
    }

	@RequestMapping(params = { "find=ByTemplateNameEqualsAndRecordedByEquals", "form" }, method = RequestMethod.GET)
    public String findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEqualsForm(Model uiModel) {
        return "bulkloadtemplates/findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals";
    }

	@RequestMapping(params = "find=ByTemplateNameEqualsAndRecordedByEquals", method = RequestMethod.GET)
    public String findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals(@RequestParam("templateName") String templateName, @RequestParam("recordedBy") String recordedBy, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bulkloadtemplates", BulkLoadTemplate.findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals(templateName, recordedBy, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) BulkLoadTemplate.countFindBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals(templateName, recordedBy) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bulkloadtemplates", BulkLoadTemplate.findBulkLoadTemplatesByTemplateNameEqualsAndRecordedByEquals(templateName, recordedBy, sortFieldName, sortOrder).getResultList());
        }
        return "bulkloadtemplates/list";
    }
}
