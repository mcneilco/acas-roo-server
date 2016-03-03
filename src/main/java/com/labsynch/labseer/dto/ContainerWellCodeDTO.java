package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ContainerWellCodeDTO {
	
	private String requestCodeName;
	private List<String> wellCodeNames;
	
	public ContainerWellCodeDTO(){
	}
	
	public ContainerWellCodeDTO(String requestCodeName, List<String> wellCodeNames){
		this.requestCodeName = requestCodeName;
		this.wellCodeNames = wellCodeNames;
	}
	
	public String toJson() {
        return new JSONSerializer().include("wellCodeNames").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<ContainerWellCodeDTO> collection) {
        return new JSONSerializer().include("wellCodeNames").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

}


