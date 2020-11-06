package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import com.labsynch.labseer.dto.ValidationResponseDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class BulkLoadRegisterSDFResponseDTO {

    private String summary;

    private Collection<ValidationResponseDTO> results;
    
    private Collection<String> reportFiles;
    
    public BulkLoadRegisterSDFResponseDTO(){
    	
    }
    
    public BulkLoadRegisterSDFResponseDTO(String summary, Collection<ValidationResponseDTO> results, Collection<String> reportFiles){
    	this.summary = summary;
    	this.results = results;
    	this.reportFiles = reportFiles;
    }
    
    public String toJson() {
        return new JSONSerializer()
        .include("reportFiles", "results").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    

	public String getSummary() {
        return this.summary;
    }

	public void setSummary(String summary) {
        this.summary = summary;
    }

	public Collection<ValidationResponseDTO> getResults() {
        return this.results;
    }

	public void setResults(Collection<ValidationResponseDTO> results) {
        this.results = results;
    }

	public Collection<String> getReportFiles() {
        return this.reportFiles;
    }

	public void setReportFiles(Collection<String> reportFiles) {
        this.reportFiles = reportFiles;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static BulkLoadRegisterSDFResponseDTO fromJsonToBulkLoadRegisterSDFResponseDTO(String json) {
        return new JSONDeserializer<BulkLoadRegisterSDFResponseDTO>()
        .use(null, BulkLoadRegisterSDFResponseDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<BulkLoadRegisterSDFResponseDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<BulkLoadRegisterSDFResponseDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<BulkLoadRegisterSDFResponseDTO> fromJsonArrayToBulkLoadRegisterSDFRespoes(String json) {
        return new JSONDeserializer<List<BulkLoadRegisterSDFResponseDTO>>()
        .use("values", BulkLoadRegisterSDFResponseDTO.class).deserialize(json);
    }
}
