package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class CodeTypeKindDTO {
		
	private String codeName;
	
	private String lsType;
	
	private String lsKind;
	
	public CodeTypeKindDTO(){
	}
	
	public CodeTypeKindDTO(String codeName, String lsType, String lsKind){
		this.codeName = codeName;
		this.lsType = lsType;
		this.lsKind = lsKind;
	}
	
	public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<CodeTypeKindDTO> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	

}


