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
    private int record;
    private String categoryCode;
    private String categoryDescription;
    private String message;
    
    public ValidationResponseDTO(String level, int record, String categoryCode, String categoryDescription, String message){
    	this.level = level;
    	this.record = record;
    	this.categoryCode = categoryCode;
    	this.categoryDescription = categoryDescription;
    	this.message = message;
    }

    public String toJson() {
        return new JSONSerializer()
        .include("level", "record", "categoryCode", "categoryDescription", "message").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }

}
