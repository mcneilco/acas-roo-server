package com.labsynch.labseer.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

public class PurgeFileResponseDTO {

    private String summary;
    
    private boolean success;
    
    private String fileName;
    
    public PurgeFileResponseDTO(){
    	
    }
    
    public PurgeFileResponseDTO(String summary, boolean success, String fileName){
    	this.summary = summary;
    	this.success = success;
    	this.fileName = fileName;
    }
    
    public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    

	public static PurgeFileResponseDTO fromJsonToPurgeFileResponseDTO(String json) {
        return new JSONDeserializer<PurgeFileResponseDTO>()
        .use(null, PurgeFileResponseDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<PurgeFileResponseDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<PurgeFileResponseDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<PurgeFileResponseDTO> fromJsonArrayToPurgeFileRespoes(String json) {
        return new JSONDeserializer<List<PurgeFileResponseDTO>>()
        .use("values", PurgeFileResponseDTO.class).deserialize(json);
    }

	public String getSummary() {
        return this.summary;
    }

	public void setSummary(String summary) {
        this.summary = summary;
    }

	public boolean isSuccess() {
        return this.success;
    }

	public void setSuccess(boolean success) {
        this.success = success;
    }

	public String getFileName() {
        return this.fileName;
    }

	public void setFileName(String fileName) {
        this.fileName = fileName;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
