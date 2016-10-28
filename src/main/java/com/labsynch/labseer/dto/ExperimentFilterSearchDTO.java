package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ExperimentFilterSearchDTO {
	
	private String queryId;

	private String termName;

	private Long experimentId;

	private String codeName;
	
	private String experimentCode;

	private String lsType;

	private String lsKind;

	private String operator;

	private String filterValue;

}


