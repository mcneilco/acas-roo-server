package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ExperimentCodeDTO {
	
	private String experimentCode;
	private String protocolCode;
	
	public ExperimentCodeDTO(){
	}


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ExperimentCodeDTO fromJsonToExperimentCodeDTO(String json) {
        return new JSONDeserializer<ExperimentCodeDTO>()
        .use(null, ExperimentCodeDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ExperimentCodeDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ExperimentCodeDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ExperimentCodeDTO> fromJsonArrayToExperimentCoes(String json) {
        return new JSONDeserializer<List<ExperimentCodeDTO>>()
        .use("values", ExperimentCodeDTO.class).deserialize(json);
    }

	public String getExperimentCode() {
        return this.experimentCode;
    }

	public void setExperimentCode(String experimentCode) {
        this.experimentCode = experimentCode;
    }

	public String getProtocolCode() {
        return this.protocolCode;
    }

	public void setProtocolCode(String protocolCode) {
        this.protocolCode = protocolCode;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


