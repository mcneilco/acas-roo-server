package com.labsynch.labseer.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

@RooJavaBean
@RooToString
@RooJson
public class ExportResultDTO {

    private String summary;
    
    private String level;
    
    private String message;
    
    private String reportFilePath;
    
    public ExportResultDTO(){
    	
    }
    
    public ExportResultDTO(String summary, String reportFilePath){
    	this.summary = summary;
    	this.reportFilePath = reportFilePath;
    }
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    

	public static ExportResultDTO fromJsonToExportResultDTO(String json) {
        return new JSONDeserializer<ExportResultDTO>()
        .use(null, ExportResultDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ExportResultDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ExportResultDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ExportResultDTO> fromJsonArrayToExpoes(String json) {
        return new JSONDeserializer<List<ExportResultDTO>>()
        .use("values", ExportResultDTO.class).deserialize(json);
    }

	public String getSummary() {
        return this.summary;
    }

	public void setSummary(String summary) {
        this.summary = summary;
    }

	public String getLevel() {
        return this.level;
    }

	public void setLevel(String level) {
        this.level = level;
    }

	public String getMessage() {
        return this.message;
    }

	public void setMessage(String message) {
        this.message = message;
    }

	public String getReportFilePath() {
        return this.reportFilePath;
    }

	public void setReportFilePath(String reportFilePath) {
        this.reportFilePath = reportFilePath;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
