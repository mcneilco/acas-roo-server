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
public class AuthProjectDTO {
	
	private Collection<String> projects;
	
	private Collection<String> projectAdmins;


	public String toJson() {
        return new JSONSerializer()
        	.exclude("*.class")
        	.include("projects", "projectAdmins")
        	.serialize(this);
    }

	public static AuthProjectDTO fromJsonToAuthProjectDTO(String json) {
        return new JSONDeserializer<AuthProjectDTO>().use(null, AuthProjectDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AuthProjectDTO> collection) {
        return new JSONSerializer()
        	.exclude("*.class")
        	.include("projects", "projectAdmins")
        	.serialize(collection);
    }

	public static Collection<AuthProjectDTO> fromJsonArrayToAuthProes(String json) {
        return new JSONDeserializer<List<AuthProjectDTO>>().use(null, ArrayList.class).use("values", AuthProjectDTO.class).deserialize(json);
    }
}


