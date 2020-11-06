package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.ThingPage;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class SubjectDTO {
	
    public SubjectDTO(Subject subject) {
    	this.setId(subject.getId());
    	this.setVersion(subject.getVersion());
    	this.setRecordedBy(subject.getRecordedBy());
		this.setRecordedDate(subject.getRecordedDate());
		this.setLsTransaction(subject.getLsTransaction());
		this.setModifiedBy(subject.getModifiedBy());
		this.setModifiedDate(subject.getModifiedDate());
		this.setIgnored(subject.isIgnored());
		this.setCodeName(subject.getCodeName());
		this.setLsKind(subject.getLsKind());
		Set<TreatmentGroupMiniDTO> treatmentGroups = new HashSet<TreatmentGroupMiniDTO>();
		for (TreatmentGroup treatmentGroup : subject.getTreatmentGroups()){
			treatmentGroups.add(new TreatmentGroupMiniDTO(treatmentGroup));
		}
		this.setTreatmentGroups(treatmentGroups);
    }

    
	private Long id;
	
	private Integer version;
    
	private String lsType;

	private String lsKind;
	
	private String codeName;

	private String recordedBy;

	private Date recordedDate;

	private String modifiedBy;

	private Date modifiedDate;

	private boolean ignored;

	private Long lsTransaction;

	
    private Set<TreatmentGroupMiniDTO> treatmentGroups = new HashSet<TreatmentGroupMiniDTO>();

    private Set<SubjectLabelDTO> lsLabels = new HashSet<SubjectLabelDTO>();

    private Set<SubjectStateDTO> lsStates = new HashSet<SubjectStateDTO>();
    
	private Set<ThingPage> thingPage = new HashSet<ThingPage>();




	public String toJson() {
        return new JSONSerializer()
        .include("lsLabels", "lsStates.lsValues")
        .exclude("*.class", "lsStates.subject", "lsStates.lsValues.lsState", "treatmentGroup.analysisGroup")
        .serialize(this);
    }

	public static SubjectDTO fromJsonToSubjectDTO(String json) {
        return new JSONDeserializer<SubjectDTO>().use(null, SubjectDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SubjectDTO> collection) {
        return new JSONSerializer()
        .include("lsLabels", "lsStates.lsValues")
        .exclude("*.class", "lsStates.subject", "lsStates.lsValues.lsState", "treatmentGroup.analysisGroup")
        .serialize(collection);
    }

	public static Collection<SubjectDTO> fromJsonArrayToSubjectDTO(String json) {
        return new JSONDeserializer<List<SubjectDTO>>().use(null, ArrayList.class).use("values", SubjectDTO.class).deserialize(json);
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
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

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
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

	public boolean isIgnored() {
        return this.ignored;
    }

	public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

	public Long getLsTransaction() {
        return this.lsTransaction;
    }

	public void setLsTransaction(Long lsTransaction) {
        this.lsTransaction = lsTransaction;
    }

	public Set<TreatmentGroupMiniDTO> getTreatmentGroups() {
        return this.treatmentGroups;
    }

	public void setTreatmentGroups(Set<TreatmentGroupMiniDTO> treatmentGroups) {
        this.treatmentGroups = treatmentGroups;
    }

	public Set<SubjectLabelDTO> getLsLabels() {
        return this.lsLabels;
    }

	public void setLsLabels(Set<SubjectLabelDTO> lsLabels) {
        this.lsLabels = lsLabels;
    }

	public Set<SubjectStateDTO> getLsStates() {
        return this.lsStates;
    }

	public void setLsStates(Set<SubjectStateDTO> lsStates) {
        this.lsStates = lsStates;
    }

	public Set<ThingPage> getThingPage() {
        return this.thingPage;
    }

	public void setThingPage(Set<ThingPage> thingPage) {
        this.thingPage = thingPage;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


