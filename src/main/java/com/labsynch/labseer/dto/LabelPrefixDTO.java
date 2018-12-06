package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class LabelPrefixDTO {

	private Long id;
	
	private String code;
	
	private String name;
	
	private Boolean ignore;
	
	private String labelTypeAndKind;
	
	private String thingTypeAndKind;
	
	private String labelPrefix;
	
	private Long numberOfLabels;
	
	
	public String toSafeJson() {
		return new JSONSerializer().exclude("*.class", "id", "code", "name", "ignore").serialize(this);
	}
}
