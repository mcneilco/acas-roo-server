package com.labsynch.labseer.dto.configuration;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ClientUILabelsDTO {

    private String corpNameLabel;
    
    private String applicationNameForTitleBar;


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getCorpNameLabel() {
        return this.corpNameLabel;
    }

	public void setCorpNameLabel(String corpNameLabel) {
        this.corpNameLabel = corpNameLabel;
    }

	public String getApplicationNameForTitleBar() {
        return this.applicationNameForTitleBar;
    }

	public void setApplicationNameForTitleBar(String applicationNameForTitleBar) {
        this.applicationNameForTitleBar = applicationNameForTitleBar;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ClientUILabelsDTO fromJsonToClientUILabelsDTO(String json) {
        return new JSONDeserializer<ClientUILabelsDTO>()
        .use(null, ClientUILabelsDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ClientUILabelsDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ClientUILabelsDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ClientUILabelsDTO> fromJsonArrayToClientUILabelsDTO(String json) {
        return new JSONDeserializer<List<ClientUILabelsDTO>>()
        .use("values", ClientUILabelsDTO.class).deserialize(json);
    }
}
