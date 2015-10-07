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
}


