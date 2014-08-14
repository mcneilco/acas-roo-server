package com.labsynch.labseer.web;

import com.labsynch.labseer.domain.ApplicationSetting;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/applicationsettings")
@Controller
@RooWebScaffold(path = "applicationsettings", formBackingObject = ApplicationSetting.class)
@RooWebJson(jsonObject = ApplicationSetting.class)
@RooWebFinder
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
}
