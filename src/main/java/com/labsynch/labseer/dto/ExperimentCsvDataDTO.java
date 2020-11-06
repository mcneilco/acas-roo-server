package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ExperimentCsvDataDTO {
	
	private String experimentCsvFilePath;
	
	private String analysisGroupCsvFilePath;

	private String treatmentGroupCsvFilePath;

	private String subjectCsvFilePath;
	

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getExperimentCsvFilePath() {
        return this.experimentCsvFilePath;
    }

	public void setExperimentCsvFilePath(String experimentCsvFilePath) {
        this.experimentCsvFilePath = experimentCsvFilePath;
    }

	public String getAnalysisGroupCsvFilePath() {
        return this.analysisGroupCsvFilePath;
    }

	public void setAnalysisGroupCsvFilePath(String analysisGroupCsvFilePath) {
        this.analysisGroupCsvFilePath = analysisGroupCsvFilePath;
    }

	public String getTreatmentGroupCsvFilePath() {
        return this.treatmentGroupCsvFilePath;
    }

	public void setTreatmentGroupCsvFilePath(String treatmentGroupCsvFilePath) {
        this.treatmentGroupCsvFilePath = treatmentGroupCsvFilePath;
    }

	public String getSubjectCsvFilePath() {
        return this.subjectCsvFilePath;
    }

	public void setSubjectCsvFilePath(String subjectCsvFilePath) {
        this.subjectCsvFilePath = subjectCsvFilePath;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ExperimentCsvDataDTO fromJsonToExperimentCsvDataDTO(String json) {
        return new JSONDeserializer<ExperimentCsvDataDTO>()
        .use(null, ExperimentCsvDataDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ExperimentCsvDataDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ExperimentCsvDataDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ExperimentCsvDataDTO> fromJsonArrayToExperimentCsvDataDTO(String json) {
        return new JSONDeserializer<List<ExperimentCsvDataDTO>>()
        .use("values", ExperimentCsvDataDTO.class).deserialize(json);
    }
}


