package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ErrorMessage {

    @Size(max = 50)
    private String level;

    @Size(max = 255)
    private String message;

    public ErrorMessage(String level, String message) {
        this.level = level;
        this.message = message;
    }

    public ErrorMessage() {
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

    public static ErrorMessage fromJsonToErrorMessage(String json) {
        return new JSONDeserializer<ErrorMessage>()
                .use(null, ErrorMessage.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ErrorMessage> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ErrorMessage> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ErrorMessage> fromJsonArrayToErrorMessages(String json) {
        return new JSONDeserializer<List<ErrorMessage>>()
                .use("values", ErrorMessage.class).deserialize(json);
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
}
