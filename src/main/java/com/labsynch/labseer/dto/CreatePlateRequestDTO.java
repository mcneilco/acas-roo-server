package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class CreatePlateRequestDTO {

    private String barcode;

    private String definition;

    private String template;

    private String description;

    private String recordedBy;

    private String createdUser;

    private Date createdDate;

    private String physicalState;

    private String batchConcentrationUnits;

    private Collection<WellContentDTO> wells = new ArrayList<WellContentDTO>();

    public String toJson() {
        return new JSONSerializer().include("wells").exclude("*.class").serialize(this);
    }

    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDefinition() {
        return this.definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getTemplate() {
        return this.template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecordedBy() {
        return this.recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public String getCreatedUser() {
        return this.createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getPhysicalState() {
        return this.physicalState;
    }

    public void setPhysicalState(String physicalState) {
        this.physicalState = physicalState;
    }

    public String getBatchConcentrationUnits() {
        return this.batchConcentrationUnits;
    }

    public void setBatchConcentrationUnits(String batchConcentrationUnits) {
        this.batchConcentrationUnits = batchConcentrationUnits;
    }

    public Collection<WellContentDTO> getWells() {
        return this.wells;
    }

    public void setWells(Collection<WellContentDTO> wells) {
        this.wells = wells;
    }

    public static CreatePlateRequestDTO fromJsonToCreatePlateRequestDTO(String json) {
        return new JSONDeserializer<CreatePlateRequestDTO>()
                .use(null, CreatePlateRequestDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<CreatePlateRequestDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<CreatePlateRequestDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<CreatePlateRequestDTO> fromJsonArrayToCreatePlateRequestDTO(String json) {
        return new JSONDeserializer<List<CreatePlateRequestDTO>>()
                .use("values", CreatePlateRequestDTO.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
