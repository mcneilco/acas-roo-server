package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public Collection<String> getMembers() {
        return this.members;
    }

	public void setMembers(Collection<String> members) {
        this.members = members;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


