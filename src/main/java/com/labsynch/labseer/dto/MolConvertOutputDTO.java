package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class MolConvertOutputDTO {
	
	private String structure;
	
	private String format;
	
	private String contentUrl;


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static MolConvertOutputDTO fromJsonToMolConvertOutputDTO(String json) {
        return new JSONDeserializer<MolConvertOutputDTO>()
        .use(null, MolConvertOutputDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<MolConvertOutputDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<MolConvertOutputDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<MolConvertOutputDTO> fromJsonArrayToMolCoes(String json) {
        return new JSONDeserializer<List<MolConvertOutputDTO>>()
        .use("values", MolConvertOutputDTO.class).deserialize(json);
    }

	public String getStructure() {
        return this.structure;
    }

	public void setStructure(String structure) {
        this.structure = structure;
    }

	public String getFormat() {
        return this.format;
    }

	public void setFormat(String format) {
        this.format = format;
    }

	public String getContentUrl() {
        return this.contentUrl;
    }

	public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
