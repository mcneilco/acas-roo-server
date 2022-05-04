package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class KeyValueDTO {

    private String key;

    private String value;

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static KeyValueDTO fromJsonToKeyValueDTO(String json) {
        return new JSONDeserializer<KeyValueDTO>()
                .use(null, KeyValueDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<KeyValueDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<KeyValueDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<KeyValueDTO> fromJsonArrayToKeyValueDTO(String json) {
        return new JSONDeserializer<List<KeyValueDTO>>()
                .use("values", KeyValueDTO.class).deserialize(json);
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
