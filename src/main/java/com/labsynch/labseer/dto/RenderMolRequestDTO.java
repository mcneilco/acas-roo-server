package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class RenderMolRequestDTO {
	
	private String codeName;
	
	private String molStructure;
	
	private Integer height;
	
	private Integer width;
	
	private String format;


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

	public static RenderMolRequestDTO fromJsonToRenderMolRequestDTO(String json) {
        return new JSONDeserializer<RenderMolRequestDTO>()
        .use(null, RenderMolRequestDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<RenderMolRequestDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<RenderMolRequestDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<RenderMolRequestDTO> fromJsonArrayToRenderMoes(String json) {
        return new JSONDeserializer<List<RenderMolRequestDTO>>()
        .use("values", RenderMolRequestDTO.class).deserialize(json);
    }

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

	public String getMolStructure() {
        return this.molStructure;
    }

	public void setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }

	public Integer getHeight() {
        return this.height;
    }

	public void setHeight(Integer height) {
        this.height = height;
    }

	public Integer getWidth() {
        return this.width;
    }

	public void setWidth(Integer width) {
        this.width = width;
    }

	public String getFormat() {
        return this.format;
    }

	public void setFormat(String format) {
        this.format = format;
    }
}


