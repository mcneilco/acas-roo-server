package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class MolPropertiesDTO {
	
	private String molStructure;
	
	private Double molWeight;
	
	private String molFormula;

	private String smiles;


	public String getMolStructure() {
        return this.molStructure;
    }

	public void setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }

	public Double getMolWeight() {
        return this.molWeight;
    }

	public void setMolWeight(Double molWeight) {
        this.molWeight = molWeight;
    }

	public String getMolFormula() {
        return this.molFormula;
    }

	public void setMolFormula(String molFormula) {
        this.molFormula = molFormula;
    }

	public String getSmiles() {
        return this.smiles;
    }

	public void setSmiles(String smiles) {
        this.smiles = smiles;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static MolPropertiesDTO fromJsonToMolPropertiesDTO(String json) {
        return new JSONDeserializer<MolPropertiesDTO>()
        .use(null, MolPropertiesDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<MolPropertiesDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<MolPropertiesDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<MolPropertiesDTO> fromJsonArrayToMolProes(String json) {
        return new JSONDeserializer<List<MolPropertiesDTO>>()
        .use("values", MolPropertiesDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


