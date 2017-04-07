package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class LsTransactionQueryDTO {
	
	Date recordedDateGreaterThan;
	
	Date recordedDateLessThan;
	
	String recordedBy;
	
	String status;
	
	String type;
		
	Integer maxResults;
	
	Integer numberOfResults;
	
	Collection<LsTransaction> results;
	
	public String toJson() {
        return new JSONSerializer().exclude("*.class").include("results").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<LsTransactionQueryDTO> collection) {
        return new JSONSerializer().exclude("*.class").include("results").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

}


