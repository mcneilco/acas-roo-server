package com.labsynch.labseer.exceptions;

import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class LsThingValidationErrorMessage {

    @Size(max = 50)
    private String errorLevel;
    
    @Size(max = 255)
    private String message;
    
    @Size(max = 50)
    private String matchingThingCodeName;
    
    @Size(max = 50)
	private String matchingThingCorpName;
    
    public LsThingValidationErrorMessage(UniqueInteractionsException e){
    	this.setErrorLevel("error");
        this.setMessage(e.getMessage());
        this.setMatchingThingCodeName(e.getMatchingThingCodeName());
        this.setMatchingThingCorpName(e.getMatchingThingCorpName());
    }
    
    public LsThingValidationErrorMessage(UniqueNameException e){
    	this.setErrorLevel("error");
        this.setMessage(e.getMessage());
        this.setMatchingThingCodeName(e.getMatchingThingCodeName());
        this.setMatchingThingCorpName(e.getMatchingThingCorpName());
    }
    
}