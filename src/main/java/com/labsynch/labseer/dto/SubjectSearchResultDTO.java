package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class SubjectSearchResultDTO {

	Integer maxResults;
	
	Integer numberOfResults;
	
	Collection<Subject> results;
	
	public SubjectSearchResultDTO(){
		
	}
	
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class").include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	 @Transactional
    public String toJsonWithNestedStubs() {
        return new JSONSerializer().exclude("*.class").include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues", "results.firstSubjects.firstSubject.lsLabels","results.secondSubjects.secondSubject.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
    @Transactional
    public String toJsonWithNestedFull() {
        return new JSONSerializer().exclude("*.class").include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues", "results.firstSubjects.firstSubject.lsStates.lsValues","results.firstSubjects.firstSubject.lsLabels", "results.secondSubjects.secondSubject.lsStates.lsValues","results.secondSubjects.secondSubject.lsLabels","results.firstSubjects.lsStates.lsValues","results.firstSubjects.lsLabels","results.secondSubjects.lsStates.lsValues","results.secondSubjects.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toPrettyJson() {
        return new JSONSerializer().exclude("*.class", "results.lsStates.lsValues.lsState", "results.lsStates.subject", "results.lsLabels.subject").include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "results.lsStates").include("results.lsTags", "results.lsLabels").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
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

	public Collection<Subject> getResults() {
        return this.results;
    }

	public void setResults(Collection<Subject> results) {
        this.results = results;
    }

	public static SubjectSearchResultDTO fromJsonToSubjectSearchResultDTO(String json) {
        return new JSONDeserializer<SubjectSearchResultDTO>()
        .use(null, SubjectSearchResultDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SubjectSearchResultDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SubjectSearchResultDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SubjectSearchResultDTO> fromJsonArrayToSubjectSearchResultDTO(String json) {
        return new JSONDeserializer<List<SubjectSearchResultDTO>>()
        .use("values", SubjectSearchResultDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
