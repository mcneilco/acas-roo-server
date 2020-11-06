package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ReparentLotDTO {
	
	private String parentCorpName;
	
	private String lotCorpName;

	private String modifiedBy;
	

	public String getParentCorpName() {
        return this.parentCorpName;
    }

	public void setParentCorpName(String parentCorpName) {
        this.parentCorpName = parentCorpName;
    }

	public String getLotCorpName() {
        return this.lotCorpName;
    }

	public void setLotCorpName(String lotCorpName) {
        this.lotCorpName = lotCorpName;
    }

	public String getModifiedBy() {
        return this.modifiedBy;
    }

	public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
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

	public static ReparentLotDTO fromJsonToReparentLotDTO(String json) {
        return new JSONDeserializer<ReparentLotDTO>()
        .use(null, ReparentLotDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ReparentLotDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ReparentLotDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ReparentLotDTO> fromJsonArrayToReparentLoes(String json) {
        return new JSONDeserializer<List<ReparentLotDTO>>()
        .use("values", ReparentLotDTO.class).deserialize(json);
    }
}
