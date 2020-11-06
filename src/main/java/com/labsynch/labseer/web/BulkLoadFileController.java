package com.labsynch.labseer.web;
import com.labsynch.labseer.domain.BulkLoadFile;
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

@RequestMapping("/bulkloadfiles")
@Controller
@RooWebScaffold(path = "bulkloadfiles", formBackingObject = BulkLoadFile.class)
@RooWebFinder
public class BulkLoadFileController {

	@RequestMapping(params = { "find=ByFileNameEquals", "form" }, method = RequestMethod.GET)
    public String findBulkLoadFilesByFileNameEqualsForm(Model uiModel) {
        return "bulkloadfiles/findBulkLoadFilesByFileNameEquals";
    }

	@RequestMapping(params = "find=ByFileNameEquals", method = RequestMethod.GET)
    public String findBulkLoadFilesByFileNameEquals(@RequestParam("fileName") String fileName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bulkloadfiles", BulkLoadFile.findBulkLoadFilesByFileNameEquals(fileName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) BulkLoadFile.countFindBulkLoadFilesByFileNameEquals(fileName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bulkloadfiles", BulkLoadFile.findBulkLoadFilesByFileNameEquals(fileName, sortFieldName, sortOrder).getResultList());
        }
        return "bulkloadfiles/list";
    }

	@RequestMapping(params = { "find=ByRecordedByEquals", "form" }, method = RequestMethod.GET)
    public String findBulkLoadFilesByRecordedByEqualsForm(Model uiModel) {
        return "bulkloadfiles/findBulkLoadFilesByRecordedByEquals";
    }

	@RequestMapping(params = "find=ByRecordedByEquals", method = RequestMethod.GET)
    public String findBulkLoadFilesByRecordedByEquals(@RequestParam("recordedBy") String recordedBy, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bulkloadfiles", BulkLoadFile.findBulkLoadFilesByRecordedByEquals(recordedBy, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) BulkLoadFile.countFindBulkLoadFilesByRecordedByEquals(recordedBy) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bulkloadfiles", BulkLoadFile.findBulkLoadFilesByRecordedByEquals(recordedBy, sortFieldName, sortOrder).getResultList());
        }
        return "bulkloadfiles/list";
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid BulkLoadFile bulkLoadFile, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bulkLoadFile);
            return "bulkloadfiles/create";
        }
        uiModel.asMap().clear();
        bulkLoadFile.persist();
        return "redirect:/bulkloadfiles/" + encodeUrlPathSegment(bulkLoadFile.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new BulkLoadFile());
        return "bulkloadfiles/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("bulkloadfile", BulkLoadFile.findBulkLoadFile(id));
        uiModel.addAttribute("itemId", id);
        return "bulkloadfiles/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bulkloadfiles", BulkLoadFile.findBulkLoadFileEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) BulkLoadFile.countBulkLoadFiles() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bulkloadfiles", BulkLoadFile.findAllBulkLoadFiles(sortFieldName, sortOrder));
        }
        return "bulkloadfiles/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid BulkLoadFile bulkLoadFile, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bulkLoadFile);
            return "bulkloadfiles/update";
        }
        uiModel.asMap().clear();
        bulkLoadFile.merge();
        return "redirect:/bulkloadfiles/" + encodeUrlPathSegment(bulkLoadFile.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, BulkLoadFile.findBulkLoadFile(id));
        return "bulkloadfiles/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        BulkLoadFile bulkLoadFile = BulkLoadFile.findBulkLoadFile(id);
        bulkLoadFile.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/bulkloadfiles";
    }

	void populateEditForm(Model uiModel, BulkLoadFile bulkLoadFile) {
        uiModel.addAttribute("bulkLoadFile", bulkLoadFile);
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
