package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class LotsByProjectDTO {
    private Long id;
    private String lotCorpName;
    private Integer lotNumber;
    private Date registrationDate;
    private String parentCorpName;
    private String project;
    private String chemist;
    private String vendorName;
    private String vendorCode;
    private String supplier;

    public LotsByProjectDTO(
            long id,
            String lotCorpName,
            int lotNumber,
            Date registrationDate,
            String parentCorpName,
            String project,
            String chemist,
            String vendorName,
            String vendorCode,
            String supplier) {

        this.id = id;
        this.lotCorpName = lotCorpName;
        this.lotNumber = lotNumber;
        this.registrationDate = registrationDate;
        this.parentCorpName = parentCorpName;
        this.project = project;
        this.chemist = chemist;
        this.vendorName = vendorName;
        this.vendorCode = vendorCode;
        this.supplier = supplier;
    }

    public LotsByProjectDTO() {
        // Empty constructor
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLotCorpName() {
        return this.lotCorpName;
    }

    public void setLotCorpName(String lotCorpName) {
        this.lotCorpName = lotCorpName;
    }

    public String getChemist() {
        return this.chemist;
    }

    public void setChemist(String chemist) {
        this.chemist = chemist;
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorCode() {
        return this.vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getSupplier() {
        return this.supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Integer getLotNumber() {
        return this.lotNumber;
    }

    public void setLotNumber(Integer lotNumber) {
        this.lotNumber = lotNumber;
    }

    public Date getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getParentCorpName() {
        return this.parentCorpName;
    }

    public void setParentCorpName(String parentCorpName) {
        this.parentCorpName = parentCorpName;
    }

    public String getProject() {
        return this.project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static LotsByProjectDTO fromJsonToLotsByProjectDTO(String json) {
        return new JSONDeserializer<LotsByProjectDTO>()
                .use(null, LotsByProjectDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<LotsByProjectDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<LotsByProjectDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<LotsByProjectDTO> fromJsonArrayToLotsByProes(String json) {
        return new JSONDeserializer<List<LotsByProjectDTO>>()
                .use("values", LotsByProjectDTO.class).deserialize(json);
    }
}
