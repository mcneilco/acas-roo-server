package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class AuthGroupsAndProjectsDTO {
	

	private Collection<AuthGroupsDTO> groups;
	
	private Collection<AuthProjectGroupsDTO> projects;
	

	public String toJson() {
        return new JSONSerializer()
        	.exclude("*.class")
        	.include("groups.members", "projects.groups")
        	.transform(new ExcludeNulls(), void.class)
        	.serialize(this);
    }

	public static AuthGroupsAndProjectsDTO fromJsonToAuthProjectDTO(String json) {
        return new JSONDeserializer<AuthGroupsAndProjectsDTO>().use(null, AuthGroupsAndProjectsDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AuthGroupsAndProjectsDTO> collection) {
        return new JSONSerializer()
        	.exclude("*.class")
        	.include("groups", "projects.groups")
          	.transform(new ExcludeNulls(), void.class)
        	.serialize(collection);
    }

	public static Collection<AuthGroupsAndProjectsDTO> fromJsonArrayToAuthProes(String json) {
        return new JSONDeserializer<List<AuthGroupsAndProjectsDTO>>().use(null, ArrayList.class).use("values", AuthGroupsAndProjectsDTO.class).deserialize(json);
    }
}


