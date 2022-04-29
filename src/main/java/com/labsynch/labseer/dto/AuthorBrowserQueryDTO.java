package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class AuthorBrowserQueryDTO {
	
	private String queryString;
	
	private AuthorQueryDTO queryDTO;


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static AuthorBrowserQueryDTO fromJsonToAuthorBrowserQueryDTO(String json) {
        return new JSONDeserializer<AuthorBrowserQueryDTO>()
        .use(null, AuthorBrowserQueryDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AuthorBrowserQueryDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<AuthorBrowserQueryDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<AuthorBrowserQueryDTO> fromJsonArrayToAuthorBroes(String json) {
        return new JSONDeserializer<List<AuthorBrowserQueryDTO>>()
        .use("values", AuthorBrowserQueryDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getQueryString() {
        return this.queryString;
    }

	public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

	public AuthorQueryDTO getQueryDTO() {
        return this.queryDTO;
    }

	public void setQueryDTO(AuthorQueryDTO queryDTO) {
        this.queryDTO = queryDTO;
    }
}


