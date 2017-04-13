package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ItxQueryDTO {

	
	String interactionType;
	String interactionKind;
	String thingType;
	String thingKind;
	String thingLabelText;
	String thingLabelType;
	String thingLabelKind;
	String thingCodeName;
	String operator;
	
	public ItxQueryDTO(){
		
	}
}
