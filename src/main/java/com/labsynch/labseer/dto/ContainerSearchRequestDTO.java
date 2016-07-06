package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ContainerSearchRequestDTO {

	private String barcode;
	
	private String definition;
	
	private String status;
	
	private String description;
	
	private String createdUser;
	
	private String type;
	
//	private String requestId;
	
	private String lsType;
	
	private String lsKind;
	
	
	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
	
}
