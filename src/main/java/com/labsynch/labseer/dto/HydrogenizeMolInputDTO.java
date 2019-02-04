package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class HydrogenizeMolInputDTO {
	
	private String structure;
	
	private String inputFormat;
	
	private MethodOptionDTO parameters;

}
