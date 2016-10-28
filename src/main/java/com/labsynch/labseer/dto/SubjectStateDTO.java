package com.labsynch.labseer.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;

@RooJavaBean
@RooToString
@RooJson
public class SubjectStateDTO {
	
    public SubjectStateDTO(SubjectState subjectState) {
    	this.setId(subjectState.getId());
		this.setRecordedBy(subjectState.getRecordedBy());
		this.setRecordedDate(subjectState.getRecordedDate());
		this.setLsTransaction_Id(subjectState.getLsTransaction());
		this.setModifiedBy(subjectState.getModifiedBy());
		this.setModifiedDate(subjectState.getModifiedDate());
		this.setIgnored(subjectState.isIgnored());
		this.setLsKind(subjectState.getLsKind());	
		this.setLsType(subjectState.getLsType());	
		this.setLsTypeAndKind(subjectState.getLsTypeAndKind());
		this.setComments(subjectState.getComments());
		this.setSubject(new SubjectMiniDTO(subjectState.getSubject()));
    }

	private Long id;
    
	private SubjectMiniDTO subject;
	
	private String recordedBy;

	private Date recordedDate;

	private String modifiedBy;

	private Date modifiedDate;

	private String lsType;

	private String lsKind;

	private String lsTypeAndKind;

	private String comments;

	private boolean ignored;

	private Long lsTransaction_Id;

    private Set<SubjectValue> subjectValues = new HashSet<SubjectValue>();


}

