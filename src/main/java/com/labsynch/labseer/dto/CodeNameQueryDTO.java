package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class CodeNameQueryDTO {

	String codeName;
	String operator;
	
	public CodeNameQueryDTO(){
		
	}

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

	public String getOperator() {
        return this.operator;
    }

	public void setOperator(String operator) {
        this.operator = operator;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static CodeNameQueryDTO fromJsonToCodeNameQueryDTO(String json) {
        return new JSONDeserializer<CodeNameQueryDTO>()
        .use(null, CodeNameQueryDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<CodeNameQueryDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<CodeNameQueryDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<CodeNameQueryDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<CodeNameQueryDTO>>()
        .use("values", CodeNameQueryDTO.class).deserialize(json);
    }
}
