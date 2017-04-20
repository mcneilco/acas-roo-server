package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class AuthorQueryResultDTO {

	Integer maxResults;
	
	Integer numberOfResults;
	
	Collection<Author> results;
	
	public AuthorQueryResultDTO(){
		
	}
	
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class").include("results.lsTags", "results.lsLabels", "results.lsStates.lsValues", "results.authorRoles.roleEntry").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
}
