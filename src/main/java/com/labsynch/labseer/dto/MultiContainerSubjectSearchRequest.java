package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class MultiContainerSubjectSearchRequest {
	
	Integer maxResults;

	String protocolLabelLike;
	
	String experimentLabelLike;
	
	Collection<String> containerCodes;
	
	String subjectType;
	
	String subjectKind;

	Collection<ValueQueryDTO> values;
	
	public MultiContainerSubjectSearchRequest(){
		
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

	public Collection<String> getContainerCodes() {
        return this.containerCodes;
    }

	public void setContainerCodes(Collection<String> containerCodes) {
        this.containerCodes = containerCodes;
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

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static MultiContainerSubjectSearchRequest fromJsonToMultiContainerSubjectSearchRequest(String json) {
        return new JSONDeserializer<MultiContainerSubjectSearchRequest>()
        .use(null, MultiContainerSubjectSearchRequest.class).deserialize(json);
    }

	public static String toJsonArray(Collection<MultiContainerSubjectSearchRequest> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<MultiContainerSubjectSearchRequest> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<MultiContainerSubjectSearchRequest> fromJsonArrayToMultiContainerSubjectSearchRequests(String json) {
        return new JSONDeserializer<List<MultiContainerSubjectSearchRequest>>()
        .use("values", MultiContainerSubjectSearchRequest.class).deserialize(json);
    }
}
