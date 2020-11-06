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
public class SubjectStateMiniDTO {
	
    public SubjectStateMiniDTO(SubjectState subjectState) {
    	this.setId(subjectState.getId());
    }

	private Long id;


	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

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

	public static SubjectStateMiniDTO fromJsonToSubjectStateMiniDTO(String json) {
        return new JSONDeserializer<SubjectStateMiniDTO>()
        .use(null, SubjectStateMiniDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SubjectStateMiniDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SubjectStateMiniDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SubjectStateMiniDTO> fromJsonArrayToSubjectStateMiniDTO(String json) {
        return new JSONDeserializer<List<SubjectStateMiniDTO>>()
        .use("values", SubjectStateMiniDTO.class).deserialize(json);
    }
}

