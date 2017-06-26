package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class RenderMolRequestDTO {
	
	private String codeName;
	
	private String molStructure;
	
	private Integer height;
	
	private Integer width;
	
	private String format;

}


