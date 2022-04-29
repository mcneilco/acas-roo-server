package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class PurgeFileDependencyCheckResponseDTO {

    private String summary;
    
    private boolean canPurge;
    
    public PurgeFileDependencyCheckResponseDTO(){
    	
    }
    
    public PurgeFileDependencyCheckResponseDTO(String summary, boolean canPurge){
    	this.summary = summary;
    	this.canPurge = canPurge;
    }
    
    public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    

	public static PurgeFileDependencyCheckResponseDTO fromJsonToPurgeFileDependencyCheckResponseDTO(String json) {
        return new JSONDeserializer<PurgeFileDependencyCheckResponseDTO>()
        .use(null, PurgeFileDependencyCheckResponseDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<PurgeFileDependencyCheckResponseDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<PurgeFileDependencyCheckResponseDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<PurgeFileDependencyCheckResponseDTO> fromJsonArrayToPurgeFileDependencyCheckRespoes(String json) {
        return new JSONDeserializer<List<PurgeFileDependencyCheckResponseDTO>>()
        .use("values", PurgeFileDependencyCheckResponseDTO.class).deserialize(json);
    }

	public String getSummary() {
        return this.summary;
    }

	public void setSummary(String summary) {
        this.summary = summary;
    }

	public boolean isCanPurge() {
        return this.canPurge;
    }

	public void setCanPurge(boolean canPurge) {
        this.canPurge = canPurge;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
