package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ErrorMessageDTO {

    String level;

    String message;

    public ErrorMessageDTO() {

    }

    public ErrorMessageDTO(String level, String message) {
        this.level = level;
        this.message = message;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static ErrorMessageDTO fromJsonToErrorMessageDTO(String json) {
        return new JSONDeserializer<ErrorMessageDTO>()
                .use(null, ErrorMessageDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ErrorMessageDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ErrorMessageDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ErrorMessageDTO> fromJsonArrayToErroes(String json) {
        return new JSONDeserializer<List<ErrorMessageDTO>>()
                .use("values", ErrorMessageDTO.class).deserialize(json);
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
