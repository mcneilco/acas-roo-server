package com.labsynch.labseer.dto;

import java.util.ArrayList;
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
public class ContainerSubjectsDTO {
	
	private String containerIdOrCodeName;
	private String interactionType; 
	private String interactionKind;
	private Collection<Subject> subjects = new ArrayList<Subject>();
	
	public ContainerSubjectsDTO(){
	}
	
	public String toJson() {
        return new JSONSerializer().include("subjects.lsStates.lsValues","subjects.lsLabels").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<ContainerSubjectsDTO> collection) {
        return new JSONSerializer().include("subjects.lsStates.lsValues","subjects.lsLabels").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	
	public String toJsonStub() {
        return new JSONSerializer().include("subjects").exclude("*.class","subjects.lsStates","subjects.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArrayStub(Collection<ContainerSubjectsDTO> collection) {
        return new JSONSerializer().include("subjects").exclude("*.class","subjects.lsStates","subjects.lsLabels").transform(new ExcludeNulls(), void.class).serialize(collection);
    }


	public String getContainerIdOrCodeName() {
        return this.containerIdOrCodeName;
    }

	public void setContainerIdOrCodeName(String containerIdOrCodeName) {
        this.containerIdOrCodeName = containerIdOrCodeName;
    }

	public String getInteractionType() {
        return this.interactionType;
    }

	public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

	public String getInteractionKind() {
        return this.interactionKind;
    }

	public void setInteractionKind(String interactionKind) {
        this.interactionKind = interactionKind;
    }

	public Collection<Subject> getSubjects() {
        return this.subjects;
    }

	public void setSubjects(Collection<Subject> subjects) {
        this.subjects = subjects;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static ContainerSubjectsDTO fromJsonToContainerSubjectsDTO(String json) {
        return new JSONDeserializer<ContainerSubjectsDTO>()
        .use(null, ContainerSubjectsDTO.class).deserialize(json);
    }

	public static Collection<ContainerSubjectsDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<ContainerSubjectsDTO>>()
        .use("values", ContainerSubjectsDTO.class).deserialize(json);
    }
}


