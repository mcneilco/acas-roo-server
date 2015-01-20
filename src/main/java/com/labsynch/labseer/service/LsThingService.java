package com.labsynch.labseer.service;

import java.util.List;
import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.exceptions.UniqueNameException;

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

	boolean validateComponentName(String componentName);

	boolean validateAssembly(List<String> componentCodeNames);

	LsThing saveLsThing(LsThing lsThing) throws UniqueNameException;

	LsThing updateLsThing(LsThing jsonLsThing);

	boolean validateComponentName(LsThing lsThing);

	LsThing saveLsThing(LsThing lsThing, boolean checkLsThingName)
			throws UniqueNameException;

	boolean validateAssembly(LsThing assembly);

	String generateBatchCodeName(LsThing parent);

	Collection<LsThing> findBatchesByParentEquals(LsThing parent);

	LsThing saveLsThing(LsThing lsThing, boolean isParent, boolean isBatch,
			boolean isAssembly, boolean isComponent, Long parentId)
			throws UniqueNameException;

	Collection<LsThing> findLsThingsByGenericMetaDataSearch(String searchQuery);

	Collection<LsThing> findLsThingsByGenericMetaDataSearch(String lsType,
			String searchQuery);

	List<String> getComponentCodeNamesFromNewAssembly(LsThing lsThing);
	
	
	
}
