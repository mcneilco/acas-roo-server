package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroupState;

@RooJavaBean
@RooToString
@RooJson
public class SubjectValueDTO {
	
	
    public SubjectValueDTO(SubjectValue subjectValue) {
    	this.setSubjectId(subjectValue.getSubjectId());
    	
    	this.setLsState(subjectValue.getLsState());
    	
    	this.setId(subjectValue.getId());
    	this.setLsType(subjectValue.getLsType());
    	this.setLsKind(subjectValue.getLsKind());
    	this.setLsTypeAndKind(subjectValue.getLsTypeAndKind());
    	
		this.setStringValue(subjectValue.getStringValue());
		this.setCodeValue(subjectValue.getCodeValue());
		this.setFileValue(subjectValue.getFileValue());
		this.setUrlValue(subjectValue.getUrlValue());
		this.setDateValue(subjectValue.getDateValue());
		this.setClobValue(subjectValue.getClobValue());
		this.setBlobValue(subjectValue.getBlobValue());
		this.setNumericValue(subjectValue.getNumericValue());
		
		this.setSigFigs(subjectValue.getSigFigs());
		this.setUncertainty(subjectValue.getUncertainty());
		this.setNumberOfReplicates(subjectValue.getNumberOfReplicates());
		this.setUncertaintyType(subjectValue.getUncertaintyType());
		
		this.setComments(subjectValue.getComments());
		this.setIgnored(subjectValue.isIgnored());
		this.setModifiedDate(subjectValue.getModifiedDate());
		this.setRecordedDate(subjectValue.getRecordedDate());
		this.setPublicData(subjectValue.isPublicData());

		this.setCodeOrigin(subjectValue.getCodeOrigin());
		this.setCodeType(subjectValue.getCodeType());
		this.setCodeKind(subjectValue.getCodeKind());
		this.setOperatorType(subjectValue.getOperatorType());
		this.setOperatorKind(subjectValue.getOperatorKind());
		this.setUnitType(subjectValue.getUnitType());
		this.setUnitKind(subjectValue.getUnitKind());

		this.setLsTransaction(subjectValue.getLsTransaction());
    	this.setRecordedBy(subjectValue.getRecordedBy());
		this.setModifiedBy(subjectValue.getModifiedBy());
		this.setStateId(subjectValue.getStateId());	
		this.setStateKind(subjectValue.getStateKind());	
		this.setStateType(subjectValue.getStateType());
    }
    
    private Long treatmentGroupId;
    private Long subjectId;
	
    private SubjectState lsState;
	
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

