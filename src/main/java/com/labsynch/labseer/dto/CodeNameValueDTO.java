package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.domain.AbstractValue;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

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
	


	public static CodeNameValueDTO fromJsonToCodeNameValueDTO(String json) {
        return new JSONDeserializer<CodeNameValueDTO>()
        .use(null, CodeNameValueDTO.class).deserialize(json);
    }

	public static Collection<CodeNameValueDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<CodeNameValueDTO>>()
        .use("values", CodeNameValueDTO.class).deserialize(json);
    }

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

	public AbstractValue getValue() {
        return this.value;
    }

	public void setValue(AbstractValue value) {
        this.value = value;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


