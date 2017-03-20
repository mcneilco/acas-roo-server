package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.SubjectState;

@RooJavaBean
@RooToString
@RooJson
public class SubjectCodeStateDTO {
	
	private String subjectCode;
	private SubjectState subjectState;
	
	public SubjectCodeStateDTO(){
	}
	
	public SubjectCodeStateDTO(String subjectCode, SubjectState subjectState){
		this.subjectCode = subjectCode;
		this.subjectState = subjectState;
	}

}


