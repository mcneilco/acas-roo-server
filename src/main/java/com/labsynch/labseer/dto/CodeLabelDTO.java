package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class CodeLabelDTO {
	
	private List<String> foundCodeNames;
	
	private String level;
	
	private String message;
	
	private String requestLabel;
	
	public CodeLabelDTO(){
	}
	
	public CodeLabelDTO(String requestLabel, List<String> foundCodeNames){
		this.requestLabel = requestLabel;
		this.foundCodeNames = foundCodeNames;
	}
	
	public CodeLabelDTO(String requestLabel, String level, String message){
		this.requestLabel = requestLabel;
		this.level = level;
		this.message = message;
	}
	
	public String toJson() {
        return new JSONSerializer().include("foundCodeNames").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<CodeLabelDTO> collection) {
        return new JSONSerializer().include("foundCodeNames").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	

}


