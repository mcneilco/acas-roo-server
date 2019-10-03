package com.labsynch.labseer.dto;

import java.util.Collection;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ValidationResponseDTO {
		
    private String level;
    private String message;
    
    public ValidationResponseDTO(String level, String message){
    	this.level = level;
    	this.message = message;
    }

    public String toJson() {
        return new JSONSerializer()
        .include("level", "message").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
}
