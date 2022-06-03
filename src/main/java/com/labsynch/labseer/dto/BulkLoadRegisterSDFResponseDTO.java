package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class BulkLoadRegisterSDFResponseDTO {

    private String summary;

    private Collection<ValidationResponseDTO> results;

    private Collection<String> reportFiles;

    private Long id;

    public BulkLoadRegisterSDFResponseDTO() {

    }

    public BulkLoadRegisterSDFResponseDTO(String summary, Collection<ValidationResponseDTO> results,
            Collection<String> reportFiles, Long id) {
        this.summary = summary;
        this.results = results;
        this.reportFiles = reportFiles;
        this.id = id;
    }

    public String toJson() {
        return new JSONSerializer()
                .include("reportFiles", "results").exclude("*.class").transform(new ExcludeNulls(), void.class)
                .serialize(this);
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Collection<ValidationResponseDTO> getResults() {
        return this.results;
    }

    public void setResults(Collection<ValidationResponseDTO> results) {
        this.results = results;
    }

    public Collection<String> getReportFiles() {
        return this.reportFiles;
    }

    public void setReportFiles(Collection<String> reportFiles) {
        this.reportFiles = reportFiles;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static BulkLoadRegisterSDFResponseDTO fromJsonToBulkLoadRegisterSDFResponseDTO(String json) {
        return new JSONDeserializer<BulkLoadRegisterSDFResponseDTO>()
                .use(null, BulkLoadRegisterSDFResponseDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<BulkLoadRegisterSDFResponseDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<BulkLoadRegisterSDFResponseDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<BulkLoadRegisterSDFResponseDTO> fromJsonArrayToBulkLoadRegisterSDFRespoes(String json) {
        return new JSONDeserializer<List<BulkLoadRegisterSDFResponseDTO>>()
                .use("values", BulkLoadRegisterSDFResponseDTO.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
