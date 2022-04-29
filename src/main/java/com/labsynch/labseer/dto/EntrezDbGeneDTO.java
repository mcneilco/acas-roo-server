package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class EntrezDbGeneDTO {
	
    private String taxId;
    private String geneId;
    private String symbol;
    private String locusTag;
    private String synonyms;
    private String dbXrefs;
    private String chromosome;
    private String mapLocation;
    private String description;
    private String typeOfGene;
    private String symbolFromAuthority;
    private String fullNameFromAuthority;
    private String nomenclatureStatus;
    private String otherDesignations;
    private Date modificationDate;


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

	public static EntrezDbGeneDTO fromJsonToEntrezDbGeneDTO(String json) {
        return new JSONDeserializer<EntrezDbGeneDTO>()
        .use(null, EntrezDbGeneDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<EntrezDbGeneDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<EntrezDbGeneDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<EntrezDbGeneDTO> fromJsonArrayToEntrezDbGeneDTO(String json) {
        return new JSONDeserializer<List<EntrezDbGeneDTO>>()
        .use("values", EntrezDbGeneDTO.class).deserialize(json);
    }

	public String getTaxId() {
        return this.taxId;
    }

	public void setTaxId(String taxId) {
        this.taxId = taxId;
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

	public String getLocusTag() {
        return this.locusTag;
    }

	public void setLocusTag(String locusTag) {
        this.locusTag = locusTag;
    }

	public String getSynonyms() {
        return this.synonyms;
    }

	public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

	public String getDbXrefs() {
        return this.dbXrefs;
    }

	public void setDbXrefs(String dbXrefs) {
        this.dbXrefs = dbXrefs;
    }

	public String getChromosome() {
        return this.chromosome;
    }

	public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

	public String getMapLocation() {
        return this.mapLocation;
    }

	public void setMapLocation(String mapLocation) {
        this.mapLocation = mapLocation;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public String getTypeOfGene() {
        return this.typeOfGene;
    }

	public void setTypeOfGene(String typeOfGene) {
        this.typeOfGene = typeOfGene;
    }

	public String getSymbolFromAuthority() {
        return this.symbolFromAuthority;
    }

	public void setSymbolFromAuthority(String symbolFromAuthority) {
        this.symbolFromAuthority = symbolFromAuthority;
    }

	public String getFullNameFromAuthority() {
        return this.fullNameFromAuthority;
    }

	public void setFullNameFromAuthority(String fullNameFromAuthority) {
        this.fullNameFromAuthority = fullNameFromAuthority;
    }

	public String getNomenclatureStatus() {
        return this.nomenclatureStatus;
    }

	public void setNomenclatureStatus(String nomenclatureStatus) {
        this.nomenclatureStatus = nomenclatureStatus;
    }

	public String getOtherDesignations() {
        return this.otherDesignations;
    }

	public void setOtherDesignations(String otherDesignations) {
        this.otherDesignations = otherDesignations;
    }

	public Date getModificationDate() {
        return this.modificationDate;
    }

	public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
}
