package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.DateValueComparisonRequest;
import com.labsynch.labseer.dto.ExperimentDataDTO;
import com.labsynch.labseer.dto.ExperimentErrorMessageDTO;
import com.labsynch.labseer.dto.ExperimentFilterDTO;
import com.labsynch.labseer.dto.ExperimentSearchRequestDTO;
import com.labsynch.labseer.dto.JSTreeNodeDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.StringCollectionDTO;
import com.labsynch.labseer.exceptions.NotFoundException;
import com.labsynch.labseer.exceptions.TooManyResultsException;
import com.labsynch.labseer.exceptions.UniqueNameException;

@Service
public interface ExperimentService {

	public Experiment saveLsExperiment(Experiment experiment) throws UniqueNameException, NotFoundException;

	public void deleteLsExperiment(Experiment experiment);

	public Experiment updateExperiment(Experiment experiment) throws UniqueNameException;

	public Experiment getFullExperiment(Experiment queryExperiment);

	Collection<JSTreeNodeDTO> getExperimentNodes(Collection<String> codeValues);

	public List<AnalysisGroupValueDTO> getFilteredAGData(ExperimentSearchRequestDTO searchRequest, Boolean onlyPublicData);

	public Collection<ExperimentFilterDTO> getExperimentFilters(Collection<String> experimentCodes);

	public boolean isSoftDeleted(Experiment experiment);
	
	public Collection<JSTreeNodeDTO> getExperimentNodesByProtocolTree(
			Collection<String> codeValues);

	public Collection<Experiment> findExperimentsByMetadataJson(String json);
	
	public Collection<Experiment> findExperimentsByMetadataJson(
			List<StringCollectionDTO> metaDataList);
	
	public Collection<Experiment> findExperimentsByGenericMetaDataSearch(String query, String userName) throws TooManyResultsException;

	public Collection<Experiment> findExperimentsByGenericMetaDataSearch(String query)  throws TooManyResultsException;
	
	public Collection<Experiment> findExperimentsByMetadata(String queryString, String searchBy);

	public Set<Experiment> findExperimentsByRequestMetadata(
			Map<String, String> requestParams);

	Collection<JSTreeNodeDTO> getExperimentNodesMod(
			Collection<String> codeValues);

	public boolean deleteAnalysisGroupsByExperiment(Experiment experiment);

	public Collection<ExperimentErrorMessageDTO> findExperimentsByCodeNames(List<String> codeNames);

	public Collection<Experiment> saveLsExperiments(
			Collection<Experiment> experiments) throws UniqueNameException, NotFoundException;

	public List<CodeTableDTO> getExperimentsAsCodeTables(String lsType,
			String lsKind);

	List<CodeTableDTO> convertExperimentsToCodeTables(
			Collection<Experiment> experiments);

	public PreferredNameResultsDTO getCodeNameFromName(String experimentType,
			String experimentKind, String labelType, String labelKind,
			PreferredNameRequestDTO requestDTO);
	public List<ExperimentDataDTO> getExperimentData(String batchCode, boolean showOnlyPublicData);
	
	public Collection<String> getExperimentCodesByDateValueComparison(
			DateValueComparisonRequest requestDTO) throws Exception;

	
}
