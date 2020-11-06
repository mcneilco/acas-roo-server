package com.labsynch.labseer.dto.configuration;

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
public class MarvinDTO {

    private String marvinImplicitH;


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static MarvinDTO fromJsonToMarvinDTO(String json) {
        return new JSONDeserializer<MarvinDTO>()
        .use(null, MarvinDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<MarvinDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<MarvinDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<MarvinDTO> fromJsonArrayToMarvinDTO(String json) {
        return new JSONDeserializer<List<MarvinDTO>>()
        .use("values", MarvinDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getMarvinImplicitH() {
        return this.marvinImplicitH;
    }

	public void setMarvinImplicitH(String marvinImplicitH) {
        this.marvinImplicitH = marvinImplicitH;
    }
}
