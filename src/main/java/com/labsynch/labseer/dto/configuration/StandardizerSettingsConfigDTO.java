package com.labsynch.labseer.dto.configuration;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
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

}