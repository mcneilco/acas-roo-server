package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ContainerQueryDTO {

	Date recordedDateGreaterThan;
	
	Date recordedDateLessThan;
	
	String recordedBy;
	
	Integer maxResults;
	
	String lsType;
	
	String lsKind;
	
	Collection<ItxQueryDTO> firstInteractions;
	
	Collection<ItxQueryDTO> secondInteractions;
	
	Collection<ItxQueryDTO> subjects;
	
	Collection<ValueQueryDTO> values;
	
	Collection<LabelQueryDTO> labels;
	
	public ContainerQueryDTO(){
		
	}
	
	public ContainerQueryDTO(ContainerQueryDTO queryDTO){
		this.recordedDateGreaterThan = queryDTO.getRecordedDateGreaterThan();
		this.recordedDateLessThan = queryDTO.getRecordedDateLessThan();
		this.lsType = queryDTO.getLsType();
		this.lsKind = queryDTO.getLsKind();
		this.recordedBy = queryDTO.getRecordedBy();
		this.maxResults = queryDTO.getMaxResults();
		this.firstInteractions = queryDTO.getFirstInteractions();
		this.secondInteractions = queryDTO.getSecondInteractions();
		this.values = queryDTO.getValues();
		this.labels = queryDTO.getLabels();
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

	public static ContainerQueryDTO fromJsonToContainerQueryDTO(String json) {
        return new JSONDeserializer<ContainerQueryDTO>()
        .use(null, ContainerQueryDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ContainerQueryDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ContainerQueryDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ContainerQueryDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<ContainerQueryDTO>>()
        .use("values", ContainerQueryDTO.class).deserialize(json);
    }

	public Date getRecordedDateGreaterThan() {
        return this.recordedDateGreaterThan;
    }

	public void setRecordedDateGreaterThan(Date recordedDateGreaterThan) {
        this.recordedDateGreaterThan = recordedDateGreaterThan;
    }

	public Date getRecordedDateLessThan() {
        return this.recordedDateLessThan;
    }

	public void setRecordedDateLessThan(Date recordedDateLessThan) {
        this.recordedDateLessThan = recordedDateLessThan;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public Integer getMaxResults() {
        return this.maxResults;
    }

	public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
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

	public Collection<ItxQueryDTO> getFirstInteractions() {
        return this.firstInteractions;
    }

	public void setFirstInteractions(Collection<ItxQueryDTO> firstInteractions) {
        this.firstInteractions = firstInteractions;
    }

	public Collection<ItxQueryDTO> getSecondInteractions() {
        return this.secondInteractions;
    }

	public void setSecondInteractions(Collection<ItxQueryDTO> secondInteractions) {
        this.secondInteractions = secondInteractions;
    }

	public Collection<ItxQueryDTO> getSubjects() {
        return this.subjects;
    }

	public void setSubjects(Collection<ItxQueryDTO> subjects) {
        this.subjects = subjects;
    }

	public Collection<ValueQueryDTO> getValues() {
        return this.values;
    }

	public void setValues(Collection<ValueQueryDTO> values) {
        this.values = values;
    }

	public Collection<LabelQueryDTO> getLabels() {
        return this.labels;
    }

	public void setLabels(Collection<LabelQueryDTO> labels) {
        this.labels = labels;
    }
}
