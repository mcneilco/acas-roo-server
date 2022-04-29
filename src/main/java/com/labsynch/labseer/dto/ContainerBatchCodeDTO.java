package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ContainerBatchCodeDTO {

    private String batchCode;

    private String containerCodeName;

    private String containerBarcode;

    private String wellCodeName;

    private String wellName;

    public ContainerBatchCodeDTO() {
    }

    public ContainerBatchCodeDTO(String batchCode, String containerCodeName, String containerBarcode,
            String wellCodeName, String wellName) {
        this.batchCode = batchCode;
        this.containerCodeName = containerCodeName;
        this.containerBarcode = containerBarcode;
        this.wellCodeName = wellCodeName;
        this.wellName = wellName;
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public static String toJsonArray(Collection<ContainerBatchCodeDTO> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    public String getBatchCode() {
        return this.batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getContainerCodeName() {
        return this.containerCodeName;
    }

    public void setContainerCodeName(String containerCodeName) {
        this.containerCodeName = containerCodeName;
    }

    public String getContainerBarcode() {
        return this.containerBarcode;
    }

    public void setContainerBarcode(String containerBarcode) {
        this.containerBarcode = containerBarcode;
    }

    public String getWellCodeName() {
        return this.wellCodeName;
    }

    public void setWellCodeName(String wellCodeName) {
        this.wellCodeName = wellCodeName;
    }

    public String getWellName() {
        return this.wellName;
    }

    public void setWellName(String wellName) {
        this.wellName = wellName;
    }

    public static ContainerBatchCodeDTO fromJsonToContainerBatchCodeDTO(String json) {
        return new JSONDeserializer<ContainerBatchCodeDTO>()
                .use(null, ContainerBatchCodeDTO.class).deserialize(json);
    }

    public static Collection<ContainerBatchCodeDTO> fromJsonArrayToContainerBatchCoes(String json) {
        return new JSONDeserializer<List<ContainerBatchCodeDTO>>()
                .use("values", ContainerBatchCodeDTO.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
