package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class AuthProjectGroupsDTO {
	

	private String name;
	
	private String alias;

	private Long id;
	
	private Collection<String> groups;
	



	public String toJson() {
        return new JSONSerializer()
        	.exclude("*.class")
        	.include("groups")
        	.serialize(this);
    }

	public static AuthProjectGroupsDTO fromJsonToAuthProjectDTO(String json) {
        return new JSONDeserializer<AuthProjectGroupsDTO>().use(null, AuthProjectGroupsDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AuthProjectGroupsDTO> collection) {
        return new JSONSerializer()
        	.exclude("*.class")
        	.include("groups")
        	.serialize(collection);
    }

	public static Collection<AuthProjectGroupsDTO> fromJsonArrayToAuthProes(String json) {
        return new JSONDeserializer<List<AuthProjectGroupsDTO>>().use(null, ArrayList.class).use("values", AuthProjectGroupsDTO.class).deserialize(json);
    }
}


