package com.labsynch.labseer.dto.configuration;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.dto.SimpleBulkLoadPropertyDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class BulkLoadSettingsConfigDTO {

	private Boolean useProjectRoles;
	
	private Collection<SimpleBulkLoadPropertyDTO> dbProperties;
    

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static BulkLoadSettingsConfigDTO fromJsonToBulkLoadSettingsConfigDTO(String json) {
        return new JSONDeserializer<BulkLoadSettingsConfigDTO>()
        .use(null, BulkLoadSettingsConfigDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<BulkLoadSettingsConfigDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<BulkLoadSettingsConfigDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<BulkLoadSettingsConfigDTO> fromJsonArrayToBulkLoadSettingsCoes(String json) {
        return new JSONDeserializer<List<BulkLoadSettingsConfigDTO>>()
        .use("values", BulkLoadSettingsConfigDTO.class).deserialize(json);
    }

	public Boolean getUseProjectRoles() {
        return this.useProjectRoles;
    }

	public void setUseProjectRoles(Boolean useProjectRoles) {
        this.useProjectRoles = useProjectRoles;
    }

	public Collection<SimpleBulkLoadPropertyDTO> getDbProperties() {
        return this.dbProperties;
    }

	public void setDbProperties(Collection<SimpleBulkLoadPropertyDTO> dbProperties) {
        this.dbProperties = dbProperties;
    }
}
