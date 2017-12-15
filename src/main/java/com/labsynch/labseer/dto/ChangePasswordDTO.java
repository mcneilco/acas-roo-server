package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ChangePasswordDTO {
	
	private String username;
	
	private String oldPassword;
	
	private String newPassword;
	
	private String newPasswordAgain;
	
	public ChangePasswordDTO(){
	}
}


