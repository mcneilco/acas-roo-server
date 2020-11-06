package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class StructureSearchDTO {
	
	private String queryMol;
	private String lsType;
	private String lsKind;
	private String searchType;
	private Integer maxResults;
	private Float similarity;
	
	public StructureSearchDTO(){
	}
	
	public StructureSearchDTO(String queryMol, String lsType, String lsKind, String searchType, Integer maxResults, Float similarity){
		this.queryMol = queryMol;
		this.lsType = lsType;
		this.lsKind = lsKind;
		this.searchType = searchType;
		this.maxResults = maxResults;
		this.similarity = similarity;
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

	public static StructureSearchDTO fromJsonToStructureSearchDTO(String json) {
        return new JSONDeserializer<StructureSearchDTO>()
        .use(null, StructureSearchDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<StructureSearchDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<StructureSearchDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<StructureSearchDTO> fromJsonArrayToStructureSearchDTO(String json) {
        return new JSONDeserializer<List<StructureSearchDTO>>()
        .use("values", StructureSearchDTO.class).deserialize(json);
    }

	public String getQueryMol() {
        return this.queryMol;
    }

	public void setQueryMol(String queryMol) {
        this.queryMol = queryMol;
    }

	public String getLsType() {
        return this.lsType;
    }

	public void setLsType(String lsType) {
        this.lsType = lsType;
    }

	public String getLsKind() {
        return this.lsKind;
    }

	public void setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }

	public String getSearchType() {
        return this.searchType;
    }

	public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

	public Integer getMaxResults() {
        return this.maxResults;
    }

	public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

	public Float getSimilarity() {
        return this.similarity;
    }

	public void setSimilarity(Float similarity) {
        this.similarity = similarity;
    }
}


