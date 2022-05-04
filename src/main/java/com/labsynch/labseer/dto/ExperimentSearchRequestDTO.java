package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 *
 * @model ExperimentSearchRequestDTO
 *
 */

public class ExperimentSearchRequestDTO {

    /**
     * @property advancedFilter:string "example (Q1 and Q2) or (Q3 and Q4)"
     */
    private String advancedFilter;

    private String advancedFilterSQL;

    private String booleanFilter;

    private Set<ExperimentFilterSearchDTO> searchFilters = new HashSet<ExperimentFilterSearchDTO>();

    private Set<String> batchCodeList = new HashSet<String>();

    private Set<String> experimentCodeList = new HashSet<String>();

    public String toJson() {
        return new JSONSerializer().include("searchFilters", "batchCodeList", "experimentCodeList").exclude("*.class")
                .serialize(this);
    }

    public static ExperimentSearchRequestDTO fromJsonToExperimentSearchRequestDTO(String json) {
        return new JSONDeserializer<ExperimentSearchRequestDTO>().use(null, ExperimentSearchRequestDTO.class)
                .deserialize(json);
    }

    public static String toJsonArray(Collection<ExperimentSearchRequestDTO> collection) {
        return new JSONSerializer().include("searchFilters", "batchCodeList", "experimentCodeList").exclude("*.class")
                .serialize(collection);
    }

    public static Collection<ExperimentSearchRequestDTO> fromJsonArrayToExperimentSearchRequestDTO(String json) {
        return new JSONDeserializer<List<ExperimentSearchRequestDTO>>().use(null, ArrayList.class)
                .use("values", ExperimentSearchRequestDTO.class).deserialize(json);
    }

    public String getAdvancedFilter() {
        return this.advancedFilter;
    }

    public void setAdvancedFilter(String advancedFilter) {
        this.advancedFilter = advancedFilter;
    }

    public String getAdvancedFilterSQL() {
        return this.advancedFilterSQL;
    }

    public void setAdvancedFilterSQL(String advancedFilterSQL) {
        this.advancedFilterSQL = advancedFilterSQL;
    }

    public String getBooleanFilter() {
        return this.booleanFilter;
    }

    public void setBooleanFilter(String booleanFilter) {
        this.booleanFilter = booleanFilter;
    }

    public Set<ExperimentFilterSearchDTO> getSearchFilters() {
        return this.searchFilters;
    }

    public void setSearchFilters(Set<ExperimentFilterSearchDTO> searchFilters) {
        this.searchFilters = searchFilters;
    }

    public Set<String> getBatchCodeList() {
        return this.batchCodeList;
    }

    public void setBatchCodeList(Set<String> batchCodeList) {
        this.batchCodeList = batchCodeList;
    }

    public Set<String> getExperimentCodeList() {
        return this.experimentCodeList;
    }

    public void setExperimentCodeList(Set<String> experimentCodeList) {
        this.experimentCodeList = experimentCodeList;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
