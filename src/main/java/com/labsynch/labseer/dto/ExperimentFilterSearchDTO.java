package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class ExperimentFilterSearchDTO {
	
	private String queryId;

	private String termName;

	private Long experimentId;

	private String codeName;
	
	private String experimentCode;

	private String lsType;

	private String lsKind;

	private String operator;

	private String filterValue;


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

	public static ExperimentFilterSearchDTO fromJsonToExperimentFilterSearchDTO(String json) {
        return new JSONDeserializer<ExperimentFilterSearchDTO>()
        .use(null, ExperimentFilterSearchDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ExperimentFilterSearchDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ExperimentFilterSearchDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ExperimentFilterSearchDTO> fromJsonArrayToExperimentFilterSearchDTO(String json) {
        return new JSONDeserializer<List<ExperimentFilterSearchDTO>>()
        .use("values", ExperimentFilterSearchDTO.class).deserialize(json);
    }

	public String getQueryId() {
        return this.queryId;
    }

	public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

	public String getTermName() {
        return this.termName;
    }

	public void setTermName(String termName) {
        this.termName = termName;
    }

	public Long getExperimentId() {
        return this.experimentId;
    }

	public void setExperimentId(Long experimentId) {
        this.experimentId = experimentId;
    }

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

	public String getExperimentCode() {
        return this.experimentCode;
    }

	public void setExperimentCode(String experimentCode) {
        this.experimentCode = experimentCode;
    }

	public String getLsType() {
        return this.lsType;
    }

	public void setLsType(String lsType) {
        this.lsType = lsType;
    }

	public String getLsKind() {
        return this.lsKind;
    }

	public void setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }

	public String getOperator() {
        return this.operator;
    }

	public void setOperator(String operator) {
        this.operator = operator;
    }

	public String getFilterValue() {
        return this.filterValue;
    }

	public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }
}


