package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.Container;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class ContainerMiniDTO {
	
    public ContainerMiniDTO(Container container) {
    	this.setId(container.getId());
    	this.setVersion(container.getVersion());
    }

	private Long id;
    
	private Integer version;
	


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ContainerMiniDTO fromJsonToContainerMiniDTO(String json) {
        return new JSONDeserializer<ContainerMiniDTO>()
        .use(null, ContainerMiniDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ContainerMiniDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ContainerMiniDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ContainerMiniDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<ContainerMiniDTO>>()
        .use("values", ContainerMiniDTO.class).deserialize(json);
    }
}


