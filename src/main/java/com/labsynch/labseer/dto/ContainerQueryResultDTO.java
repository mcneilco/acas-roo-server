package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
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
	
}
