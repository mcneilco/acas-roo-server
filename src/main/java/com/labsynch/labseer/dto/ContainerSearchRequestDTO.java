package com.labsynch.labseer.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

@RooJavaBean
@RooToString
@RooJson
public class ContainerSearchRequestDTO {

	private String barcode;
	
	private String definition;
	
	private String status;
	
	private String description;
	
	private String createdUser;
	
	private String type;
	
//	private String requestId;
	
	private String lsType;
	
	private String lsKind;
	
	
	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
	

	public static ContainerSearchRequestDTO fromJsonToContainerSearchRequestDTO(String json) {
        return new JSONDeserializer<ContainerSearchRequestDTO>()
        .use(null, ContainerSearchRequestDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ContainerSearchRequestDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ContainerSearchRequestDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ContainerSearchRequestDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<ContainerSearchRequestDTO>>()
        .use("values", ContainerSearchRequestDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getBarcode() {
        return this.barcode;
    }

	public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

	public String getDefinition() {
        return this.definition;
    }

	public void setDefinition(String definition) {
        this.definition = definition;
    }

	public String getStatus() {
        return this.status;
    }

	public void setStatus(String status) {
        this.status = status;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public String getCreatedUser() {
        return this.createdUser;
    }

	public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

	public String getType() {
        return this.type;
    }

	public void setType(String type) {
        this.type = type;
    }

	public String getLsType() {
        return this.lsType;
    }

	public void setLsType(String lsType) {
        this.lsType = lsType;
    }

	public String getLsKind() {
        return this.lsKind;
    }

	public void setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }
}
