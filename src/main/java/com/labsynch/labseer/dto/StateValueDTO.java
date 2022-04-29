package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class StateValueDTO {

    Long id;

    String lsValue;

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

    public static StateValueDTO fromJsonToStateValueDTO(String json) {
        return new JSONDeserializer<StateValueDTO>()
                .use(null, StateValueDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<StateValueDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<StateValueDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<StateValueDTO> fromJsonArrayToStateValueDTO(String json) {
        return new JSONDeserializer<List<StateValueDTO>>()
                .use("values", StateValueDTO.class).deserialize(json);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLsValue() {
        return this.lsValue;
    }

    public void setLsValue(String lsValue) {
        this.lsValue = lsValue;
    }
}
