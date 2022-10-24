package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class StandardizationSettingsConfigCheckResponseDTO {

    private Boolean valid;

    private Boolean needsRestandardization;

    private List<String> reasons;

    private String validatedSettings;

    public StandardizationSettingsConfigCheckResponseDTO() {

    }

    public StandardizationSettingsConfigCheckResponseDTO(Boolean valid, Boolean needsRestandardization, List<String> reasons, String validatedSettings) {
        this.valid = valid;
        this.needsRestandardization = needsRestandardization;
        this.reasons = reasons;
        this.validatedSettings = validatedSettings;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean getNeedsRestandardization() {
        return needsRestandardization;
    }

    public void setNeedsRestandardization(Boolean needsRestandardization) {
        this.needsRestandardization = needsRestandardization;
    }

    public List<String> getReasons() {
        return this.reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = new ArrayList<String>(Arrays.asList(reasons.split("\\s*,\\s*")));
    }

    public void setReasons(List<String> reasons) {
        if(reasons != null) {
            this.reasons = reasons;
        }
    }

    public void addReasons(List<String> reasons) {
        if(reasons != null) {
            this.reasons.addAll(reasons);
        }
    }

    public void addReason(String reason) {
        this.reasons.add(reason);
    }

    public String getValidatedSettings() {
        return this.validatedSettings;
    }

    public void setValidatedSettings(String validatedSettings) {
        this.validatedSettings = validatedSettings;
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

    public static StandardizationSettingsConfigCheckResponseDTO fromJsonToStandardizationSettingsConfigCheckResponseDTO(String json) {
        return new JSONDeserializer<StandardizationSettingsConfigCheckResponseDTO>()
                .use(null, StandardizationSettingsConfigCheckResponseDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<StandardizationSettingsConfigCheckResponseDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<StandardizationSettingsConfigCheckResponseDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<StandardizationSettingsConfigCheckResponseDTO> fromJsonArrayToStandardizationSettingsConfigCheckResponseDTO(String json) {
        return new JSONDeserializer<List<StandardizationSettingsConfigCheckResponseDTO>>()
                .use("values", StandardizationSettingsConfigCheckResponseDTO.class).deserialize(json);
    }

}
