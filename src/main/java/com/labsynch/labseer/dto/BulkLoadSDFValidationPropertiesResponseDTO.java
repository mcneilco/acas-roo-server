package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class BulkLoadSDFValidationPropertiesResponseDTO {

    private Collection<String> chemists;

    private Collection<String> projects;

    public BulkLoadSDFValidationPropertiesResponseDTO(Collection<String> chemists, Collection<String> projects) {
        this.chemists = chemists;
        this.projects = projects;
    }

    public String toJson() {
        return new JSONSerializer()
                .include("chemists", "projects").exclude("*.class").transform(new ExcludeNulls(), void.class)
                .serialize(this);
    }

    public Collection<String> getChemists() {
        return this.chemists;
    }

    public void setChemists(Collection<String> chemists) {
        this.chemists = chemists;
    }

    public Collection<String> getProjects() {
        return this.projects;
    }

    public void setProjects(Collection<String> projects) {
        this.projects = projects;
    }

    public static BulkLoadSDFValidationPropertiesResponseDTO fromJsonToBulkLoadSDFValidationPropertiesResponseDTO(
            String json) {
        return new JSONDeserializer<BulkLoadSDFValidationPropertiesResponseDTO>()
                .use(null, BulkLoadSDFValidationPropertiesResponseDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<BulkLoadSDFValidationPropertiesResponseDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<BulkLoadSDFValidationPropertiesResponseDTO> collection,
            String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<BulkLoadSDFValidationPropertiesResponseDTO> fromJsonArrayToBulkLoadSDFValidationPropertiesRespoes(
            String json) {
        return new JSONDeserializer<List<BulkLoadSDFValidationPropertiesResponseDTO>>()
                .use("values", BulkLoadSDFValidationPropertiesResponseDTO.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
