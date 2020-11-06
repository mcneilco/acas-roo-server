package com.labsynch.labseer.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.domain.Subject;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

public class SubjectMiniDTO {
	
    public SubjectMiniDTO(Subject subject) {
    	this.setId(subject.getId());
    	this.setVersion(subject.getVersion());
    	subject.getCodeName();
    }

	private Long id;
    
	private Integer version;
	
	private String codeName;




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

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static SubjectMiniDTO fromJsonToSubjectMiniDTO(String json) {
        return new JSONDeserializer<SubjectMiniDTO>()
        .use(null, SubjectMiniDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SubjectMiniDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SubjectMiniDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SubjectMiniDTO> fromJsonArrayToSubjectMiniDTO(String json) {
        return new JSONDeserializer<List<SubjectMiniDTO>>()
        .use("values", SubjectMiniDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


