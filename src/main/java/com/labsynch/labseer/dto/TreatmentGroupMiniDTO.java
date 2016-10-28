package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.TreatmentGroup;

@RooJavaBean
@RooToString
@RooJson
public class TreatmentGroupMiniDTO {
	
    public TreatmentGroupMiniDTO(TreatmentGroup treatmentGroup) {
    	this.setId(treatmentGroup.getId());
    	this.setVersion(treatmentGroup.getVersion());
    	this.setCodeName(treatmentGroup.getCodeName());
    }

	private Long id;
    
	private Integer version;
	
	private String codeName;


}


