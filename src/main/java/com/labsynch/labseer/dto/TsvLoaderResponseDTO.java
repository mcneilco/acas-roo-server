package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class TsvLoaderResponseDTO {

	private static final Logger logger = LoggerFactory.getLogger(TsvLoaderResponseDTO.class);

	public TsvLoaderResponseDTO(){
		//empty constructor
	}

	private Collection<TempThingDTO> analysisGroups;
	
	private Collection<TempThingDTO> treatmentGroups;
	
	private Collection<TempThingDTO> subjects;
	
	public String toJson() {
        return new JSONSerializer().include("analysisGroups","treatmentGroups","subjects").exclude("*.class").serialize(this);
    }
	
	public static String toJsonArray(Collection<TsvLoaderResponseDTO> collection) {
        return new JSONSerializer().include("analysisGroups","treatmentGroups","subjects").exclude("*.class").serialize(collection);
    }


	public static TsvLoaderResponseDTO fromJsonToTsvLoaderResponseDTO(String json) {
        return new JSONDeserializer<TsvLoaderResponseDTO>()
        .use(null, TsvLoaderResponseDTO.class).deserialize(json);
    }

	public static Collection<TsvLoaderResponseDTO> fromJsonArrayToTsvLoaderRespoes(String json) {
        return new JSONDeserializer<List<TsvLoaderResponseDTO>>()
        .use("values", TsvLoaderResponseDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Collection<TempThingDTO> getAnalysisGroups() {
        return this.analysisGroups;
    }

	public void setAnalysisGroups(Collection<TempThingDTO> analysisGroups) {
        this.analysisGroups = analysisGroups;
    }

	public Collection<TempThingDTO> getTreatmentGroups() {
        return this.treatmentGroups;
    }

	public void setTreatmentGroups(Collection<TempThingDTO> treatmentGroups) {
        this.treatmentGroups = treatmentGroups;
    }

	public Collection<TempThingDTO> getSubjects() {
        return this.subjects;
    }

	public void setSubjects(Collection<TempThingDTO> subjects) {
        this.subjects = subjects;
    }
}


