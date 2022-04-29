package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class ContainerRequestDTO {
	
	private String containerCodeName;
	private BigDecimal amount;
	private String amountUnits;
	private String modifiedBy;
	private Date modifiedDate;
	
	public ContainerRequestDTO(){
	}
	
	public ContainerRequestDTO(String containerCodeName, String modifiedBy, Date modifiedDate){
		this.containerCodeName = containerCodeName;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
	}
	


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ContainerRequestDTO fromJsonToContainerRequestDTO(String json) {
        return new JSONDeserializer<ContainerRequestDTO>()
        .use(null, ContainerRequestDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ContainerRequestDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ContainerRequestDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ContainerRequestDTO> fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<ContainerRequestDTO>>()
        .use("values", ContainerRequestDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getContainerCodeName() {
        return this.containerCodeName;
    }

	public void setContainerCodeName(String containerCodeName) {
        this.containerCodeName = containerCodeName;
    }

	public BigDecimal getAmount() {
        return this.amount;
    }

	public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

	public String getAmountUnits() {
        return this.amountUnits;
    }

	public void setAmountUnits(String amountUnits) {
        this.amountUnits = amountUnits;
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


