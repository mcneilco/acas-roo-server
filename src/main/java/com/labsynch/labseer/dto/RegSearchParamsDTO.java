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
public class RegSearchParamsDTO {

    private String corpName;

    private String molStructure;
    
	private String parentCorpName;
	
	private String saltFormCorpName;
	
	private String lotCorpName;
    


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getCorpName() {
        return this.corpName;
    }

	public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

	public String getMolStructure() {
        return this.molStructure;
    }

	public void setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }

	public String getParentCorpName() {
        return this.parentCorpName;
    }

	public void setParentCorpName(String parentCorpName) {
        this.parentCorpName = parentCorpName;
    }

	public String getSaltFormCorpName() {
        return this.saltFormCorpName;
    }

	public void setSaltFormCorpName(String saltFormCorpName) {
        this.saltFormCorpName = saltFormCorpName;
    }

	public String getLotCorpName() {
        return this.lotCorpName;
    }

	public void setLotCorpName(String lotCorpName) {
        this.lotCorpName = lotCorpName;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static RegSearchParamsDTO fromJsonToRegSearchParamsDTO(String json) {
        return new JSONDeserializer<RegSearchParamsDTO>()
        .use(null, RegSearchParamsDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<RegSearchParamsDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<RegSearchParamsDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<RegSearchParamsDTO> fromJsonArrayToRegSearchParamsDTO(String json) {
        return new JSONDeserializer<List<RegSearchParamsDTO>>()
        .use("values", RegSearchParamsDTO.class).deserialize(json);
    }
}
