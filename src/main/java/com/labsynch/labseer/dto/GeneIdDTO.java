package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class GeneIdDTO {
	
	private String gid;


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static GeneIdDTO fromJsonToGeneIdDTO(String json) {
        return new JSONDeserializer<GeneIdDTO>()
        .use(null, GeneIdDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<GeneIdDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<GeneIdDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<GeneIdDTO> fromJsonArrayToGeneIdDTO(String json) {
        return new JSONDeserializer<List<GeneIdDTO>>()
        .use("values", GeneIdDTO.class).deserialize(json);
    }

	public String getGid() {
        return this.gid;
    }

	public void setGid(String gid) {
        this.gid = gid;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


