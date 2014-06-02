package com.labsynch.labseer.dto;

import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

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
//		this.setSubject(new SubjectMiniDTO(subjectState.getSubject()));
    }
    
	private SubjectStateMiniDTO subjectState;
    
	private String valueType;

	private String valueKind;

	private String valueTypeAndKind;

	private String stringValue;

	private String codeValue;
	
	private String fileValue;

	private String urlValue;

	private Date dateValue;

    private String clobValue;
    
    private byte[] blobValue;

    private String valueOperator;

	private Float numericValue;

	private Integer sigFigs;

	private Float uncertainty;
	
	private Integer numberOfReplicates;
	
	private String uncertaintyType;

	private String valueUnit;

	private String comments;

	private boolean ignored;
	
	private Long lsTransaction_Id;

	private Date recordedDate;

	private Date modifiedDate;

	private boolean publicData;

	private Long id;


}

