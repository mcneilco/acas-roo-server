package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class GeneOrthologDTO {
	
	private Long id;
	private String recordedBy;

    private Integer rowIndex;

	private String geneId;
    private String symbol;
    private String taxId;
    private String species;
    private String typeOfGene;

    private String mappedGeneId;
    private String mappedSymbol;
    private String mappedTaxId;
    private String mappedSpecies;
    private String mappedTypeOfGene;
    
    private Integer mappedScore;
    private Integer mappedPerc;
    private Integer mappedHitLen;
    private Double mappedPercRatio;
    
    private Integer mappedLocalIndex;
    private Integer mappedGeneIndex;
    private Integer mappedIsoformIndex;
    
    private String orthCode;
    private String versionName;
    private Integer curationLevel;
    private String curator;

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
	

	public static GeneOrthologDTO fromJsonToGeneOrthologDTO(String json) {
        return new JSONDeserializer<GeneOrthologDTO>().use(null, GeneOrthologDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<GeneOrthologDTO> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
	
	public static Collection<GeneOrthologDTO> fromJsonArrayToGeneOrtholoes(String json) {
        return new JSONDeserializer<List<GeneOrthologDTO>>().use(null, ArrayList.class).use("values", GeneOrthologDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public Integer getRowIndex() {
        return this.rowIndex;
    }

	public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

	public String getGeneId() {
        return this.geneId;
    }

	public void setGeneId(String geneId) {
        this.geneId = geneId;
    }

	public String getSymbol() {
        return this.symbol;
    }

	public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

	public String getTaxId() {
        return this.taxId;
    }

	public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

	public String getSpecies() {
        return this.species;
    }

	public void setSpecies(String species) {
        this.species = species;
    }

	public String getTypeOfGene() {
        return this.typeOfGene;
    }

	public void setTypeOfGene(String typeOfGene) {
        this.typeOfGene = typeOfGene;
    }

	public String getMappedGeneId() {
        return this.mappedGeneId;
    }

	public void setMappedGeneId(String mappedGeneId) {
        this.mappedGeneId = mappedGeneId;
    }

	public String getMappedSymbol() {
        return this.mappedSymbol;
    }

	public void setMappedSymbol(String mappedSymbol) {
        this.mappedSymbol = mappedSymbol;
    }

	public String getMappedTaxId() {
        return this.mappedTaxId;
    }

	public void setMappedTaxId(String mappedTaxId) {
        this.mappedTaxId = mappedTaxId;
    }

	public String getMappedSpecies() {
        return this.mappedSpecies;
    }

	public void setMappedSpecies(String mappedSpecies) {
        this.mappedSpecies = mappedSpecies;
    }

	public String getMappedTypeOfGene() {
        return this.mappedTypeOfGene;
    }

	public void setMappedTypeOfGene(String mappedTypeOfGene) {
        this.mappedTypeOfGene = mappedTypeOfGene;
    }

	public Integer getMappedScore() {
        return this.mappedScore;
    }

	public void setMappedScore(Integer mappedScore) {
        this.mappedScore = mappedScore;
    }

	public Integer getMappedPerc() {
        return this.mappedPerc;
    }

	public void setMappedPerc(Integer mappedPerc) {
        this.mappedPerc = mappedPerc;
    }

	public Integer getMappedHitLen() {
        return this.mappedHitLen;
    }

	public void setMappedHitLen(Integer mappedHitLen) {
        this.mappedHitLen = mappedHitLen;
    }

	public Double getMappedPercRatio() {
        return this.mappedPercRatio;
    }

	public void setMappedPercRatio(Double mappedPercRatio) {
        this.mappedPercRatio = mappedPercRatio;
    }

	public Integer getMappedLocalIndex() {
        return this.mappedLocalIndex;
    }

	public void setMappedLocalIndex(Integer mappedLocalIndex) {
        this.mappedLocalIndex = mappedLocalIndex;
    }

	public Integer getMappedGeneIndex() {
        return this.mappedGeneIndex;
    }

	public void setMappedGeneIndex(Integer mappedGeneIndex) {
        this.mappedGeneIndex = mappedGeneIndex;
    }

	public Integer getMappedIsoformIndex() {
        return this.mappedIsoformIndex;
    }

	public void setMappedIsoformIndex(Integer mappedIsoformIndex) {
        this.mappedIsoformIndex = mappedIsoformIndex;
    }

	public String getOrthCode() {
        return this.orthCode;
    }

	public void setOrthCode(String orthCode) {
        this.orthCode = orthCode;
    }

	public String getVersionName() {
        return this.versionName;
    }

	public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

	public Integer getCurationLevel() {
        return this.curationLevel;
    }

	public void setCurationLevel(Integer curationLevel) {
        this.curationLevel = curationLevel;
    }

	public String getCurator() {
        return this.curator;
    }

	public void setCurator(String curator) {
        this.curator = curator;
    }
}
