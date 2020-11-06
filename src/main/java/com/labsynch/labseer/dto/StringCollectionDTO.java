package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class StringCollectionDTO {
	
	private String name;
	
	private String code;


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getCode() {
        return this.code;
    }

	public void setCode(String code) {
        this.code = code;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static StringCollectionDTO fromJsonToStringCollectionDTO(String json) {
        return new JSONDeserializer<StringCollectionDTO>()
        .use(null, StringCollectionDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<StringCollectionDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<StringCollectionDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<StringCollectionDTO> fromJsonArrayToStringCollectioes(String json) {
        return new JSONDeserializer<List<StringCollectionDTO>>()
        .use("values", StringCollectionDTO.class).deserialize(json);
    }
}


