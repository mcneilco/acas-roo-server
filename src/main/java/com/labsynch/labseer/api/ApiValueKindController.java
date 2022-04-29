package com.labsynch.labseer.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.NoResultException;

import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.domain.ValueType;
import com.labsynch.labseer.dto.ValueTypeKindDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Transactional
@RequestMapping("api/v1/valuekinds")
@Controller

public class ApiValueKindController {

	private static final Logger logger = LoggerFactory.getLogger(ApiValueKindController.class);

	@RequestMapping(value = "/getOrCreate/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> getOrCreateValueKinds(@RequestBody String json ){
		Collection<ValueTypeKindDTO> valueTypeKindDTOs = ValueTypeKindDTO.fromJsonArrayToValueTypeKindDTO(json);
		Collection<ValueKind> valueKinds = new HashSet<ValueKind>();
        for (ValueTypeKindDTO typeKind : valueTypeKindDTOs){
        	String lsType = typeKind.getLsType();
        	String lsKind = typeKind.getLsKind();
        	ValueType valueType;
        	try{
        		valueType = ValueType.findValueTypesByTypeNameEquals(lsType).getSingleResult();
        	} catch(NoResultException e){
        		valueType = new ValueType();
        		valueType.setTypeName(lsType);
        		valueType.persist();
        	}
        	ValueKind valueKind;
        	try{
        		valueKind = ValueKind.findValueKindsByKindNameEqualsAndLsType(lsKind, valueType).getSingleResult();
        		valueKinds.add(valueKind);
        	} catch(NoResultException e){
        		valueKind = new ValueKind();
        		valueKind.setKindName(lsKind);
        		valueKind.setLsType(valueType);
        		valueKind.setLsTypeAndKind(lsType+"_"+lsKind);
        		valueKind.persist();
        		valueKinds.add(valueKind);
        	}
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        return new ResponseEntity<String>(ValueKind.toJsonArray(valueKinds), headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/get/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> getValueKindsWithValueTypeKindDTO(@RequestBody String json){
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
		logger.info("incoming json:"+json);
        List<ValueTypeKindDTO> valueTypeKindDTOs = (List<ValueTypeKindDTO>) ValueTypeKindDTO.fromJsonArrayToValueTypeKindDTO(json);
        try{
			for (ValueTypeKindDTO valueTypeKindDTO : valueTypeKindDTOs){
				valueTypeKindDTO.findValueKind();
				logger.info(valueTypeKindDTO.toJson());
			}
	        return new ResponseEntity<String>(ValueTypeKindDTO.toJsonArray(valueTypeKindDTOs), headers, HttpStatus.OK);
		}catch (Exception e){
			logger.error(e.toString());
			return new ResponseEntity<String>(e.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
}
