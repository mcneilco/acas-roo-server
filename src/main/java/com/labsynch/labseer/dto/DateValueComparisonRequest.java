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
public class DateValueComparisonRequest {
	
	private String lsType;
	
	private String lsKind;
	
	private String stateType;
	
	private String stateKind;
	
	private String valueKind;
	
	private Integer secondsDelta;
	
	private Boolean newerThanModified; 
	

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

	public String getStateType() {
        return this.stateType;
    }

	public void setStateType(String stateType) {
        this.stateType = stateType;
    }

	public String getStateKind() {
        return this.stateKind;
    }

	public void setStateKind(String stateKind) {
        this.stateKind = stateKind;
    }

	public String getValueKind() {
        return this.valueKind;
    }

	public void setValueKind(String valueKind) {
        this.valueKind = valueKind;
    }

	public Integer getSecondsDelta() {
        return this.secondsDelta;
    }

	public void setSecondsDelta(Integer secondsDelta) {
        this.secondsDelta = secondsDelta;
    }

	public Boolean getNewerThanModified() {
        return this.newerThanModified;
    }

	public void setNewerThanModified(Boolean newerThanModified) {
        this.newerThanModified = newerThanModified;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static DateValueComparisonRequest fromJsonToDateValueComparisonRequest(String json) {
        return new JSONDeserializer<DateValueComparisonRequest>()
        .use(null, DateValueComparisonRequest.class).deserialize(json);
    }

	public static String toJsonArray(Collection<DateValueComparisonRequest> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<DateValueComparisonRequest> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<DateValueComparisonRequest> fromJsonArrayToDateValueComparisonRequests(String json) {
        return new JSONDeserializer<List<DateValueComparisonRequest>>()
        .use("values", DateValueComparisonRequest.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


