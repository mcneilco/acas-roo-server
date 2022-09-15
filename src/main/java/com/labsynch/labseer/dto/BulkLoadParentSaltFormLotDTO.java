package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class BulkLoadParentSaltFormLotDTO {

    private String parentCorpName;

    private Long parentBulkLoadFileId;

    private String saltFormCorpName;

    private Long saltFormBulkLoadFileId;

    private String lotCorpName;

    private Long lotBulkLoadFileId;

    public BulkLoadParentSaltFormLotDTO(String parentCorpName, Long parentBulkLoadFileId, String saltFormCorpName, Long saltFormBulkLoadFileId, String lotCorpName, Long lotBulkLoadFileId) {
        this.parentCorpName = parentCorpName;
        this.parentBulkLoadFileId = parentBulkLoadFileId;
        this.saltFormCorpName = saltFormCorpName;
        this.saltFormBulkLoadFileId = saltFormBulkLoadFileId;
        this.lotCorpName = lotCorpName;
        this.lotBulkLoadFileId = lotBulkLoadFileId;
    }

    public BulkLoadParentSaltFormLotDTO() {

    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public static String toJsonArray(Collection<BulkLoadParentSaltFormLotDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static BulkLoadParentSaltFormLotDTO fromJsonToBulkLoadParentSaltFormLotDTO(String json) {
        return new JSONDeserializer<BulkLoadParentSaltFormLotDTO>()
                .use(null, BulkLoadParentSaltFormLotDTO.class).deserialize(json);
    }

    public static Collection<BulkLoadParentSaltFormLotDTO> fromJsonArrayToBulkLoadParentSaltFormLotDTOs(String json) {
        return new JSONDeserializer<List<BulkLoadParentSaltFormLotDTO>>()
                .use("values", BulkLoadParentSaltFormLotDTO.class).deserialize(json);
    }

    public String getParentCorpName() {
        return this.parentCorpName;
    }

    public void setParentCorpName(String parentCorpName) {
        this.parentCorpName = parentCorpName;
    }

    public Long getParentBulkLoadFileId() {
        return this.parentBulkLoadFileId;
    }

    public void setParentBulkLoadFileId(Long parentBulkLoadFileId) {
        this.parentBulkLoadFileId = parentBulkLoadFileId;
    }

    public String getSaltFormCorpName() {
        return this.saltFormCorpName;
    }

    public void setSaltFormCorpName(String saltFormCorpName) {
        this.saltFormCorpName = saltFormCorpName;
    }

    public Long getSaltFormBulkLoadFileId() {
        return this.saltFormBulkLoadFileId;
    }

    public void setSaltFormBulkLoadFileId(Long saltFormBulkLoadFileId) {
        this.saltFormBulkLoadFileId = saltFormBulkLoadFileId;
    }

    public String getLotCorpName() {
        return this.lotCorpName;
    }

    public void setLotCorpName(String lotCorpName) {
        this.lotCorpName = lotCorpName;
    }

    public Long getLotBulkLoadFileId() {
        return this.lotBulkLoadFileId;
    }

    public void setLotBulkLoadFileId(Long lotBulkLoadFileId) {
        this.lotBulkLoadFileId = lotBulkLoadFileId;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
