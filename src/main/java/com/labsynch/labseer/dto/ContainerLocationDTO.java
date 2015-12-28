package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ContainerLocationDTO {
	
	private String locationCodeName;
	
	private String containerCodeName;
	
	private String containerBarcode;
	
	public ContainerLocationDTO(String locationCodeName, String containerCodeName, String containerBarcode){
		this.locationCodeName = locationCodeName;
		this.containerCodeName = containerCodeName;
		this.containerBarcode = containerBarcode;
	}
	

}


