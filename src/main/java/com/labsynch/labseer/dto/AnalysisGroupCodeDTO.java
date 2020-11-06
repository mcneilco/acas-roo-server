package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AnalysisGroupCodeDTO {
	
	private String analysisGroupCode;
	private Collection<ExperimentCodeDTO> experimentCodes;
	
	public AnalysisGroupCodeDTO(){
	}
	

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getAnalysisGroupCode() {
        return this.analysisGroupCode;
    }

	public void setAnalysisGroupCode(String analysisGroupCode) {
        this.analysisGroupCode = analysisGroupCode;
    }

	public Collection<ExperimentCodeDTO> getExperimentCodes() {
        return this.experimentCodes;
    }

	public void setExperimentCodes(Collection<ExperimentCodeDTO> experimentCodes) {
        this.experimentCodes = experimentCodes;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static AnalysisGroupCodeDTO fromJsonToAnalysisGroupCodeDTO(String json) {
        return new JSONDeserializer<AnalysisGroupCodeDTO>()
        .use(null, AnalysisGroupCodeDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AnalysisGroupCodeDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<AnalysisGroupCodeDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<AnalysisGroupCodeDTO> fromJsonArrayToAnalysisGroupCoes(String json) {
        return new JSONDeserializer<List<AnalysisGroupCodeDTO>>()
        .use("values", AnalysisGroupCodeDTO.class).deserialize(json);
    }
}


