package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class PlateWellDTO {
	
	private String plateBarcode;
	private String plateCodeName;
	
	private String wellCodeName;
	
	private String wellLabel;
	
	public PlateWellDTO(){
	}
	
	public PlateWellDTO(String plateBarcode, String plateCodeName, String wellCodeName, String wellLabel){
		this.plateBarcode = plateBarcode;
		this.plateCodeName = plateCodeName;
		this.wellCodeName = wellCodeName;
		this.wellLabel = wellLabel;
	}
	

}


