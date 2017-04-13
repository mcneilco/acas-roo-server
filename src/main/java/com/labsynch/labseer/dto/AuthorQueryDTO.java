package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class AuthorQueryDTO {

	Date recordedDateGreaterThan;
	
	Date recordedDateLessThan;
	
	String recordedBy;
	
	Integer maxResults;
	
	String lsType;
	
	String lsKind;
	
	String firstName;
	
	String lastName;
	
	String userName;
		
	Collection<ValueQueryDTO> values;
	
	Collection<LabelQueryDTO> labels;
	
	public AuthorQueryDTO(){
		
	}
	
	public AuthorQueryDTO(AuthorQueryDTO queryDTO){
		this.recordedDateGreaterThan = queryDTO.getRecordedDateGreaterThan();
		this.recordedDateLessThan = queryDTO.getRecordedDateLessThan();
		this.lsType = queryDTO.getLsType();
		this.lsKind = queryDTO.getLsKind();
		this.firstName = queryDTO.getFirstName();
		this.lastName = queryDTO.getLastName();
		this.userName = queryDTO.getUserName();
		this.recordedBy = queryDTO.getRecordedBy();
		this.maxResults = queryDTO.getMaxResults();
		this.values = queryDTO.getValues();
		this.labels = queryDTO.getLabels();
	}
}
