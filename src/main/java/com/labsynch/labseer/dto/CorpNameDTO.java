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
public class CorpNameDTO{

	private int corpNumber;
	
	private String corpName;


	public int getCorpNumber() {
        return this.corpNumber;
    }

	public void setCorpNumber(int corpNumber) {
        this.corpNumber = corpNumber;
    }

	public String getCorpName() {
        return this.corpName;
    }

	public void setCorpName(String corpName) {
        this.corpName = corpName;
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

	public static CorpNameDTO fromJsonToCorpNameDTO(String json) {
        return new JSONDeserializer<CorpNameDTO>()
        .use(null, CorpNameDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<CorpNameDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<CorpNameDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<CorpNameDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<CorpNameDTO>>()
        .use("values", CorpNameDTO.class).deserialize(json);
    }
}
