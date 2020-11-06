package com.labsynch.labseer.service;

import javax.validation.constraints.Size;


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
