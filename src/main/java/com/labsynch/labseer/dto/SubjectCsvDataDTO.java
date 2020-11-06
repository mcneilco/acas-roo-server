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
public class SubjectCsvDataDTO {
	
	private List<Long> treatmentGroupIds;

	private String subjectCsvFilePath;
	

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static SubjectCsvDataDTO fromJsonToSubjectCsvDataDTO(String json) {
        return new JSONDeserializer<SubjectCsvDataDTO>()
        .use(null, SubjectCsvDataDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SubjectCsvDataDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SubjectCsvDataDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SubjectCsvDataDTO> fromJsonArrayToSubjectCsvDataDTO(String json) {
        return new JSONDeserializer<List<SubjectCsvDataDTO>>()
        .use("values", SubjectCsvDataDTO.class).deserialize(json);
    }

	public List<Long> getTreatmentGroupIds() {
        return this.treatmentGroupIds;
    }

	public void setTreatmentGroupIds(List<Long> treatmentGroupIds) {
        this.treatmentGroupIds = treatmentGroupIds;
    }

	public String getSubjectCsvFilePath() {
        return this.subjectCsvFilePath;
    }

	public void setSubjectCsvFilePath(String subjectCsvFilePath) {
        this.subjectCsvFilePath = subjectCsvFilePath;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


