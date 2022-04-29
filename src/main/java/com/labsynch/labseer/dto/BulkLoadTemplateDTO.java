package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class BulkLoadTemplateDTO {
    
    private String templateName;
    
    private String recordedBy;
    
    private boolean ignored;
    
    private Collection<BulkLoadPropertyMappingDTO> mappings;
    
    public BulkLoadTemplateDTO(){
    }


	public String getTemplateName() {
        return this.templateName;
    }

	public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public boolean isIgnored() {
        return this.ignored;
    }

	public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

	public Collection<BulkLoadPropertyMappingDTO> getMappings() {
        return this.mappings;
    }

	public void setMappings(Collection<BulkLoadPropertyMappingDTO> mappings) {
        this.mappings = mappings;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static BulkLoadTemplateDTO fromJsonToBulkLoadTemplateDTO(String json) {
        return new JSONDeserializer<BulkLoadTemplateDTO>()
        .use(null, BulkLoadTemplateDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<BulkLoadTemplateDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<BulkLoadTemplateDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<BulkLoadTemplateDTO> fromJsonArrayToBulkLoes(String json) {
        return new JSONDeserializer<List<BulkLoadTemplateDTO>>()
        .use("values", BulkLoadTemplateDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
