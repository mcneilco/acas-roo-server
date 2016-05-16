package com.labsynch.labseer.dto;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class TsvLoaderResponseDTO {

	private static final Logger logger = LoggerFactory.getLogger(TsvLoaderResponseDTO.class);

	public TsvLoaderResponseDTO(){
		//empty constructor
	}

	private Collection<TempThingDTO> analysisGroups;
	
	private Collection<TempThingDTO> treatmentGroups;
	
	private Collection<TempThingDTO> subjects;
	
	public String toJson() {
        return new JSONSerializer().include("analysisGroups","treatmentGroups","subjects").exclude("*.class").serialize(this);
    }
	
	public static String toJsonArray(Collection<TsvLoaderResponseDTO> collection) {
        return new JSONSerializer().include("analysisGroups","treatmentGroups","subjects").exclude("*.class").serialize(collection);
    }

}


