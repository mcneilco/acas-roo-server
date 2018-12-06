package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class PurgeFileResponseDTO {

    private String summary;
    
    private boolean success;
    
    private String fileName;
    
    public PurgeFileResponseDTO(){
    	
    }
    
    public PurgeFileResponseDTO(String summary, boolean success, String fileName){
    	this.summary = summary;
    	this.success = success;
    	this.fileName = fileName;
    }
    
    public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
}
