package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AuthorNameDTO {
	
	private String name;


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static AuthorNameDTO fromJsonToAuthorNameDTO(String json) {
        return new JSONDeserializer<AuthorNameDTO>()
        .use(null, AuthorNameDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AuthorNameDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<AuthorNameDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<AuthorNameDTO> fromJsonArrayToAuthoes(String json) {
        return new JSONDeserializer<List<AuthorNameDTO>>()
        .use("values", AuthorNameDTO.class).deserialize(json);
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


