package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class GenericQueryCodeTableResultDTO {

	Integer maxResults;
	
	Integer numberOfResults;
	
	Collection<CodeTableDTO> results;
	
	public GenericQueryCodeTableResultDTO(){
		
	}
	
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class").include("results").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static GenericQueryCodeTableResultDTO fromJsonToGenericQueryCodeTableResultDTO(String json) {
        return new JSONDeserializer<GenericQueryCodeTableResultDTO>()
        .use(null, GenericQueryCodeTableResultDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<GenericQueryCodeTableResultDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<GenericQueryCodeTableResultDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<GenericQueryCodeTableResultDTO> fromJsonArrayToGenericQueryCoes(String json) {
        return new JSONDeserializer<List<GenericQueryCodeTableResultDTO>>()
        .use("values", GenericQueryCodeTableResultDTO.class).deserialize(json);
    }

	public Integer getMaxResults() {
        return this.maxResults;
    }

	public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

	public Integer getNumberOfResults() {
        return this.numberOfResults;
    }

	public void setNumberOfResults(Integer numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

	public Collection<CodeTableDTO> getResults() {
        return this.results;
    }

	public void setResults(Collection<CodeTableDTO> results) {
        this.results = results;
    }
}
