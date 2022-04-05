package com.labsynch.labseer.dto;

import javax.persistence.NoResultException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.domain.ValueType;

@RooJavaBean
@RooToString
@RooJson
public class ValueTypeKindDTO {
	
	private String lsType;
	
	private String lsKind;
	
	private int displayOrder;
	
	private ValueKind valueKind;

	public ValueTypeKindDTO(
			String lsType, 
			String lsKind){
		
		this.lsType = lsType;
		this.lsKind = lsKind;
	}


	public ValueTypeKindDTO() {
	}
	
	@Transactional
	public void findValueKind(){
		ValueType valueType;
    	try{
    		valueType = ValueType.findValueTypesByTypeNameEquals(this.lsType).getSingleResult();
    	} catch(NoResultException e){
    		this.valueKind = null;
    		valueType = null;
    	}
    	ValueKind valueKind;
    	try{
    		valueKind = ValueKind.findValueKindsByKindNameEqualsAndLsType(this.lsKind, valueType).getSingleResult();
    		this.valueKind = valueKind;
    	} catch(NoResultException e){
    		this.valueKind = null;
    	}
	}
}


