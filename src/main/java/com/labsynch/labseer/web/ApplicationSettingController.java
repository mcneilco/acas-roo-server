package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ApplicationSetting;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/applicationsettings")
@Controller
public class ApplicationSettingController {

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        ApplicationSetting applicationSetting = ApplicationSetting.findApplicationSetting(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (applicationSetting == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(applicationSetting.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ApplicationSetting> result = ApplicationSetting.findAllApplicationSettings();
        return new ResponseEntity<String>(ApplicationSetting.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/propname/{propName}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> getByPropName(@PathVariable("propName") String propName) {
        List<ApplicationSetting> applicationSettings = ApplicationSetting.findApplicationSettingsByPropNameEqualsAndIgnoredNot(propName, true).getResultList();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (applicationSettings.size() == 0) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(ApplicationSetting.toJsonArray(applicationSettings), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
        ApplicationSetting applicationSetting = ApplicationSetting.fromJsonToApplicationSetting(json);
        applicationSetting.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
        for (ApplicationSetting applicationSetting : ApplicationSetting.fromJsonArrayToApplicationSettings(json)) {
            applicationSetting.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ApplicationSetting applicationSetting = ApplicationSetting.fromJsonToApplicationSetting(json);
        if (applicationSetting.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        for (ApplicationSetting applicationSetting : ApplicationSetting.fromJsonArrayToApplicationSettings(json)) {
            if (applicationSetting.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        ApplicationSetting applicationSetting = ApplicationSetting.findApplicationSetting(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (applicationSetting == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        applicationSetting.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByPropNameEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindApplicationSettingsByPropNameEquals(@RequestParam("propName") String propName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ApplicationSetting.toJsonArray(ApplicationSetting.findApplicationSettingsByPropNameEquals(propName).getResultList()), headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByPropNameEqualsAndIgnoredNot", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindApplicationSettingsByPropNameEqualsAndIgnoredNot(@RequestParam("propName") String propName, @RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ApplicationSetting.toJsonArray(ApplicationSetting.findApplicationSettingsByPropNameEqualsAndIgnoredNot(propName, ignored).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = { "find=ByPropNameEquals", "form" }, method = RequestMethod.GET)
    public String findApplicationSettingsByPropNameEqualsForm(Model uiModel) {
        return "applicationsettings/findApplicationSettingsByPropNameEquals";
    }

	@RequestMapping(params = "find=ByPropNameEquals", method = RequestMethod.GET)
    public String findApplicationSettingsByPropNameEquals(@RequestParam("propName") String propName, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("applicationsettings", ApplicationSetting.findApplicationSettingsByPropNameEquals(propName, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ApplicationSetting.countFindApplicationSettingsByPropNameEquals(propName) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("applicationsettings", ApplicationSetting.findApplicationSettingsByPropNameEquals(propName, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "applicationsettings/list";
    }

	@RequestMapping(params = { "find=ByPropNameEqualsAndIgnoredNot", "form" }, method = RequestMethod.GET)
    public String findApplicationSettingsByPropNameEqualsAndIgnoredNotForm(Model uiModel) {
        return "applicationsettings/findApplicationSettingsByPropNameEqualsAndIgnoredNot";
    }

	@RequestMapping(params = "find=ByPropNameEqualsAndIgnoredNot", method = RequestMethod.GET)
    public String findApplicationSettingsByPropNameEqualsAndIgnoredNot(@RequestParam("propName") String propName, @RequestParam(value = "ignored", required = false) boolean ignored, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("applicationsettings", ApplicationSetting.findApplicationSettingsByPropNameEqualsAndIgnoredNot(propName, ignored, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) ApplicationSetting.countFindApplicationSettingsByPropNameEqualsAndIgnoredNot(propName, ignored) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("applicationsettings", ApplicationSetting.findApplicationSettingsByPropNameEqualsAndIgnoredNot(propName, ignored, sortFieldName, sortOrder).getResultList());
        }
        addDateTimeFormatPatterns(uiModel);
        return "applicationsettings/list";
    }

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ApplicationSetting applicationSetting, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, applicationSetting);
            return "applicationsettings/create";
        }
        uiModel.asMap().clear();
        applicationSetting.persist();
        return "redirect:/applicationsettings/" + encodeUrlPathSegment(applicationSetting.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ApplicationSetting());
        return "applicationsettings/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("applicationsetting", ApplicationSetting.findApplicationSetting(id));
        uiModel.addAttribute("itemId", id);
        return "applicationsettings/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("applicationsettings", ApplicationSetting.findApplicationSettingEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) ApplicationSetting.countApplicationSettings() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("applicationsettings", ApplicationSetting.findAllApplicationSettings(sortFieldName, sortOrder));
        }
        addDateTimeFormatPatterns(uiModel);
        return "applicationsettings/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ApplicationSetting applicationSetting, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, applicationSetting);
            return "applicationsettings/update";
        }
        uiModel.asMap().clear();
        applicationSetting.merge();
        return "redirect:/applicationsettings/" + encodeUrlPathSegment(applicationSetting.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ApplicationSetting.findApplicationSetting(id));
        return "applicationsettings/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ApplicationSetting applicationSetting = ApplicationSetting.findApplicationSetting(id);
        applicationSetting.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/applicationsettings";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("applicationSetting_recordeddate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, ApplicationSetting applicationSetting) {
        uiModel.addAttribute("applicationSetting", applicationSetting);
        addDateTimeFormatPatterns(uiModel);
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
