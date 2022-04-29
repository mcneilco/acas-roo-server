package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class ItxQueryDTO {

	
	String interactionType;
	String interactionKind;
	String thingType;
	String thingKind;
	String thingLabelText;
	String thingLabelType;
	String thingLabelKind;
	String thingCodeName;
	String operator;
	
	Collection<ValueQueryDTO> thingValues;
		
	public ItxQueryDTO(){
		
	}

	public String getInteractionType() {
        return this.interactionType;
    }

	public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

	public String getInteractionKind() {
        return this.interactionKind;
    }

	public void setInteractionKind(String interactionKind) {
        this.interactionKind = interactionKind;
    }

	public String getThingType() {
        return this.thingType;
    }

	public void setThingType(String thingType) {
        this.thingType = thingType;
    }

	public String getThingKind() {
        return this.thingKind;
    }

	public void setThingKind(String thingKind) {
        this.thingKind = thingKind;
    }

	public String getThingLabelText() {
        return this.thingLabelText;
    }

	public void setThingLabelText(String thingLabelText) {
        this.thingLabelText = thingLabelText;
    }

	public String getThingLabelType() {
        return this.thingLabelType;
    }

	public void setThingLabelType(String thingLabelType) {
        this.thingLabelType = thingLabelType;
    }

	public String getThingLabelKind() {
        return this.thingLabelKind;
    }

	public void setThingLabelKind(String thingLabelKind) {
        this.thingLabelKind = thingLabelKind;
    }

	public String getThingCodeName() {
        return this.thingCodeName;
    }

	public void setThingCodeName(String thingCodeName) {
        this.thingCodeName = thingCodeName;
    }

	public String getOperator() {
        return this.operator;
    }

	public void setOperator(String operator) {
        this.operator = operator;
    }

	public Collection<ValueQueryDTO> getThingValues() {
        return this.thingValues;
    }

	public void setThingValues(Collection<ValueQueryDTO> thingValues) {
        this.thingValues = thingValues;
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

	public static ItxQueryDTO fromJsonToItxQueryDTO(String json) {
        return new JSONDeserializer<ItxQueryDTO>()
        .use(null, ItxQueryDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ItxQueryDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ItxQueryDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ItxQueryDTO> fromJsonArrayToItxQueryDTO(String json) {
        return new JSONDeserializer<List<ItxQueryDTO>>()
        .use("values", ItxQueryDTO.class).deserialize(json);
    }
}
