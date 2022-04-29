package com.labsynch.labseer.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.SubjectState;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

@RooJavaBean
@RooToString
@RooJson
public class SubjectCodeStateDTO {
	
	private String subjectCode;
	private SubjectState subjectState;
	
	public SubjectCodeStateDTO(){
	}
	
	public SubjectCodeStateDTO(String subjectCode, SubjectState subjectState){
		this.subjectCode = subjectCode;
		this.subjectState = subjectState;
	}


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getSubjectCode() {
        return this.subjectCode;
    }

	public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

	public SubjectState getSubjectState() {
        return this.subjectState;
    }

	public void setSubjectState(SubjectState subjectState) {
        this.subjectState = subjectState;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static SubjectCodeStateDTO fromJsonToSubjectCodeStateDTO(String json) {
        return new JSONDeserializer<SubjectCodeStateDTO>()
        .use(null, SubjectCodeStateDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SubjectCodeStateDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SubjectCodeStateDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SubjectCodeStateDTO> fromJsonArrayToSubjectCoes(String json) {
        return new JSONDeserializer<List<SubjectCodeStateDTO>>()
        .use("values", SubjectCodeStateDTO.class).deserialize(json);
    }
}


