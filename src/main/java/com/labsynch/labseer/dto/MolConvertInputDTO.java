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
public class MolConvertInputDTO {
	
	private String structure;
	
	private String inputFormat;
	
	private String parameters;


	public String getStructure() {
        return this.structure;
    }

	public void setStructure(String structure) {
        this.structure = structure;
    }

	public String getInputFormat() {
        return this.inputFormat;
    }

	public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

	public String getParameters() {
        return this.parameters;
    }

	public void setParameters(String parameters) {
        this.parameters = parameters;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static MolConvertInputDTO fromJsonToMolConvertInputDTO(String json) {
        return new JSONDeserializer<MolConvertInputDTO>()
        .use(null, MolConvertInputDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<MolConvertInputDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<MolConvertInputDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<MolConvertInputDTO> fromJsonArrayToMolCoes(String json) {
        return new JSONDeserializer<List<MolConvertInputDTO>>()
        .use("values", MolConvertInputDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
