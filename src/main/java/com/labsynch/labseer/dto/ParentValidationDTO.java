package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.service.ErrorMessage;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ParentValidationDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(ParentValidationDTO.class);

	private Collection<CodeTableDTO> affectedLots;
	
	private boolean parentUnique;
	
	private Collection<ParentDTO> dupeParents;
	
	private Collection<ErrorMessage> errors; 
		
	public ParentValidationDTO() {
		this.errors = new ArrayList<ErrorMessage>();
	}
    
    public String toJson() {
        return new JSONSerializer()
        .include("affectedLots", "dupeParents","errors")
        .exclude("*.class").serialize(this);
    }
    
}
