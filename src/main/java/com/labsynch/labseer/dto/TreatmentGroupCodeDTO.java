package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class TreatmentGroupCodeDTO {

    private String treatmentGroupCode;
    private Collection<AnalysisGroupCodeDTO> analysisGroupCodes;

    public TreatmentGroupCodeDTO() {
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

    public static TreatmentGroupCodeDTO fromJsonToTreatmentGroupCodeDTO(String json) {
        return new JSONDeserializer<TreatmentGroupCodeDTO>()
                .use(null, TreatmentGroupCodeDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<TreatmentGroupCodeDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<TreatmentGroupCodeDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<TreatmentGroupCodeDTO> fromJsonArrayToTreatmentGroupCoes(String json) {
        return new JSONDeserializer<List<TreatmentGroupCodeDTO>>()
                .use("values", TreatmentGroupCodeDTO.class).deserialize(json);
    }

    public String getTreatmentGroupCode() {
        return this.treatmentGroupCode;
    }

    public void setTreatmentGroupCode(String treatmentGroupCode) {
        this.treatmentGroupCode = treatmentGroupCode;
    }

    public Collection<AnalysisGroupCodeDTO> getAnalysisGroupCodes() {
        return this.analysisGroupCodes;
    }

    public void setAnalysisGroupCodes(Collection<AnalysisGroupCodeDTO> analysisGroupCodes) {
        this.analysisGroupCodes = analysisGroupCodes;
    }
}
