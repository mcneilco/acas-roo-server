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
public class LabelSequenceDTO {
	
    private Long numberOfLabels;

    private String thingTypeAndKind;

    private String labelTypeAndKind;

    private String labelPrefix;

    private String labelSeparator;

    private boolean groupDigits;

    private Integer digits;

    private Long startingNumber;


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

	public static LabelSequenceDTO fromJsonToLabelSequenceDTO(String json) {
        return new JSONDeserializer<LabelSequenceDTO>()
        .use(null, LabelSequenceDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<LabelSequenceDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<LabelSequenceDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<LabelSequenceDTO> fromJsonArrayToLabelSequenceDTO(String json) {
        return new JSONDeserializer<List<LabelSequenceDTO>>()
        .use("values", LabelSequenceDTO.class).deserialize(json);
    }

	public Long getNumberOfLabels() {
        return this.numberOfLabels;
    }

	public void setNumberOfLabels(Long numberOfLabels) {
        this.numberOfLabels = numberOfLabels;
    }

	public String getThingTypeAndKind() {
        return this.thingTypeAndKind;
    }

	public void setThingTypeAndKind(String thingTypeAndKind) {
        this.thingTypeAndKind = thingTypeAndKind;
    }

	public String getLabelTypeAndKind() {
        return this.labelTypeAndKind;
    }

	public void setLabelTypeAndKind(String labelTypeAndKind) {
        this.labelTypeAndKind = labelTypeAndKind;
    }

	public String getLabelPrefix() {
        return this.labelPrefix;
    }

	public void setLabelPrefix(String labelPrefix) {
        this.labelPrefix = labelPrefix;
    }

	public String getLabelSeparator() {
        return this.labelSeparator;
    }

	public void setLabelSeparator(String labelSeparator) {
        this.labelSeparator = labelSeparator;
    }

	public boolean isGroupDigits() {
        return this.groupDigits;
    }

	public void setGroupDigits(boolean groupDigits) {
        this.groupDigits = groupDigits;
    }

	public Integer getDigits() {
        return this.digits;
    }

	public void setDigits(Integer digits) {
        this.digits = digits;
    }

	public Long getStartingNumber() {
        return this.startingNumber;
    }

	public void setStartingNumber(Long startingNumber) {
        this.startingNumber = startingNumber;
    }
}
