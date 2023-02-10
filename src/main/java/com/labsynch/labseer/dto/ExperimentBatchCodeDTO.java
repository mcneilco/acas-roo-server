package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import flexjson.JSONSerializer;
import flexjson.JSONDeserializer;

public class ExperimentBatchCodeDTO {

    private String protocolCode;
    private String protocolName;

    private String experimentCode;
    private String experimentName;

    private String comments;
    private String description;

    private Boolean ignored;

    // Getter and setter methods.

    public String getProtocolCode() {
        return this.protocolCode;
    }

    public void setProtocolCode(String protocolCode) {
        this.protocolCode = protocolCode;
    }

    public String getProtocolName() {
        return this.protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getExperimentCode() {
        return this.experimentCode;
    }

    public void setExperimentCode(String experimentCode) {
        this.experimentCode = experimentCode;
    }

    public String getExperimentName() {
        return this.experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIgnored() {
        return this.ignored;
    }

    public void setIgnored(Boolean ignored) {
        this.ignored = ignored;
    }

    // Json serialization and deserialization methods.

    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public static ExperimentBatchCodeDTO fromJson(String json) {
        return new JSONDeserializer<ExperimentBatchCodeDTO>()
                .use(null, ExperimentBatchCodeDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ExperimentBatchCodeDTO> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static Collection<ExperimentBatchCodeDTO> fromJsonArray(String json) {
        return new JSONDeserializer<List<ExperimentBatchCodeDTO>>()
                .use("values", ExperimentBatchCodeDTO.class).deserialize(json);
    }
}
