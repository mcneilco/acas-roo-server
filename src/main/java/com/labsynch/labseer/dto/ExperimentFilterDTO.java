package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ExperimentFilterDTO {
	
	private Long experimentId;

	private String experimentCode;
	
	private String experimentName;

	private Collection<ValueTypeKindDTO> valueKinds = new HashSet<ValueTypeKindDTO>();


	

	

	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public static ExperimentFilterDTO fromJsonToExperimentFilterDTO(String json) {
        return new JSONDeserializer<ExperimentFilterDTO>().use(null, ExperimentFilterDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ExperimentFilterDTO> collection) {
        return new JSONSerializer().include("valueKinds").exclude("*.class").serialize(collection);
    }
	
	public static String toPrettyJsonArray(Collection<ExperimentFilterDTO> collection) {
        return new JSONSerializer().include("valueKinds").exclude("*.class").prettyPrint(true).serialize(collection);
    }

	public static Collection<ExperimentFilterDTO> fromJsonArrayToExperimentFilterDTO(String json) {
        return new JSONDeserializer<List<ExperimentFilterDTO>>().use(null, ArrayList.class).use("values", ExperimentFilterDTO.class).deserialize(json);
    }
}


