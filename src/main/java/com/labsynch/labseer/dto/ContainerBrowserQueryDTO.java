package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ContainerBrowserQueryDTO {

    private String queryString;

    private ContainerQueryDTO queryDTO;

    public String getQueryString() {
        return this.queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public ContainerQueryDTO getQueryDTO() {
        return this.queryDTO;
    }

    public void setQueryDTO(ContainerQueryDTO queryDTO) {
        this.queryDTO = queryDTO;
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

    public static ContainerBrowserQueryDTO fromJsonToContainerBrowserQueryDTO(String json) {
        return new JSONDeserializer<ContainerBrowserQueryDTO>()
                .use(null, ContainerBrowserQueryDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ContainerBrowserQueryDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ContainerBrowserQueryDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ContainerBrowserQueryDTO> fromJsonArrayToContainerBroes(String json) {
        return new JSONDeserializer<List<ContainerBrowserQueryDTO>>()
                .use("values", ContainerBrowserQueryDTO.class).deserialize(json);
    }
}
