package com.labsynch.labseer.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.ItxProtocolProtocol;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.service.ItxProtocolProtocolService;

@RequestMapping("/api/v1/itxprotocolprotocols")
@Controller
public class ApiItxProtocolProtocolController {
	
    private static final Logger logger = LoggerFactory.getLogger(ApiItxProtocolProtocolController.class);
	
	@Autowired
    private ItxProtocolProtocolService itxProtocolProtocolService;

    @RequestMapping(value = "/{id}", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        ItxProtocolProtocol itxProtocolProtocol = ItxProtocolProtocol.findItxProtocolProtocol(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (itxProtocolProtocol == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(itxProtocolProtocol.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<ItxProtocolProtocol> result = ItxProtocolProtocol.findAllItxProtocolProtocols();
        return new ResponseEntity<String>(ItxProtocolProtocol.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try{
        	ItxProtocolProtocol itxProtocolProtocol = ItxProtocolProtocol.fromJsonToItxProtocolProtocol(json);
            itxProtocolProtocol = itxProtocolProtocolService.saveLsItxProtocol(itxProtocolProtocol);
            return new ResponseEntity<String>(itxProtocolProtocol.toJson(), headers, HttpStatus.CREATED);
        }catch (Exception e){
        	logger.error("Uncaught exception in createFromJson",e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
    	Collection<ItxProtocolProtocol> itxProtocolProtocols = ItxProtocolProtocol.fromJsonArrayToItxProtocolProtocols(json);
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try{
        	Collection<ItxProtocolProtocol> savedItxProtocolProtocols = itxProtocolProtocolService.saveLsItxProtocols(itxProtocolProtocols);
            return new ResponseEntity<String>(ItxProtocolProtocol.toJsonArray(savedItxProtocolProtocols), headers, HttpStatus.CREATED);
        }catch (Exception e){
        	logger.error("Uncaught exception in createFromJsonArray",e);
            return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        
    }

    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ItxProtocolProtocol itxProtocolProtocol = ItxProtocolProtocol.fromJsonToItxProtocolProtocol(json);
        ItxProtocolProtocol updatedItxProtocolProtocol = null;
        try{
			updatedItxProtocolProtocol = itxProtocolProtocolService.updateItxProtocolProtocol(itxProtocolProtocol);
	        return new ResponseEntity<String>(updatedItxProtocolProtocol.toJson(), headers, HttpStatus.OK);
        } catch (Exception e) {
			logger.error("Caught error updating ItxProtocolProtocol from JSON",e);
			return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJsonArray(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<ItxProtocolProtocol> updatedItxProtocolProtocols = new HashSet<ItxProtocolProtocol>();
        try{
            for (ItxProtocolProtocol itxProtocolProtocol: ItxProtocolProtocol.fromJsonArrayToItxProtocolProtocols(json)) {
            	ItxProtocolProtocol updatedItxProtocolProtocol = itxProtocolProtocolService.updateItxProtocolProtocol(itxProtocolProtocol);
            	updatedItxProtocolProtocols.add(updatedItxProtocolProtocol);
            }
	        return new ResponseEntity<String>(ItxProtocolProtocol.toJsonArray(updatedItxProtocolProtocols), headers, HttpStatus.OK);
        } catch (Exception e) {
        	logger.error("Caught error updating ItxProtocolProtocols from JSON",e);
			return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        ItxProtocolProtocol itxProtocolProtocol = ItxProtocolProtocol.findItxProtocolProtocol(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (itxProtocolProtocol == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        itxProtocolProtocol.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    @RequestMapping(params = "find=ByLsTransactionEquals", headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindItxProtocolProtocolsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ItxProtocolProtocol.toJsonArray(ItxProtocolProtocol.findItxProtocolProtocolsByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
    }
    
    @Transactional
    @RequestMapping(value = "/findByFirstProtocol/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> findItxProtocolProtocolsByFirstProtocol(@PathVariable("id") Long firstProtocolId) {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try{
        	Collection<ItxProtocolProtocol> itxProtocolProtocols = itxProtocolProtocolService.findItxProtocolProtocolsByFirstProtocol( firstProtocolId);
            return new ResponseEntity<String>(ItxProtocolProtocol.toJsonArray(itxProtocolProtocols), headers, HttpStatus.OK);
        }catch (Exception e){
        	logger.error("Caught exception in findByFirstProtocol",e);
        	return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Transactional
    @RequestMapping(value = "/findBySecondProtocol/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> findItxProtocolProtocolsBySecondProtocol(@PathVariable("id") Long secondProtocolId) {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Protocol secondProtocol;
        try{
    		secondProtocol = Protocol.findProtocol(secondProtocolId);
    	} catch(Exception e){
    		logger.error("Error in findItxProtocolProtocolsBySecondProtocol: secondProtocol "+ secondProtocolId.toString()+" not found");
    		return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
    	}
        Collection<ItxProtocolProtocol> itxProtocolProtocols = ItxProtocolProtocol.findItxProtocolProtocolsBySecondProtocol(secondProtocol).getResultList();
        return new ResponseEntity<String>(ItxProtocolProtocol.toJsonArray(itxProtocolProtocols), headers, HttpStatus.OK);
    }
}
