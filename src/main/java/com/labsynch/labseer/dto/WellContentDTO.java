package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
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
		this.amount = amount;
		this.amountUnits = amountUnits;
		this.batchCode = batchCode;
		this.batchConcentration = batchConcentration;
		this.batchConcUnits = batchConcUnits;
		this.solventCode = solventCode;
		this.physicalState = physicalState;
	}
	
	
	

}


