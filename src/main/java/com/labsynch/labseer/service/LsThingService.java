package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.CodeTypeKindDTO;
import com.labsynch.labseer.dto.DateValueComparisonRequest;
import com.labsynch.labseer.dto.DependencyCheckDTO;
import com.labsynch.labseer.dto.LsThingBrowserQueryDTO;
import com.labsynch.labseer.dto.LsThingQueryDTO;
import com.labsynch.labseer.dto.LsThingValidationDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.StoichiometryPropertiesResultsDTO;
import com.labsynch.labseer.exceptions.LsThingValidationErrorMessage;
import com.labsynch.labseer.exceptions.NotFoundException;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.service.ChemStructureService.SearchType;

import org.hibernate.StaleObjectStateException;
import org.openscience.cdk.exception.CDKException;
import org.springframework.stereotype.Service;

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

	Collection<LsThing> findLsThingsByGenericMetaDataSearch(String searchQuery,
			String lsType);

	Collection<LsThing> findLsThingsByGenericMetaDataSearch(String searchQuery,
			String lsType,
			String lsKind
	);

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

	Collection<LsThing> structureSearch(String queryMol, SearchType searchType,
			Integer maxResults, Float similarity);

	DependencyCheckDTO checkDependencies(LsThing lsThing);
	
	Collection<Long> searchLsThingIdsByQueryDTO(LsThingQueryDTO query) throws Exception;

	Collection<LsThing> getLsThingsByIds(Collection<Long> lsThingIds);

	Collection<CodeTableDTO> convertToCodeTables(Collection<LsThing> lsThings);

	Collection<CodeTableDTO> convertToCodeTables(Collection<LsThing> lsThings, String labelType);

	Collection<Long> searchLsThingIdsByBrowserQueryDTO(LsThingBrowserQueryDTO query) throws Exception;

	Collection<Long> searchLsThingIdsByQueryDTOandStructure(LsThingQueryDTO query, String queryMol, SearchType searchType,
			Integer maxResults, Float similarity) throws Exception;

	Collection<LsThing> structureSearch(String queryMol, String lsType, String lsKind, SearchType searchType,
			Integer maxResults, Float similarity);
	
	Collection<String> getLsThingCodesByDateValueComparison(
			DateValueComparisonRequest requestDTO) throws Exception;
	
	
}
