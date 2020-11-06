package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SubjectSearchRequest {
	
	Integer maxResults;

	String protocolLabelLike;
	
	String experimentLabelLike;
	
	String containerCode;
	
	String subjectType;
	
	String subjectKind;

	Collection<ValueQueryDTO> values;
	
	public SubjectSearchRequest(){
		
	}

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static SubjectSearchRequest fromJsonToSubjectSearchRequest(String json) {
        return new JSONDeserializer<SubjectSearchRequest>()
        .use(null, SubjectSearchRequest.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SubjectSearchRequest> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SubjectSearchRequest> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SubjectSearchRequest> fromJsonArrayToSubjectSearchRequests(String json) {
        return new JSONDeserializer<List<SubjectSearchRequest>>()
        .use("values", SubjectSearchRequest.class).deserialize(json);
    }

	public Integer getMaxResults() {
        return this.maxResults;
    }

	public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

	public String getProtocolLabelLike() {
        return this.protocolLabelLike;
    }

	public void setProtocolLabelLike(String protocolLabelLike) {
        this.protocolLabelLike = protocolLabelLike;
    }

	public String getExperimentLabelLike() {
        return this.experimentLabelLike;
    }

	public void setExperimentLabelLike(String experimentLabelLike) {
        this.experimentLabelLike = experimentLabelLike;
    }

	public String getContainerCode() {
        return this.containerCode;
    }

	public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

	public String getSubjectType() {
        return this.subjectType;
    }

	public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

	public String getSubjectKind() {
        return this.subjectKind;
    }

	public void setSubjectKind(String subjectKind) {
        this.subjectKind = subjectKind;
    }

	public Collection<ValueQueryDTO> getValues() {
        return this.values;
    }

	public void setValues(Collection<ValueQueryDTO> values) {
        this.values = values;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
