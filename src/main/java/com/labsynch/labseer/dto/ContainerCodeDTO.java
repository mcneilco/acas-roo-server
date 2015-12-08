package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ContainerCodeDTO {
	
	private String codeName;
	private String barcode;
	
	public ContainerCodeDTO(){
	}
	
	public ContainerCodeDTO(String codeName, String barcode){
		this.codeName = codeName;
		this.barcode = barcode;
	}
	

}


