package com.labsynch.labseer.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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

import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
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
    
    @RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> genericLsThingSearch(@RequestParam(value="lsType", required = false) String lsType, @RequestParam("q") String searchQuery) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try {
			String result = LsThing.toJsonArrayStub(lsThingService.findLsThingsByGenericMetaDataSearch(lsType, searchQuery));
			return new ResponseEntity<String>(result, headers, HttpStatus.OK);
		} catch(Exception e){
			String error = e.getMessage() + e.getStackTrace();
			return new ResponseEntity<String>(error, headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    
    @RequestMapping(value = "/getGeneCodeNameFromNameRequest", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getGeneCodeNameFromName(@RequestBody String json) {
    	PreferredNameRequestDTO requestDTO = PreferredNameRequestDTO.fromJsonToPreferredNameRequestDTO(json);
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
    public ResponseEntity<java.lang.String> getCodeNameFromName(@RequestBody String json, 
    		@RequestParam(value = "thingType", required = true) String thingType, 
    		@RequestParam(value = "thingKind", required = true) String thingKind, 
    		@RequestParam(value = "labelType", required = false) String labelType, 
    		@RequestParam(value = "labelKind", required = false) String labelKind) {
    	PreferredNameRequestDTO requestDTO = PreferredNameRequestDTO.fromJsonToPreferredNameRequestDTO(json);
        logger.info("getCodeNameFromNameRequest incoming json: " + requestDTO.toJson());
        PreferredNameResultsDTO results = lsThingService.getCodeNameFromName(thingType, thingKind, labelType, labelKind, requestDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(results.toJson(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/getPreferredNameFromNameRequest", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getPreferredNameFromName(@RequestBody String json, @RequestParam(value = "thingType", required = true) String thingType, @RequestParam(value = "thingKind", required = true) String thingKind, @RequestParam(value = "labelType", required = false) String labelType, @RequestParam(value = "labelKind", required = false) String labelKind) {
    	PreferredNameRequestDTO requestDTO = PreferredNameRequestDTO.fromJsonToPreferredNameRequestDTO(json);
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
    
    @RequestMapping(value = "/codetable", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<String> getCodeTableLsThings(@RequestParam(value = "lsType", required = true) String lsType,
    		@RequestParam(value = "lsKind", required = true) String lsKind){
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Content-Type", "application/json; charset=utf-8");
    	try{
        	Collection<CodeTableDTO> codeTableLsThings = lsThingService.getCodeTableLsThings(lsType, lsKind, true);
            return new ResponseEntity<String>(CodeTableDTO.toJsonArray(codeTableLsThings), headers, HttpStatus.OK);
    	} catch (Exception e){
    		logger.error(e.toString());
    		return new ResponseEntity<String>(e.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
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
			if (with.equalsIgnoreCase("nestedfull")) {
				return new ResponseEntity<String>(LsThing.toJsonArrayWithNestedFull(results), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(LsThing.toJsonArrayPretty(results), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("nestedstub")) {
				return new ResponseEntity<String>(LsThing.toJsonArrayWithNestedStubs(results), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("stub")) {
				return new ResponseEntity<String>(LsThing.toJsonArrayStub(results), headers, HttpStatus.OK);
			}
		}
    	return new ResponseEntity<String>(LsThing.toJsonArray(results), headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{lsType}/{lsKind}/{idOrCodeName}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getLsThingByIdCodeName(@PathVariable("lsType") String lsType, 
    		@PathVariable("lsKind") String lsKind,
    		@PathVariable("idOrCodeName") String idOrCodeName,
    		@RequestParam(value = "with", required = false) String with) {
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
        	if (with != null) {
    			if (with.equalsIgnoreCase("nestedfull")) {
    				return new ResponseEntity<String>(lsThing.toJsonWithNestedFull(), headers, HttpStatus.OK);
    			} else if (with.equalsIgnoreCase("prettyjson")) {
    				return new ResponseEntity<String>(lsThing.toPrettyJson(), headers, HttpStatus.OK);
    			} else if (with.equalsIgnoreCase("nestedstub")) {
    				return new ResponseEntity<String>(lsThing.toJsonWithNestedStubs(), headers, HttpStatus.OK);
    			} else if (with.equalsIgnoreCase("stub")) {
    				return new ResponseEntity<String>(lsThing.toJsonStub(), headers, HttpStatus.OK);
    			}
    		}
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
    

//	@Transactional
    @RequestMapping(value="/{lsType}/{lsKind}", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@PathVariable("lsType") String lsType, 
    		@PathVariable("lsKind") String lsKind,
    		@RequestParam(value="parentIdOrCodeName", required = false) String parentIdOrCodeName,
    		@RequestParam(value="with", required = false) String with,
    		@RequestBody String json) {
       //headers and setup
		logger.debug("----from the LsThing POST controller----");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        boolean errorsFound = false;
        Long parentId = null;
        LsThing lsThing = LsThing.fromJsonToLsThing(json);
        //decide what kind of validation we need to do
        boolean isParent = false;
        boolean isBatch = false;
        boolean isAssembly = false;
        boolean isComponent = false;
        if (lsType.equals("parent")) isParent = true;
        if (lsType.equals("batch")) isBatch = true;
        List<String> assemblyKinds = propertiesUtilService.getAssemblyKindList();
        List<String> componentKinds = propertiesUtilService.getComponentKindList();
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
            parentId = parent.getId();
        }
        //if all's well so far, go ahead with the save
        if (!errorsFound){
        		try {
        			lsThing = lsThingService.saveLsThing(lsThing, isParent, isBatch, isAssembly, isComponent, parentId);
                } catch (Exception e) {
                    logger.error("----from the POST controller----" + " ERROR:  " + e.toString());
                    ErrorMessage error = new ErrorMessage();
                    error.setErrorLevel("error");
                    error.setMessage("error occurred during saving");
                    errors.add(error);
                    errorsFound = true;
                }
        	}	
        if (errorsFound) {
            return new ResponseEntity<String>(ErrorMessage.toJsonArray(errors), headers, HttpStatus.CONFLICT);
        }else if (with != null) {
			if (with.equalsIgnoreCase("nestedfull")) {
				return new ResponseEntity<String>(lsThing.toJsonWithNestedFull(), headers, HttpStatus.CREATED);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(lsThing.toPrettyJson(), headers, HttpStatus.CREATED);
			} else if (with.equalsIgnoreCase("nestedstub")) {
				return new ResponseEntity<String>(lsThing.toJsonWithNestedStubs(), headers, HttpStatus.CREATED);
			} else if (with.equalsIgnoreCase("stub")) {
				return new ResponseEntity<String>(lsThing.toJsonStub(), headers, HttpStatus.CREATED);
			}
		}
        return new ResponseEntity<String>(lsThing.toJson(), headers, HttpStatus.CREATED);
    }
    
    @RequestMapping(value = "/validatename", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> validateName(@RequestParam(value = "lsKind", required = true) String lsKind, 
    		@RequestBody List<String> names) {
        boolean isComponent = false;
        boolean isAssembly = false;
    	List<String> assemblyKinds = propertiesUtilService.getAssemblyKindList();
        List<String> componentKinds = propertiesUtilService.getComponentKindList();
        logger.debug(assemblyKinds.toString());
        logger.debug(componentKinds.toString());
        if (componentKinds.contains(lsKind)) isComponent=true;
        if (assemblyKinds.contains(lsKind)) isAssembly=true;
        boolean isValid = false;
        if (isComponent){
        	String componentName = names.get(0);
        	logger.debug("Validating "+lsKind+" name: "+componentName);
        	isValid = lsThingService.validateComponentName(componentName, lsKind);
        }
        else if (isAssembly){
        	List<String> componentCodeNames = names;
        	isValid = lsThingService.validateAssembly(componentCodeNames);
        }
        else {
        	isValid = true;
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
    		@RequestParam(value="with", required = false) String with,
    		@RequestBody String json) {
       //headers and setup
		logger.debug("----from the LsThing PUT controller----");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
        boolean errorsFound = false;
        LsThing lsThing = LsThing.fromJsonToLsThing(json);
        //decide what kind of validation we need to do
        boolean isAssembly = false;
        boolean isComponent = false;
        List<String> assemblyKinds = propertiesUtilService.getAssemblyKindList();
        List<String> componentKinds = propertiesUtilService.getComponentKindList();
        if (assemblyKinds.contains(lsKind)) isAssembly=true;
        if (componentKinds.contains(lsKind)) isComponent=true;
        //do required validation
        if (isComponent){
        	Collection<String> oldLabelTexts = new ArrayList<String>();
        	for (LsThingLabel label : LsThing.findLsThing(lsThing.getId()).getLsLabels()){
        		oldLabelTexts.add(label.getLabelText());
        	}
        	Collection<String> newLabelTexts = new ArrayList<String>();
        	for (LsThingLabel label : lsThing.getLsLabels()){
        		newLabelTexts.add(label.getLabelText());
        	}
        	boolean nameChanged = false;
        	for (String newLabelText : newLabelTexts){
        		if (!oldLabelTexts.contains(newLabelText)) nameChanged=true;
        	}
        	if (nameChanged){
        		if (!lsThingService.validateComponentName(lsThing)){
    	            ErrorMessage error = new ErrorMessage();
    	            error.setErrorLevel("error");
    	            error.setMessage("not unique lsThing name");
    	            errors.add(error);
    	            errorsFound = true;
            	}
        	}
        } else if(isAssembly){
        	List<String> oldComponents = lsThingService.getComponentCodeNamesFromNewAssembly(LsThing.findLsThing(lsThing.getId()));
        	List<String> newComponents = lsThingService.getComponentCodeNamesFromNewAssembly(lsThing);
        	boolean componentsChanged = (!oldComponents.equals(newComponents));
        	if (componentsChanged) {
        		if (!lsThingService.validateAssembly(lsThing)){
                    ErrorMessage error = new ErrorMessage();
                    error.setErrorLevel("error");
                    error.setMessage("not unique assembly lsThing");
                    errors.add(error);
                    errorsFound = true;
                }
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
        }else if (with != null) {
			if (with.equalsIgnoreCase("nestedfull")) {
				return new ResponseEntity<String>(lsThing.toJsonWithNestedFull(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("prettyjson")) {
				return new ResponseEntity<String>(lsThing.toPrettyJson(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("nestedstub")) {
				return new ResponseEntity<String>(lsThing.toJsonWithNestedStubs(), headers, HttpStatus.OK);
			} else if (with.equalsIgnoreCase("stub")) {
				return new ResponseEntity<String>(lsThing.toJsonStub(), headers, HttpStatus.OK);
			}
		}
        return new ResponseEntity<String>(lsThing.toJson(), headers, HttpStatus.OK);
    }

  @RequestMapping(value = "/gene/v1/loadGeneEntities", method = RequestMethod.POST, headers = "Accept=application/json")
  public ResponseEntity<java.lang.String> loadGeneEntities(@RequestParam(value = "fileName", required = true) String fileName) {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", "application/json; charset=utf-8");
      logger.info("loading genes from tab delimited file: " + fileName);
      try {
          geneThingService.RegisterGeneThingsFromCSV(fileName);
      } catch (IOException e) {
          logger.error("IOException: " + e.toString());
          return new ResponseEntity<String>("ERROR: IOError. Unable to load file. " + fileName, headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
      }
      return new ResponseEntity<String>(headers, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/documentmanagersearch", method = RequestMethod.GET)
  public ResponseEntity<java.lang.String> documentManagerSearch(@RequestParam Map<String,String> searchParamsMap){
	  HttpHeaders headers = new HttpHeaders();
      headers.add("Content-Type", "application/json");
      Collection<LsThing> results = new HashSet<LsThing>();
      try{
    	  results = lsThingService.searchForDocumentThings(searchParamsMap);
      }catch (Exception e){
    	  logger.error("Caught error in documentManagerSearch: " + e.toString());
    	  return new ResponseEntity<String>(e.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return new ResponseEntity<String>(LsThing.toJsonArray(results), headers, HttpStatus.OK);
  }
	
}
