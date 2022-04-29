package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Long getExperimentId() {
        return this.experimentId;
    }

	public void setExperimentId(Long experimentId) {
        this.experimentId = experimentId;
    }

	public String getExperimentCode() {
        return this.experimentCode;
    }

	public void setExperimentCode(String experimentCode) {
        this.experimentCode = experimentCode;
    }

	public String getExperimentName() {
        return this.experimentName;
    }

	public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

	public Collection<ValueTypeKindDTO> getValueKinds() {
        return this.valueKinds;
    }

	public void setValueKinds(Collection<ValueTypeKindDTO> valueKinds) {
        this.valueKinds = valueKinds;
    }
}


