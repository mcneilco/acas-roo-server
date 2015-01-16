package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.service.GeneThingService;
import com.labsynch.labseer.service.LsThingService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

@Controller
@RequestMapping("api/v1/lsthings")
@Transactional
public class ApiLsThingController {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(ApiLsThingController.class);

    @Autowired
    private LsThingService lsThingService;

    @Autowired
    private GeneThingService geneThingService;

    @Autowired
    private PropertiesUtilService propertiesUtilService;
    
    @RequestMapping(value = "/getGeneCodeNameFromNameRequest", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getGeneCodeNameFromName(@RequestBody PreferredNameRequestDTO requestDTO) {
        String thingType = "gene";
        String thingKind = "entrez gene";
        String labelType = "name";
        String labelKind = "Entrez Gene ID";
        logger.info("getGeneCodeNameFromNameRequest incoming json: " + requestDTO.toJson());
        PreferredNameResultsDTO results = lsThingService.getPreferredNameFromName(thingType, thingKind, labelType, labelKind, requestDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(results.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/getCodeNameFromNameRequest", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getCodeNameFromName(@RequestBody PreferredNameRequestDTO requestDTO, 
    		@RequestParam(value = "thingType", required = true) String thingType, 
    		@RequestParam(value = "thingKind", required = true) String thingKind, 
    		@RequestParam(value = "labelType", required = false) String labelType, 
    		@RequestParam(value = "labelKind", required = false) String labelKind) {
        logger.info("getCodeNameFromNameRequest incoming json: " + requestDTO.toJson());
        PreferredNameResultsDTO results = lsThingService.getCodeNameFromName(thingType, thingKind, labelType, labelKind, requestDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(results.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/getPreferredNameFromNameRequest", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getPreferredNameFromName(@RequestBody PreferredNameRequestDTO requestDTO, @RequestParam(value = "thingType", required = true) String thingType, @RequestParam(value = "thingKind", required = true) String thingKind, @RequestParam(value = "labelType", required = false) String labelType, @RequestParam(value = "labelKind", required = false) String labelKind) {
        PreferredNameResultsDTO results = lsThingService.getPreferredNameFromName(thingType, thingKind, labelType, labelKind, requestDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(results.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getProjects() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(lsThingService.getProjectCodes(), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{lsType}/{lsKind}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getLsThingByTypeAndKindAll(@PathVariable("lsType") String lsType, 
    		@PathVariable("lsKind") String lsKind, 
    		@RequestParam(value = "labelText", required = false) String labelText,
    		@RequestParam(value = "labelType", required = false) String labelType,
    		@RequestParam(value = "labelKind", required = false) String labelKind,
    		@RequestParam(value = "with", required = false) String with) {
    	List<LsThing> results;
    	boolean withLabelText = (labelText != null && !labelText.equalsIgnoreCase(""));
    	boolean withLabelType = (labelType != null && !labelType.equalsIgnoreCase(""));
    	boolean withLabelKind = (labelKind != null && !labelKind.equalsIgnoreCase(""));
    	if (withLabelText) {
    		if (withLabelType){	
    			if (withLabelKind){
    				results = LsThing.findLsThingByLabelText(lsType, lsKind, labelType, labelKind, labelText).getResultList();
    			} else results = LsThing.findLsThingByLabelTypeAndLabelText(lsType, lsKind, labelType, labelText).getResultList();
    		} else if (withLabelKind) {
    			results = LsThing.findLsThingByLabelKindAndLabelText(lsType, lsKind, labelKind, labelText).getResultList();
    		} else results = LsThing.findLsThingByLabelText(lsType, lsKind, labelText).getResultList();
    	}
    	else if (withLabelType) {
    		if (withLabelKind){
    			results = LsThing.findLsThingByLabelTypeAndKind(lsType, lsKind, labelType, labelKind).getResultList();
    		} else results = LsThing.findLsThingByLabelType(lsType, lsKind, labelType).getResultList();
    	}
    	else if (withLabelKind) {
    		results = LsThing.findLsThingByLabelKind(lsType, lsKind, labelKind).getResultList();
    	}
    	else {
    		results = LsThing.findLsThing(lsType, lsKind).getResultList();
    		
    	}
    	logger.info("query labelText: " + labelText + " number of results: " + results.size());
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Content-Type", "application/json; charset=utf-8");
    	if (with != null) {
			if (with.equalsIgnoreCase("fullobject")) {
				return new ResponseEntity<String>(LsThing.toJsonArray(results), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(LsThing.toJsonArrayPretty(results), headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(LsThing.toJsonArrayStub(results), headers, HttpStatus.OK);
			}
		}
    	return new ResponseEntity<String>(LsThing.toJsonArray(results), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{lsType}/{lsKind}/{idOrCodeName}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getLsThingByIdCodeName(@PathVariable("lsType") String lsType, 
    		@PathVariable("lsKind") String lsKind,
    		@PathVariable("idOrCodeName") String idOrCodeName) {
    	logger.debug("----from the LsThing GET controller----");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        boolean errorsFound = false;
        LsThing lsThing;
        if(SimpleUtil.isNumeric(idOrCodeName)) {
			lsThing = LsThing.findLsThing(Long.valueOf(idOrCodeName));
		} else {		
			try {
				lsThing = LsThing.findLsThingsByCodeNameEquals(idOrCodeName).getSingleResult();
			} catch(Exception ex) {
				lsThing = null;
				ErrorMessage error = new ErrorMessage();
	            error.setErrorLevel("error");
	            error.setMessage("lsThing:" + idOrCodeName +" not found");
	            errors.add(error);
	            errorsFound = true;
			}
		}
        if (errorsFound) {
            return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<String>(lsThing.toJson(), headers, HttpStatus.OK);
        }
    }
    
    @RequestMapping(value = "/{lsType}/{lsKind}/getbatches/{parentIdOrCodeName}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<String> getLsThingBatchesByParentIdCodeName(@PathVariable("lsType") String lsType, 
    		@PathVariable("lsKind") String lsKind,
    		@PathVariable("parentIdOrCodeName") String parentIdOrCodeName) {
    	logger.debug("----from the LsThing GET controller----");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        boolean errorsFound = false;
        LsThing parent;
        if(SimpleUtil.isNumeric(parentIdOrCodeName)) {
			parent = LsThing.findLsThing(Long.valueOf(parentIdOrCodeName));
		} else {		
			try {
				parent = LsThing.findLsThingsByCodeNameEquals(parentIdOrCodeName).getSingleResult();
			} catch(Exception ex) {
				parent = null;
				ErrorMessage error = new ErrorMessage();
	            error.setErrorLevel("error");
	            error.setMessage("parent:" + parentIdOrCodeName +" not found");
	            errors.add(error);
	            errorsFound = true;
			}
		}
        Collection<LsThing> batches = lsThingService.findBatchesByParentEquals(parent);
        if (errorsFound) {
            return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<String>(LsThing.toJsonArray(batches), headers, HttpStatus.OK);
        }
    }
    

	@Transactional
    @RequestMapping(value="/{lsType}/{lsKind}", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@PathVariable("lsType") String lsType, 
    		@PathVariable("lsKind") String lsKind,
    		@RequestParam(value="parentIdOrCodeName", required = false) String parentIdOrCodeName,
    		@RequestBody LsThing lsThing) {
       //headers and setup
		logger.debug("----from the LsThing POST controller----");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        boolean errorsFound = false;
        //decide what kind of validation we need to do
        boolean isParent = false;
        boolean isBatch = false;
        boolean isAssembly = false;
        boolean isComponent = false;
        if (lsType.equals("parent")) isParent = true;
        if (lsType.equals("batch")) isBatch = true;
        List<String> assemblyKinds = Arrays.asList("internalization agent", "RNA", "polymer", "linker","formulation");
        List<String> componentKinds = Arrays.asList("protein", "cationic block", "spacer", "linker small molecule");
        if (assemblyKinds.contains(lsKind)) isAssembly=true;
        if (componentKinds.contains(lsKind)) isComponent=true;
        //do required validation
        if (isComponent){
        	if (!lsThingService.validateComponentName(lsThing)){
	            ErrorMessage error = new ErrorMessage();
	            error.setErrorLevel("error");
	            error.setMessage("not unique lsThing name");
	            errors.add(error);
	            errorsFound = true;
        	}
        } else if(isAssembly){
        	if (!lsThingService.validateAssembly(lsThing)){
                ErrorMessage error = new ErrorMessage();
                error.setErrorLevel("error");
                error.setMessage("not unique assembly lsThing");
                errors.add(error);
                errorsFound = true;
            }
        }
        LsThing parent;
        //if it's a batch, try to make an appropriate codeName
        if (isBatch && parentIdOrCodeName == null){
        	ErrorMessage error = new ErrorMessage();
            error.setErrorLevel("error");
            error.setMessage("must provide parentIdOrCodeName to save a batch");
            errors.add(error);
            errorsFound = true;
        }else if (isBatch){
            if(SimpleUtil.isNumeric(parentIdOrCodeName)) {
    			parent = LsThing.findLsThing(Long.valueOf(parentIdOrCodeName));
    		} else {		
    			try {
    				parent = LsThing.findLsThingsByCodeNameEquals(parentIdOrCodeName).getSingleResult();
    			} catch(Exception ex) {
    				parent = null;
    				ErrorMessage error = new ErrorMessage();
    	            error.setErrorLevel("error");
    	            error.setMessage("parent:" + parentIdOrCodeName +" not found");
    	            errors.add(error);
    	            errorsFound = true;
    			}
    		}
            lsThing.setCodeName(lsThingService.generateBatchCodeName(parent));
        }
        //if all's well so far, go ahead with the save
        if (!errorsFound){
        	//if it's a batch or an assembly, we don't care about name uniqueness
        	if (isAssembly | isBatch){
        		try {
            		lsThing = lsThingService.saveLsThing(lsThing, false);
            	} catch (UniqueNameException e) {}
        	}
        	//otherwise, decide whether to check for uniqueness based on property in properties file (in saveLsThing method)
        	else {
        		try {
                    lsThing = lsThingService.saveLsThing(lsThing);
                } catch (UniqueNameException e) {
                    logger.error("----from the controller----" + e.getMessage().toString() + " whole message  " + e.toString());
                    ErrorMessage error = new ErrorMessage();
                    error.setErrorLevel("error");
                    error.setMessage("not unique lsThing name");
                    errors.add(error);
                    errorsFound = true;
                }
        	}	
        }
        if (errorsFound) {
            return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<String>(lsThing.toJson(), headers, HttpStatus.CREATED);
        }
    }
    
    @RequestMapping(value = "/validatename", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> validateName(@RequestParam(value = "lsKind", required = true) String lsKind, 
    		@RequestBody List<String> names) {
        boolean isComponent = false;
        boolean isAssembly = false;
    	List<String> assemblyKinds = Arrays.asList("internalization agent", "RNA", "polymer", "linker","formulation");
        List<String> componentKinds = Arrays.asList("protein", "cationic block", "spacer", "linker small molecule");
        if (componentKinds.contains(lsKind)) isComponent=true;
        if (assemblyKinds.contains(lsKind)) isAssembly=true;
        boolean isValid = false;
        if (isComponent){
        	String componentName = names.get(0);
        	isValid = lsThingService.validateComponentName(componentName);
        }
        if (isAssembly){
        	List<String> componentCodeNames = names;
        	isValid = lsThingService.validateAssembly(componentCodeNames);
        }
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(String.valueOf(isValid), headers, HttpStatus.OK);
    }
    
    
    @Transactional
    @RequestMapping(value="/{lsType}/{lsKind}/{idOrCodeName}", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@PathVariable("lsType") String lsType, 
    		@PathVariable("lsKind") String lsKind,
    		@PathVariable("idOrCodeName") String idOrCodeName,
    		@RequestBody LsThing lsThing) {
       //headers and setup
		logger.debug("----from the LsThing PUT controller----");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        boolean errorsFound = false;
        //decide what kind of validation we need to do
        boolean isAssembly = false;
        boolean isComponent = false;
        List<String> assemblyKinds = Arrays.asList("internalization agent", "RNA", "polymer", "linker","formulation");
        List<String> componentKinds = Arrays.asList("protein", "cationic block", "spacer", "linker small molecule");
        if (assemblyKinds.contains(lsKind)) isAssembly=true;
        if (componentKinds.contains(lsKind)) isComponent=true;
        //do required validation
        if (isComponent){
        	if (!lsThingService.validateComponentName(lsThing)){
	            ErrorMessage error = new ErrorMessage();
	            error.setErrorLevel("error");
	            error.setMessage("not unique lsThing name");
	            errors.add(error);
	            errorsFound = true;
        	}
        } else if(isAssembly){
        	if (!lsThingService.validateAssembly(lsThing)){
                ErrorMessage error = new ErrorMessage();
                error.setErrorLevel("error");
                error.setMessage("not unique assembly lsThing");
                errors.add(error);
                errorsFound = true;
            }
        }
        //if all's well so far, go ahead with the save
        if (!errorsFound){
        	//if it's a batch or an assembly, we don't care about name uniqueness
    		try {
        		lsThing = lsThingService.updateLsThing(lsThing);
        	} catch (Exception e) {
        		logger.error("----from the controller----"
						+ e.getMessage().toString() + " whole message  "
						+ e.toString());
				ErrorMessage error = new ErrorMessage();
				error.setErrorLevel("error");
				error.setMessage("internal error occurred while trying to update lsThing");
				errors.add(error);
				errorsFound = true;
        	}
        }
        if (errorsFound) {
            return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<String>(lsThing.toJson(), headers, HttpStatus.CREATED);
        }
    }


	
}
