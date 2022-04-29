package com.labsynch.labseer.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import javax.persistence.Lob;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class RegSearch {

	
    private String corpName;

    @Lob
    //@Size(max = 10485760)
    private String molStructure;


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static RegSearch fromJsonToRegSearch(String json) {
        return new JSONDeserializer<RegSearch>()
        .use(null, RegSearch.class).deserialize(json);
    }

	public static String toJsonArray(Collection<RegSearch> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<RegSearch> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<RegSearch> fromJsonArrayToRegSearches(String json) {
        return new JSONDeserializer<List<RegSearch>>()
        .use("values", RegSearch.class).deserialize(json);
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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
