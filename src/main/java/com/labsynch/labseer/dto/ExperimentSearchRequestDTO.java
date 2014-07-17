package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


/**
*
* @model ExperimentSearchRequestDTO
*
*/

@RooJavaBean
@RooToString
@RooJson
public class ExperimentSearchRequestDTO {

	/**
	  * @property advancedFilter:string "example (Q1 and Q2) or (Q3 and Q4)"
	  */
	private String advancedFilter;
	
	private String advancedFilterSQL;
	
	private String booleanFilter;

	private Set<ExperimentFilterSearchDTO> searchFilters = new HashSet<ExperimentFilterSearchDTO>();

	private Set<String> batchCodeList = new HashSet<String>();
	
	private Set<String> experimentCodeList = new HashSet<String>();
	

	public String toJson() {
        return new JSONSerializer().include("searchFilters", "batchCodeList", "experimentCodeList").exclude("*.class").serialize(this);
    }

	public static ExperimentSearchRequestDTO fromJsonToExperimentSearchRequestDTO(String json) {
        return new JSONDeserializer<ExperimentSearchRequestDTO>().use(null, ExperimentSearchRequestDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ExperimentSearchRequestDTO> collection) {
        return new JSONSerializer().include("searchFilters", "batchCodeList", "experimentCodeList").exclude("*.class").serialize(collection);
    }

	public static Collection<ExperimentSearchRequestDTO> fromJsonArrayToExperimentSearchRequestDTO(String json) {
        return new JSONDeserializer<List<ExperimentSearchRequestDTO>>().use(null, ArrayList.class).use("values", ExperimentSearchRequestDTO.class).deserialize(json);
    }
}
	



