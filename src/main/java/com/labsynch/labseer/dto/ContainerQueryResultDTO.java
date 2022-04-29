package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class ContainerQueryResultDTO {

	Integer maxResults;
	
	Integer numberOfResults;
	
	Collection<Container> results;
	
	public ContainerQueryResultDTO(){
		
	}
	
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class").include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	 @Transactional
    public String toJsonWithNestedStubs() {
        return new JSONSerializer().exclude("*.class").include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues", "results.firstContainers.firstContainer.lsLabels","results.secondContainers.secondContainer.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
    @Transactional
    public String toJsonWithNestedFull() {
        return new JSONSerializer().exclude("*.class").include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues", "results.firstContainers.firstContainer.lsStates.lsValues","results.firstContainers.firstContainer.lsLabels", "results.secondContainers.secondContainer.lsStates.lsValues","results.secondContainers.secondContainer.lsLabels","results.firstContainers.lsStates.lsValues","results.firstContainers.lsLabels","results.secondContainers.lsStates.lsValues","results.secondContainers.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toPrettyJson() {
        return new JSONSerializer().exclude("*.class", "results.lsStates.lsValues.lsState", "results.lsStates.lsThing", "results.lsLabels.lsThing").include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "results.lsStates.lsThing").include("results.lsTags", "results.lsLabels").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
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

	public Collection<Container> getResults() {
        return this.results;
    }

	public void setResults(Collection<Container> results) {
        this.results = results;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static ContainerQueryResultDTO fromJsonToContainerQueryResultDTO(String json) {
        return new JSONDeserializer<ContainerQueryResultDTO>()
        .use(null, ContainerQueryResultDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ContainerQueryResultDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ContainerQueryResultDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ContainerQueryResultDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<ContainerQueryResultDTO>>()
        .use("values", ContainerQueryResultDTO.class).deserialize(json);
    }
}
