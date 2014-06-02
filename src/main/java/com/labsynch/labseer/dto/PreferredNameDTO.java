package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class PreferredNameDTO {
	
	public PreferredNameDTO() {		
	}
	
	public PreferredNameDTO(
			String requestName,
			String preferredName,
			String referenceName
			) {
		
		this.requestName = requestName;
		this.preferredName = preferredName;
		this.referenceName = referenceName;
	}

	private String requestName;

	private String preferredName;

	private String referenceName;
		
}




