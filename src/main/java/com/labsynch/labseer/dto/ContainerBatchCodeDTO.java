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
public class ContainerBatchCodeDTO {
	
	private String batchCode;
	
	private String containerCodeName;
	
	private String containerBarcode;
	
	private String wellCodeName;
	
	private String wellName;
		
	public ContainerBatchCodeDTO(){
	}
	
	public ContainerBatchCodeDTO(String batchCode, String containerCodeName, String containerBarcode, String wellCodeName, String wellName){
		this.batchCode = batchCode;
		this.containerCodeName = containerCodeName;
		this.containerBarcode = containerBarcode;
		this.wellCodeName = wellCodeName;
		this.wellName = wellName;
	}
	
	public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<ContainerBatchCodeDTO> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	

}


