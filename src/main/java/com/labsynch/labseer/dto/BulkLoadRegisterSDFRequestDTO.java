package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class BulkLoadRegisterSDFRequestDTO {

    private String filePath;
            
    private String userName;
    
    private Date fileDate;
    
    private LabelPrefixDTO labelPrefix;

    private Boolean validate;

    private Collection<BulkLoadPropertyMappingDTO> mappings;
    
    public BulkLoadRegisterSDFRequestDTO(){
    	
    }
    
    public BulkLoadRegisterSDFRequestDTO(String filePath, String userName, Date fileDate, Boolean validate, Collection<BulkLoadPropertyMappingDTO> mappings){
    	this.filePath = filePath;
    	this.userName = userName;
    	this.fileDate = fileDate;
    	this.validate = validate;
    	this.mappings = mappings;
    }
    
    public String toJson() {
        return new JSONSerializer()
        .include("filePath", "fileDate", "userName",  "validate", "mappings").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    

	public String getFilePath() {
        return this.filePath;
    }

	public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

	public String getUserName() {
        return this.userName;
    }

	public void setUserName(String userName) {
        this.userName = userName;
    }

	public Date getFileDate() {
        return this.fileDate;
    }

	public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

	public LabelPrefixDTO getLabelPrefix() {
        return this.labelPrefix;
    }

	public void setLabelPrefix(LabelPrefixDTO labelPrefix) {
        this.labelPrefix = labelPrefix;
    }

	public Boolean getValidate() {
        return this.validate;
    }

	public void setValidate(Boolean validate) {
        this.validate = validate;
    }

	public Collection<BulkLoadPropertyMappingDTO> getMappings() {
        return this.mappings;
    }

	public void setMappings(Collection<BulkLoadPropertyMappingDTO> mappings) {
        this.mappings = mappings;
    }

	public static BulkLoadRegisterSDFRequestDTO fromJsonToBulkLoadRegisterSDFRequestDTO(String json) {
        return new JSONDeserializer<BulkLoadRegisterSDFRequestDTO>()
        .use(null, BulkLoadRegisterSDFRequestDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<BulkLoadRegisterSDFRequestDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<BulkLoadRegisterSDFRequestDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<BulkLoadRegisterSDFRequestDTO> fromJsonArrayToBulkLoes(String json) {
        return new JSONDeserializer<List<BulkLoadRegisterSDFRequestDTO>>()
        .use("values", BulkLoadRegisterSDFRequestDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
