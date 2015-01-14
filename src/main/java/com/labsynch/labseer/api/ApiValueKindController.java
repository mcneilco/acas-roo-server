package com.labsynch.labseer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.domain.ValueType;
import com.labsynch.labseer.dto.ValueTypeKindDTO;

@Transactional
@RequestMapping("api/v1")
@Controller

public class ApiValueKindController {

	private static final Logger logger = LoggerFactory.getLogger(ApiValueKindController.class);

	@RequestMapping(value = "/valuekinds/getOrCreate/jsonArray", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<String> getOrCreateValueKinds(@RequestBody List<ValueTypeKindDTO> valueTypeKindDTOs ){
        Collection<ValueKind> valueKinds = new HashSet<ValueKind>();
        for (ValueTypeKindDTO typeKind : valueTypeKindDTOs){
        	String lsType = typeKind.getLsType();
        	String lsKind = typeKind.getLsKind();
        	ValueType valueType;
        	try{
        		valueType = ValueType.findValueTypesByTypeNameEquals(lsType).getSingleResult();
        	} catch(EmptyResultDataAccessException e){
        		valueType = new ValueType();
        		valueType.setTypeName(lsType);
        		valueType.persist();
        	}
        	ValueKind valueKind;
        	try{
        		valueKind = ValueKind.findValueKindsByKindNameEqualsAndLsType(lsKind, valueType).getSingleResult();
        		valueKinds.add(valueKind);
        	} catch(EmptyResultDataAccessException e){
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
	
	
}
