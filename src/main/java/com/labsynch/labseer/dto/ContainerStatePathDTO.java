package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ContainerStatePathDTO {

    public ContainerStatePathDTO() {
    }

    private String idOrCodeName;

    private String stateType;

    private String stateKind;

    private Collection<ContainerState> states;

    public String toJson() {
        return new JSONSerializer().include("states.lsValues").exclude("*.class")
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public static String toJsonArray(Collection<ContainerStatePathDTO> collection) {
        return new JSONSerializer().include("states.lsValues").exclude("*.class")
                .transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static ContainerStatePathDTO fromJsonToContainerStatePathDTO(String json) {
        return new JSONDeserializer<ContainerStatePathDTO>()
                .use(null, ContainerStatePathDTO.class).deserialize(json);
    }

    public static Collection<ContainerStatePathDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<ContainerStatePathDTO>>()
                .use("values", ContainerStatePathDTO.class).deserialize(json);
    }

    public String getIdOrCodeName() {
        return this.idOrCodeName;
    }

    public void setIdOrCodeName(String idOrCodeName) {
        this.idOrCodeName = idOrCodeName;
    }

    public String getStateType() {
        return this.stateType;
    }

    public void setStateType(String stateType) {
        this.stateType = stateType;
    }

    public String getStateKind() {
        return this.stateKind;
    }

    public void setStateKind(String stateKind) {
        this.stateKind = stateKind;
    }

    public Collection<ContainerState> getStates() {
        return this.states;
    }

    public void setStates(Collection<ContainerState> states) {
        this.states = states;
    }
}
