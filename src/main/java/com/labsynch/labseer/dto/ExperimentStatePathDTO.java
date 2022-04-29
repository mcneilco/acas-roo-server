package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ExperimentStatePathDTO {

    public ExperimentStatePathDTO() {
    }

    private String idOrCodeName;

    private String stateType;

    private String stateKind;

    private Collection<ExperimentState> states;

    public String toJson() {
        return new JSONSerializer().include("states.lsValues").exclude("*.class")
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public static String toJsonArray(Collection<ExperimentStatePathDTO> collection) {
        return new JSONSerializer().include("states.lsValues").exclude("*.class")
                .transform(new ExcludeNulls(), void.class).serialize(collection);
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

    public Collection<ExperimentState> getStates() {
        return this.states;
    }

    public void setStates(Collection<ExperimentState> states) {
        this.states = states;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static ExperimentStatePathDTO fromJsonToExperimentStatePathDTO(String json) {
        return new JSONDeserializer<ExperimentStatePathDTO>()
                .use(null, ExperimentStatePathDTO.class).deserialize(json);
    }

    public static Collection<ExperimentStatePathDTO> fromJsonArrayToExperimentStatePathDTO(String json) {
        return new JSONDeserializer<List<ExperimentStatePathDTO>>()
                .use("values", ExperimentStatePathDTO.class).deserialize(json);
    }
}
