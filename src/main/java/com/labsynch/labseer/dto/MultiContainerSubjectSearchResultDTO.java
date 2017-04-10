package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.utils.ExcludeNulls;

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
	
}
