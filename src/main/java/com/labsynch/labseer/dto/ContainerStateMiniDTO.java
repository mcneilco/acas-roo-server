package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.ContainerState;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ContainerStateMiniDTO {

    public ContainerStateMiniDTO(ContainerState state) {
        this.setId(state.getId());
    }

    private Long id;

    private Integer version;

    private ContainerMiniDTO container;

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static ContainerStateMiniDTO fromJsonToContainerStateMiniDTO(String json) {
        return new JSONDeserializer<ContainerStateMiniDTO>()
                .use(null, ContainerStateMiniDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ContainerStateMiniDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ContainerStateMiniDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ContainerStateMiniDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<ContainerStateMiniDTO>>()
                .use("values", ContainerStateMiniDTO.class).deserialize(json);
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

    public ContainerMiniDTO getContainer() {
        return this.container;
    }

    public void setContainer(ContainerMiniDTO container) {
        this.container = container;
    }
}
