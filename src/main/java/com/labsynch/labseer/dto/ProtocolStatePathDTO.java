package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ProtocolStatePathDTO {

	public ProtocolStatePathDTO() {
	}

	private String idOrCodeName;

	private String stateType;
	
	private String stateKind;
	
	private Collection<ProtocolState> states;
	
	public String toJson() {
        return new JSONSerializer().include("states.lsValues").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<ProtocolStatePathDTO> collection) {
        return new JSONSerializer().include("states.lsValues").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
}


