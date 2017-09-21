package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class DateValueComparisonRequest {
	
	private String lsType;
	
	private String lsKind;
	
	private String stateType;
	
	private String stateKind;
	
	private String valueKind;
	
	private Integer secondsDelta;
	
	private Boolean newerThanModified; 
	
}


