package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class RequestNameDTO {

    private String requestName;

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getRequestName() {
        return this.requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static RequestNameDTO fromJsonToRequestNameDTO(String json) {
        return new JSONDeserializer<RequestNameDTO>()
                .use(null, RequestNameDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<RequestNameDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<RequestNameDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<RequestNameDTO> fromJsonArrayToRequestNameDTO(String json) {
        return new JSONDeserializer<List<RequestNameDTO>>()
                .use("values", RequestNameDTO.class).deserialize(json);
    }
}
