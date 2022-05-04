package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ExportResultDTO {

    private String summary;

    private String level;

    private String message;

    private String reportFilePath;

    public ExportResultDTO() {

    }

    public ExportResultDTO(String summary, String reportFilePath) {
        this.summary = summary;
        this.reportFilePath = reportFilePath;
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public static ExportResultDTO fromJsonToExportResultDTO(String json) {
        return new JSONDeserializer<ExportResultDTO>()
                .use(null, ExportResultDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ExportResultDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ExportResultDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ExportResultDTO> fromJsonArrayToExpoes(String json) {
        return new JSONDeserializer<List<ExportResultDTO>>()
                .use("values", ExportResultDTO.class).deserialize(json);
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public String getReportFilePath() {
        return this.reportFilePath;
    }

    public void setReportFilePath(String reportFilePath) {
        this.reportFilePath = reportFilePath;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
