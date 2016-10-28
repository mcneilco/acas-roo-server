package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;

@RooJavaBean
@RooToString
@RooJson
public class TreatmentGroupValueDTO {
	
	
    public TreatmentGroupValueDTO(TreatmentGroupValue TreatmentGroupValue) {
    	this.setTreatmentGroupId(TreatmentGroupValue.getTreatmentGroupId());
    	
    	this.setLsState(TreatmentGroupValue.getLsState());
    	
    	this.setId(TreatmentGroupValue.getId());
    	this.setLsType(TreatmentGroupValue.getLsType());
    	this.setLsKind(TreatmentGroupValue.getLsKind());
    	this.setLsTypeAndKind(TreatmentGroupValue.getLsTypeAndKind());
    	
		this.setStringValue(TreatmentGroupValue.getStringValue());
		this.setCodeValue(TreatmentGroupValue.getCodeValue());
		this.setFileValue(TreatmentGroupValue.getFileValue());
		this.setUrlValue(TreatmentGroupValue.getUrlValue());
		this.setDateValue(TreatmentGroupValue.getDateValue());
		this.setClobValue(TreatmentGroupValue.getClobValue());
		this.setBlobValue(TreatmentGroupValue.getBlobValue());
		this.setNumericValue(TreatmentGroupValue.getNumericValue());
		
		this.setSigFigs(TreatmentGroupValue.getSigFigs());
		this.setUncertainty(TreatmentGroupValue.getUncertainty());
		this.setNumberOfReplicates(TreatmentGroupValue.getNumberOfReplicates());
		this.setUncertaintyType(TreatmentGroupValue.getUncertaintyType());
		
		this.setComments(TreatmentGroupValue.getComments());
		this.setIgnored(TreatmentGroupValue.isIgnored());
		this.setModifiedDate(TreatmentGroupValue.getModifiedDate());
		this.setRecordedDate(TreatmentGroupValue.getRecordedDate());
		this.setPublicData(TreatmentGroupValue.isPublicData());

		this.setCodeOrigin(TreatmentGroupValue.getCodeOrigin());
		this.setCodeType(TreatmentGroupValue.getCodeType());
		this.setCodeKind(TreatmentGroupValue.getCodeKind());
		this.setOperatorType(TreatmentGroupValue.getOperatorType());
		this.setOperatorKind(TreatmentGroupValue.getOperatorKind());
		this.setUnitType(TreatmentGroupValue.getUnitType());
		this.setUnitKind(TreatmentGroupValue.getUnitKind());

		this.setLsTransaction(TreatmentGroupValue.getLsTransaction());
    	this.setRecordedBy(TreatmentGroupValue.getRecordedBy());
		this.setModifiedBy(TreatmentGroupValue.getModifiedBy());
		this.setStateId(TreatmentGroupValue.getStateId());	
		this.setStateKind(TreatmentGroupValue.getStateKind());	
		this.setStateType(TreatmentGroupValue.getStateType());
    }
    
    private Long analysisGroupId;
    private Long treatmentGroupId;
	
    private TreatmentGroupState lsState;
	
	private Long id;
    private String lsType;
	private String lsKind;
	private String lsTypeAndKind;
	
	private String stringValue;
	private String codeValue;
	private String fileValue;
	private String urlValue;
	private Date dateValue;
	private String clobValue;
    private byte[] blobValue;
    private BigDecimal numericValue;
    
	private Integer sigFigs;
	private BigDecimal uncertainty;
	private Integer numberOfReplicates;
	private String uncertaintyType;
	
	private String comments;
	private boolean ignored;
	private Date recordedDate;
	private Date modifiedDate;
	private boolean publicData;
	
	private String codeOrigin;
	private String codeType;
	private String codeKind;
    private String operatorType;
	private String operatorKind;
    private String unitType;
	private String unitKind;
	private Long lsTransaction;
	private String recordedBy;
	private String modifiedBy;
    private Long stateId;
	private String stateType;
	private String stateKind;
	
	private BigDecimal concentration;
	private String concUnit;
	
	public boolean getIgnored() {
		return this.isIgnored();
	}
	
	public boolean getPublicData() {
		return this.isPublicData();
	}

}

