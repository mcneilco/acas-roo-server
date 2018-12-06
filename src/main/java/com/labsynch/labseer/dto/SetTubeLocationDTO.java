package com.labsynch.labseer.dto;


import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class SetTubeLocationDTO {

	private String barcode;
	
	private String locationBreadCrumb;
		
	private String user;
		
	private Date date;
	
	private String rootLabel;
	
}
