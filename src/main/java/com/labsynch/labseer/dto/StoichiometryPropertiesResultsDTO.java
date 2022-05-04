package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class StoichiometryPropertiesResultsDTO {

    boolean hasError;

    boolean hasWarning;

    Collection<ErrorMessageDTO> errorMessages = new ArrayList<ErrorMessageDTO>();

    Collection<StoichiometryPropertiesDTO> results = new ArrayList<StoichiometryPropertiesDTO>();

    public String toJson() {
        return new JSONSerializer().include("errorMessages", "results").exclude("*.class")
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public static String toJsonArray(Collection<StoichiometryPropertiesResultsDTO> collection) {
        return new JSONSerializer().include("errorMessages", "results").exclude("*.class")
                .transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    public StoichiometryPropertiesResultsDTO() {
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public boolean isHasError() {
        return this.hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public boolean isHasWarning() {
        return this.hasWarning;
    }

    public void setHasWarning(boolean hasWarning) {
        this.hasWarning = hasWarning;
    }

    public Collection<ErrorMessageDTO> getErrorMessages() {
        return this.errorMessages;
    }

    public void setErrorMessages(Collection<ErrorMessageDTO> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public Collection<StoichiometryPropertiesDTO> getResults() {
        return this.results;
    }

    public void setResults(Collection<StoichiometryPropertiesDTO> results) {
        this.results = results;
    }

    public static StoichiometryPropertiesResultsDTO fromJsonToStoichiometryPropertiesResultsDTO(String json) {
        return new JSONDeserializer<StoichiometryPropertiesResultsDTO>()
                .use(null, StoichiometryPropertiesResultsDTO.class).deserialize(json);
    }

    public static Collection<StoichiometryPropertiesResultsDTO> fromJsonArrayToStoichiometryProes(String json) {
        return new JSONDeserializer<List<StoichiometryPropertiesResultsDTO>>()
                .use("values", StoichiometryPropertiesResultsDTO.class).deserialize(json);
    }
}
