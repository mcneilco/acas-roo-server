package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ContainerLocationDTO {
	
	private String locationCodeName;
	
	private String containerCodeName;
	
	private String containerBarcode;
	
	private String level;
	
	private String message;
	
	private String modifiedBy;
	
	private Date modifiedDate;
	
	public ContainerLocationDTO(){
	}
	
	public ContainerLocationDTO(String locationCodeName, String containerCodeName, String containerBarcode){
		this.locationCodeName = locationCodeName;
		this.containerCodeName = containerCodeName;
		this.containerBarcode = containerBarcode;
	}
	
	public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<ContainerLocationDTO> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	

}


