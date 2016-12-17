package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
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
        return new JSONSerializer().exclude("*.class", "results.lsStates.subject").include("results.lsTags", "results.lsLabels").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
}
