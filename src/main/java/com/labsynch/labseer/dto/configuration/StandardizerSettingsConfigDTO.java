package com.labsynch.labseer.dto.configuration;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class StandardizerSettingsConfigDTO {

    private Boolean shouldStandardize;

    private String type;

    private String settings;

    public int hashCode() {
	    // you pick a hard-coded, randomly chosen, non-zero, odd number
	    // ideally different for each class
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(17, 37).
	            append(shouldStandardize).
	            append(type);
		hashCodeBuilder.append(settings);

	    return hashCodeBuilder.toHashCode();
    }

	public boolean equals(Object obj) {
	   if (obj == null) { return false; }
	   if (obj == this) { return true; }
	   if (obj.getClass() != getClass()) {
	     return false;
	   }
	   StandardizerSettingsConfigDTO rhs = (StandardizerSettingsConfigDTO) obj;
	   EqualsBuilder equalsBuilder = new EqualsBuilder()
	             .appendSuper(super.equals(obj))
	             .append(shouldStandardize, rhs.shouldStandardize)
	             .append(type, rhs.type);
		equalsBuilder.append(settings, rhs.settings);

		return equalsBuilder.isEquals();
	}


	public Boolean getShouldStandardize() {
        return this.shouldStandardize;
    }

	public void setShouldStandardize(Boolean shouldStandardize) {
        this.shouldStandardize = shouldStandardize;
    }

	public String getType() {
        return this.type;
    }

	public void setType(String type) {
        this.type = type;
    }

	public String getSettings() {
        return this.settings;
    }

	public void setSettings(String settings) {
        this.settings = settings;
    }

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

	public static StandardizerSettingsConfigDTO fromJsonToStandardizerSettingsConfigDTO(String json) {
        return new JSONDeserializer<StandardizerSettingsConfigDTO>()
        .use(null, StandardizerSettingsConfigDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<StandardizerSettingsConfigDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<StandardizerSettingsConfigDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<StandardizerSettingsConfigDTO> fromJsonArrayToStandardizerSettingsCoes(String json) {
        return new JSONDeserializer<List<StandardizerSettingsConfigDTO>>()
        .use("values", StandardizerSettingsConfigDTO.class).deserialize(json);
    }
}