package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class WellContentDTO {
	
	private String containerCodeName;
	private BigDecimal amount;
	private String amountUnits;
	private String batchCode;
	private Double batchConcentration;
	private String batchConcUnits;
	private String solventCode;
	private String physicalState;
	private String level;
	private String message;
	
	private String wellName;
	private Integer rowIndex;
	private Integer columnIndex;
	private String recordedBy;
	private Date recordedDate;
	private Date lastModifiedDate;
	
	public WellContentDTO(){
	}
	
	public WellContentDTO(String containerCodeName,
			String wellName,
			Integer rowIndex,
			Integer columnIndex,
			String recordedBy,
			Date recordedDate
			){
		this.containerCodeName = containerCodeName; 
		this.wellName = wellName;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		this.recordedBy = recordedBy;
		this.recordedDate = recordedDate;
	}
	
	public WellContentDTO(String containerCodeName,
			String wellName,
			Integer rowIndex,
			Integer columnIndex,
			String recordedBy,
			Date recordedDate,
			Date lastModifiedDate,
			BigDecimal amount,
			String amountUnits,
			String batchCode,
			Double batchConcentration,
			String batchConcUnits,
			String solventCode,
			String physicalState){
		this.containerCodeName = containerCodeName; 
		this.wellName = wellName;
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
		this.recordedBy = recordedBy;
		this.recordedDate = recordedDate;
		this.lastModifiedDate = lastModifiedDate;
		this.amount = amount;
		this.amountUnits = amountUnits;
		this.batchCode = batchCode;
		this.batchConcentration = batchConcentration;
		this.batchConcUnits = batchConcUnits;
		this.solventCode = solventCode;
		this.physicalState = physicalState;
	}
	
	
	


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static WellContentDTO fromJsonToWellContentDTO(String json) {
        return new JSONDeserializer<WellContentDTO>()
        .use(null, WellContentDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<WellContentDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<WellContentDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<WellContentDTO> fromJsonArrayToWellCoes(String json) {
        return new JSONDeserializer<List<WellContentDTO>>()
        .use("values", WellContentDTO.class).deserialize(json);
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

	public String getBatchCode() {
        return this.batchCode;
    }

	public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

	public Double getBatchConcentration() {
        return this.batchConcentration;
    }

	public void setBatchConcentration(Double batchConcentration) {
        this.batchConcentration = batchConcentration;
    }

	public String getBatchConcUnits() {
        return this.batchConcUnits;
    }

	public void setBatchConcUnits(String batchConcUnits) {
        this.batchConcUnits = batchConcUnits;
    }

	public String getSolventCode() {
        return this.solventCode;
    }

	public void setSolventCode(String solventCode) {
        this.solventCode = solventCode;
    }

	public String getPhysicalState() {
        return this.physicalState;
    }

	public void setPhysicalState(String physicalState) {
        this.physicalState = physicalState;
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

	public String getWellName() {
        return this.wellName;
    }

	public void setWellName(String wellName) {
        this.wellName = wellName;
    }

	public Integer getRowIndex() {
        return this.rowIndex;
    }

	public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

	public Integer getColumnIndex() {
        return this.columnIndex;
    }

	public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public Date getRecordedDate() {
        return this.recordedDate;
    }

	public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

	public Date getLastModifiedDate() {
        return this.lastModifiedDate;
    }

	public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}


