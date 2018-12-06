package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ExportResultDTO {

    private String summary;
    
    private String level;
    
    private String message;
    
    private String reportFilePath;
    
    public ExportResultDTO(){
    	
    }
    
    public ExportResultDTO(String summary, String reportFilePath){
    	this.summary = summary;
    	this.reportFilePath = reportFilePath;
    }
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
}
