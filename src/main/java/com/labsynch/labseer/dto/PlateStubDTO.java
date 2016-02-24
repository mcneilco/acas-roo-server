package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class PlateStubDTO {
	
	private String barcode;
	private String codeName;
	
	private Collection<WellStubDTO> wells;
	
	public PlateStubDTO(){
	}
	
	public String toJson() {
        return new JSONSerializer().include("wells").exclude("*.class").serialize(this);
    }
	

}


