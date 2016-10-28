package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class AnalysisGroupValueBaseDTO {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupValueBaseDTO.class);

	public AnalysisGroupValueBaseDTO(){
		//empty constructor
	}

	public AnalysisGroupValueBaseDTO(
			Long id, 
			Long stateId,
			String agCodeName,
			String lsType,
			String lsKind,
			 String stringValue,
			 String codeValue,
			 String fileValue,
			 String urlValue,
			 Date dateValue,
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
		this.stateId = stateId;
		this.agCodeName = agCodeName;
		this.lsType = lsType;
		this.lsKind = lsKind;
		this.numericValue = numericValue;
		this.stringValue = stringValue;
		this.codeValue = codeValue;		
		this.fileValue = fileValue;
		this.urlValue = urlValue;
		this.dateValue = dateValue;
	    this.clobValue = clobValue;
		this.operatorType = operatorType;
		this.operatorKind = operatorKind;
		this.sigFigs = sigFigs;
		this.uncertainty = uncertainty;
		this.numberOfReplicates = numberOfReplicates;
		this.uncertaintyType = uncertaintyType;
		this.unitType = unitType;
		this.unitKind = unitKind;
		this.comments = unitKind;
		this.ignored = ignored;
		this.lsTransaction = lsTransaction;
		this.publicData = publicData;	
		}
	
	private Long id;
	private Long stateId;
	private String agCodeName;
	private String lsType;
	private String lsKind;
	private String stringValue;
	protected String codeValue;
	private String fileValue;
	private String urlValue;
	private Date dateValue;
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


