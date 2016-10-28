package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
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
}


