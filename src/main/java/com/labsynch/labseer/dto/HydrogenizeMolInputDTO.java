package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class HydrogenizeMolInputDTO {

    private String structure;

    private String inputFormat;

    private MethodOptionDTO parameters;

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static HydrogenizeMolInputDTO fromJsonToHydrogenizeMolInputDTO(String json) {
        return new JSONDeserializer<HydrogenizeMolInputDTO>()
                .use(null, HydrogenizeMolInputDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<HydrogenizeMolInputDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<HydrogenizeMolInputDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<HydrogenizeMolInputDTO> fromJsonArrayToHydrogenizeMoes(String json) {
        return new JSONDeserializer<List<HydrogenizeMolInputDTO>>()
                .use("values", HydrogenizeMolInputDTO.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getStructure() {
        return this.structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getInputFormat() {
        return this.inputFormat;
    }

    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

    public MethodOptionDTO getParameters() {
        return this.parameters;
    }

    public void setParameters(MethodOptionDTO parameters) {
        this.parameters = parameters;
    }
}
