package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class MethodOptionDTO {
		
	private String method;
	

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static MethodOptionDTO fromJsonToMethodOptionDTO(String json) {
        return new JSONDeserializer<MethodOptionDTO>()
        .use(null, MethodOptionDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<MethodOptionDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<MethodOptionDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<MethodOptionDTO> fromJsonArrayToMethodOptioes(String json) {
        return new JSONDeserializer<List<MethodOptionDTO>>()
        .use("values", MethodOptionDTO.class).deserialize(json);
    }

	public String getMethod() {
        return this.method;
    }

	public void setMethod(String method) {
        this.method = method;
    }
}
