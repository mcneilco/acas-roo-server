package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.TreatmentGroup;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class TreatmentGroupMiniDTO {

    public TreatmentGroupMiniDTO(TreatmentGroup treatmentGroup) {
        this.setId(treatmentGroup.getId());
        this.setVersion(treatmentGroup.getVersion());
        this.setCodeName(treatmentGroup.getCodeName());
    }

    private Long id;

    private Integer version;

    private String codeName;

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

    public String getCodeName() {
        return this.codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static TreatmentGroupMiniDTO fromJsonToTreatmentGroupMiniDTO(String json) {
        return new JSONDeserializer<TreatmentGroupMiniDTO>()
                .use(null, TreatmentGroupMiniDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<TreatmentGroupMiniDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<TreatmentGroupMiniDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<TreatmentGroupMiniDTO> fromJsonArrayToTreatmentGroes(String json) {
        return new JSONDeserializer<List<TreatmentGroupMiniDTO>>()
                .use("values", TreatmentGroupMiniDTO.class).deserialize(json);
    }
}
