package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

@RooJavaBean
@RooToString
@RooJson
public class LDStandardizerOutputDTO {

    private HashMap<String, LDStandardizerOutputStructureDTO> structures;
//TODO need to get example messages array before parsing this    
//    private HashMap<String, String> messages;

    private String job_status;

	public String toJson() {
        return new JSONSerializer().include("structures")
        		.exclude("*.class")
        		.serialize(this);
    }

}