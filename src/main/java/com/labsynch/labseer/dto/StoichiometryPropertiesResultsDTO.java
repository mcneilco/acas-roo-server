package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class StoichiometryPropertiesResultsDTO {

	boolean hasError;
	
	boolean hasWarning;
	
	Collection<ErrorMessageDTO> errorMessages = new ArrayList<ErrorMessageDTO>();
	
	Collection<StoichiometryPropertiesDTO> results = new ArrayList<StoichiometryPropertiesDTO>();
	
	public String toJson() {
        return new JSONSerializer().include("errorMessages","results").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<StoichiometryPropertiesResultsDTO> collection) {
        return new JSONSerializer().include("errorMessages","results").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	
	public StoichiometryPropertiesResultsDTO(){
	}
	
}
