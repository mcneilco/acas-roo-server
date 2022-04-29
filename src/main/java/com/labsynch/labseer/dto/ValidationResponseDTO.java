package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class ValidationResponseDTO {
		
    private String level;
    private int record;
    private String categoryCode;
    private String categoryDescription;
    private String message;
    
    public ValidationResponseDTO(String level, int record, String categoryCode, String categoryDescription, String message){
    	this.level = level;
    	this.record = record;
    	this.categoryCode = categoryCode;
    	this.categoryDescription = categoryDescription;
    	this.message = message;
    }

    public String toJson() {
        return new JSONSerializer()
        .include("level", "record", "categoryCode", "categoryDescription", "message").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getLevel() {
        return this.level;
    }

	public void setLevel(String level) {
        this.level = level;
    }

	public int getRecord() {
        return this.record;
    }

	public void setRecord(int record) {
        this.record = record;
    }

	public String getCategoryCode() {
        return this.categoryCode;
    }

	public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

	public String getCategoryDescription() {
        return this.categoryDescription;
    }

	public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

	public String getMessage() {
        return this.message;
    }

	public void setMessage(String message) {
        this.message = message;
    }

	public static ValidationResponseDTO fromJsonToValidationResponseDTO(String json) {
        return new JSONDeserializer<ValidationResponseDTO>()
        .use(null, ValidationResponseDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ValidationResponseDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ValidationResponseDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ValidationResponseDTO> fromJsonArrayToValidationRespoes(String json) {
        return new JSONDeserializer<List<ValidationResponseDTO>>()
        .use("values", ValidationResponseDTO.class).deserialize(json);
    }
}
