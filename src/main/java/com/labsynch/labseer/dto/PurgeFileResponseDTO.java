package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class PurgeFileResponseDTO {

    private String summary;

    private boolean success;

    private String fileName;

    private String originalFileName;

    public PurgeFileResponseDTO() {

    }

    public PurgeFileResponseDTO(String summary, boolean success, String fileName, String originalFileName) {
        this.summary = summary;
        this.success = success;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return this.originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public static PurgeFileResponseDTO fromJsonToPurgeFileResponseDTO(String json) {
        return new JSONDeserializer<PurgeFileResponseDTO>()
                .use(null, PurgeFileResponseDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<PurgeFileResponseDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<PurgeFileResponseDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<PurgeFileResponseDTO> fromJsonArrayToPurgeFileRespoes(String json) {
        return new JSONDeserializer<List<PurgeFileResponseDTO>>()
                .use("values", PurgeFileResponseDTO.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
