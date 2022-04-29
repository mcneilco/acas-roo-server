package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class AuthProjectGroupsDTO {
	

	private String name;
	
	private String code;
	
	private String alias;
	
	private boolean active;
	
	private boolean isRestricted;

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

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getCode() {
        return this.code;
    }

	public void setCode(String code) {
        this.code = code;
    }

	public String getAlias() {
        return this.alias;
    }

	public void setAlias(String alias) {
        this.alias = alias;
    }

	public boolean isActive() {
        return this.active;
    }

	public void setActive(boolean active) {
        this.active = active;
    }

	public boolean isIsRestricted() {
        return this.isRestricted;
    }

	public void setIsRestricted(boolean isRestricted) {
        this.isRestricted = isRestricted;
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Collection<String> getGroups() {
        return this.groups;
    }

	public void setGroups(Collection<String> groups) {
        this.groups = groups;
    }

	public static AuthProjectGroupsDTO fromJsonToAuthProjectGroupsDTO(String json) {
        return new JSONDeserializer<AuthProjectGroupsDTO>()
        .use(null, AuthProjectGroupsDTO.class).deserialize(json);
    }

	public static Collection<AuthProjectGroupsDTO> fromJsonArrayToAuthProjectGroes(String json) {
        return new JSONDeserializer<List<AuthProjectGroupsDTO>>()
        .use("values", AuthProjectGroupsDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


