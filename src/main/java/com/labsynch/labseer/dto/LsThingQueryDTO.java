package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class LsThingQueryDTO {

	Date recordedDateGreaterThan;
	
	Date recordedDateLessThan;
	
	String recordedBy;
	
	String lsType;
	
	String lsKind;
	
	Integer maxResults;
	
	Collection<ItxQueryDTO> firstInteractions;
	
	Collection<ItxQueryDTO> secondInteractions;
	
	Collection<ValueQueryDTO> values;
	
	Collection<LabelQueryDTO> labels;
	
	public LsThingQueryDTO(){
		
	}
}
