package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.AbstractValue;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class CodeNameValueDTO {
		
	private String codeName;
	
	private AbstractValue value;
		
	public CodeNameValueDTO(){
	}
	
	public CodeNameValueDTO(String codeName, AbstractValue value){
		this.codeName = codeName;
		this.value = value;
	}
	
	public String toJson() {
        return new JSONSerializer().include("value").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<CodeNameValueDTO> collection) {
        return new JSONSerializer().include("value").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	

}


