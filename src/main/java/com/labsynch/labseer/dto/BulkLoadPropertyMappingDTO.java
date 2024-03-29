package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class BulkLoadPropertyMappingDTO {

    private static final Logger logger = LoggerFactory.getLogger(BulkLoadPropertyMappingDTO.class);

    private String dbProperty;

    private String sdfProperty;

    private boolean required;

    private Collection<String> invalidValues;

    private Collection<String> validValues;

    private String defaultVal;

    private boolean ignored;

    public BulkLoadPropertyMappingDTO() {

    }

    public BulkLoadPropertyMappingDTO(String dbProperty, String sdfProperty, boolean required, String defaultVal,
            Collection<String> invalidValues, Collection<String> validValues, boolean ignored) {
        this.dbProperty = dbProperty;
        this.sdfProperty = sdfProperty;
        this.required = required;
        this.defaultVal = defaultVal;
        this.invalidValues = invalidValues;
        this.validValues = validValues;
        this.ignored = ignored;
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class", "class")
                .include("dbProperty", "sdfProperty", "required", "defaultVal", "ignored")
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public static BulkLoadPropertyMappingDTO findMappingByDbPropertyEquals(
            Collection<BulkLoadPropertyMappingDTO> mappings, String dbProperty) {
        for (BulkLoadPropertyMappingDTO mapping : mappings) {
            if (mapping.getDbProperty().equals(dbProperty))
                return mapping;
        }
        return null;
    }

    public static Collection<BulkLoadPropertyMappingDTO> findMappingsByDbPropertyEquals(
            Collection<BulkLoadPropertyMappingDTO> mappings, String dbProperty) {
        Collection<BulkLoadPropertyMappingDTO> foundMappings = new HashSet<BulkLoadPropertyMappingDTO>();
        for (BulkLoadPropertyMappingDTO mapping : mappings) {
            if (mapping.getDbProperty().equals(dbProperty))
                foundMappings.add(mapping);
        }
        return foundMappings;
    }

    public static BulkLoadPropertyMappingDTO findMappingBySdfPropertyEquals(
            Collection<BulkLoadPropertyMappingDTO> mappings, String sdfProperty) {
        for (BulkLoadPropertyMappingDTO mapping : mappings) {
            logger.debug(mapping.toJson());
            logger.debug(sdfProperty);
            if (mapping.getSdfProperty() != null && sdfProperty != null && mapping.getSdfProperty().equals(sdfProperty))
                return mapping;
        }
        return null;
    }

    public static BulkLoadPropertyMappingDTO fromJsonToBulkLoadPropertyMappingDTO(String json) {
        return new JSONDeserializer<BulkLoadPropertyMappingDTO>()
                .use(null, BulkLoadPropertyMappingDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<BulkLoadPropertyMappingDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<BulkLoadPropertyMappingDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<BulkLoadPropertyMappingDTO> fromJsonArrayToBulkLoadProes(String json) {
        return new JSONDeserializer<List<BulkLoadPropertyMappingDTO>>()
                .use("values", BulkLoadPropertyMappingDTO.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getDbProperty() {
        return this.dbProperty;
    }

    public void setDbProperty(String dbProperty) {
        this.dbProperty = dbProperty;
    }

    public String getSdfProperty() {
        return this.sdfProperty;
    }

    public void setSdfProperty(String sdfProperty) {
        this.sdfProperty = sdfProperty;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Collection<String> getInvalidValues() {
        return this.invalidValues;
    }

    public void setInvalidValues(Collection<String> invalidValues) {
        this.invalidValues = invalidValues;
    }

    public Collection<String> getValidValues() {
        return this.validValues;
    }

    public void setValidValues(Collection<String> validValues) {
        this.validValues = validValues;
    }

    public String getDefaultVal() {
        return this.defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public boolean isIgnored() {
        return this.ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }
}
