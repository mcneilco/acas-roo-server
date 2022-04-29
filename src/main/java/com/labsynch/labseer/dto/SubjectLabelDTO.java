package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.domain.SubjectLabel;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class SubjectLabelDTO {
	
    public SubjectLabelDTO(SubjectLabel subjectLabel) {
      this.setId(subjectLabel.getId());
      this.setSubject(new SubjectMiniDTO(subjectLabel.getSubject()));
      this.setLsType(subjectLabel.getLsType());
      this.setLsKind(subjectLabel.getLsKind());
      this.setLsTypeAndKind(subjectLabel.getLsTypeAndKind());
      this.setLabelText(subjectLabel.getLabelText());
      this.setPreferred(subjectLabel.isPreferred());
      this.setLsTransaction_Id(subjectLabel.getLsTransaction());
      this.setRecordedBy(subjectLabel.getRecordedBy());
      this.setRecordedDate(subjectLabel.getRecordedDate());
      this.setPhysicallyLabled(subjectLabel.isPhysicallyLabled());
    }

	private Long id;
    
	private SubjectMiniDTO subject;

	private String labelText;

	private String recordedBy;

	private Date recordedDate;

	private Date modifiedDate;

	private boolean physicallyLabled;

	private String imageFile;

	private String lsType;

	private String lsKind;

	private String lsTypeAndKind;

	private boolean preferred;

	private boolean ignored;

	private Long lsTransaction_Id;




	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static SubjectLabelDTO fromJsonToSubjectLabelDTO(String json) {
        return new JSONDeserializer<SubjectLabelDTO>()
        .use(null, SubjectLabelDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SubjectLabelDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SubjectLabelDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SubjectLabelDTO> fromJsonArrayToSubjectLabelDTO(String json) {
        return new JSONDeserializer<List<SubjectLabelDTO>>()
        .use("values", SubjectLabelDTO.class).deserialize(json);
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

	public String getLabelText() {
        return this.labelText;
    }

	public void setLabelText(String labelText) {
        this.labelText = labelText;
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

	public Date getModifiedDate() {
        return this.modifiedDate;
    }

	public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

	public boolean isPhysicallyLabled() {
        return this.physicallyLabled;
    }

	public void setPhysicallyLabled(boolean physicallyLabled) {
        this.physicallyLabled = physicallyLabled;
    }

	public String getImageFile() {
        return this.imageFile;
    }

	public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
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

	public boolean isPreferred() {
        return this.preferred;
    }

	public void setPreferred(boolean preferred) {
        this.preferred = preferred;
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


//public SubjectLabel(com.labsynch.labseer.domain.SubjectLabel subjectLabel) {
//    super.setLabelType(subjectLabel.getLabelType());
//    super.setLabelKind(subjectLabel.getLabelKind());
//    super.setLabelTypeAndKind(subjectLabel.getLabelType() + "_" + subjectLabel.getLabelKind());
//    super.setLabelText(subjectLabel.getLabelText());
//    super.setPreferred(subjectLabel.isPreferred());
//    super.setLsTransaction(subjectLabel.getLsTransaction());
//    super.setRecordedBy(subjectLabel.getRecordedBy());
//    super.setRecordedDate(subjectLabel.getRecordedDate());
//    super.setPhysicallyLabled(subjectLabel.isPhysicallyLabled());
//}

