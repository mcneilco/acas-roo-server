package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


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


	public static LsTransactionQueryDTO fromJsonToLsTransactionQueryDTO(String json) {
        return new JSONDeserializer<LsTransactionQueryDTO>()
        .use(null, LsTransactionQueryDTO.class).deserialize(json);
    }

	public static Collection<LsTransactionQueryDTO> fromJsonArrayToLsTransactioes(String json) {
        return new JSONDeserializer<List<LsTransactionQueryDTO>>()
        .use("values", LsTransactionQueryDTO.class).deserialize(json);
    }

	public Date getRecordedDateGreaterThan() {
        return this.recordedDateGreaterThan;
    }

	public void setRecordedDateGreaterThan(Date recordedDateGreaterThan) {
        this.recordedDateGreaterThan = recordedDateGreaterThan;
    }

	public Date getRecordedDateLessThan() {
        return this.recordedDateLessThan;
    }

	public void setRecordedDateLessThan(Date recordedDateLessThan) {
        this.recordedDateLessThan = recordedDateLessThan;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public String getStatus() {
        return this.status;
    }

	public void setStatus(String status) {
        this.status = status;
    }

	public String getType() {
        return this.type;
    }

	public void setType(String type) {
        this.type = type;
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

	public Collection<LsTransaction> getResults() {
        return this.results;
    }

	public void setResults(Collection<LsTransaction> results) {
        this.results = results;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


