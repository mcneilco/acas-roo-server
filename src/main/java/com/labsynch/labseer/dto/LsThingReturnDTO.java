package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class LsThingReturnDTO {

    
    private Collection<String> thingAttributes;
    private Collection<LsThingReturnValueDTO> thingValues;

    public LsThingReturnDTO() {

    }

    public Collection<String> getThingAttributes() {
        return this.thingAttributes;
    }

    public void setThingAttributes(Collection<String> thingAttributes) {
        this.thingAttributes = thingAttributes;
    }

    public Collection<LsThingReturnValueDTO> getThingValues() {
        return this.thingValues;
    }

    public void setThingValues(Collection<LsThingReturnValueDTO> thingValues) {
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

    public static LsThingReturnDTO fromJsonToValueReturnDTO(String json) {
        return new JSONDeserializer<LsThingReturnDTO>()
                .use(null, LsThingReturnDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<LsThingReturnDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<LsThingReturnDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<LsThingReturnDTO> fromJsonArrayToValueReturnDTO(String json) {
        return new JSONDeserializer<List<LsThingReturnDTO>>()
                .use("values", LsThingReturnDTO.class).deserialize(json);
    }
}
