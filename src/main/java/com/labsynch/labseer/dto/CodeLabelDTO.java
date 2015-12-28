package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class CodeLabelDTO {
	
	private String codeName;
	private String label;
	
	public CodeLabelDTO(){
	}
	
	public CodeLabelDTO(String codeName, String label){
		this.codeName = codeName;
		this.label = label;
	}
	

}


