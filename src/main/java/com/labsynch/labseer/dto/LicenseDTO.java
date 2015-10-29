package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class LicenseDTO {

	public LicenseDTO() {
	}


	private String licensee;

	private String validDateString;
	
	private boolean valid;
	
	private String edition;

	private String features;
	
	private Integer numberOfUsers;
	
	private Integer numberOfDaysLeft;

	

}


