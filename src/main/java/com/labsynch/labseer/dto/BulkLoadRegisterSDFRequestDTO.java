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
public class BulkLoadRegisterSDFRequestDTO {

    private String filePath;
            
    private String userName;
    
    private Date fileDate;
    
    private LabelPrefixDTO labelPrefix;
    
    private Collection<BulkLoadPropertyMappingDTO> mappings;
    
    public BulkLoadRegisterSDFRequestDTO(){
    	
    }
    
    public BulkLoadRegisterSDFRequestDTO(String filePath, String userName, Date fileDate, Collection<BulkLoadPropertyMappingDTO> mappings){
    	this.filePath = filePath;
    	this.userName = userName;
    	this.fileDate = fileDate;
    	this.mappings = mappings;
    }
    
    public String toJson() {
        return new JSONSerializer()
        .include("filePath", "userName", "mappings").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
}
