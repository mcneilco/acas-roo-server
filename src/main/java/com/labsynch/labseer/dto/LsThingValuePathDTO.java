package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class LsThingValuePathDTO {

	public LsThingValuePathDTO() {
	}

	private String idOrCodeName;

	private String stateType;
	
	private String stateKind;
	
	private String valueType;
	
	private String valueKind;
	
	private Collection<LsThingValue> values;
	
	public String toJson() {
        return new JSONSerializer().include("values").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<LsThingValuePathDTO> collection) {
        return new JSONSerializer().include("values").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
}


