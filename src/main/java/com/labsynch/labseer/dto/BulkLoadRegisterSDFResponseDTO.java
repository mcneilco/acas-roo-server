package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class BulkLoadRegisterSDFResponseDTO {

    private String summary;
    
    private Collection<String> reportFiles;
    
    public BulkLoadRegisterSDFResponseDTO(){
    	
    }
    
    public BulkLoadRegisterSDFResponseDTO(String summary, Collection<String> reportFiles){
    	this.summary = summary;
    	this.reportFiles = reportFiles;
    }
    
    public String toJson() {
        return new JSONSerializer()
        .include("reportFiles").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
}
