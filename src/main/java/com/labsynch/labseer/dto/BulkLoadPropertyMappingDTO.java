package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class BulkLoadPropertyMappingDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(BulkLoadPropertyMappingDTO.class);

    private String dbProperty;
    
    private String sdfProperty;
    
    private boolean required;

	private Collection<String> invalidValues;

	private String defaultVal;
	
    private boolean ignored;
    
    public BulkLoadPropertyMappingDTO(){
    	
    }
    
    public BulkLoadPropertyMappingDTO(String dbProperty, String sdfProperty, boolean required, String defaultVal, Collection<String> invalidValues, boolean ignored){
    	this.dbProperty = dbProperty;
    	this.sdfProperty = sdfProperty;
    	this.required = required;
    	this.defaultVal = defaultVal;
    	this.invalidValues = invalidValues;
    	this.ignored = ignored;
    }
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class", "class").include("dbProperty","sdfProperty","required","defaultVal","ignored").transform(new ExcludeNulls(), void.class).serialize(this);
    }
    
    public static BulkLoadPropertyMappingDTO findMappingByDbPropertyEquals(Collection<BulkLoadPropertyMappingDTO> mappings, String dbProperty){
    	for (BulkLoadPropertyMappingDTO mapping: mappings){
    		if (mapping.getDbProperty().equals(dbProperty)) return mapping;
    	}
    	return null;
    }
    
    public static Collection<BulkLoadPropertyMappingDTO> findMappingsByDbPropertyEquals(Collection<BulkLoadPropertyMappingDTO> mappings, String dbProperty){
    	Collection<BulkLoadPropertyMappingDTO> foundMappings = new HashSet<BulkLoadPropertyMappingDTO>();
    	for (BulkLoadPropertyMappingDTO mapping: mappings){
    		if (mapping.getDbProperty().equals(dbProperty)) foundMappings.add(mapping);
    	}
    	return foundMappings;
    }
    
    public static BulkLoadPropertyMappingDTO findMappingBySdfPropertyEquals(Collection<BulkLoadPropertyMappingDTO> mappings, String sdfProperty){
    	for (BulkLoadPropertyMappingDTO mapping: mappings){
    		logger.debug(mapping.toJson());
    		logger.debug(sdfProperty);
    		if (mapping.getSdfProperty() != null && sdfProperty != null && mapping.getSdfProperty().equals(sdfProperty)) return mapping;
    	}
    	return null;
    }
    
}
