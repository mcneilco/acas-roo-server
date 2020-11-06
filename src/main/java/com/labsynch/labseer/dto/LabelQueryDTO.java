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
public class LabelQueryDTO {

	String labelText;
	String labelType;
	String labelKind;
	String operator;
	
	public LabelQueryDTO(){
		
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

	public static LabelQueryDTO fromJsonToLabelQueryDTO(String json) {
        return new JSONDeserializer<LabelQueryDTO>()
        .use(null, LabelQueryDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<LabelQueryDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<LabelQueryDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<LabelQueryDTO> fromJsonArrayToLabelQueryDTO(String json) {
        return new JSONDeserializer<List<LabelQueryDTO>>()
        .use("values", LabelQueryDTO.class).deserialize(json);
    }

	public String getLabelText() {
        return this.labelText;
    }

	public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

	public String getLabelType() {
        return this.labelType;
    }

	public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

	public String getLabelKind() {
        return this.labelKind;
    }

	public void setLabelKind(String labelKind) {
        this.labelKind = labelKind;
    }

	public String getOperator() {
        return this.operator;
    }

	public void setOperator(String operator) {
        this.operator = operator;
    }
}
