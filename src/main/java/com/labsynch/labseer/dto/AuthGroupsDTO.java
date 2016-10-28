package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class AuthGroupsDTO {
	
	private String name;
	
	private Collection<String> members;
	

	public String toJson() {
        return new JSONSerializer().exclude("*.class").include("members").serialize(this);
    }

	public static AuthGroupsDTO fromJsonToAuthGroupsDTO(String json) {
        return new JSONDeserializer<AuthGroupsDTO>().use(null, AuthGroupsDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AuthGroupsDTO> collection) {
        return new JSONSerializer().exclude("*.class").include("members").serialize(collection);
    }

	public static Collection<AuthGroupsDTO> fromJsonArrayToAuthGroes(String json) {
        return new JSONDeserializer<List<AuthGroupsDTO>>().use(null, ArrayList.class).use("values", AuthGroupsDTO.class).deserialize(json);
    }
}


