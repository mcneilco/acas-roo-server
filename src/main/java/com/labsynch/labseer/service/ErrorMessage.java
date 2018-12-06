package com.labsynch.labseer.service;

import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ErrorMessage {

    @Size(max = 50)
    private String level;
    
    @Size(max = 255)
    private String message;
    
    public ErrorMessage(String level, String message){
    	this.level = level;
    	this.message = message;
    }

	public ErrorMessage() {
	}
    
}
