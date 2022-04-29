package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class PreferredNameResultsDTO {

	private boolean error;
	
	private Collection<ErrorMessageDTO> errorMessages;

	private Collection<PreferredNameDTO> results;
	

	public String toJson() {
        return new JSONSerializer().exclude("*.class").include("errorMessages", "results").serialize(this);
    }

	public static PreferredNameResultsDTO fromJsonToPreferredNameResultsDTO(String json) {
        return new JSONDeserializer<PreferredNameResultsDTO>().use(null, PreferredNameResultsDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<PreferredNameResultsDTO> collection) {
        return new JSONSerializer().exclude("*.class").include("errorMessages", "results").serialize(collection);
    }

	public static Collection<PreferredNameResultsDTO> fromJsonArrayToPreferredNameResultsDTO(String json) {
        return new JSONDeserializer<List<PreferredNameResultsDTO>>().use(null, ArrayList.class).use("values", PreferredNameResultsDTO.class).deserialize(json);
    }

	public boolean isError() {
        return this.error;
    }

	public void setError(boolean error) {
        this.error = error;
    }

	public Collection<ErrorMessageDTO> getErrorMessages() {
        return this.errorMessages;
    }

	public void setErrorMessages(Collection<ErrorMessageDTO> errorMessages) {
        this.errorMessages = errorMessages;
    }

	public Collection<PreferredNameDTO> getResults() {
        return this.results;
    }

	public void setResults(Collection<PreferredNameDTO> results) {
        this.results = results;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


