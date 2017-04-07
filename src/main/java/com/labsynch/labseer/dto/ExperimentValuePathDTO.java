package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ExperimentValuePathDTO {

	public ExperimentValuePathDTO() {
	}

	private String idOrCodeName;

	private String stateType;
	
	private String stateKind;
	
	private String valueType;
	
	private String valueKind;
	
	private Collection<ExperimentValue> values;
	
	public String toJson() {
        return new JSONSerializer().include("values").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<ExperimentValuePathDTO> collection) {
        return new JSONSerializer().include("values").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
}


