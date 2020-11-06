package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Collection<String> getProjects() {
        return this.projects;
    }

	public void setProjects(Collection<String> projects) {
        this.projects = projects;
    }

	public Collection<String> getProjectAdmins() {
        return this.projectAdmins;
    }

	public void setProjectAdmins(Collection<String> projectAdmins) {
        this.projectAdmins = projectAdmins;
    }
}


