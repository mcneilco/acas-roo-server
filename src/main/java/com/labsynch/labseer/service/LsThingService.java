package com.labsynch.labseer.service;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;

@Service
public interface LsThingService {

	PreferredNameResultsDTO getCodeNameFromName(String thingType,
			String thingKind, String labelType, String labelKind, String json);
	
	PreferredNameResultsDTO getCodeNameFromName(String thingType,
			String thingKind, String labelType, String labelKind, PreferredNameRequestDTO requestDTO);

	PreferredNameResultsDTO getGeneCodeNameFromName(String json);

	PreferredNameResultsDTO getPreferredNameFromName(String thingType,
			String thingKind, String labelType, String labelKind, String json);

	String getProjectCodes();

	PreferredNameResultsDTO getPreferredNameFromName(String thingType,
			String thingKind, String labelType, String labelKind,
			PreferredNameRequestDTO requestDTO);
	
	
	
}
