package com.labsynch.labseer.dto;

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
	

}


