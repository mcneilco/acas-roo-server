package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class BulkLoadSDFPropertyRequestDTO {

    private String fileName;
    
    private int numRecords;
    
    private String templateName;
    
    private String userName;
    
    private Collection<BulkLoadPropertyMappingDTO> mappings;
    
    public BulkLoadSDFPropertyRequestDTO(){
    	
    }
    
    public BulkLoadSDFPropertyRequestDTO(String fileName, int numRecords, String templateName, String userName, Collection<BulkLoadPropertyMappingDTO> mappings){
    	this.fileName = fileName;
    	this.numRecords = numRecords;
    	this.templateName = templateName;
    	this.userName = userName;
    	this.mappings = mappings;
    }
    
}
