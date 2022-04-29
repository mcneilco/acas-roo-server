package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.labsynch.labseer.service.ErrorMessage;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class ParentValidationDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(ParentValidationDTO.class);

	private Collection<CodeTableDTO> affectedLots;
	
	private boolean parentUnique;
	
	private Collection<ParentDTO> dupeParents;
	
	private Collection<ErrorMessage> errors; 
		
	public ParentValidationDTO() {
		this.errors = new ArrayList<ErrorMessage>();
	}
    
    public String toJson() {
        return new JSONSerializer()
        .include("affectedLots", "dupeParents","errors")
        .exclude("*.class").serialize(this);
    }
    

	public Collection<CodeTableDTO> getAffectedLots() {
        return this.affectedLots;
    }

	public void setAffectedLots(Collection<CodeTableDTO> affectedLots) {
        this.affectedLots = affectedLots;
    }

	public boolean isParentUnique() {
        return this.parentUnique;
    }

	public void setParentUnique(boolean parentUnique) {
        this.parentUnique = parentUnique;
    }

	public Collection<ParentDTO> getDupeParents() {
        return this.dupeParents;
    }

	public void setDupeParents(Collection<ParentDTO> dupeParents) {
        this.dupeParents = dupeParents;
    }

	public Collection<ErrorMessage> getErrors() {
        return this.errors;
    }

	public void setErrors(Collection<ErrorMessage> errors) {
        this.errors = errors;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static ParentValidationDTO fromJsonToParentValidationDTO(String json) {
        return new JSONDeserializer<ParentValidationDTO>()
        .use(null, ParentValidationDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ParentValidationDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ParentValidationDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ParentValidationDTO> fromJsonArrayToParentValidatioes(String json) {
        return new JSONDeserializer<List<ParentValidationDTO>>()
        .use("values", ParentValidationDTO.class).deserialize(json);
    }
}
