package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class SubjectStateValueDTO {
	long subjectId;
	String subjectValue;

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static SubjectStateValueDTO fromJsonToSubjectStateValueDTO(String json) {
        return new JSONDeserializer<SubjectStateValueDTO>()
        .use(null, SubjectStateValueDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SubjectStateValueDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SubjectStateValueDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SubjectStateValueDTO> fromJsonArrayToSubjectStateValueDTO(String json) {
        return new JSONDeserializer<List<SubjectStateValueDTO>>()
        .use("values", SubjectStateValueDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public long getSubjectId() {
        return this.subjectId;
    }

	public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

	public String getSubjectValue() {
        return this.subjectValue;
    }

	public void setSubjectValue(String subjectValue) {
        this.subjectValue = subjectValue;
    }
}
