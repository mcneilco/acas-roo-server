package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class StructureAndThingSearchDTO {
	
	private String queryMol;
	private String searchType;
	private Integer maxResults;
	private Float similarity;
	private LsThingQueryDTO lsThingQueryDTO;
	
	public StructureAndThingSearchDTO(){
	}
	
	public StructureAndThingSearchDTO(LsThingQueryDTO lsThingQueryDTO, String queryMol, String searchType, Integer maxResults, Float similarity){
		this.lsThingQueryDTO = lsThingQueryDTO;
		this.queryMol = queryMol;
		this.searchType = searchType;
		this.maxResults = maxResults;
		this.similarity = similarity;
	}


	public String toJson() {
        return new JSONSerializer().exclude("*.class").include("lsThingQueryDTO.labels","lsThingQueryDTO.values").serialize(this);
    }

	public static StructureAndThingSearchDTO fromJsonToStructureAndThingSearchDTO(String json) {
        return new JSONDeserializer<StructureAndThingSearchDTO>().use(null, StructureAndThingSearchDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<StructureAndThingSearchDTO> collection) {
        return new JSONSerializer().exclude("*.class").include("lsThingQueryDTO.labels","lsThingQueryDTO.values").serialize(collection);
    }

	public static Collection<StructureAndThingSearchDTO> fromJsonArrayToStructureAndThingSearchDTO(String json) {
        return new JSONDeserializer<List<StructureAndThingSearchDTO>>().use(null, ArrayList.class).use("values", StructureAndThingSearchDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getQueryMol() {
        return this.queryMol;
    }

	public void setQueryMol(String queryMol) {
        this.queryMol = queryMol;
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

	public LsThingQueryDTO getLsThingQueryDTO() {
        return this.lsThingQueryDTO;
    }

	public void setLsThingQueryDTO(LsThingQueryDTO lsThingQueryDTO) {
        this.lsThingQueryDTO = lsThingQueryDTO;
    }
}


