package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class MultiContainerSubjectSearchResultDTO {

	Integer maxResults;
	
	Integer numberOfResults;
	
	Collection<ContainerSubjectsDTO> results;
	
	public MultiContainerSubjectSearchResultDTO(){
		
	}
	
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class").include("results", "results.subjects.lsTags", "results.subjects.lsLabels", "results.subjects.lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	 @Transactional
    public String toJsonWithNestedStubs() {
        return new JSONSerializer().exclude("*.class").include("results", "results.subjects.lsTags", "results.subjects.lsLabels", "results.subjects.lsStates.lsValues", "results.subjects.firstSubjects.firstSubject.lsLabels","results.subjects.secondSubjects.secondSubject.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
    @Transactional
    public String toJsonWithNestedFull() {
        return new JSONSerializer().exclude("*.class").include("results", "results.subjects.lsTags", "results.subjects.lsLabels", "results.subjects.lsStates.lsValues", "results.subjects.firstSubjects.firstSubject.lsStates.lsValues","results.subjects.firstSubjects.firstSubject.lsLabels", "results.subjects.secondSubjects.secondSubject.lsStates.lsValues","results.subjects.secondSubjects.secondSubject.lsLabels","results.subjects.firstSubjects.lsStates.lsValues","results.subjects.firstSubjects.lsLabels","results.subjects.secondSubjects.lsStates.lsValues","results.subjects.secondSubjects.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toPrettyJson() {
        return new JSONSerializer().exclude("*.class", "results.subjects.lsStates.lsValues.lsState", "results.subjects.lsStates.subject", "results.subjects.lsLabels.subject").include("results", "results.subjects.lsTags", "results.subjects.lsLabels", "results.subjects.lsStates.lsValues").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "results.subjects.lsStates").include("results", "results.subjects.lsTags", "results.subjects.lsLabels").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
    }
	

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static MultiContainerSubjectSearchResultDTO fromJsonToMultiContainerSubjectSearchResultDTO(String json) {
        return new JSONDeserializer<MultiContainerSubjectSearchResultDTO>()
        .use(null, MultiContainerSubjectSearchResultDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<MultiContainerSubjectSearchResultDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<MultiContainerSubjectSearchResultDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<MultiContainerSubjectSearchResultDTO> fromJsonArrayToMultiCoes(String json) {
        return new JSONDeserializer<List<MultiContainerSubjectSearchResultDTO>>()
        .use("values", MultiContainerSubjectSearchResultDTO.class).deserialize(json);
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

	public Collection<ContainerSubjectsDTO> getResults() {
        return this.results;
    }

	public void setResults(Collection<ContainerSubjectsDTO> results) {
        this.results = results;
    }
}
