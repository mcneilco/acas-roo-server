package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class LsThingQueryResultDTO {

	Integer maxResults;
	
	Integer numberOfResults;
	
	Collection<LsThing> results;
	
	public LsThingQueryResultDTO(){
		
	}
	
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class").include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	 @Transactional
    public String toJsonWithNestedStubs() {
        return new JSONSerializer().exclude("*.class").include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues", "results.firstLsThings.firstLsThing.lsLabels","results.secondLsThings.secondLsThing.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
    @Transactional
    public String toJsonWithNestedFull() {
        return new JSONSerializer().exclude("*.class").include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues", "results.firstLsThings.firstLsThing.lsStates.lsValues","results.firstLsThings.firstLsThing.lsLabels", "results.secondLsThings.secondLsThing.lsStates.lsValues","results.secondLsThings.secondLsThing.lsLabels","results.firstLsThings.lsStates.lsValues","results.firstLsThings.lsLabels","results.secondLsThings.lsStates.lsValues","results.secondLsThings.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
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
