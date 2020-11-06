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
public class MetalotReturnDTO{

	private long id;
	
	private String corpName;
	
	private long buid;


	public long getId() {
        return this.id;
    }

	public void setId(long id) {
        this.id = id;
    }

	public String getCorpName() {
        return this.corpName;
    }

	public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

	public long getBuid() {
        return this.buid;
    }

	public void setBuid(long buid) {
        this.buid = buid;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static MetalotReturnDTO fromJsonToMetalotReturnDTO(String json) {
        return new JSONDeserializer<MetalotReturnDTO>()
        .use(null, MetalotReturnDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<MetalotReturnDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<MetalotReturnDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<MetalotReturnDTO> fromJsonArrayToMetaloes(String json) {
        return new JSONDeserializer<List<MetalotReturnDTO>>()
        .use("values", MetalotReturnDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
