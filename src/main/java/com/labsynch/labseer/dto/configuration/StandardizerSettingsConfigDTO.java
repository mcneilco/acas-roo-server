package com.labsynch.labseer.dto.configuration;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.dto.SimpleStandardizerJchemPropertyDTO;
import com.labsynch.labseer.dto.SimpleStandardizerLiveDesignPropertyDTO;

@RooJavaBean
@RooToString
@RooJson
public class StandardizerSettingsConfigDTO {

    private Boolean shouldStandardize;

    private String type;

    private SimpleStandardizerLiveDesignPropertyDTO livedesignSettings;

    private SimpleStandardizerJchemPropertyDTO jchemSettings;


    public int hashCode() {
    	Logger logger = LoggerFactory.getLogger(StandardizerSettingsConfigDTO.class);

	    // you pick a hard-coded, randomly chosen, non-zero, odd number
	    // ideally different for each class
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(17, 37).
	            append(shouldStandardize).
	            append(type);
		switch (type) {
			case "livedesign":
				hashCodeBuilder.append(livedesignSettings);
				break;
			case "jchem":
				hashCodeBuilder.append(jchemSettings);
				break;
		}
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
		switch (type) {
			case "livedesign":
				equalsBuilder.append(livedesignSettings, rhs.livedesignSettings);
				break;
			case "jchem":
				equalsBuilder.append(jchemSettings, rhs.jchemSettings);
				break;
		}
		return equalsBuilder.isEquals();
	}

}