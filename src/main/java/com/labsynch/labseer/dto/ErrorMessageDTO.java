package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ErrorMessageDTO {

	String level;
	
	String message;
	
	public ErrorMessageDTO(){
		
	}
	
	public ErrorMessageDTO(String level, String message){
		this.level = level;
		this.message = message;
	}
	
}
