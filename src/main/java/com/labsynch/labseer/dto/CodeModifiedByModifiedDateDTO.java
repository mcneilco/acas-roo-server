package com.labsynch.labseer.dto;

import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class CodeModifiedByModifiedDateDTO {
	
	private String containerCodeName;
	private String modifiedBy;
	private Date modifiedDate;
	
	public CodeModifiedByModifiedDateDTO(){
	}
	
	public CodeModifiedByModifiedDateDTO(String containerCodeName, String modifiedBy, Date modifiedDate){
		this.containerCodeName = containerCodeName;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
	}
	

}


