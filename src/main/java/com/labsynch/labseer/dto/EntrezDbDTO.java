package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class EntrezDbDTO {

    private String taxonomyId;
    private String entrezGenesFile;
    private String geneHistoryFile;

    public String getTaxonomyId() {
        return this.taxonomyId;
    }

    public void setTaxonomyId(String taxonomyId) {
        this.taxonomyId = taxonomyId;
    }

    public String getEntrezGenesFile() {
        return this.entrezGenesFile;
    }

    public void setEntrezGenesFile(String entrezGenesFile) {
        this.entrezGenesFile = entrezGenesFile;
    }

    public String getGeneHistoryFile() {
        return this.geneHistoryFile;
    }

    public void setGeneHistoryFile(String geneHistoryFile) {
        this.geneHistoryFile = geneHistoryFile;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static EntrezDbDTO fromJsonToEntrezDbDTO(String json) {
        return new JSONDeserializer<EntrezDbDTO>()
                .use(null, EntrezDbDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<EntrezDbDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<EntrezDbDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<EntrezDbDTO> fromJsonArrayToEntrezDbDTO(String json) {
        return new JSONDeserializer<List<EntrezDbDTO>>()
                .use("values", EntrezDbDTO.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
