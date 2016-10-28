package com.labsynch.labseer.dto;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class DoseReponseParamsDTO {

	private static final Logger logger = LoggerFactory.getLogger(DoseReponseParamsDTO.class);

	public DoseReponseParamsDTO(){
		//empty constructor
	}

	public DoseReponseParamsDTO(
			Long id, 
			Integer version,
			String agCodeName,
			String curveId,
			String lsType,
			String lsKind,
			String stringValue,
			String codeValue,
			String clobValue,
			String operatorType,
			String operatorKind,
			BigDecimal numericValue,
			Integer sigFigs,
			BigDecimal uncertainty,
			Integer numberOfReplicates,
			String uncertaintyType,
			String unitType,
			String unitKind,
			String comments,
			boolean ignored,
			Long lsTransaction,
			boolean publicData
			){

		this.id = id;
		this.lsType = lsType;
		this.lsKind = lsKind;
		this.numericValue = numericValue;
		this.stringValue = stringValue;
		this.codeValue = codeValue;
		this.clobValue = clobValue;

		//		private String operatorType;
		//		private String operatorKind;
		//		private Integer sigFigs;
		//		private BigDecimal uncertainty;
		//		private Integer numberOfReplicates;
		//		private String uncertaintyType;
		//		private String unitType;
		//		private String unitKind;
		//		private String comments;
		//		private boolean ignored;
		//		private Long lsTransaction;
		//		private boolean publicData;	
	}

	private Long id;
	private Integer version;
	private Long Stateid;
	private String agCodeName;
	private String curveId;
	private String lsType;
	private String lsKind;
	private String stringValue;
	private String codeValue;
	private String clobValue;
	private String operatorType;
	private String operatorKind;
	private BigDecimal numericValue;
	private Integer sigFigs;
	private BigDecimal uncertainty;
	private Integer numberOfReplicates;
	private String uncertaintyType;
	private String unitType;
	private String unitKind;
	private String comments;
	private boolean ignored;
	private Long lsTransaction;
	private boolean publicData;

}


