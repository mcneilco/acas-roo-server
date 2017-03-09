package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.hibernate.StaleObjectStateException;
import org.openscience.cdk.exception.CDKException;
import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.CodeTypeKindDTO;
import com.labsynch.labseer.dto.DependencyCheckDTO;
import com.labsynch.labseer.dto.LsThingValidationDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.StoichiometryPropertiesResultsDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
import com.labsynch.labseer.exceptions.LsThingValidationErrorMessage;
import com.labsynch.labseer.exceptions.NotFoundException;
import com.labsynch.labseer.exceptions.UniqueNameException;

@Service
public interface LsThingService {
	
	PreferredNameResultsDTO getCodeNameFromName(String thingType,
			String thingKind, String labelType, String labelKind, PreferredNameRequestDTO requestDTO);

	PreferredNameResultsDTO getGeneCodeNameFromName(String json);

	PreferredNameResultsDTO getPreferredNameFromName(String thingType,
			String thingKind, String labelType, String labelKind, String json);
	
	PreferredNameResultsDTO getPreferredNameFromName(String json);

	String getProjectCodes();

	PreferredNameResultsDTO getPreferredNameFromName(String thingType,
			String thingKind, String labelType, String labelKind,
			PreferredNameRequestDTO requestDTO);

	PreferredNameResultsDTO getPreferredNameFromName(
			PreferredNameRequestDTO requestDTO);

	LsThing saveLsThing(LsThing lsThing) throws UniqueNameException;

	LsThing updateLsThing(LsThing jsonLsThing) throws StaleObjectStateException;

	LsThing saveLsThing(LsThing lsThing, boolean checkLsThingName)
			throws UniqueNameException;

	Collection<LsThing> findBatchesByParentEquals(LsThing parent);

	LsThing saveLsThing(LsThing lsThing, boolean isParent, boolean isBatch,
			Long parentId)
			throws UniqueNameException;

	Collection<LsThing> findLsThingsByGenericMetaDataSearch(String searchQuery);

	Collection<LsThing> findLsThingsByGenericMetaDataSearch(String lsType,
			String searchQuery);

	Collection<CodeTableDTO> getCodeTableLsThings(String lsType, String lsKind, boolean includeIgnored);

	Collection<LsThing> findLsThingsByLsTypeAndLsKindAndIncludeIgnored(
			String lsType, String lsKind, boolean includeIgnored);

	LsThing findParentByBatchEquals(LsThing batch);

	Collection<LsThing> searchForDocumentThings(
			Map<String, String> searchParamsMap);

	Collection<LsThing> findCompositesByComponentEquals(LsThing component);

	ArrayList<LsThingValidationErrorMessage> validateLsThing(LsThingValidationDTO validationDTO);

	Collection<LsThing> sortLsThingsByCodeName(Collection<LsThing> lsThings);
	
	Collection<LsThing> sortBatches(Collection<LsThing> batches);

	PreferredNameResultsDTO getCodeNameFromName(String thingType,
			String thingKind, String labelType, String labelKind, String json);

	DependencyCheckDTO checkBatchDependencies(LsThing batch);

	DependencyCheckDTO checkParentDependencies(LsThing parent);

	boolean deleteBatch(LsThing batch);

	boolean deleteParent(LsThing parent);

	int getBatchNumber(LsThing parent);

	Collection<LsThing> findLsThingProjectsByGenericMetaDataSearch(
			String searchQuery, String userName);

	byte[] renderStructureByLsThingCodeName(String codeName, Integer height,
			Integer width, String format) throws IOException, CDKException, NotFoundException;

	StoichiometryPropertiesResultsDTO getStoichiometryProperties(
			Collection<CodeTypeKindDTO> requests);

	Collection<LsThing> structureSearch(String queryMol, String searchType,
			Integer maxResults, Float similarity);

	DependencyCheckDTO checkDependencies(LsThing lsThing);
	
	
}
