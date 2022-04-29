package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;



public class CmpdRegStructureSearchDTO{
	
	private List<String> projects;
	
	private String searchType;

	private Float percentSimilarity;
	
	private String molStructure;
  
	private Integer maxResults;


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

	public static CmpdRegStructureSearchDTO fromJsonToCmpdRegStructureSearchDTO(String json) {
        return new JSONDeserializer<CmpdRegStructureSearchDTO>()
        .use(null, CmpdRegStructureSearchDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<CmpdRegStructureSearchDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<CmpdRegStructureSearchDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<CmpdRegStructureSearchDTO> fromJsonArrayToCmpdRegStructureSearchDTO(String json) {
        return new JSONDeserializer<List<CmpdRegStructureSearchDTO>>()
        .use("values", CmpdRegStructureSearchDTO.class).deserialize(json);
    }

	public List<String> getProjects() {
        return this.projects;
    }

	public void setProjects(List<String> projects) {
        this.projects = projects;
    }

	public String getSearchType() {
        return this.searchType;
    }

	public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

	public Float getPercentSimilarity() {
        return this.percentSimilarity;
    }

	public void setPercentSimilarity(Float percentSimilarity) {
        this.percentSimilarity = percentSimilarity;
    }

	public String getMolStructure() {
        return this.molStructure;
    }

	public void setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }

	public Integer getMaxResults() {
        return this.maxResults;
    }

	public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }
}
