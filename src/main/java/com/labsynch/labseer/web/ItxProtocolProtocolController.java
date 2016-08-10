package com.labsynch.labseer.web;

import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.labsynch.labseer.domain.ItxProtocolProtocol;

@RequestMapping("/itxprotocolprotocols")
@Controller
@RooWebScaffold(path = "itxprotocolprotocols", formBackingObject = ItxProtocolProtocol.class)
@RooWebJson(jsonObject = ItxProtocolProtocol.class)
@RooWebFinder
public class ItxProtocolProtocolController {

	
//    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
//        ItxProtocolProtocol itxProtocolProtocol = ItxProtocolProtocol.findItxProtocolProtocol(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        if (itxProtocolProtocol == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(itxProtocolProtocol.toJson(), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        List<ItxProtocolProtocol> result = ItxProtocolProtocol.findAllItxProtocolProtocols();
//        return new ResponseEntity<String>(ItxProtocolProtocol.toJsonArray(result), headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
//        ItxProtocolProtocol itxProtocolProtocol = ItxProtocolProtocol.fromJsonToItxProtocolProtocol(json);
//        itxProtocolProtocol.persist();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
//        for (ItxProtocolProtocol itxProtocolProtocol : ItxProtocolProtocol.fromJsonArrayToItxProtocolProtocols(json)) {
//            itxProtocolProtocol.persist();
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
//    }
//
//    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        ItxProtocolProtocol itxProtocolProtocol = ItxProtocolProtocol.fromJsonToItxProtocolProtocol(json);
//        if (itxProtocolProtocol.merge() == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        for (ItxProtocolProtocol itxProtocolProtocol : ItxProtocolProtocol.fromJsonArrayToItxProtocolProtocols(json)) {
//            if (itxProtocolProtocol.merge() == null) {
//                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//            }
//        }
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
//    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
//        ItxProtocolProtocol itxProtocolProtocol = ItxProtocolProtocol.findItxProtocolProtocol(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json");
//        if (itxProtocolProtocol == null) {
//            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
//        }
//        itxProtocolProtocol.remove();
//        return new ResponseEntity<String>(headers, HttpStatus.OK);
//    }
//
//    @RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> jsonFindItxProtocolProtocolsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        return new ResponseEntity<String>(ItxProtocolProtocol.toJsonArray(ItxProtocolProtocol.findItxProtocolProtocolsByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
//    }
//
//    void populateEditForm(Model uiModel, ItxProtocolProtocol itxProtocolProtocol) {
//        uiModel.addAttribute("itxProtocolProtocol", itxProtocolProtocol);
//        addDateTimeFormatPatterns(uiModel);
//        uiModel.addAttribute("itxprotocolprotocolstates", ItxProtocolProtocolState.findAllItxProtocolProtocolStates());
//        uiModel.addAttribute("protocols", Protocol.findAllProtocols());
//        uiModel.addAttribute("thingpages", ThingPage.findAllThingPages());
//        uiModel.addAttribute("interactiontypes", InteractionType.findAllInteractionTypes());
//        uiModel.addAttribute("interactionkinds", InteractionKind.findAllInteractionKinds());
//    }
}
