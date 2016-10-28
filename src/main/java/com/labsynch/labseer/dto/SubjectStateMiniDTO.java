package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.SubjectState;

@RooJavaBean
@RooToString
@RooJson
public class SubjectStateMiniDTO {
	
    public SubjectStateMiniDTO(SubjectState subjectState) {
    	this.setId(subjectState.getId());
    }

	private Long id;

}

