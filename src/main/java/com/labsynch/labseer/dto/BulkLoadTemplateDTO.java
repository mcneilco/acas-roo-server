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

public class BulkLoadTemplateDTO {
    
    private String templateName;
    
    private String recordedBy;
    
    private boolean ignored;
    
    private Collection<BulkLoadPropertyMappingDTO> mappings;
    
    public BulkLoadTemplateDTO(){
    }


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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
}
