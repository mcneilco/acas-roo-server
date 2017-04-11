package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class SubjectCodeDTO {
	
	private String subjectCode;
	private Collection<TreatmentGroupCodeDTO> treatmentGroupCodes;
	
	public SubjectCodeDTO(){
	}
	
	public String toJson() {
        return new JSONSerializer().include("treatmentGroupCodes.analysisGroupCodes.experimentCodes").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
	}

	public static String toJsonArray(Collection<SubjectCodeDTO> collection) {
		return new JSONSerializer().include("treatmentGroupCodes.analysisGroupCodes.experimentCodes").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
	}
	

}


