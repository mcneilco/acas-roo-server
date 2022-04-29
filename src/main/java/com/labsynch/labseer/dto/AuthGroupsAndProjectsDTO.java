package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

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
        return new JSONDeserializer<AuthGroupsAndProjectsDTO>().use(null, AuthGroupsAndProjectsDTO.class)
                .deserialize(json);
    }

    public static String toJsonArray(Collection<AuthGroupsAndProjectsDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class")
                .include("groups", "projects.groups")
                .transform(new ExcludeNulls(), void.class)
                .serialize(collection);
    }

    public static Collection<AuthGroupsAndProjectsDTO> fromJsonArrayToAuthProes(String json) {
        return new JSONDeserializer<List<AuthGroupsAndProjectsDTO>>().use(null, ArrayList.class)
                .use("values", AuthGroupsAndProjectsDTO.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static AuthGroupsAndProjectsDTO fromJsonToAuthGroupsAndProjectsDTO(String json) {
        return new JSONDeserializer<AuthGroupsAndProjectsDTO>()
                .use(null, AuthGroupsAndProjectsDTO.class).deserialize(json);
    }

    public static Collection<AuthGroupsAndProjectsDTO> fromJsonArrayToAuthGroupsAndProes(String json) {
        return new JSONDeserializer<List<AuthGroupsAndProjectsDTO>>()
                .use("values", AuthGroupsAndProjectsDTO.class).deserialize(json);
    }

    public Collection<AuthGroupsDTO> getGroups() {
        return this.groups;
    }

    public void setGroups(Collection<AuthGroupsDTO> groups) {
        this.groups = groups;
    }

    public Collection<AuthProjectGroupsDTO> getProjects() {
        return this.projects;
    }

    public void setProjects(Collection<AuthProjectGroupsDTO> projects) {
        this.projects = projects;
    }
}
