package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class CodeLabelDTO {
	
	private List<String> foundCodeNames;
	
	private String level;
	
	private String message;
	
	private String requestLabel;
	
	public CodeLabelDTO(){
	}
	
	public CodeLabelDTO(String requestLabel, List<String> foundCodeNames){
		this.requestLabel = requestLabel;
		this.foundCodeNames = foundCodeNames;
	}
	
	public CodeLabelDTO(String requestLabel, String level, String message){
		this.requestLabel = requestLabel;
		this.level = level;
		this.message = message;
	}
	
	public String toJson() {
        return new JSONSerializer().include("foundCodeNames").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<CodeLabelDTO> collection) {
        return new JSONSerializer().include("foundCodeNames").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	


	public List<String> getFoundCodeNames() {
        return this.foundCodeNames;
    }

	public void setFoundCodeNames(List<String> foundCodeNames) {
        this.foundCodeNames = foundCodeNames;
    }

	public String getLevel() {
        return this.level;
    }

	public void setLevel(String level) {
        this.level = level;
    }

	public String getMessage() {
        return this.message;
    }

	public void setMessage(String message) {
        this.message = message;
    }

	public String getRequestLabel() {
        return this.requestLabel;
    }

	public void setRequestLabel(String requestLabel) {
        this.requestLabel = requestLabel;
    }

	public static CodeLabelDTO fromJsonToCodeLabelDTO(String json) {
        return new JSONDeserializer<CodeLabelDTO>()
        .use(null, CodeLabelDTO.class).deserialize(json);
    }

	public static Collection<CodeLabelDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<CodeLabelDTO>>()
        .use("values", CodeLabelDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


