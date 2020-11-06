package com.labsynch.labseer.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

public class LabelPrefixDTO {

	private Long id;
	
	private String code;
	
	private String name;
	
	private Boolean ignore;
	
	private String labelTypeAndKind;
	
	private String thingTypeAndKind;
	
	private String labelPrefix;
	
	private Long numberOfLabels;
	
	
	public String toSafeJson() {
		return new JSONSerializer().exclude("*.class", "id", "code", "name", "ignore").serialize(this);
	}

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public String getCode() {
        return this.code;
    }

	public void setCode(String code) {
        this.code = code;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public Boolean getIgnore() {
        return this.ignore;
    }

	public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

	public String getLabelTypeAndKind() {
        return this.labelTypeAndKind;
    }

	public void setLabelTypeAndKind(String labelTypeAndKind) {
        this.labelTypeAndKind = labelTypeAndKind;
    }

	public String getThingTypeAndKind() {
        return this.thingTypeAndKind;
    }

	public void setThingTypeAndKind(String thingTypeAndKind) {
        this.thingTypeAndKind = thingTypeAndKind;
    }

	public String getLabelPrefix() {
        return this.labelPrefix;
    }

	public void setLabelPrefix(String labelPrefix) {
        this.labelPrefix = labelPrefix;
    }

	public Long getNumberOfLabels() {
        return this.numberOfLabels;
    }

	public void setNumberOfLabels(Long numberOfLabels) {
        this.numberOfLabels = numberOfLabels;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static LabelPrefixDTO fromJsonToLabelPrefixDTO(String json) {
        return new JSONDeserializer<LabelPrefixDTO>()
        .use(null, LabelPrefixDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<LabelPrefixDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<LabelPrefixDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<LabelPrefixDTO> fromJsonArrayToLabelPrefixDTO(String json) {
        return new JSONDeserializer<List<LabelPrefixDTO>>()
        .use("values", LabelPrefixDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
