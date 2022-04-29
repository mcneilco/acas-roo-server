package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class CleanMolOptionsDTO {
	
	private int dim;
	
	private String opts;
	
	

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static CleanMolOptionsDTO fromJsonToCleanMolOptionsDTO(String json) {
        return new JSONDeserializer<CleanMolOptionsDTO>()
        .use(null, CleanMolOptionsDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<CleanMolOptionsDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<CleanMolOptionsDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<CleanMolOptionsDTO> fromJsonArrayToCleanMolOptioes(String json) {
        return new JSONDeserializer<List<CleanMolOptionsDTO>>()
        .use("values", CleanMolOptionsDTO.class).deserialize(json);
    }

	public int getDim() {
        return this.dim;
    }

	public void setDim(int dim) {
        this.dim = dim;
    }

	public String getOpts() {
        return this.opts;
    }

	public void setOpts(String opts) {
        this.opts = opts;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
