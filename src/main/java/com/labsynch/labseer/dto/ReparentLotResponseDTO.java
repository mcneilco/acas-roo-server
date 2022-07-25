package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.domain.Lot;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ReparentLotResponseDTO {

    private Lot newLot;

    private Boolean originalParentDeleted;

    public Boolean isOriginalParentDeleted() {
        return this.originalParentDeleted;
    }

    public Boolean getOriginalParentDeleted() {
        return this.originalParentDeleted;
    }

    public void setOriginalParentDeleted(Boolean originalParentDeleted) {
        this.originalParentDeleted = originalParentDeleted;
    }

    private String originalLotCorpName;

    private String originalParentCorpName;

    private String modifiedBy;

    public Lot getNewLot() {
        return this.newLot;
    }

    public void setNewLot(Lot newLot) {
        this.newLot = newLot;
    }

    public String getOriginalParentCorpName() {
        return this.originalParentCorpName;
    }

    public void setOriginalParentCorpName(String originalParentCorpName) {
        this.originalParentCorpName = originalParentCorpName;
    }

    public String getOriginalLotCorpName() {
        return this.originalLotCorpName;
    }

    public void setOriginalLotCorpName(String originalLotCorpName) {
        this.originalLotCorpName = originalLotCorpName;
    }

    public String getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    
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

    public static ReparentLotDTO fromJsonToReparentLotDTO(String json) {
        return new JSONDeserializer<ReparentLotDTO>()
                .use(null, ReparentLotDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ReparentLotDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ReparentLotDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ReparentLotDTO> fromJsonArrayToReparentLoes(String json) {
        return new JSONDeserializer<List<ReparentLotDTO>>()
                .use("values", ReparentLotDTO.class).deserialize(json);
    }
}
