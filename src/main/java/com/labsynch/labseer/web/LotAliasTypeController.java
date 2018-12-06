package com.labsynch.labseer.web;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.gvnix.addon.datatables.annotations.GvNIXDatatables;
import org.gvnix.addon.web.mvc.annotations.jquery.GvNIXWebJQuery;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.labsynch.labseer.domain.LotAliasType;

@RooWebScaffold(path = "lotaliastypes", formBackingObject = LotAliasType.class)
@RequestMapping("/lotaliastypes")
@Controller
@Transactional
@GvNIXWebJQuery
@GvNIXDatatables(ajax = false)
@RooWebFinder
@RooWebJson(jsonObject = LotAliasType.class)
public class LotAliasTypeController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> showJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Kind", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        try {
            LotAliasType lotAliasType = LotAliasType.findLotAliasType(id);
            if (lotAliasType == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(lotAliasType.toJson(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Content-Kind", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        try {
            List<LotAliasType> result = LotAliasType.findAllLotAliasTypes();
            return new ResponseEntity<String>(LotAliasType.toJsonArray(result), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Content-Kind", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        try {
            LotAliasType lotAliasType = LotAliasType.fromJsonToLotAliasType(json);
            lotAliasType.persist();
            RequestMapping a = (RequestMapping) getClass().getAnnotation(RequestMapping.class);
            headers.add("Location", uriBuilder.path(a.value()[0] + "/" + lotAliasType.getId().toString()).build().toUriString());
            return new ResponseEntity<String>(lotAliasType.toJson(), headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Content-Kind", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        Collection<LotAliasType> lotAliasTypes = new HashSet<LotAliasType>();
        try {
            for (LotAliasType lotAliasType : LotAliasType.fromJsonArrayToLotAliasTypes(json)) {
                lotAliasType.persist();
                lotAliasTypes.add(lotAliasType);
            }
            return new ResponseEntity<String>(LotAliasType.toJsonArray(lotAliasTypes), headers, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json, @PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Content-Kind", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        try {
            LotAliasType lotAliasType = LotAliasType.fromJsonToLotAliasType(json);
            lotAliasType.setId(id);
            if (lotAliasType.merge() == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<String>(lotAliasType.toJson(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Content-Kind", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        try {
            LotAliasType lotAliasType = LotAliasType.findLotAliasType(id);
            if (lotAliasType == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            lotAliasType.remove();
            return new ResponseEntity<String>(headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("{\"ERROR\":" + e.getMessage() + "\"}", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<String> getIsotopeOptions() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Kind", "application/json");
        headers.add("Access-Control-Allow-Headers", "Content-Kind");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Cache-Control", "no-store, no-cache, must-revalidate"); //HTTP 1.1
        headers.add("Pragma", "no-cache"); //HTTP 1.0
        headers.setExpires(0); // Expire the cache
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(produces = "text/html", value = "/list")
    public String listDatatablesDetail(Model uiModel, HttpServletRequest request, @ModelAttribute LotAliasType lotAliasType) {
        // Do common datatables operations: get entity list filtered by request parameters
        listDatatables(uiModel, request, lotAliasType);
        // Show only the list fragment (without footer, header, menu, etc.) 
        return "forward:/WEB-INF/views/lotaliastypes/list.jspx";
    }
}
