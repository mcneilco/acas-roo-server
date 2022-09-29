package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class LsThingBrowserQueryDTO {

    private String queryString;

    private LsThingQueryDTO queryDTO;

    private LsThingReturnDTO returnDTO;

    public String getQueryString() {
        return this.queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public LsThingQueryDTO getQueryDTO() {
        return this.queryDTO;
    }

    public void setQueryDTO(LsThingQueryDTO queryDTO) {
        this.queryDTO = queryDTO;
    }

    public LsThingReturnDTO getReturnDTO() {
        return this.returnDTO;
    }

    public void setReturnDTO(LsThingReturnDTO returnDTO) {
        this.returnDTO = returnDTO;
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

    public static LsThingBrowserQueryDTO fromJsonToLsThingBrowserQueryDTO(String json) {
        return new JSONDeserializer<LsThingBrowserQueryDTO>()
                .use(null, LsThingBrowserQueryDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<LsThingBrowserQueryDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<LsThingBrowserQueryDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<LsThingBrowserQueryDTO> fromJsonArrayToLsThingBroes(String json) {
        return new JSONDeserializer<List<LsThingBrowserQueryDTO>>()
                .use("values", LsThingBrowserQueryDTO.class).deserialize(json);
    }
}
