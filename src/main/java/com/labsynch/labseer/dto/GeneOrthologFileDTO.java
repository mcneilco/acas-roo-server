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
public class GeneOrthologFileDTO {
	
	private String versionName;
	private String testFileName;
    private String orthologType;
    private Long curationLevel;
    private String description;
    private String curator;
    private String recordedBy;
    

	public String getVersionName() {
        return this.versionName;
    }

	public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

	public String getTestFileName() {
        return this.testFileName;
    }

	public void setTestFileName(String testFileName) {
        this.testFileName = testFileName;
    }

	public String getOrthologType() {
        return this.orthologType;
    }

	public void setOrthologType(String orthologType) {
        this.orthologType = orthologType;
    }

	public Long getCurationLevel() {
        return this.curationLevel;
    }

	public void setCurationLevel(Long curationLevel) {
        this.curationLevel = curationLevel;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public String getCurator() {
        return this.curator;
    }

	public void setCurator(String curator) {
        this.curator = curator;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static GeneOrthologFileDTO fromJsonToGeneOrthologFileDTO(String json) {
        return new JSONDeserializer<GeneOrthologFileDTO>()
        .use(null, GeneOrthologFileDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<GeneOrthologFileDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<GeneOrthologFileDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<GeneOrthologFileDTO> fromJsonArrayToGeneOrtholoes(String json) {
        return new JSONDeserializer<List<GeneOrthologFileDTO>>()
        .use("values", GeneOrthologFileDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
