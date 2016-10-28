package com.labsynch.labseer.dto;

import java.util.Set;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.LsThing;


@RooJavaBean
@RooToString
@RooJson
public class LsThingValidationDTO {
	
    public LsThingValidationDTO() {

    }

    
	private LsThing lsThing;
	
	private boolean uniqueName;
	
	private boolean uniqueInteractions;
	
	private boolean orderMatters;
	
	private boolean forwardAndReverseAreSame;
	
	private Set<ValueRuleDTO> valueRules;
}


