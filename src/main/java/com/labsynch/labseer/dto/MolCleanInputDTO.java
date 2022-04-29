package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MolCleanInputDTO {

    private String structure;

    private CleanMolOptionsDTO parameters;

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

    public static MolCleanInputDTO fromJsonToMolCleanInputDTO(String json) {
        return new JSONDeserializer<MolCleanInputDTO>()
                .use(null, MolCleanInputDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<MolCleanInputDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<MolCleanInputDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<MolCleanInputDTO> fromJsonArrayToMoes(String json) {
        return new JSONDeserializer<List<MolCleanInputDTO>>()
                .use("values", MolCleanInputDTO.class).deserialize(json);
    }

    public String getStructure() {
        return this.structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public CleanMolOptionsDTO getParameters() {
        return this.parameters;
    }

    public void setParameters(CleanMolOptionsDTO parameters) {
        this.parameters = parameters;
    }
}
