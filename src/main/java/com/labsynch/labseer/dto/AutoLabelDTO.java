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
public class AutoLabelDTO {
	
    private String autoLabel;

    private Long labelNumber;
    
    public AutoLabelDTO () {
    	
    }
    
    public AutoLabelDTO (String autoLabel, Long labelNumber){
    		this.autoLabel = autoLabel;
    		this.labelNumber = labelNumber;
    }


	public String getAutoLabel() {
        return this.autoLabel;
    }

	public void setAutoLabel(String autoLabel) {
        this.autoLabel = autoLabel;
    }

	public Long getLabelNumber() {
        return this.labelNumber;
    }

	public void setLabelNumber(Long labelNumber) {
        this.labelNumber = labelNumber;
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

	public static AutoLabelDTO fromJsonToAutoLabelDTO(String json) {
        return new JSONDeserializer<AutoLabelDTO>()
        .use(null, AutoLabelDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AutoLabelDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<AutoLabelDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<AutoLabelDTO> fromJsonArrayToAutoes(String json) {
        return new JSONDeserializer<List<AutoLabelDTO>>()
        .use("values", AutoLabelDTO.class).deserialize(json);
    }
}
