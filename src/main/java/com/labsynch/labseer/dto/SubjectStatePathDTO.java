package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class SubjectStatePathDTO {

	public SubjectStatePathDTO() {
	}

	private String idOrCodeName;

	private String stateType;
	
	private String stateKind;
	
	private Collection<SubjectState> states;
	
	public String toJson() {
        return new JSONSerializer().include("states.lsValues").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<SubjectStatePathDTO> collection) {
        return new JSONSerializer().include("states.lsValues").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
}


