package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Collection;
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
	
	public WellContentDTO(){
	}
	
	public WellContentDTO(String containerCodeName, 
			BigDecimal amount,
			String amountUnits,
			String batchCode,
			Double batchConcentration,
			String batchConcUnits,
			String solventCode,
			String physicalState){
		this.containerCodeName = containerCodeName; 
		this.amount = amount;
		this.amountUnits = amountUnits;
		this.batchCode = batchCode;
		this.batchConcentration = batchConcentration;
		this.batchConcUnits = batchConcUnits;
		this.solventCode = solventCode;
		this.physicalState = physicalState;
	}
	
	
	

}


