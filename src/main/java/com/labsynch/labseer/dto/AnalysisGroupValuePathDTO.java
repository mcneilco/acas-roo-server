package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class AnalysisGroupValuePathDTO {

    public AnalysisGroupValuePathDTO() {
    }

    private String idOrCodeName;

    private String stateType;

    private String stateKind;

    private String valueType;

    private String valueKind;

    private Collection<AnalysisGroupValue> values;

    public String toJson() {
        return new JSONSerializer().include("values").exclude("*.class").transform(new ExcludeNulls(), void.class)
                .serialize(this);
    }

    public static String toJsonArray(Collection<AnalysisGroupValuePathDTO> collection) {
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

    public Collection<AnalysisGroupValue> getValues() {
        return this.values;
    }

    public void setValues(Collection<AnalysisGroupValue> values) {
        this.values = values;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static AnalysisGroupValuePathDTO fromJsonToAnalysisGroupValuePathDTO(String json) {
        return new JSONDeserializer<AnalysisGroupValuePathDTO>()
                .use(null, AnalysisGroupValuePathDTO.class).deserialize(json);
    }

    public static Collection<AnalysisGroupValuePathDTO> fromJsonArrayToAnalysisGroes(String json) {
        return new JSONDeserializer<List<AnalysisGroupValuePathDTO>>()
                .use("values", AnalysisGroupValuePathDTO.class).deserialize(json);
    }
}
