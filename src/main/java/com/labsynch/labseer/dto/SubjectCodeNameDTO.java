package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class SubjectCodeNameDTO {
	
	private String requestCodeName;
	private Subject subject;
	
	public SubjectCodeNameDTO(){
	}
	
	public SubjectCodeNameDTO(String requestCodeName, Subject subject){
		this.requestCodeName = requestCodeName;
		this.subject = subject;
	}
	
	public String toJson() {
	        return new JSONSerializer().include("subject.lsStates.lsValues","subject.lsLabels").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
	}
	
	public static String toJsonArray(Collection<SubjectCodeNameDTO> collection) {
        return new JSONSerializer().include("subject.lsStates.lsValues","subject.lsLabels").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getRequestCodeName() {
        return this.requestCodeName;
    }

	public void setRequestCodeName(String requestCodeName) {
        this.requestCodeName = requestCodeName;
    }

	public Subject getSubject() {
        return this.subject;
    }

	public void setSubject(Subject subject) {
        this.subject = subject;
    }

	public static SubjectCodeNameDTO fromJsonToSubjectCodeNameDTO(String json) {
        return new JSONDeserializer<SubjectCodeNameDTO>()
        .use(null, SubjectCodeNameDTO.class).deserialize(json);
    }

	public static Collection<SubjectCodeNameDTO> fromJsonArrayToSubjectCoes(String json) {
        return new JSONDeserializer<List<SubjectCodeNameDTO>>()
        .use("values", SubjectCodeNameDTO.class).deserialize(json);
    }
}


