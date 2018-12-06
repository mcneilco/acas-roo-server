package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ParentLotCodeDTO {
	
	private String requestName;
	
	private String referenceCode;
	
	private Collection<String> lotCodes;
	
	public ParentLotCodeDTO(){
		
	}
	
	public String toJson() {
        return new JSONSerializer()
        .include("lotCodes")
        .exclude("*.class").serialize(this);
    }
	
	public static String toJsonArray(Collection<ParentLotCodeDTO> collection) {
        return new JSONSerializer()
        .include("lotCodes")
        .exclude("*.class").serialize(collection);
    }

}
