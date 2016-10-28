package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class IdSetDTO {
	
	private Set<Long> idSet = new HashSet<Long>();

	

	public String toJson() {
        return new JSONSerializer().include("idSet").exclude("*.class").serialize(this);
    }

	public static IdSetDTO fromJsonToIdSetDTO(String json) {
        return new JSONDeserializer<IdSetDTO>().use(null, IdSetDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<IdSetDTO> collection) {
        return new JSONSerializer().include("idSet").exclude("*.class").serialize(collection);
    }

	public static Collection<IdSetDTO> fromJsonArrayToIdSetDTO(String json) {
        return new JSONDeserializer<List<IdSetDTO>>().use(null, ArrayList.class).use("values", IdSetDTO.class).deserialize(json);
    }
}


