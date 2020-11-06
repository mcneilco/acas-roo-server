package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BulkLoadSDFPropertyRequestDTO {

    private String fileName;
    
    private int numRecords;
    
    private String templateName;
    
    private String userName;
    
    private Collection<BulkLoadPropertyMappingDTO> mappings;
    
    public BulkLoadSDFPropertyRequestDTO(){
    	
    }
    
    public BulkLoadSDFPropertyRequestDTO(String fileName, int numRecords, String templateName, String userName, Collection<BulkLoadPropertyMappingDTO> mappings){
    	this.fileName = fileName;
    	this.numRecords = numRecords;
    	this.templateName = templateName;
    	this.userName = userName;
    	this.mappings = mappings;
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

	public static BulkLoadSDFPropertyRequestDTO fromJsonToBulkLoadSDFPropertyRequestDTO(String json) {
        return new JSONDeserializer<BulkLoadSDFPropertyRequestDTO>()
        .use(null, BulkLoadSDFPropertyRequestDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<BulkLoadSDFPropertyRequestDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<BulkLoadSDFPropertyRequestDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<BulkLoadSDFPropertyRequestDTO> fromJsonArrayToBulkLoadSDFProes(String json) {
        return new JSONDeserializer<List<BulkLoadSDFPropertyRequestDTO>>()
        .use("values", BulkLoadSDFPropertyRequestDTO.class).deserialize(json);
    }

	public String getFileName() {
        return this.fileName;
    }

	public void setFileName(String fileName) {
        this.fileName = fileName;
    }

	public int getNumRecords() {
        return this.numRecords;
    }

	public void setNumRecords(int numRecords) {
        this.numRecords = numRecords;
    }

	public String getTemplateName() {
        return this.templateName;
    }

	public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

	public String getUserName() {
        return this.userName;
    }

	public void setUserName(String userName) {
        this.userName = userName;
    }

	public Collection<BulkLoadPropertyMappingDTO> getMappings() {
        return this.mappings;
    }

	public void setMappings(Collection<BulkLoadPropertyMappingDTO> mappings) {
        this.mappings = mappings;
    }
}
