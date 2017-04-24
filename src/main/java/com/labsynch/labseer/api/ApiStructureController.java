package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.NoResultException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.Structure;
import com.labsynch.labseer.dto.MolPropertiesDTO;
import com.labsynch.labseer.dto.RenderMolRequestDTO;
import com.labsynch.labseer.dto.StructureSearchDTO;
import com.labsynch.labseer.exceptions.NotFoundException;
import com.labsynch.labseer.service.LsThingService;
import com.labsynch.labseer.service.StructureService;

@Controller
@RequestMapping("api/v1/structure")
@Transactional
public class ApiStructureController {
	private static final Logger logger = LoggerFactory.getLogger(ApiStructureController.class);

	@Autowired
	private StructureService structureService;
	
	@Autowired
	private LsThingService lsThingService;

	@RequestMapping(value = "/renderMolStructure", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<byte[]> renderMolStructure(@RequestBody String json) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);
	    headers.add("Access-Control-Allow-Headers", "Content-Type");
	    headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		
		RenderMolRequestDTO request = RenderMolRequestDTO.fromJsonToRenderMolRequestDTO(json);
		try{
			byte[] image = structureService.renderMolStructure(request.getMolStructure(), request.getHeight(), request.getWidth(), request.getFormat());  
			return new ResponseEntity<byte[]>(image, headers, HttpStatus.OK);
		}catch (Exception e){
			logger.error("Caught exception in renderMolStructure",e);
			return new ResponseEntity<byte[]>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/calculateMoleculeProperties", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> calculateMoleculeProperties(@RequestBody String json){
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        
        RenderMolRequestDTO request = RenderMolRequestDTO.fromJsonToRenderMolRequestDTO(json);
        try{
        	MolPropertiesDTO responseDTO = structureService.calculateMoleculeProperties(request.getMolStructure());
        	return new ResponseEntity<String>(responseDTO.toJson(), headers, HttpStatus.OK);
        }catch (Exception e){
        	logger.error("Caught exception in calculateMoleculeProperties",e);
			return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
	}
	
	@Transactional
    @RequestMapping(value = "/getByCodeName/{codeName}", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> getByCodeName(
    		@PathVariable("codeName") String codeName) {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
    	try{
    		Structure structure = Structure.findStructureByCodeName(codeName);
            return new ResponseEntity<String>(structure.toJson(), headers, HttpStatus.OK);
    	}catch (EmptyResultDataAccessException empty){
    		return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
    	}catch (Exception e){
    		logger.error("Caught exception saving structure",e);
    		return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
        
    }
	
	@RequestMapping(value = "/renderStructureByCodeName", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<byte[]> renderStructureByCodeName(@RequestParam(value="codeName", required=true) String codeName,
			@RequestParam(value="height", required=true) int height,
			@RequestParam(value="width", required=true) int width,
			@RequestParam(value="format", required=true) String format) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);
	    headers.add("Access-Control-Allow-Headers", "Content-Type");
	    headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		
		try{
			byte[] image = structureService.renderStructureByCodeName(codeName, height, width, format);  
			return new ResponseEntity<byte[]>(image, headers, HttpStatus.OK);
		}catch (EmptyResultDataAccessException empty){
			return new ResponseEntity<byte[]>(headers, HttpStatus.NOT_FOUND);
		}catch (Exception e){
			logger.error("Caught exception in renderStructureByCodeName",e);
			return new ResponseEntity<byte[]>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/renderStructureByLsThingCodeName", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<byte[]> renderStructureByLsThingCodeName(@RequestParam(value="codeName", required=true) String codeName,
			@RequestParam(value="height", required=true) int height,
			@RequestParam(value="width", required=true) int width,
			@RequestParam(value="format", required=true) String format) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_PNG);
	    headers.add("Access-Control-Allow-Headers", "Content-Type");
	    headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Cache-Control","no-store, no-cache, must-revalidate"); //HTTP 1.1
		headers.add("Pragma","no-cache"); //HTTP 1.0
		headers.setExpires(0); // Expire the cache
		
		try{
			byte[] image = lsThingService.renderStructureByLsThingCodeName(codeName, height, width, format);  
			return new ResponseEntity<byte[]>(image, headers, HttpStatus.OK);
		}catch (EmptyResultDataAccessException empty){
			return new ResponseEntity<byte[]>(headers, HttpStatus.NOT_FOUND);
		}catch (NotFoundException notFound){
			return new ResponseEntity<byte[]>(headers, HttpStatus.NOT_FOUND);
		}catch (Exception e){
			logger.error("Caught exception in renderStructureByLsThingCodeName",e);
			return new ResponseEntity<byte[]>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Transactional
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJson(@RequestBody String json) {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
    	try{
    		Structure structure = Structure.fromJsonToStructure(json);
    		structure = structureService.saveStructure(structure);
    		structure.flush();
            return new ResponseEntity<String>(structure.toJson(), headers, HttpStatus.CREATED);
    	}catch (Exception e){
    		logger.error("Caught exception saving structure",e);
    		return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
        
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> createFromJsonArray(@RequestBody String json) {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        try{
        	Collection<Structure> structures = Structure.fromJsonArrayToStructures(json);
            Collection<Structure> savedStructures = new ArrayList<Structure>();
        	for (Structure structure : structures) {
                Structure savedStructure = structureService.saveStructure(structure);
                savedStructures.add(savedStructure);
            }
            return new ResponseEntity<String>(Structure.toJsonArray(savedStructures), headers, HttpStatus.CREATED);
        }catch (Exception e){
    		logger.error("Caught exception saving structure",e);
    		return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
    }

	@Transactional
    @RequestMapping(value = { "/{id}", "/" }, method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> updateFromJson(@RequestBody String json) {
        Structure structure = Structure.fromJsonToStructure(json);
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        structure = structureService.updateStructure(structure);
        if (structure.getId() == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(structure.toJson(), headers, HttpStatus.OK);
    }

	@RequestMapping(value = "/jsonArray", method = RequestMethod.PUT, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> updateFromJsonArray(@RequestBody String json) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		try{
			Collection<Structure> structures = Structure.fromJsonArrayToStructures(json);
			Collection<Structure> updatedStructures = new ArrayList<Structure>();
			for (Structure structure : structures) {
				Structure updatedStructure = structureService.saveStructure(structure);
				updatedStructures.add(updatedStructure);
			}
            return new ResponseEntity<String>(Structure.toJsonArray(updatedStructures), headers, HttpStatus.OK);
        }catch (Exception e){
    		logger.error("Caught exception updating structures",e);
    		return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<java.lang.String> deleteFromJson(@PathVariable("id") Long id) {
		Structure structure = Structure.findStructure(id);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		if (structure == null) {
			return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
		}
		structure.remove();
		return new ResponseEntity<String>(headers, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/searchStructures", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<java.lang.String> searchStructures(@RequestBody String json) {
    	HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=utf-8");
        Collection<Structure> structures;
        try{
        	logger.info("##################################################################");

        	logger.info("incoming JSON: " + json);
        	StructureSearchDTO query = StructureSearchDTO.fromJsonToStructureSearchDTO(json);
        	logger.info("incoming query: " + query.getLsType() + "  " + query.getLsKind());
        	if (query.getLsType() != null && query.getLsKind() != null){
            	structures = structureService.searchStructuresByTypeKind( query.getQueryMol(), query.getLsType(), query.getLsKind(), query.getSearchType(), query.getMaxResults(), query.getSimilarity());
        	} else {
            	structures = structureService.searchStructures( query.getQueryMol(), query.getSearchType(), query.getMaxResults(), query.getSimilarity());
        	}
            return new ResponseEntity<String>(Structure.toJsonArray(structures), headers, HttpStatus.OK);
        }catch (Exception e){
    		logger.error("Caught exception saving structure",e);
    		return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    	
    }
	
}
