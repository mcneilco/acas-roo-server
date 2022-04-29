package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class ContainerLocationDTO {
	
	private String locationCodeName;
	
	private String containerCodeName;
	
	private String containerBarcode;
	
	private String level;
	
	private String message;
	
	private String modifiedBy;
	
	private Date modifiedDate;
	
	public ContainerLocationDTO(){
	}
	
	public ContainerLocationDTO(String locationCodeName, String containerCodeName, String containerBarcode){
		this.locationCodeName = locationCodeName;
		this.containerCodeName = containerCodeName;
		this.containerBarcode = containerBarcode;
	}
	
	public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<ContainerLocationDTO> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	


	public static ContainerLocationDTO fromJsonToContainerLocationDTO(String json) {
        return new JSONDeserializer<ContainerLocationDTO>()
        .use(null, ContainerLocationDTO.class).deserialize(json);
    }

	public static Collection<ContainerLocationDTO> fromJsonArrayToContainerLocatioes(String json) {
        return new JSONDeserializer<List<ContainerLocationDTO>>()
        .use("values", ContainerLocationDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getLocationCodeName() {
        return this.locationCodeName;
    }

	public void setLocationCodeName(String locationCodeName) {
        this.locationCodeName = locationCodeName;
    }

	public String getContainerCodeName() {
        return this.containerCodeName;
    }

	public void setContainerCodeName(String containerCodeName) {
        this.containerCodeName = containerCodeName;
    }

	public String getContainerBarcode() {
        return this.containerBarcode;
    }

	public void setContainerBarcode(String containerBarcode) {
        this.containerBarcode = containerBarcode;
    }

	public String getLevel() {
        return this.level;
    }

	public void setLevel(String level) {
        this.level = level;
    }

	public String getMessage() {
        return this.message;
    }

	public void setMessage(String message) {
        this.message = message;
    }

	public String getModifiedBy() {
        return this.modifiedBy;
    }

	public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

	public Date getModifiedDate() {
        return this.modifiedDate;
    }

	public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}


