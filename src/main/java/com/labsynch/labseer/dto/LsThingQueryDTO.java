package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class LsThingQueryDTO {

	Boolean combineTermsWithAnd = false;

	Date recordedDateGreaterThan;
	
	Date recordedDateLessThan;
	
	String recordedBy;
	
	Integer maxResults;
	
	String lsType;
	
	String lsKind;
	
	Collection<ItxQueryDTO> firstInteractions;
	
	Collection<ItxQueryDTO> secondInteractions;
	
	Collection<ValueQueryDTO> values;
	
	Collection<LabelQueryDTO> labels;
	
	CodeNameQueryDTO codeName;
	
	public LsThingQueryDTO(){
		
	}
	
	public LsThingQueryDTO(LsThingQueryDTO queryDTO){
		if(queryDTO.getCombineTermsWithAnd() != null) this.combineTermsWithAnd = queryDTO.getCombineTermsWithAnd();
		this.recordedDateGreaterThan = queryDTO.getRecordedDateGreaterThan();
		this.recordedDateLessThan = queryDTO.getRecordedDateLessThan();
		this.lsType = queryDTO.getLsType();
		this.lsKind = queryDTO.getLsKind();
		this.recordedBy = queryDTO.getRecordedBy();
		this.maxResults = queryDTO.getMaxResults();
		this.firstInteractions = queryDTO.getFirstInteractions();
		this.secondInteractions = queryDTO.getSecondInteractions();
		this.values = queryDTO.getValues();
		this.labels = queryDTO.getLabels();
		this.codeName = queryDTO.getCodeName();
	}
}
