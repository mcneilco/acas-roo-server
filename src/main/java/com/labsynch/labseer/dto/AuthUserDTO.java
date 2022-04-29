package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class AuthUserDTO {
	
	private String name;


	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

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

	public static AuthUserDTO fromJsonToAuthUserDTO(String json) {
        return new JSONDeserializer<AuthUserDTO>()
        .use(null, AuthUserDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AuthUserDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<AuthUserDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<AuthUserDTO> fromJsonArrayToAuthUserDTO(String json) {
        return new JSONDeserializer<List<AuthUserDTO>>()
        .use("values", AuthUserDTO.class).deserialize(json);
    }
}


