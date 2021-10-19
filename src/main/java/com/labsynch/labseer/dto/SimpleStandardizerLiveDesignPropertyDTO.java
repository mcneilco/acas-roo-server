package com.labsynch.labseer.dto;

import java.util.Collection;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;



@RooJavaBean
@RooToString
@RooJson
public class SimpleStandardizerLiveDesignPropertyDTO {

	private String url;
	private String version;
    private String token;
    private int timeout;
    private String outputFormat;
    private Collection<LDStandardizerActionDTO>  actions;

    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(17, 37).
//                append(url).
//                append(token).
//                append(timeout).
//                append(outputFormat).
                append(actions).
                append(version).
          toHashCode();
      }

    public boolean equals(Object obj) {
    	   if (obj == null) { return false; }
    	   if (obj == this) { return true; }
    	   if (obj.getClass() != getClass()) {
    	     return false;
    	   }
    	   SimpleStandardizerLiveDesignPropertyDTO rhs = (SimpleStandardizerLiveDesignPropertyDTO) obj;
    	   return new EqualsBuilder()
    	                 .appendSuper(super.equals(obj))
//    	                 .append(url, rhs.url)
//    	                 .append(token, rhs.token)
//    	                 .append(timeout, rhs.timeout)
//    	                 .append(outputFormat, rhs.outputFormat)
    	                 .append(actions, rhs.actions)
    	                 .append(version, rhs.version)
    	                 .isEquals();
    	  }
}