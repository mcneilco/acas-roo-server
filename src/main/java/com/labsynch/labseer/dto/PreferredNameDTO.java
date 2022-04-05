package com.labsynch.labseer.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import javax.persistence.NoResultException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.PhysicalState;

@RooJavaBean
@RooToString
@RooJson
public class PreferredNameDTO {
	
	public PreferredNameDTO() {		
	}
	
	public PreferredNameDTO(
			String requestName,
			String preferredName,
			String referenceName
			) {
		
		this.requestName = requestName;
		this.preferredName = preferredName;
		this.referenceName = referenceName;
	}

	private String requestName;

	private String preferredName;

	private String referenceName;
		
	@Transactional
    public static Collection<PreferredNameDTO> getPreferredNames(Collection<PreferredNameDTO> preferredNameDTOs){
    	for (PreferredNameDTO preferredNameDTO : preferredNameDTOs){
    		String preferredName;
    		try {
    			preferredName = Lot.findLotsByCorpNameEquals(preferredNameDTO.getRequestName()).getSingleResult().getCorpName();
    		} catch (NoResultException e){
    			preferredName = "";
    		}
    		preferredNameDTO.setPreferredName(preferredName);
    	}
    	return preferredNameDTOs;
    }

	public static Collection<PreferredNameDTO> getParentPreferredNames(Collection<PreferredNameDTO> preferredNameDTOs) {
		for (PreferredNameDTO preferredNameDTO : preferredNameDTOs){
    		String preferredName;
    		try {
    			preferredName = Parent.findParentsByCorpNameEquals(preferredNameDTO.getRequestName()).getSingleResult().getCorpName();
    		} catch (NoResultException e){
    			preferredName = "";
    		}
    		preferredNameDTO.setPreferredName(preferredName);
    	}
    	return preferredNameDTOs;
	}
  
}




