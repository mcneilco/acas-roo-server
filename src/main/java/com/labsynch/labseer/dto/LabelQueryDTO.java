package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class LabelQueryDTO {

	String labelText;
	String labelType;
	String labelKind;
	
	public LabelQueryDTO(){
		
	}
}
