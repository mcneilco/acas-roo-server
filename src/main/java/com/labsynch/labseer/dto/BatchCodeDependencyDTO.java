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

public class BatchCodeDependencyDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(BatchCodeDependencyDTO.class);
	
	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	private Collection<String> batchCodes;
	
	private Boolean linkedDataExists;
	
	private Collection<CodeTableDTO> linkedExperiments;
	
	private Collection<ErrorMessage> errors;
	
	public BatchCodeDependencyDTO() {
	}
    
    public BatchCodeDependencyDTO(Collection<String> batchCodes) {
		this.setBatchCodes(batchCodes);
	}
    
    public String toJson() {
        return new JSONSerializer()
        .include("batchCodes", "linkedExperiments", "errors")
        .exclude("*.class").serialize(this);
    }
    

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Collection<String> getBatchCodes() {
        return this.batchCodes;
    }

	public void setBatchCodes(Collection<String> batchCodes) {
        this.batchCodes = batchCodes;
    }

	public Boolean getLinkedDataExists() {
        return this.linkedDataExists;
    }

	public void setLinkedDataExists(Boolean linkedDataExists) {
        this.linkedDataExists = linkedDataExists;
    }

	public Collection<CodeTableDTO> getLinkedExperiments() {
        return this.linkedExperiments;
    }

	public void setLinkedExperiments(Collection<CodeTableDTO> linkedExperiments) {
        this.linkedExperiments = linkedExperiments;
    }

	public Collection<ErrorMessage> getErrors() {
        return this.errors;
    }

	public void setErrors(Collection<ErrorMessage> errors) {
        this.errors = errors;
    }

	public static BatchCodeDependencyDTO fromJsonToBatchCodeDependencyDTO(String json) {
        return new JSONDeserializer<BatchCodeDependencyDTO>()
        .use(null, BatchCodeDependencyDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<BatchCodeDependencyDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<BatchCodeDependencyDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<BatchCodeDependencyDTO> fromJsonArrayToBatchCoes(String json) {
        return new JSONDeserializer<List<BatchCodeDependencyDTO>>()
        .use("values", BatchCodeDependencyDTO.class).deserialize(json);
    }
}
