package com.labsynch.labseer.exceptions;

import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ErrorMessage {

    @Size(max = 50)
    private String errorLevel;
    
    @Size(max = 255)
    private String message;
    
}