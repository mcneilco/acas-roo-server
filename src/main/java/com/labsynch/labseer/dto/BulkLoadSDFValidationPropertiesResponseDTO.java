package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.BulkLoadTemplate;
import com.labsynch.labseer.domain.CorpName;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.PhysicalState;
import com.labsynch.labseer.dto.configuration.MainConfigDTO;
import com.labsynch.labseer.service.ErrorMessage;
import com.labsynch.labseer.utils.Configuration;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class BulkLoadSDFValidationPropertiesResponseDTO {
		
    private Collection<String> chemists;
    
    private Collection<String> projects;
    
    public BulkLoadSDFValidationPropertiesResponseDTO(Collection<String> chemists, Collection<String> projects){
    	this.chemists = chemists;
    	this.projects = projects;
    }

    public String toJson() {
        return new JSONSerializer()
        .include("chemists","projects").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }

	public Collection<String> getChemists() {
        return this.chemists;
    }

	public void setChemists(Collection<String> chemists) {
        this.chemists = chemists;
    }

	public Collection<String> getProjects() {
        return this.projects;
    }

	public void setProjects(Collection<String> projects) {
        this.projects = projects;
    }

	public static BulkLoadSDFValidationPropertiesResponseDTO fromJsonToBulkLoadSDFValidationPropertiesResponseDTO(String json) {
        return new JSONDeserializer<BulkLoadSDFValidationPropertiesResponseDTO>()
        .use(null, BulkLoadSDFValidationPropertiesResponseDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<BulkLoadSDFValidationPropertiesResponseDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<BulkLoadSDFValidationPropertiesResponseDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<BulkLoadSDFValidationPropertiesResponseDTO> fromJsonArrayToBulkLoadSDFValidationPropertiesRespoes(String json) {
        return new JSONDeserializer<List<BulkLoadSDFValidationPropertiesResponseDTO>>()
        .use("values", BulkLoadSDFValidationPropertiesResponseDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
