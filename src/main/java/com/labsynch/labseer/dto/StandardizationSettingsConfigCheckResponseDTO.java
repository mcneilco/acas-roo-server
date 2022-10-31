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
    
    private List<String> invalidReasons = new ArrayList<String>();

    private Boolean needsRestandardization;

    private List<String> needsRestandardizationReasons = new ArrayList<String>();

    private List<String> suggestedConfigurationChanges = new ArrayList<String>();

    private String validatedSettings;

    public StandardizationSettingsConfigCheckResponseDTO() {

    }

    public StandardizationSettingsConfigCheckResponseDTO(Boolean valid, Boolean needsRestandardization, List<String> needsRestandardizationReasons,  List<String> invalidReasons,  List<String> suggestedConfigurationChanges, String validatedSettings) {
        this.valid = valid;
        this.invalidReasons = invalidReasons;
        this.needsRestandardization = needsRestandardization;
        this.needsRestandardizationReasons = needsRestandardizationReasons;
        this.suggestedConfigurationChanges = suggestedConfigurationChanges;
        this.validatedSettings = validatedSettings;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public List<String> getInvalidReasons() {
        return invalidReasons;
    }

    public void setInvalidReasons(List<String> invalidReasons) {
        this.invalidReasons = invalidReasons;
    }

    public void addInvalidReason(String invalidReason) {
        this.invalidReasons.add(invalidReason);
    }

    public Boolean getNeedsRestandardization() {
        return needsRestandardization;
    }

    public void setNeedsRestandardization(Boolean needsRestandardization) {
        this.needsRestandardization = needsRestandardization;
    }

    public List<String> getNeedsRestandardizationReasons() {
        return this.needsRestandardizationReasons;
    }

    public void setNeedsRestandardizationReasons(String reasons) {
        if(reasons != null && !reasons.isEmpty()) {
            this.needsRestandardizationReasons = new ArrayList<String>(Arrays.asList(reasons.split("\\s*,\\s*")));
        }
    }

    public void setNeedsRestandardizationReasons(List<String> reasons) {
        if(reasons != null) {
            this.needsRestandardizationReasons = reasons;
        }
    }

    public void addNeedsRestandardizationReasons(List<String> reasons) {
        if(reasons != null) {
            this.needsRestandardizationReasons.addAll(reasons);
        }
    }

    public void addNeedsRestandardizationReason(String reason) {
        if(reason != null && !reason.isEmpty()) {
            this.needsRestandardizationReasons.add(reason);
        }
    }

    public List<String> getSuggestedConfigurationChanges() {
        return this.suggestedConfigurationChanges;
    }

    public void setSuggestedConfigurationChanges(String suggestedConfigurationChanges) {
        if(suggestedConfigurationChanges != null && !suggestedConfigurationChanges.isEmpty()) {
            this.suggestedConfigurationChanges = new ArrayList<String>(Arrays.asList(suggestedConfigurationChanges.split("\\s*,\\s*")));
        }
    }

    public void setSuggestedConfigurationChanges(List<String> suggestedConfigurationChanges) {
        if(suggestedConfigurationChanges != null) {
            this.suggestedConfigurationChanges = suggestedConfigurationChanges;
        }
    }

    public void addSuggestedConfigurationChanges(List<String> suggestedConfigurationChanges) {
        if(suggestedConfigurationChanges != null) {
            this.suggestedConfigurationChanges.addAll(suggestedConfigurationChanges);
        }
    }

    public void addSuggestedConfigurationChange(String suggestedConfigurationChange) {
        if(suggestedConfigurationChange != null && !suggestedConfigurationChange.isEmpty()) {
            this.suggestedConfigurationChanges.add(suggestedConfigurationChange);
        }
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
                .exclude("*.class").include("invalidReasons", "needsRestandardizationReasons", "suggestedConfigurationChanges").serialize(this);
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
