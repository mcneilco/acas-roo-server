package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class PreferredNameRequestDTO {

	private boolean error;
	
	private Collection<ErrorMessageDTO> errorMessages;

	private Collection<PreferredNameDTO> requests;
	

	public String toJson() {
        return new JSONSerializer().exclude("*.class").include("errorMessages", "requests").serialize(this);
    }

	public static PreferredNameRequestDTO fromJsonToPreferredNameResultsDTO(String json) {
        return new JSONDeserializer<PreferredNameRequestDTO>().use(null, PreferredNameRequestDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<PreferredNameRequestDTO> collection) {
        return new JSONSerializer().exclude("*.class").include("errorMessages", "requests").serialize(collection);
    }

	public static Collection<PreferredNameRequestDTO> fromJsonArrayToPreferredNameResultsDTO(String json) {
        return new JSONDeserializer<List<PreferredNameRequestDTO>>().use(null, ArrayList.class).use("values", PreferredNameRequestDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static PreferredNameRequestDTO fromJsonToPreferredNameRequestDTO(String json) {
        return new JSONDeserializer<PreferredNameRequestDTO>()
        .use(null, PreferredNameRequestDTO.class).deserialize(json);
    }

	public static Collection<PreferredNameRequestDTO> fromJsonArrayToPreferredNameRequestDTO(String json) {
        return new JSONDeserializer<List<PreferredNameRequestDTO>>()
        .use("values", PreferredNameRequestDTO.class).deserialize(json);
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

	public Collection<PreferredNameDTO> getRequests() {
        return this.requests;
    }

	public void setRequests(Collection<PreferredNameDTO> requests) {
        this.requests = requests;
    }
}


