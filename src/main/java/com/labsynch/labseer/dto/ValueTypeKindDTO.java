package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ValueTypeKindDTO {
	
	private String lsType;
	
	private String lsKind;
	
	private int displayOrder;

	public ValueTypeKindDTO(
			String lsType, 
			String lsKind){
		
		this.lsType = lsType;
		this.lsKind = lsKind;
	}


	public ValueTypeKindDTO() {
	}
	
}


