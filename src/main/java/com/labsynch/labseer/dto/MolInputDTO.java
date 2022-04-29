package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class MolInputDTO {
	
	private String structure;
	

	public String getStructure() {
        return this.structure;
    }

	public void setStructure(String structure) {
        this.structure = structure;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static MolInputDTO fromJsonToMolInputDTO(String json) {
        return new JSONDeserializer<MolInputDTO>()
        .use(null, MolInputDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<MolInputDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<MolInputDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<MolInputDTO> fromJsonArrayToMoes(String json) {
        return new JSONDeserializer<List<MolInputDTO>>()
        .use("values", MolInputDTO.class).deserialize(json);
    }
}
