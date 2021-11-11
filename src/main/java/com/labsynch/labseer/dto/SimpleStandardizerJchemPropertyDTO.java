package com.labsynch.labseer.dto;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
//import chemaxon.jchem.version.VersionInfo;
@RooJavaBean
@RooToString
@RooJson
public class SimpleStandardizerJchemPropertyDTO {

    private String standardizerConfigFilePath;
//    private String jchemVersion = VersionInfo.getVersion();
    private String jchemVersion = "";

    public String getJchemStandardizerConfigFileContents () {
        File standardizerConfigFile = new File(standardizerConfigFilePath);
        String standardizerConfigFileContents = "";
        try {
            standardizerConfigFileContents = FileUtils.readFileToString(standardizerConfigFile);
        } catch (IOException e) {

        }
        return standardizerConfigFileContents;
    }
    public int hashCode() {
        // you pick a hard-coded, randomly chosen, non-zero, odd number
        // ideally different for each class
        return new HashCodeBuilder(17, 37).
                append(this.getJchemStandardizerConfigFileContents()).
                append(jchemVersion).
          toHashCode();
      }

    public boolean equals(Object obj) {

    	   if (obj == null) { return false; }
    	   if (obj == this) { return true; }
    	   if (obj.getClass() != getClass()) {
    	     return false;
    	   }
    	   SimpleStandardizerJchemPropertyDTO rhs = (SimpleStandardizerJchemPropertyDTO) obj;
    	   return new EqualsBuilder()
    	                 .appendSuper(super.equals(obj))
    	                 .append(this.getJchemStandardizerConfigFileContents(), rhs.getJchemStandardizerConfigFileContents())
    	                 .append(jchemVersion, rhs.jchemVersion)
    	                 .isEquals();
    	  }
}