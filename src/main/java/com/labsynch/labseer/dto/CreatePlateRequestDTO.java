package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class CreatePlateRequestDTO {

	private String barcode;
	
	private String definition;
	
	private String template;
	
	private String supplier;
	
	private String recordedBy;
	
	private Collection<WellContentDTO> wells;
	
}
