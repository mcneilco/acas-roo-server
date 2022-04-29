package com.labsynch.labseer.exceptions;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class LsThingValidationErrorMessage {

    @Size(max = 50)
    private String errorLevel;

    @Size(max = 255)
    private String message;

    @Size(max = 50)
    private String matchingThingCodeName;

    @Size(max = 50)
    private String matchingThingCorpName;

    public LsThingValidationErrorMessage(UniqueInteractionsException e) {
        this.setErrorLevel("error");
        this.setMessage(e.getMessage());
        this.setMatchingThingCodeName(e.getMatchingThingCodeName());
        this.setMatchingThingCorpName(e.getMatchingThingCorpName());
    }

    public LsThingValidationErrorMessage(UniqueNameException e) {
        this.setErrorLevel("error");
        this.setMessage(e.getMessage());
        this.setMatchingThingCodeName(e.getMatchingThingCodeName());
        this.setMatchingThingCorpName(e.getMatchingThingCorpName());
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

    public String getMatchingThingCodeName() {
        return this.matchingThingCodeName;
    }

    public void setMatchingThingCodeName(String matchingThingCodeName) {
        this.matchingThingCodeName = matchingThingCodeName;
    }

    public String getMatchingThingCorpName() {
        return this.matchingThingCorpName;
    }

    public void setMatchingThingCorpName(String matchingThingCorpName) {
        this.matchingThingCorpName = matchingThingCorpName;
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

    public static LsThingValidationErrorMessage fromJsonToLsThingValidationErrorMessage(String json) {
        return new JSONDeserializer<LsThingValidationErrorMessage>()
                .use(null, LsThingValidationErrorMessage.class).deserialize(json);
    }

    public static String toJsonArray(Collection<LsThingValidationErrorMessage> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<LsThingValidationErrorMessage> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<LsThingValidationErrorMessage> fromJsonArrayToLsThingValidationErrorMessages(String json) {
        return new JSONDeserializer<List<LsThingValidationErrorMessage>>()
                .use("values", LsThingValidationErrorMessage.class).deserialize(json);
    }
}