package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class AuthorRoleDTO {
	
	private String roleType;
	
	private String roleKind;
	
	private String roleName;
	
	private String userName;
	
}


