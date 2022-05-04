package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class LsThingValuePathDTO {

    public LsThingValuePathDTO() {
    }

    private String idOrCodeName;

    private String stateType;

    private String stateKind;

    private String valueType;

    private String valueKind;

    private Collection<LsThingValue> values;

    public String toJson() {
        return new JSONSerializer().include("values").exclude("*.class").transform(new ExcludeNulls(), void.class)
                .serialize(this);
    }

    public static String toJsonArray(Collection<LsThingValuePathDTO> collection) {
        return new JSONSerializer().include("values").exclude("*.class").transform(new ExcludeNulls(), void.class)
                .serialize(collection);
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

    public String getValueType() {
        return this.valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getValueKind() {
        return this.valueKind;
    }

    public void setValueKind(String valueKind) {
        this.valueKind = valueKind;
    }

    public Collection<LsThingValue> getValues() {
        return this.values;
    }

    public void setValues(Collection<LsThingValue> values) {
        this.values = values;
    }

    public static LsThingValuePathDTO fromJsonToLsThingValuePathDTO(String json) {
        return new JSONDeserializer<LsThingValuePathDTO>()
                .use(null, LsThingValuePathDTO.class).deserialize(json);
    }

    public static Collection<LsThingValuePathDTO> fromJsonArrayToLsThingValuePathDTO(String json) {
        return new JSONDeserializer<List<LsThingValuePathDTO>>()
                .use("values", LsThingValuePathDTO.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
