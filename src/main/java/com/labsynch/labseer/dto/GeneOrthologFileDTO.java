package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class GeneOrthologFileDTO {
	
	private String versionName;
	private String testFileName;
    private String orthologType;
    private Long curationLevel;
    private String description;
    private String curator;
    private String recordedBy;
    
}
