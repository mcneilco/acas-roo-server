package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ValueQueryDTO {

	String stateType;
	String stateKind;
	String valueType;
	String valueKind;
	String value;
	
	public ValueQueryDTO(){
		
	}
}
