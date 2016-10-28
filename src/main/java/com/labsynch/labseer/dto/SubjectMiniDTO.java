package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.Subject;

@RooJavaBean
@RooToString
@RooJson
public class SubjectMiniDTO {
	
    public SubjectMiniDTO(Subject subject) {
    	this.setId(subject.getId());
    	this.setVersion(subject.getVersion());
    	subject.getCodeName();
    }

	private Long id;
    
	private Integer version;
	
	private String codeName;



}


