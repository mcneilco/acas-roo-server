package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class LicenseDTO {

	public LicenseDTO() {
	}


	private String licensee;

	private String validDateString;
	
	private boolean valid;
	
	private String edition;

	private String features;
	
	private Integer numberOfUsers;
	
	private Integer numberOfDaysLeft;

	


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getLicensee() {
        return this.licensee;
    }

	public void setLicensee(String licensee) {
        this.licensee = licensee;
    }

	public String getValidDateString() {
        return this.validDateString;
    }

	public void setValidDateString(String validDateString) {
        this.validDateString = validDateString;
    }

	public boolean isValid() {
        return this.valid;
    }

	public void setValid(boolean valid) {
        this.valid = valid;
    }

	public String getEdition() {
        return this.edition;
    }

	public void setEdition(String edition) {
        this.edition = edition;
    }

	public String getFeatures() {
        return this.features;
    }

	public void setFeatures(String features) {
        this.features = features;
    }

	public Integer getNumberOfUsers() {
        return this.numberOfUsers;
    }

	public void setNumberOfUsers(Integer numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

	public Integer getNumberOfDaysLeft() {
        return this.numberOfDaysLeft;
    }

	public void setNumberOfDaysLeft(Integer numberOfDaysLeft) {
        this.numberOfDaysLeft = numberOfDaysLeft;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static LicenseDTO fromJsonToLicenseDTO(String json) {
        return new JSONDeserializer<LicenseDTO>()
        .use(null, LicenseDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<LicenseDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<LicenseDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<LicenseDTO> fromJsonArrayToLicenseDTO(String json) {
        return new JSONDeserializer<List<LicenseDTO>>()
        .use("values", LicenseDTO.class).deserialize(json);
    }
}


