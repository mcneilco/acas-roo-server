package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class LsThingQueryDTO {

	Date recordedDateGreaterThan;
	
	Date recordedDateLessThan;
	
	String recordedBy;
	
	Integer maxResults;
	
	String lsType;
	
	String lsKind;
	
	Collection<ItxQueryDTO> firstInteractions;
	
	Collection<ItxQueryDTO> secondInteractions;
	
	Collection<ValueQueryDTO> values;
	
	Collection<LabelQueryDTO> labels;
	
	CodeNameQueryDTO codeName;
	
	public LsThingQueryDTO(){
		
	}
	
	public LsThingQueryDTO(LsThingQueryDTO queryDTO){
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
		this.codeName = queryDTO.getCodeName();
	}

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static LsThingQueryDTO fromJsonToLsThingQueryDTO(String json) {
        return new JSONDeserializer<LsThingQueryDTO>()
        .use(null, LsThingQueryDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<LsThingQueryDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<LsThingQueryDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<LsThingQueryDTO> fromJsonArrayToLsThingQueryDTO(String json) {
        return new JSONDeserializer<List<LsThingQueryDTO>>()
        .use("values", LsThingQueryDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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

	public CodeNameQueryDTO getCodeName() {
        return this.codeName;
    }

	public void setCodeName(CodeNameQueryDTO codeName) {
        this.codeName = codeName;
    }
}
