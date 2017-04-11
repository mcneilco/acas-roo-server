package com.labsynch.labseer.api;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
import org.springframework.web.servlet.HandlerMapping;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ProtocolDTO;
import com.labsynch.labseer.dto.ProtocolErrorMessageDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.service.ExperimentValueService;
import com.labsynch.labseer.service.ProtocolService;
import com.labsynch.labseer.service.ProtocolValueService;
import com.labsynch.labseer.utils.PropertiesUtilService;

import flexjson.JSONSerializer;

//@RooWebJson(jsonObject = Protocol.class)
@Controller
@RequestMapping("api/v1/protocols")
//@RooWebFinder
@Transactional
public class ApiProtocolController {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(ApiProtocolController.class);

    @Autowired
    private ProtocolService protocolService;
    
    @Autowired
    private ProtocolValueService protocolValueService;

    @Autowired
    private ExperimentValueService experimentValueService;
    
    @Autowired
    private PropertiesUtilService propertiesUtilService;
    
//    @Transactional
//	@RequestMapping(value = "/find=bymetadata", method = RequestMethod.GET, headers = "Accept=application/json")
//    @ResponseBody
//    public ResponseEntity<java.lang.String> findProtocolsByMetadata(
//			@RequestBody String json,
//			@RequestParam(value = "with", required = false) String with) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
//        
//		Collection<Protocol> protocols = protocolService.findProtocolsByMetadataJson(json);
//
//        
//        return new ResponseEntity<String>(Protocol.toJsonArray(protocols), headers, HttpStatus.OK);
//    }


    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> showJson(@PathVariable("id") Long id) {
        Protocol protocol = Protocol.findProtocol(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        if (protocol == null || protocol.isIgnored()) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(protocol.toJson(), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{lstype}/{lskind}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJsonByPath(@PathVariable("lstype") String lsType, @PathVariable("lskind") String lsKind, @RequestParam(value = "with", required = false) String with, @RequestParam(value = "prettyjson", required = false) String prettyjson) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        boolean prettyJson = false;
        if (prettyjson != null) {
            if (prettyjson.equalsIgnoreCase("true")) {
                prettyJson = true;
            }
        }
        boolean includeExperiments = false;
        if (with != null) {
            if (with.equalsIgnoreCase("experiments") || with.equalsIgnoreCase("fullobject")) {
                includeExperiments = true;
            }
        }
        List<Protocol> protocols = null;
        if (lsKind != null) {
            if (lsType != null) {
                protocols = Protocol.findProtocolsByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList();
            } else {
                protocols = Protocol.findProtocolsByLsKindEquals(lsKind).getResultList();
            }
        } else {
            protocols = Protocol.findAllProtocols();
        }
        return new ResponseEntity<String>(Protocol.toJsonArray(protocols, prettyJson, includeExperiments), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJson(@RequestParam(value = "with", required = false) String with, @RequestParam(value = "prettyjson", required = false) String prettyjson, @RequestParam(value = "lstype", required = false) String lsType, @RequestParam(value = "lskind", required = false) String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        logger.debug("value for prettyjson: " + prettyjson);
        boolean prettyJson = false;
        if (prettyjson != null) {
            if (prettyjson.equalsIgnoreCase("true")) {
                prettyJson = true;
            }
        }
        boolean includeExperiments = false;
        if (with != null) {
            if (with.equalsIgnoreCase("experiments") || with.equalsIgnoreCase("fullobject")) {
                includeExperiments = true;
            }
        }
        List<Protocol> protocols = null;
        if (lsKind != null) {
            logger.debug("incoming lsKind is: " + lsKind);
            if (lsType != null) {
                protocols = Protocol.findProtocolsByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList();
            } else {
                protocols = Protocol.findProtocolsByLsKindEquals(lsKind).getResultList();
            }
        } else {
            protocols = Protocol.findAllProtocols();
        }
        return new ResponseEntity<String>(Protocol.toJsonArray(protocols, prettyJson, includeExperiments), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/codetable", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> listJsonCodeTable(
    		@RequestParam(value = "with", required = false) String with, 
    		@RequestParam(value = "prettyjson", required = false) String prettyjson, 
    		@RequestParam(value = "lstype", required = false) String lsType, 
    		@RequestParam(value = "lskind", required = false) String lsKind, 
    		@RequestParam(value = "protocolName", required = false) String protocolName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        List<CodeTableDTO> result;
        if (lsKind != null) {
            result = Protocol.getProtocolCodeTableByKindEquals(lsKind);
        } else if (protocolName != null) {
        	result = Protocol.getProtocolCodeTableByNameLike(protocolName);
        }else {
            result = Protocol.getProtocolCodeTable();
        }
        result = CodeTableDTO.sortCodeTables(result);
        return new ResponseEntity<String>(CodeTableDTO.toJsonArray(result), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody Protocol protocol){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        boolean errorsFound = false;
        try{
            protocol = protocolService.saveLsProtocol(protocol);
        } catch(UniqueNameException e){
        	logger.error("----from the controller----" + e.getMessage().toString() + " whole message  " + e.toString());
            ErrorMessage error = new ErrorMessage();
            error.setErrorLevel("error");
            error.setMessage("not unique protocol name");
            errors.add(error);
            errorsFound = true;
        }
        if (errorsFound) {
            return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<String>(protocol.toJson(), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) throws UniqueNameException {
        Collection<Protocol> savedProtocols = new ArrayList<Protocol>();
        int batchSize = propertiesUtilService.getBatchSize();
        int i = 0;
        StringReader sr = new StringReader(json);
        BufferedReader br = new BufferedReader(sr);
        for (Protocol protocol : Protocol.fromJsonArrayToProtocols(br)) {
            protocol = protocolService.saveLsProtocol(protocol);
            savedProtocols.add(protocol);
            if (i % batchSize == 0) {
                protocol.flush();
                protocol.clear();
            }
            i++;
        }
        IOUtils.closeQuietly(sr);
        IOUtils.closeQuietly(br);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(Protocol.toJsonArray(savedProtocols), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = { "/", "/{id}" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody Protocol protocol) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        boolean errorsFound = false;
        try{
        	protocol = protocolService.updateProtocol(protocol);
        } catch(UniqueNameException e){
        	logger.error("----from the controller----" + e.getMessage().toString() + " whole message  " + e.toString());
            ErrorMessage error = new ErrorMessage();
            error.setErrorLevel("error");
            error.setMessage("not unique protocol name");
            errors.add(error);
            errorsFound = true;
        }
        if (errorsFound) {
            return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.CONFLICT);
        }
        if (protocol.getId() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(protocol.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody List<Protocol> protocols) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Collection<Protocol> updatedProtocols = new ArrayList<Protocol>();
        ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        boolean errorsFound = false;
        for (Protocol protocol : protocols) {
        	try{
        		updatedProtocols.add(protocolService.updateProtocol(protocol));
            } catch(UniqueNameException e){
            	logger.error("----from the controller----" + e.getMessage().toString() + " whole message  " + e.toString());
                ErrorMessage error = new ErrorMessage();
                error.setErrorLevel("error");
                error.setMessage("not unique experiment name");
                errors.add(error);
                errorsFound = true;
            }
        	if (errorsFound) {
                return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<String>(Protocol.toJsonArray(updatedProtocols), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
        Protocol protocol = Protocol.findProtocol(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (protocol == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
//        protocol.remove();
        protocol.setIgnored(true);
        protocol.setDeleted(true);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/browser/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> softDeleteById(@PathVariable("id") Long id) {
        Protocol protocol = Protocol.findProtocol(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (protocol == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        ProtocolValue protocolValue = protocolValueService.updateProtocolValue(protocol.getCodeName(), "metadata", "protocol metadata", "codeValue", "protocol status", "deleted");
		protocol.setIgnored(true);
		for (Experiment experiment : Experiment.findExperimentsByProtocol(protocol).getResultList()){
			ExperimentValue experimentValue = experimentValueService.updateExperimentValue(experiment.getCodeName(), "metadata", "experiment metadata", "codeValue", "experiment status", "deleted");
			experiment.setIgnored(true);
		}
        return new ResponseEntity<String>(protocolValue.toJson(), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/lsprotocols", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createLsProtocolFromJson(@RequestBody Protocol protocol) throws UniqueNameException {
        protocol = protocolService.saveLsProtocol(protocol);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(protocol.toJson(), headers, HttpStatus.CREATED);
    }

    @Transactional
    @RequestMapping(value = "/codename/{codeName}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindProtocolsByCodeNameEqualsRoute(@PathVariable("codeName") String codeName) {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Protocol protocol;
        try{
        	protocol = Protocol.findProtocolsByCodeNameEquals(codeName).getSingleResult();
        } catch (EmptyResultDataAccessException e){
        	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(protocol.toJson(), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(params = "FindByCodeName", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindProtocolsByCodeNameEquals(@RequestParam("codeName") String codeName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByCodeNameEqualsAndIgnoredNot(codeName, true).getResultList()), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "/protocolname/**", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindProtocolsByProtocolNameEqualsRoute(HttpServletRequest request) {
        String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String protocolName = restOfTheUrl.split("protocolname\\/")[1].replaceAll("/$", "");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolByProtocolName(protocolName)), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(params = "FindByProtocolName", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindProtocolsByProtocolNameEqualsGet(@RequestParam("protocolName") String protocolName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolByProtocolName(protocolName)), headers, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(params = "FindByName", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> jsonFindProtocolByNameGet(@RequestParam("name") String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolByName(name)), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByIgnoredNot", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByIgnoredNot(@RequestParam(value = "ignored", required = false) boolean ignored) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByIgnoredNot(ignored).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByLsKindEquals", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByLsKindEquals(@RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByLsKindEquals(lsKind).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByLsTransactionEquals", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByLsTransactionEquals(@RequestParam("lsTransaction") Long lsTransaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByLsTransactionEquals(lsTransaction).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByLsTypeAndKindEquals", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByLsTypeAndKindEquals(@RequestParam("lsTypeAndKind") String lsTypeAndKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByLsTypeAndKindEquals(lsTypeAndKind).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByLsTypeEquals", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByLsTypeEquals(@RequestParam("lsType") String lsType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByLsTypeEquals(lsType).getResultList()), headers, HttpStatus.OK);
    }

	@RequestMapping(params = "find=ByLsTypeEqualsAndLsKindEquals", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<String> jsonFindProtocolsByLsTypeEqualsAndLsKindEquals(@RequestParam("lsType") String lsType, @RequestParam("lsKind") String lsKind) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(Protocol.toJsonArray(Protocol.findProtocolsByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList()), headers, HttpStatus.OK);
    }
	
    @RequestMapping(params = "find=ByMetadata", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    @Transactional
    public ResponseEntity<java.lang.String> listJsonByMetadata(@RequestParam Map<String,String> requestParams) {
//example url: http://localhost:8080/acas/api/v1/protocols/?find=ByMetadata&name=Target%20Y&codeName=PROT-00000004&type=default&kind=default
    	//Filter parameters supported: type, kind, name, codeName
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Set<Protocol> result = new HashSet<Protocol>();
        
        result = protocolService.findProtocolsByRequestMetadata(requestParams);
        
        return new ResponseEntity<String>(Protocol.toJsonArrayStub(result), headers, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> protocolBrowserSearch(@RequestParam("q") String searchQuery) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try {
		Collection<ProtocolDTO> result = ProtocolDTO.convertCollectionToProtocolDTO(protocolService.findProtocolsByGenericMetaDataSearch(searchQuery));
		return new ResponseEntity<String>(ProtocolDTO.toJsonArrayStub(result), headers, HttpStatus.OK);
		}catch(Exception e){
			String error = e.getMessage() + e.getStackTrace();
			return new ResponseEntity<String>(error, headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value= "/experimentCount/{codeName}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> experimentCount(@PathVariable String codeName) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		int numberOfExperiments = Protocol.findProtocolsByCodeNameEquals(codeName).getSingleResult().getExperiments().size();
        String result = new JSONSerializer().serialize(numberOfExperiments);
		return new ResponseEntity<String>(result, headers, HttpStatus.OK);
	}
	
	@Transactional
    @RequestMapping(value = "/codename/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public ResponseEntity<java.lang.String> findProtocolsByCodeNames(@RequestBody List<String> codeNames, @RequestParam(value = "with", required = false) String with) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        Collection<ProtocolErrorMessageDTO> foundProtocols;
        try{
        	foundProtocols = protocolService.findProtocolsByCodeNames(codeNames);
        } catch (EmptyResultDataAccessException e){
        	return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        if (with != null) {
            if (with.equalsIgnoreCase("fullobject")) {
                return new ResponseEntity<String>(ProtocolErrorMessageDTO.toJsonArray(foundProtocols), headers, HttpStatus.OK);
            } else if (with.equalsIgnoreCase("stub")) {
                return new ResponseEntity<String>(ProtocolErrorMessageDTO.toJsonArrayStub(foundProtocols), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("ERROR: with" + with + " format option not recognized. ", headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<String>(ProtocolErrorMessageDTO.toJsonArrayStub(foundProtocols), headers, HttpStatus.OK);
        }
    }
	
}
