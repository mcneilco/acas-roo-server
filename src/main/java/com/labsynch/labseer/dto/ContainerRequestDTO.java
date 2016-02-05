package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
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
	

}


