package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class TreatmentGroupCodeDTO {
	
	private String treatmentGroupCode;
	private Collection<AnalysisGroupCodeDTO> analysisGroupCodes;
	
	public TreatmentGroupCodeDTO(){
	}

}


