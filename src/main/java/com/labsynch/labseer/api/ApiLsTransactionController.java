package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.List;

import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.dto.LsTransactionQueryDTO;
import com.labsynch.labseer.service.LsTransactionService;

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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/v1/lstransactions")
@Transactional

public class ApiLsTransactionController {

	private static final Logger logger = LoggerFactory.getLogger(ApiLsTransactionController.class);
	
	@Autowired
	private LsTransactionService lsTransactionService;
	
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        LsTransaction lsTransaction = LsTransaction.findLsTransaction(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (lsTransaction == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(lsTransaction.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<LsTransaction> result = LsTransaction.findAllLsTransactions();
        return new ResponseEntity<String>(LsTransaction.toJsonArray(result), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String lsTransaction) {
    	LsTransaction inputLsTransaction = LsTransaction.fromJsonToLsTransaction(lsTransaction);
    	logger.info(inputLsTransaction.toJson());
    	inputLsTransaction.persist();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(inputLsTransaction.toJson(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody List<LsTransaction> lsTransactions) {
        for (LsTransaction lsTransaction : lsTransactions) {
            lsTransaction.persist();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(LsTransaction.toJsonArray(lsTransactions),headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String lsTransaction, @PathVariable("id") Long id) {

    	LsTransaction inputLsTransaction = LsTransaction.fromJsonToLsTransaction(lsTransaction);
    	logger.info(inputLsTransaction.toJson());
    	
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (inputLsTransaction.merge() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(inputLsTransaction.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody List<LsTransaction> lsTransactions) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        List<LsTransaction> updatedLsTransactions = new ArrayList<LsTransaction>();
        for (LsTransaction lsTransaction : lsTransactions) {
        	LsTransaction updatedLsTransaction = lsTransaction.merge();
            if (updatedLsTransaction == null) {
                return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
            }
            updatedLsTransactions.add(updatedLsTransaction);
        }
        return new ResponseEntity<String>(LsTransaction.toJsonArray(updatedLsTransactions), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        LsTransaction lsTransaction = LsTransaction.findLsTransaction(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (lsTransaction == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        lsTransaction.remove();
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> searchLsTransactions(@RequestBody LsTransactionQueryDTO query) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try{
        	LsTransactionQueryDTO response = lsTransactionService.searchLsTransactions(query);
        	return new ResponseEntity<String>(response.toJson(), headers, HttpStatus.OK);
        }catch (Exception e){
        	logger.error("Caught exception searching for LsTransactions",e);
        	return new ResponseEntity<String>(e.getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
}
