package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.utils.ExcludeNulls;

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

}


