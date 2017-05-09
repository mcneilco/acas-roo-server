package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class MultiContainerSubjectSearchRequest {
	
	Integer maxResults;

	String protocolLabelLike;
	
	String experimentLabelLike;
	
	Collection<String> containerCodes;
	
	String subjectType;
	
	String subjectKind;

	Collection<ValueQueryDTO> values;
	
	public MultiContainerSubjectSearchRequest(){
		
	}
}
