package com.labsynch.labseer.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class TempThingDTO {

	private static final Logger logger = LoggerFactory.getLogger(TempThingDTO.class);

	public TempThingDTO(){
		//empty constructor
	}

	private Long parentId;
	private String parentCodeName;

	private Long id;	
	private String codeName;
	private String tempId;

}


