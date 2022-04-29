package com.labsynch.labseer.exceptions;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class ErrorMessage {

    @Size(max = 50)
    private String errorLevel;
    
    @Size(max = 255)
    private String message;
    

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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getErrorLevel() {
        return this.errorLevel;
    }

	public void setErrorLevel(String errorLevel) {
        this.errorLevel = errorLevel;
    }

	public String getMessage() {
        return this.message;
    }

	public void setMessage(String message) {
        this.message = message;
    }
}