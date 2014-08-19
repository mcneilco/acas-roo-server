package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;

@RooJavaBean
@RooToString
@RooJson
public class SubjectValueDTO {
	
	
    public SubjectValueDTO(SubjectValue subjectvalue) {
//    	this.setId(subjectvalue.getId());
//		this.setRecordedBy(subjectvalue.getRecordedBy());
//		this.setRecordedDate(subjectvalue.getRecordedDate());
//		this.setLsTransaction_Id(subjectvalue.getLsTransaction().getId());
//		this.setModifiedBy(subjectvalue.getModifiedBy());
//		this.setModifiedDate(subjectvalue.getModifiedDate());
//		this.setIgnored(subjectvalue.isIgnored());
//		this.setStateKind(subjectvalue.getStateKind());	
//		this.setStateType(subjectvalue.getStateType());	
//		this.setStateTypeAndKind(subjectvalue.getStateTypeAndKind());
//		this.setComments(subjectvalue.getComments());
    }
    
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


}

