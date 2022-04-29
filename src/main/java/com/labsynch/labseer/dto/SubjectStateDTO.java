package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class SubjectStateDTO {

    public SubjectStateDTO(SubjectState subjectState) {
        this.setId(subjectState.getId());
        this.setRecordedBy(subjectState.getRecordedBy());
        this.setRecordedDate(subjectState.getRecordedDate());
        this.setLsTransaction_Id(subjectState.getLsTransaction());
        this.setModifiedBy(subjectState.getModifiedBy());
        this.setModifiedDate(subjectState.getModifiedDate());
        this.setIgnored(subjectState.isIgnored());
        this.setLsKind(subjectState.getLsKind());
        this.setLsType(subjectState.getLsType());
        this.setLsTypeAndKind(subjectState.getLsTypeAndKind());
        this.setComments(subjectState.getComments());
        this.setSubject(new SubjectMiniDTO(subjectState.getSubject()));
    }

    private Long id;

    private SubjectMiniDTO subject;

    private String recordedBy;

    private Date recordedDate;

    private String modifiedBy;

    private Date modifiedDate;

    private String lsType;

    private String lsKind;

    private String lsTypeAndKind;

    private String comments;

    private boolean ignored;

    private Long lsTransaction_Id;

    private Set<SubjectValue> subjectValues = new HashSet<SubjectValue>();

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

    public static SubjectStateDTO fromJsonToSubjectStateDTO(String json) {
        return new JSONDeserializer<SubjectStateDTO>()
                .use(null, SubjectStateDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<SubjectStateDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<SubjectStateDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<SubjectStateDTO> fromJsonArrayToSubjectStateDTO(String json) {
        return new JSONDeserializer<List<SubjectStateDTO>>()
                .use("values", SubjectStateDTO.class).deserialize(json);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubjectMiniDTO getSubject() {
        return this.subject;
    }

    public void setSubject(SubjectMiniDTO subject) {
        this.subject = subject;
    }

    public String getRecordedBy() {
        return this.recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public Date getRecordedDate() {
        return this.recordedDate;
    }

    public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

    public String getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
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

    public String getLsTypeAndKind() {
        return this.lsTypeAndKind;
    }

    public void setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isIgnored() {
        return this.ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public Long getLsTransaction_Id() {
        return this.lsTransaction_Id;
    }

    public void setLsTransaction_Id(Long lsTransaction_Id) {
        this.lsTransaction_Id = lsTransaction_Id;
    }

    public Set<SubjectValue> getSubjectValues() {
        return this.subjectValues;
    }

    public void setSubjectValues(Set<SubjectValue> subjectValues) {
        this.subjectValues = subjectValues;
    }
}
