package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class AuthorQueryDTO {

    Date recordedDateGreaterThan;

    Date recordedDateLessThan;

    String recordedBy;

    Integer maxResults;

    String lsType;

    String lsKind;

    String firstName;

    String lastName;

    String userName;

    Collection<ValueQueryDTO> values;

    Collection<LabelQueryDTO> labels;

    public AuthorQueryDTO() {

    }

    public AuthorQueryDTO(AuthorQueryDTO queryDTO) {
        this.recordedDateGreaterThan = queryDTO.getRecordedDateGreaterThan();
        this.recordedDateLessThan = queryDTO.getRecordedDateLessThan();
        this.lsType = queryDTO.getLsType();
        this.lsKind = queryDTO.getLsKind();
        this.firstName = queryDTO.getFirstName();
        this.lastName = queryDTO.getLastName();
        this.userName = queryDTO.getUserName();
        this.recordedBy = queryDTO.getRecordedBy();
        this.maxResults = queryDTO.getMaxResults();
        this.values = queryDTO.getValues();
        this.labels = queryDTO.getLabels();
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static AuthorQueryDTO fromJsonToAuthorQueryDTO(String json) {
        return new JSONDeserializer<AuthorQueryDTO>()
                .use(null, AuthorQueryDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<AuthorQueryDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<AuthorQueryDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<AuthorQueryDTO> fromJsonArrayToAuthoes(String json) {
        return new JSONDeserializer<List<AuthorQueryDTO>>()
                .use("values", AuthorQueryDTO.class).deserialize(json);
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

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
