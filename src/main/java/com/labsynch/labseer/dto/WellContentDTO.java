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
	
	private String wellCodeName;
	private BigDecimal grossMass;
	private String grossMassUnits;
	private BigDecimal netMass;
	private String netMassUnits;
	private String batchCode;
	private Double batchConcentration;
	private String batchConcUnits;
	private String solventCode;
	private String physicalState;
	private BigDecimal volume;
	private String volumeUnits; 
	
	public WellContentDTO(){
	}
	
	public WellContentDTO(String wellCodeName, 
			BigDecimal grossMass,
			String grossMassUnits,
			BigDecimal netMass,
			String netMassUnits,
			String batchCode,
			Double batchConcentration,
			String batchConcUnits,
			String solventCode,
			String physicalState,
			BigDecimal volume,
			String volumeUnits){
		this.wellCodeName = wellCodeName; 
		this.grossMass = grossMass;
		this.grossMassUnits = grossMassUnits;
		this.netMass = netMass;
		this.netMassUnits = netMassUnits;
		this.batchCode = batchCode;
		this.batchConcentration = batchConcentration;
		this.batchConcUnits = batchConcUnits;
		this.solventCode = solventCode;
		this.physicalState = physicalState;
		this.volume = volume;
		this.volumeUnits = volumeUnits;
	}
	
	
	

}


