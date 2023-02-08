package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import flexjson.JSONSerializer;
import flexjson.JSONDeserializer;


public class CmpdRegBatchCodeExperimentDTO {

    private String protocolCode;
    
    private String experimentCode;
    private String experimentName;
    
    private String description;
    private String comments;

    private Boolean ignored;

    public CmpdRegBatchCodeExperimentDTO() {
    }

    public CmpdRegBatchCodeExperimentDTO(String protocolCode, String experimentCode,
			String experimentName, String description, String comments, Boolean ignored) {
		this.protocolCode = protocolCode;
		this.experimentCode = experimentCode;
		this.experimentName = experimentName;
		this.description = description;
		this.comments = comments;
		this.ignored = ignored;
	}

    public String getProtocolCode() {
        return this.protocolCode;
    }

    public void setProtocolCode(String protocolCode) {
        this.protocolCode = protocolCode;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean isIgnored() {
        return this.ignored;
    }

    public void setIgnored(Boolean ignored) {
        this.ignored = ignored;
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public static CmpdRegBatchCodeExperimentDTO fromJson(String json) {
        return new JSONDeserializer<CmpdRegBatchCodeExperimentDTO>().use(null, CmpdRegBatchCodeExperimentDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<CmpdRegBatchCodeExperimentDTO> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static Collection<CmpdRegBatchCodeExperimentDTO> fromJsonArray(String json) {
        return new JSONDeserializer<List<CmpdRegBatchCodeExperimentDTO>>().use("values", CmpdRegBatchCodeExperimentDTO.class).deserialize(json);
    }
}