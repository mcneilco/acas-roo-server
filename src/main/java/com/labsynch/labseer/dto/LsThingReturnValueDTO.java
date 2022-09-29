package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class LsThingReturnValueDTO {

    
    String stateType;
    String stateKind;
    String valueType;
    String valueKind;
    String labelType;
    String labelKind;
    String key;

    public LsThingReturnValueDTO() {

    }

    public String getStateType() {
        return this.stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    public String getStateKind() {
        return this.stateKind;
    }

    public void setStateKind(String stateKind) {
        this.stateKind = stateKind;
    }

    public String getValueType() {
        return this.valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getValueKind() {
        return this.valueKind;
    }

    public void setValueKind(String valueKind) {
        this.valueKind = valueKind;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabelType() {
        return this.labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

    public String getLabelKind() {
        return this.labelKind;
    }

    public void setLabelKind(String labelKind) {
        this.labelKind = labelKind;
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

    public static LsThingReturnValueDTO fromJsonToValueReturnDTO(String json) {
        return new JSONDeserializer<LsThingReturnValueDTO>()
                .use(null, LsThingReturnValueDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<LsThingReturnValueDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<LsThingReturnValueDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<LsThingReturnValueDTO> fromJsonArrayToValueReturnDTO(String json) {
        return new JSONDeserializer<List<LsThingReturnValueDTO>>()
                .use("values", LsThingReturnValueDTO.class).deserialize(json);
    }
}
