package com.labsynch.labseer.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson

public class LDStandardizerActionDTO {

	private String name;
	private String structure;
	private String label;
	private String method;
	private String[] salts;

    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(17, 37).
                append(name).
                append(structure).
                append(label).
                append(method).
                append(salts).
          toHashCode();
      }

    public boolean equals(Object obj) {
    	   if (obj == null) { return false; }
    	   if (obj == this) { return true; }
    	   if (obj.getClass() != getClass()) {
    	     return false;
    	   }
    	   LDStandardizerActionDTO rhs = (LDStandardizerActionDTO) obj;
    	   return new EqualsBuilder()
    	                 .appendSuper(super.equals(obj))
    	                 .append(name, rhs.name)
    	                 .append(structure, rhs.structure)
    	                 .append(label, rhs.label)
    	                 .append(method, rhs.method)
    	                 .append(salts, rhs.salts)
    	                 .isEquals();
    	  }
}